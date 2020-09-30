package model;

public class Series {
    private String nameEN;
    private String nameJP;
    private CreaStatus creaStatus;

    public Series(String nameEN, String nameJP, CreaStatus creaStatus) {
        this.nameEN = nameEN;
        this.nameJP = nameJP;
        this.creaStatus = creaStatus;
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

    public CreaStatus getCrea() {
        return creaStatus;
    }

    public void setCrea(CreaStatus creaStatus) {
        this.creaStatus = creaStatus;
    }
}
