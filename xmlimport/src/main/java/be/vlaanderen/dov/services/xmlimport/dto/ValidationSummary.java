// Copyright (C) 2010-2017 DOV, http://dov.vlaanderen.be/
// All rights reserved
package be.vlaanderen.dov.services.xmlimport.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Gives a summary of the validated objects.
 *
 * @author DEBAETSP
 *
 */
public class ValidationSummary {

    private List<ValidationSummaryItem> items = new ArrayList<ValidationSummaryItem>();

    public ValidationSummary() {

    }


    public void addOption(Code option) {
        if (!hasOption(option)) {
            ValidationSummaryItem item = new ValidationSummaryItem();
            item.setOption(new Code(option.getCode(), option.getBeschrijving()));
            items.add(item);
        }
    }

    public void addItem(Code option) {
        for (ValidationSummaryItem item : items) {
            if (item.getOption().getCode().equals(option.getCode())) {
                item.plusOne();
            }
        }
    }

    public List<ValidationSummaryItem> getItems() {
        return items;
    }

    public void setItems(List<ValidationSummaryItem> items) {
        this.items = items;
    }



    private boolean hasOption(Code option) {
        for (ValidationSummaryItem item : items) {
            if (item.getOption().getCode().equals(option.getCode())) {
                return true;
            }
        }
        return false;
    }

}
