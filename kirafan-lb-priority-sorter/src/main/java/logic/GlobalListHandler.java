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
*
* NOTE2: ListView.getSelectionModel().getSelectedItem() returns only a snapshot of the selected object, not the actual
* object contained in the lists. This is why in all the update functions of this class we first remove the old object
* from the list and then add the new object (with the same id but possible changes done in the update window) so that the
* list has correct fields for the object and the ListView is refreshed automatically.
*/
public class GlobalListHandler extends DataHandler {
    private boolean filterOn;
    private GameEvent event;
    private List<GameCharacter> allCharacters;
    private List<GameCharacter> eventCharacters;
    private List<GameCharacter> nonLimitBrokenCharacters;
    private List<Series> allSeries;
    private List<Series> eventSeries;
    private List<Weapon> allWeapons;

    public GlobalListHandler(GameEvent event) {
        filterOn = false;
        this.event = event;
        allCharacters = FXCollections.observableArrayList();
        eventCharacters = FXCollections.observableArrayList();
        nonLimitBrokenCharacters = FXCollections.observableArrayList();
        allSeries = FXCollections.observableArrayList();
        eventSeries = FXCollections.observableArrayList();
        allWeapons = FXCollections.observableArrayList();
    }

    public void assignExclusiveCharactersToWeapons() {
        /*
        * JsonDB cannot handle bidirectional relationships between models (GameCharacter.preferredWeapon and
        * Weapon.exclusiveCharacter cause an infinite recursion during deserialization), so Weapon.exclusiveCharacter
        * is not saved into the json (annotation @JsonIgnore), but the character's id is (Weapon.exclusiveCharacterId).
        * So in this function we assign the actual exclusive characters to the weapons' exclusiveCharacter field.
        */
        Mapper.assignExclusiveCharactersToWeapons(allCharacters, allWeapons);
    }

    @Override
    public List<Series> getAllSeries() {
        return allSeries;
    }

    @Override
    public List<Series> getEventSeries() {
        return eventSeries;
    }

    public void setEventSeries(List<Series> eventSeries) {
        this.eventSeries = eventSeries;
    }

    public void setAllSeries(List<Series> allSeries) {
        this.allSeries = allSeries;
    }

    @Override
    protected boolean insertToAllSeries(Series series) {
        allSeries.add(series);

        return true;
    }

    @Override
    public boolean eventSeriesContains(Series series) {
        return eventSeries.contains(series);
    }

    @Override
    protected boolean insertToEventSeries(Series series) {
        eventSeries.add(series);

        return true;
    }

    @Override
    protected boolean updateInAllSeries(Series series) {
        allSeries.remove(series);
        allSeries.add(series);

        return true;
    }

    @Override
    protected boolean updateInEventSeries(Series series) {
        eventSeries.remove(series);
        eventSeries.add(series);

        return true;
    }

    @Override
    protected boolean removeFromAllSeries(Series series) {
        allSeries.remove(series);

        return true;
    }

    @Override
    protected boolean removeFromEventSeries(Series series) {
        eventSeries.remove(series);

        return true;
    }

    @Override
    protected boolean removeAllFromEventSeries() {
        eventSeries.clear();

        return true;
    }

    @Override
    public List<GameCharacter> getAllCharacters() {
        return allCharacters;
    }

    public List<GameCharacter> getNonLimitBrokenCharacters() {
        return nonLimitBrokenCharacters;
    }

    public void setNonLimitBrokenCharacters(List<GameCharacter> nonLimitBrokenCharacters) {
        this.nonLimitBrokenCharacters = nonLimitBrokenCharacters;
    }

    @Override
    public List<GameCharacter> getEventCharacters() {
        return eventCharacters;
    }

    public void setEventCharacters(List<GameCharacter> eventCharacters) {
        this.eventCharacters = eventCharacters;
    }

    public void setAllCharacters(List<GameCharacter> charactersAll) {
        this.allCharacters = charactersAll;
    }

    @Override
    protected boolean insertToAllCharacters(GameCharacter character) {
        allCharacters.add(character);

        return true;
    }

    @Override
    protected boolean insertToNonLBCharacters(GameCharacter character) {
        if (characterGetsPastEventSeriesFilter(character)) {
            nonLimitBrokenCharacters.add(character);
        }

        return true;
    }

    @Override
    public boolean eventCharactersContain(GameCharacter character) {
        return eventCharacters.contains(character);
    }

    @Override
    protected boolean insertToEventCharacters(GameCharacter character) {
        eventCharacters.add(character);

        return true;
    }

    private boolean characterGetsPastEventSeriesFilter(GameCharacter character) {
        // Returns true if the character belongs to a series that has lb mats currently available in the event or if
        // the filter is not currently on.
        return (filterOn && eventSeriesContains(character.getSeries())) || !filterOn;

    }

    @Override
    protected boolean updateInAllCharacters(GameCharacter character) {
        allCharacters.remove(character);
        allCharacters.add(character);

        return true;
    }

    @Override
    protected boolean updateInLBCharacters(GameCharacter character) {
        nonLimitBrokenCharacters.remove(character);

        if (characterGetsPastEventSeriesFilter(character)) {
            nonLimitBrokenCharacters.add(character);
        }

        return true;
    }

    @Override
    protected boolean updateInEventCharacters(GameCharacter character) {
        eventCharacters.remove(character);
        eventCharacters.add(character);

        return true;
    }

    @Override
    protected boolean removeFromAllCharacters(GameCharacter character) {
        allCharacters.remove(character);

        return true;
    }

    @Override
    protected boolean removeFromNonLBCharacters(GameCharacter character) {
        nonLimitBrokenCharacters.remove(character);

        return true;
    }

    @Override
    protected boolean removeFromEventCharacters(GameCharacter character) {
        eventCharacters.remove(character);

        return true;
    }

    @Override
    protected boolean removeAllFromEventCharacters() {
        eventCharacters.clear();

        return true;
    }

    @Override
    public List<Weapon> getAllWeapons() {
        return allWeapons;
    }

    public void setAllWeapons(List<Weapon> allWeapons) {
        this.allWeapons = allWeapons;
    }


    public List<Weapon> getUsableWeapons(GameCharacter character) {
        // Returns the weapons that the given character is able to use (i.e. weapons that are either exclusive to the
        // character or do not have an exclusive character at all)
        return getAllWeapons().stream()
                .filter(w -> w.getExclusiveCharacter() == null || w.getExclusiveCharacter().equals(character))
                .collect(Collectors.toList());
    }

    public List<Weapon> getNonExclusiveWeapons() {
        return getAllWeapons().stream()
                .filter(w -> w.getExclusiveCharacter() == null)
                .collect(Collectors.toList());
    }

    @Override
    protected boolean insertToAllWeapons(Weapon weapon) {
        allWeapons.add(weapon);

        return true;
    }

    @Override
    protected boolean updateInAllWeapons(Weapon weapon) {
        allWeapons.remove(weapon);
        allWeapons.add(weapon);

        return true;
    }

    @Override
    protected boolean removeFromAllWeapons(Weapon weapon) {
        allWeapons.remove(weapon);

        return true;
    }

    public void filterNonLimitBrokenCharacters(boolean filterOn, DatabaseHandler databaseHandler) {
        // This is for filtering non-limit broken characters based on the availability of limit-breakable series in the current event
        // If the filter is ON then return non-limit broken characters that belong to series which are event series
        // If the filter is OFF then return all non-limit broken characters
        this.filterOn = filterOn;

        if (this.filterOn) {
            List<GameCharacter> filteredNonLBCharacters = nonLimitBrokenCharacters.stream()
                    .filter(c -> eventSeries.contains(c.getSeries()))
                    .collect(Collectors.toList());

            nonLimitBrokenCharacters.clear();
            nonLimitBrokenCharacters.addAll(filteredNonLBCharacters);
        } else {
            nonLimitBrokenCharacters.clear();
            nonLimitBrokenCharacters.addAll(databaseHandler.getNonLimitBrokenCharacters());
        }
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

        nonLimitBrokenCharacters.sort(comparator);
    }
}
