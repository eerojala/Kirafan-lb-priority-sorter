package logic;

import domain.CharacterClass;
import domain.CharacterElement;
import domain.model.GameCharacter;
import domain.model.Series;

import java.util.*;

public class GameCharacterMapper {
    private final Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> charactersByElementAndClass;
    private final Map<Series, List<GameCharacter>> charactersBySeries;

    public GameCharacterMapper() {
        charactersByElementAndClass = new HashMap<>();
        charactersBySeries = new HashMap<>();
    }

    public void addCharacters(List<GameCharacter> characters) {
        for (GameCharacter character : characters) {
            addCharacter(character);
        }
    }

    public void addCharacter(GameCharacter character) {
        addCharacterToElementClassMap(character);
        addCharacterToSeriesMap(character);
    }

    public Map<AbstractMap.SimpleEntry<CharacterElement, CharacterClass>, List<GameCharacter>> getCharactersByElementAndClass() {
        return charactersByElementAndClass;
    }

    public Map<Series, List<GameCharacter>> getCharactersBySeries() {
        return charactersBySeries;
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

    private void addCharacterToSeriesMap(GameCharacter character) {
        Series series = character.getSeries();
        List<GameCharacter> characters = charactersBySeries.get(series);

        if (characters == null) {
            characters = new ArrayList<>();
        }

        characters.add(character);
        charactersBySeries.put(series, characters);
    }
}
