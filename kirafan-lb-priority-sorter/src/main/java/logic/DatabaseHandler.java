package logic;

import domain.model.GameCharacter;
import domain.model.Series;
import domain.model.Weapon;

import java.util.List;

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
        return characterDatabase.remove(character);
    }

    public List<Series> getAllSeries() {
        return seriesDatabase.findAll();
    }

    public boolean createSeries(Series series) {
        return seriesDatabase.insert(series);
    }

    public boolean updateSeries(Series series) {
        return seriesDatabase.update(series);
    }

    public boolean deleteSeries(Series series) {
        return seriesDatabase.remove(series);
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
        return weaponDatabase.remove(weapon);
    }
}
