package tj.horner.villagergpt.conversation.pipeline.actions

import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import me.cyperion.normalskyblock.GeminiConnection.NPC.Actions.ConversationMessageAction
import org.bukkit.Sound

class PlaySoundAction(private val player: Player, private val entity: Entity, private val sound: Sound) : ConversationMessageAction {
    override fun run() {
        player.playSound(entity, sound,1.0f,1.0f)
    }
}