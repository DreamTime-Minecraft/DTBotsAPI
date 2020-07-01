package su.dreamtime.dtbotsapi.bots.vk;

public class VkCommandData {
    private String firstName;
    private String lastName;
    private String line;
    private Integer chatId;
    private Integer userId;

    public String getLine() {
        return line;
    }

    public Integer getChatId() {
        return chatId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLine(String line) {
        this.line = line;
    }

}
