//package logic;
//
//import domain.CharacterClass;
//import domain.CharacterElement;
//import domain.CreaStatus;
//import domain.model.GameCharacter;
//import domain.model.GameEvent;
//import domain.model.Series;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class GameCharacterComparatorTest {
//    private GameCharacterComparator comparator;
//    private GameEvent event;
//    private List<GameCharacter> characters;
//    private GameCharacter c1;
//    private GameCharacter c2;
//    private GameCharacter c3;
//    private Series s1;
//    private Series s2;
//
//    public GameCharacterComparatorTest() {
//        s1 = new Series("NEW GAME", CreaStatus.COMPLETE);
//        s2 = new Series("Gochiusa", CreaStatus.NONE);
//
//        c1 = new GameCharacter.Builder("Momiji", s1, CharacterElement.EARTH, CharacterClass.WARRIOR).build();
//        c2 = new GameCharacter.Builder("Moka", s2, CharacterElement.WIND, CharacterClass.KNIGHT).build();
//        c3 = new GameCharacter.Builder("Chiya", s2, CharacterElement.WIND, CharacterClass.PRIEST).build();
//        characters = new ArrayList<>(Arrays.asList(c2, c1));
//
//        event = new GameEvent();
//        event.setBonusCharacters(new ArrayList<GameCharacter>(Arrays.asList(c1)));
//
//        comparator = new GameCharacterComparator(characters, event);
//    }
//
//    @BeforeEach
//    public void setUp() {
//        Collections.sort(characters, comparator);
//    }
//
//    @Test
//    public void compare_SortsEventBonusCharactersFirst() {
//        assertEquals(c1, characters.get(0));
//        assertEquals(c2, characters.get(1));
//    }
//}