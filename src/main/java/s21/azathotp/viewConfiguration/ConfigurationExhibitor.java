package s21.azathotp.viewConfiguration;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import s21.azathotp.controllers.CalculatorController;
import s21.azathotp.model.helpers.FileExistingCheckerAndCreator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class ConfigurationExhibitor {
    private final Logger LOGGER = LogManager.getLogger(CalculatorController.class.getName());
    private final Path pathToConfiguration;
    private String BACKGROUND_COLOR = "#E6E7EE";
    private String BORDER_RADIUS = "20px";
    private String BORDER_COLOR = "#44476A";

    public ConfigurationExhibitor(Path pathToConfiguration) {
        this.pathToConfiguration = pathToConfiguration;
    }

    public void setConfigurationFromFile(Node node) {
        getConfigurationFromFile();

        String properties = String.format("    -fx-border-radius: %s;\n" +
                "    -fx-border-color: %s;\n" +
                "    -fx-background-color: %s;\n", BORDER_RADIUS, BORDER_COLOR, BACKGROUND_COLOR);

        Set<Node> buttonNodes = node.getScene().getRoot().lookupAll("#button");
        for (Node elem : buttonNodes) {
            if (elem instanceof Button) {
                Button button = (Button) elem;
                button.setStyle(properties);
            }
        }

        Set<Node> labelNodes = node.getScene().getRoot().lookupAll("#label");
        for (Node elem : labelNodes) {
            if (elem instanceof Label) {
                Label label = (Label) elem;
                label.setStyle(properties);

            }
        }

        Set<Node> textFieldNodes = node.getScene().getRoot().lookupAll("#input");
        for (Node elem : textFieldNodes) {
            if (elem instanceof TextField) {
                TextField textField = (TextField) elem;
                textField.setStyle(properties);
            }
        }
    }

    private void getConfigurationFromFile() {
        FileExistingCheckerAndCreator.checkOrCreateFile(pathToConfiguration);
        setDefaultConfigurationInFile();

        try (FileReader in = new FileReader(pathToConfiguration.toFile());
             BufferedReader reader = new BufferedReader(in)) {
            while (reader.ready()) {
                String[] curLine = reader.readLine().split("=");
                if (curLine.length == 2) {
                    switch (curLine[0]) {
                        case "BACKGROUND-COLOR":
                            if (isHexCorrect(curLine[1]))
                                BACKGROUND_COLOR = curLine[1].toUpperCase();
                            break;
                        case "BORDER_RADIUS":
                            if (isPxCorrect(curLine[1]))
                                BORDER_RADIUS = curLine[1].toLowerCase();
                            break;
                        case "BORDER_COLOR":
                            if (isHexCorrect(curLine[1]))
                                BORDER_COLOR = curLine[1].toUpperCase();
                            break;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error(String.format("file doesn't exist: %s", pathToConfiguration));
        }
    }

    private void setDefaultConfigurationInFile() {
        try {
            List<String> strings = Files.readAllLines(pathToConfiguration);
            if (strings.isEmpty()) {
                String selectors = String.format("BACKGROUND-COLOR=%s\nBORDER_RADIUS=%s\nBORDER_COLOR=%s", BACKGROUND_COLOR, BORDER_RADIUS, BORDER_COLOR);

                BufferedWriter writer = new BufferedWriter(new FileWriter(pathToConfiguration.toFile()));
                writer.write(selectors);
                writer.newLine();

                writer.close();
            }
        } catch (IOException e) {
            LOGGER.error(String.format("file doesn't exist: %s", pathToConfiguration));
        }
    }

    private boolean isHexCorrect(String hex) {
        String hexPattern = "^#([A-Fa-f0-9]{6})$";
        return hex.matches(hexPattern);
    }

    private boolean isPxCorrect(String px) {
        String pxPattern = "^[1-9]\\d*px$";
        return px.matches(pxPattern);
    }
}
