package me.cyperion.normalskyblock.GeminiConnection.NPC.Actions.actions;

import me.cyperion.normalskyblock.GeminiConnection.NPC.Actions.ConversationMessageAction
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.entity.Player


class SendPlayerMessageAction(private val player: Player, private val message: Component) : ConversationMessageAction {
    override fun run() {
        val legacyMessage: String = LegacyComponentSerializer.legacySection().serialize(message)
        player.sendMessage(legacyMessage)
    }
}