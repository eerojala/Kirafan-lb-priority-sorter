package model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "characters", schemaVersion = "1.0")
public class Character {
//    public static class Builder {
//        private String name;
//        private Series series;
//        private CharacterClass characterClass;
//        private CharacterElement characterElement;
//        private List<Skill> skills;
//        private int damageDealingStat;
//        private boolean hasWeapon;
//        private boolean limitBroken;
//        private int wokeLevel;
//        private int personalPreference;
//
//        public Builder(String name, Series series, CharacterClass characterClass, CharacterElement characterElement) {
//            this.name = name;
//            this.series = series;
//            this.characterClass = characterClass;
//            this.characterElement = characterElement;
//            skills = new ArrayList<>();
//            damageDealingStat = 0;
//            hasWeapon = false;
//            limitBroken = false;
//            wokeLevel = 0;
//            personalPreference = 0;
//        }
//
//        public Builder withSkill(Skill skill) {
//            skills.add(skill);
//
//            return this;
//        }
//
//        public Builder damageDealingStatIs(int amount) {
//            damageDealingStat = amount;
//
//            return this;
//        }
//
//        public Builder withWeapon() {
//            hasWeapon = true;
//
//            return this;
//        }
//
//        public Builder limitBroken() {
//            limitBroken = true;
//
//            return this;
//        }
//
//        public Builder withWokeLevel(int wokeLevel) {
//            this.wokeLevel = wokeLevel;
//
//            return this;
//        }
//
//        public Builder withPersonalPreference(int personalPreference) {
//            this.personalPreference = personalPreference;
//
//            return this;
//        }
//
//        public Character build() {
//            Character character = new Character();
//            character.name = name;
//            character.series = series;
//            character.characterClass = characterClass;
//            character.characterElement = characterElement;
//            character.skills = skills;
//            character.hasWeapon = hasWeapon;
//            character.limitBroken = limitBroken;
//            character.wokeLevel = wokeLevel;
//            character.personalPreference = personalPreference;
//
//            return character;
//        }
//    }

    @Id
    private String id;
    private String name;
    private Series series;
    private CharacterClass characterClass;
    private CharacterElement characterElement;
    private List<Skill> skills;
    private int damageDealingStat;
    private boolean hasWeapon;
    private boolean limitBroken;
    private int wokeLevel;
    private int personalPreference;

    private Character() {}

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

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }

    public CharacterElement getCharacterElement() {
        return characterElement;
    }

    public void setCharacterElement(CharacterElement characterElement) {
        this.characterElement = characterElement;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public int getDamageDealingStat() {
        return damageDealingStat;
    }

    public void setDamageDealingStat(int damageDealingStat) {
        this.damageDealingStat = damageDealingStat;
    }

    public boolean hasWeapon() {
        return hasWeapon;
    }

    public void setHasWeapon(boolean hasWeapon) {
        this.hasWeapon = hasWeapon;
    }

    public boolean isLimitBroken() {
        return limitBroken;
    }

    public void setLimitBroken(boolean limitBroken) {
        this.limitBroken = limitBroken;
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
        Character character = (Character) o;
        return name.equals(character.name) &&
                series.equals(character.series) &&
                characterClass == character.characterClass &&
                characterElement == character.characterElement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, series, characterClass, characterElement);
    }
}
