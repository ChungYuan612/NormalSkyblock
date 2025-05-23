package me.cyperion.normalskyblock.Event;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class ConversationSession {
    private final Player player;
    private final Villager villager;

    public ConversationSession(Player player, Villager villager) {
        this.player = player;
        this.villager = villager;
    }

    public Player getPlayer() {
        return player;
    }

    public Villager getVillager() {
        return villager;
    }
}
