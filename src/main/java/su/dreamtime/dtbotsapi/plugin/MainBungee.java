package su.dreamtime.dtbotsapi.plugin;

import net.md_5.bungee.api.plugin.Plugin;
import su.dreamtime.dtbotsapi.DTBotsAPI;

public class MainBungee extends Plugin
{
    private static MainBungee instance;
    @Override
    public void onEnable() {
        instance = this;
        DTBotsAPI.init(this);
    }

    @Override
    public void onDisable() {
        DTBotsAPI.destroy();
    }

    public static MainBungee getInstance() {
        return instance;
    }
}
