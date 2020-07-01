package su.dreamtime.dtbotsapi.bots.vk;

public interface VkCommandExecutor {
    public void execute(String command, VKBot sender, String[] args, VkCommandData other);
}
