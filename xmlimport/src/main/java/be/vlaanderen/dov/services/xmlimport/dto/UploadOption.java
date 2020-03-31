// Copyright (C) 2010-2017 DOV, http://dov.vlaanderen.be/
// All rights reserved
package be.vlaanderen.dov.services.xmlimport.dto;


/**
 * Data transfer object that represents the InvoerOptie pojo.
 *
 * @author DEBAETSP
 */

public final class UploadOption {

    private Code property;

    private String propertyValue;

    public UploadOption() {
    }

    public UploadOption(String code, String beschrijving) {
        propertyValue = Boolean.TRUE.toString();
        property = new Code(code, beschrijving);
    }


    /**
     * simple getter.
     *
     * @return the property
     */
    public Code getProperty() {
        return property;
    }

    /**
     * simple setter.
     *
     * @param property the property to set
     */
    public void setProperty(Code property) {
        this.property = property;
    }

    /**
     * simple getter.
     *
     * @return the propertyValue
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**
     * simple setter.
     *
     * @param propertyValue the propertyValue to set
     */
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    /**
     * convenience method to convert the property to a boolean.
     *
     * @return true of false, according to Boolean.parseBoolean
     */
    public boolean isOptieActivated() {
        boolean result = false;
        if (this.propertyValue != null) {
            result = Boolean.parseBoolean(this.propertyValue);
        }
        return result;
    }

}
