package domain.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import domain.CharacterClass;
import domain.CharacterElement;
import domain.Skill;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Document(collection = "characters", schemaVersion = "1.0")
public class GameCharacter implements Comparable<GameCharacter> {
    public static class Builder {
        private String id;
        private String name;
        private Series series;
        private CharacterClass characterClass;
        private CharacterElement characterElement;
        private List<Skill> skills;
        private Weapon preferredWeapon;
        private boolean limitBroken;
        private int offensiveStat;
        private int defense;
        private int magicDefense;
        private int wokeLevel;
        private int personalPreference;

        public Builder(String name, Series series, CharacterElement characterElement, CharacterClass characterClass) {
            this.name = name;
            this.series = series;
            this.characterElement = characterElement;
            this.characterClass = characterClass;
            id = new Date().toString();
            skills = new ArrayList<>();
            preferredWeapon = null;
            limitBroken = false;
            offensiveStat = 0;
            defense = 0;
            magicDefense = 0;
            wokeLevel = 0;
            personalPreference = 0;
        }

        public Builder overwriteID(String id) {
            this.id = id;

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

        public Builder prefersWeapon(Weapon weapon) {
            this.preferredWeapon = weapon;

            return this;
        }

        public Builder limitBroken(boolean limitBroken) {
            this.limitBroken = limitBroken;

            return this;
        }

        public Builder offensiveStatIs(int amount) {
            offensiveStat = amount;

            return this;
        }

        public Builder defenseIs(int amount) {
            defense = amount;

            return this;
        }

        public Builder magicDefenseIs(int amount) {
            magicDefense = amount;

            return this;
        }

        public Builder wokeLevelIs(int wokeLevel) {
            this.wokeLevel = wokeLevel;

            return this;
        }

        public Builder personalPreferenceIs(int personalPreference) {
            this.personalPreference = personalPreference;

            return this;
        }

        public GameCharacter build() {
            GameCharacter character = new GameCharacter();
            character.id = id;
            character.name = name;
            character.series = series;
            character.characterElement = characterElement;
            character.characterClass = characterClass;
            character.skills = skills;
            character.preferredWeapon = preferredWeapon;
            character.limitBroken = limitBroken;
            character.offensiveStat = offensiveStat;
            character.defense = defense;
            character.magicDefense = magicDefense;
            character.wokeLevel = wokeLevel;
            character.personalPreference = personalPreference;

            return character;
        }
    }

    @Id
    private String id; // Jsondb requires a string variable named id for all model classes
    private String name;
    private Series series;
    private CharacterElement characterElement;
    private CharacterClass characterClass;
    private List<Skill> skills;
    private Weapon preferredWeapon;
    private boolean limitBroken;
    private int offensiveStat;
    private int defense;
    private int magicDefense;
    private int wokeLevel;
    private int personalPreference;

    // Jackson requires a public constructor with no parameters
    public GameCharacter() {
    }

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

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public CharacterElement getCharacterElement() {
        return characterElement;
    }

    public void setCharacterElement(CharacterElement characterElement) {
        this.characterElement = characterElement;
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public Weapon getPreferredWeapon() {
        return preferredWeapon;
    }

    public void setPreferredWeapon(Weapon weapon) {
        this.preferredWeapon = weapon;
    }

    public boolean isLimitBroken() {
        return limitBroken;
    }

    public void setLimitBroken(boolean limitBroken) {
        this.limitBroken = limitBroken;
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

    public int getWokeLevel() {
        return wokeLevel;
    }

    public void setWokeLevel(int wokeLevel) {
        this.wokeLevel = wokeLevel;
    }

    public int getPersonalPreference() {
        return personalPreference;
    }

    public void setPersonalPreference(int personalPreference) {
        this.personalPreference = personalPreference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameCharacter that = (GameCharacter) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(name);
        sb.append(" (").append(characterElement).append(" ").append(characterClass).append(")");

        return sb.toString();
    }

    @Override
    public int compareTo(GameCharacter o) {
        return this.toString().compareTo(o.toString());
    }
}
