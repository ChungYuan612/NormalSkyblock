package me.cyperion.normalskyblock.GeminiConnection;

import me.cyperion.normalskyblock.GeminiConnection.NPC.Actions.ConversationMessageAction;
import me.cyperion.normalskyblock.GeminiConnection.NPC.TradeOfferProcessor;
import me.cyperion.normalskyblock.GeminiConnection.Prompt.VillagerConversation;
import me.cyperion.normalskyblock.GeminiConnection.Prompt.VillagerConversationManager;
import me.cyperion.normalskyblock.NormalSkyblock;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 負責與Gemini對話的主程式
 */
public class GeminiClient {
    private final NormalSkyblock plugin;
    private static final String MODEL_NAME = "gemini-2.0-flash";
    private static final String API_URL
            = "https://generativelanguage.googleapis.com/v1beta/models/"+MODEL_NAME+":generateContent";
    private final String apiKey;
    private final GeminiConversationManager conversationManager = new GeminiConversationManager();
    private final VillagerConversationManager villagerConversationManager;
    private final TradeOfferProcessor processor;

    public GeminiClient(NormalSkyblock plugin, String apiKey) {
        this.plugin = plugin;
        this.apiKey = apiKey;
        villagerConversationManager = new VillagerConversationManager(plugin);
        processor = new TradeOfferProcessor(plugin.getLogger());
    }

    /**
     *
     * @param promptText 給Gemin的文字
     * @return Gemini 回傳的結果(提取文字完畢)
     */
    public String sendPrompt(String promptText) throws IOException, InterruptedException {
        // 加入使用者訊息
        conversationManager.addUserMessage(promptText);
        String requestBody = "";//GeminiRequestBuilder.build(conversationManager.getConversation());
        System.out.println(requestBody);
        HttpURLConnection connection = GeminiHttpHelper
                .createPostConnection(API_URL + "?key=" + apiKey);

        GeminiHttpHelper.sendRequest(connection, requestBody);
        // 讀取回應
        String response = GeminiHttpHelper.readResponse(connection);
        System.out.println("raw response: " + response);
        String replyText = GeminiResponseParser.extractModelReply(response);
        if (replyText != null) {
            conversationManager.addModelMessage(replyText);
        }
        return replyText != null ? replyText : response;
    }

    /**
     * 新增可以和村民溝通的
     * @param promptText 給Gemin的文字
     * @return Gemini 回傳的結果(提取文字完畢)
     */
    public String sendPrompt(String promptText,Player player, Villager villager) throws IOException, InterruptedException {
        // 加入使用者訊息

        //conversationManager.addUserMessage(promptText);
        GeminiRequestBuilder builder = new GeminiRequestBuilder();
        VillagerConversation conv = villagerConversationManager.getConversation(player);
        if(conv == null){
            conv  = villagerConversationManager.startConversation(player,villager);
            builder.setSystemMessage(conv.generateSystemPrompt());
        }
        if(conv.hasPlayerLeft())
            return "你離太遠了";
        conv.addMessage(new GeminiMessage("user",promptText));
        String requestBody = builder.build(conv.getMessages());
        System.out.println(requestBody);
        HttpURLConnection connection = GeminiHttpHelper
                .createPostConnection(API_URL + "?key=" + apiKey);

        GeminiHttpHelper.sendRequest(connection, requestBody);
        // 讀取回應
        String response = GeminiHttpHelper.readResponse(connection);
        System.out.println("raw response: " + response);
        String replyText = GeminiResponseParser.extractModelReply(response);
        //處理特殊格式的回覆
        Collection<ConversationMessageAction> actions = processor.processMessage(replyText,conv);
        if(actions != null)
            actions.forEach(ConversationMessageAction::run);
        if (replyText != null) {
            conv.addMessage(new GeminiMessage("model",replyText));
            //conversationManager.addModelMessage(replyText);
        }
        return replyText != null ? replyText : response;
    }

    public void clearConversation() {
        conversationManager.clearHistory();
    }


}