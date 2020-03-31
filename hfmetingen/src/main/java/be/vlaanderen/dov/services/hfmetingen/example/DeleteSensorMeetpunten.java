
package be.vlaanderen.dov.services.hfmetingen.example;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import be.vlaanderen.dov.services.config.ClientConfig;
import be.vlaanderen.dov.services.hfmetingen.dto.DeleteResponse;
import be.vlaanderen.dov.services.hfmetingen.dto.Meetpunt;
import be.vlaanderen.dov.services.hfmetingen.dto.Meetpunt.Meetstatus;

/**
 * Demo class to demonstrate how to delete a range of measurement points.
 *
 * @author Patrick De Baets
 *
 */
public class DeleteSensorMeetpunten {

    private static final Logger LOG = LoggerFactory.getLogger("main");

    /**
     * delete all points of one sensor between a date range.
     *
     * @throws IOException
     * @return meetpuntdata which contains a list of the points.
     */
    public DeleteResponse delete(ClientConfig cc, String instrumentPermkey, String sensorId, OffsetDateTime startDate,
            OffsetDateTime endDate, Meetstatus meetstatus) throws IOException {

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

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                cc.getHttpClient());

        ResponseEntity<String> response = new RestTemplate(requestFactory).exchange(cb.build(true).toUri(),
                HttpMethod.DELETE, null, String.class);
        if (HttpStatus.OK.equals(response.getStatusCode())) {
            String entityAsString = response.getBody();
            LOG.debug("Content: {}", entityAsString);
            return cc.getMapper().readerFor(DeleteResponse.class).readValue(entityAsString);
        }
        return null;
    }

    private UriComponentsBuilder base(ClientConfig cc, String instrumentPermkey, String sensorId) {
        return UriComponentsBuilder.fromUriString(cc.getBaseUrl()).pathSegment("hfmetingen", "instrumenten",
                instrumentPermkey, "sensoren", sensorId, "meetpunten");
    }

}
