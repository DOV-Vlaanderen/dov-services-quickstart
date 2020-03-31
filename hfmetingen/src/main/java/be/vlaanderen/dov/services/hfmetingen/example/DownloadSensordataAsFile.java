
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
 * Demo class to demonstrate how to download multiple sensort measurements into a file.
 *
 * We currently support 2 types: csv and zip
 *
 * @author Patrick De Baets
 *
 */
public class DownloadSensordataAsFile {

    private static final Logger LOG = LoggerFactory.getLogger("main");

    private DownloadType mediatype;

    public DownloadSensordataAsFile(DownloadType mediatype) {
        this.mediatype = mediatype;
    }

    /**
     * download all points via the dov service.
     *
     * @throws IOException
     */
    public void downloadAll(ClientConfig cc, String instrumentPermkey, String sensorId) throws IOException {
        downloadQuery(cc, instrumentPermkey, sensorId, null, null, null);
    }

    /**
     * download all points that meet the criteria.
     *
     * @throws IOException
     * @return meetpuntdata which contains a list of the points.
     */
    public void downloadQuery(ClientConfig cc, String instrumentPermkey, String sensorId,
            OffsetDateTime startDate, OffsetDateTime endDate, Meetstatus meetstatus) throws IOException {

        UriComponentsBuilder cb = base(cc, instrumentPermkey, sensorId);
        if (startDate != null) {
            cb.queryParam("startDatum", URLEncoder.encode(startDate.format(Meetpunt.FORMATTER), "UTF-8"));
        }
        if (endDate != null) {
            cb.queryParam("eindDatum", URLEncoder.encode(endDate.format(Meetpunt.FORMATTER), "UTF-8"));
        }
        if (meetstatus != null) {
            cb.queryParam("type", meetstatus.name());
        }
        download(cc, cb.build(true));
    }

    private UriComponentsBuilder base(ClientConfig cc, String instrumentPermkey, String sensorId) {
        return UriComponentsBuilder.fromUriString(cc.getBaseUrl()).pathSegment("hfmetingen", "instrumenten",
                instrumentPermkey, "sensoren", sensorId, "meetpunten");
    }

    private void download(ClientConfig cc, UriComponents c) throws IOException {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                cc.getHttpClient());

        new RestTemplate(requestFactory).execute(c.toUri(), HttpMethod.GET,
                clientHttpRequest -> clientHttpRequest.getHeaders().setAccept(MediaType.parseMediaTypes(mediatype.mediatype())),
                clientHttpResponse -> {
            File ret = File.createTempFile("download", mediatype.extension());
            StreamUtils.copy(clientHttpResponse.getBody(), new FileOutputStream(ret));
            LOG.info("file written: {}", ret.getCanonicalPath());
            return ret;
        });
    }

}
