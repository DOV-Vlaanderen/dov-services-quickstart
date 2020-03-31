
package be.vlaanderen.dov.services.hfmetingen.dto;

import java.util.ArrayList;
import java.util.List;

public class UploadRequest {

    public enum HFStatus {
        NIET_VERWERKT, IN_VERWERKING,VERWERKT_MET_FOUTEN,VERWERKT_ZONDER_FOUTEN,
        GEVALIDEERD_MET_FOUTEN,GEVALIDEERD_ZONDER_FOUTEN;

        public boolean isFinalState() {
            return this == VERWERKT_MET_FOUTEN || this == VERWERKT_ZONDER_FOUTEN || this == GEVALIDEERD_MET_FOUTEN;
        }
    }

    private int id;

    private int sensorId;

    private String uploadTime;

    private String auteur;

    private Bestand bestand;

    private HFStatus status;

    private String startVerwerking;

    private String eindVerwerking;

    private List<UploadMessage> messages = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public Bestand getBestand() {
        return bestand;
    }

    public void setBestand(Bestand bestand) {
        this.bestand = bestand;
    }

    public HFStatus getStatus() {
        return status;
    }

    public void setStatus(HFStatus status) {
        this.status = status;
    }

    public String getStartVerwerking() {
        return startVerwerking;
    }

    public void setStartVerwerking(String startVerwerking) {
        this.startVerwerking = startVerwerking;
    }

    public String getEindVerwerking() {
        return eindVerwerking;
    }

    public void setEindVerwerking(String eindVerwerking) {
        this.eindVerwerking = eindVerwerking;
    }

    public List<UploadMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<UploadMessage> messages) {
        this.messages = messages;
    }

}
