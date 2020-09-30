package model;

public enum CharacterElement {
    FIRE("Fire", "Tuli", "炎"),
    WIND("Wind", "Tuuli", "風"),
    EARTH("Earth", "Maa", "土"),
    WATER("Water", "Vesi", "水"),
    MOON("Moon", "Kuu", "月"),
    SUN("Sun", "Aurinko", "陽");

    private final String nameEN;
    private final String nameFIN;
    private final String nameJP;

    private CharacterElement(String nameEN, String nameFIN, String nameJP) {
        this.nameEN = nameEN;
        this.nameFIN = nameFIN;
        this.nameJP = nameJP;
    }

    public String getNameEN() {
        return nameEN;
    }

    public String getNameFIN() {
        return nameFIN;
    }

    public String getNameJP() {
        return nameJP;
    }
}
