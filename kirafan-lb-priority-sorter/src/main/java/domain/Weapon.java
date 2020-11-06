package domain;

import java.util.List;

public class Weapon {
    private int attackPower;
    private int defense;
    private int magicDefense;
    private List<Skill> skills;

    public Weapon(int attackPower, int defense, int magicDefense, List<Skill> skills) {
        this.attackPower = attackPower;
        this.defense = defense;
        this.magicDefense = magicDefense;
        this.skills = skills;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getMagicDefense() {
        return magicDefense;
    }

    public void setMagicDefense(int magicDefense) {
        this.magicDefense = magicDefense;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
}
