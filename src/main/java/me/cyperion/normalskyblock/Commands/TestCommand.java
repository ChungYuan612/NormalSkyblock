package me.cyperion.normalskyblock.Commands;

import me.cyperion.normalskyblock.GeminiConnection.GeminiResponseParser;
import me.cyperion.normalskyblock.NormalSkyblock;
import me.cyperion.normalskyblock.Utils.MessageLevel;
import me.cyperion.normalskyblock.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements CommandExecutor {

    private NormalSkyblock plugin;

    public TestCommand(NormalSkyblock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!(sender instanceof Player))
            return false;
        if(args.length == 1){
            Player player = (Player) sender;
            //舊版Ngrok
//            Utils.sendMessage(player, MessageLevel.NORMAL, "已向 NPC 發送請求");
//            String response = this.plugin.getNgrokConnection().request(args[0]);
//            Utils.sendMessage(player, MessageLevel.NORMAL, "NPC 回應: " + response);

            //AI Studio連接
            Utils.sendMessage(player, MessageLevel.NORMAL, "已向 AI Studio 發送請求");
            GeminiResponseParser.GeminiResponse response = this.plugin.getGeminiAPI().getResponse(args[0]);
            if(response == null){
                Utils.sendMessage(player, MessageLevel.ERROR, "GeminiAPI getResponse 回應錯誤");
                return false;
            }
            String aiResponse = response.getFirstTextResponse();
            Utils.sendMessage(player, MessageLevel.NORMAL, "&3NPC回應: ");

            Utils.sendMessage(player, MessageLevel.NORMAL, "&f "+aiResponse);
            Utils.sendMessage(player, MessageLevel.NORMAL, "&2回應完畢");
            return true;
        }

        return true;
    }
}