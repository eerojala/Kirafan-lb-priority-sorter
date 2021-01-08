package domain;

public enum SkillType {
    // *** BEGIN SKILLTYPES FOR BUFFS/DEBUFFS ***

    // Skill types which affect stats
    ATK("ATK", "ATK"), // Attack, stat which determines physical damage
    MAT("MAT", "MAT"), // Magic attack, stat which determines magical damage
    DEF("DEF", "DEF"), // Defense, stat which determines defense from physical damage
    MDF("MDF", "SPD"), // Magic defense, stat which determines defense from magical damage
    LUK("LUK", "LUK"), // Luck, stat which determines chance for critical damage (both dealing or receiving damage)
    SPD("SPD", "SPD"), // Speed, stat which determines how frequently a character gets to have a turn in the combat timeline

    // Skill types which affect elemental resistances
    FIRE_RESIST("Fire resistance", "火耐性"), // Reduces fire damage taken
    WIND_RESIST("Wind resistance", "風耐性"), // Reduces wind damage taken
    EARTH_RESIST("Earth resistance", "土耐性"), // Reduces earth damage taken
    WATER_RESIST("Water resistance", "水耐性"), // Reduces water damage taken
    MOON_RESIST("Moon resistance", "月耐性"), // Reduces moon damage taken
    SUN_RESIST("Sun resistance", "日耐性"), // Reduces sun damage taken

    // Skill types which affect other multipliers in damage calculation
    CRIT_DAMAGE("Critical damage", "クリティカルダメージ"), // Affect the damage of a critical hit
    NEXT_ATK("Next physical attack", "次回の物理攻撃"), // Affect damage for the next physical attack (dispels after 1 attack, other buffs/debuffs dispel usually after 3 attacks)
    NEXT_MAT("Next magical attack up", "次回の魔法攻撃"), // Affect damage for the next magical attack
    WEAK_ELEMENT_BONUS("Weak element bonus", "有利属性ボーナス"), // Increases damage done against an opponent with a element which is weak to the damage dealers element

    // *** END SKILL TYPES FOR BUFFS/DEBUFFS //

    // *** BEGIN SKILL TYPES FOR OTHER EFFECTS //

    // Skill types which cause abnormal effects (known as status affects in other games)
    CONFUSION("Confusion", "混乱"), // Causes enemies to take damage randomly instead of using a skills and player characters to select a random skill and random target (for skills which target one ally/enemy)
    ISOLATION("Isolation", "孤立"), // Causes the target to be unable to switch places with another party member
    HUNGER("Hunger", "腹ペコ"), // Causes the target to take damage at the end of their turn
    MISFORTUNE("Misfortune", "不幸"), // Causes all kinds of healing having no effect on the target
    PARALYSIS("Paralysis", "金縛り"), // Causes the target to randomly skip their turn
    SILENCE("Silence", "沈黙"), // Causes the target unable to use other skills than their default attack
    SLEEP("Sleep", "眠り"), // Causes the target to skip their turn until they take damage or wake up (after enough turns have passed)
    TIMID("Timid", "弱気"), // Causes all damage dealt to the target to be critical hits

    // Skill types which cause damage
    DAMAGE("Damage", "ダメージ"), // Deal physical or magical damage
    TOTTEOKI("Totteoki", "とっておき"), // A special attack, totteoki means along the lines of  "ace in the hole" in english (deals either physical or magical damage)

    // Skill types for misc. effects
    ABNORMAL_DISABLE("Disable abnormals", "状態異常無効"), // Gives character immunity from status effects
    ABNORMAL_RECOVER("Clear abnormals", "状態異常解除"), // Clears all abnormal effects from character
    BARRIER_FULL("Full barrier", "フルバリア"), // Blocks all of the received damage from the next attack
    BARRIER_FULL_TRIPLE("Full triple barrier", "三重フルバリア"), // Blocks all of the received damage from the 3 next attacks
    HEAL_CARD("Heal card", "回復カード"); // Places cards on the combat timeline which heal the party when activated


    // *** END SKILLTYPES FOR OTHER EFFECTS **

    // There are even more different types of skills in the actual game, but I consider only these to be relevant for this program
    // (Might add them later though if I change my mind)

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

    public static SkillType getAppropriateElementalResistance(CharacterElement element) {
        switch (element) {
            case FIRE:
                return SkillType.FIRE_RESIST;

            case WIND:
                return SkillType.WIND_RESIST;

            case EARTH:
                return SkillType.EARTH_RESIST;

            case WATER:
                return SkillType.WATER_RESIST;

            case MOON:
                return SkillType.MOON_RESIST;

            default:
                return SkillType.SUN_RESIST;
        }
    }

    public static boolean isBuffOrDebuff(SkillType skillType) {
        return isStat(skillType) || isElementalResist(skillType) || isOtherMultiplier(skillType);
    }

    public static boolean isOtherEffect(SkillType skillType) {
        return isAbnormalEffect(skillType) || isDamageEffect(skillType) || isMiscEffect(skillType);
    }

    public static boolean isStat(SkillType skillType) {
        return skillType == ATK || skillType == MAT || skillType == DEF || skillType == MDF || skillType == LUK || skillType == SPD;
    }

    public static boolean isOtherMultiplier(SkillType skillType) {
        return skillType == CRIT_DAMAGE || skillType == NEXT_ATK || skillType == NEXT_MAT || skillType == WEAK_ELEMENT_BONUS;
    }

    public static boolean isElementalResist(SkillType skilltype) {
        return skilltype == FIRE_RESIST || skilltype == WIND_RESIST || skilltype == EARTH_RESIST
                || skilltype == WATER_RESIST || skilltype == MOON_RESIST || skilltype == SUN_RESIST;
    }

    public static boolean isAbnormalEffect(SkillType skilltype) {
        return skilltype == CONFUSION || skilltype == ISOLATION || skilltype == HUNGER || skilltype == MISFORTUNE
                || skilltype == PARALYSIS || skilltype == SILENCE || skilltype == SLEEP || skilltype == TIMID;
    }

    public static boolean isDamageEffect(SkillType skillType) {
        return skillType == DAMAGE || skillType == TOTTEOKI;
    }

    public static boolean isMiscEffect(SkillType skillType) {
        return skillType == ABNORMAL_DISABLE || skillType == ABNORMAL_RECOVER || skillType == BARRIER_FULL
                || skillType == BARRIER_FULL_TRIPLE || skillType == HEAL_CARD;
    }

    @Override
    public String toString() {
        return nameEN;
    }
}
