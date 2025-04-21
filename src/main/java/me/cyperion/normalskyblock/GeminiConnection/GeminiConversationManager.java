package me.cyperion.normalskyblock.GeminiConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GeminiConversationManager {

    private final List<GeminiMessage> conversationHistory = new ArrayList<>();

    public void addUserMessage(String message) {
        conversationHistory.add(new GeminiMessage("user", message));
    }

    public void addModelMessage(String message) {
        conversationHistory.add(new GeminiMessage("model", message));
    }

    public List<GeminiMessage> getConversation() {
        return Collections.unmodifiableList(conversationHistory);
    }

    public void clearHistory() {
        conversationHistory.clear();
    }
}
