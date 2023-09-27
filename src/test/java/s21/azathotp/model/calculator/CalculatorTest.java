package s21.azathotp.model.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import s21.azathotp.model.exceptions.ExpressionError;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;


public class CalculatorTest {
    private final Calculator calculator = new Calculator();
    private final double EPS = 1e-7;

    @ParameterizedTest
    @ValueSource(strings = {"sqrt(-1)", "ln(-1)", "log(-1)", "asin(-2)", "asin(2)", "acos(-2)", "acos(2)"})
    void Calculator_IncorrectExpressions_NaN_Expected(String expression) {
        Assertions.assertEquals(Double.NaN, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Brackets_Test_1() {
        String expression = "1+1";
        double result = 1 + 1;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Brackets_Test_2() {
        String expression = "(1+1 )";
        double result = (1 + 1);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Brackets_Test_3() {
        String expression = "( 1+1 )";
        double result = (1 + 1);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Brackets_Test_4() {
        String expression = "( 1 +1 )";
        double result = (1 + 1);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Brackets_Test_5() {
        String expression = "( 1 + 1 )";
        double result = (1 + 1);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Brackets_Test_6() {
        String expression = "( (((1) + (1))) )";
        double result = ((((1) + (1))));
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Brackets_Test_7() {
        String expression = "(5*9)-(2+(3-(2/1)+35))";
        double result = (5 * 9) - (2 + (3 - (2) + 35));
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"(", "()", "1+(", "sin(60)cos(60)", ")1+9", "18/9.3)"})
    void Calculator_Brackets_Test_Exception_Expected(String expression) {
        Assertions.assertThrows(ExpressionError.class, () -> calculator.calculate(expression, ""));
    }

    @Test
    void Calculator_Addition_Test_1() {
        String expression = "1.000002 + 5";
        double result = 1.000002 + 5;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Addition_Test_2() {
        String expression = "1.000002 + 5 + 9 + 6";
        double result = 1.000002 + 5 + 9 + 6;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.54837495+1.34521343+", "(1.54837495+1.34521343)+", "1.54837495+1.34521343+", "1.54837495+1.34521343*+", "1.54837495+1.34521343 +( - (5)", "+"})
    void Calculator_Addition_Test_Exception_Expected(String expression) {
        Assertions.assertThrows(ExpressionError.class, () -> calculator.calculate(expression, ""));
    }

    @Test
    void Calculator_Subtraction_Test_1() {
        String expression = "(1 - 1)";
        double result = 0;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Subtraction_Test_2() {
        String expression = "(1 - 0)";
        double result = 1;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Subtraction_Test_3() {
        String expression = "(1 - 0.000001)";
        double result = (1 - 0.000001);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1-0.000001-", "-"})
    void Calculator_Subtraction_Test_Exception_Expected(String expression) {
        Assertions.assertThrows(ExpressionError.class, () -> calculator.calculate(expression, ""));
    }

    @Test
    void Calculator_Multiplication_Test_1() {
        String expression = "156156 * 3";
        double result = 156156 * 3;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Multiplication_Test_2() {
        String expression = "156156 * 3 * 3 * 0.1 * 0.1";
        double result = 156156 * 3 * 3 * 0.1 * 0.1;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"156156*3*3*0.1*0.1*", "*156156*3*3*0.1*0.1", "156156*3*3*0.1*0.1*()"})
    void Calculator_Multiplication_Test_Exception_Expected(String expression) {
        Assertions.assertThrows(ExpressionError.class, () -> calculator.calculate(expression, ""));
    }

    @Test
    void Calculator_Division_Test_1() {
        String expression = "156 / 2";
        double result = (double) 156 / 2;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Division_Test_2() {
        String expression = "512 / 2 / 2 / 2";
        double result = (double) 512 / 2 / 2 / 2;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Division_Test_3() {
        String expression = "512 / 2 / 2 / 2 / 0.5";
        double result = (double) 512 / 2 / 2 / 2 / 0.5;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1/0.5/", "1//", "/1", "1//2"})
    void Calculator_Division_Test_Exception_Expected(String expression) {
        Assertions.assertThrows(ExpressionError.class, () -> calculator.calculate(expression, ""));
    }

    @Test
    void Calculator_Pow_Test_1() {
        String expression = "2^3^2";
        double result = Math.pow(2, Math.pow(3, 2));
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Pow_Test_2() {
        String expression = "2^(3+3)^2";
        double result = Math.pow(2, Math.pow(3 + 3, 2));
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Pow_Test_3() {
        String expression = "2^(3+3)^-2";
        double result = Math.pow(2, Math.pow(3 + 3, -2));
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Pow_Test_4() {
        String expression = "-sqrt(3.141592^log(5-3.141592))+log(55/(2+3.141592))";
        double result = -0.1372804;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Pow_Test_5() {
        String expression = "-2^2";
        double result = -4;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Pow_Test_6() {
        String expression = "(-2)^2";
        double result = 4;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Pow_Test_7() {
        String expression = "ln((9.980000)^123)";
        double result = 282.9717201;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2^2^", "^2^2", "2^^2"})
    void Calculator_Pow_Test_Exception_Expected(String expression) {
        Assertions.assertThrows(ExpressionError.class, () -> calculator.calculate(expression, ""));
    }

    @Test
    void Calculator_Mod_Test_1() {
        String expression = "3.4%3";
        double result = 0.4;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Mod_Test_2() {
        String expression = "(0.3+ 0.3)%0.3";
        double result = 0;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"% 0", "%1", "1%", "1% %3"})
    void Calculator_Mod_Test_Exception_Expected(String expression) {
        Assertions.assertThrows(ExpressionError.class, () -> calculator.calculate(expression, ""));
    }

    @Test
    void Calculator_Uno_Plus_Test_1() {
        String expression = "+1";
        double result = 1;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Uno_Plus_Test_2() {
        String expression = "+(1)";
        double result = 1;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Uno_Plus_Test_3() {
        String expression = "1+(1)";
        double result = 2;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Uno_Plus_Test_4() {
        String expression = "1+(+1)";
        double result = 2;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Uno_Plus_Test_5() {
        String expression = "1++1";
        double result = 2;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Uno_Plus_Test_6() {
        String expression = "+1++1++1++1";
        double result = 4;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"+1.54837495+++1.34521343", "+++1.54837495+1.34521343"})
    void Calculator_Uno_Plus_Test_Exception_Expected(String expression) {
        Assertions.assertThrows(ExpressionError.class, () -> calculator.calculate(expression, ""));
    }

    @Test
    void Calculator_Uno_Minus_Test_1() {
        String expression = "-1";
        double result = -1;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Uno_Minus_Test_2() {
        String expression = "-(1)";
        double result = -1;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Uno_Minus_Test_3() {
        String expression = "1-(1)";
        double result = 0;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Uno_Minus_Test_4() {
        String expression = "1-(-1)";
        double result = 2;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Uno_Minus_Test_5() {
        String expression = "1--1";
        double result = 2;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Uno_Minus_Test_6() {
        String expression = "-1--1--1--1";
        double result = 2;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1.54837495---1.34521343", "---1.54837495-1.34521343"})
    void Calculator_Uno_Minus_Test_Exception_Expected(String expression) {
        Assertions.assertThrows(ExpressionError.class, () -> calculator.calculate(expression, ""));
    }

    @Test
    void Calculator_Sin_Test_1() {
        String expression = "sin(90)";
        double result = Math.sin(90);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Cos_Test_1() {
        String expression = "cos(90)";
        double result = Math.cos(90);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Tan_Test_1() {
        String expression = "tan(90)";
        double result = Math.tan(90);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Sqrt_Test_1() {
        String expression = "sqrt(90)";
        double result = Math.sqrt(90);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Log_Test_1() {
        String expression = "log(90)";
        double result = Math.log10(90);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Ln_Test_1() {
        String expression = "ln(90)";
        double result = Math.log(90);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Acos_Test_1() {
        String expression = "acos(90)";
        double result = Math.acos(90);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Asin_Test_1() {
        String expression = "asin(90)";
        double result = Math.asin(90);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Atan_Test_1() {
        String expression = "atan(90)";
        double result = Math.atan(90);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Long_Expressions_Test_1() {
        String expression = "1.54837495 + 1.34521343 ++9.3 --9.8";
        double result = 21.99358838;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Long_Expressions_Test_2() {
        String expression = "1+1/3*2^12-44";
        double result = 1322.3333333;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Long_Expressions_Test_3() {
        String expression = "(1+2)*4*cos(3.141592*7-2)+sin(2*3.141592)";
        double result = 4.9938107;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Long_Expressions_Test_4() {
        String expression = "-(3) * (-3.141592 - (7 * (-(-(-(-7))))))";
        double result = -(3) * (-3.141592 - (7 * ((7))));
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Long_Expressions_Test_5() {
        String expression = "sin(sin(sin(sin(sin(90)))))";
        double result = Math.sin(Math.sin(Math.sin(Math.sin(Math.sin(90)))));
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Long_Expressions_Test_6() {
        String expression = "(123) * (123)";
        double result = (123) * (123);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Long_Expressions_Test_7() {
        String expression = "(123)";
        double result = (123);
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @Test
    void Calculator_Long_Expressions_Test_8() {
        String expression = "1E+10-5.9";
        double result = 1E+10 - 5.9;
        Assertions.assertEquals(result, calculator.calculate(expression, ""), EPS);
    }

    @ParameterizedTest
    @ValueSource(strings = {"*1", "1(", "1)", "asin", "()", "1.2.3", "tan()+3", "sqrt4", "-*13", "90-3*()*3", "123-34-", "sin(3+4-)", "%", "."})
    void Calculator_Test_Exception_Expected(String expression) {
        Assertions.assertThrows(ExpressionError.class, () -> calculator.calculate(expression, ""));
    }

    @Test
    void Calculator_Calculate_With_Save_History_Test_1() throws IOException {
        Path historyFile = Files.createTempFile("history_file", ".txt");
        List<String> expressionsExpected = Arrays.asList("1+3", "sin(9)*9", "(1+9)");
        Calculator calculatorWithSaving = new Calculator(historyFile);

        for (String expression : expressionsExpected) {
            calculatorWithSaving.calculate(expression, "", true);
        }

        calculatorWithSaving.saveHistory(historyFile);
        List<String> expressionsActual = Files.readAllLines(historyFile);

        for (int i = 0; i < expressionsExpected.size() && i < expressionsActual.size(); i++) {
            Assertions.assertEquals(expressionsExpected.get(i), expressionsActual.get(i).substring(0, expressionsActual.get(i).indexOf(" =")));
        }

        calculatorWithSaving.cleanHistory();
        expressionsActual = Files.readAllLines(historyFile);
        Assertions.assertEquals(0, expressionsActual.size());
    }
}
