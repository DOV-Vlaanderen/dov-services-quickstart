// Copyright (C) 2010-2020 DOV, http://dov.vlaanderen.be/
// All rights reserved
package be.vlaanderen.dov.services.validatie.dto;

/**
 * Data transfer object that represents a Bodemlocatie.
 *
 *  @author Patrick De Baets
 *
 */
public class Bodemlocatie {

    private String id;

    private String naam;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

}
