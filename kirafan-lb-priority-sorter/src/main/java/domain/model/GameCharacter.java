package domain.model;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.Skill;
import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "characters", schemaVersion = "1.0")
public class GameCharacter {
    public static class Builder {
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

        public Builder(String name, Series series, CharacterElement characterElement, CharacterClass characterClass) {
            this.name = name;
            this.series = series;
            this.characterElement = characterElement;
            this.characterClass = characterClass;
            id = name + series.getNameEN() +  characterElement.getNameEN() + characterClass.getNameEN();
            skills = new ArrayList<>();
            damageDealingStat = 0;
            hasWeapon = false;
            limitBroken = false;
            wokeLevel = 0;
            personalPreference = 0;
        }

        public Builder withSkill(Skill skill) {
            skills.add(skill);

            return this;
        }

        public Builder damageDealingStatIs(int amount) {
            damageDealingStat = amount;

            return this;
        }

        public Builder withWeapon() {
            hasWeapon = true;

            return this;
        }

        public Builder limitBroken() {
            limitBroken = true;

            return this;
        }

        public Builder withWokeLevel(int wokeLevel) {
            this.wokeLevel = wokeLevel;

            return this;
        }

        public Builder withPersonalPreference(int personalPreference) {
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
            character.hasWeapon = hasWeapon;
            character.limitBroken = limitBroken;
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
    private int damageDealingStat;
    private boolean hasWeapon;
    private boolean limitBroken;
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
        GameCharacter that = (GameCharacter) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
