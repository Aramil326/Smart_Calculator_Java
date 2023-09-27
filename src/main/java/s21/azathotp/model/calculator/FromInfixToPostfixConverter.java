package s21.azathotp.model.calculator;

import s21.azathotp.model.exceptions.ExpressionError;
import s21.azathotp.model.helpers.PriorityQualifier;
import s21.azathotp.model.helpers.StringChecker;

import java.util.*;

public class FromInfixToPostfixConverter {
    private FromInfixToPostfixConverter() {
    }

    public static List<String> convert(String expression) throws ExpressionError {
        List<String> output = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        StringTokenizer tokenizer = new StringTokenizer(expression, "+-/*~%()^e", true);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (token.equals("e")) {
                handleExpCase(token, tokenizer, output);
            } else if (StringChecker.isNum(token)) {
                output.add(token);
            } else if (StringChecker.isFunction(token)) {
                stack.push(token);
            } else if (StringChecker.isOperator(token)) {
                handleOperatorCase(token, output, stack);
            } else if (StringChecker.isOpenBracket(token)) {
                stack.push(token);
            } else if (StringChecker.isCloseBracket(token)) {
                handleCloseBracketCase(output, stack);
            } else {
                throw new ExpressionError(String.format("Invalid character: %s", token));
            }
        }

        handleEndOfInputData(output, stack);

        return output;
    }

    private static void handleExpCase(String token, StringTokenizer tokenizer, List<String> output) throws ExpressionError {
        String numBeforeE = output.get(output.size() - 1);
        output.remove(output.size() - 1);

        try {
            output.add(numBeforeE + token + tokenizer.nextToken() + tokenizer.nextToken());
        } catch (NoSuchElementException e) {
            throw new ExpressionError("incorrect notation of exponential number");
        }
    }

    private static void handleOperatorCase(String token, List<String> output, Stack<String> stack) {
        while (!stack.empty() && StringChecker.isOperator(stack.peek())
                && (PriorityQualifier.getPriority(stack.peek()) > PriorityQualifier.getPriority(token)
                || PriorityQualifier.getPriority(stack.peek()) == PriorityQualifier.getPriority(token)
                && !token.equals("^"))) {
            output.add(stack.pop());
        }
        stack.push(token);
    }

    private static void handleCloseBracketCase(List<String> output, Stack<String> stack) throws ExpressionError {
        try {
            while (!StringChecker.isOpenBracket(stack.peek())) {
                output.add(stack.pop());
            }
            stack.pop();
            if (!stack.isEmpty() && StringChecker.isFunction(stack.peek())) {
                output.add(stack.pop());
            }
        } catch (EmptyStackException e) {
            throw new ExpressionError("Extra brackets in expression");
        }
    }

    private static void handleEndOfInputData(List<String> output, Stack<String> stack) throws ExpressionError {
        while (!stack.empty()) {
            if (StringChecker.isOpenBracket(stack.peek())) {
                throw new ExpressionError("Extra brackets in expression");
            }
            output.add(stack.pop());
        }
    }
}
