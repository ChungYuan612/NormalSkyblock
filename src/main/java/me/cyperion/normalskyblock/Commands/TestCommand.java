package me.cyperion.normalskyblock.Commands;

import me.cyperion.normalskyblock.GeminiConnection.GeminiResponseParser;
import me.cyperion.normalskyblock.NormalSkyblock;
import me.cyperion.normalskyblock.Utils.MessageLevel;
import me.cyperion.normalskyblock.Utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TestCommand implements CommandExecutor {

    private NormalSkyblock plugin;

    public TestCommand(NormalSkyblock plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(!(sender instanceof Player))
            return false;
        Player player = (Player) sender;
        if(args.length == 1){
            //舊版Ngrok
//            Utils.sendMessage(player, MessageLevel.NORMAL, "已向 NPC 發送請求");
//            String response = this.plugin.getNgrokConnection().request(args[0]);
//            Utils.sendMessage(player, MessageLevel.NORMAL, "NPC 回應: " + response);

            //AI Studio連接

            List<Entity> entityList = player.getNearbyEntities(10, 10, 10);
            Villager villager = null;
            for(Entity entity : entityList){
                if(entity instanceof Villager){
                    villager = (Villager) entity;
                    break;
                }
            }
            if(villager == null){
                Utils.sendMessage(player, MessageLevel.ERROR, "未找到 NPC");
                return false;
            }
            villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,10000,5,false,false));
            //不要動

            Utils.sendMessage(player, MessageLevel.NORMAL
                    , "&b["+player.getName()+"]: &f"+args[0]);
            Utils.sendMessage(player, MessageLevel.NORMAL, "已向 AI Studio 發送請求");
            String response = this.plugin.getGeminiAPI().getResponse(args[0],player,villager);

            if(response == null){
                Utils.sendMessage(player, MessageLevel.ERROR, "GeminiAPI getResponse 回應錯誤");
                return false;
            }
            Utils.sendMessage(player, MessageLevel.NORMAL, "&3NPC回應: ");

            Utils.sendMessage(player, MessageLevel.NORMAL, "&f "+response);
            Utils.sendMessage(player, MessageLevel.NORMAL, "&2回應完畢");
            return true;
        }
        if(plugin.getGeminiListener().isPlayerActive(player)){
            plugin.getGeminiListener().removePlayer(player);
        }else{
            plugin.getGeminiListener().addPlayer(player);
        }

        return true;
    }
}