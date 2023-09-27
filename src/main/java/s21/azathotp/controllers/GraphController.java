package s21.azathotp.controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import s21.azathotp.model.exceptions.ExpressionError;
import s21.azathotp.model.graph.GraphModel;
import s21.azathotp.viewConfiguration.ConfigurationExhibitor;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class GraphController implements Initializable {
    private final Logger LOGGER = LogManager.getLogger(CalculatorController.class.getName());
    private SceneController sceneController;
    private GraphModel graphModel;

    @FXML
    private AnchorPane graphPane;
    @FXML
    private ScatterChart<Number, Number> graph;
    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private TextField xMinInput;
    @FXML
    private TextField xMaxInput;
    @FXML
    private TextField yMinInput;
    @FXML
    private TextField yMaxInput;
    @FXML
    private TextField graphExpression;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UnaryOperator<TextFormatter.Change> allowOnlyDigitsLess2Million = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("-?\\d*") && Integer.parseInt(newText) <= 1000000
                    && Integer.parseInt(newText) >= -1000000) {
                return change;
            }
            return null;
        };

        xMinInput.setTextFormatter(new TextFormatter<>(allowOnlyDigitsLess2Million));
        xMaxInput.setTextFormatter(new TextFormatter<>(allowOnlyDigitsLess2Million));
        yMinInput.setTextFormatter(new TextFormatter<>(allowOnlyDigitsLess2Million));
        yMaxInput.setTextFormatter(new TextFormatter<>(allowOnlyDigitsLess2Million));

        graphPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onPrintGraphClicked();
            }
        });
    }

    public void setGraphModel(GraphModel graphModel) {
        this.graphModel = graphModel;
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    public void setConfiguration(ConfigurationExhibitor configurationExhibitor) {
        configurationExhibitor.setConfigurationFromFile(graph);
    }

    @FXML
    private void onCalculatorClicked() {
        sceneController.showScene("calculator");
    }

    @FXML
    private void onCreditClicked() {
        sceneController.showScene("credit");
    }

    @FXML
    private void onSettingsClicked() {
        sceneController.showScene("settings");
    }

    @FXML
    private void onInfoClicked() {
        sceneController.showScene("info");
    }

    @FXML
    private void onPrintGraphClicked() {
        int xMin = xMinInput.getText().isEmpty() ? -10 : Integer.parseInt(xMinInput.getText());
        int xMax = xMaxInput.getText().isEmpty() ? 10 : Integer.parseInt(xMaxInput.getText());
        int yMin = yMinInput.getText().isEmpty() ? -10 : Integer.parseInt(yMinInput.getText());
        int yMax = yMaxInput.getText().isEmpty() ? 10 : Integer.parseInt(yMaxInput.getText());

        graph.setScaleShape(true);

        if (xMin > xMax || yMin > yMax) {
            graphExpression.setText("The lower limit cannot be greater than or equal to the upper");
        } else {
            xAxis.setLowerBound(xMin);
            xAxis.setUpperBound(xMax);

            yAxis.setLowerBound(yMin);
            yAxis.setUpperBound(yMax);

            xAxis.setTickUnit((xMax - xMin) / 10D);
            yAxis.setTickUnit((yMax - yMin) / 10D);

            xAxis.setAutoRanging(false);
            yAxis.setAutoRanging(false);

            XYChart.Series<Number, Number> series = new XYChart.Series<>();

            if (!graphExpression.getText().isEmpty()) {
                try {
                    ObservableList<XYChart.Data<Number, Number>> data = graphModel.calculateGraphData(xMin, xMax, graphExpression.getText());
                    series.setData(data);
                    graph.getData().clear();
                    graph.getData().addAll(series);
                    LOGGER.info(graphExpression.getText());
                } catch (ExpressionError e) {
                    graphExpression.setText(e.getMessage());
                    LOGGER.info(graphExpression.getText() + ": " + e.getMessage());
                }
            } else {
                graphExpression.setText("");
            }
        }
    }
}
