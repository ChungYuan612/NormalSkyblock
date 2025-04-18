package me.cyperion.normalskyblock.GeminiConnection;

import java.io.IOException;
import java.net.HttpURLConnection;

public class GeminiClient {
    private static final String MODEL_NAME = "gemini-2.0-flash";
    private static final String API_URL
            = "https://generativelanguage.googleapis.com/v1beta/models/"+MODEL_NAME+":generateContent";
    private final String apiKey;

    public GeminiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public String sendPrompt(String promptText) throws IOException, InterruptedException {
        String requestBody = GeminiRequestBuilder.build(promptText);
        HttpURLConnection connection = GeminiHttpHelper
                .createPostConnection(API_URL + "?key=" + apiKey);

        GeminiHttpHelper.sendRequest(connection, requestBody);
        return GeminiHttpHelper.readResponse(connection);
    }
}