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

    @Override
    public List<Series> getAllSeries() {
        return seriesDatabase.findAll();
    }

    @Override
    protected boolean insertToAllSeries(Series series) {
        return seriesDatabase.insert(series);
    }

    public boolean updateSeries(Series series) {
        if (seriesDatabase.update(series)) {
            // update also the series in the event's available series list (JsonDB doesn't automatically update this)
            if (event.getAvailableSeries().contains(series)) {
                removeAndAddAvailableSeries(series);
            }

            // apparently JsonDB automatically updates the characters of the updated series, so this is unnecessary
            // ^^^ Turns out I was wrong
            getSeriesCharacters(series).stream()
                    .forEach(c -> {
                        c.setSeries(series);
                        updateCharacter(c);
                    });

            return true;
        } else {
            System.out.println("Failed to update series " + series + " to json");
            return false;
        }
    }

    private List<GameCharacter> getSeriesCharacters(Series series) {
        return getAllCharacters().stream()
                .filter(c -> c.getSeries().equals(series))
                .collect(Collectors.toList());
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
    protected boolean insertToAllCharacters(GameCharacter character) {
        return characterDatabase.insert(character);
    }

    @Override
    protected boolean insertToNonLBCharacters(GameCharacter character) {
        // We do not store non-limit broken characters on a separate JSON, so this function always returns true
        return true;
    }

    public List<GameCharacter> getNonLimitBrokenCharacters() {
        return getAllCharacters().stream()
                .filter(c -> !c.isLimitBroken())
                .collect(Collectors.toList());
    }

    public boolean updateCharacter(GameCharacter character) {
        if (characterDatabase.update(character)) {
            if (event.getBonusCharacters().contains(character)) {
                // update also the character in the event's available character list (JsonDB doesn't automatically update this)
                removeAndAddEventCharacter(character);
            }

            return true;
        } else {
            System.out.println("Failed to update character " + character);
            return false;
        }
    }

    public boolean deleteCharacter(GameCharacter character) {
        if (!characterDatabase.remove(character)) {
            System.out.println("Failed to delete character " + character + " from json");
            return false;
        }
        // Remove the character from the event bonus character list
        removeEventCharacter(character);

        // Delete character's exclusive weapons as well
        getAllWeapons().stream()
                .filter(w -> character.getId().equals(w.getExclusiveCharacterId())) // w.getExclusiveCharacterId() can be null
                .forEach(w -> deleteWeapon(w));

        return true;
    }

    @Override
    public List<Weapon> getAllWeapons() {
        return weaponDatabase.findAll();
    }

    @Override
    protected boolean insertToAllWeapons(Weapon weapon) {
        return weaponDatabase.insert(weapon);
    }

    public boolean updateWeapon(Weapon weapon) {
        if (weaponDatabase.update(weapon)) {
            return true;
        } else {
            System.out.println("Failed to update weapon " + weapon);
            return false;
        }
    }

    public boolean deleteWeapon(Weapon weapon) {
       if (!weaponDatabase.remove(weapon)) {
           System.out.println("Failed to delete weapon " + weapon + " from json");
           return false;
       }

       // Set the preferred weapon to null for characters whose preferred weapon is weapon
        getAllCharacters().stream()
                .filter(c -> weapon.equals(c.getPreferredWeapon())) // c.getPreferredWeapon can be null
                .forEach(c -> {
                    c.setPreferredWeapon(null);
                    updateCharacter(c);
                });

       return true;
    }

    public GameEvent getEvent() {
        return this.event;
    }

    @Override
    public List<Series> getEventSeries() {
        return event.getAvailableSeries();
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
    protected boolean removeFromEventSeries(Series series) {
        event.removeAvailableSeries(series);

        return updateEvent();
    }

    @Override
    protected boolean removeAllFromEventSeries() {
        event.clearAvailableSeries();

        return updateEvent();
    }

    private boolean removeAndAddAvailableSeries(Series series) {
        event.removeAvailableSeries(series);
        event.addAvailableSeries(series);

        return updateEvent();
    }

    @Override
    public List<GameCharacter> getEventCharacters() {
        return event.getBonusCharacters();
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
    protected boolean removeFromEventCharacters(GameCharacter character) {
        event.removeBonusCharacter(character);

        return updateEvent();
    }

    public boolean clearEventCharacters() {
        event.clearBonusCharacters();

        return updateEvent();
    }

    private boolean removeAndAddEventCharacter(GameCharacter character) {
        event.removeBonusCharacter(character);
        event.addBonusCharacter(character);

        return updateEvent();
    }
}
