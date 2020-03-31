// Copyright (C) 2010-2020 DOV, http://dov.vlaanderen.be/
// All rights reserved

package be.vlaanderen.dov.services.config;

/**
 *
 * @author Patrick De Baets
 */
public enum DownloadType {

    CSV("text/csv", ".csv"), ZIP("application/zip", ".zip");

    private String mediatype;

    private String extension;

    public String mediatype() {
        return mediatype;
    }

    public String extension() {
        return extension;
    }

    private DownloadType(String mediatype, String extension) {
        this.mediatype = mediatype;
        this.extension = extension;
    }
}
