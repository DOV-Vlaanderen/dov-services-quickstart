
package be.vlaanderen.dov.services.hfmetingen.example;

import java.io.IOException;

import org.apache.hc.client5.http.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import be.vlaanderen.dov.services.config.ClientConfig;
import be.vlaanderen.dov.services.hfmetingen.dto.UploadRequest;

public class UploadStatusCheck {

    private static Logger LOG = LoggerFactory.getLogger("main");

    /**
     * check the status of an upload.
     *
     * @throws IOException
     * @throws ClientProtocolException
     * @return a summary of the status.
     */
    public UploadRequest statusSummary(ClientConfig cc, String importId) throws ClientProtocolException, IOException {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                cc.getHttpClient());

        ResponseEntity<String> response = new RestTemplate(requestFactory).exchange(base(cc, importId).build().toUri(),
                HttpMethod.GET, null, String.class);

        if (HttpStatus.OK.equals(response.getStatusCode())) {
            String entityAsString = response.getBody();
            LOG.debug("Content: {}", entityAsString);
            return cc.getMapper().readerFor(UploadRequest.class).readValue(entityAsString);
        }
        return null;
    }

    private UriComponentsBuilder base(ClientConfig cc, String importId) {
        return UriComponentsBuilder.fromUriString(cc.getBaseUrl()).pathSegment("hfmetingen", "importlog", importId);
    }

}
