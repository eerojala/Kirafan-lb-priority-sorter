package logic;

import domain.model.GameCharacter;
import domain.model.Series;
import domain.model.Weapon;

import java.util.List;
import java.util.stream.Collectors;

public class ListHandler {
    private List<GameCharacter> charactersAll;
    private List<Series> seriesAll;
    private List<Weapon> weaponsAll;

    public ListHandler() { }

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

    public void setWeaponsAll(List<Weapon> weaponsAll) {
        this.weaponsAll = weaponsAll;
    }

    public void addCharacterToCharactersAll(GameCharacter character) {
        charactersAll.add(character);
    }

    public void removeCharacter(GameCharacter character) {
        charactersAll.remove(character);
    }

    public void deleteCharacter(GameCharacter character) {
        removeCharacter(character);
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
}
