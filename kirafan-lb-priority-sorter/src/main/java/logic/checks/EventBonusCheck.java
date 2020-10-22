package logic.checks;

import domain.model.GameCharacter;
import domain.model.GameEvent;

import java.util.List;

public class EventBonusCheck extends Check {
    private GameEvent currentEvent;

    public EventBonusCheck(GameEvent currentEvent) {
        this.currentEvent = currentEvent;
    }

    @Override
    public int compare(GameCharacter c1, GameCharacter c2) {
        // If there is no event currently, both characters have equal priority
        if (currentEvent == null) {
            return 0;
        }

        List<GameCharacter> bonusCharacters = currentEvent.getBonusCharacters();

        if (bonusCharacters.contains(c1) && !bonusCharacters.contains(c2)) {
            // if c1 is a bonus character and c2 isn't then c1 has higher priority
            return -1;
        } else if (!bonusCharacters.contains(c1) && bonusCharacters.contains(c2)) {
            //  if c2 is a bonus character and c1 isn't then c2 has higher priority
            return 1;
        } else {
            // else (both or neither character is an bonus character) both have equal priority
            return 0;
        }
    }
}
