package domain.model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "events", schemaVersion = "1.0")
public class GameEvent {
    @Id
    private String id;
    private List<GameCharacter> bonusCharacters; // Characters which give out bonus during the event
    private List<Series> availableSeries; // Series which have limit breaks available during the event

    // Jackson requires a public constructor with no parameters
    public GameEvent() {
    }

    public GameEvent(String id) {
        this.id = id;
        bonusCharacters = new ArrayList<>();
        availableSeries = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<GameCharacter> getBonusCharacters() {
        return bonusCharacters;
    }

    public void setBonusCharacters(List<GameCharacter> bonusCharacters) {
        this.bonusCharacters = bonusCharacters;
    }

    public void addBonusCharacter(GameCharacter character) {
        if (!bonusCharacters.contains(character)) {
            bonusCharacters.add(character);
        }
    }

    public void removeBonusCharacter(GameCharacter character) {
        bonusCharacters.remove(character);
    }

    public void clearBonusCharacters() {
        bonusCharacters.clear();
    }

    public List<Series> getAvailableSeries() {
        return availableSeries;
    }

    public void setAvailableSeries(List<Series> availableSeries) {
        this.availableSeries = availableSeries;
    }

    public void addAvailableSeries(Series series) {
        if (!this.availableSeries.contains(series)) {
            this.availableSeries.add(series);
        }
    }

    public void removeAvailableSeries(Series series) {
        this.availableSeries.remove(series);
    }

    public void clearAvailableSeries() {
        availableSeries.clear();
    }
}
