package me.cyperion.normalskyblock.GeminiConnection.NPC;

import me.cyperion.normalskyblock.GeminiConnection.Prompt.VillagerConversation;

public interface ConversationMessageTransformer {
    public String transformMessage(String message,VillagerConversation conversation);
}
