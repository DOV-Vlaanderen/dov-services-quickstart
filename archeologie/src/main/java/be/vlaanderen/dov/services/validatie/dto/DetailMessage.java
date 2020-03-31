// Copyright (C) 2010-2020 DOV, http://dov.vlaanderen.be/
// All rights reserved
package be.vlaanderen.dov.services.validatie.dto;

/**
 * Data transfer object that represents an DetaillogMessage.
 *
 *  @author Patrick De Baets
 *
 */
public final class DetailMessage {

    private String id;

    private Code severity;

    private String hierarchy;

    private String message;

    private String info;

    /**
     * simple getter.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * simple setter.
     *
     * @param id
     *            the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * simple getter.
     *
     * @return the severity
     */
    public Code getSeverity() {
        return severity;
    }

    /**
     * simple setter.
     *
     * @param severity
     *            the severity to set
     */
    public void setSeverity(Code severity) {
        this.severity = severity;
    }

    /**
     * simple getter.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * simple setter.
     *
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * @return the hierarchy
     */
    public String getHierarchy() {
        return hierarchy;
    }


    /**
     * @param hierarchy the hierarchy to set
     */
    public void setHierarchy(String hierarchy) {
        this.hierarchy = hierarchy;
    }


    /**
     * @return the info
     */
    public String getInfo() {
        return info;
    }


    /**
     * @param info the info to set
     */
    public void setInfo(String info) {
        this.info = info;
    }

}
