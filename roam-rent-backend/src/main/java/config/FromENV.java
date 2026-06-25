package config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FromENV {
    public static String get (String varName){
        Properties prop = new Properties();
        try (
                InputStream is = FromENV.class
                        .getResourceAsStream("/config/config.properties")
        ) {
            if (is == null) {
                throw new FileNotFoundException("Could not find config.properties file");
            }
            prop.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop.getProperty(varName);
    }
}
