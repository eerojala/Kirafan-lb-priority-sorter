package domain;

import java.util.Objects;

public class Character implements  Comparable<Character> {
    private String name;
    private Series series;
    private CharacterClass characterClass;
    private CharacterElement characterElement;
    private boolean hasWeapon;
    private int wokeLevel;
    private Niche niche; // See comment in domain.Niche for explanation
    private int personalPreference;
    private String description;

    public Character(String name, Series series, CharacterClass characterClass, CharacterElement characterElement,
                     boolean hasWeapon, int wokeLevel, Niche niche, int personalPreference, String description) {
        this.name = name;
        this.series = series;
        this.characterClass = characterClass;
        this.characterElement = characterElement;
        this.hasWeapon = hasWeapon;
        this.wokeLevel = wokeLevel;
        this.niche = niche;
        this.personalPreference = personalPreference;
        this.description = description;
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

    public boolean isHasWeapon() {
        return hasWeapon;
    }

    public void setHasWeapon(boolean hasWeapon) {
        this.hasWeapon = hasWeapon;
    }

    public int getWokeLevel() {
        return wokeLevel;
    }

    public void setWokeLevel(int wokeLevel) {
        this.wokeLevel = wokeLevel;
    }

    public Niche getNiche() {
        return niche;
    }

    public void setNiche(Niche niche) {
        this.niche = niche;
    }

    public int getPersonalPreference() {
        return personalPreference;
    }

    public void setPersonalPreference(int personalPreference) {
        this.personalPreference = personalPreference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    /*
    * Comparison hierarchy, from highest to lowest
    *
    * 01: Important niche
    * 02: High personal preference (9-10)
    * 03: Incomplete crea
    * 04: Medium personal preference (7-8)
    * 05: Character does not have a weapon
    * 06: Non-important niche
    * 07: Lower number of not yet limit broken characters which have the same class
    * 08: Low personal preference (<7)
    */
    public int compareTo(Character o) {
        int importantNicheCheck = compareNiche(o, Niche.IMPORTANT); // Check for important niche
        /*
        * If either character (but not both) has an important niche
        *   return comparison value, otherwise continue
        */
        if (importantNicheCheck != 0) {
            return importantNicheCheck;
        }

        int highPersonalPreferenceCheck = comparePreference(0, 9, 10); // Check for high personal preference
        /*
        * If either character has a high personal preference (9-10)
        * which is also higher than the other's personal preference
        *   return comparison value, otherwise continue
        */
        if (highPersonalPreferenceCheck != 0) {
            return highPersonalPreferenceCheck;
        }


    }

    /*
    * if this character fills the niche but the other doesn't
    *   return -1 (this character has higher priority)
    *
    * else if the other fills the niche but this doesn't
    *   return 1 (the other character has higher priority)
    *
    * else (both characters fill the niche or neither character fills the niche)
    *   return 0 (both characters have the same priority)
    */
    private int compareNiche(Character o, Niche niche) {
        Niche nicheThis = this.niche;
        Niche nicheOther = o.getNiche();

        if (nicheThis == niche && nicheOther != niche) {
            return -1;
        } else if (nicheOther == niche && nicheThis != niche) {
            return 1;
        } else {
            return 0;
        }
    }

    /* Note: min and max are inclusive, i.e. [min, max]
    *
    * if this character's personal preference is within given range
    * and if this character's personal preference is higher than the other's
    *   return -1 (this character has higher priority)
    *
    * if the other character's personal preference is within given range
    * and the other character's personal preference is higher than this character's
    *   return 1 (the other character has higher priority)
    *
    * else (if both characters are in the given range and have the same personal preference
    * or neither character has a personal preference in the given range)
    *   return 0 (both characters have the same priority)
    * */
    private int comparePreference(Character o, int min, int max) {
        int personalPreferenceThis = this.personalPreference;
        int personalPreferenceOther = o.getPersonalPreference();

        if (personalPreferenceWithinRange(personalPreferenceThis, min, max)
                && personalPreferenceThis > personalPreferenceOther) {
            return -1;
        } else if (personalPreferenceWithinRange(personalPreferenceOther, min, max)
                && personalPreferenceOther > personalPreferenceThis) {
            return 1;
        } else {
            return 0;
        }
    }

    private boolean personalPreferenceWithinRange(int personalPreference, int min, int max) {
        return personalPreference >= min && personalPreference <= max;
    }

    private int compareCrea(Character o) {

    }
}
