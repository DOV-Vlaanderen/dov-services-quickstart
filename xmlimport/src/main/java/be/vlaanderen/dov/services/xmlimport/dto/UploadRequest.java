// Copyright (C) 2010-2013 DOV, http://dov.vlaanderen.be/
// All rights reserved

package be.vlaanderen.dov.services.xmlimport.dto;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Data transfer object that represents the InvoerLog pojo.
 *
 * @author DEBAETSP
 */
public final class UploadRequest {

    private String id;

    private String omschrijving;

    private String gebruikersnaam;

    private Date datumOpladen;

    private UploadedFile bestand;

    private Date datumProcessed;

    private Set<UploadOption> options = new HashSet<UploadOption>();

    private int aantalVerwerkt;

    private int aantalFouten;

    private String partner;

    private Code invoerwijze;

    private Code status;

    /**
     * simple getter for dto.
     *
     * @return the gebruikersnaam
     */
    public String getGebruikersnaam() {
        return gebruikersnaam;
    }

    /**
     * simple setter for dto.
     *
     * @param gebruikersnaam the gebruikersnaam to set
     */
    public void setGebruikersnaam(String gebruikersnaam) {
        this.gebruikersnaam = gebruikersnaam;
    }

    /**
     * simple setter for dto.
     *
     * @param omschrijving the omschrijving to set
     */
    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    /**
     * simple getter for dto.
     *
     * @return the omschrijving
     */
    public String getOmschrijving() {
        return omschrijving;
    }


    /**
     * simple getter for dto.
     *
     * @return the datumOpladen
     */
    public Date getDatumOpladen() {
        if (datumOpladen != null) {
            return (Date) datumOpladen.clone();
        }
        return null;
    }

    /**
     * simple setter for dto.
     *
     * @param datumOpladen the datumOpladen to set
     */
    public void setDatumOpladen(Date datumOpladen) {
        this.datumOpladen = null;
        if (datumOpladen != null) {
            this.datumOpladen = (Date) datumOpladen.clone();
        }
    }

    /**
     * simple setter for dto.
     *
     * @param datumProcessed the datumProcessed to set
     */
    public void setDatumProcessed(Date datumProcessed) {
        this.datumProcessed = null;
        if (datumProcessed != null) {
            this.datumProcessed = (Date) datumProcessed.clone();
        }
    }

    /**
     * simple getter for dto.
     *
     * @return the datumProcessed
     */
    public Date getDatumProcessed() {
        if (datumProcessed != null) {
            return (Date) datumProcessed.clone();
        }
        return null;
    }

    /**
     * simple getter for DTO.
     *
     * @return the partner
     */
    public String getPartner() {
        return partner;
    }


    /**
     * simple getter for dto.
     *
     * @return the aantalVerwerkt
     */
    public int getAantalVerwerkt() {
        return aantalVerwerkt;
    }

    /**
     * simple setter for dto.
     *
     * @param aantalVerwerkt the aantalVerwerkt to set
     */
    public void setAantalVerwerkt(int aantalVerwerkt) {
        this.aantalVerwerkt = aantalVerwerkt;
    }

    /**
     * simple getter for dto.
     *
     * @return the aantalFouten
     */
    public int getAantalFouten() {
        return aantalFouten;
    }

    /**
     * simple setter for dto.
     *
     * @param aantalFouten the aantalFouten to set
     */
    public void setAantalFouten(int aantalFouten) {
        this.aantalFouten = aantalFouten;
    }

    /**
     * simple setter for dto.
     *
     * @param partner the partner to set
     */
    public void setPartner(String partner) {
        this.partner = partner;
    }

    /**
     * simple getter for dto.
     *
     * @return the options
     */
    public Set<UploadOption> getOptions() {
        return options;
    }

    /**
     * simple setter for dto.
     *
     * @param options the options to set
     */
    public void setOptions(Set<UploadOption> options) {
        this.options = options;
    }

    public UploadedFile getBestand() {
        return bestand;
    }

    public void setBestand(UploadedFile bestand) {
        this.bestand = bestand;
    }

    public Code getInvoerwijze() {
        return invoerwijze;
    }

    public void setInvoerwijze(Code invoerwijze) {
        this.invoerwijze = invoerwijze;
    }

    public Code getStatus() {
        return status;
    }

    public void setStatus(Code status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
