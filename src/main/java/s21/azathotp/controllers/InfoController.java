package s21.azathotp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import s21.azathotp.App;
import s21.azathotp.viewConfiguration.ConfigurationExhibitor;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class InfoController implements Initializable {
    private SceneController sceneController;

    @FXML
    private Button tempButton;
    @FXML
    private ScrollPane scrollPane;

    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    public void setConfiguration(ConfigurationExhibitor configurationExhibitor) {
        configurationExhibitor.setConfigurationFromFile(tempButton);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image image = new Image(Objects.requireNonNull(App.class.getResourceAsStream("/img/info.jpeg")));
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(1270);
        imageView.setPreserveRatio(true);
        scrollPane.setContent(imageView);
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
    private void onSettingsClicked() {
        sceneController.showScene("settings");
    }
}
