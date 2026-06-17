package config;


import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {
    private static final Gson gson = new Gson();

    public static void sendJson(HttpServletResponse resp, int statusCode, String message, Object data) throws IOException {
        resp.setStatus(statusCode);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Set CORS headers so your Next.js server (port 3000) can talk to Tomcat (port 8080)
        resp.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type");

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", statusCode >= 200 && statusCode < 300);
        responseMap.put("message", message);
        if (data != null) {
            responseMap.put("data", data);
        }

        resp.getWriter().write(gson.toJson(responseMap));
    }
}