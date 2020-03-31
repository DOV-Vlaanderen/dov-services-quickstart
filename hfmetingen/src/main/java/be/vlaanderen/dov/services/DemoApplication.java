package be.vlaanderen.dov.services;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import be.vlaanderen.dov.services.config.ClientConfig;
import be.vlaanderen.dov.services.config.ClientProperties;
import be.vlaanderen.dov.services.config.DownloadType;
import be.vlaanderen.dov.services.hfmetingen.dto.Code;
import be.vlaanderen.dov.services.hfmetingen.dto.DeleteResponse;
import be.vlaanderen.dov.services.hfmetingen.dto.FilterMetingen;
import be.vlaanderen.dov.services.hfmetingen.dto.SensorMetingen;
import be.vlaanderen.dov.services.hfmetingen.dto.UploadRequest;
import be.vlaanderen.dov.services.hfmetingen.dto.UploadResponse;
import be.vlaanderen.dov.services.hfmetingen.example.DeleteSensorMeetpunten;
import be.vlaanderen.dov.services.hfmetingen.example.DownloadFilterdataAsFile;
import be.vlaanderen.dov.services.hfmetingen.example.DownloadFilterdataAsJson;
import be.vlaanderen.dov.services.hfmetingen.example.DownloadSensordataAsFile;
import be.vlaanderen.dov.services.hfmetingen.example.DownloadSensordataAsJson;
import be.vlaanderen.dov.services.hfmetingen.example.DownloadSensoren;
import be.vlaanderen.dov.services.hfmetingen.example.HealthCheck;
import be.vlaanderen.dov.services.hfmetingen.example.UploadSensorCsv;
import be.vlaanderen.dov.services.hfmetingen.example.UploadSensorMeetpunt;
import be.vlaanderen.dov.services.hfmetingen.example.UploadSensorMeetpunten;
import be.vlaanderen.dov.services.hfmetingen.example.UploadStatusCheck;

/**
 * Demo applicatie die aan de hand van de certificaten in een keystore beveiligde request naar de dov-services doet om
 * een xml op te laden, te valdieren, aan te leveren en op te volgen.
 * <p>
 * Meer informatie over de service
 * https://www.milieuinfo.be/confluence/display/DDOV/Case+1%3A+Opladen+van+XML-bestanden+voor+boorbedrijven
 *
 */
@SpringBootApplication(scanBasePackages = { "be.vlaanderen.dov.services.hfmetingen" })
@EnableAutoConfiguration
@EnableConfigurationProperties(ClientProperties.class)
public class DemoApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger("main");

    @Bean
    public ClientConfig clientConfig() {
        return new ClientConfig();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        boolean doUploads = false;

        // *********************************************************************************************//
        // Demo 1: the first call - a healthcheck
        // should always work if the certificates are valid and configured correctly.
        // *********************************************************************************************//
        new HealthCheck().execute(clientConfig());

        // *********************************************************************************************//
        // Demo 2: How to fetch the list of sensors of an instrument
        // using the permkey of the instrument.
        // the result is a list of sensors (id and name) of that instrument.
        // Use one of those sensors to make the next calls
        // *********************************************************************************************//

        String instrumentPermKey = "1";
        List<Code> sensoren = new DownloadSensoren().fetch(clientConfig(), instrumentPermKey);
        sensoren.stream().forEach(i -> LOG.info("sensor code '{}': {} ", i.getCode(), i.getBeschrijving()));

        // *********************************************************************************************//
        // Demo 3: How to upload sensordata.
        // You can upload sensordata to an instrument. there are various ways
        // - uploading one point (in code): the request is handled instantly
        // - uploading multiple points (in code): the request is handled asynchronously
        // - uploading a csv file: the request is handled asynchronously
        // the 3 ways are demonstrated here
        //
        // in all cases you use the permkey of the instrument and the id of the sensor.
        // *********************************************************************************************//

        String sensorId = "4";
        if (doUploads) {

            // *********************************************************************************************//
            // uploading one point: the result is a response with the immediate result
            // *********************************************************************************************//
            UploadResponse response = new UploadSensorMeetpunt().upload(clientConfig(), instrumentPermKey, sensorId);
            if (response == null) {
                LOG.error("upload one point failed");
            } else {
                LOG.info("status {}: {} meetpunt(en) opgeladen", response.getStatus(), response.getAantal());
            }

            // *********************************************************************************************//
            // uploading multiple points
            // submitting multiple points are queued; in that case we receive an id which permits us to track the
            // progress of the upload.
            // *********************************************************************************************//
            response = new UploadSensorMeetpunten(10000).upload(clientConfig(), instrumentPermKey, sensorId);
            String importId = null;
            if (response == null) {
                LOG.error("upload multiple points failed");
            } else {
                importId = response.getImportLogId();
                LOG.info("status multiple {}: importlog = {} ", response.getStatus(), response.getImportLogId());
            }

            // *********************************************************************************************//
            // tracking the submit of multiple points
            // *********************************************************************************************//
            if (importId != null) {
                UploadStatusCheck check = new UploadStatusCheck();
                UploadRequest resultStatus = check.statusSummary(clientConfig(), importId);

                while (check != null && !resultStatus.getStatus().isFinalState()) {
                    TimeUnit.SECONDS.sleep(30);
                    resultStatus = check.statusSummary(clientConfig(), importId);
                    LOG.info("Status multiple: {}", resultStatus.getStatus().name());
                }
            }

            // *********************************************************************************************//
            // uploading a csv file with one or more points (here 10.000)
            // submitting a csv is always queued, so here we also receive an id to track the upload.
            // *********************************************************************************************//
            response = new UploadSensorCsv(10000).upload(clientConfig(), instrumentPermKey, sensorId);
            if (response == null) {
                LOG.error("upload CSV points failed");
            } else {
                importId = response.getImportLogId();
                LOG.info("status CSV {}: importlog = {} ", response.getStatus(), response.getImportLogId());
                UploadStatusCheck check = new UploadStatusCheck();
                UploadRequest resultStatus = check.statusSummary(clientConfig(), importId);
                while (check != null && !resultStatus.getStatus().isFinalState()) {
                    TimeUnit.SECONDS.sleep(30);
                    resultStatus = check.statusSummary(clientConfig(), importId);
                    LOG.info("Importstatus CSV: {}", resultStatus.getStatus().name());
                }
            }
        }

        // *********************************************************************************************//
        // Demo 4: How to delete sensordata.
        // You can delete a range of sensordata.
        // *********************************************************************************************//
        DeleteSensorMeetpunten sensorDelete = new DeleteSensorMeetpunten();
        OffsetDateTime today = OffsetDateTime.now();
        DeleteResponse deleteResponse = sensorDelete.delete(clientConfig(), instrumentPermKey, sensorId,
                today.plusSeconds(2000), today.plusSeconds(2100), null);
        LOG.info("Number of points deleted = {}", deleteResponse.getAantal());

        // *********************************************************************************************//
        // Demo 5: How to download the sensordata
        // *********************************************************************************************//
        DownloadSensordataAsJson download = new DownloadSensordataAsJson();
        SensorMetingen data = download.downloadQuery(clientConfig(), instrumentPermKey, sensorId,
                OffsetDateTime.now().plusDays(5L), null, null);
        LOG.info("Number of points is {}", data.getMeetdata().size());

        data = download.downloadQuery(clientConfig(), instrumentPermKey, sensorId, OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(6L), null);
        LOG.info("Number of points is {}", data.getMeetdata().size());

        // download directly as csv
        DownloadSensordataAsFile downloadCsv = new DownloadSensordataAsFile(DownloadType.CSV);
        downloadCsv.downloadQuery(clientConfig(), instrumentPermKey, sensorId, OffsetDateTime.now(), null, null);

        // download directly as zip
        DownloadSensordataAsFile downloadZip = new DownloadSensordataAsFile(DownloadType.ZIP);
        downloadZip.downloadQuery(clientConfig(), instrumentPermKey, sensorId, OffsetDateTime.now(), null, null);

        // *********************************************************************************************//
        // Demo 6: How to download filterdata
        // In a filter, multiple instruments can be used in sequence to take the measurements.
        // So here we can collect data from different instruments and sensors.
        //
        // what is needed: the permkey of the filter and the type of sensor.
        // *********************************************************************************************//

        String filterPermKey = "2015-000881";
        // *********************************************************************************************//
        // fetch all sensortypes for your filter of choice
        // *********************************************************************************************//
        DownloadFilterdataAsJson filterDownload = new DownloadFilterdataAsJson();
        List<Code> sensortypes = filterDownload.getSensortypes(clientConfig(), filterPermKey);
        sensortypes.stream().forEach(i -> LOG.info("sensor code '{}': {} ", i.getCode(), i.getBeschrijving()));

        // *********************************************************************************************//
        // we pick one from the list above
        // *********************************************************************************************//
        String sensortype = "4017";

        // *********************************************************************************************//
        // demonstration how to get the data.
        // *********************************************************************************************//
        FilterMetingen filterData = filterDownload.downloadQuery(clientConfig(), filterPermKey, sensortype,
                OffsetDateTime.now(), OffsetDateTime.now().plusDays(5L), null);
        LOG.info("Number of points is {}", filterData.getMeetdata().size());

        // download as csv
        DownloadFilterdataAsFile filterCsv = new DownloadFilterdataAsFile(DownloadType.CSV);
        filterCsv.downloadQuery(clientConfig(), filterPermKey, sensortype, OffsetDateTime.now(), null, null);

        // download as zip
        DownloadFilterdataAsFile filterZip = new DownloadFilterdataAsFile(DownloadType.ZIP);
        filterZip.downloadQuery(clientConfig(), filterPermKey, sensortype, OffsetDateTime.now(), null, null);

    }
}
