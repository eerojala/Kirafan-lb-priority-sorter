package logic.checks;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.model.GameCharacter;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class SkillSetCheck extends Check {
    private Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charactersByElementAndClass;

    public SkillSetCheck(Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charactersByElementAndClass) {
        this.charactersByElementAndClass = charactersByElementAndClass;
    }

    @Override
    public int compare(GameCharacter c1, GameCharacter c2) {
        return 0;
    }
}
