package iuh.fit.se.group1.infrastructure;

import iuh.fit.se.group1.config.AppLogger;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JPAUtil {
    private static final String PERSISTENCE_UNIT_NAME = "daotien";
    private static final String PROPERTIES_FILE = "application-secret.properties";
    private static final EntityManagerFactory emf;

    static {
        try (var input = JPAUtil.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new IOException("Unable to find application-secret.properties");
            }

            Properties properties = new Properties();

            properties.load(input);
            String url = properties.getProperty("datasource.url");
            String username = properties.getProperty("datasource.username");
            String password = properties.getProperty("datasource.password");

            Map<String, String > jpaProp = new HashMap<>();
            jpaProp.put("jakarta.persistence.jdbc.user", username);
            jpaProp.put("jakarta.persistence.jdbc.password", password);
            jpaProp.put("jakarta.persistence.jdbc.url", url);



            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME,jpaProp);
        } catch (Exception e) {
            AppLogger.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

}
