
package be.vlaanderen.dov.services.xmlimport.example;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Iterator;

import org.apache.hc.client5.http.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import be.vlaanderen.dov.services.config.ClientConfig;
import be.vlaanderen.dov.services.xmlimport.dto.Detail;
import be.vlaanderen.dov.services.xmlimport.dto.UploadRequest;

public class UploadStatusCheck {

    private static Logger LOG = LoggerFactory.getLogger("main");

    private static final String STATUS_URL = "dov-xdov-server/logs/";

    /**
     * Fetch the status of one import.
     *
     * @throws IOException
     * @throws ClientProtocolException
     * @return
     */
    public UploadRequest statusSummary(ClientConfig cc, String importId) throws ClientProtocolException, IOException {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                cc.getHttpClient());

        ResponseEntity<String> response = new RestTemplate(requestFactory)
                .exchange(cc.getBaseUrl() + STATUS_URL + importId, HttpMethod.GET, null, String.class);

        if (HttpStatus.OK.equals(response.getStatusCode())) {
            String entityAsString = response.getBody();
            LOG.debug("Content: " + entityAsString);
            return cc.getMapper().readerFor(UploadRequest.class).readValue(entityAsString);
        }
        return null;
    }

    /**
     * Fetch the details of an import.
     *
     * @param cc
     *            the {@link ClientConfig}
     * @param importId
     */
    public Iterator<Detail> statusDetail(ClientConfig cc, String importId)
            throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, IOException {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                cc.getHttpClient());

        ResponseEntity<String> response = new RestTemplate(requestFactory)
                .exchange(cc.getBaseUrl() + STATUS_URL + importId + "/details", HttpMethod.GET, null, String.class);

        if (HttpStatus.OK.equals(response.getStatusCode())) {
            String entityAsString = response.getBody();
            LOG.debug("Content: " + entityAsString);
            return cc.getMapper().readerFor(Detail.class).readValues(entityAsString);
        }
        return null;
    }
}
