// Copyright (C) 2010-2017 DOV, http://dov.vlaanderen.be/
// All rights reserved
package be.vlaanderen.dov.services.xmlimport.dto;


/**
 * Data transfer object that represents the Option pojo.
 *
 * @author DEBAETSP
 *
 */
public final class Code {


    private String code;

    private String beschrijving;

    /**
     * empty constructor.
     */
    public Code() {
    }

    /**
     * constructor based on the attributes.
     *
     * @param code
     *            the code
     * @param beschrijving
     *            the description
     */
    public Code(String code, String beschrijving) {
        this.code = code;
        this.beschrijving = beschrijving;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }
}
