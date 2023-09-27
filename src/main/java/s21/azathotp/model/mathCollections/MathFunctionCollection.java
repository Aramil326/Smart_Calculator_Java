package s21.azathotp.model.mathCollections;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

public class MathFunctionCollection {
    private final Map<String, UnaryOperator<Double>> FUNCTIONS;

    public MathFunctionCollection() {
        FUNCTIONS = new HashMap<>();
        FUNCTIONS.put("sin", Math::sin);
        FUNCTIONS.put("cos", Math::cos);
        FUNCTIONS.put("tan", Math::tan);
        FUNCTIONS.put("acos", Math::acos);
        FUNCTIONS.put("asin", Math::asin);
        FUNCTIONS.put("atan", Math::atan);
        FUNCTIONS.put("sqrt", Math::sqrt);
        FUNCTIONS.put("ln", Math::log);
        FUNCTIONS.put("log", Math::log10);
    }

    public double apply(String name, Double num) {
        return FUNCTIONS.get(name).apply(num);
    }
}
