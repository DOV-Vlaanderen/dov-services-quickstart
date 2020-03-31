
package be.vlaanderen.dov.services.hfmetingen.example;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.UriComponentsBuilder;

import be.vlaanderen.dov.services.config.ClientConfig;
import be.vlaanderen.dov.services.hfmetingen.dto.Meetpunt;
import be.vlaanderen.dov.services.hfmetingen.dto.Meetpunt.Meetstatus;
import be.vlaanderen.dov.services.hfmetingen.dto.UploadResponse;
/**
 * Demo class to demonstrate how to upload one measurement points.
 *
 * @author Patrick De Baets
 *
 */
public class UploadSensorMeetpunt {

    protected static Logger LOG = LoggerFactory.getLogger("main");

    /**
     * add one or more points via the dov service.
     *
     * @throws IOException
     * @throws ClientProtocolException
     * @return a reference to the uploaded file. The id is needed for all future request.
     */
    public UploadResponse upload(ClientConfig cc, String instrument, String sensorId) throws ClientProtocolException, IOException {

        HttpPost httpPost = new HttpPost(base(cc, instrument, sensorId).build().toUri());

        EntityBuilder builder = EntityBuilder.create();

        List<Meetpunt> body = createBody();

        builder.setText(cc.getMapper().writer().writeValueAsString(body));
        LOG.debug("Sent: {}", builder.getText());
        builder.setContentType(ContentType.APPLICATION_JSON);
        httpPost.setEntity(builder.build());

        CloseableHttpResponse response = cc.getHttpClient().execute(httpPost);
        HttpEntity responseEntity = response.getEntity();

        String entityAsString = EntityUtils.toString(responseEntity);
        LOG.debug("Content: {}", entityAsString);
        if (response.getStatusLine().getStatusCode() == 200) {
            return cc.getMapper().readerFor(UploadResponse.class).readValue(entityAsString);
        }
        return null;
    }

    protected List<Meetpunt> createBody() {
        List<Meetpunt> results = new ArrayList<>();
        results.add(new Meetpunt(OffsetDateTime.now().format(Meetpunt.FORMATTER), 10.66, Meetstatus.GEVALIDEERD));
        //results.add(new MeetpuntDto("2020-02-10T10:10:10.000+01:00", 10.66, Meetstatus.GEVALIDEERD));
        return results;
    }

    protected UriComponentsBuilder base(ClientConfig cc, String instrumentPermkey, String sensorId) {
        return UriComponentsBuilder.fromUriString(cc.getBaseUrl()).pathSegment("hfmetingen", "instrumenten",
                instrumentPermkey, "sensoren", sensorId, "meetpunten");
    }
}
