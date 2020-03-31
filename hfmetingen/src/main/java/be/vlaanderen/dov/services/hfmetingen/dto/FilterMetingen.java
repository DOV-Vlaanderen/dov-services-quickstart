
package be.vlaanderen.dov.services.hfmetingen.dto;

import java.util.ArrayList;
import java.util.List;

public class FilterMetingen {

    private String putPermkey;

    private String filterPermkey;

    private String filterNummer;

    private String sensorType;

    private String plotEenheid;

    private List<FilterLink> links = new ArrayList<>();

    private List<Meetpunt> meetdata = new ArrayList<>();

    public String getPutPermkey() {
        return putPermkey;
    }

    public void setPutPermkey(String putPermkey) {
        this.putPermkey = putPermkey;
    }

    public String getFilterPermkey() {
        return filterPermkey;
    }

    public void setFilterPermkey(String filterPermkey) {
        this.filterPermkey = filterPermkey;
    }

    public String getFilterNummer() {
        return filterNummer;
    }

    public void setFilterNummer(String filterNummer) {
        this.filterNummer = filterNummer;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public String getPlotEenheid() {
        return plotEenheid;
    }

    public void setPlotEenheid(String plotEenheid) {
        this.plotEenheid = plotEenheid;
    }

    public List<Meetpunt> getMeetdata() {
        return meetdata;
    }

    public void setMeetdata(List<Meetpunt> meetdata) {
        this.meetdata = meetdata;
    }

    public List<FilterLink> getLinks() {
        return links;
    }

    public void setLinks(List<FilterLink> links) {
        this.links = links;
    }

}
