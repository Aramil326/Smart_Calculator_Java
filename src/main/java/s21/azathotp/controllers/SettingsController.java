package s21.azathotp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import s21.azathotp.model.cron.CronConfiguration;
import s21.azathotp.model.cron.CronConfigurationChanger;
import s21.azathotp.viewConfiguration.ConfigurationExhibitor;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    private SceneController sceneController;
    private CronConfigurationChanger cronConfigurationChanger;

    @FXML
    private RadioButton hourly;
    @FXML
    private RadioButton daily;
    @FXML
    private RadioButton monthly;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hourly.setSelected(true);

        setActionForRadioButtons(hourly, daily, monthly);
        setActionForRadioButtons(daily, hourly, monthly);
        setActionForRadioButtons(monthly, daily, hourly);
    }

    private void setActionForRadioButtons(RadioButton btn1, RadioButton btn2, RadioButton btn3) {
        btn1.setOnAction(event -> {
            if (btn1.isSelected()) {
                btn2.setSelected(false);
                btn3.setSelected(false);
            } else if (!btn1.isSelected()) {
                btn2.setSelected(true);
                btn3.setSelected(false);
            }
        });
    }

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    public void setConfiguration(ConfigurationExhibitor configurationExhibitor) {
        configurationExhibitor.setConfigurationFromFile(daily);
    }

    public void setCronConfigurationChanger(CronConfigurationChanger cronConfigurationChanger) {
        this.cronConfigurationChanger = cronConfigurationChanger;
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
    private void onCreditClicked() {
        sceneController.showScene("credit");
    }

    @FXML
    private void onInfoClicked() {
        sceneController.showScene("info");
    }

    @FXML
    private void onApplyClicked() {
        CronConfiguration cronConfiguration;

        if (hourly.isSelected()) {
            cronConfiguration = CronConfiguration.HOURLY;
        } else if (daily.isSelected()) {
            cronConfiguration = CronConfiguration.DAILY;
        } else {
            cronConfiguration = CronConfiguration.MONTHLY;
        }

        cronConfigurationChanger.changeCronConfiguration(cronConfiguration);
    }
}
