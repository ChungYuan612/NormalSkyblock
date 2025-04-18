package me.cyperion.normalskyblock.NPC;

import me.cyperion.normalskyblock.NormalSkyblock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NgrokConnection {

    public static final String CONNECT_REQUEST_ERROR = "連線請求錯誤";

    private NormalSkyblock plugin;

    public NgrokConnection(NormalSkyblock plugin) {
        this.plugin = plugin;
    }

    // 設定 API URL
    //String apiUrl = "http://127.0.0.1:5000/v1/chat/completions";
    String apiUrl = "https://4dda-34-125-168-146.ngrok-free.app/v1/chat/completions";
    public String request(String text){
        apiUrl = plugin.getConfig().getString("api-url")+"/v1/chat/completions";
        try{

            HttpURLConnection connection = getHttpURLConnection(text);
            // 讀取回應
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("API 回應: " + response.toString());
                    return response.toString();
                }
            } else {
                System.out.println("HTTP 錯誤碼: " + responseCode);
            }
        }catch (IOException e) {
            System.out.println("NPC/NgrokConnection 發生錯誤: " + e);
        }
        return CONNECT_REQUEST_ERROR;
    }

    private HttpURLConnection getHttpURLConnection(String text) throws IOException {
        // 準備 JSON 請求資料
        String jsonInputString = "{"
                + "\"model\": \"google_gemma-3-12b-it-Q5_K_M.gguf\","
                + "\"messages\": [{\"role\": \"user\", \"content\": \""+ text +"\"}],"
                + "\"temperature\": 0.7"
                + "}";

        // 建立 HTTP 連線
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        // 傳送請求資料
        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection;
    }
}