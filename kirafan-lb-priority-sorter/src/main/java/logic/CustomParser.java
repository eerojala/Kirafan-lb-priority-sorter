package logic;

public class CustomParser {
    public static double parseDouble(String text) {
        double value = 0;

        try {
            value = Double.parseDouble(text);
        } catch (Exception e) {

        }

        return value;
    }

    public static int parseInteger(String text) {
        int value = 0;

        try {
            value = Integer.parseInt(text);
        } catch (Exception e) {

        }

        return value;
    }
}
