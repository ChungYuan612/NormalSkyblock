package me.cyperion.normalskyblock.GeminiConnection;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;

public class GeminiRequestBuilder {
//    public static String build(String promptText) {
//        return "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + escapeJson(promptText) + "\" }] }] }";
//    }
//
//    private static String escapeJson(String text) {
//        return text.replace("\"", "\\\"").replace("\n", "\\n");
//    }

    public static String build(List<GeminiMessage> messages) {
        JsonObject request = new JsonObject();
        JsonArray contents = new JsonArray();

        for (GeminiMessage message : messages) {
            JsonObject content = new JsonObject();
            content.addProperty("role", message.getRole());
            JsonArray parts = new JsonArray();
            JsonObject part = new JsonObject();
            part.addProperty("text", message.getText());
            parts.add(part);
            content.add("parts", parts);
            contents.add(content);
        }

        request.add("contents", contents);
        return request.toString();
    }

}
