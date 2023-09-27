package s21.azathotp.model.helpers;

import java.util.HashMap;
import java.util.Map;

public class PriorityQualifier {
    private static final Map<String, Integer> PRIORITIES = new HashMap<>();
    private static final int DEFAULT_PRIORITY = -1;

    static {
        PRIORITIES.put("+", 1);
        PRIORITIES.put("-", 1);
        PRIORITIES.put("*", 2);
        PRIORITIES.put("/", 2);
        PRIORITIES.put("%", 2);
        PRIORITIES.put("^", 3);
        PRIORITIES.put("cos", 4);
        PRIORITIES.put("sin", 4);
        PRIORITIES.put("tan", 4);
        PRIORITIES.put("acos", 4);
        PRIORITIES.put("asin", 4);
        PRIORITIES.put("atan", 4);
        PRIORITIES.put("sqrt", 4);
        PRIORITIES.put("ln", 4);
        PRIORITIES.put("log", 4);
        PRIORITIES.put("~", 5);
    }

    public static int getPriority(String token) {
        return PRIORITIES.getOrDefault(token, DEFAULT_PRIORITY);
    }
}
