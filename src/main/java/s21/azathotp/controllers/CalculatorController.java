package s21.azathotp.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import s21.azathotp.model.calculator.Calculator;
import s21.azathotp.model.exceptions.ExpressionError;
import s21.azathotp.viewConfiguration.ConfigurationExhibitor;

import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class CalculatorController implements Initializable {
    private final Logger LOGGER = LogManager.getLogger(CalculatorController.class.getName());
    private Calculator calculator;
    private SceneController sceneController;

    @FXML
    private AnchorPane calculatorPane;
    @FXML
    private TextField input;
    @FXML
    private TextField inputX;
    @FXML
    private TableView<String> tableView;
    @FXML
    private TableColumn<String, String> tableColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                String item = tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                input.setText(item.substring(0, item.indexOf(" =")));
            }
        });

        calculatorPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onEvaluateClicked();
            }
        });
    }

    public void initHistory() {
        tableColumn.getTableView().setItems(calculator.getHistory());
    }

    public void setCalculator(Calculator calculator) {
        this.calculator = calculator;
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    public void setTextFormatter(UnaryOperator<TextFormatter.Change> changeUnaryOperator) {
        inputX.setTextFormatter(new TextFormatter<>(changeUnaryOperator));
    }

    public void setConfiguration(ConfigurationExhibitor configurationExhibitor) {
        configurationExhibitor.setConfigurationFromFile(input);
    }

    @FXML
    private void onGraphClicked() {
        sceneController.showScene("graph");
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
    private void onOperandClicked(MouseEvent event) {
        Button button = (Button) event.getSource();
        String operand = button.getText();

        if (operand.length() > 1 || operand.equals("√")) {
            input.setText(input.getText() + operand + "(");
        } else {
            input.setText(input.getText() + operand);
        }
    }

    @FXML
    private void onDeleteOneCharClicked() {
        String inputText = input.getText();
        if (!input.getText().isEmpty()) {
            input.setText(inputText.substring(0, inputText.length() - 1));
        }
    }

    @FXML
    private void onClearClicked() {
        input.setText("");
    }

    @FXML
    private void onEvaluateClicked() {
        if (!input.getText().isEmpty()) {
            String inputString = input.getText();
            String inputXString = inputX.getText();
            try {
                double result = calculator.calculate(inputString, inputXString, true);
                String inputStringForLogger = inputString.replace("x", inputXString);
                input.setText(Double.toString(result));
                input.positionCaret(input.getText().length());
                LOGGER.info(inputStringForLogger + "=" + result);
            } catch (ExpressionError e) {
                input.setText(e.getMessage());
                LOGGER.info(inputString + ": " + e.getMessage());
            }
        }
    }

    public void saveHistoryOfModel(Path path) {
        calculator.saveHistory(path);
    }

    @FXML
    public void clearHistory() {
        calculator.cleanHistory();
    }
}
