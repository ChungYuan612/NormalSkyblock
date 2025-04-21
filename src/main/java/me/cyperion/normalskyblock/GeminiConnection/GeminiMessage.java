package me.cyperion.normalskyblock.GeminiConnection;

public class GeminiMessage {
    private final String role;  // "user" 或 "model"
    private final String text;

    public GeminiMessage(String role, String text) {
        this.role = role;
        this.text = text;
    }

    public String getRole() {
        return role;
    }

    public String getText() {
        return text;
    }
}
