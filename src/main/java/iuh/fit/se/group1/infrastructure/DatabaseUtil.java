package iuh.fit.se.group1.infrastructure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseUtil {

    private static final Logger log = LoggerFactory.getLogger(DatabaseUtil.class);
    private static final Properties properties = new Properties();
    private static final Connection connection;
    private static final String PROPERTIES_FILE = "application-secret.properties";

    static {
        try (var input = DatabaseUtil.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {

            if (input == null) {
                throw new IOException("Unable to find application-secret.properties");
            }

            properties.load(input);

            String url = properties.getProperty("datasource.url");
            String username = properties.getProperty("datasource.username");
            String password = properties.getProperty("datasource.password");

            connection = DriverManager.getConnection(url, username, password);
            log.info("Database connection initialized successfully.");

        } catch (Exception e) {
            log.error("Error initializing database connection: ", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    private DatabaseUtil() {}

    public static Connection getConnection() {
        return connection;
    }
}
