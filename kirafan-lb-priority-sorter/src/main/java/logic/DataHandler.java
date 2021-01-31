package logic;

import domain.model.GameCharacter;
import domain.model.Series;
import domain.model.Weapon;

import java.util.List;
import java.util.stream.Collectors;

public abstract class DataHandler {
    public DataHandler() {
    }

    public abstract List<Series> getAllSeries();

    public boolean addNewSeries(Series series) {
        if (!insertToAllSeries(series)) {
            System.out.println("Failed to add series " + series);
            return false;
        }

        return true;
    }

    protected abstract boolean insertToAllSeries(Series series);

//    protected abstract boolean updateSeriesInData(Series series);
//
//    protected abstract boolean isEventSeries(Series series);
//
//    protected abstract boolean updateEventSeriesInData(Series series);
//
//
//    public boolean updateSeries(Series series) {
//        if (!updateSeriesInData(series)) {
//            System.out.println("Failed to update series " + series);
//            return false;
//        }
//
//        if (isEventSeries(series)) {
//            if(!updateEventSeriesInData(series)) {
//                System.out.println("Failed to update the series " + series + " in the event series list.");
//                System.out.println("The old version of the series still remains in the event series list.");
//                System.out.println("If you wish the event series list to have an updated version of the series, " +
//                        "remove it manually from the event series list and then add it back again.");
//            }
//        }
//    }

    public abstract List<GameCharacter> getAllCharacters();

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

    public boolean updateCharacter(GameCharacter character, boolean updateExclusiveWeapon) {
        if (!updateCharacterInAllCharacters(character)) {
            System.out.println("Failed to update character " + character);
            return false;
        }

        if (eventCharactersContain(character)) {
            if (!updateCharacterInEventCharacters(character)) {
                System.out.println("Failed to update character " + character + " in event characters");
            }

        }

        if (!character.isLimitBroken()) {
            if (!updateCharacterInLBCharacters(character)) {
                System.out.println("Failed to update character " + character + " in non-limit broken characters");
            }
        }

        if (updateExclusiveWeapon) {
            Weapon exclusiveWeapon = getExclusiveWeapon(character);

            if (exclusiveWeapon != null) {
                exclusiveWeapon.setExclusiveCharacterId(character.getId());
                exclusiveWeapon.setExclusiveCharacter(character);

                // we input false as parameter so we not incur an infinite recursion of updating character.preferredWeapon
                // and weapon.exclusiveCharacter
                if (!updateWeapon(exclusiveWeapon, false)) {
                    System.out.println("Failed to update exlusive weapon " + exclusiveWeapon + " belonging to " + character);
                }

            }
        }

        return true;
    }

    protected abstract boolean updateCharacterInAllCharacters(GameCharacter character);

    protected abstract boolean updateCharacterInEventCharacters(GameCharacter character);

    protected abstract boolean updateCharacterInLBCharacters(GameCharacter character);

    private Weapon getExclusiveWeapon(GameCharacter character) {
        return getAllWeapons().stream()
                .filter(w -> character.equals(w.getExclusiveCharacter())) // w.exclusiveCharacter can be null
                .findAny()
                .orElse(null);
    }

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
        if (!updateWeaponInAllWeapons(weapon)) {
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

    protected abstract boolean updateWeaponInAllWeapons(Weapon weapon);

    private List<GameCharacter> getWeaponUsers(Weapon weapon) {
        return getAllCharacters().stream()
                .filter(c -> weapon.equals(c.getPreferredWeapon())) // c.preferredWeapon can be null
                .collect(Collectors.toList());
    }

    public abstract List<Series> getEventSeries();

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

    public abstract List<GameCharacter> getEventCharacters();

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
}
