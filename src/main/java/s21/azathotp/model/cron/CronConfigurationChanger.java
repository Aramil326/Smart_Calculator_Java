package s21.azathotp.model.cron;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CronConfigurationChanger {
    private static final Logger LOGGER = LogManager.getLogger(CronConfigurationChanger.class);
    private final Path pathToLog4j;

    public CronConfigurationChanger(Path pathToLog4j) {
        this.pathToLog4j = pathToLog4j;
    }

    public void changeCronConfiguration(CronConfiguration cronConfiguration) {
        String newCronString = getNewCronString(cronConfiguration);

        try {
            List<String> strings = Files.readAllLines(pathToLog4j);

            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToLog4j.toFile()));

            for (String string : strings) {
                if (string.contains("CronTriggeringPolicy")) {
                    writer.write(newCronString);
                } else {
                    writer.write(string);
                }

                writer.newLine();
            }

            writer.close();
        } catch (IOException e) {
            LOGGER.error("file doesn't exist: " + pathToLog4j);
        }
    }

    private String getNewCronString(CronConfiguration cronConfiguration) {
        String templateForCronSettings = "                <CronTriggeringPolicy schedule=\"%s\"/>";
        String hourlyCronSetting = "0 * * * * ?";
        String dailyCronSetting = "0 0 * * * ?";
        String monthlyCronSetting = "0 0 1 * * ?";

        switch (cronConfiguration) {
            case HOURLY:
                return String.format(templateForCronSettings, hourlyCronSetting);
            case DAILY:
                return String.format(templateForCronSettings, dailyCronSetting);
            case MONTHLY:
                return String.format(templateForCronSettings, monthlyCronSetting);
        }
        return "";
    }
}
