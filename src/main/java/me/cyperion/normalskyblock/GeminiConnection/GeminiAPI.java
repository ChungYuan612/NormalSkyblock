package me.cyperion.normalskyblock.GeminiConnection;

import me.cyperion.normalskyblock.GeminiConnection.GeminiResponseParser.GeminiResponse;
import me.cyperion.normalskyblock.NormalSkyblock;

import javax.annotation.Nullable;
import java.io.IOException;

public class GeminiAPI {
    private NormalSkyblock plugin;

    public static final String API_KEY = "AIzaSyBjh_4uy7i4lbxZyYl-_hiV4QXLziHq8C0";
    public static GeminiClient geminiClient = new GeminiClient(API_KEY);

    public GeminiAPI(NormalSkyblock plugin) {
        this.plugin = plugin;
    }

    @Nullable
    public String getResponse(String text) {
        //GeminiClient geminiClient = new GeminiClient(API_KEY);
        try {
            String response = geminiClient.sendPrompt(text);
            System.out.println("模型回答：\n" + response);
            return response;

        } catch (IOException | InterruptedException e) {
            System.err.println("錯誤：" + e.getMessage());
        }
        return null;
    }

    public void refreshAPIKey() {
        plugin.getConfig().getString("api-key");
        geminiClient = new GeminiClient(API_KEY);
    }

    public void test(String text) {
        //text = "我要怎麼讓你記住剛才的話?";

        GeminiClient geminiClient = new GeminiClient(API_KEY);

        try {
            String response = geminiClient.sendPrompt(text);
            System.out.println("Response:\n" + response);

            GeminiResponseParser.GeminiResponse gResponse = GeminiResponseParser.parse(response);

            System.out.println("模型回答：\n" + gResponse.getFirstTextResponse());
            System.out.println("總 Token 數：" + gResponse.usageMetadata.totalTokenCount);
            System.out.println("模型版本：" + gResponse.modelVersion);

        } catch (IOException | InterruptedException e) {
            System.err.println("錯誤：" + e.getMessage());
        }
    }
}