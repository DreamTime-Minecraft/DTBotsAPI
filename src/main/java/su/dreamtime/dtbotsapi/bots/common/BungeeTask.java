package su.dreamtime.dtbotsapi.bots.common;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import su.dreamtime.dtbotsapi.plugin.MainBungee;

public class BungeeTask implements Task {
    private ScheduledTask task;
    private boolean cancelled;

    public BungeeTask() {
        cancelled = false;
    }

    @Override
    public void start(Runnable runnable) {
        task = MainBungee.getInstance().getProxy().getScheduler().runAsync(MainBungee.getInstance(), runnable);
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void stop() {
        if (task != null  && !isCancelled()) {
            task.cancel();
            cancelled = true;
        }
    }

    @Override
    public void runAsync(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(MainBungee.getInstance(), runnable);
    }

}
