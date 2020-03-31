// Copyright (C) 2010-2020 DOV, http://dov.vlaanderen.be/
// All rights reserved

package be.vlaanderen.dov.services.validatie.example;

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
import org.springframework.web.util.UriComponentsBuilder;

import be.vlaanderen.dov.services.config.ClientConfig;
import be.vlaanderen.dov.services.validatie.dto.ValidationResponse;

/**
 * Validate an XML against the dov schema.
 * 
 * @author Patrick De Baets
 */
public class ValidateXml {

    protected static Logger LOG = LoggerFactory.getLogger("main");

    private static final String OE_FILENAME = "dov-archeologie.zip";

    /**
     * validates an uploaded file
     *
     * @throws IOException
     * @throws ClientProtocolException
     * @return the results of the validation.
     */
    public ValidationResponse validate(ClientConfig cc) throws ClientProtocolException, IOException {

        HttpPost httpPost = new HttpPost(base(cc).build().toUri());

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        // attach the file to the POST
        InputStream is = getClass().getResourceAsStream("/" + OE_FILENAME);
        builder.addBinaryBody("file", is, ContentType.APPLICATION_OCTET_STREAM, OE_FILENAME);

        HttpEntity multipart = builder.build();
        httpPost.setEntity(multipart);
        CloseableHttpResponse response = cc.getHttpClient().execute(httpPost);
        HttpEntity responseEntity = response.getEntity();

        if (response.getStatusLine().getStatusCode() == 200 && responseEntity != null) {
            String entityAsString = EntityUtils.toString(responseEntity);
            LOG.debug("Content: {}", entityAsString);
            return cc.getMapper().readerFor(ValidationResponse.class).readValue(entityAsString);
        }
        EntityUtils.consume(responseEntity);
        return null;
    }

    private UriComponentsBuilder base(ClientConfig cc) {
        return UriComponentsBuilder.fromUriString(cc.getBaseUrl()).pathSegment("dov-xdov-server", "import", "validate",
                "public");
    }

}
