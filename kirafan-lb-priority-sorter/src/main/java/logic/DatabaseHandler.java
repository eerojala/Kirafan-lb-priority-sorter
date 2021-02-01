package logic;

import domain.model.GameCharacter;
import domain.model.GameEvent;
import domain.model.Series;
import domain.model.Weapon;

import java.util.List;
import java.util.stream.Collectors;

/*
* Class which handles the various Database<T> objects (i.e. objects that interact with the json files with JsonDB) used
* by the GUI controller classes. Since AFAIK JsonDB does not support cascading deletions, the main purpose of this class
* is to encapsulate those deletions behind single function calls (e.g deleting a series should also delete the characters
* which belong to that series)
*/
public class DatabaseHandler extends DataHandler {
    private final static String EVENT_ID = "1";

    private Database<GameCharacter> characterDatabase;
    private Database<GameEvent> eventDatabase;
    private Database<Series> seriesDatabase;
    private Database<Weapon> weaponDatabase;
    private GameEvent event;

    public DatabaseHandler(Database<GameCharacter> characterDatabase, Database<GameEvent> eventDatabase,
                           Database<Series> seriesDatabase, Database<Weapon> weaponDatabase) {
        this.characterDatabase = characterDatabase;
        this.eventDatabase = eventDatabase;
        this.seriesDatabase = seriesDatabase;
        this.weaponDatabase = weaponDatabase;
    }

    public void initializeCollections() {
        characterDatabase.createCollection();
        eventDatabase.createCollection();
        seriesDatabase.createCollection();
        weaponDatabase.createCollection();
    }

    public void initializeEvent() {
        event = eventDatabase.findById(EVENT_ID);

        if (event == null) {
            event = new GameEvent(EVENT_ID);
            eventDatabase.insert(event);
        }
    }

    public GameEvent getEvent() {
        return this.event;
    }

    @Override
    public List<Series> getAllSeries() {
        return seriesDatabase.findAll();
    }

    @Override
    public List<Series> getEventSeries() {
        return event.getAvailableSeries();
    }

    @Override
    protected boolean insertToAllSeries(Series series) {
        return seriesDatabase.insert(series);
    }

    @Override
    public boolean eventSeriesContains(Series series) {
        return event.getAvailableSeries().contains(series);
    }

    @Override
    protected boolean insertToEventSeries(Series series) {
        event.addAvailableSeries(series);

        return updateEvent();
    }

    private boolean updateEvent() {
        return eventDatabase.update(event);
    }

    @Override
    protected boolean updateInAllSeries(Series series) {
        return seriesDatabase.update(series);
    }

    @Override
    protected boolean updateInEventSeries(Series series) {
        event.removeAvailableSeries(series);
        event.addAvailableSeries(series);

        return updateEvent();
    }

    @Override
    protected boolean removeFromEventSeries(Series series) {
        event.removeAvailableSeries(series);

        return updateEvent();
    }

    @Override
    protected boolean removeAllFromEventSeries() {
        event.clearAvailableSeries();

        return updateEvent();
    }

    public boolean deleteSeries(Series series) {
        if (!seriesDatabase.remove(series)) {
            System.out.println("Failed to delete series " + series + " from json");
            return false;
        }
        // Remove the series from the event available series list
        removeEventSeries(series);

        // Delete the characters belonging to the series as well
        getAllCharacters().stream()
                .filter(c -> c.getSeries().equals(series)) // c.getSeries() should never be null
                .forEach(c -> deleteCharacter(c));

        return true;
    }

    @Override
    public List<GameCharacter> getAllCharacters() {
        return characterDatabase.findAll();
    }

    @Override
    public List<GameCharacter> getEventCharacters() {
        return event.getBonusCharacters();
    }

    public List<GameCharacter> getNonLimitBrokenCharacters() {
        return getAllCharacters().stream()
                .filter(c -> !c.isLimitBroken())
                .collect(Collectors.toList());
    }

    @Override
    protected boolean insertToAllCharacters(GameCharacter character) {
        return characterDatabase.insert(character);
    }

    @Override
    protected boolean insertToNonLBCharacters(GameCharacter character) {
        // We do not store non-limit broken characters on a separate JSON file, so this function always returns true
        return true;
    }

    @Override
    public boolean eventCharactersContain(GameCharacter character) {
        return event.getBonusCharacters().contains(character);
    }

    @Override
    protected boolean insertToEventCharacters(GameCharacter character) {
        event.addBonusCharacter(character);

        return updateEvent();
    }


    @Override
    protected boolean updateInAllCharacters(GameCharacter character) {
        return characterDatabase.update(character);
    }

    @Override
    protected boolean updateInLBCharacters(GameCharacter character) {
        // We do not store non-limit broken characters on a separate JSON file, so this function always returns true
        return true;
    }

    @Override
    protected boolean updateInEventCharacters(GameCharacter character) {
        event.removeBonusCharacter(character);
        event.addBonusCharacter(character);

        return updateEvent();
    }

    @Override
    protected boolean removeFromAllCharacters(GameCharacter character) {
        return characterDatabase.remove(character);
    }

    @Override
    protected boolean removeFromNonLBCharacters(GameCharacter character) {
        // We do not store non-limit broken characters on a separate JSON file, so this function always returns true
        return true;
    }

    @Override
    protected boolean removeFromEventCharacters(GameCharacter character) {
        event.removeBonusCharacter(character);

        return updateEvent();
    }

    @Override
    protected boolean removeAllFromEventCharacters() {
        event.clearBonusCharacters();

        return updateEvent();
    }

    @Override
    public List<Weapon> getAllWeapons() {
        return weaponDatabase.findAll();
    }

    @Override
    protected boolean insertToAllWeapons(Weapon weapon) {
        return weaponDatabase.insert(weapon);
    }

    @Override
    protected boolean updateInAllWeapons(Weapon weapon) {
        return weaponDatabase.update(weapon);
    }

    @Override
    protected boolean removeFromAllWeapons(Weapon weapon) {
        return weaponDatabase.remove((weapon));
    }
}
