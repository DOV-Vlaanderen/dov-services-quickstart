package be.vlaanderen.dov.services.xmlimport.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import be.vlaanderen.dov.services.config.ClientConfig;

/**
 * @author Patrick De Baets
 *
 */
public class HealthCheck {

    private static Logger LOG = LoggerFactory.getLogger("main");

    private static final String HEALTH_CHECK_URL = "dov-xdov-server/logs/chucknorris";

    /**
     * Roept de chuck norris healthcheck url op.
     */
    public void execute(ClientConfig cc) {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                cc.getHttpClient());

        ResponseEntity<String> response = new RestTemplate(requestFactory).exchange(cc.getBaseUrl() + HEALTH_CHECK_URL,
                HttpMethod.GET, null, String.class);

        LOG.info("Healthcheck: chuck says: " + response.getBody());
    }

}
