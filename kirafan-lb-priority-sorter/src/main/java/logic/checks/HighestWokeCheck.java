package logic.checks;

import domain.model.GameCharacter;
import domain.model.Series;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HighestWokeCheck extends Check{
    private Map<Series, List<GameCharacter>> charasBySeries;

    public HighestWokeCheck(Map<Series, List<GameCharacter>> charasBySeries) {
        this.charasBySeries = charasBySeries;
    }

    @Override
    public int compare(GameCharacter c1, GameCharacter c2) {
        boolean c1HighestWoke = charaIsHighestWokeInSeries(c1);
        boolean c2HighestWoke = charaIsHighestWokeInSeries(c2);

        if (c1HighestWoke && !c2HighestWoke) {
            // If c1 is the highest woke character in her series and c2 is not
            return -1;
        } else if (!c1HighestWoke && c2HighestWoke) {
            // If c2 is the highest woke character in her series and c1 is not
            return 1;
        } else {
            // If both c1 and c2 are the highest woke character in their series
            // (or if neither are the highest woke character in their series)
            return 0;
        }
    }

    private boolean charaIsHighestWokeInSeries(GameCharacter chara) {
        /*
        * Returns true when:
        *   -chara has the undisputed highest woke level out of her series' characters
        *   -chara and another character who is not limit broken have the highest woke level out of their series
        *   (chara is assumed to be not limit broken)
        *
        * Returns false when:
        *   -chara has a woke level of under 1 (the defult woke level)
        *   -There exists a character who is not limit broken and has a higher woke level and the same series as chara
        *   -There exists a character who is limit broken and has the same woke level and the same series as chara
        * */

        int charasWokeLevel = chara.getWokeLevel();

        if (charasWokeLevel < 1) {
            return false;
        }

        Series series = chara.getSeries(); // Chara's series
        List<GameCharacter> others = charasBySeries.get(series)
                .stream()
                .filter(c -> !c.equals(chara))
                .collect(Collectors.toList()); // Other characters in the same series as chara

        for (GameCharacter other : others) {
            if ((other.getWokeLevel() >= charasWokeLevel && other.isLimitBroken())
                    || (other.getWokeLevel() > charasWokeLevel)) {
                return false;
            }
        }

        return true;
    }
}
