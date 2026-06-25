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

            HikariConfig config = new HikariConfig();
            config.setDriverClassName("org.postgresql.Driver");
            config.setJdbcUrl(FromENV.get("DB_URL"));
            config.setUsername(FromENV.get("DB_USER"));
            config.setPassword(FromENV.get("DB_PASSWORD"));

            // Pool optimizations
            config.setMaximumPoolSize(Integer.parseInt(FromENV.get("MAX_POOL_SIZE")));
            config.setConnectionTimeout(Long.parseLong(FromENV.get("SET_TIME_OUT")));
            config.setIdleTimeout(Long.parseLong(FromENV.get("IDLE_TIME_OUT")));

            dataSource = new HikariDataSource(config);


    }

    // This method now borrows a connection instantly from the pool
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}