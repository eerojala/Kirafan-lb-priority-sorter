package logic;

import domain.*;
import domain.model.GameCharacter;
import domain.model.Series;
import domain.model.Weapon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

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

    public static double sumSkillTotalPowers(Map<Skill, Double> skillPowerTotals, SkillType type, SkillChange change) {
        /*
        * Sums all skills of a certain type, e.g. all DEF UP skills (DEF UP to self + DEF UP to single ally + DEF UP
        * to all allies) or all 'cause hunger status effect single' (HUNGER to a single enemy + HUNGER to all enemies)
        * */

        if (type == SkillType.DEF || type == SkillType.MDF || SkillType.isElementalResist(type) || type == SkillType.SPD) {
            if (change == SkillChange.UP) {
                return sumBuffsToSelf(type, skillPowerTotals);
            } else { // when change == SkillChange.DOWN
                return sumDebuffsToOpponent(type, skillPowerTotals);
            }
        } else if (SkillType.isStatusEffect(type)) {
            return sumStatusEffects(type, skillPowerTotals);
        }

        return 0;
    }


    public static double sumBuffsToSelf(SkillType type, Map<Skill, Double> skillTotalPowers) {
        Double selfBuffs = skillTotalPowers.get(new Skill(type, SkillChange.UP, SkillTarget.SELF, 0));
        selfBuffs = selfBuffs == null ? 0 : selfBuffs;

        Double singleTargetBuffs = skillTotalPowers.get(new Skill(type, SkillChange.UP, SkillTarget.ALLIES_SINGLE, 0));
        singleTargetBuffs = singleTargetBuffs == null ? 0 : singleTargetBuffs;

        Double allyWideBuffs = skillTotalPowers.get(new Skill(type, SkillChange.UP, SkillTarget.ALLIES_ALL, 0));
        allyWideBuffs = allyWideBuffs == null ? 0 : allyWideBuffs;

        // ALLIES_SINGLE can be targeted to self, and ALLIES_ALL also affect self
        return convertPercentageToDecimal(sumDoubles(selfBuffs, singleTargetBuffs, allyWideBuffs));
    }

    public static double sumDebuffsToSelf(SkillType type, Map<Skill, Double> skillTotalPowers) {
        Double selfDebuffs = skillTotalPowers.get(new Skill(type, SkillChange.DOWN, SkillTarget.SELF, 0));
        selfDebuffs = selfDebuffs == null ? 0 : selfDebuffs;

        Double singleTargetDebuffs = skillTotalPowers.get(new Skill(type, SkillChange.DOWN, SkillTarget.ALLIES_SINGLE, 0));
        singleTargetDebuffs = singleTargetDebuffs == null ? 0 : singleTargetDebuffs;

        Double allyWideDebuffs = skillTotalPowers.get(new Skill(type, SkillChange.DOWN, SkillTarget.ALLIES_ALL, 0));
        allyWideDebuffs = allyWideDebuffs == null ? 0 : allyWideDebuffs;

        // ALLIES_SINGLE can be targeted to self, and ALLIES_ALL also affect self
        return convertPercentageToDecimal(sumDoubles(selfDebuffs, singleTargetDebuffs, allyWideDebuffs));
    }

    public static double sumBuffsToOpponent(SkillType type, Map<Skill, Double> skillTotalPowers) {
        Double singleEnemyBuffs = skillTotalPowers.get(new Skill(type, SkillChange.UP, SkillTarget.ENEMY_SINGLE, 0));
        singleEnemyBuffs = singleEnemyBuffs == null ? 0 : singleEnemyBuffs;

        Double enemyWideBuffs = skillTotalPowers.get(new Skill(type, SkillChange.UP, SkillTarget.ENEMY_ALL, 0));
        enemyWideBuffs = enemyWideBuffs == null ? 0 : enemyWideBuffs;

        return convertPercentageToDecimal(sumDoubles(singleEnemyBuffs, enemyWideBuffs));
    }

    public static double sumDebuffsToOpponent(SkillType type, Map<Skill, Double> skillTotalPowers) {
        Double singleEnemyDebuffs = skillTotalPowers.get(new Skill(type, SkillChange.DOWN, SkillTarget.ENEMY_SINGLE, 0));
        singleEnemyDebuffs = singleEnemyDebuffs == null ? 0 : singleEnemyDebuffs;

        Double enemyWideDebuffs = skillTotalPowers.get(new Skill(type, SkillChange.DOWN, SkillTarget.ENEMY_ALL, 0));
        enemyWideDebuffs = enemyWideDebuffs == null ? 0 : enemyWideDebuffs;

        return convertPercentageToDecimal(sumDoubles(singleEnemyDebuffs, enemyWideDebuffs));
    }

    public static double sumStatusEffects(SkillType type, Map<Skill, Double> skillTotalPowers) {
        Double singleEnemyStatusEffects = skillTotalPowers.get(new Skill(type, null, SkillTarget.ENEMY_SINGLE, 0));
        singleEnemyStatusEffects = singleEnemyStatusEffects == null ? 0 : singleEnemyStatusEffects;

        Double allEnemyStatusEffects = skillTotalPowers.get((new Skill(type, null, SkillTarget.ENEMY_ALL, 0)));
        allEnemyStatusEffects = allEnemyStatusEffects == null ? 0 : allEnemyStatusEffects;

        return sumDoubles(singleEnemyStatusEffects, allEnemyStatusEffects); // Status effect powers don't need to be converted to decimals
    }

    public static long countAmountOfSkills(GameCharacter chara, boolean includeWeapon, Skill... desiredSkills) {
        long amountOfSkills = 0;

        for (Skill desiredSkill : desiredSkills) {
            amountOfSkills += countAmountOfSkill(chara.getSkills(), desiredSkill);

            if (includeWeapon) {
                Weapon weapon = chara.getPreferredWeapon();

                if (weapon != null) {
                    amountOfSkills += countAmountOfSkill(weapon.getSkills(), desiredSkill);
                }
            }
        }

        return amountOfSkills;
    }

    private static long countAmountOfSkill(List<Skill> skills, Skill desiredSkill) {
        return skills
                .stream()
                .filter(s -> s.equals(desiredSkill))
                .count();
    }

    public static long calculateMaxDamageGiven(GameCharacter chara) {
        CharacterClass charaClass = chara.getCharacterClass();

        if (!(charaClass == CharacterClass.MAGE || charaClass == CharacterClass.WARRIOR)) {
            return 0;
        }

        CharacterElement enemyElement = CharacterElement.getElementThatIsWeakTo(chara.getCharacterElement());
        Series series = new Series("series", null);
        GameCharacter enemy = new GameCharacter.Builder("enemy", series, enemyElement, CharacterClass.ALCHEMIST)
                .defenseIs(200)
                .magicDefenseIs(200)
                .build();

        // Calculator assumes that the enemy has a weak element against chara and that the damage is a critical hit
        return calculateDamage(chara, enemy, getTotteokiPower(chara), true);
    }

    public static long calculateDamageTaken(GameCharacter chara, SkillType typeOfDefense) {
        if (typeOfDefense != SkillType.DEF && typeOfDefense != SkillType.MDF && chara.getCharacterClass() != CharacterClass.KNIGHT) {
            return 0;
        }

        // The above function should filter out other skill types than DEF and MDF
        CharacterClass enemyClass = typeOfDefense == SkillType.DEF ? CharacterClass.WARRIOR : CharacterClass.MAGE;
        Series series = new Series("series", null);
        GameCharacter enemy = new GameCharacter.Builder("enemy", series, chara.getCharacterElement(), enemyClass)
                .offensiveStatIs(72000)
                .build();

        /* Calculator assumes that the enemy is of the same element as character and that the damage is not a critical hit
        *
        * We give the enemy the same element as the inputted character because:
        *   1) if we gave the enemy an element which is weak against the characters element it would be unfair to sun and
        *   moon because there are no element which they are strong against (i.e. take reduced damage from) (sun and moon
        *   simultaneously weak against each other, which means that they take extra damage from each other, while also
        *   dealing extra damage to each other.)
        *
        *   2) It doesn't make sense to use a knight which is weak against the enemy because they take a lot of extra damage
        *
        * NOTE: enemy is input as parameter chara, and chara is inputted as parameter enemy because this function returns
        * how much damage character receives from an enemy
        */
        return calculateDamage(enemy, chara, 2.5, false);
    }

    private static double getTotteokiPower(GameCharacter chara) {
        // Each character should have only one totteoki
        List<Skill> filteredSkills = chara.getSkills()
                .stream()
                .filter(s -> s.getType() == SkillType.TOTTEOKI)
                .collect(Collectors.toList());

        if (filteredSkills.isEmpty()) {
            return 0;
        }

        // Every character has one totteoki (though I will not input alchemist/knight/priest totteokis while using this program
        // because they are not relevant damage-wise when compared to mages and warriors)
        Skill totteoki = filteredSkills.get(0);

        return convertPercentageToDecimal(totteoki.getPower());
    }

    private static long calculateDamage(GameCharacter chara, GameCharacter enemy, double offensiveSkillPower, boolean criticalHit) {
        /*
         * Formula for damage calculation:
         * (character's base offensive stat * skill power * offensive stat buff multiplier * next attack buff multiplier *
         * elemental multiplier * critical damage multiplier * totteoki chain position multiplier * random value from 0.85
         * to 1.0) / (enemy's base defensive stat * defensive stat buff multiplier * 0.06)
         *
         * The function assumes that totteoki chain position is 1st (multiplier: 1.0) and the random value is max (
         * multiplier: 1.0x), so they are omitted from the calculation
         *
         * https://kirarafantasia.miraheze.org/wiki/Game_Mechanics#Damage_Calculation (The miraheze kirafan wiki is in
         * english, but it omits some details from the damage calculation which can be found in kirafan.moe's documentation)
         *
         * https://calc.kirafan.moe/#/document/0 (kirafan.moe documents are available either in japanese or (mostly) in
         * chinese, but one should be able to make sense of it with google translate)
         *
         */

        CharacterClass charaClass = chara.getCharacterClass();
        Map<Skill, Double> charaSkillTotalPowers = Mapper.getSkillTotalPowers(chara);
        Map<Skill, Double> enemySkillTotalPowers = Mapper.getSkillTotalPowers(enemy);

        // Offensive modifiers
        int baseOffensiveStat = getBaseOffensiveStat(chara);
        double offensiveStatBuffMultiplier = getOffensiveStatBuffMultiplier(charaClass, charaSkillTotalPowers, enemySkillTotalPowers);
        double nextAttackBuffMultiplier = getNextAttackUpBuffMultiplier(charaClass, charaSkillTotalPowers);
        double elementalMultiplier = getElementMultiplier(chara.getCharacterElement(), enemy.getCharacterElement(),
                charaSkillTotalPowers, enemySkillTotalPowers);

        double criticalDamageMultiplier = getCriticalDamageMultiplier(criticalHit, charaSkillTotalPowers, enemySkillTotalPowers);

        // Defensive modifiers
        double baseDefensiveStat = getBaseDefensiveStat(charaClass, enemy);
        double defensiveStatBuffMultiplier = getDefensiveStatBuffMultiplier(charaClass, charaSkillTotalPowers, enemySkillTotalPowers);
        double constant = 0.06;

        double offense = multiplyDoubles(baseOffensiveStat, offensiveSkillPower, offensiveStatBuffMultiplier,
                nextAttackBuffMultiplier, elementalMultiplier, criticalDamageMultiplier);

        double defense = multiplyDoubles(baseDefensiveStat, defensiveStatBuffMultiplier, constant);

        return Math.round(divideDoubles(offense, defense));
    }


    private static int getBaseOffensiveStat(GameCharacter chara) {
        int baseOffensiveStat = chara.getOffensiveStat();
        Weapon weapon = chara.getPreferredWeapon();

        if (weapon == null) {
            return baseOffensiveStat;
        } else {
            return baseOffensiveStat + weapon.getOffensiveStat();
        }
    }

    private static double getOffensiveStatBuffMultiplier(CharacterClass charaClass,
                                                         Map<Skill, Double> charaSkillTotalPowers,
                                                         Map<Skill, Double> enemySkillTotalPowers) {
        /*
        * Formula for the offensive stat buff multiplier:
        * (1 + atk/mat buff - atk/mat debuff) Max: 2.5, Min: 0.5
        *
        * Mages use MAT, warriors use ATK
        *
        * https://calc.kirafan.moe/#/document/2
        */

        SkillType skillType = charaClass == CharacterClass.MAGE ? SkillType.MAT : SkillType.ATK;
        double charaOffensiveBuffsBySelf = sumBuffsToSelf(skillType, charaSkillTotalPowers);
        double charaOffensiveDebuffsBySelf = negate(sumDebuffsToSelf(skillType, charaSkillTotalPowers));
        double charaOffensiveBuffsByEnemy = sumBuffsToOpponent(skillType, enemySkillTotalPowers);
        double charaOffensiveDebuffsByEnemy = negate(sumDebuffsToOpponent(skillType, enemySkillTotalPowers));

        double offensiveStatBuffMultiplier = sumDoubles(1, charaOffensiveBuffsBySelf, charaOffensiveDebuffsBySelf,
                charaOffensiveBuffsByEnemy, charaOffensiveDebuffsByEnemy);

        offensiveStatBuffMultiplier = Math.max(offensiveStatBuffMultiplier, 0.5);

        return Math.min(offensiveStatBuffMultiplier, 2.5);
    }

    private static double getNextAttackUpBuffMultiplier(CharacterClass charaClass, Map<Skill, Double> charaSkillTotalPowers) {
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

        return sumDoubles(1, getStrongestBuff(skillType, charaSkillTotalPowers));
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


    private static double getElementMultiplier(CharacterElement charaElement, CharacterElement enemyElement,
                                               Map<Skill, Double> charaSkillTotalPowers,
                                               Map<Skill, Double> enemySkillTotalPowers) {
        /*
        * Formula for the element multiplier:
        * [Initial value * (1 - enemy's element resist buff + enemy's element resist debuff)] (+ weak element bonus)
        *
        * Initial value is 2.0, 0.5 and 1.0 for weak, strong and regular element respectively.
        *
        * The max value for the portion in the square brackets is 2.4, 0.9 and 1.4 for weak, strong and regular element respectively
        * THe min value for the portion in the square brackets is 1.6, 0.1 and 0.6 for weak, strong and regular element respectively
        *
        * Weak element bonus is only applied if the enemy is weak to chara's element
        *
        * Currently the game has no weak elemental bonus damage debuffs, but the function takes it into account in case
        * it gets later implemented
        *
        * From what I gather from the kirafan.moe documentation, weak element bonus has no max or min value
        *
        * https://calc.kirafan.moe/#/document/8
        * https://calc.kirafan.moe/#/document/10
        */

        double initialValue, maxValue, minValue;

        if (CharacterElement.getElementThatIsWeakTo(charaElement) == enemyElement) { // If enemy is weak to chara's element
            initialValue = 2.0;
            maxValue = 2.4;
            minValue = 1.6;
        } else if (CharacterElement.getElementThatIsWeakTo(enemyElement) == charaElement) { // If chara is weak to enemy's element
            initialValue = 0.5;
            maxValue = 0.9;
            minValue = 0.1;
        } else {
            initialValue = 1.0;
            maxValue = 1.4;
            minValue = 0.6;
        }

        double enemyElementResistanceBuffs = negate(getEnemyElementalResistanceBuffs(charaElement, charaSkillTotalPowers,
                enemySkillTotalPowers));

        double enemyElementResistanceDebuffs = getEnemyElementalResistanceDebuffs(charaElement, charaSkillTotalPowers,
                enemySkillTotalPowers);

        double sumOfBuffsAndDebuffs = sumDoubles(1, enemyElementResistanceBuffs, enemyElementResistanceDebuffs);
        double initialElementMultiplier = multiplyDoubles(initialValue, sumOfBuffsAndDebuffs);
        initialElementMultiplier = Math.min(Math.max(initialElementMultiplier, minValue), maxValue);

        if (CharacterElement.getElementThatIsWeakTo(charaElement) == enemyElement) {
            double charaWeakElementBuffBySelf = sumBuffsToSelf(SkillType.WEAK_ELEMENT_BONUS, charaSkillTotalPowers);
            double charaWeakElementDebuffBySelf = negate(sumDebuffsToSelf(SkillType.WEAK_ELEMENT_BONUS, charaSkillTotalPowers));
            double charaWeakElementBuffByEnemy = sumBuffsToOpponent(SkillType.WEAK_ELEMENT_BONUS, enemySkillTotalPowers);
            double charaWeakElementDebuffByEnemy = negate(sumDebuffsToOpponent(SkillType.WEAK_ELEMENT_BONUS, enemySkillTotalPowers));

            double weakElementBonus = sumDoubles(charaWeakElementBuffBySelf, charaWeakElementDebuffBySelf,
                    charaWeakElementBuffByEnemy, charaWeakElementDebuffByEnemy);

            return sumDoubles(initialElementMultiplier, weakElementBonus);
        } else {
            return initialElementMultiplier;
        }
    }

    private static double getEnemyElementalResistanceBuffs(CharacterElement charaElement,
                                                           Map<Skill, Double> charaSkillTotalAmounts,
                                                           Map<Skill, Double> enemySkillTotalAmounts) {
        SkillType elementalResistance = SkillType.getAppropriateElementalResistance(charaElement);

        return sumDoubles(sumBuffsToOpponent(elementalResistance, charaSkillTotalAmounts),
                sumBuffsToSelf(elementalResistance, enemySkillTotalAmounts));
    }

    private static double getEnemyElementalResistanceDebuffs(CharacterElement charaElement,
                                                             Map<Skill, Double> charaSkillTotalAmounts,
                                                             Map<Skill, Double> enemySkillTotalAmounts) {
        SkillType elementalResistance = SkillType.getAppropriateElementalResistance(charaElement);

        return sumDoubles(sumDebuffsToOpponent(elementalResistance, charaSkillTotalAmounts),
                sumDebuffsToSelf(elementalResistance, enemySkillTotalAmounts));
    }

    private static double getCriticalDamageMultiplier(boolean criticalHit, Map<Skill, Double> charaSkillTotalPowers,
                                                      Map<Skill, Double> enemySkillTotalPowers) {
        /*
        * Formula for the critical damage multiplier:
        * If critical hit:
        *   1.5 * (1 + critical damage up - critical damage down)] Max: 3.0, Min: 1.0
        *
        * If NOT critical hit:
        *   1.0
        *
        * https://calc.kirafan.moe/#/document/26
        * https://kirarafantasia.miraheze.org/wiki/Game_Mechanics#Damage_Calculation
        * */

        if (criticalHit) {
            double charaCritDamageBuffBySelf = sumBuffsToSelf(SkillType.CRIT_DAMAGE, charaSkillTotalPowers);
            double charaCritDamageDebuffBySelf = negate(sumDebuffsToSelf(SkillType.CRIT_DAMAGE, charaSkillTotalPowers));
            double charaCritDamageBuffByEnemy = sumBuffsToOpponent(SkillType.CRIT_DAMAGE, enemySkillTotalPowers);
            double charaCritDamageDebuffByEnemy = negate(sumDebuffsToOpponent(SkillType.CRIT_DAMAGE, enemySkillTotalPowers));

            double criticalDamageMultiplier = multiplyDoubles(1.5, sumDoubles(1, charaCritDamageBuffBySelf,
                    charaCritDamageDebuffBySelf, charaCritDamageBuffByEnemy, charaCritDamageDebuffByEnemy));


            return Math.min(Math.max(criticalDamageMultiplier, 1.0),  3.0);
        } else {
            return 1.0;
        }
    }

    private static int getBaseDefensiveStat(CharacterClass charaClass, GameCharacter enemy) {
        Weapon weapon = enemy.getPreferredWeapon();

        // The function should only be used when chara's class is either Mage or Warrior
        if (charaClass == CharacterClass.WARRIOR) {
            return weapon == null ? enemy.getDefense() : enemy.getDefense() + weapon.getDefense();
        } else if (charaClass == CharacterClass.MAGE) {
            return weapon == null ? enemy.getMagicDefense() : enemy.getMagicDefense() + weapon.getMagicDefense();
        } else {
            return 0;
        }
    }

    private static double getDefensiveStatBuffMultiplier(CharacterClass charaClass,
                                                         Map<Skill, Double> charaSkillTotalPowers,
                                                         Map<Skill, Double> enemySkillTotalPowers) {
        /*
        * Formula for the defensive buff multiplier:
        * (1 + defensive buff - defensive debuff) Max: 5.0, Min: 0.330
        *
        * NOTE: For playable characters the max is 2.0 instead, but this function uses 5.0 for all characters for the
        * sake of simplicity
        *
        * Against mages MDF is used, against warriors DEF is used
        *
        * https://calc.kirafan.moe/#/document/2
        * */

        SkillType skillType = charaClass == CharacterClass.MAGE ? SkillType.MDF : SkillType.DEF;
        double enemyDefensiveBuffsByChara = sumBuffsToOpponent(skillType, charaSkillTotalPowers);
        double enemyDefensiveDebuffsByChara = negate(sumDebuffsToOpponent(skillType, charaSkillTotalPowers));
        double enemyDefensiveBuffsByEnemy = sumBuffsToSelf(skillType, enemySkillTotalPowers);
        double enemyDefensiveDebuffsByEnemy = negate(sumDebuffsToSelf(skillType, enemySkillTotalPowers));

        double defensiveStatBuffMultiplier = sumDoubles(1, enemyDefensiveBuffsByChara, enemyDefensiveDebuffsByChara,
                enemyDefensiveBuffsByEnemy, enemyDefensiveDebuffsByEnemy);

        return Math.min(Math.max(defensiveStatBuffMultiplier, 0.330), 5.0);
    }
}
