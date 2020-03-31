
package be.vlaanderen.dov.services.xmlimport.example;

import java.io.IOException;
import java.util.Date;

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
import be.vlaanderen.dov.services.xmlimport.dto.Code;
import be.vlaanderen.dov.services.xmlimport.dto.OptionEnum;
import be.vlaanderen.dov.services.xmlimport.dto.StatusEnum;
import be.vlaanderen.dov.services.xmlimport.dto.UploadRequest;
import be.vlaanderen.dov.services.xmlimport.dto.UploadedFile;
import be.vlaanderen.dov.services.xmlimport.dto.ValidationResponse;

public class ValidateXml {

    protected static Logger LOG = LoggerFactory.getLogger("main");

    private static final String VALIDATION_URL = "dov-xdov-server/import/validate";

    protected static String UPLOAD_USERNAME = "demo-user"; // naam van de gebruiker

    protected static String UPLOAD_KBONUMMER = "0658786782"; // kbo nummer van boorbedrijf

    protected static String UPLOAD_BESCHRIJVING = "opladen data van december"; // beschrijving van de import

    /**
     * validates an uploaded file
     *
     * @throws IOException
     * @throws ClientProtocolException
     */
    public ValidationResponse validate(ClientConfig cc, UploadedFile file) throws ClientProtocolException, IOException {

        HttpPost httpPost = new HttpPost(cc.getBaseUrl() + VALIDATION_URL);
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
            return cc.getMapper().readerFor(ValidationResponse.class).readValue(entityAsString);
        }
        return null;
    }

    /**
     * Maak de body voor het valideren of starten van de import van een bestand.
     *
     * @param fileId
     *            hier moet het id ingevuld worden dat als response uit stap 1 gekomen is
     * @param beschrijving
     *            vul hier een beschrijving in van de het bestand (bvb 'opladen data van december'). Dit is vrije tekst
     * @param gebruikersnaam
     *            hier moet de gebruikersnaam ingevuld worden; de bedrijfsnaam is ook goed
     * @param kbonummer
     *            hier moet het KBO-nummer van uw organisatie ingevuld worden (zonder puntjes!)
     * @return object to be posted to dov services
     */
    protected UploadRequest createInvoerlogBody(String fileId, String beschrijving, String gebruikersnaam,
            String kbonummer) {
        UploadRequest invoerLogDto = new UploadRequest();
        invoerLogDto.setOmschrijving(beschrijving);
        invoerLogDto.setGebruikersnaam(gebruikersnaam);
        invoerLogDto.setDatumOpladen(new Date());
        invoerLogDto.setPartner(kbonummer);

        // reference to the uploaded file
        UploadedFile bestand = new UploadedFile();
        bestand.setId(fileId);
        invoerLogDto.setBestand(bestand);

        // De opties bepalen welke delen van de xml ingelezen kunnen worden.
        // De lijst ligt vast voor externe gebruikers.
        invoerLogDto.getOptions().add(OptionEnum.BORING.asOption());
        invoerLogDto.getOptions().add(OptionEnum.BORING_ALTERNATIEF.asOption());
        invoerLogDto.getOptions().add(OptionEnum.BORING_OPMERKING.asOption());
        invoerLogDto.getOptions().add(OptionEnum.INTERPRETATIE_INFORMEEL.asOption());
        invoerLogDto.getOptions().add(OptionEnum.INTERPRETATIE_LITHOLOGIE.asOption());
        invoerLogDto.getOptions().add(OptionEnum.INTERPRETATIE_HYDRO.asOption());
        invoerLogDto.getOptions().add(OptionEnum.INTERPRETATIE_GEOTECHNISCH.asOption());

        // Invoerwijze voor aanleveren van boringen is altijd edov
        invoerLogDto.setInvoerwijze(new Code("2", "edov"));

        // De status start altijd op niet verwerkt.
        invoerLogDto
                .setStatus(new Code(StatusEnum.NIET_VERWERKT.code(), StatusEnum.NIET_VERWERKT.beschrijving()));
        return invoerLogDto;
    }
}
