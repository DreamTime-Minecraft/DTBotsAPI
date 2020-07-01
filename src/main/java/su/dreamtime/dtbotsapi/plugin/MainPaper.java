package su.dreamtime.dtbotsapi.plugin;

import org.bukkit.plugin.java.JavaPlugin;
import su.dreamtime.dtbotsapi.DTBotsAPI;

public class MainPaper extends JavaPlugin
{
    @Override
    public void onEnable() {
        DTBotsAPI.init(this);
    }

    @Override
    public void onDisable() {
        DTBotsAPI.destroy();
    }
}
