
package be.vlaanderen.dov.services.xmlimport.example;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.vlaanderen.dov.services.config.ClientConfig;
import be.vlaanderen.dov.services.xmlimport.dto.UploadedFile;
import be.vlaanderen.dov.services.xmlimport.dto.UploadRequest;

public class RegisterXml extends ValidateXml {

    private static Logger LOG = LoggerFactory.getLogger("main");

    private static String IMPORT_URL = "dov-xdov-server/import/";

    /**
     * validates an uploaded file
     *
     * @throws IOException
     * @throws ClientProtocolException
     * @return a reference to the submitted data. Is can be used to do the follow up of the submitted import
     */
    public UploadRequest submit(ClientConfig cc, UploadedFile file) throws ClientProtocolException, IOException {

        HttpPost httpPost = new HttpPost(cc.getBaseUrl() + IMPORT_URL);

        EntityBuilder builder = EntityBuilder.create();

        UploadRequest body = createInvoerlogBody(file.getId(), UPLOAD_BESCHRIJVING, UPLOAD_USERNAME, UPLOAD_KBONUMMER);

        builder.setText(cc.getMapper().writer().writeValueAsString(body));
        builder.setContentType(ContentType.APPLICATION_JSON);
        httpPost.setEntity(builder.build());

        CloseableHttpResponse response = cc.getHttpClient().execute(httpPost);
        HttpEntity responseEntity = response.getEntity();

        String entityAsString = EntityUtils.toString(responseEntity);
        LOG.debug("Content: {}", entityAsString);
        if (response.getStatusLine().getStatusCode() == 200) {
            return cc.getMapper().readerFor(UploadRequest.class).readValue(entityAsString);
        }
        return null;
    }
}
