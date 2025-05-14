package me.cyperion.normalskyblock.GeminiConnection.NPC

import me.cyperion.normalskyblock.GeminiConnection.NPC.Actions.ConversationMessageAction
import me.cyperion.normalskyblock.GeminiConnection.Prompt.VillagerConversation

interface ConversationMessageProcessor {
    fun processMessage(message: String, conversation: VillagerConversation): Collection<ConversationMessageAction>?
}