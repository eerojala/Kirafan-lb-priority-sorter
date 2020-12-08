package domain;

public enum CharacterElement {
    FIRE("Fire", "炎"),
    WIND("Wind", "風"),
    EARTH("Earth", "土"),
    WATER("Water", "水"),
    MOON("Moon",  "月"),
    SUN("Sun", "陽");

    private final String nameEN;
    private final String nameJP;

    private CharacterElement(String nameEN, String nameJP) {
        this.nameEN = nameEN;
        this.nameJP = nameJP;
    }

    public String getNameEN() {
        return nameEN;
    }

    public String getNameJP() {
        return nameJP;
    }

    public static CharacterElement getElementThatIsWeakTo(CharacterElement element) {
        /*
        * Wind is weak against fire
        * Earth is weak against wind
        * Water is weak against earth
        * Fire is weak against water
        * Sun is weak against Moon
        * Moon is weak against Sun (So sun and moon are weak to each other)
        * */
        switch (element) {
            case FIRE:
                return WIND;

            case WIND:
                return EARTH;

            case EARTH:
                return WATER;

            case WATER:
                return FIRE;

            case MOON:
                return SUN;

            default: // a.k.a case SUN:
                 return MOON;
        }
    }
}
