//package config;
//
//import redis.clients.jedis.JedisPool;
//import redis.clients.jedis.JedisPoolConfig;
//
//public class RedisConfig {
//    private static final JedisPool jedisPool;
//
//    static {
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(64); // Maximum active network links
//        poolConfig.setMaxIdle(16);  // Maximum idle links kept open
//
//        // Connect to your Redis server (Replace localhost if using a remote cloud Redis)
//        jedisPool = new JedisPool(poolConfig, "localhost", 6379);
//    }
//
//    public static JedisPool getPool() {
//        return jedisPool;
//    }
//}

package config;

import redis.clients.jedis.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Properties;

public class RedisConfig {
    private static final RedisClient redisClient;

    static {
        // 1. Use the modern pool configuration wrapper
        ConnectionPoolConfig poolConfig = new ConnectionPoolConfig();
        poolConfig.setMaxTotal(64); // Maximum active concurrent sockets
        poolConfig.setMaxIdle(16);  // Maximum idle connection buffers
        poolConfig.setMinIdle(4);   // Keep at least 4 connections warm
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setMaxWait(Duration.ofSeconds(2)); // Fail gracefully if pool is choked


        // 2. Build the unified client with built-in pooling automatically
        URI upstashUri = null;
        try {
            upstashUri = new URI(FromENV.get("REDIS_URL"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        // Extract username and password tokens safely
        String[] userInfo = upstashUri.getUserInfo().split(":");
        String username = userInfo[0];
        String password = userInfo[1];

        // 3. SECURE CONFIGURATION FIX: Map credentials and SSL inside the dedicated Client Config builder
        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder()
                .user(username)
                .password(password)
                .ssl(true) // Enforces TLS communication required by Upstash cloud endpoints
                .build();

        // 4. Encapsulate server coordinates
        HostAndPort endpoint = new HostAndPort(upstashUri.getHost(), upstashUri.getPort());

        // 5. Build the unified client using the modern structured parameters
        redisClient = RedisClient.builder()
                .hostAndPort(endpoint)
                .clientConfig(clientConfig)
                .poolConfig(poolConfig)
                .build();

    }


// Expose the thread-safe client instance directly
    public static RedisClient getClient() {
        return redisClient;
    }
}