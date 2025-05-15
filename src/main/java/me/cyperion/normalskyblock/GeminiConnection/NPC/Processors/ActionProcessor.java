package me.cyperion.normalskyblock.GeminiConnection.NPC.Processors;

import me.cyperion.normalskyblock.GeminiConnection.NPC.Actions.ConversationMessageAction;
import me.cyperion.normalskyblock.GeminiConnection.NPC.Actions.ShakeHeadAction;
import me.cyperion.normalskyblock.GeminiConnection.NPC.ConversationMessageTransformer;
import me.cyperion.normalskyblock.GeminiConnection.Prompt.VillagerConversation;
import me.cyperion.normalskyblock.NPC.Actions.actions.PlaySoundAction;
import org.bukkit.Sound;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionProcessor implements ConversationMessageProcessor, ConversationMessageTransformer {
    private final Pattern actionRegex = Pattern.compile("ACTION:([A-Z_]+)");

    @Override
    public Collection<ConversationMessageAction> processMessage(
            String message,
            VillagerConversation conversation
    ) {
        Set<String> parsedActions = getActions(message);
        List<ConversationMessageAction> actions = new ArrayList<>();

        for (String action : parsedActions) {
            ConversationMessageAction messageAction = textToAction(action, conversation);
            if (messageAction != null) {
                actions.add(messageAction);
            }
        }

        return actions;
    }

    @Override
    public String transformMessage(String message, VillagerConversation conversation) {
        return actionRegex.matcher(message).replaceAll("").trim();
    }

    private Set<String> getActions(String message) {
        Set<String> actions = new HashSet<>();
        Matcher matcher = actionRegex.matcher(message);

        while (matcher.find()) {
            actions.add(matcher.group(1));
        }

        return actions;
    }

    private ConversationMessageAction textToAction(String text, VillagerConversation conversation) {
        switch (text) {
            case "SHAKE_HEAD":
                return new ShakeHeadAction(conversation.getVillager());
            case "SOUND_YES":
                return new PlaySoundAction(conversation.getPlayer(), conversation.getVillager(), Sound.ENTITY_VILLAGER_YES);
            case "SOUND_NO":
                return new PlaySoundAction(conversation.getPlayer(), conversation.getVillager(), Sound.ENTITY_VILLAGER_NO);
            case "SOUND_AMBIENT":
                return new PlaySoundAction(conversation.getPlayer(), conversation.getVillager(), Sound.ENTITY_VILLAGER_AMBIENT);
            default:
                return null;
        }
    }
}