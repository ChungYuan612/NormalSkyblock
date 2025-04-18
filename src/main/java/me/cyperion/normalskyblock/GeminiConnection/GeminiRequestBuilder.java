package me.cyperion.normalskyblock.GeminiConnection;

public class GeminiRequestBuilder {
    public static String build(String promptText) {
        return "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + escapeJson(promptText) + "\" }] }] }";
    }

    private static String escapeJson(String text) {
        return text.replace("\"", "\\\"").replace("\n", "\\n");
    }
}
