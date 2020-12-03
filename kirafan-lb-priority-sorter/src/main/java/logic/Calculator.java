package logic;

import domain.*;
import domain.model.GameCharacter;
import domain.model.Weapon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public final class Calculator {
    private Calculator() {}

    // Returns sum of doubles rounded to 3 decimal places
    public static double sumDoubles(double... doubles) {
        BigDecimal sum = BigDecimal.valueOf(0);

        for (double d : doubles) {
            sum = sum.add(BigDecimal.valueOf(d));
        }

        sum = sum.setScale(4, RoundingMode.HALF_UP);
        return sum.doubleValue();
    }

    public static double multiplyDoubles(double... doubles) {
        if (doubles.length == 0) {
            return 0;
        }

        BigDecimal product = BigDecimal.valueOf(1);

        for (double d : doubles) {
            product = product.multiply(BigDecimal.valueOf(d));
        }

        return product.doubleValue();
    }

    public static double divideDoubles(double a, double b) {
        if (b != 0) {
            BigDecimal dividend = BigDecimal.valueOf(a);
            BigDecimal divisor = BigDecimal.valueOf(b);
            BigDecimal quotient = dividend.divide(divisor, 4, RoundingMode.HALF_UP);

            return quotient.doubleValue();
        }

        return 0;
    }

    // Skill values are stored as percentages and need to be converted to decimals for calculation
    public static double convertPercentageToDecimal(double percentage) {
        return divideDoubles(percentage, 100);
    }

    public static double negate(double d) {
        BigDecimal bd = BigDecimal.valueOf(d);
        return bd.negate().doubleValue();
    }

    public static long calculateMaxDamage(GameCharacter chara) {
        /*
        * Formula for damage calculation:
        * (character's base offensive power * skill power * offensive stat buff multiplier * next attack buff multiplier *
        * elemental multiplier * critical damage multiplier * totteoki chain position multiplier * random value from 0.85
        * to 1.0) / (enemy's base defensive power * defensive stat buff multiplier * 0.06)
        *
        * The function assumes that the damage is done with the character's totteoki skill, a special attack which deals
        * a lot of damage (in the case of mages and warriors), so skill power is referred to totteoki power in the function.
        * Totteoki means along the lines of 'ace in the hole' in japanese.
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

        Map<Skill, Double> skillTotalAmounts = Mapper.getSkillTotalPowers(chara);

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

        double offense = multiplyDoubles(baseOffensivePower, totteokiPower, offensiveStatBuffMultiplier,
                nextAttackBuffMultiplier, elementalMultiplier, criticalDamageMultiplier);

        double defense = multiplyDoubles(baseDefensivePower, defensiveStatBuffMultiplier, constant);

        return Math.round(divideDoubles(offense, defense));
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

        if (enemyWideTotteokiPower == null && singleEnemyTotteokiPower == null) {
            return 0;
        } else if (enemyWideTotteokiPower == null) {
            return convertPercentageToDecimal(singleEnemyTotteokiPower);
        } else {
            return convertPercentageToDecimal(enemyWideTotteokiPower);
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

        SkillType skillType = charaClass == CharacterClass.MAGE ? SkillType.MAT : SkillType.ATK;
        double offensiveBuffs = sumCharacterBuffs(skillType, skillTotalAmounts);
        double offensiveDebuffs = negate(sumCharacterDebuffs(skillType, skillTotalAmounts));
        double offensiveStatBuffMultiplier = sumDoubles(1, offensiveBuffs, offensiveDebuffs);
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

        return convertPercentageToDecimal(sumDoubles(selfBuffs, singleTargetBuffs, allyWideBuffs));
    }

    private static double sumCharacterDebuffs(SkillType type, Map<Skill, Double> skillTotalAmounts) {
        Double selfDebuffs = skillTotalAmounts.get(new Skill(type, SkillChange.DOWN, SkillTarget.SELF, 0));
        selfDebuffs = selfDebuffs == null ? 0 : selfDebuffs;

        Double singleTargetDebuffs = skillTotalAmounts.get(new Skill(type, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 0));
        singleTargetDebuffs = singleTargetDebuffs == null ? 0 : singleTargetDebuffs;

        Double allyWideDebuffs = skillTotalAmounts.get(new Skill(type, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0));
        allyWideDebuffs = allyWideDebuffs == null ? 0 : allyWideDebuffs;

        return convertPercentageToDecimal(sumDoubles(selfDebuffs, singleTargetDebuffs, allyWideDebuffs));
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
        * Next attack down debuffs are not taken into account because a player should not be using a totteoki with them
        * in the first place since the player can get rid of them simply by attacking an enemy with a basic attack
        * (plus next attack down debuffs do not currently exist in-game in the first place)
        *
        * https://calc.kirafan.moe/#/document/11
        */

        SkillType skillType = charaClass == CharacterClass.MAGE ? SkillType.NEXT_MAT : SkillType.NEXT_ATK;

        return sumDoubles(1, getStrongestBuff(skillType, skillTotalAmounts));
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
        return convertPercentageToDecimal(Math.max(selfBuffs, Math.max(singleTargetBuffs, allyWideBuffs)));
    }


    private static double getElementMultiplier(CharacterElement charaElement, Map<Skill, Double> skillTotalAmounts) {
        /*
        * Formula for the element multiplier:
        * [Initial value * (1 - enemy's element resist buff + enemy's element resist debuff)] + weak element bonus
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
        * From what I gather from the kirafan.moe documentation, weak element bonus has no max or min value
        *
        * https://calc.kirafan.moe/#/document/8
        * https://calc.kirafan.moe/#/document/10
        */

        double enemyElementResistanceBuffs = negate(getEnemyElementalResistanceBuffs(charaElement, skillTotalAmounts));
        double enemyElementResistanceDebuffs = getEnemyElementalResistanceDebuffs(charaElement, skillTotalAmounts);
        double sumOfBuffsAndDebuffs = sumDoubles(1, enemyElementResistanceBuffs, enemyElementResistanceDebuffs);
        double initialElementMultiplier = multiplyDoubles(2.0, sumOfBuffsAndDebuffs);

        initialElementMultiplier = Math.max(initialElementMultiplier, 1.6);
        initialElementMultiplier = Math.min(initialElementMultiplier, 2.4);

        double weakElementBuff = sumCharacterBuffs(SkillType.WEAK_ELEMENT_BONUS, skillTotalAmounts);
        double weakElementDebuff = negate(sumCharacterDebuffs(SkillType.WEAK_ELEMENT_BONUS, skillTotalAmounts));
        double weakElementBonus = sumDoubles(weakElementBuff, weakElementDebuff);

        return sumDoubles(initialElementMultiplier, weakElementBonus);
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

        return convertPercentageToDecimal(sumDoubles(singleEnemyBuffs, enemyWideBuffs));
    }

    private static double sumEnemyDebuffs(SkillType type, Map<Skill, Double> skillTotalAmounts) {
        Double singleEnemyDebuffs = skillTotalAmounts.get(new Skill(type, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0));
        singleEnemyDebuffs = singleEnemyDebuffs == null ? 0 : singleEnemyDebuffs;

        Double enemyWideDebuffs = skillTotalAmounts.get(new Skill(type, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0));
        enemyWideDebuffs = enemyWideDebuffs == null ? 0 : enemyWideDebuffs;

        return convertPercentageToDecimal(sumDoubles(singleEnemyDebuffs, enemyWideDebuffs));
    }

    private static double getCriticalDamageMultiplier(Map<Skill, Double> skillTotalAmounts) {
        /*
        * Formula for the critical damage multiplier:
        * 1.5 * (1 + critical damage up - critical damage down)] Max: 3.0, Min: 1.0
        *
        * If the attack is not a critical hit then this multiplier is set to 1.0, but since this calculator assumes
        * that a critical hit always occurs, the function does not need to take it into account
        *
        * https://calc.kirafan.moe/#/document/26
        * */

        double critDamageUp = sumCharacterBuffs(SkillType.CRIT_DAMAGE, skillTotalAmounts);
        double critDamageDown = negate(sumCharacterDebuffs(SkillType.CRIT_DAMAGE, skillTotalAmounts));

        double initialCriticalDamageMultiplier = multiplyDoubles(1.5, sumDoubles(1, critDamageUp, critDamageDown));
        double criticalDamageMultiplier = Math.max(initialCriticalDamageMultiplier, 1.0);

        return Math.min(criticalDamageMultiplier, 3.0);
    }

    private static double getDefensiveStatBuffMultiplier(CharacterClass charaClass, Map<Skill, Double> skillTotalAmounts) {
        /*
        * Formula for the defensive buff multiplier:
        * (1 + defensive buff - defensive debuff) Max: 5.0, Min: 0.330
        *
        * NOTE: For playable characters the max is 2.0 instead (but this calculator is used for calculating damage done
        * by player characters against enemies, it doesn't need to be taken into account)
        *
        * Against mages MDF is used, against warriors DEF is used
        *
        * https://calc.kirafan.moe/#/document/2
        * */
        SkillType skillType = charaClass == CharacterClass.MAGE ? SkillType.MDF : SkillType.DEF;
        double defensiveBuffs = sumEnemyBuffs(skillType, skillTotalAmounts);
        double defensiveDebuffs = negate(sumEnemyDebuffs(skillType, skillTotalAmounts));
        double defensiveStatBuffMultiplier = sumDoubles(1, defensiveBuffs, defensiveDebuffs);
        defensiveStatBuffMultiplier = Math.max(defensiveStatBuffMultiplier, 0.330);

        return Math.min(defensiveStatBuffMultiplier, 5.0);
    }
}
