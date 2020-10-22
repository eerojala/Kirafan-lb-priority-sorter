package logic.checks;

import domain.model.GameCharacter;

public abstract class Check {
    public Check() {
    }

    public abstract int compare(GameCharacter c1, GameCharacter c2);
}
