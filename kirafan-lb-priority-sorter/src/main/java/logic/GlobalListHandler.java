package logic;

import domain.model.GameCharacter;
import domain.model.Series;
import domain.model.Weapon;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.stream.Collectors;

/*
* This class handles the various lists of model classes (GameCharacter, Series and Weapon) used by the ListViews and
* ComboBoxes of the GUI. The main purpose of this class is to encapsulate cascading deletions behind a single function
* call (e.g. deleting a series should on top of removing the series from the all series list also remove the characters
* belonging to that series from the all characters list). By handling the deletions this way the GUI updates automatically
* (since the lists are set with the ObservableLists from MainController) and we do not need to manually refresh the GUI
* by rereading data from the json files.
*
* NOTE: This class does NOT handle skill lists, since they exist only as part of characters and weapons, and thus there
* is no global list of all skills (instead there are only smaller character- and weapon specific skill lists) that would
* need to be managed in the case of character/weapon deletion etc..
*/

public class GlobalListHandler {
    private List<GameCharacter> allCharacters;
    private List<GameCharacter> eventCharacters;
    private List<Series> allSeries;
    private List<Series> eventSeries;
    private List<Weapon> allWeapons;

    public GlobalListHandler() {
        allCharacters = FXCollections.observableArrayList();
        eventCharacters = FXCollections.observableArrayList();
        allSeries = FXCollections.observableArrayList();
        eventSeries = FXCollections.observableArrayList();
        allWeapons = FXCollections.observableArrayList();
    }

    public List<GameCharacter> getAllCharacters() {
        return allCharacters;
    }

    public void setAllCharacters(List<GameCharacter> charactersAll) {
        this.allCharacters = charactersAll;
    }

    public List<GameCharacter> getEventCharacters() {
        return eventCharacters;
    }

    public void setEventCharacters(List<GameCharacter> eventCharacters) {
        this.eventCharacters = eventCharacters;
    }

    public List<Series> getAllSeries() {
        return allSeries;
    }

    public void setAllSeries(List<Series> allSeries) {
        this.allSeries = allSeries;
    }

    public List<Series> getEventSeries() {
        return eventSeries;
    }

    public void setEventSeries(List<Series> eventSeries) {
        this.eventSeries = eventSeries;
    }

    public List<Weapon> getAllWeapons() {
        return allWeapons;
    }

    // Returns the weapons that the given character is able to use (i.e. weapons that are either exclusive to the
    // character or do not have an exclusive character at all)
    public List<Weapon> getUsableWeapons(GameCharacter character) {
        return getAllWeapons().stream()
                .filter(w -> w.getExclusiveCharacter() == null || w.getExclusiveCharacter().equals(character))
                .collect(Collectors.toList());
    }

    public List<Weapon> getNonExclusiveWeapons() {
        return getAllWeapons().stream()
                .filter(w -> w.getExclusiveCharacter() == null)
                .collect(Collectors.toList());
    }

    public void setAllWeapons(List<Weapon> allWeapons) {
        this.allWeapons = allWeapons;
    }

    public void addSeriesToAllSeries(Series series) {
        allSeries.add(series);
    }

    public void addSeriesToEventSeries(Series series) {
        if (!eventSeries.contains(series)) {
            eventSeries.add(series);
        }
    }

    public void removeSeriesFromAllSeries(Series series) {
        allSeries.remove(series);
    }

    public void removeSeriesFromEventSeries(Series series) {
        eventSeries.remove(series);
    }

    public void clearEventSeries() {
        eventSeries.clear();
    }

    public void updateSeries(Series series) {
        removeSeriesFromAllSeries(series);
        addSeriesToAllSeries(series);

        if (eventSeries.contains(series)) {
            removeSeriesFromEventSeries(series);
            addSeriesToEventSeries(series);
        }
    }

    public void deleteSeries(Series series) {
        removeSeriesFromAllSeries(series);
        removeSeriesFromEventSeries(series);

        // remove the characters belonging to the series
        List<GameCharacter> seriesCharacters = getAllCharacters().stream()
                .filter(c -> c.getSeries().equals(series))
                .collect(Collectors.toList());

        for (GameCharacter character : seriesCharacters) {
            deleteCharacter(character);
        }
    }

    public void addCharacterToAllCharacters(GameCharacter character) {
        allCharacters.add(character);
    }

    public void addCharacterToEventCharacters(GameCharacter character) {
        if (!eventCharacters.contains(character)) {
            eventCharacters.add(character);
        }
    }

    public void removeCharacterFromAllCharacters(GameCharacter character) {
        allCharacters.remove(character);
    }

    public void removeCharacterFromEventCharacters(GameCharacter character) {
        eventCharacters.remove(character);
    }

    public void clearEventCharacters() {
        eventCharacters.clear();
    }

    public void updateCharacter(GameCharacter character) {
        removeCharacterFromAllCharacters(character);
        addCharacterToAllCharacters(character);

        if (eventCharacters.contains(character)) {
            removeCharacterFromEventCharacters(character);
            addCharacterToEventCharacters(character);
        }
    }

    public void deleteCharacter(GameCharacter character) {
        removeCharacterFromAllCharacters(character);
        removeCharacterFromEventCharacters(character);

        // remove weapons which are exclusive to the character
        List<Weapon> weaponsExclusiveToCharacter = getAllWeapons().stream()
                .filter(w -> character.equals(w.getExclusiveCharacter())) // w.getExclusiveCharacter can be null
                .collect(Collectors.toList());

        for (Weapon weapon : weaponsExclusiveToCharacter) {
            deleteWeapon(weapon);
        }
    }

    public void addWeaponToAllWeapons(Weapon weapon) {
        allWeapons.add(weapon);
    }

    public void removeWeaponFromAllWeapons(Weapon weapon) {
        allWeapons.remove(weapon);
    }

    public void updateWeapon(Weapon weapon) {
        removeWeaponFromAllWeapons(weapon);
        addWeaponToAllWeapons(weapon);
    }

    public void deleteWeapon(Weapon weapon) {
        removeWeaponFromAllWeapons(weapon);

        // Set preferred weapon as null to characters who have their preferred weapon as the deleted weapon
        getAllCharacters().stream()
                .filter(c -> c.getPreferredWeapon().equals(weapon))
                .forEach(c -> c.setPreferredWeapon(null));
    }
}
