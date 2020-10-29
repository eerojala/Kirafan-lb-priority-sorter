package logic;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.model.GameCharacter;

import java.util.*;

public class GameCharacterMapper {
    private Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charactersByElementAndClass;

    public GameCharacterMapper() {
        charactersByElementAndClass = new HashMap<>();
    }

    public void addCharacters(List<GameCharacter> characters) {
        for (GameCharacter character : characters) {
            addCharacter(character);
        }
    }

    public void addCharacter(GameCharacter character) {
        addCharacterToElementClassMap(character);
    }

    public Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> getCharactersByElementAndClass() {
        return charactersByElementAndClass;
    }

    private void addCharacterToElementClassMap(GameCharacter character) {
        AbstractMap.SimpleEntry<CharacterElement, CharacterClass> elementClassPair = new AbstractMap.SimpleEntry<>(character.getCharacterElement(), character.getCharacterClass());
        List<GameCharacter> characters = charactersByElementAndClass.get(elementClassPair);

        if (characters == null) {
            characters = new ArrayList<>();
        }

        characters.add(character);
        charactersByElementAndClass.put(elementClassPair, characters);
    }
}
