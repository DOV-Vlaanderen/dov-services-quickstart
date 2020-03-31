
package be.vlaanderen.dov.services.xmlimport.example;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.vlaanderen.dov.services.config.ClientConfig;
import be.vlaanderen.dov.services.xmlimport.dto.UploadedFile;

public class UploadXml {

    private static Logger LOG = LoggerFactory.getLogger("main");

    private static final String FILE_UPLOAD_URL = "dov-xdov-server/import/upload";

    /**
     * name of the file, located in the resources folder.
     */
    private static String XML_FILENAME = "1419-b-P6-bis.xml";

    /**
     * upload a file to dov service.
     *
     * @throws IOException
     * @throws ClientProtocolException
     * @return a reference to the uploaded file. The id is needed for all future request.
     */
    public UploadedFile upload(ClientConfig cc) throws ClientProtocolException, IOException {

        HttpPost httpPost = new HttpPost(cc.getBaseUrl() + FILE_UPLOAD_URL);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        // This attaches the file to the POST:
        InputStream is = UploadXml.class.getResourceAsStream("/" + XML_FILENAME);
        builder.addBinaryBody("file", is, ContentType.APPLICATION_OCTET_STREAM, XML_FILENAME);

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);
        CloseableHttpResponse response = cc.getHttpClient().execute(httpPost);
        HttpEntity responseEntity = response.getEntity();

        if (response.getStatusLine().getStatusCode() == 200 && responseEntity != null) {
            String entityAsString = EntityUtils.toString(responseEntity);
            LOG.debug("Content: {}", entityAsString);
            return cc.getMapper().readerFor(UploadedFile.class).readValue(entityAsString);
        }
        EntityUtils.consume(responseEntity);

        return null;

    }

}
