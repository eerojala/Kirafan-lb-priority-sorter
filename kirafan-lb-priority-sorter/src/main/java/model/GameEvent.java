package model;

import io.jsondb.annotation.Document;
import io.jsondb.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "events", schemaVersion = "1.0")
public class GameEvent {
    @Id
    private String id;
    private List<Character> bonusCharacters; // Characters which give out bonus during the event
    private List<Series> availableSeries; // Series which have limit breaks available during the event

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

    public List<Character> getBonusCharacters() {
        return bonusCharacters;
    }

    public void setBonusCharacters(List<Character> bonusCharacters) {
        this.bonusCharacters = bonusCharacters;
    }

    public List<Series> getAvailableSeries() {
        return availableSeries;
    }

    public void setAvailableSeries(List<Series> availableSeries) {
        this.availableSeries = availableSeries;
    }
}
