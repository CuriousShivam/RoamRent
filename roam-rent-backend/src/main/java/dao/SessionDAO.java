package dao;

import com.google.gson.Gson;
import config.RedisConfig;
import redis.clients.jedis.RedisClient;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionDAO {
    private final Gson gson = new Gson();
    private static final int SIX_HOURS_IN_SECONDS = 6 * 60 * 60; // Strict 21600 sec threshold

    // Acquire the shared client link reference
    private RedisClient getClient() {
        return RedisConfig.getClient();
    }

    // 1. Issue a brand new, random rotated Session token tracking map data items
    public String createSession(Long userId, String email, String role) {
        String sessionId = UUID.randomUUID().toString();

        Map<String, String> sessionData = new HashMap<>();
        sessionData.put("userId", String.valueOf(userId));
        sessionData.put("email", email);
        sessionData.put("role", role);

        String jsonPayload = gson.toJson(sessionData);

        // Directly call execution parameters on the client without closing it!
        getClient().setex("session:" + sessionId, SIX_HOURS_IN_SECONDS, jsonPayload);

        // Append token reference string to the user id set block index map
        getClient().sadd("user_sessions:" + userId, sessionId);
        getClient().expire("user_sessions:" + userId, SIX_HOURS_IN_SECONDS);

        return sessionId;
    }

    public String createSession(Long userId, String email) {
        String sessionId = UUID.randomUUID().toString();

        Map<String, String> sessionData = new HashMap<>();
        sessionData.put("userId", String.valueOf(userId));
        sessionData.put("email", email);

        String jsonPayload = gson.toJson(sessionData);

        // Directly call execution parameters on the client without closing it!
        getClient().setex("session:" + sessionId, SIX_HOURS_IN_SECONDS, jsonPayload);

        // Append token reference string to the user id set block index map
        getClient().sadd("user_sessions:" + userId, sessionId);
        getClient().expire("user_sessions:" + userId, SIX_HOURS_IN_SECONDS);

        return sessionId;
    }

    // 2. Extract active key payload strings
    public Map<String, String> getSession(String sessionId) {
        String jsonPayload = getClient().get("session:" + sessionId);
        if (jsonPayload == null) return null;
        return gson.fromJson(jsonPayload, Map.class);
    }

    // 3. Rotate Session ID (Secures active data layers against cross-site hijacking leaks)
    public String rotateSession(String oldSessionId) {
        Map<String, String> currentData = getSession(oldSessionId);
        if (currentData == null) return null;

        // Revoke the old token address pointer reference completely
        destroySession(oldSessionId, Long.parseLong(currentData.get("userId")));

        // Issue a completely brand new UUID containing old profile data payloads
        return createSession(
                Long.parseLong(currentData.get("userId")),
                currentData.get("email"),
                currentData.get("role")
        );
    }

    // 4. Wipe a single device reference (Local logout)
    public void destroySession(String sessionId, Long userId) {
        getClient().del("session:" + sessionId);
        getClient().srem("user_sessions:" + userId, sessionId);
    }

    // 5. Remote Logout Mechanism: Target and destroy every active device token linked to a User ID
    public void remoteLogoutAllDevices(Long userId) {
        String trackingKey = "user_sessions:" + userId;

        // Pull down the comprehensive set list of device session ID tokens
        java.util.Set<String> activeSessions = getClient().smembers(trackingKey);

        if (activeSessions != null && !activeSessions.isEmpty()) {
            for (String sessionId : activeSessions) {
                getClient().del("session:" + sessionId); // Terminate active token
            }
        }
        getClient().del(trackingKey); // Clear master indexing lookup keys
    }
}