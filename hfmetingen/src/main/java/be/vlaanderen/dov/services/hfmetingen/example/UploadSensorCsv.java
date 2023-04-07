
package be.vlaanderen.dov.services.hfmetingen.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import be.vlaanderen.dov.services.config.ClientConfig;
import be.vlaanderen.dov.services.hfmetingen.dto.Meetpunt;
import be.vlaanderen.dov.services.hfmetingen.dto.Meetpunt.Meetstatus;
import be.vlaanderen.dov.services.hfmetingen.dto.UploadResponse;

/**
 * Demo class to demonstrate how to upload one measurement points.
 *
 * @author Patrick De Baets
 *
 */
public class UploadSensorCsv extends UploadSensorMeetpunten {

    protected static Logger LOG = LoggerFactory.getLogger("main");

    /**
     * name of the file.
     */
    private static String CSV_FILENAME = "meetpunten.csv";

    public UploadSensorCsv() {
        super();
    }

    public UploadSensorCsv(int items) {
        super(items);
    }

    /**
     * add one or more points via the dov service via a csv file.
     *
     * @throws IOException
     * @throws ClientProtocolException
     * @return a reference to the uploaded file. The id is needed for all future request.
     */
    @Override
    public UploadResponse upload(ClientConfig cc, String instrument, String sensorId) throws ClientProtocolException, IOException, ParseException {

        // create the csv file
        File f = createCSV();
        HttpPost httpPost = new HttpPost(base(cc, instrument, sensorId).build().toUri());

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        // This attaches the file to the POST. content type must be set to 'text/csv'!
        FileInputStream fis = new FileInputStream(f);
        builder.addBinaryBody("file", fis, ContentType.parse("text/csv"), CSV_FILENAME);

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);
        CloseableHttpResponse response = cc.getHttpClient().execute(httpPost);
        HttpEntity responseEntity = response.getEntity();

        if (response.getCode() == 200 && responseEntity != null) {
            String entityAsString = EntityUtils.toString(responseEntity);
            LOG.debug("Content: {}", entityAsString);
            return cc.getMapper().readerFor(UploadResponse.class).readValue(entityAsString);
        }
        EntityUtils.consume(responseEntity);

        return null;

    }

    @Override
    protected UriComponentsBuilder base(ClientConfig cc, String instrumentPermkey, String sensorId) {
        return UriComponentsBuilder.fromUriString(cc.getBaseUrl()).pathSegment("hfmetingen", "instrumenten",
                instrumentPermkey, "sensoren", sensorId, "meetpunten");
    }

    public File createCSV() {
        List<Meetpunt> datalines = createBody();

        File csvOutputFile = new File(System.getProperty("java.io.tmpdir") + File.separator + CSV_FILENAME);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            datalines.stream().forEach(m -> {
                StringBuffer sb = new StringBuffer(m.getTijd()).append(",").append(m.getWaarde()).append(",")
                        .append(Meetstatus.GEVALIDEERD == m.getStatus() ? "1" : "0");
                pw.println(sb.toString());
            });
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage(), e);
        }
        csvOutputFile.deleteOnExit();
        return csvOutputFile;
    }
}
