
package be.vlaanderen.dov.services.hfmetingen.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import be.vlaanderen.dov.services.config.ClientConfig;
import be.vlaanderen.dov.services.config.DownloadType;
import be.vlaanderen.dov.services.hfmetingen.dto.Meetpunt;
import be.vlaanderen.dov.services.hfmetingen.dto.Meetpunt.Meetstatus;

/**
 * Demo class to demonstrate how to download all or a part of the measurements for one filter as a file.
 *
 * We currently support 2 types: csv with all the requested data and a zip with csv files per month.
 *
 * A quick reminder: the measurements of a filter can be the result of different sensors, each with potentially
 * different units.
 *
 * @author Patrick De Baets
 *
 */
public class DownloadFilterdataAsFile {

    private static final Logger LOG = LoggerFactory.getLogger("main");

    private DownloadType mediatype;

    public DownloadFilterdataAsFile(DownloadType mediatype) {
        this.mediatype = mediatype;
    };

    /**
     * download all measurements for a certain type of sensor of a filter.
     *
     * @throws IOException
     */
    public void downloadAll(ClientConfig cc, String filterPermkey, String sensortype) throws IOException {
        downloadQuery(cc, filterPermkey, sensortype, null, null, null);
    }

    /**
     * download only measurements for a certain type of sensor of a filter that meets the specified criteria.
     *
     * @param filterPermkey
     *            the unique identification of the instrument.
     * @param sensortype
     *            the unisue identification of the sensor within the instrument.
     * @param startDate
     *            only datapoints registered after (or on) this {@link OffsetDateTime} are fetched. Can be null.
     * @param endDate
     *            only datapoints registered before (or on) this {@link OffsetDateTime} are fetched. Can be null.
     * @param meetstatus
     *            the possibility to include only validted or non-validated datapoints. Can be null; in that case you
     *            get all the datapoints.
     *
     * @throws IOException
     */
    public void downloadQuery(ClientConfig cc, String filterPermkey, String sensortype, OffsetDateTime startDate,
            OffsetDateTime endDate, Meetstatus meetstatus) throws IOException {

        UriComponentsBuilder cb = base(cc, filterPermkey, sensortype);
        if (startDate != null) {
            cb.queryParam("startDatum", URLEncoder.encode(startDate.format(Meetpunt.FORMATTER), "UTF-8"));
        }
        if (endDate != null) {
            cb.queryParam("eindDatum", URLEncoder.encode(endDate.format(Meetpunt.FORMATTER), "UTF-8"));
        }
        if (meetstatus != null) {
            cb.queryParam("type", meetstatus);
        }
        download(cc, cb.build(true));
    }

    private UriComponentsBuilder base(ClientConfig cc, String filterPermkey, String sensorType) {
        return UriComponentsBuilder.fromUriString(cc.getBaseUrl()).pathSegment("hfmetingen", "meetreeksen",
                filterPermkey, "sensortypes", sensorType, "meetpunten");
    }

    private void download(ClientConfig cc, UriComponents c) throws IOException {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                cc.getHttpClient());

        new RestTemplate(requestFactory).execute(c.toUri(), HttpMethod.GET,
            clientHttpRequest -> clientHttpRequest.getHeaders()
                    .setAccept(MediaType.parseMediaTypes(mediatype.mediatype())),
            clientHttpResponse -> {
                File ret = File.createTempFile("download", mediatype.extension());
                StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
                LOG.info("file written: {}", ret.getCanonicalPath());
                return ret;
            });
    }
}
