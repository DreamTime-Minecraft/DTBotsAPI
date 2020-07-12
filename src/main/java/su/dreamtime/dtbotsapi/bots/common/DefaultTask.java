package su.dreamtime.dtbotsapi.bots.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DefaultTask implements Task {
    private ScheduledExecutorService service;
    private ScheduledFuture<?> scheduledFuture;
    private ScheduledExecutorService threadPool;
    public DefaultTask() {

        service = Executors.newSingleThreadScheduledExecutor();
        threadPool = Executors.newScheduledThreadPool(4);
    }
    @Override
    public void start(Runnable runnable) {
        scheduledFuture = service.schedule(runnable, 0, TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isCancelled() {
        return scheduledFuture.isCancelled();
    }

    @Override
    public void stop() {
        if (scheduledFuture != null && !scheduledFuture.isCancelled()) {
            scheduledFuture.cancel(false);
        }
        service.shutdown();
        threadPool.shutdown();
    }

    @Override
    public void runAsync(Runnable runnable) {
        threadPool.schedule(runnable, 0, TimeUnit.MILLISECONDS);
    }
}
