package logic;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.model.GameCharacter;
import domain.model.GameEvent;
import domain.model.Series;
import domain.model.Weapon;
import javafx.collections.FXCollections;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
* This class handles the various lists of model classes (GameCharacter, Series and Weapon) used by the ListViews and
* ComboBoxes of the GUI. The main purpose of this class is to encapsulate cascading deletions behind a single function
* call (e.g. deleting a series should on top of removing the series from the all series list also remove the characters
* belonging to that series from the all characters list). By handling the deletions this way the GUI updates automatically
* (since the lists are set with the ObservableLists from MainController) and we do not need to manually refresh the GUI
* by rereading data from the json files.
*
* The other purpose of this class is to handle sorting of the lists. Non-limit broken characters are sorted by their
* priority (see GameCharacterPriorityComparator for details), other lists are sorted based on alphabetical order of the
* elements' toString methods.
*
* NOTE: This class does NOT handle skill lists, since they exist only as part of characters and weapons, and thus there
* is no global list of all skills (instead there are only smaller character- and weapon specific skill lists) that would
* need to be managed in the case of character/weapon deletion etc..
*/

public class GlobalListHandler {
    private GameEvent event;
    private List<GameCharacter> allCharacters;
    private List<GameCharacter> eventCharacters;
    private List<GameCharacter> nonLimitBrokenCharacters;
    private List<Series> allSeries;
    private List<Series> eventSeries;
    private List<Weapon> allWeapons;

    public GlobalListHandler(GameEvent event) {
        this.event = event;
        allCharacters = FXCollections.observableArrayList();
        eventCharacters = FXCollections.observableArrayList();
        nonLimitBrokenCharacters = FXCollections.observableArrayList();
        allSeries = FXCollections.observableArrayList();
        eventSeries = FXCollections.observableArrayList();
        allWeapons = FXCollections.observableArrayList();
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

    public void createSeries(Series series) {
        allSeries.add(series);
    }

    public void addSeriesToEventSeries(Series series) {
        if (!eventSeries.contains(series)) {
            eventSeries.add(series);
        }
    }

    public void removeSeriesFromEventSeries(Series series) {
        eventSeries.remove(series);
    }

    public void clearEventSeries() {
        eventSeries.clear();
    }

    public boolean isSeriesInEventSeries(Series series) {
        return eventSeries.contains(series);
    }

    public void updateSeries(Series series) {
        allSeries.remove(series);
        allSeries.add(series);

        if (isSeriesInEventSeries(series)) {
            removeSeriesFromEventSeries(series);
            addSeriesToEventSeries(series);
        }
    }

    public void deleteSeries(Series series) {
        allSeries.remove(series);
        removeSeriesFromEventSeries(series);

        // remove the characters belonging to the series
        List<GameCharacter> seriesCharacters = getAllCharacters().stream()
                .filter(c -> c.getSeries().equals(series))
                .collect(Collectors.toList());

        for (GameCharacter character : seriesCharacters) {
            deleteCharacter(character);
        }
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

    public List<GameCharacter> getNonLimitBrokenCharacters() {
        return nonLimitBrokenCharacters;
    }

    public void setNonLimitBrokenCharacters(List<GameCharacter> nonLimitBrokenCharacters) {
        this.nonLimitBrokenCharacters = nonLimitBrokenCharacters;
    }

    public void filterNonLimitBrokenCharacters(boolean filterOn, DatabaseHandler databaseHandler) {
        if (filterOn) {
            nonLimitBrokenCharacters = nonLimitBrokenCharacters.stream()
                    .filter(c -> eventSeries.contains(c.getSeries()))
                    .collect(Collectors.toList());
        } else {
            nonLimitBrokenCharacters = databaseHandler.getNonLimitBrokenCharacters();
        }
    }

    public void createCharacter(GameCharacter character) {
        allCharacters.add(character);

        if (!character.isLimitBroken()) {
            nonLimitBrokenCharacters.add(character);
        }
    }

    public void addCharacterToEventCharacters(GameCharacter character) {
        if (!eventCharacters.contains(character)) {
            eventCharacters.add(character);
        }
    }

    public void removeCharacterFromEventCharacters(GameCharacter character) {
        eventCharacters.remove(character);
    }

    public void clearEventCharacters() {
        eventCharacters.clear();
    }

    public boolean isCharacterInEventCharacters(GameCharacter character) {
        return eventCharacters.contains(character);
    }

    public void updateCharacter(GameCharacter character) {
        allCharacters.remove(character);
        allCharacters.add(character);

        if (nonLimitBrokenCharacters.contains(character) && character.isLimitBroken()) {
            nonLimitBrokenCharacters.remove(character);
        } else if (!nonLimitBrokenCharacters.contains(character) && !character.isLimitBroken()) {
            nonLimitBrokenCharacters.add(character);
        }

        if (eventCharacters.contains(character)) {
            removeCharacterFromEventCharacters(character);
            addCharacterToEventCharacters(character);
        }
    }

    public void deleteCharacter(GameCharacter character) {
        allCharacters.remove(character);
        removeCharacterFromEventCharacters(character);

        if (nonLimitBrokenCharacters.contains(character)) {
            nonLimitBrokenCharacters.remove(character);
        }

        // remove weapons which are exclusive to the character
        List<Weapon> weaponsExclusiveToCharacter = getAllWeapons().stream()
                .filter(w -> character.equals(w.getExclusiveCharacter())) // w.getExclusiveCharacter can be null
                .collect(Collectors.toList());

        for (Weapon weapon : weaponsExclusiveToCharacter) {
            deleteWeapon(weapon);
        }
    }

    public List<Weapon> getAllWeapons() {
        return allWeapons;
    }

    public void setAllWeapons(List<Weapon> allWeapons) {
        this.allWeapons = allWeapons;
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

    public void sortAllLists() {
        // These lists are sorted in alphabetical order
        sortAllCharacters();
        sortEventCharacters();
        sortAllSeries();
        sortEventSeries();
        sortAllWeapons();
        // Non-limit broken character list is sorted based on their priority (See GameCharacterPrioritySorter for details)
        sortNonLimitBrokenCharacters();
    }

    public void sortAllCharacters() {
        Collections.sort(allCharacters);
    }

    public void sortEventCharacters() {
        Collections.sort(eventCharacters);
    }

    public void sortAllSeries() {
        Collections.sort(allSeries);
    }

    public void sortEventSeries() {
        Collections.sort(eventSeries);
    }

    public void sortAllWeapons() {
        Collections.sort(allWeapons);
    }

    public void sortNonLimitBrokenCharacters() {
        Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charactersByElementAndClass
                = Mapper.getCharactersByElementAndClass(allCharacters);

        Map<Series, List<GameCharacter>> charactersBySeries = Mapper.getCharactersBySeries(allCharacters);
        Map<GameCharacter, Weapon> exclusiveWeaponsByCharacter = Mapper.getWeaponsByExclusiveCharacter(allWeapons);

        GameCharacterPriorityComparator comparator = new GameCharacterPriorityComparator(charactersByElementAndClass,
                charactersBySeries, exclusiveWeaponsByCharacter, event);

        Collections.sort(nonLimitBrokenCharacters, comparator);
    }



}
