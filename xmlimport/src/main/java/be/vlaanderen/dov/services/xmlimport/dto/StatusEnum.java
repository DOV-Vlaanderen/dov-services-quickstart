// Copyright (C) 2010-2013 DOV, http://dov.vlaanderen.be/
// All rights reserved
package be.vlaanderen.dov.services.xmlimport.dto;

/**
 * Convenience Enum to handle statussen.
 *
 * @author DEBAETSP
 *
 */
public enum StatusEnum {

    // status voor globale import
    NIET_VERWERKT("1", "niet verwerkt"),

    IN_VERWERKING("2", "in verwerking"),

    VERWERKT_MET_FOUTEN("3", "verwerkt met fouten"),

    VERWERKT_ZONDER_FOUTEN("4", "verwerkt zonder fouten"),

    VERWERKT_MET_WARNING("5", "verwerkt met waarschuwingen"),

    GEVALIDEERD_MET_FOUTEN("6", "validatie met fouten"),

    GEVALIDEERD_ZONDER_FOUTEN("7", "validatie zonder fouten"),

    GEVALIDEERD_MET_WARNING("8", "validatie met waarschuwingen");

    private String code;

    private String beschrijving;

    /**
     * getter.
     *
     * @return the code
     */
    public String code() {
        return this.code;
    }

    /**
     * getter.
     *
     * @return the description
     */
    public String beschrijving() {
        return this.beschrijving;
    }

    /**
     * private constructor.
     *
     * @param code
     * @param beschrijving
     */
    private StatusEnum(String code, String beschrijving) {
        this.code = code;
        this.beschrijving = beschrijving;
    }


    public static StatusEnum fromCode(String code) {
        StatusEnum[] arr = values();

        for (StatusEnum c : arr) {
            if (c.code.equals(code)) {
                return c;
            }
        }
        throw new IllegalArgumentException(code);
    }

    public static boolean isFinalState(String code) {
        StatusEnum v = fromCode(code);
        return v == VERWERKT_MET_FOUTEN || v == VERWERKT_MET_WARNING || v == StatusEnum.VERWERKT_ZONDER_FOUTEN;
    }
}
