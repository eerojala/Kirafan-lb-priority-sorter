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
    private List<GameCharacter> charactersAll;
    private List<Series> seriesAll;
    private List<Weapon> weaponsAll;

    public GlobalListHandler() {
        charactersAll = FXCollections.observableArrayList();
        seriesAll = FXCollections.observableArrayList();
        weaponsAll = FXCollections.observableArrayList();
    }

    public List<GameCharacter> getCharactersAll() {
        return charactersAll;
    }

    public void setCharactersAll(List<GameCharacter> charactersAll) {
        this.charactersAll = charactersAll;
    }

    public List<Series> getSeriesAll() {
        return seriesAll;
    }

    public void setSeriesAll(List<Series> seriesAll) {
        this.seriesAll = seriesAll;
    }

    public List<Weapon> getWeaponsAll() {
        return weaponsAll;
    }

    // Returns the weapons that the given character is able to use (i.e. weapons that are either exclusive to the
    // character or do not have an exclusive character at all)
    public List<Weapon> getUsableWeapons(GameCharacter character) {
        return getWeaponsAll().stream()
                .filter(w -> w.getExclusiveCharacter() == null || w.getExclusiveCharacter().equals(character))
                .collect(Collectors.toList());
    }

    public List<Weapon> getNonExclusiveWeapons() {
        return getWeaponsAll().stream()
                .filter(w -> w.getExclusiveCharacter() == null)
                .collect(Collectors.toList());
    }

    public void setWeaponsAll(List<Weapon> weaponsAll) {
        this.weaponsAll = weaponsAll;
    }

    public void addSeriesToSeriesAll(Series series) {
        seriesAll.add(series);
    }

    // Removes only the series
    public void removeSeries(Series series) {
        seriesAll.remove(series);
    }

    // Removes the series as well as the characters belonging to it
    public void deleteSeries(Series series) {
        removeSeries(series);

        // remove the characters belonging to the series
        List<GameCharacter> seriesCharacters = getCharactersAll().stream()
                .filter(c -> c.getSeries().equals(series))
                .collect(Collectors.toList());

        for (GameCharacter character : seriesCharacters) {
            deleteCharacter(character);
        }
    }

    public void addCharacterToCharactersAll(GameCharacter character) {
        charactersAll.add(character);
    }

    public void removeCharacter(GameCharacter character) {
        charactersAll.remove(character);
    }

    public void deleteCharacter(GameCharacter character) {
        removeCharacter(character);

        // remove weapons which are exclusive to the character
        List<Weapon> weaponsExclusiveToCharacter = getWeaponsAll().stream()
                .filter(w -> w.getExclusiveCharacter().equals(character))
                .collect(Collectors.toList());

        for (Weapon weapon : weaponsExclusiveToCharacter) {
            deleteWeapon(weapon);
        }
    }

    public void addWeaponToWeaponsAll(Weapon weapon) {
        weaponsAll.add(weapon);
    }

    public void removeWeapon(Weapon weapon) {
        weaponsAll.remove(weapon);
    }

    public void deleteWeapon(Weapon weapon) {
        removeWeapon(weapon);

        // Set preferred weapon as null to characters who have their preferred weapon as the deleted weapon
        getCharactersAll().stream()
                .filter(c -> c.getPreferredWeapon().equals(weapon))
                .forEach(c -> c.setPreferredWeapon(null));
    }
}
