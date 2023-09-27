package s21.azathotp.model.helpers;

public class StringPreparer {
    private StringPreparer() {
    }

    public static String prepareExpression(String expression, String x) {
        String result = expression.replace(" ", "")
                .replace("x", x)
                .replace("++", "+")
                .replace("-+", "-")
                .replace("/+", "/")
                .replace("*+", "*")
                .replace("(+", "(")
                .replace("%+", "%")
                .replace("--", "-~")
                .replace("^-", "^~")
                .replace("+-", "+~")
                .replace("/-", "/~")
                .replace("*-", "*~")
                .replace("(-", "(~")
                .replace("%-", "%~")
                .replace("~-", "-")
                .replace("E", "e")
                .replace("π", Double.toString(Math.PI))
                .replace("√", "sqrt");

        if (result.length() > 0 && (result.charAt(0) == '-' || result.charAt(0) == '+')) {
            result = "0" + result;
        }

        return result;
    }
}
