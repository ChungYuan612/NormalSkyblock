package me.cyperion.normalskyblock.GeminiConnection;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeminiHttpHelper {
    private static final int minIntervalMillis = 4000;
    private static long lastRequestTime = 0;
    public static HttpURLConnection createPostConnection(String fullUrl) throws IOException {
        URL url = new URL(fullUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        return conn;
    }

    public static void sendRequest(HttpURLConnection conn, String jsonBody) throws IOException, InterruptedException {
        waitIfNeeded();
        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonBody.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
    }

    public static String readResponse(HttpURLConnection conn) throws IOException {
        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode == HttpURLConnection.HTTP_OK) ?
                conn.getInputStream() : conn.getErrorStream();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) response.append(line);
            return response.toString();
        }
    }

    private static void waitIfNeeded() throws InterruptedException {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRequestTime;
        if (elapsed < minIntervalMillis) {
            long waitTime = minIntervalMillis - elapsed;
            System.out.println("等待 " + waitTime + " 毫秒以符合速率限制...");
            Thread.sleep(waitTime);
        }
        lastRequestTime = System.currentTimeMillis();
    }
}
