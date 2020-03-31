// Copyright (C) 2010-2020 DOV, http://dov.vlaanderen.be/
// All rights reserved

package be.vlaanderen.dov.services.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Patrick De Baets
 */
@ConfigurationProperties(prefix = ClientProperties.PREFIX)
public class ClientProperties {

    static final String PREFIX = "service";

    private String baseUrl;

    private String keystoreType;

    private String keystorelocation;

    private String keystorePassword;

    private String certificatePassword;

    private String proxyHost;

    private int proxyPort;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getKeystoreType() {
        return keystoreType;
    }

    public void setKeystoreType(String keystoreType) {
        this.keystoreType = keystoreType;
    }

    public String getKeystorelocation() {
        return keystorelocation;
    }

    public void setKeystorelocation(String keystorelocation) {
        this.keystorelocation = keystorelocation;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getCertificatePassword() {
        return certificatePassword;
    }

    public void setCertificatePassword(String certificatePassword) {
        this.certificatePassword = certificatePassword;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

}
