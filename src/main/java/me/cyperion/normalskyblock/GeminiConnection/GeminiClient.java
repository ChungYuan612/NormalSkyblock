package me.cyperion.normalskyblock.GeminiConnection;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * 負責與Gemini對話的主程式
 */
public class GeminiClient {
    private static final String MODEL_NAME = "gemini-2.0-flash";
    private static final String API_URL
            = "https://generativelanguage.googleapis.com/v1beta/models/"+MODEL_NAME+":generateContent";
    private final String apiKey;
    private final GeminiConversationManager conversationManager = new GeminiConversationManager();


    public GeminiClient(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     *
     * @param promptText 給Gemin的文字
     * @return Gemini 回傳的結果(提取文字完畢)
     */
    public String sendPrompt(String promptText) throws IOException, InterruptedException {
        // 加入使用者訊息
        conversationManager.addUserMessage(promptText);
        String requestBody = GeminiRequestBuilder.build(conversationManager.getConversation());
        System.out.println(requestBody);
        HttpURLConnection connection = GeminiHttpHelper
                .createPostConnection(API_URL + "?key=" + apiKey);

        GeminiHttpHelper.sendRequest(connection, requestBody);
        // 讀取回應
        String response = GeminiHttpHelper.readResponse(connection);
        System.out.println("raw response: " + response);
        String replyText = GeminiResponseParser.extractModelReply(response);
        if (replyText != null) {
            conversationManager.addModelMessage(replyText);
        }
        return replyText != null ? replyText : response;
    }

    public void clearConversation() {
        conversationManager.clearHistory();
    }


}