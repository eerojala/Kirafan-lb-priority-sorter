package domain;

public enum CreaStatus {
    COMPLETE("Complete", "完成"),
    INCOMPLETE("Incomplete", "未完成"),
    NONE("None", "無し");

    private final String nameEN;
    private final String nameJP;

    private CreaStatus(String nameEN, String nameJP) {
        this.nameEN = nameEN;
        this.nameJP = nameJP;
    }

    public String getNameEN() {
        return nameEN;
    }

    public String getNameJP() {
        return nameJP;
    }

    @Override
    public String toString() {
        return nameEN;
    }
}
