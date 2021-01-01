package domain;

public enum SkillTarget {
    ALLY_SELF("Self", "自身"), // Skill can only be targeted at self (at the caster)
    ALLY_SINGLE("Single ally", "味方単位"), // Skill can be cast on a single ally of choice (including the caster itself)
    ALLY_ALL("All allies", "味方全体"), // Skill is cast on all allies (including on the caster itself)
    ENEMY_SINGLE("Single enemy", "敵単位"), // Skill can only be casted on a single enemy
    ENEMY_ALL("All enemies", "敵全体"); // Skill is cast on all enemies

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
