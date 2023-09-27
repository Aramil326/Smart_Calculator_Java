package s21.azathotp.model.graph;

import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import s21.azathotp.model.calculator.Calculator;
import s21.azathotp.model.exceptions.ExpressionError;

public class GraphModelTest {
    GraphModel graphModel = new GraphModel(new Calculator());

    @Test
    void Graph_Test_1() {
        Assertions.assertEquals(10000, graphModel.calculateGraphData(-10, 10, "sin(x)").size());
    }

    @Test
    void Graph_Test_2() {
        ObservableList<XYChart.Data<Number, Number>> actualData = graphModel.calculateGraphData(-10, 10, "5");
        for (XYChart.Data<Number, Number> actualDataItem : actualData) {
            Assertions.assertEquals(5.0, actualDataItem.getYValue());
        }
    }

    @Test
    void Graph_Test_1_Exception_Expected() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> graphModel.calculateGraphData(10, -10, "5"));
    }

    @Test
    void Graph_Test_2_Exception_Expected() {
        Assertions.assertThrows(ExpressionError.class, () -> graphModel.calculateGraphData(-10, 10, "sin(x"));
    }
}
