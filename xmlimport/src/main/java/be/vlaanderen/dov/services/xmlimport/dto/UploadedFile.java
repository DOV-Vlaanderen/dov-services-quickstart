// Copyright (C) 2010-2013 DOV, http://dov.vlaanderen.be/
// All rights reserved
package be.vlaanderen.dov.services.xmlimport.dto;


/**
 * Data Transfer Object that represents a XmlBestand.
 * <p>
 * Does NOT include the uploaded XML itself.
 *
 * @author Timothy De Bock
 */
public final class UploadedFile {

    private String id;

    private String naam;


    /**
     * @return the id
     */

    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the naam
     */
    public String getNaam() {
        return naam;
    }

    /**
     * @param naam the naam to set
     */
    public void setNaam(String naam) {
        this.naam = naam;
    }

}
