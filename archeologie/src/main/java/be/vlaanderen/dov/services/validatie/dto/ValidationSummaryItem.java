// Copyright (C) 2010-2020 DOV, http://dov.vlaanderen.be/
// All rights reserved
package be.vlaanderen.dov.services.validatie.dto;

/**
 * @author Patrick De Baets
 *
 */
public class ValidationSummaryItem  {

    private Code option;

    private int numberOfObjects;

    public ValidationSummaryItem() {
        super();
    }

    public ValidationSummaryItem(Code option) {
        super();
        this.option = option;
    }

    public ValidationSummaryItem(Code option, int numberOfObjects) {
        super();
        this.option = option;
        this.numberOfObjects = numberOfObjects;
    }

    public void plusOne() {
        numberOfObjects++;
    }

    public Code getOption() {
        return option;
    }

    public void setOption(Code option) {
        this.option = option;
    }

    public int getNumberOfObjects() {
        return numberOfObjects;
    }

    public void setNumberOfObjects(int numberOfObjects) {
        this.numberOfObjects = numberOfObjects;
    }


}
