package logic;

public class Parser {
    public static double parseDouble(String text) {
        double value = 0;

        try {
            value = Double.parseDouble(text);
        } catch (Exception e) {

        }

        return value;
    }
}
