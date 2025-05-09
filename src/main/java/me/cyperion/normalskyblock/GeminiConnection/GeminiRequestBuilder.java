package me.cyperion.normalskyblock.GeminiConnection;

import me.cyperion.normalskyblock.GeminiConnection.Prompt.VillagerConversation;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class GeminiRequestBuilder {
    private static final SystemInstructionManager instructionManager = new SystemInstructionManager();

    public void setSystemMessage(String message) {
        instructionManager.setSystemMessage(message);
    }

    public void clearSystemMessage() {
        instructionManager.clearSystemMessage();
    }

    public String build(List<GeminiMessage> messages) {
        JSONObject request = new JSONObject();


        if (instructionManager.hasSystemMessage()) {
            request.put("system_instruction", instructionManager.buildSystemInstructionJson());

        }
        JSONArray contentArray = new JSONArray();
        for (GeminiMessage message : messages) {
            JSONObject messageObject = new JSONObject();
            messageObject.put("role", message.getRole());

            JSONArray partsArray = new JSONArray();
            partsArray.put(new JSONObject().put("text", message.getText()));
            messageObject.put("parts", partsArray);

            contentArray.put(messageObject);
        }

        request.put("contents", contentArray);
        return request.toString();
    }
}

