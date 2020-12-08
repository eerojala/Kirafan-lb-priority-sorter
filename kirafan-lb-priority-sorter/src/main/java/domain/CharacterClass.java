package domain;

public enum CharacterClass {
    WARRIOR("Warrior", "せんし"),
    MAGE("Mage", "まほうつかい"),
    PRIEST("Priest","そうりょ"),
    KNIGHT("Knight", "ナイト"),
    ALCHEMIST("Alchemist", "アルケミスト");

    private final String nameEN;
    private final String nameJP;

    private CharacterClass(String nameEN, String nameJP) {
        this.nameEN = nameEN;
        this.nameJP = nameJP;
    }

    public String getNameEN() {
        return nameEN;
    }

    public String getNameJP() {
        return nameJP;
    }
}
