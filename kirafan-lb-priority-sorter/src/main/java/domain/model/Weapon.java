package domain.model;

import domain.Skill;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "weapons", schemaVersion = "1.0")
public class Weapon {
    public static class Builder {
        private String id;
        private String name;
        private int offensiveStat;
        private int defense;
        private int magicDefense;
        private List<Skill> skills;
        private GameCharacter exclusiveCharacter;

        public Builder(String name) {
            id = name;
            this.name = name;
            offensiveStat = 0;
            defense = 0;
            magicDefense = 0;
            skills = new ArrayList<>();
            exclusiveCharacter = null;
        }

        public Builder offensiveStatIs(int offensivePower) {
            this.offensiveStat = offensivePower;

            return this;
        }

        public Builder defenseIs(int defense) {
            this.defense = defense;

            return this;
        }

        public Builder magicDefenseIs(int magicDefense) {
            this.magicDefense = magicDefense;

            return this;
        }

        public Builder withSkill(Skill skill) {
            skills.add(skill);

            return this;
        }

        public Builder isExclusiveTo(GameCharacter exclusiveCharacter) {
            this.exclusiveCharacter = exclusiveCharacter;

            return this;
        }

        public Weapon build() {
            Weapon weapon = new Weapon();
            weapon.id = id;
            weapon.name = name;
            weapon.offensiveStat = offensiveStat;
            weapon.defense = defense;
            weapon.magicDefense = magicDefense;
            weapon.skills = skills;
            weapon.exclusiveCharacter = exclusiveCharacter;

            return weapon;
        }
    }

    @Id
    private String id;
    private String name;
    private int offensiveStat;
    private int defense;
    private int magicDefense;
    private List<Skill> skills;
    private GameCharacter exclusiveCharacter;

    // Jackson requires a public constructor with no parameters
    public Weapon() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffensiveStat() {
        return offensiveStat;
    }

    public void setOffensiveStat(int offensiveStat) {
        this.offensiveStat = offensiveStat;
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

    public GameCharacter getExclusiveCharacter() {
        return exclusiveCharacter;
    }

    public void setExclusiveCharacter(GameCharacter exclusiveCharacter) {
        this.exclusiveCharacter = exclusiveCharacter;
    }

    @Override
    public String toString() {
        return name;
    }
}
