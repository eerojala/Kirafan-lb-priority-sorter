package domain;

public enum SkillType {
    // Skill types which affect stats
    ATK("ATK", "ATK"), // Attack, stat which determines physical damage
    MAT("MAT", "MAT"), // Magic attack, stat which determines magical damage
    DEF("DEF", "DEF"), // Defense, stat which determines defense from physical damage
    MDF("MDF", "SPD"), // Magic defense, stat which determines defense from magical damage
    LUK("LUK", "LUK"), // Luck, stat which determines chance for critical damage (both dealing or receiving damage)
    SPD("SPD", "SPD"), // Speed, stat which determines how frequently a character gets to have a turn in the combat timeline

    // Skill types which affect damage directly
    CRIT_DAMAGE("Critical damage", "クリティカルダメージ"), // Affect the damage of a critical hit
    NEXT_ATK("Next physical attack up", "次回の物理攻撃の威力アップ"), // Affect ATK for next attack only
    NEXT_MAT("Next magical attack up", "次回の魔法攻撃の威力アップ"), // Affect MAT for next attack only
    WEAK_ELEMENT_BONUS("Weak element bonus", "有利属性ボーナス"), // Increases damage done against an opponent with a element which is weak to the damage dealers element

    // Skill types which affect elemental resistances
    FIRE_RESIST("Fire resistance", "火耐性"), // Reduces fire damage taken
    WIND_RESIST("Wind resistance", "風耐性"), // Reduces wind damage taken
    EARTH_RESIST("Earth resistance", "土耐性"), // Reduces earth damage taken
    WATER_RESIST("Water resistance", "水耐性"), // Reduces water damage taken
    MOON_RESIST("Moon resistance", "月耐性"), // Reduces moon damage taken
    SUN_RESIST("Sun resistance", "日耐性"), // Reduces sun damage taken

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
    DAMAGE("Damage", "ダメージ"), // Deal physical or magical damage
    HEAL_CARD("Heal card", "回復カード"), // Places cards on the combat timeline which heal the party when activated
    TOTTEOKI("Totteoki", "とっておき"); // A special attack, totteoki means along the lines of  "ace in the hole" in english
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

    public static boolean isElementalResist(SkillType skilltype) {
        return skilltype == SkillType.FIRE_RESIST || skilltype == SkillType.WIND_RESIST || skilltype == SkillType.EARTH_RESIST
                || skilltype == SkillType.WATER_RESIST || skilltype == SkillType.MOON_RESIST || skilltype == SkillType.SUN_RESIST;
    }

    public static boolean isStatusEffect(SkillType skilltype) {
        return skilltype == SkillType.CONFUSION || skilltype == SkillType.ISOLATION || skilltype == SkillType.HUNGER
                || skilltype == SkillType.MISFORTUNE || skilltype == SkillType.PARALYSIS || skilltype == SkillType.SILENCE
                || skilltype == SkillType.SLEEP || skilltype == SkillType.TIMID;
    }
}
