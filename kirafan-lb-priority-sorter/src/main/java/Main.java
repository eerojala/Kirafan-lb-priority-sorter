import domain.*;
import domain.model.Series;
import logic.Database;

public class Main {
    public static void main(String[] args) {
//        SeriesDatabase seriesDatabase = new SeriesDatabase("src/main/resources");
//        Database characterDatabase = new CharacterDatabase("src/main/resources");
//

//        Character character = new Character.Builder("Naru",series, CharacterElement.SUN, CharacterClass.PRIEST)
//                .withSkill(new Skill(SkillType.MAT, SkillChange.UP, SkillTarget.ALLIES_ALL, 36.6))
//                .withWeapon()
//                .limitBroken()
//                .withPersonalPreference(9)
//                .build();
//
//        seriesDatabase.insert(series);
//        characterDatabase.insert(character);
//
//        Series fetchedSeries = seriesDatabase.get
//
//
//        database.insertSeries(series);

        Database seriesDatabase = new Database("kirafan-lb-priority-sorter/src/main/resources", "domain.model", "series");
        Series series = new Series("Hanayamata","ハナヤマタ", CreaStatus.INCOMPLETE);
        seriesDatabase.createCollection();
        seriesDatabase.insert(series);
    }
}
