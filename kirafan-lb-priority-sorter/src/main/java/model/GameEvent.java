package model;

import java.util.ArrayList;
import java.util.List;

public class GameEvent {
    private List<Character> bonusCharacters; // Characters which give out bonus during the event
    private List<Series> availableSeries; // Series which have limit breaks available during the event

    public GameEvent() {
        bonusCharacters = new ArrayList<>();
        availableSeries = new ArrayList<>();
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
