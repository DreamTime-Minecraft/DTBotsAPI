package su.dreamtime.dtbotsapi.bots.vk;

public class VkMessage {
    private int userId;
    private int chatId;
    private String message;

    public VkMessage(int userId, int chatId, String message) {
        this.userId = userId;
        this.chatId = chatId;
        this.message = message;
    }
}
