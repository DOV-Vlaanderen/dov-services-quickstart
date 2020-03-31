// Copyright (C) 2010-2013 DOV, http://dov.vlaanderen.be/
// All rights reserved

package be.vlaanderen.dov.services.xmlimport.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum that maps the optionCode from the userinterface to a xmltag.
 *
 * @author DEBAETSP
 */
public enum OptionEnum {

    // generic
    UNKNOWN_OPTION("0", false),

    CREATE_ORGANISATIE("1", false),

    CREATE_ACTOR("2", false),

    OCDOV("3", false),

    // opdracht and sub-elements
    OPDRACHT("10", true), OPDRACHT_OPMERKING("11", false),

    // boring and sub-elements
    BORING("20", true), BORING_OPMERKING("21", false), BORING_ALTERNATIEF("22", false),

    // grondmonster en sub-elements
    GRONDMONSTER("23", true), GRONDMONSTER_ONDERKENNING("24", false), GRONDMONSTER_KORRELVERDELING("25",
            false), GRONDMONSTER_MECHANISCH("26", false), GRONDMONSTER_STEENKOOL("27", false), GRONDMONSTER_CHEMISCH(
                    "28", false), GRONDMONSTER_OPMERKING("29", false), GRONDMONSTER_PROEVEN("30", false),

    // sondering and sub-elements
    SONDERING("40", true), SONDERING_OPMERKING("41", false),

    // put/filter and sub-elements
    PUT("50", true), PUT_OPMERKING("51", false), FILTER("60", true), FILTER_OPMERKING("61", false), FILTER_PEILMETING(
            "62", true), FILTER_ONTTREKKING("65", true), FILTER_GXG("66", true), WATERMONSTER("63", true),

    // interpretatie and sub-elements
    INTERPRETATIE_INFORMEEL("101", true), INTERPRETATIE_FORMEEL("102", true), INTERPRETATIE_LITHOLOGIE("103",
            true), INTERPRETATIE_GECODEERD("104", true), INTERPRETATIE_HYDRO("105", true), INTERPRETATIE_QUARTAIR("106",
                    true), INTERPRETATIE_GEOTECHNISCH("108",
                            true), INTERPRETATIE_INFORMELE_HYDRO("109", true), INTERPRETATIE_OPMERKING("120", false);

    public static final Map<String, OptionEnum> OPTION_MAP = new HashMap<String, OptionEnum>();

    static {
        for (OptionEnum item : OptionEnum.values()) {
            OPTION_MAP.put(item.option(), item);
        }
    }

    private String optionCode;

    private boolean mainObject;

    /**
     * getter.
     *
     * @return the option code
     */
    public String option() {
        return optionCode;
    }

    /**
     * getter.
     *
     * @return the xml tag
     */
    public boolean mainObject() {
        return mainObject;
    }

    /**
     * private constructor
     *
     * @param optioncode
     *            code
     * @param mainObject
     *            Is this an object or an attachment to one of the objects?
     */
    private OptionEnum(String optioncode, boolean mainObject) {
        this.optionCode = optioncode;
        this.mainObject = mainObject;
    }

    /**
     * construct an option from the given code.
     *
     * @return constructed Option. Or {@link IllegalArgumentException} if the code doesn't map to an existing option.
     */
    public static OptionEnum fromCode(String optioncode) {
        if (OPTION_MAP.containsKey(optioncode)) {
            return OPTION_MAP.get(optioncode);
        }
        return UNKNOWN_OPTION;
    }

    /**
     * construct an {@link Code}.
     */
    public UploadOption asOption() {
        return new UploadOption(option(), name());
    }
}
