package config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FromENV {

    private static final Properties prop = new Properties();

    static {
        try (
                InputStream is = FromENV.class.getResourceAsStream("/config/config.properties")
        ) {
            if (is == null) {
                throw new FileNotFoundException("Could not find config.properties file");
            }
            prop.load(is);
        } catch (IOException e) {
            // Crash early during server startup if configuration is missing
            throw new ExceptionInInitializerError("Failed to load global configurations: " + e.getMessage());
        }
    }

    public static String get(String varName) {
        return prop.getProperty(varName);
    }
}
