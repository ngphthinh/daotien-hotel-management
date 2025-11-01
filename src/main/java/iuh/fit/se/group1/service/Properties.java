package iuh.fit.se.group1.service;

import java.io.*; 

public class Properties extends java.util.Properties {  // Extend java.util.Properties to inherit all methods (load, store, getProperty, setProperty)

    // Đọc file properties
    public static Properties loadProperties(String filePath) {
        Properties props = new Properties();  // This now creates an instance of the extended class
        try (InputStream input = new FileInputStream(filePath)) {
            props.load(input);  // Calls inherited super.load() - no longer throws exception
        } catch (IOException e) {
            System.err.println("Không thể đọc file: " + filePath);
        }
        return props;
    }

    // Lưu properties vào file
    public static void saveProperties(String filePath, Properties props) {
        try (OutputStream output = new FileOutputStream(filePath)) {
            props.store(output, "Updated by application");  // Calls inherited super.store()
        } catch (IOException e) {
            System.err.println("Không thể ghi file: " + filePath);
        }
    }

    // Lấy giá trị theo key
    public static String get(String filePath, String key) {
        Properties props = loadProperties(filePath);
        return props.getProperty(key, "");  // Calls inherited super.getProperty()
    }

    // Set key-value và lưu
    public static void set(String filePath, String key, String value) {
        Properties props = loadProperties(filePath);
        props.setProperty(key, value);  // Fixed: Use inherited super.setProperty() instead of getProperty
        saveProperties(filePath, props);
    }

    // Removed all unimplemented private methods and static load - now fully inherited and functional
}