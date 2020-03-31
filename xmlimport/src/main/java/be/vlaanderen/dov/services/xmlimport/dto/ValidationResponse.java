// Copyright (C) 2010-2013 DOV, http://dov.vlaanderen.be/
// All rights reserved
package be.vlaanderen.dov.services.xmlimport.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DEBAETSP
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
