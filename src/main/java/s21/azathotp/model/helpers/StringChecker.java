package s21.azathotp.model.helpers;

import java.util.Arrays;

public class StringChecker {
    private static final String OPERATORS = "+-~%/*^";
    private static final String[] FUNCTIONS = {"acos", "asin", "atan", "cos", "ctg", "ln", "log", "sin", "sqrt", "tan"};

    private StringChecker() {
    }

    public static boolean isNum(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    public static boolean isFunction(String token) {
        return Arrays.binarySearch(FUNCTIONS, token) >= 0;
    }

    public static boolean isOperator(String token) {
        return token.length() == 1 && OPERATORS.contains(token);
    }

    public static boolean isOpenBracket(String token) {
        return token.equals("(");
    }

    public static boolean isCloseBracket(String token) {
        return token.equals(")");
    }
}
