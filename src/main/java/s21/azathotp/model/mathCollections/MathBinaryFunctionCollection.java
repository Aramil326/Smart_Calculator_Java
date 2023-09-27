package s21.azathotp.model.mathCollections;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;

public class MathBinaryFunctionCollection {
    private final Map<String, BinaryOperator<Double>> FUNCTIONS;

    public MathBinaryFunctionCollection() {
        FUNCTIONS = new HashMap<>();
        FUNCTIONS.put("+", Double::sum);
        FUNCTIONS.put("-", (num1, num2) -> num1 - num2);
        FUNCTIONS.put("/", (num1, num2) -> num1 / num2);
        FUNCTIONS.put("*", (num1, num2) -> num1 * num2);
        FUNCTIONS.put("^", Math::pow);
        FUNCTIONS.put("%", (num1, num2) -> num1 % num2);
    }

    public double apply(String name, Double num1, Double num2) {
        return FUNCTIONS.get(name).apply(num1, num2);
    }
}
