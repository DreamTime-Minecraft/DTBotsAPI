package su.dreamtime.dtbotsapi.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import su.dreamtime.dtbotsapi.DTBotsAPI;

public class MainPaper extends JavaPlugin
{
    private static MainPaper instance;
    @Override
    public void onEnable() {
        instance = this;
        DTBotsAPI.init(this);
    }

    @Override
    public void onDisable() {
        DTBotsAPI.destroy();
    }

    public static MainPaper getInstance() {
        return instance;
    }
}
