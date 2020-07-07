package su.dreamtime.dtbotsapi.bots.common;

import org.bukkit.Bukkit;
import su.dreamtime.dtbotsapi.plugin.MainPaper;

public class BukkitTask implements Task {

    private org.bukkit.scheduler.BukkitTask bukkitTask;

    @Override
    public void start(Runnable runnable) {
        bukkitTask = Bukkit.getScheduler().runTaskAsynchronously(MainPaper.getInstance(), runnable);
    }


    @Override
    public boolean isCancelled() {
        return bukkitTask.isCancelled();
    }

    @Override
    public void stop() {
        if (bukkitTask != null && !isCancelled()) {
            bukkitTask.cancel();
        }
    }

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(MainPaper.getInstance(), runnable);
    }
}
