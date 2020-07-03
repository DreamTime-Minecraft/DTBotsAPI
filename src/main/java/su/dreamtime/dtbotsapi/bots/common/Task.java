package su.dreamtime.dtbotsapi.bots.common;

public interface Task {
    public void start(Runnable runnable);
    public boolean isCancelled();
    public void stop();

    public void runAsync(Runnable runnable);
}
