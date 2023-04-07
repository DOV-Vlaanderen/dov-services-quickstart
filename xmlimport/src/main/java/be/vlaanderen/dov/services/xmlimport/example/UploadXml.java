
package be.vlaanderen.dov.services.xmlimport.example;

import java.io.IOException;
import java.io.InputStream;

import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
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
    public UploadedFile upload(ClientConfig cc) throws ClientProtocolException, IOException, ParseException {

        HttpPost httpPost = new HttpPost(cc.getBaseUrl() + FILE_UPLOAD_URL);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        // This attaches the file to the POST:
        InputStream is = UploadXml.class.getResourceAsStream("/" + XML_FILENAME);
        builder.addBinaryBody("file", is, ContentType.APPLICATION_OCTET_STREAM, XML_FILENAME);

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);
        CloseableHttpResponse response = cc.getHttpClient().execute(httpPost);
        HttpEntity responseEntity = response.getEntity();

        if (response.getCode() == 200 && responseEntity != null) {
            String entityAsString = EntityUtils.toString(responseEntity);
            LOG.debug("Content: {}", entityAsString);
            return cc.getMapper().readerFor(UploadedFile.class).readValue(entityAsString);
        }
        EntityUtils.consume(responseEntity);

        return null;

    }

}
