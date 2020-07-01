package su.dreamtime.dtbotsapi.util;

import java.util.regex.Pattern;

public class Util {
    private static final Pattern CLEAR_PATTERN = Pattern.compile("[\\s]+");

    public static String removeSpaces(String string) {
        if (string == null) {
            return null;
        }
        return CLEAR_PATTERN .matcher(string).replaceAll(" ").trim();
    }
}
