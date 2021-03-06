package domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import domain.Skill;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Document(collection = "weapons", schemaVersion = "1.0")
public class Weapon implements Comparable<Weapon> {
    public static class Builder {
        private String id;
        private String name;
        private int offensiveStat;
        private int defense;
        private int magicDefense;
        private List<Skill> skills;
        private GameCharacter exclusiveCharacter;
        private String exclusiveCharacterId;

        public Builder(String name) {
            id = new Date().toString();
            this.name = name;
            offensiveStat = 0;
            defense = 0;
            magicDefense = 0;
            skills = new ArrayList<>();
            exclusiveCharacter = null;
            exclusiveCharacterId = null;
        }

        public Builder overwriteID(String id) {
            this.id = id;

            return this;
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

        public Builder withSkills(List<Skill> skills) {
            this.skills = skills;

            return this;
        }

        public Builder isExclusiveTo(GameCharacter exclusiveCharacter) {
            this.exclusiveCharacter = exclusiveCharacter;
            exclusiveCharacterId = exclusiveCharacter == null ? null : exclusiveCharacter.getId();

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
            weapon.exclusiveCharacterId = exclusiveCharacterId;

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
    @JsonIgnore
    private GameCharacter exclusiveCharacter;
    private String exclusiveCharacterId;

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

    public String getExclusiveCharacterId() {
        return exclusiveCharacterId;
    }

    public void setExclusiveCharacterId(String exclusiveCharacterId) {
        this.exclusiveCharacterId = exclusiveCharacterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weapon weapon = (Weapon) o;
        return getId().equals(weapon.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Weapon o) {
        return this.toString().compareTo(o.toString());
    }
}
