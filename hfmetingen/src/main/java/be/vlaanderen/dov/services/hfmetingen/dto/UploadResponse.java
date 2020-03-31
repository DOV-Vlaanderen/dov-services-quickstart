
package be.vlaanderen.dov.services.hfmetingen.dto;

public class UploadResponse {

    private String status;

    private int aantal;

    private String foutmelding;
    private String importLogId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAantal() {
        return aantal;
    }

    public void setAantal(int aantal) {
        this.aantal = aantal;
    }

    public String getFoutmelding() {
        return foutmelding;
    }

    public void setFoutmelding(String foutmelding) {
        this.foutmelding = foutmelding;
    }

    public String getImportLogId() {
        return importLogId;
    }

    public void setImportLogId(String importLogId) {
        this.importLogId = importLogId;
    }

}
