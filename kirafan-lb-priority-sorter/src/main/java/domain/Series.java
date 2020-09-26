package domain;

public class Series {
    private String nameEN;
    private String nameJP;
    private Crea crea;

    public Series(String nameEN, String nameJP, Crea crea) {
        this.nameEN = nameEN;
        this.nameJP = nameJP;
        this.crea = crea;
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

    public Crea getCrea() {
        return crea;
    }

    public void setCrea(Crea crea) {
        this.crea = crea;
    }
}
