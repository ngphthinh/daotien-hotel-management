package iuh.fit.se.group1.config;

import java.io.*;
import java.util.Properties;

public final class AppInfo {

    private static final String CONFIG_DIR = "config";
    private static final String CONFIG_FILE = CONFIG_DIR + "/app.properties";
    private static final String DEFAULT_VERSION = "1.0.0";

    private static final Properties props = new Properties();

    static {
        loadOrCreateConfig();
    }

    private static void loadOrCreateConfig() {
        try {
            File dir = new File(CONFIG_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File file = new File(CONFIG_FILE);

            if (!file.exists()) {
                // Tạo file mới nếu chưa có
                createDefaultConfig(file);
            }

            try (InputStream is = new FileInputStream(file)) {
                props.load(is);
            }

            // Nếu thiếu version → ghi lại
            if (!props.containsKey("app.version")) {
                props.setProperty("app.version", DEFAULT_VERSION);
                saveConfig(file);
            }

        } catch (IOException e) {
            System.err.println("Lỗi xử lý app.properties: " + e.getMessage());
        }
    }

    private static void createDefaultConfig(File file) throws IOException {
        props.setProperty("app.name", "Hotel Management");
        props.setProperty("app.version", DEFAULT_VERSION);
        props.setProperty("app.buildDate", "2025-12-21");

        saveConfig(file);
    }

    private static void saveConfig(File file) throws IOException {
        try (OutputStream os = new FileOutputStream(file)) {
            props.store(os, "Application Configuration");
        }
    }

    public static String getVersion() {
        return props.getProperty("app.version", DEFAULT_VERSION);
    }
}
