package logic.checks;

import domain.model.Character;
import domain.model.Event;
import logic.Searchable;

import java.util.List;

public class EventBonusCheck extends Check {
    public EventBonusCheck(Searchable<Character> characterDatabase, Event currentEvent) {
        super(characterDatabase, currentEvent);
    }

    @Override
    public int check(Character c1, Character c2) {
        // If there is no event currently, both characters have equal priority
        if (currentEvent == null) {
            return 0;
        }

        List<Character> bonusCharacters = currentEvent.getBonusCharacters();

        if (bonusCharacters.contains(c1) && !bonusCharacters.contains(c2)) {
            return -1; // if c1 is a bonus character and c2 isn't then c1 has higher priority
        } else if (!bonusCharacters.contains(c1) && bonusCharacters.contains(c2)) {
            return 1; //  if c2 is a bonus character and c1 isn't then c2 has higher priority
        } else {
            return 0; // else (both or neither character is an bonus character) both have equal priority
        }
    }
}
