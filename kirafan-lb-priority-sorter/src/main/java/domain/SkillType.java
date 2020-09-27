package domain;

public enum SkillType {
    ATK("ATK", "ATK"), // Stat which determines physical damage
    MAT("MAT", "MAT"), // Stat which determines magical damage
    DEF("DEF", "DEF"), // Stat which determines defense from physical damage
    MDF("MDF", "SPD"), // Stat which determines defense from magical damage
    LUK("LUK", "LUK"), // Stat which determines chance for critical damage (both dealing or receiving damage)
    NEXT_ATK("Next physical attack up", "次回の物理攻撃の威力アップ"), // Affect ATK for next attack
    NEXT_MAT("Next magical attack up", "次回の魔法攻撃の威力アップ"), // Affect MAT for next attack
    ELEMENT_RESIST("Elemental resistance", "属性耐性"), // Reduces damage of a specific or all elements
    BARRIER("Barrier", "バリア"), // Blocks all of the received damage from the next attack
    BARRIER_TRIPLE("Triple barrier", "三重バリア"), // Blocks all of the received damage from the 3 next attacks
    STATUS_EFFECTS_CLEAR("Clear status effects", "状態異常解除"); // Clears all negative status effects

    private final String nameEN;
    private final String nameJP;

    private SkillType(String nameEN, String nameJP) {
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
