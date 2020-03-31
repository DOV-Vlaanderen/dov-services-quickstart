
package be.vlaanderen.dov.services.hfmetingen.dto;

public class UploadMessage {

    public enum Severity {
        FATAL;
    }

    private int id;

    private Severity severity;

    private String message;

    private int volgnummer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getVolgnummer() {
        return volgnummer;
    }

    public void setVolgnummer(int volgnummer) {
        this.volgnummer = volgnummer;
    }

}
