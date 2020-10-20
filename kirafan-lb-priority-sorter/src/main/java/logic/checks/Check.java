package logic.checks;

import domain.model.Character;
import domain.model.Event;
import logic.Searchable;

public abstract class Check {
    protected Searchable<Character> characterDatabase;
    protected Event currentEvent;

    public Check(Searchable<Character> characterDatabase, Event currentEvent) {
        this.characterDatabase = characterDatabase;
        this.currentEvent = currentEvent;
    }

    public abstract int check(Character c1, Character c2);
}
