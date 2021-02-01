package logic;

import domain.model.GameCharacter;
import domain.model.Series;
import domain.model.Weapon;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DataHandler {
    public DataHandler() {
    }

    // Series operations
    public abstract List<Series> getAllSeries();

    public abstract List<Series> getEventSeries();

    public boolean addNewSeries(Series series) {
        if (!insertToAllSeries(series)) {
            System.out.println("Failed to add series " + series);
            return false;
        }

        return true;
    }

    protected abstract boolean insertToAllSeries(Series series);

    public boolean addEventSeries(Series series) {
        // add series to event series if series has not already been added there
        if (!eventSeriesContains(series)) {
            if (!insertToEventSeries(series)) {
                System.out.println("Failed to add series " + series + " to event series");
                return false;
            }
        }

        return true;
    }

    public abstract boolean eventSeriesContains(Series series);

    protected abstract boolean insertToEventSeries(Series series);

    public boolean updateSeries(Series series) {
        // Update the series in the all series collection (JSON file for DatabaseHandler, List for GlobalListHandler)
        if (!updateInAllSeries(series)) {
            System.out.println("Failed to update series " + series);
            return false;
        }

        // Update the series in the event series list as well (provided that the series is in the event series list)
        if (eventSeriesContains(series)) {
            if(!updateInEventSeries(series)) {
                System.out.println("Failed to update the series " + series + " in the event series list.");
            }
        }

        // Update all the characters belonging to the series with an updated version of the series
        getSeriesCharacters(series).stream()
                .forEach(c -> {
                    c.setSeries(series);
                    updateCharacter(c, true);
                });

        return true;
    }


    protected abstract boolean updateInAllSeries(Series series);

    protected abstract boolean updateInEventSeries(Series series);

    private List<GameCharacter> getSeriesCharacters(Series series) {
        // Get all characters belonging to the given series
        return getAllCharacters().stream()
                .filter(c -> series.equals(c.getSeries())) // c.series should never be null
                .collect(Collectors.toList());
    }

    public boolean deleteSeries(Series series) {
        // Remove the series from the all series collection
        if (!removeFromAllSeries(series)) {
            System.out.println("Failed to delete series " + series);
            return false;
        }

        // Remove the deleted series from the event series list (if it is not included there then nothing bad should happen)
        removeEventSeries(series);

        List<GameCharacter> seriesCharacters = getSeriesCharacters(series);

        // Delete all the character belonging to the deleted series
        for (GameCharacter character : seriesCharacters) {
            deleteCharacter(character);
        }

        return true;
    }

    protected abstract boolean removeFromAllSeries(Series series);


    public boolean removeEventSeries(Series series) {
        // Remove the series from the event series list
        if (!removeFromEventSeries(series)) {
            System.out.printf("Failed to remove series " + series + " from bonus characters");
            return false;
        }

        return true;
    }

    protected abstract boolean removeFromEventSeries(Series series);

    public boolean clearEventSeries() {
        // Remove all series from the event series list
        if (!removeAllFromEventSeries()) {
            System.out.println("Failed to remove all series from event");
            return false;
        }

        return true;
    }

    protected abstract boolean removeAllFromEventSeries();



    // GameCharacter operations
    public abstract List<GameCharacter> getAllCharacters();

    public abstract List<GameCharacter> getEventCharacters();

    public boolean addNewCharacter(GameCharacter character) {
        // Insert the character to the all character collection
        if (!insertToAllCharacters(character)) {
            System.out.println("Failed to add character " + character);
            return false;
        }

        /*
        * If the character is not limit broken, add it also to the non-limit broken character list
        * NOTE: DatabaseHandler does not keep track of non-limit broken characters (since there does not exist a separate
        * JSON file for non-limit broken characters), so the DatabaseHandler implementation of insertToNonLBCharacters
        * doesn't do anything else than always return true.
        *
        * The GlobalListHandler implementation checks also that the the non-limit broken character gets past the event
        * series filter (if it's on). If the filter is on, the character only gets added to the non-limit broken character
        * list if the character belongs to a series which is currently in the event series list. If the filter is off,
        * then the non-limit broken character always gets added.
         */
        if (!character.isLimitBroken()) {
            if (!insertToNonLBCharacters(character)) {
                System.out.println("Failed to add character " + character + " to non-LB character list");
            }
        }

        return true;
    }

    protected abstract boolean insertToAllCharacters(GameCharacter character);

    protected abstract boolean insertToNonLBCharacters(GameCharacter character);

    public boolean addEventCharacter(GameCharacter character) {
        // Add character to the event character list provided it is not already there
        if (!eventCharactersContain(character)) {
            if (!insertToEventCharacters(character)) {
                System.out.println("Failed to add character " + character + " to bonus characters");
                return false;
            }
        }

        return true;
    }

    public abstract boolean eventCharactersContain(GameCharacter character);

    protected abstract boolean insertToEventCharacters(GameCharacter character);

    public boolean updateCharacter(GameCharacter character, boolean updateExclusiveWeapons) {
        /*
        * Parameter updateExclusiveWeapons is for preventing infinite recursion of update operations
        * (updateCharacter -> updateWeapon -> updateCharacter -> etc..) in the case where a character prefers their
        * exclusive weapon. (i.e. weapon.exclusiveCharacter.preferredWeapon.equals(weapon))
        *
        * updateExclusiveWeapons == true when calling updateCharacter from
        *   CharacterWindowController (i.e. when the user finishes updating the character from the GUI)
        *   updateSeries (i.e. when a series which character belongs to is updated)
        *
        * updateExclusiveWeapons == false when calling updateCharacter from
        *   updateWeapon (i.e. character is updated to have an up-to-date version of the weapon as their preferred weapon)
        *   deleteWeapon (i.e. character is updated to have preferred weapon as null)
        *
        *
        *
        */

        // Update the character in the all character collection
        if (!updateInAllCharacters(character)) {
            System.out.println("Failed to update character " + character);
            return false;
        }

        // Update the character in the non-limit broken character list (if the character is non-limit broken)
        if (!character.isLimitBroken()) {
            if (!updateInLBCharacters(character)) {
                System.out.println("Failed to update character " + character + " in non-limit broken characters");
            }
        }

        // Update the character in the event character list (provided that the character is included in there)
        if (eventCharactersContain(character)) {
            if (!updateInEventCharacters(character)) {
                System.out.println("Failed to update character " + character + " in event characters");
            }

        }

        // Update the exclusiveCharacter field in all weapons which are exclusive to the updated character
        if (updateExclusiveWeapons) {
            getExclusiveWeapons(character).stream()
                    .forEach(w -> {
                        w.setExclusiveCharacterId(character.getId());
                        w.setExclusiveCharacter(character);
                        updateWeapon(w, false);
                    });
        }

        return true;
    }

    protected abstract boolean updateInAllCharacters(GameCharacter character);

    protected abstract boolean updateInLBCharacters(GameCharacter character);

    protected abstract boolean updateInEventCharacters(GameCharacter character);

    private List<Weapon> getExclusiveWeapons(GameCharacter character) {
        // Get all the weapons which are exclusive to the given character

        /*
        * We compare exclusiveCharacterIds because the field exclusiveCharacter is not stored into JSON (to prevent
        * infinite recursions in (de-)serialization). If we would compare with field exclusiveCharacter this function
        * would not work properly in DatabaseHandler (because the Weapon objects received from DatabaseHandlers
        * getAllWeapons() do not have the the field exclusiveCharacter)
        */
        return getAllWeapons().stream()
                .filter(w -> character.getId().equals(w.getExclusiveCharacterId()))
                .collect(Collectors.toList());
    }

    public boolean deleteCharacter(GameCharacter character) {
        // Remove the character from the all characters collection
        if (!removeFromAllCharacters(character)) {
            System.out.println("Failed to delete character " + character);
            return false;
        }

        // Remove the character from the non-limit broken character list
        if (!removeFromNonLBCharacters(character)) {
            System.out.println("Failed to remove character from non-lb characters");
        }

        // Remove the character from the event character list
        removeEventCharacter(character);

        List<Weapon> exclusiveWeapons = getExclusiveWeapons((character));

        // Delete all weapons exclusive to the deleted character
        for (Weapon exclusiveWeapon : exclusiveWeapons) {
            deleteWeapon(exclusiveWeapon);
        }

        return true;
    }

    protected abstract boolean removeFromAllCharacters(GameCharacter character);

    protected abstract boolean removeFromNonLBCharacters(GameCharacter character);

    public boolean removeEventCharacter(GameCharacter character) {
        // Remove character from the event character list
        if (!removeFromEventCharacters(character)) {
            System.out.println("Failed to remove character " + character + " from event bonus characters");
            return false;
        }

        return true;
    }

    protected abstract boolean removeFromEventCharacters(GameCharacter character);

    public boolean clearEventCharacters() {
        // Remove all the characters from the event character list
        if (!removeAllFromEventCharacters()) {
            System.out.println("Failed to remove all characters from event");
            return false;
        }

        return true;
    }

    protected abstract boolean removeAllFromEventCharacters();



    // Weapon operations
    public abstract List<Weapon> getAllWeapons();

    public boolean addNewWeapon(Weapon weapon) {
        // Add the weapon to the all weapon collection
        if (!insertToAllWeapons(weapon)) {
            System.out.println("Failed to add weapon " + weapon);
            return false;
        }

        return true;
    }

    protected abstract boolean insertToAllWeapons(Weapon weapon);

    public boolean updateWeapon(Weapon weapon, boolean updateWeaponUsers) {
        /*
         * Parameter updateWeaponUsers is for preventing infinite recursion of update operations
         * (updateCharacter -> updateWeapon -> updateCharacter -> etc..) in the case where a character prefers their
         * exclusive weapon. (i.e. weapon.exclusiveCharacter.preferredWeapon.equals(weapon))
         *
         * in this context weapon user means a character who prefers the weapon in question (i.e.
         * character.preferredWeapon == weapon). updateCharactersWhoPreferWeapon would be too verbose and
         * updateWeaponPreferrers sounds weird
         *
         * updateWeaponUsers == true when calling updateWeapon from
         *   WeaponWindowController (i.e. when the user finishes updating the weapon from the GUI)
         *
         * updateWeaponUsers == false when calling updateWeapon from
         *   updateCharacter (i.e. when the weapon is updated to have an up-to-date version of the exclusive character)
         *
         */

        // Update the weapon in the all weapon collection
        if (!updateInAllWeapons(weapon)) {
            System.out.println("Failed to update weapon " + weapon);
            return false;
        }

        // Update the characters who prefer this weapon to have an up-to-date version of this weapon
        if (updateWeaponUsers) {
            getWeaponUsers(weapon).stream()
                    .forEach(c -> {
                        c.setPreferredWeapon(weapon);
                        updateCharacter(c, false);
                    });
        }

        return true;
    }

    protected abstract boolean updateInAllWeapons(Weapon weapon);

    private List<GameCharacter> getWeaponUsers(Weapon weapon) {
        // Get the characters who prefer this weapon
        // (getCharactersWhoPreferWeapon is too verbose and getWeaponPreferrers sounds too weird)
        return getAllCharacters().stream()
                .filter(c -> weapon.equals(c.getPreferredWeapon())) // c.preferredWeapon can be null
                .collect(Collectors.toList());
    }

    public boolean deleteWeapon(Weapon weapon) {
        // Remove the weapon from the all weapon collection
        if (!removeFromAllWeapons(weapon)) {
            System.out.println("Failed to delete weapon " + weapon);
            return false;
        }

        // Set the preferred weapon to null for all characters who had this weapon as their preferred weapon
        getWeaponUsers(weapon).stream()
                .forEach(c -> {
                    c.setPreferredWeapon(null);
                    updateCharacter(c, false);
                });

        return true;
    }

    protected abstract boolean removeFromAllWeapons(Weapon weapon);
}
