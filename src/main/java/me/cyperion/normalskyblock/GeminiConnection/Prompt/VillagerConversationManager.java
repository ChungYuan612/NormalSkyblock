package me.cyperion.normalskyblock.GeminiConnection.Prompt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;

public class VillagerConversationManager {
    private final Plugin plugin;
    private final List<VillagerConversation> conversations = new ArrayList<>();

    public VillagerConversationManager(Plugin plugin) {
        this.plugin = plugin;
    }

    public void endStaleConversations() {
        List<VillagerConversation> staleConversations = conversations.stream()
                .filter(it ->
                        it.getVillager().isDead() ||
                                !it.getPlayer().isOnline() ||
                                it.hasExpired() ||
                                it.hasPlayerLeft())
                .collect(Collectors.toList());

        endConversations(staleConversations);
    }

    public void endAllConversations() {
        endConversations(new ArrayList<>(conversations));
    }

    public VillagerConversation getConversation(Player player) {
        for (VillagerConversation conversation : conversations) {
            if (conversation.getPlayer().getUniqueId().equals(player.getUniqueId())) {
                return conversation;
            }
        }
        return null;
    }

    public VillagerConversation getConversation(Villager villager) {
        for (VillagerConversation conversation : conversations) {
            if (conversation.getVillager().getUniqueId().equals(villager.getUniqueId())) {
                return conversation;
            }
        }
        return null;
    }

    public VillagerConversation startConversation(Player player, Villager villager) {
        if (getConversation(player) != null || getConversation(villager) != null) {
            return null;
        }

        return getConversation(player, villager);
    }

    private VillagerConversation getConversation(Player player, Villager villager) {
        VillagerConversation conversation = null;

        for (VillagerConversation conv : conversations) {
            if (conv.getPlayer().getUniqueId().equals(player.getUniqueId()) &&
                    conv.getVillager().getUniqueId().equals(villager.getUniqueId())) {
                conversation = conv;
                break;
            }
        }

        if (conversation == null) {
            conversation = new VillagerConversation(plugin, villager, player);
            conversations.add(conversation);
        }

        return conversation;
    }

    public void endConversation(VillagerConversation conversation) {
        List<VillagerConversation> singleConversation = new ArrayList<>();
        singleConversation.add(conversation);
        endConversations(singleConversation);
    }

    private void endConversations(Collection<VillagerConversation> conversationsToEnd) {
        for (VillagerConversation conversation : conversationsToEnd) {
            conversation.ended = true;

        }

        conversations.removeAll(conversationsToEnd);
    }
}