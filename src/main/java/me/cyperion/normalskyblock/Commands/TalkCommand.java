package me.cyperion.normalskyblock.Commands;

import me.cyperion.normalskyblock.Event.ConversationSession;
import me.cyperion.normalskyblock.NormalSkyblock;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TalkCommand implements CommandExecutor, Listener {

    private NormalSkyblock plugin;

    private final Map<UUID, ConversationSession> conversationSessions = new HashMap<>();
    private final Set<UUID> activePlayers = new HashSet<>();

    public TalkCommand(NormalSkyblock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command cmd,
                             @NotNull String s,
                             @NotNull String[] strings) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(strings.length != 0) return false;
            if(!activePlayers.contains(player.getUniqueId())) {
                activePlayers.add(player.getUniqueId());
                player.sendMessage(ChatColor.GREEN + "右鍵村民開始對話，再次輸入指令結束對話模式");
            }else{
                activePlayers.remove(player.getUniqueId());
                player.sendMessage(ChatColor.RED + "已結束語村民們的對話");
            }

            return true;
        }


        return false;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof Villager villager)) return;
        if(!activePlayers.contains(event.getPlayer().getUniqueId())) return;
        Player player = event.getPlayer();
        if(conversationSessions.containsKey(player.getUniqueId())) {//有在對話
            if(!this.getSession(player).getVillager().getUniqueId()
                    .equals(villager.getUniqueId())) { //不是同一個村民
                this.endConversation(player,ChatColor.YELLOW+"已換和下一個村民對話(上一個村民的對話會被清除喔)");
                plugin.getGeminiAPI().geminiClient.endConversation(player);
                this.startConversation(player, villager);
                event.setCancelled(true);
                return;
            }

        }else{
            // 開始對話
            this.startConversation(player, villager);
            player.sendMessage(ChatColor.GREEN + "你開始與村民對話，請在聊天輸入訊息");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        ConversationSession session = this.getSession(player);
        if (session == null) return;

        event.setCancelled(true); // 不要顯示原始訊息

        Villager villager = session.getVillager();
        player.sendMessage(ChatColor.translateAlternateColorCodes(
                '&',
                "&3你 &f→ &b"+villager.getName() +"&f:"
                )
        );
        player.sendMessage(ChatColor.WHITE+event.getMessage());
        if (villager.getLocation().distanceSquared(player.getLocation()) > 12 * 12
                || villager.isDead()) {
            this.endConversation(player, "你離村民太遠，對話已中止");
            return;
        }

        String message = event.getMessage();
        String response = plugin.getGeminiAPI().getResponse(message, player, villager);

        if (response == null) {
            player.sendMessage(ChatColor.RED + "AI 回應錯誤");
        }
    }




    public void startConversation(Player player, Villager villager) {
        conversationSessions.put(player.getUniqueId(), new ConversationSession(player, villager));
    }

    public void endConversation(Player player, String reason) {
        conversationSessions.remove(player.getUniqueId());
        player.sendMessage(ChatColor.RED + reason);
    }

    public ConversationSession getSession(Player player) {
        return conversationSessions.get(player.getUniqueId());
    }

}