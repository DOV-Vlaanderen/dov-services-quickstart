package be.vlaanderen.dov.services;

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
import be.vlaanderen.dov.services.validatie.dto.Code;
import be.vlaanderen.dov.services.validatie.dto.ValidationResponse;
import be.vlaanderen.dov.services.validatie.example.ValidateXml;

/**
 *
 */
@SpringBootApplication(scanBasePackages = { "be.vlaanderen.dov.services.xmlimport" })
@EnableAutoConfiguration
@EnableConfigurationProperties(ClientProperties.class)
public class DemoApplication implements CommandLineRunner {

    private static Logger LOG = LoggerFactory.getLogger("main");

    @Bean
    public ClientConfig clientConfig() {
        // validation service is public, so no need for certificates)
        return new ClientConfig(false);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        // *********************************************************************************************//
        // *** upload the file and inspect the results (both summary and details)
        // *********************************************************************************************//
        ValidationResponse validationResponse = new ValidateXml().validate(clientConfig());

        if (validationResponse == null) {
            LOG.error("Validation: error while validating");
            return;
        }

        AtomicBoolean xmlValid = new AtomicBoolean(true);
        validationResponse.getDetails().stream().forEach(detail -> {
            Code status = detail.getStatus();
            // log the state for every object submitted, and check the status of each of them
            LOG.info("Bodemlocatie {} with status: {}", detail.getBodemlocatie().getNaam(), status.getBeschrijving());
            if (!"validatie zonder fouten".equalsIgnoreCase(status.getBeschrijving())) {
                xmlValid.set(false);
            }
            detail.getMessages().stream().forEach(m -> LOG.info("\t{}, {}", m.getHierarchy(), m.getMessage()));
        });

        if (!xmlValid.get()) {
            LOG.info("Validation verdict: failed");
            return;
        }
        LOG.info("Validation verdict: passed");
    }
}
