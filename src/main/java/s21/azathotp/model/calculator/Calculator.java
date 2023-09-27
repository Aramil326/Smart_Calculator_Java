package s21.azathotp.model.calculator;

import javafx.collections.ObservableList;
import s21.azathotp.model.exceptions.ExpressionError;
import s21.azathotp.model.helpers.StringPreparer;

import java.nio.file.Path;
import java.util.List;

public class Calculator {
    private final HistoryOfModel HISTORY;

    public Calculator() {
        HISTORY = new HistoryOfModel();
    }

    public Calculator(Path pathToLoad) {
        this();
        HISTORY.loadHistoryFromFile(pathToLoad);
    }

    public double calculate(String expression, String x) throws ExpressionError {
        String preparedInput = StringPreparer.prepareExpression(expression, x);
        List<String> postfix = FromInfixToPostfixConverter.convert(preparedInput);
        double result = FromPostfixToResult.getResult(postfix);

        return Math.ceil(result * 1E+7) / 1E+7;
    }

    public double calculate(String expression, String x, boolean needToSave) throws ExpressionError {
        String preparedInput = StringPreparer.prepareExpression(expression, x);
        List<String> postfix = FromInfixToPostfixConverter.convert(preparedInput);
        double result = FromPostfixToResult.getResult(postfix);
        if (needToSave) {
            HISTORY.addNewRecord(String.format("%s = %s", preparedInput, result));
        }
        return Math.ceil(result * 1E+7) / 1E+7;
    }

    public void saveHistory(Path path) {
        HISTORY.writeHistoryToFile(path);
    }

    public void cleanHistory() {
        HISTORY.cleanHistory();
    }

    public ObservableList<String> getHistory() {
        return HISTORY.getHistory();
    }
}
