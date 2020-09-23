package domain;

public class Series {
    private final int id;
    private String nameEN;
    private String nameJP;
    private boolean hasUnfinishedCrea;

    public Series(int id, String nameEN, String nameJP, boolean hasUnfinishedCrea) {
        this.id = id;
        this.nameEN = nameEN;
        this.nameJP = nameJP;
        this.hasUnfinishedCrea = hasUnfinishedCrea;
    }

    public Series(int id, String nameEN, String nameJP) {
        this(id, nameEN, nameJP, false);
    }

    public int getId() {
        return id;
    }

    public String getNameEN() {
        return nameEN;
    }

    public String getNameJP() {
        return nameJP;
    }

    public boolean hasUnfinishedCrea() {
        return hasUnfinishedCrea;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public void setNameJP(String nameJP) {
        this.nameJP = nameJP;
    }

    public void setHasUnfinishedCrea(boolean hasUnfinishedCrea) {
        this.hasUnfinishedCrea = hasUnfinishedCrea;
    }
}
