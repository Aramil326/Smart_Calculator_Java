package s21.azathotp.model.helpers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import s21.azathotp.controllers.CalculatorController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileExistingCheckerAndCreator {
    private static final Logger LOGGER = LogManager.getLogger(CalculatorController.class.getName());

    public static void checkOrCreateFile(Path path) {
        if (!Files.exists(path.getParent())) {
            try {
                Files.createDirectory(path.getParent());
            } catch (IOException e) {
                LOGGER.error(String.format("directory doesn't exist: %s", path.getParent()));
            }
        }

        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                LOGGER.error(String.format("file doesn't exist: %s", path));
            }
        }
    }
}
