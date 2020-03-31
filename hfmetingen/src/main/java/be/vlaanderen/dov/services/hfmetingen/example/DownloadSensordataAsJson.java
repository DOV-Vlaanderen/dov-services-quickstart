
package be.vlaanderen.dov.services.hfmetingen.example;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import be.vlaanderen.dov.services.config.ClientConfig;
import be.vlaanderen.dov.services.hfmetingen.dto.Meetpunt;
import be.vlaanderen.dov.services.hfmetingen.dto.Meetpunt.Meetstatus;
import be.vlaanderen.dov.services.hfmetingen.dto.SensorMetingen;

/**
 * Demo class to demonstrate how to download multiple sensort measuremenrs into a file.
 *
 * We currently support 2 types: csv and zip
 *
 * @author Patrick De Baets
 *
 */
public class DownloadSensordataAsJson {

    private static final Logger LOG = LoggerFactory.getLogger("main");

    /**
     * download all points via the dov service.
     *
     * @throws IOException
     * @return meetpuntdata which contains a list of the points.
     */
    public SensorMetingen downloadAll(ClientConfig cc, String instrumentPermkey, String sensorId) throws IOException {
        return downloadQuery(cc, instrumentPermkey, sensorId, null, null, null);
    }

    /**
     * download all points via the dov service.
     *
     * @throws IOException
     * @return meetpuntdata which contains a list of the points.
     */
    public SensorMetingen downloadQuery(ClientConfig cc, String instrumentPermkey, String sensorId,
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
        return download(cc, cb.build(true));
    }

    private UriComponentsBuilder base(ClientConfig cc, String instrumentPermkey, String sensorId) {
        return UriComponentsBuilder.fromUriString(cc.getBaseUrl()).pathSegment("hfmetingen", "instrumenten",
                instrumentPermkey, "sensoren", sensorId, "meetpunten");
    }

    private SensorMetingen download(ClientConfig cc, UriComponents c) throws IOException {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                cc.getHttpClient());

        HttpHeaders header = new HttpHeaders();
        header.setAccept(MediaType.parseMediaTypes(MediaType.APPLICATION_JSON_VALUE));
        HttpEntity<String> entity = new HttpEntity<>(header);

        ResponseEntity<String> response = new RestTemplate(requestFactory).exchange(c.toUri(), HttpMethod.GET, entity,
                String.class);

        if (HttpStatus.OK.equals(response.getStatusCode())) {
            String entityAsString = response.getBody();
            LOG.debug("Content: {}", entityAsString);
            return cc.getMapper().readerFor(SensorMetingen.class).readValue(entityAsString);
        }
        return null;
    }

}
