
package be.vlaanderen.dov.services.hfmetingen.dto;

import java.util.List;

public class SensorMetingen {

    private String instrumentId;

    private String sensorId;

    private List<Meetpunt> meetdata;

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public List<Meetpunt> getMeetdata() {
        return meetdata;
    }

    public void setMeetdata(List<Meetpunt> meetdata) {
        this.meetdata = meetdata;
    }

}
