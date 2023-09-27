package s21.azathotp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import s21.azathotp.controllers.*;
import s21.azathotp.model.calculator.Calculator;
import s21.azathotp.model.credit.CreditModel;
import s21.azathotp.model.cron.CronConfigurationChanger;
import s21.azathotp.model.graph.GraphModel;
import s21.azathotp.viewConfiguration.ConfigurationExhibitor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.function.UnaryOperator;

public class App extends Application {
    public static void main(String[] args) {
        Application.launch(App.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader calculatorLoader = new FXMLLoader(getClass().getResource("/fxml/calculator.fxml"));
        FXMLLoader graphLoader = new FXMLLoader(getClass().getResource("/fxml/graph.fxml"));
        FXMLLoader creditLoader = new FXMLLoader(getClass().getResource("/fxml/credit.fxml"));
        FXMLLoader settingsLoader = new FXMLLoader(getClass().getResource("/fxml/settings.fxml"));
        FXMLLoader infoLoader = new FXMLLoader(getClass().getResource("/fxml/info.fxml"));

        Pane calcPane = calculatorLoader.load();
        Pane graphPane = graphLoader.load();
        Pane creditPane = creditLoader.load();
        Pane settingsPane = settingsLoader.load();
        Pane infoPane = infoLoader.load();

        Scene scene = new Scene(calcPane);
        SceneController sceneController = new SceneController(scene);

        scene.getStylesheets().clear();
        scene.getStylesheets().add("/css/style.css");

        sceneController.addPane("calculator", calcPane);
        sceneController.addPane("graph", graphPane);
        sceneController.addPane("credit", creditPane);
        sceneController.addPane("settings", settingsPane);
        sceneController.addPane("info", infoPane);

        Path pathForHistory = Paths.get("history/history.txt").toAbsolutePath();
        Calculator calculator = new Calculator(pathForHistory);

        UnaryOperator<TextFormatter.Change> allowOnlyDigits = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change;
            }
            return null;
        };

        ConfigurationExhibitor configurationExhibitor = new ConfigurationExhibitor(Paths.get("configuration/configuration.conf"));

        CalculatorController calculatorController = calculatorLoader.getController();
        prepareCalculatorController(calculatorController, sceneController, calculator, allowOnlyDigits, configurationExhibitor);

        scene.setRoot(graphPane);
        GraphController graphController = graphLoader.getController();
        prepareGraphController(graphController, sceneController, calculator, configurationExhibitor);

        scene.setRoot(creditPane);
        CreditController creditController = creditLoader.getController();
        prepareCreditController(creditController, sceneController, allowOnlyDigits, calculator, configurationExhibitor);

        scene.setRoot(settingsPane);
        SettingsController settingsController = settingsLoader.getController();
        prepareSettingsController(settingsController, sceneController, configurationExhibitor);

        scene.setRoot(infoPane);
        InfoController infoController = infoLoader.getController();
        prepareInfoController(infoController, sceneController, configurationExhibitor);


        stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("/img/icon.png"))));

        stage.setTitle("Calculator");
        scene.setRoot(calcPane);
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
        stage.setOnCloseRequest(e -> calculatorController.saveHistoryOfModel(pathForHistory));
    }

    private void prepareCalculatorController(CalculatorController calculatorController, SceneController sceneController,
                                             Calculator calculator, UnaryOperator<TextFormatter.Change> allowOnlyDigits,
                                             ConfigurationExhibitor configurationExhibitor) {
        calculatorController.setSceneController(sceneController);
        calculatorController.setCalculator(calculator);
        calculatorController.setTextFormatter(allowOnlyDigits);
        calculatorController.setConfiguration(configurationExhibitor);
        calculatorController.initHistory();
    }

    private void prepareGraphController(GraphController graphController, SceneController sceneController,
                                        Calculator calculator, ConfigurationExhibitor configurationExhibitor) {
        graphController.setSceneController(sceneController);
        graphController.setGraphModel(new GraphModel(calculator));
        graphController.setConfiguration(configurationExhibitor);
    }

    private void prepareCreditController(CreditController creditController, SceneController sceneController,
                                         UnaryOperator<TextFormatter.Change> allowOnlyDigits, Calculator calculator,
                                         ConfigurationExhibitor configurationExhibitor) {
        creditController.setSceneController(sceneController);
        creditController.setTextFormatter(allowOnlyDigits);
        creditController.setCreditModel(new CreditModel(calculator));
        creditController.setConfiguration(configurationExhibitor);
    }

    private void prepareSettingsController(SettingsController settingsController, SceneController sceneController,
                                           ConfigurationExhibitor configurationExhibitor) {
        settingsController.setSceneController(sceneController);
        settingsController.setConfiguration(configurationExhibitor);
        settingsController.setCronConfigurationChanger(new CronConfigurationChanger(Paths.get("/log4j2.xml")));
    }

    private void prepareInfoController(InfoController infoController, SceneController sceneController,
                                       ConfigurationExhibitor configurationExhibitor) {
        infoController.setSceneController(sceneController);
        infoController.setConfiguration(configurationExhibitor);
    }
}
