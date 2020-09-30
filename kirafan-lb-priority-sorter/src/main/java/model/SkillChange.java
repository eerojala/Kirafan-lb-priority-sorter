package model;
// Enum for representing whether a skill has an increasing or a decreasing effect, e.g. "MAT UP, DEF DOWN";
public enum SkillChange {
    UP("Up", "アップ"),
    DOWN("Down", "ダウン");

    private final String nameEN;
    private final String nameJP;

    private SkillChange(String nameEN, String nameJP) {
        this.nameEN = nameEN;
        this.nameJP = nameJP;
    }

    public String getNameEN() {
        return nameEN;
    }

    public String getNameJP() {
        return nameJP;
    }
}
