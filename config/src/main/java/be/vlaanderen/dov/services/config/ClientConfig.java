// Copyright (C) 2010-2020 DOV, http://dov.vlaanderen.be/
// All rights reserved

package be.vlaanderen.dov.services.config;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
//import org.apache.http.conn.socket.ConnectionSocketFactory;
//import org.apache.http.conn.socket.PlainConnectionSocketFactory;
//import org.apache.http.conn.ssl.NoopHostnameVerifier;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
//import org.apache.http.ssl.SSLContexts;
//import org.apache.http.ssl.TrustStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

/**
 *
 * @author Patrick De Baets
 *
 */
public class ClientConfig {

    private static Logger LOG = LoggerFactory.getLogger("config");

    private CloseableHttpClient client;

    @Autowired
    private ClientProperties properties;

    private JsonMapper mapper;

    private boolean secured;

    public ClientConfig() {
        this(true);
    }

    public ClientConfig(boolean useCertificates) {
        this.secured = useCertificates;
        mapper = JsonMapper.builder().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).build();
    }

    public CloseableHttpClient getHttpClient() {
        if (client == null) {
            try {
                client = secured ? createSecureHttpClient() : createPublicHttpClient();
            } catch (KeyManagementException | UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException
                    | CertificateException | IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return client;
    }

    public JsonMapper getMapper() {
        return mapper;
    }

    public String getBaseUrl() {
        return properties.getBaseUrl();
    }

    /**
     * Make httpClient with ssl certificates.
     */
    private CloseableHttpClient createSecureHttpClient() throws KeyManagementException, NoSuchAlgorithmException,
            KeyStoreException, UnrecoverableKeyException, CertificateException, IOException {
        // we trust everybody else...
        TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;

        // make my keystore
        KeyStore keystore = KeyStore.getInstance(properties.getKeystoreType());

        // load the keystore with the keystore password
        InputStream is = ClientConfig.class.getResourceAsStream(properties.getKeystorelocation());
        keystore.load(is, properties.getKeystorePassword().toCharArray());

        // define the SSL context
        SSLContext sslContext = SSLContexts.custom()
                .loadKeyMaterial(keystore, properties.getCertificatePassword().toCharArray())
                .loadTrustMaterial(null, acceptingTrustStrategy).build();
        SSLConnectionSocketFactory sslsf = SSLConnectionSocketFactoryBuilder.create().setSslContext(sslContext)
                .setHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(sslsf).build();

        HttpClientBuilder cb = HttpClients.custom().setConnectionManager(connectionManager).useSystemProperties();

        // sometimes an internet proxy is needed to get outside your company.
        if (!properties.getProxyHost().isEmpty()) {
            cb.setProxy(new HttpHost(properties.getProxyHost(), properties.getProxyPort()));
        }
        return cb.build();
    }

    /**
     * Make simple httpClient.
     */
    private CloseableHttpClient createPublicHttpClient() {

        HttpClientBuilder cb = HttpClients.custom().useSystemProperties();

        // sometimes an internet proxy is needed to get outside your company.
        if (!properties.getProxyHost().isEmpty()) {
            cb.setProxy(new HttpHost(properties.getProxyHost(), properties.getProxyPort()));
        }
        return cb.build();
    }

}
