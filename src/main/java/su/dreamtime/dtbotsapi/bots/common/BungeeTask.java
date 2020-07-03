package su.dreamtime.dtbotsapi.bots.common;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import su.dreamtime.dtbotsapi.plugin.MainBungee;
import su.dreamtime.dtbotsapi.plugin.MainPaper;

public class BungeeTask implements Task {
    private ScheduledTask task;

    @Override
    public void start(Runnable runnable) {
        task = ProxyServer.getInstance().getScheduler().runAsync(MainBungee.getInstance(), runnable);
    }

    @Override
    public boolean isCancelled() {
        return true;
    }

    @Override
    public void stop() {
        if (task != null || isCancelled()) {
            task.cancel();
        }
    }

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(MainPaper.getInstance(), runnable);
    }
}
