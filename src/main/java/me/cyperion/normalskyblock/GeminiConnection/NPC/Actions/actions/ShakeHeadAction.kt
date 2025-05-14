package tj.horner.villagergpt.conversation.pipeline.actions

import org.bukkit.entity.Villager
import me.cyperion.normalskyblock.GeminiConnection.NPC.Actions.ConversationMessageAction

class ShakeHeadAction(private val villager: Villager) : ConversationMessageAction {
    override fun run() {
        villager.shakeHead()
    }
}