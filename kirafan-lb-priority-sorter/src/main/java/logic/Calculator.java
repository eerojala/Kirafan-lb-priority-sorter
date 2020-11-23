package logic;

import domain.*;
import domain.model.GameCharacter;
import domain.model.Weapon;

import java.util.*;

public final class Calculator {
    private Calculator() {}

    public static long calculateDamage(GameCharacter chara) {
        /*
        * Formula for damage calculation:
        * (character's base offensive power * skill power * offensive stat buff multiplier * next attack buff multiplier *
        * elemental multiplier * critical damage multiplier * totteoki chain position multiplier * random value from 0.85
        * to 1.0) / (enemy's base defensive power * defensive stat buff multiplier * 0.06)
        *
        * The function assumes that the damage is done with the character's totteoki skill (a special attack which deals
        * a lot of damage), so skill power is referred to totteoki power in the function. (Totteoki means along the lines
        * of 'ace in the hole' in japanese)
        *
        * The function assumes that totteoki chain position is 1st (multiplier: 1.0) and the random value is max (
        * multiplier: 1.0x), so they are omitted from the calculation
        *
        * The function also assumes that the enemy's base defensive stat is 200 (MDF against mages and DEF against
        * warriors).
        *
        * https://kirarafantasia.miraheze.org/wiki/Game_Mechanics#Damage_Calculation (The miraheze kirafan wiki is in
        * english, but it omits some details from the damage calculation which can be found in kirafan.moe's documentation)
        *
        * https://calc.kirafan.moe/#/document/0 (kirafan.moe documents are available either in japanese or (mostly) in
        * chinese, but one should be able to make sense of it with google translate)
        *
        * */
        CharacterClass charaClass = chara.getCharacterClass();

        if (!(charaClass == CharacterClass.MAGE || charaClass == CharacterClass.WARRIOR)) {
            return 0; // The damage calculator should only be used for mages and warriors
        }

        Map<Skill, Double> skillTotalAmounts = Mapper.getSkillTotalAmounts(chara);

        // Offensive modifiers
        int baseOffensivePower = getBaseOffensivePower(chara);
        double totteokiPower = getTotteokiPower(skillTotalAmounts);
        double offensiveStatBuffMultiplier = getOffensiveStatBuffMultiplier(charaClass, skillTotalAmounts);
        double nextAttackBuffMultiplier = getNextAttackUpBuffMultiplier(charaClass, skillTotalAmounts);
        double elementalMultiplier = getElementMultiplier(chara.getCharacterElement(), skillTotalAmounts);
        double criticalDamageMultiplier = getCriticalDamageMultiplier(skillTotalAmounts);

        // Defensive modifiers
        double baseDefensivePower = 200;
        double defensiveStatBuffMultiplier = getDefensiveStatBuffMultiplier(charaClass, skillTotalAmounts);
        double constant = 0.06;

        double offense = baseOffensivePower * totteokiPower * offensiveStatBuffMultiplier * nextAttackBuffMultiplier
                * elementalMultiplier * criticalDamageMultiplier;
        double defense = baseDefensivePower * defensiveStatBuffMultiplier * constant;

        return Math.round(offense / defense);
    }



    private static int getBaseOffensivePower(GameCharacter chara) {
        /*
        * Formula for base offensive power:
        * characters base offensive stat + equipped weapons offensive stat
        * */

        int charaBaseOffenseStat = chara.getOffensivePower();
        Weapon charaWeapon = chara.getPreferredWeapon();

        if (charaWeapon == null) {
            return charaBaseOffenseStat;
        } else {
            return charaBaseOffenseStat + charaWeapon.getOffensivePower();
        }
    }

    private static double getTotteokiPower(Map<Skill, Double> skillTotalAmounts) {
        /*
        * Currently every 5* mage has a totteoki which does damage to all enemies and every 5* warrior has a totteoki
        * which does damage to a single enemy.
        *
        * However if this changes in the future, such as if there is a mage who does single target damage or there is a
        * warrior that does enemywide damage, or if there is a mage or a warrior with a totteoki which does no damage at
        * all, this function should be able to handle that without crashing
        */

        Double enemyWideTotteokiPower = skillTotalAmounts.get(new Skill(SkillType.TOTTEOKI, null, SkillTarget.ENEMY_ALL, 0));
        Double singleEnemyTotteokiPower = skillTotalAmounts.get(new Skill(SkillType.TOTTEOKI, null, SkillTarget.ENEMY_SINGLE, 0));

        // Totteokis are stored as percentages, and need to be converted to decimal numbers for calculation
        if (enemyWideTotteokiPower == null && singleEnemyTotteokiPower == null) {
            return 0;
        } else if (enemyWideTotteokiPower == null) {
            return singleEnemyTotteokiPower / 100;
        } else {
            return enemyWideTotteokiPower / 100;
        }
    }

    private static double getOffensiveStatBuffMultiplier(CharacterClass charaClass, Map<Skill, Double> skillTotalAmounts) {
        /*
        * Formula for the offensive stat buff multiplier:
        * (1 + atk/mat buff - atk/mat debuff) Max: 2.5, Min: 0.5
        *
        * Mages use MAT, warriors use ATK
        *
        * https://calc.kirafan.moe/#/document/2
        */

        double offensiveStatBuffMultiplier = charaClass == CharacterClass.MAGE ?
                1 + sumCharacterBuffs(SkillType.MAT, skillTotalAmounts) - sumCharacterDebuffs(SkillType.MAT, skillTotalAmounts) :
                1 + sumCharacterBuffs(SkillType.ATK, skillTotalAmounts) - sumCharacterDebuffs(SkillType.ATK, skillTotalAmounts);

        offensiveStatBuffMultiplier = Math.max(offensiveStatBuffMultiplier, 0.5);

        return Math.min(offensiveStatBuffMultiplier, 2.5);
    }

    private static double sumCharacterBuffs(SkillType type, Map<Skill, Double> skillTotalAmounts) {
        Double selfBuffs = skillTotalAmounts.get(new Skill(type, SkillChange.UP, SkillTarget.SELF, 0));
        selfBuffs = selfBuffs == null ? 0 : selfBuffs;

        Double singleTargetBuffs = skillTotalAmounts.get(new Skill(type, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0));
        singleTargetBuffs = singleTargetBuffs == null ? 0 : singleTargetBuffs;

        Double allyWideBuffs = skillTotalAmounts.get(new Skill(type, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        allyWideBuffs = allyWideBuffs == null ? 0 : allyWideBuffs;


        return (selfBuffs + singleTargetBuffs + allyWideBuffs) / 100;
    }

    private static double sumCharacterDebuffs(SkillType type, Map<Skill, Double> skillTotalAmounts) {
        Double selfDebuffs = skillTotalAmounts.get(new Skill(type, SkillChange.DOWN, SkillTarget.SELF, 0));
        selfDebuffs = selfDebuffs == null ? 0 : selfDebuffs;

        Double singleTargetDebuffs = skillTotalAmounts.get(new Skill(type, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 0));
        singleTargetDebuffs = singleTargetDebuffs == null ? 0 : singleTargetDebuffs;

        Double allyWideDebuffs = skillTotalAmounts.get(new Skill(type, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0));
        allyWideDebuffs = allyWideDebuffs == null ? 0 : allyWideDebuffs;

        // Buffs and debuffs are stored as percentages, and need to be converted to decimal numbers for calculation
        return (selfDebuffs + singleTargetDebuffs + allyWideDebuffs) / 100;
    }

    private static double getNextAttackUpBuffMultiplier(CharacterClass charaClass, Map<Skill, Double> skillTotalAmounts) {
        /*
        * Formula for the next attack buff multiplier:
        * 1 + next attack buff
        *
        * Mages use MAT, warriors use ATK
        *
        * Unlike other buffs, next attack buffs do not stack and instead overwrite each other
        *
        * https://calc.kirafan.moe/#/document/11
        */

        if (charaClass == CharacterClass.MAGE) {
            return 1 + getStrongestBuff(SkillType.NEXT_MAT, skillTotalAmounts);
        } else {
            return 1 + getStrongestBuff(SkillType.NEXT_ATK, skillTotalAmounts);
        }
    }

    private static double getStrongestBuff(SkillType type, Map<Skill, Double> skillTotalAmounts) {
        // Used to determine the strongest next ATK/MAT buff, since unlike other buffs they do not stack and instead overwrite
        Double selfBuffs = skillTotalAmounts.get(new Skill(type, SkillChange.UP, SkillTarget.SELF, 0));
        selfBuffs = selfBuffs == null ? 0 : selfBuffs;

        Double singleTargetBuffs = skillTotalAmounts.get(new Skill(type, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0));
        singleTargetBuffs = singleTargetBuffs == null ? 0 : singleTargetBuffs;

        Double allyWideBuffs = skillTotalAmounts.get(new Skill(type, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        allyWideBuffs = allyWideBuffs == null ? 0 : allyWideBuffs;

        // Buffs and debuffs are stored as percentages, and need to be converted to decimal numbers for calculation
        return (Math.max(selfBuffs, Math.max(singleTargetBuffs, allyWideBuffs))) / 100;
    }


    private static double getElementMultiplier(CharacterElement charaElement, Map<Skill, Double> skillTotalAmounts) {
        /*
        * Formula for the element multiplier:
        * [Initial value * (1 - enemy's element resist buff + enemy's element debuff)] + weak element bonus
        *
        * Initial value is 2.0, 0.5 and 1.0 for weak, strong and regular element respectively.
        * The calculator assumes that the enemy's element is weak against the character, so the function uses 2.0
        *
        * The max value for the portion in the square brackets is 2.4, 0.9 and 1.4 for weak, strong and regular element respectively
        * THe min value for the portion in the square brackets is 1.6, 0.1 and 0.6 for weak, strong and regular element respectively
        * Since the calculator assumes that the enemy's element is weak against the character, max is 2.4 and min is 1.6
        *
        * Currently the game has no weak elemental bonus damage debuffs, but the function takes it into account in case
        * it gets later implemented
        * From what I gather from the kirafan.moe documentation, weak element bonus has no cap or floor.
        *
        * https://calc.kirafan.moe/#/document/8
        * https://calc.kirafan.moe/#/document/10
        */

        double enemyElementResistanceBuffs = getEnemyElementalResistanceBuffs(charaElement, skillTotalAmounts);
        double enemyElementResistanceDebuffs = getEnemyElementalResistanceDebuffs(charaElement, skillTotalAmounts);

        double initialElementMultiplier = 2.0 * (1 + enemyElementResistanceBuffs - enemyElementResistanceDebuffs);
        initialElementMultiplier = Math.max(initialElementMultiplier, 1.6);
        initialElementMultiplier = Math.min(initialElementMultiplier, 2.4);

        double weakElementBonus = sumCharacterBuffs(SkillType.WEAK_ELEMENT_BONUS, skillTotalAmounts) -
                sumCharacterDebuffs(SkillType.WEAK_ELEMENT_BONUS, skillTotalAmounts);

        return initialElementMultiplier + weakElementBonus;
    }

    private static double getEnemyElementalResistanceBuffs(CharacterElement charaElement, Map<Skill, Double> skillTotalAmounts) {
        SkillType elementalResistance = getAppropriateElementalResistance(charaElement);
        return sumEnemyBuffs(elementalResistance, skillTotalAmounts);
    }

    private static double getEnemyElementalResistanceDebuffs(CharacterElement charaElement, Map<Skill, Double> skillTotalAmounts) {
        SkillType elementalResistance = getAppropriateElementalResistance(charaElement);
        return sumEnemyDebuffs(elementalResistance, skillTotalAmounts);
    }

    private static SkillType getAppropriateElementalResistance(CharacterElement element) {
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

    private static double sumEnemyBuffs(SkillType type, Map<Skill, Double> skillTotalAmounts) {
        Double singleEnemyBuffs = skillTotalAmounts.get(new Skill(type, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 0));
        singleEnemyBuffs = singleEnemyBuffs == null ? 0 : singleEnemyBuffs;

        Double enemyWideBuffs = skillTotalAmounts.get(new Skill(type, SkillChange.UP, SkillTarget.ENEMY_ALL, 0));
        enemyWideBuffs = enemyWideBuffs == null ? 0 : enemyWideBuffs;

        // Buffs and debuffs are stored as percentages, and need to be converted to decimal numbers for calculation
        return (singleEnemyBuffs + enemyWideBuffs) / 100;
    }

    private static double sumEnemyDebuffs(SkillType type, Map<Skill, Double> skillTotalAmounts) {
        Double singleEnemyDebuffs = skillTotalAmounts.get(new Skill(type, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0));
        singleEnemyDebuffs = singleEnemyDebuffs == null ? 0 : singleEnemyDebuffs;

        Double enemyWideDebuffs = skillTotalAmounts.get(new Skill(type, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0));
        enemyWideDebuffs = enemyWideDebuffs == null ? 0 : enemyWideDebuffs;

        // Buffs and debuffs are stored as percentages, and need to be converted to decimal numbers for calculation
        return (singleEnemyDebuffs + enemyWideDebuffs) / 100;
    }

    private static double getCriticalDamageMultiplier(Map<Skill, Double> skillTotalAmounts) {
        /*
        * Formula for the critical damage multiplier:
        * [(critical then 1.5, else 1.0) * (1 + critical damage up - critical damage down)] Max: 3.0, Min: 1.0
        *
        * The calculator assumes a critical hit so the function uses 1.5
        *
        * https://calc.kirafan.moe/#/document/26
        * */

        double critDamageUp = sumCharacterBuffs(SkillType.CRIT_DAMAGE, skillTotalAmounts);
        double critDamageDown = sumCharacterDebuffs(SkillType.CRIT_DAMAGE, skillTotalAmounts);

        double initialCriticalDamageMultiplier = 1.5 * (1 + critDamageUp - critDamageDown);
        double criticalDamageMultiplier = Math.max(initialCriticalDamageMultiplier, 1.0);
        criticalDamageMultiplier = Math.min(criticalDamageMultiplier, 2.4);

        return criticalDamageMultiplier;
    }

    private static double getDefensiveStatBuffMultiplier(CharacterClass charaClass, Map<Skill, Double> skillTotalAmounts) {
        /*
        * Formula for the defensive buff multiplier:
        * (1 + defensive buff - defensive debuff) Max: 5.0, Min: 0.330
        *
        * Against mages MDF is used, against warriors DEF is used
        *
        * https://calc.kirafan.moe/#/document/2
        * */
        double defensiveStatBuffMultiplier = charaClass == CharacterClass.MAGE ?
                1 + sumEnemyBuffs(SkillType.MDF, skillTotalAmounts) - sumEnemyDebuffs(SkillType.MDF, skillTotalAmounts) :
                1 + sumEnemyBuffs(SkillType.DEF, skillTotalAmounts) - sumEnemyDebuffs(SkillType.DEF, skillTotalAmounts);

        defensiveStatBuffMultiplier = Math.max(defensiveStatBuffMultiplier, 0.330);

        return Math.max(defensiveStatBuffMultiplier, 5.0);
    }
}
