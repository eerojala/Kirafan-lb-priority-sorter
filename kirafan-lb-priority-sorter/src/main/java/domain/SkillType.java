package domain;

public enum SkillType {
    // Skill types which affect stats
    ATK("ATK", "ATK"), // Stat which determines physical damage
    MAT("MAT", "MAT"), // Stat which determines magical damage
    DEF("DEF", "DEF"), // Stat which determines defense from physical damage
    MDF("MDF", "SPD"), // Stat which determines defense from magical damage
    LUK("LUK", "LUK"), // Stat which determines chance for critical damage (both dealing or receiving damage)
    CRIT_DAMAGE("Critical damage", "クリティカルダメージ"), // Affect the damage of a critical hit
    NEXT_ATK("Next physical attack up", "次回の物理攻撃の威力アップ"), // Affect ATK for next attack only
    NEXT_MAT("Next magical attack up", "次回の魔法攻撃の威力アップ"), // Affect MAT for next attack only
    ELEMENT_RESIST("Elemental resistance", "属性耐性"), // Reduces damage of a specific element

    // Skill types which cause/cure status affects
    CONFUSION("Confusion", "混乱"), // Causes enemies to take damage randomly instead of using a skills / player characters to select a random skill
    ISOLATION("Isolation", "孤立"), // Causes the target to be unable to switch places with another party member
    HUNGER("Hunger", "腹ペコ"), // Causes the target to take damage at the end of their turn
    MISFORTUNE("Misfortune", "不幸"), // Causes all kinds of healing having no effect on the target
    PARALYSIS("Paralysis", "金縛り"), // Causes the target to randomly skip their turn
    SILENCE("Silence", "沈黙"), // Causes the target unable to use other skills than their default attack
    SLEEP("Sleep", "眠り"), // Causes the target to skip their turn until they take damage
    TIMID("Timid", "弱気"), // Causes all damage dealt to the target to be critical hits
    STATUS_EFFECT_CLEAR("Clear status effects", "状態異常解除"), // Clears all status effects

    // Skill types for other effects
    BARRIER("Barrier", "バリア"), // Blocks all of the received damage from the next attack
    BARRIER_TRIPLE("Triple barrier", "三重バリア"), // Blocks all of the received damage from the 3 next attacks
    DAMAGE("Damage", "ダメージ"); // Deal physical or magical damage

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
