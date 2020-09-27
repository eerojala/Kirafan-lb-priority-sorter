package domain;

public enum SkillTarget {
    SELF("Self", "自身"),
    ENEMY_SINGLE("Single enemy", "敵単位"),
    ENEMY_ALL("All enemies", "敵全体"),
    ALLIES_SINGLE("Single ally", "味方単位"), // "Single ally" skills can also be targeted on self
    ALLIES_ALL("All allies", "味方全体"); // "All allies" skills also affect self

    private final String nameEN;
    private final String nameJP;

    private SkillTarget(String nameEN, String nameJP) {
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
