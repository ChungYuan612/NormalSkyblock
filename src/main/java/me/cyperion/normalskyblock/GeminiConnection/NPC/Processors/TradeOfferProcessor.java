package me.cyperion.normalskyblock.GeminiConnection.NPC.Processors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import me.cyperion.normalskyblock.GeminiConnection.NPC.Actions.ConversationMessageAction;
import me.cyperion.normalskyblock.GeminiConnection.NPC.Actions.actions.SendPlayerMessageAction;
import me.cyperion.normalskyblock.GeminiConnection.NPC.Actions.actions.SetTradesAction;
import me.cyperion.normalskyblock.GeminiConnection.NPC.MessageFormatter;
import me.cyperion.normalskyblock.GeminiConnection.Prompt.VillagerConversation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;

public class TradeOfferProcessor implements ConversationMessageProcessor {
    private final Logger logger;
    private final Gson gson;
    private final ItemFactory itemFactory;

    public TradeOfferProcessor(Logger logger) {
        this.logger = logger;
        this.gson = new Gson();
        this.itemFactory = Bukkit.getServer().getItemFactory();
    }

    @Override
    public Collection<ConversationMessageAction> processMessage(
            String message,
            VillagerConversation conversation
    ) {
        if(message == null || message.isEmpty()) {
            return new ArrayList<>();
        }
        Pattern tradeExpressionRegex = Pattern.compile("TRADE(\\[.+?\\])ENDTRADE");
        List<String> splitMessage = splitWithMatches(message, tradeExpressionRegex);

        List<MerchantRecipe> trades = new ArrayList<>();

        ComponentBuilder messageComponent = Component.text().content("");

        for (String part : splitMessage) {
            if (part.trim().startsWith("TRADE")) {
                String response = part.trim().replaceAll("(^TRADE)|(ENDTRADE$)", "");

                try {
                    MerchantRecipe trade = parseTradeResponse(response);
                    trades.add(trade);

                    TextComponent tradeMessage = chatFormattedRecipe(trade);
                    messageComponent.append(tradeMessage);
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Chat response contained invalid trade: " + response, e);
                    messageComponent.append(invalidTradeComponent(response));
                }
            } else {
                messageComponent.append(Component.text(part).color(NamedTextColor.WHITE));
            }
        }

        Component formattedMessage = MessageFormatter.formatMessageFromVillager(
                messageComponent.build(),
                conversation.getVillager()
        );

        formattedMessage = MessageFormatter.clickableLink(formattedMessage, message, logger);

        List<ConversationMessageAction> actions = new ArrayList<>();
        actions.add(new SetTradesAction(conversation.getVillager(), trades));
        actions.add(new SendPlayerMessageAction(conversation.getPlayer(), formattedMessage));

        return actions;
    }

    private MerchantRecipe parseTradeResponse(String text) {
        List<List<String>> tradeList = gson.fromJson(text, new TypeToken<List<List<String>>>(){}.getType());
        System.out.println("[TradeOfferProcessor/parseTradeResponse]：" + text);

        List<ItemStack> ingredients = new ArrayList<>();
        for (String item : tradeList.get(0)) {
            ingredients.add(parseItemStack(item));
        }

        List<ItemStack> results = new ArrayList<>();
        for (String item : tradeList.get(1)) {
            results.add(parseItemStack(item));
        }

        MerchantRecipe recipe = new MerchantRecipe(results.get(0), 1);
        recipe.setIngredients(ingredients);

        return recipe;
    }

    private ItemStack parseItemStack(String text) {
        Pattern pattern = Pattern.compile("([0-9]+) (.+)");
        Matcher matcher = pattern.matcher(text);

        if (!matcher.find()) {
            return new ItemStack(Material.AIR);
        }

        String numItems = matcher.group(1);
        String materialString = matcher.group(2);

        ItemStack stack = itemFactory.createItemStack(materialString);
        stack.setAmount(Math.min(Integer.parseInt(numItems), 64));

        return stack;
    }

    private TextComponent chatFormattedRecipe(MerchantRecipe recipe) {
        ComponentBuilder component = Component.text().content("[");

        List<ItemStack> ingredients = recipe.getIngredients();
        for (int index = 0; index < ingredients.size(); index++) {
            ItemStack it = ingredients.get(index);
            component.append(Component.text(it.getAmount() + " ").color(NamedTextColor.LIGHT_PURPLE));
            component.append(Component.text( it.getType().name()));

            if (index + 1 < ingredients.size()) {
                component.append(Component.text(" + "));
            } else {
                component.append(Component.text(" "));
            }
        }

        component.append(Component.text("→ "));
        component.append(
                Component.text(recipe.getResult().getAmount() + " ").color(NamedTextColor.LIGHT_PURPLE)
        );
        component.append(Component.text( recipe.getResult().getType().name()));
        component.append(Component.text("]"));

        component.color(NamedTextColor.DARK_GREEN);

        return (TextComponent) component.build();
    }

    private Component invalidTradeComponent(String rawTrade) {
        return Component.text()
                .content("[無效交易]")
                .hoverEvent(HoverEvent.showText(Component.text("回應中包含了一個無效的交易:\n\n" + rawTrade)))
                .color(NamedTextColor.RED)
                .build();
    }

    private List<String> splitWithMatches(String input, Pattern pattern) {
        List<String> result = new ArrayList<>();
        Matcher matcher = pattern.matcher(input);
        int lastIndex = 0;

        while (matcher.find()) {
            result.add(input.substring(lastIndex, matcher.start()));
            result.add(matcher.group());
            lastIndex = matcher.end();
        }

        if (lastIndex < input.length()) {
            result.add(input.substring(lastIndex));
        }

        return result;
    }
}