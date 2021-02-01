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
        if (!updateInAllSeries(series)) {
            System.out.println("Failed to update series " + series);
            return false;
        }

        if (eventSeriesContains(series)) {
            if(!updateInEventSeries(series)) {
                System.out.println("Failed to update the series " + series + " in the event series list.");
            }
        }

        getSeriesCharacters(series).stream()
                .forEach(c -> {
                    c.setSeries(series);

                    if (!updateCharacter(c, true)) {
                        System.out.println("Failed to update character " + c + " who belongs to series " + series);
                    }
                });

        return true;
    }


    protected abstract boolean updateInAllSeries(Series series);

    protected abstract boolean updateInEventSeries(Series series);

    private List<GameCharacter> getSeriesCharacters(Series series) {
        return getAllCharacters().stream()
                .filter(c -> series.equals(c.getSeries())) // c.series should never be null, but just in case
                .collect(Collectors.toList());
    }

    public boolean deleteSeries(Series series) {
        if (!removeFromAllSeries(series)) {
            System.out.println("Failed to delete series " + series);
            return false;
        }

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
        if (!removeFromEventSeries(series)) {
            System.out.printf("Failed to remove series " + series + " from bonus characters");
            return false;
        }

        return true;
    }

    protected abstract boolean removeFromEventSeries(Series series);

    public boolean clearEventSeries() {
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
        if (!insertToAllCharacters(character)) {
            System.out.println("Failed to add character " + character);
            return false;
        }

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
        if (!updateInAllCharacters(character)) {
            System.out.println("Failed to update character " + character);
            return false;
        }

        if (!character.isLimitBroken()) {
            if (!updateInLBCharacters(character)) {
                System.out.println("Failed to update character " + character + " in non-limit broken characters");
            }
        }

        if (eventCharactersContain(character)) {
            if (!updateInEventCharacters(character)) {
                System.out.println("Failed to update character " + character + " in event characters");
            }

        }

        if (updateExclusiveWeapons) {
            getExclusiveWeapons(character).stream()
                    .forEach(w -> {
                        w.setExclusiveCharacterId(character.getId());
                        w.setExclusiveCharacter(character);

                        // we input false as parameter so we not incur an infinite recursion of updating character.preferredWeapon
                        // and weapon.exclusiveCharacter
                        updateWeapon(w, false);
                    });
        }

        return true;
    }

    protected abstract boolean updateInAllCharacters(GameCharacter character);

    protected abstract boolean updateInLBCharacters(GameCharacter character);

    protected abstract boolean updateInEventCharacters(GameCharacter character);

    private List<Weapon> getExclusiveWeapons(GameCharacter character) {
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
        if (!removeFromAllCharacters(character)) {
            System.out.println("Failed to delete character " + character);
            return false;
        }

        if (!removeFromNonLBCharacters(character)) {
            System.out.println("Failed to remove character from non-lb characters");
        }

        removeEventCharacter(character); // function already prints an error message so we do not need to do it again

        List<Weapon> exclusiveWeapons = getExclusiveWeapons((character));

        // Delete all weapons exclusive to character
        for (Weapon exclusiveWeapon : exclusiveWeapons) {
            deleteWeapon(exclusiveWeapon);
        }

        return true;
    }

    protected abstract boolean removeFromAllCharacters(GameCharacter character);

    protected abstract boolean removeFromNonLBCharacters(GameCharacter character);

    public boolean removeEventCharacter(GameCharacter character) {
        if (!removeFromEventCharacters(character)) {
            System.out.println("Failed to remove character " + character + " from event bonus characters");
            return false;
        }

        return true;
    }

    protected abstract boolean removeFromEventCharacters(GameCharacter character);

    public boolean clearEventCharacters() {
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
        if (!insertToAllWeapons(weapon)) {
            System.out.println("Failed to add weapon " + weapon);
            return false;
        }

        return true;
    }

    protected abstract boolean insertToAllWeapons(Weapon weapon);

    public boolean updateWeapon(Weapon weapon, boolean updateWeaponUsers) {
        if (!updateInAllWeapons(weapon)) {
            System.out.println("Failed to update weapon " + weapon);
            return false;
        }

        if (updateWeaponUsers) {
            getWeaponUsers(weapon).stream()
                    .forEach(c -> {
                        c.setPreferredWeapon(weapon);
                        if (!updateCharacter(c, false)) { // false so we prevent infinite recursions
                            System.out.println("Failed to update character " + c + " who uses weapon " + weapon);
                        }
                    });
        }

        return true;
    }

    protected abstract boolean updateInAllWeapons(Weapon weapon);

    private List<GameCharacter> getWeaponUsers(Weapon weapon) {
        return getAllCharacters().stream()
                .filter(c -> weapon.equals(c.getPreferredWeapon())) // c.preferredWeapon can be null
                .collect(Collectors.toList());
    }

    public boolean deleteWeapon(Weapon weapon) {
        if (!removeFromAllWeapons(weapon)) {
            System.out.println("Failed to delete weapon " + weapon);
            return false;
        }

        // Set the preferred weapon to null for all characters who had this weapon as their preferred weapon
        getWeaponUsers(weapon).stream()
                .forEach(c -> {
                    c.setPreferredWeapon(null);
                    if (!updateCharacter(c, false)) {
                        System.out.println("Failed to update character " + c + " who used weapon " + weapon);
                    }
                });

        return true;
    }

    protected abstract boolean removeFromAllWeapons(Weapon weapon);
}
