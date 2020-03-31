package be.vlaanderen.dov.services.hfmetingen.example;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.type.TypeReference;

import be.vlaanderen.dov.services.config.ClientConfig;
import be.vlaanderen.dov.services.hfmetingen.dto.Code;

public class DownloadSensoren {

    private static final Logger LOG = LoggerFactory.getLogger("main");

    /**
     * Fetch all sensors of one instrument.
     *
     * @throws IOException
     * @throws ClientProtocolException
     * @return
     */
    public List<Code> fetch(ClientConfig cc, String instrumentPermkey) throws ClientProtocolException, IOException {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                cc.getHttpClient());

        ResponseEntity<String> response = new RestTemplate(requestFactory)
                .exchange(base(cc, instrumentPermkey).build().toUri(), HttpMethod.GET, null, String.class);

        if (HttpStatus.OK.equals(response.getStatusCode())) {
            String entityAsString = response.getBody();
            LOG.debug("Content: {}", entityAsString);
            return cc.getMapper().readerFor(new TypeReference<List<Code>>() {
            }).readValue(entityAsString);
        }
        return null;
    }

    /**
     * build the 'base url' for this functionality.
     *
     * @param cc
     * @param instrumentPermkey
     * @return
     */
    private UriComponentsBuilder base(ClientConfig cc, String instrumentPermkey) {
        return UriComponentsBuilder.fromUriString(cc.getBaseUrl()).pathSegment("hfmetingen", "instrumenten",
                instrumentPermkey, "sensoren");
    }
}
