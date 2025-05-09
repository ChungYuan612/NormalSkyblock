package me.cyperion.normalskyblock.GeminiConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public class SystemInstructionManager {
    private String systemMessage;

    public void setSystemMessage(String message) {
        this.systemMessage = message;
    }

    public void clearSystemMessage() {
        this.systemMessage = null;
    }

    public boolean hasSystemMessage() {
        return systemMessage != null && !systemMessage.isEmpty();
    }

    public JSONObject buildSystemInstructionJson() {
        if (!hasSystemMessage()) return null;

        JSONObject instruction = new JSONObject();
        JSONArray parts = new JSONArray();
        parts.put(new JSONObject().put("text", systemMessage));
        instruction.put("parts", parts);
        return instruction;
    }
}
