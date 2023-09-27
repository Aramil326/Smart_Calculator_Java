package s21.azathotp.model.calculator;

import s21.azathotp.model.exceptions.ExpressionError;
import s21.azathotp.model.helpers.StringChecker;
import s21.azathotp.model.mathCollections.MathBinaryFunctionCollection;
import s21.azathotp.model.mathCollections.MathFunctionCollection;

import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

public class FromPostfixToResult {
    private static final MathFunctionCollection MATH_FUNCTION_COLLECTION = new MathFunctionCollection();
    private static final MathBinaryFunctionCollection MATH_BINARY_FUNCTION_COLLECTION = new MathBinaryFunctionCollection();

    private FromPostfixToResult() {
    }

    public static double getResult(List<String> postfix) {
        Stack<Double> stack = new Stack<>();
        try {
            for (String token : postfix) {
                if (StringChecker.isNum(token)) {
                    stack.push(Double.parseDouble(token));
                } else if (StringChecker.isOperator(token)) {
                    if (token.equals("~")) {
                        stack.push(0 - stack.pop());
                    } else {
                        double num1 = stack.pop();
                        double num2 = stack.pop();
                        double tmp = MATH_BINARY_FUNCTION_COLLECTION.apply(token, num2, num1);
                        stack.push(tmp);
                    }
                } else if (StringChecker.isFunction(token)) {
                    double num = stack.pop();
                    double tmp = MATH_FUNCTION_COLLECTION.apply(token, num);
                    stack.push(tmp);
                }
            }
            if (stack.size() == 1) {
                return stack.pop();
            } else {
                throw new ExpressionError("invalid expression");
            }
        } catch (EmptyStackException e) {
            throw new ExpressionError("invalid expression");
        }
    }
}
