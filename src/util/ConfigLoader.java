package util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * A utility class to load and save configuration properties from/to a file.
 */
public class ConfigLoader {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";

    static {
        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Warning: config.properties file not found. Using default values.");
            // Set default values if file is not found
            properties.setProperty("data.filepath", "books.dat");
            properties.setProperty("user.admin.username", "admin");
            properties.setProperty("user.admin.password", "admin123");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    /**
     * Saves a property to the config.properties file.
     * @param key The key of the property to save.
     * @param value The value of the property to save.
     */
    public static void saveProperty(String key, String value) {
        try (OutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.setProperty(key, value);
            properties.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
