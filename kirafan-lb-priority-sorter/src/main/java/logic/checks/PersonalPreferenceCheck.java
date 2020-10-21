package logic.checks;

import domain.model.GameCharacter;
import domain.model.GameEvent;

import java.util.List;

public class PersonalPreferenceCheck extends Check{
    private int minimumPreference; // exclusive

    public PersonalPreferenceCheck(List<GameCharacter> characters, GameEvent currentEvent, int minimumPreference) {
        super(characters, currentEvent);
        this.minimumPreference = minimumPreference;
    }

    @Override
    public int check(GameCharacter c1, GameCharacter c2) {
        int c1PersonalPreference = c1.getPersonalPreference();
        int c2PersonalPreference = c2.getPersonalPreference();

        if (personalPreferenceIsWithinRange(c1PersonalPreference) && c1PersonalPreference > c2PersonalPreference) {
            // if c1's personal preference is within the given range and is higher than c2's, give c1 higher priority
            return -1;
        } else if (personalPreferenceIsWithinRange(c2PersonalPreference) && c2PersonalPreference > c1PersonalPreference) {
            // if c2's personal preference is within the given range and is higher than c1's, give c2 higher priority
            return +1;
        } else {
            // if neither character has a personal preference within the given range
            // or if both character's personal preference is within the given range but they are both equal
            // then give equal priority to both of them
            return 0;
        }
    }

    private boolean personalPreferenceIsWithinRange(int personalPreference) {
        return personalPreference >= minimumPreference;
    }
}
