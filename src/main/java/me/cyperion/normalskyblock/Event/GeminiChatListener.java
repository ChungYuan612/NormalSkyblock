package me.cyperion.normalskyblock.Event;

import me.cyperion.normalskyblock.GeminiConnection.GeminiAPI;
import me.cyperion.normalskyblock.GeminiConnection.GeminiClient;
import me.cyperion.normalskyblock.NormalSkyblock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GeminiChatListener implements Listener {

    private NormalSkyblock plugin;
    private final Set<UUID> activePlayers = new HashSet<>();

    public GeminiChatListener(NormalSkyblock plugin) {
        this.plugin = plugin;
    }

    public void addPlayer(Player player) {
        activePlayers.add(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "[AI] 你現在可以開始與 AI 對話了！");
    }

    public void removePlayer(Player player) {
        activePlayers.remove(player.getUniqueId());
        player.sendMessage(ChatColor.RED + "[AI] 你已離開 AI 對話模式。");
    }

    public boolean isPlayerActive(Player player) {
        return activePlayers.contains(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (!isPlayerActive(player)) return;

        event.setCancelled(true); // 不廣播原始訊息

        String userInput = event.getMessage();
        player.sendMessage(ChatColor.GRAY + "[你] " + userInput);

        // 非同步請求 Gemini
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String reply = plugin.getGeminiAPI().getResponse( userInput);
            player.sendMessage(ChatColor.AQUA + "[Gemini] " + reply);
        });
    }
}
