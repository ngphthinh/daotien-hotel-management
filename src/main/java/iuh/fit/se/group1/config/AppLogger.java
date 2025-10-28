package iuh.fit.se.group1.config;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AppLogger {
    private static final Logger logger = Logger.getLogger(AppLogger.class.getName());

    static {
        try {
            FileHandler fileHandler = new FileHandler("app.log", true); // true = append
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void info(String message, Object... params) {
        logger.info(message.formatted(params));
    }

    public static void error(String message, Throwable throwable) {
        logger.severe(message + " - " + throwable.getMessage());
    }
    public static void error(String message) {
        logger.severe(message);
    }


}
