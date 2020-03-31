
package be.vlaanderen.dov.services.hfmetingen.dto;

import java.time.format.DateTimeFormatter;

public class Meetpunt {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    public enum Meetstatus {
        GEVALIDEERD, NIET_GEVALIDEERD;
    }

    private String tijd;

    private Double waarde;

    private Meetstatus status;

    private Double plotWaarde;

    public Meetpunt() {
        super();
    }

    public Meetpunt(String tijd, Double waarde, Meetstatus status) {
        super();
        this.tijd = tijd;
        this.waarde = waarde;
        this.status = status;
    }

    public String getTijd() {
        return tijd;
    }

    public void setTijd(String tijd) {
        this.tijd = tijd;
    }

    public Double getWaarde() {
        return waarde;
    }

    public void setWaarde(Double waarde) {
        this.waarde = waarde;
    }

    public Meetstatus getStatus() {
        return status;
    }

    public void setStatus(Meetstatus status) {
        this.status = status;
    }

    public Double getPlotWaarde() {
        return plotWaarde;
    }

    public void setPlotWaarde(Double plotWaarde) {
        this.plotWaarde = plotWaarde;
    }

}
