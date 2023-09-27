package s21.azathotp.model.graph;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import s21.azathotp.model.calculator.Calculator;
import s21.azathotp.model.exceptions.ExpressionError;

public class GraphModel {
    private final Calculator CALCULATOR;

    public GraphModel(Calculator calculator) {
        this.CALCULATOR = calculator;
    }

    public ObservableList<XYChart.Data<Number, Number>> calculateGraphData(int xMin, int xMax, String expression)
            throws ExpressionError, IllegalArgumentException {
        if (xMin >= xMax) {
            throw new IllegalArgumentException("xMin must be less than xMax");
        }
        ObservableList<XYChart.Data<Number, Number>> result = FXCollections.observableArrayList();
        double h = (xMax - xMin) / 10000D;

        for (double i = xMin; i <= xMax; i += h) {
            double tmp = CALCULATOR.calculate(expression, Double.toString(i));
            if (Double.isFinite(tmp)) {
                XYChart.Data<Number, Number> data = new XYChart.Data<>(i, tmp);
                Circle circle = new Circle(3);
                circle.setFill(Color.DARKBLUE);
                data.setNode(circle);
                result.add(data);
            }
        }

        return result;
    }
}
