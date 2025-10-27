package iuh.fit.se.group1.util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
    private static final PropertiesReader instance = new PropertiesReader();
    private static final String PROPERTIES_FILE = "application-secret.properties";
    private final Properties properties = new Properties();

    private PropertiesReader() {
        try (var input = PropertiesReader.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {

            if (input == null) {
                throw new IOException("Không tìm thấy file cấu hình: " + PROPERTIES_FILE);
            }

            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc file cấu hình", e);
        }
    }

    public static PropertiesReader getInstance() {
        return instance;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public void printAll() {
        properties.forEach((k, v) -> System.out.println(k + " = " + v));
    }

    public Properties getAll() {
        return properties;
    }
}
