
package be.vlaanderen.dov.services.hfmetingen.example;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.OffsetDateTime;
import java.util.List;

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

import com.fasterxml.jackson.core.type.TypeReference;

import be.vlaanderen.dov.services.config.ClientConfig;
import be.vlaanderen.dov.services.hfmetingen.dto.Code;
import be.vlaanderen.dov.services.hfmetingen.dto.FilterMetingen;
import be.vlaanderen.dov.services.hfmetingen.dto.Meetpunt;
import be.vlaanderen.dov.services.hfmetingen.dto.Meetpunt.Meetstatus;

/**
 * Demo class to demonstrate how to download all or a part of the measurements for one filter.
 *
 * Reminder: the measurements of a filter can be the result of different sensors, each with potentially different unit.
 *
 * @author Patrick De Baets
 *
 */
public class DownloadFilterdataAsJson {

    protected static final Logger LOG = LoggerFactory.getLogger("main");

    public List<Code> getSensortypes(ClientConfig cc, String filterPermkey) throws IOException {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                cc.getHttpClient());

        UriComponents c = UriComponentsBuilder.fromUriString(cc.getBaseUrl())
                .pathSegment("hfmetingen", "meetreeksen", filterPermkey, "sensortypes").build();

        HttpHeaders header = new HttpHeaders();
        header.setAccept(MediaType.parseMediaTypes(MediaType.APPLICATION_JSON_VALUE));
        HttpEntity<String> entity = new HttpEntity<>(header);

        ResponseEntity<String> response = new RestTemplate(requestFactory).exchange(c.toUri(), HttpMethod.GET, entity,
                String.class);

        if (HttpStatus.OK.equals(response.getStatusCode())) {
            String entityAsString = response.getBody();
            LOG.debug("Content: " + entityAsString);
            return cc.getMapper().readerFor(new TypeReference<List<Code>>() {
            }).readValue(entityAsString);
        }
        return null;
    }

    /**
     * download all measurements for a certain type of sensor of a filter.
     *
     * @throws IOException
     * @return meetpuntdata which contains a list of the points.
     */
    public FilterMetingen downloadAll(ClientConfig cc, String instrumentPermkey, String sensorId)
            throws IOException {
        return downloadQuery(cc, instrumentPermkey, sensorId, null, null, null);
    }

    /**
     * download only measurements for a certain type of sensor of a filter that meet the specified criteria.
     *
     * @param instrumentPermkey
     *            the unique identification of the instrument.
     * @param sensorId
     *            the unisue identification of the sensor within the instrument.
     * @param startDate
     *            only datapoints registered after (or on) this {@link OffsetDateTime} are fetched. Can be null.
     * @param endDate
     *            only datapoints registered before (or on) this {@link OffsetDateTime} are fetched. Can be null.
     * @param meetstatus
     *            the possibility to include only validted or non-validtaed datapoints. Can be null; in that case you
     *            get all the datapoints.
     *
     * @throws IOException
     * @return filterdata which contains a list of the points.
     */
    public FilterMetingen downloadQuery(ClientConfig cc, String instrumentPermkey, String sensorId,
            OffsetDateTime startDate, OffsetDateTime endDate, Meetstatus meetstatus) throws IOException {

        UriComponentsBuilder cb = base(cc, instrumentPermkey, sensorId);
        if (startDate != null) {
            cb.queryParam("startDatum", URLEncoder.encode(startDate.format(Meetpunt.FORMATTER), "UTF-8"));
        }
        if (endDate != null) {
            cb.queryParam("eindDatum", URLEncoder.encode(endDate.format(Meetpunt.FORMATTER), "UTF-8"));
        }
        if (meetstatus != null) {
            cb.queryParam("type", meetstatus);
        }
        return download(cc, cb.build(true));
    }

    private UriComponentsBuilder base(ClientConfig cc, String filterPermkey, String sensorType) {
        return UriComponentsBuilder.fromUriString(cc.getBaseUrl()).pathSegment("hfmetingen", "meetreeksen",
                filterPermkey, "sensortypes", sensorType, "meetpunten");
    }

    private FilterMetingen download(ClientConfig cc, UriComponents c) throws IOException {

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
            return cc.getMapper().readerFor(FilterMetingen.class).readValue(entityAsString);
        }
        return null;
    }

}
