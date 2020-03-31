// Copyright (C) 2010-2020 DOV, http://dov.vlaanderen.be/
// All rights reserved
package be.vlaanderen.dov.services.validatie.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Gives a summary of the validated objects.
 *
 * @author Patrick De Baets
 *
 */
public class ValidationSummary {

    private List<ValidationSummaryItem> items = new ArrayList<ValidationSummaryItem>();

    public ValidationSummary() {

    }

    public List<ValidationSummaryItem> getItems() {
        return items;
    }

    public void setItems(List<ValidationSummaryItem> items) {
        this.items = items;
    }

}
