// Copyright (C) 2010-2020 DOV, http://dov.vlaanderen.be/
// All rights reserved
package be.vlaanderen.dov.services.validatie.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer object that represents a DetailLog.
 *
 * @author Patrick De Baets
 */
public class Detail {

    private String id;

    private Code status;

    private Bodemlocatie bodemlocatie;

    private List<DetailMessage> messages = new ArrayList<DetailMessage>();

    /**
     * simple getter.
     *
     * @return the id
     */
    public final String getId() {
        return id;
    }

    /**
     * simple setter.
     *
     * @param id the id to set
     */
    public final void setId(String id) {
        this.id = id;
    }

    /**
     * simple getter.
     *
     * @return the messages
     */
    public final List<DetailMessage> getMessages() {
        return messages;
    }

    /**
     * simple setter.
     *
     * @param messages the messages to set
     */
    public final void setMessages(List<DetailMessage> messages) {
        this.messages = messages;
    }

    /**
     * simple setter.
     *
     * @param message the message to add
     */
    public final void addMessage(DetailMessage message) {
        boolean alreadyAvailable = false;
        if (message.getHierarchy() != null && message.getMessage() != null) {
            for (DetailMessage m : messages) {
                if (message.getHierarchy().equals(m.getHierarchy())
                        && message.getMessage().equals(m.getMessage())) {
                    alreadyAvailable = true;
                    break;
                }
            }
        }
        if (!alreadyAvailable) {
            this.messages.add(message);
        }
    }

    public Code getStatus() {
        return status;
    }

    public void setStatus(Code status) {
        this.status = status;
    }


    public Bodemlocatie getBodemlocatie() {
        return bodemlocatie;
    }


    public void setBodemlocatie(Bodemlocatie bodemlocatie) {
        this.bodemlocatie = bodemlocatie;
    }

}
