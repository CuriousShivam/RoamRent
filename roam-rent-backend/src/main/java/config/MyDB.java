package config;
import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;

import java.util.Properties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.io.InputStream;
import java.io.FileNotFoundException;

public class MyDB {
    private static HikariDataSource dataSource;

    static {
        Properties prop = new Properties();
        // Loading via ClassLoader
        try (
                InputStream is = MyDB.class
                        .getResourceAsStream("/config/config.properties")
        ) {
            if (is == null) {
                throw new FileNotFoundException("Could not find config.properties file");
            }
            prop.load(is);

            HikariConfig config = new HikariConfig();
            config.setDriverClassName("org.postgresql.Driver");
            config.setJdbcUrl(prop.getProperty("DB_URL"));
            config.setUsername(prop.getProperty("DB_USER"));
            config.setPassword(prop.getProperty("DB_PASSWORD"));

            // Pool optimizations
            config.setMaximumPoolSize(Integer.parseInt(prop.getProperty("MAX_POOL_SIZE")));
            config.setConnectionTimeout(Long.parseLong(prop.getProperty("SET_TIME_OUT")));
            config.setIdleTimeout(Long.parseLong(prop.getProperty("IDLE_TIME_OUT")));

            dataSource = new HikariDataSource(config);

        } catch (IOException e) {
            throw new ExceptionInInitializerError("Failed to initialize database pool: " + e.getMessage());
        }
    }

    // This method now borrows a connection instantly from the pool
    public static Connection getConnection() throws SQLException {
        System.out.println("Connecting to DB...");
        return dataSource.getConnection();
    }
}