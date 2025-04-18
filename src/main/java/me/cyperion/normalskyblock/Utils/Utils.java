package me.cyperion.normalskyblock.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Utils {

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void sendMessage(Player sender, MessageLevel level, String message) {
        sender.sendMessage(color(level.getPrefix() + message));
    }
}