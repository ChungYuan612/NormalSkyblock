package tj.horner.villagergpt.conversation.pipeline.actions

import org.bukkit.entity.Villager
import org.bukkit.inventory.MerchantRecipe
import me.cyperion.normalskyblock.GeminiConnection.NPC.Actions.ConversationMessageAction

class SetTradesAction(private val villager: Villager, private val trades: List<MerchantRecipe>) : ConversationMessageAction {
    override fun run() {
        villager.recipes = trades
    }
}