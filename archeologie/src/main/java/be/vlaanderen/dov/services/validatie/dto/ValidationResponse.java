// Copyright (C) 2010-2020 DOV, http://dov.vlaanderen.be/
// All rights reserved
package be.vlaanderen.dov.services.validatie.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * The result of the validation of an uploaded xml.
 * 
 * @author Patrick De Baets
 */
public class ValidationResponse {

    private List<Detail> details = new ArrayList<Detail>();

    private ValidationSummary summary;

    public ValidationResponse() {
        summary = new ValidationSummary();
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public ValidationSummary getSummary() {
        return summary;
    }

    public void setSummary(ValidationSummary summary) {
        this.summary = summary;
    }

}
