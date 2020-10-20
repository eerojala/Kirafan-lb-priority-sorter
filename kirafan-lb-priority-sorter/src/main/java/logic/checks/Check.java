package logic.checks;

import domain.model.GameCharacter;
import domain.model.GameEvent;

import java.util.List;

public abstract class Check {
    protected List<GameCharacter> characters;
    protected GameEvent currentEvent;

    public Check(List<GameCharacter> characters, GameEvent currentEvent) {
        this.characters = characters;
        this.currentEvent = currentEvent;
    }

    public abstract int check(GameCharacter c1, GameCharacter c2);
}
