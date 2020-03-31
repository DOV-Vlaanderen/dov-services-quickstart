package be.vlaanderen.dov.services;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import be.vlaanderen.dov.services.config.ClientConfig;
import be.vlaanderen.dov.services.config.ClientProperties;
import be.vlaanderen.dov.services.xmlimport.dto.Code;
import be.vlaanderen.dov.services.xmlimport.dto.Detail;
import be.vlaanderen.dov.services.xmlimport.dto.StatusEnum;
import be.vlaanderen.dov.services.xmlimport.dto.UploadRequest;
import be.vlaanderen.dov.services.xmlimport.dto.UploadedFile;
import be.vlaanderen.dov.services.xmlimport.dto.ValidationResponse;
import be.vlaanderen.dov.services.xmlimport.example.HealthCheck;
import be.vlaanderen.dov.services.xmlimport.example.RegisterXml;
import be.vlaanderen.dov.services.xmlimport.example.UploadStatusCheck;
import be.vlaanderen.dov.services.xmlimport.example.UploadXml;
import be.vlaanderen.dov.services.xmlimport.example.ValidateXml;

/**
 * Demo applicatie die aan de hand van de certificaten in een keystore beveiligde request naar de dov-services doet om
 * een xml op te laden, te valdieren, aan te leveren en op te volgen.
 * <p>
 * Meer informatie over de service
 * https://www.milieuinfo.be/confluence/display/DDOV/Case+1%3A+Opladen+van+XML-bestanden+voor+boorbedrijven
 *
 */
@SpringBootApplication(scanBasePackages = { "be.vlaanderen.dov.services.xmlimport" })
@EnableAutoConfiguration
@EnableConfigurationProperties(ClientProperties.class)
public class DemoApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger("main");

    @Bean
    public ClientConfig clientConfig() {
        return new ClientConfig();
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // *********************************************************************************************//
        // the first call - a healthcheck
        // should always work if the certificates are correctly configured and are valid.
        // *********************************************************************************************//
        new HealthCheck().execute(clientConfig());

        // *********************************************************************************************//
        // Step 1: upload xml file to DOV
        // the result is a reference to the uploaded fle, which will be used in step 2 and 3
        // *********************************************************************************************//
        UploadedFile uploadResponse = new UploadXml().upload(clientConfig());

        if (uploadResponse == null) {
            LOG.error("Upload: failed");
            return;
        }
        LOG.info("Upload: success");

        // *********************************************************************************************//
        // *** Step 2: validate the uploaded file and inspect the results (both summary and details)
        // *********************************************************************************************//
        ValidationResponse validationResponse = new ValidateXml().validate(clientConfig(), uploadResponse);

        if (validationResponse == null) {
            LOG.error("Validation: error while validating");
            return;
        }
        AtomicBoolean xmlValid = new AtomicBoolean(true);

        LOG.info("Validation: summary");
        validationResponse.getDetails().stream().forEach(detail -> {
            Code status = detail.getStatus();
            LOG.info("\t{} with status: {}", detail.getIdentificatie(), status.getBeschrijving());
            if (!StatusEnum.GEVALIDEERD_ZONDER_FOUTEN.equals(StatusEnum.fromCode(status.getCode()))) {
                xmlValid.set(false);
            }
            detail.getMessages().stream().forEach(m -> LOG.info(m.getMessage()));
        });

        LOG.info("Validation: details");
        validationResponse.getSummary().getItems().stream()
                .forEach(item -> LOG.info("\t{}: {}", item.getOption().getBeschrijving(), item.getNumberOfObjects()));

        if (!xmlValid.get()) {
            LOG.info("Validation verdict: failed - it ends here");
            return;
        }
        LOG.info("Validation verdict: passed");

        // *********************************************************************************************//
        // *** Step 3: Register the valid uploaded file to dov
        // *********************************************************************************************//
        UploadRequest registerResponse = new RegisterXml().submit(clientConfig(), uploadResponse);
        if (registerResponse == null) {
            LOG.error("Xml submit: failed");
            return;
        }
        // the importId will be used in step 4
        String importId = registerResponse.getId();

        // *********************************************************************************************//
        // *** Step 4 (optional): periodically check the status of the (queued) import
        // *********************************************************************************************//
        UploadStatusCheck check = new UploadStatusCheck();
        UploadRequest uploadStatusResult = check.statusSummary(clientConfig(), importId);

        // poll the status every 30 seconds until a final state is reached
        while (uploadStatusResult != null && !StatusEnum.isFinalState(uploadStatusResult.getStatus().getCode())) {
            TimeUnit.SECONDS.sleep(30);
            uploadStatusResult = check.statusSummary(clientConfig(), importId);
            LOG.info("Importstatus: {}", uploadStatusResult.getStatus().getBeschrijving());
        }

        // inspect the details
        Iterator<Detail> details = check.statusDetail(clientConfig(), importId);
        if (details != null) {
            details.forEachRemaining(detail -> {
                LOG.info("Object: {} {}", detail.getIdentificatie(), detail.getStatus().getBeschrijving());
                detail.getMessages().forEach(msg -> LOG.info("\t" + msg.getMessage()));
            });
        }

    }

}
