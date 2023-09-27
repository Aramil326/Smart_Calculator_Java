package s21.azathotp.model.calculator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import s21.azathotp.controllers.CalculatorController;
import s21.azathotp.model.helpers.FileExistingCheckerAndCreator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HistoryOfModel implements Serializable {
    private final Logger LOGGER = LogManager.getLogger(CalculatorController.class.getName());
    private final ObservableList<String> HISTORY = FXCollections.observableArrayList();
    private Path filePathFromLoaded = null;

    public void addNewRecord(String record) {
        HISTORY.add(record);
    }

    public void loadHistoryFromFile(Path path) {
        FileExistingCheckerAndCreator.checkOrCreateFile(path);

        try {
            List<String> strings = Files.readAllLines(path);
            HISTORY.addAll(strings);
            filePathFromLoaded = path;
        } catch (IOException e) {
            LOGGER.error(String.format("file doesn't exist: %s", path));
        }
    }

    public void writeHistoryToFile(Path path) {
        FileExistingCheckerAndCreator.checkOrCreateFile(path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toFile()))) {
            for (String record : HISTORY) {
                writer.write(record);
                writer.newLine();
            }
        } catch (IOException e) {
            LOGGER.error(String.format("file doesn't exist: %s", path));
        }
    }

    public void cleanHistory() {
        HISTORY.clear();
        if (filePathFromLoaded != null) {
            try (FileWriter fileWriter = new FileWriter(filePathFromLoaded.toFile())) {
                fileWriter.write("");
            } catch (IOException e) {
                LOGGER.error(String.format("file doesn't exist: %s", filePathFromLoaded));
            }
        }
    }

    public ObservableList<String> getHistory() {
        return HISTORY;
    }
}
