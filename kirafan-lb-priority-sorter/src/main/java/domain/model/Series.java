package domain.model;

import domain.CreaStatus;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

@Document(collection = "series", schemaVersion = "1.0")
public class Series {
    @Id
    private String id;
    private String nameEN;
    private String nameJP;
    private CreaStatus creaStatus;

    // Jackson requires a public constructor with no parameters
    public Series() {}

    public Series(String nameEN, String nameJP, CreaStatus creaStatus) {
        this.nameEN = nameEN;
        this.nameJP = nameJP;
        this.creaStatus = creaStatus;
        this.id = this.nameEN;
    }

    public String getId() {
        return this.id;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getNameJP() {
        return nameJP;
    }

    public void setNameJP(String nameJP) {
        this.nameJP = nameJP;
    }

    public CreaStatus getCreaStatus() {
        return creaStatus;
    }

    public void setCreaStatus(CreaStatus creaStatus) {
        this.creaStatus = creaStatus;
    }
}
