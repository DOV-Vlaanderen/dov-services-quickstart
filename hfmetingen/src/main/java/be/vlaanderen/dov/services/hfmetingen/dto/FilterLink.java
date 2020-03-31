
package be.vlaanderen.dov.services.hfmetingen.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class FilterLink {

    private String instrumentPermkey;

    private Integer sensorId;

    private String meetEenheid;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private Date van;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm")
    private Date tot;

    public String getInstrumentPermkey() {
        return instrumentPermkey;
    }

    public void setInstrumentPermkey(String instrumentPermkey) {
        this.instrumentPermkey = instrumentPermkey;
    }

    public Integer getSensorId() {
        return sensorId;
    }

    public void setSensorId(Integer sensorId) {
        this.sensorId = sensorId;
    }

    public String getMeetEenheid() {
        return meetEenheid;
    }

    public void setMeetEenheid(String meetEenheid) {
        this.meetEenheid = meetEenheid;
    }

    public Date getVan() {
        return van;
    }

    public void setVan(Date van) {
        this.van = van;
    }

    public Date getTot() {
        return tot;
    }

    public void setTot(Date tot) {
        this.tot = tot;
    }

}
