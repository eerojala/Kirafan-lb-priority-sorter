package logic;

import domain.model.GameCharacter;
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
public class DatabaseHandler {
    private Database<GameCharacter> characterDatabase;
    private Database<Series> seriesDatabase;
    private Database<Weapon> weaponDatabase;

    public DatabaseHandler(Database<GameCharacter> characterDatabase, Database<Series> seriesDatabase, Database<Weapon> weaponDatabase) {
        this.characterDatabase = characterDatabase;
        this.seriesDatabase = seriesDatabase;
        this.weaponDatabase = weaponDatabase;
    }

    public void initializeCollections() {
        characterDatabase.createCollection();
        seriesDatabase.createCollection();
        weaponDatabase.createCollection();
    }

    public List<Series> getAllSeries() {
        return seriesDatabase.findAll();
    }

    public boolean createSeries(Series series) {
        return seriesDatabase.insert(series);
    }

    public boolean updateSeries(Series series) {
        if (!seriesDatabase.update(series)) {
            System.out.println("Failed updating series " + series);
            return false;
        }
        // apparently JsonDb automatically updates the characters of the updated series, so this is unnecessary
//        List<GameCharacter> seriesCharacters = getSeriesCharacters(series);
//
//        for (GameCharacter character : seriesCharacters) {
//            character.setSeries(series);
//            updateCharacter(character);
//        }

        return true;
    }

    public boolean deleteSeries(Series series) {
        if (!seriesDatabase.remove(series)) {
            return false;
        }

        // Delete the characters belonging to the series as well
        getAllCharacters().stream()
                .filter(c -> c.getSeries().equals(series)) // c.getSeries() should never be null
                .forEach(c -> deleteCharacter(c));

        return true;
    }

    public List<GameCharacter> getAllCharacters() {
        return characterDatabase.findAll();
    }

    public boolean createCharacter(GameCharacter character) {
        return characterDatabase.insert(character);
    }

    public boolean updateCharacter(GameCharacter character) {
        return characterDatabase.update(character);
    }

    public boolean deleteCharacter(GameCharacter character) {
        if (!characterDatabase.remove(character)) {
            return false;
        }

        // Delete character's exclusive weapons as well
        getAllWeapons().stream()
                .filter(w -> character.equals(w.getExclusiveCharacter())) // w.getExclusiveCharacter() can be null
                .forEach(w -> deleteWeapon(w));

        return true;
    }

    public List<Weapon> getAllWeapons() {
        return weaponDatabase.findAll();
    }

    public boolean createWeapon(Weapon weapon) {
        return weaponDatabase.insert(weapon);
    }

    public boolean updateWeapon(Weapon weapon) {
        return weaponDatabase.update(weapon);
    }

    public boolean deleteWeapon(Weapon weapon) {
       if (!weaponDatabase.remove(weapon)) {
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
}
