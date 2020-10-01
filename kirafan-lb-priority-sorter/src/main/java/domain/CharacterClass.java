package domain;

public enum CharacterClass {
    WARRIOR("Warrior", "Soturi", "せんし"),
    MAGE("Mage", "Maagi", "まほうつかい"),
    PRIEST("Priest", "Pappi", "そうりょ"),
    KNIGHT("Knight", "Ritari", "ナイト"),
    ALCHEMIST("Alchemist", "Alkemisti","アルケミスト");

    private final String nameEN;
    private final String nameFIN;
    private final String nameJP;

    private CharacterClass(String nameEN, String nameFIN, String nameJP) {
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
