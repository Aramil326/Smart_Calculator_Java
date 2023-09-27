package s21.azathotp.controllers;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import s21.azathotp.model.credit.CreditData;
import s21.azathotp.model.credit.CreditModel;
import s21.azathotp.model.exceptions.ExpressionError;
import s21.azathotp.viewConfiguration.ConfigurationExhibitor;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class CreditController implements Initializable {
    private final Logger LOGGER = LogManager.getLogger(CalculatorController.class.getName());
    private SceneController sceneController;
    private CreditModel creditModel;

    @FXML
    private AnchorPane creditPane;
    @FXML
    private TextField creditAmountInput;
    @FXML
    private TextField creditTermInput;
    @FXML
    private TextField interestRateInput;
    @FXML
    private Label overpayment;
    @FXML
    private Label totalPayment;
    @FXML
    private RadioButton annuity;
    @FXML
    private RadioButton differentiated;
    @FXML
    private TableView<String> monthlyPayments;
    @FXML
    private TableColumn<String, String> tableColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        creditPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                onEvaluateClicked();
            }
        });

        differentiated.setSelected(true);

        annuity.setOnAction(event -> {
            if (annuity.isSelected()) {
                differentiated.setSelected(false);
            } else if (!annuity.isSelected()) {
                differentiated.setSelected(true);
            }
        });
        differentiated.setOnAction(event -> {
            if (differentiated.isSelected()) {
                annuity.setSelected(false);
            } else if (!differentiated.isSelected()) {
                annuity.setSelected(true);
            }
        });

        tableColumn.setCellValueFactory(param -> new ReadOnlyStringWrapper(param.getValue()));
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    public void setCreditModel(CreditModel creditModel) {
        this.creditModel = creditModel;
        monthlyPayments.setItems(creditModel.getMonthlyPaymentData());
    }

    public void setTextFormatter(UnaryOperator<TextFormatter.Change> allowOnlyDigits) {
        creditAmountInput.setTextFormatter(new TextFormatter<>(allowOnlyDigits));
        creditTermInput.setTextFormatter(new TextFormatter<>(allowOnlyDigits));
        interestRateInput.setTextFormatter(new TextFormatter<>(allowOnlyDigits));
    }

    public void setConfiguration(ConfigurationExhibitor configurationExhibitor) {
        configurationExhibitor.setConfigurationFromFile(creditAmountInput);
    }

    @FXML
    private void onCalculatorClicked() {
        sceneController.showScene("calculator");
    }

    @FXML
    private void onGraphClicked() {
        sceneController.showScene("graph");
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
    public void onEvaluateClicked() {
        if (!creditAmountInput.getText().isEmpty() && !creditTermInput.getText().isEmpty()
                && !interestRateInput.getText().isEmpty()) {
            String creditAmount = creditAmountInput.getText();
            String creditTerm = creditTermInput.getText();
            String interestRate = interestRateInput.getText();

            CreditData creditData;
            try {
                if (annuity.isSelected()) {
                    creditData = creditModel.calculateAnnuity(creditAmount, creditTerm, interestRate);
                } else {
                    creditData = creditModel.calculateDifferentiated(creditAmount, creditTerm, interestRate);
                }
                overpayment.setText(creditData.getOverpayment());
                totalPayment.setText(creditData.getTotalPayment());
            } catch (ExpressionError e) {
                overpayment.setText(e.getMessage());
                totalPayment.setText(e.getMessage());
                LOGGER.error("credit error: " + e.getMessage());
            }
        }
    }
}
