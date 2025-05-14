package me.cyperion.normalskyblock.GeminiConnection.NPC;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Villager;

public class MessageFormatter {

    public static Component formatMessageFromPlayer(Component message, Villager villager) {
        return formatMessage(message, playerComponent(), villagerComponent(villager));
    }

    public static TextComponent formatMessageFromVillager(Component message, Villager villager) {
        return formatMessage(message, villagerComponent(villager), playerComponent());
    }

    private static TextComponent formatMessage(
            Component message,
            Component sender,
            Component recipient
    ) {
        return Component.text()
                .content("")
                .append(sender)
                .append(Component.text(" → ").color(NamedTextColor.WHITE))
                .append(recipient)
                .append(Component.text(": "))
                .append(message)
                .build();
    }

    public static Component clickableLink(
            Component formattedMessage,
            String message,
            Logger logger
    ) {
        // Regex pattern for extracting URLs based on RFC 3986
        Pattern pattern = Pattern.compile(
                "\\b(?:https?|ftp):\\/\\/[-A-Z0-9+&@#\\/%?=~_|!:,.;]*[-A-Z0-9+&@#\\/%=~_|]",
                Pattern.CASE_INSENSITIVE
        );
        Matcher matcher = pattern.matcher(message);

        if (!matcher.find()) {
            return formattedMessage;
        }
        String link = matcher.group(0);

        logger.warning("Found URL: " + link);

        return (TextComponent) formattedMessage
                .clickEvent(ClickEvent.openUrl(link))
                .hoverEvent(HoverEvent.showText(Component.text("請自行判斷 AI 提供的網址是否安全！")));
    }

    private static Component playerComponent() {
        return Component.text("你").color(NamedTextColor.DARK_AQUA);
    }

    private static Component villagerComponent(Villager villager) {
        return Component.text(villager.getName()).color(NamedTextColor.AQUA);
    }
}