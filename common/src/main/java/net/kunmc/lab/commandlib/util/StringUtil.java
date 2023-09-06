package net.kunmc.lab.commandlib.util;

public class StringUtil {
    public static final String EMPTY = "";

    public static boolean containsIgnoreCase(String s, String search) {
        if (s == null || search == null) {
            return false;
        }

        int searchLength = search.length();
        int max = s.length() - searchLength;
        for (int i = 0; i <= max; i++) {
            if (s.regionMatches(true, i, search, 0, searchLength)) {
                return true;
            }
        }
        return false;
    }

    public static String repeat(String str, int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count is negative: " + count);
        }
        if (count == 1) {
            return str;
        }

        StringBuilder sb = new StringBuilder(str.length() * count);
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
