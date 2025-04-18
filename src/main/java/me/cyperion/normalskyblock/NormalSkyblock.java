package me.cyperion.normalskyblock;

import me.cyperion.normalskyblock.Commands.TestCommand;
import me.cyperion.normalskyblock.GeminiConnection.GeminiAPI;
import me.cyperion.normalskyblock.GeminiConnection.GeminiClient;
import me.cyperion.normalskyblock.NPC.NgrokConnection;
import org.bukkit.plugin.java.JavaPlugin;

public final class NormalSkyblock extends JavaPlugin {

    private NgrokConnection ngrokConnection;
    private GeminiAPI geminiAPI;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        //ngrokConnection = new NgrokConnection(this);
        geminiAPI = new GeminiAPI(this);
        getCommand("test").setExecutor(new TestCommand(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public NgrokConnection getNgrokConnection() {
        return ngrokConnection;
    }

    public GeminiAPI getGeminiAPI() {
        return geminiAPI;
    }
}
