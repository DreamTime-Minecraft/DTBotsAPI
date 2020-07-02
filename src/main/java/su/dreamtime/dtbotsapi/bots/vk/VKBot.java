package su.dreamtime.dtbotsapi.bots.vk;

import su.dreamtime.dtbotsapi.DTBotsAPI;
import su.dreamtime.dtbotsapi.bots.common.BotClient;
import su.dreamtime.dtbotsapi.commands.common.Command;
import su.dreamtime.dtbotsapi.util.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class VKBot extends BotClient {
    private final Integer groupId;
    private final String token;
    private String hash;
    private Map<String, VkCommandExecutor> commands;

    public VKBot(String name, String remoteIp, int remotePort, int groupId, String token) {
        super(name, remoteIp, remotePort);
        commands = new HashMap<>();
        this.groupId = groupId;
        this.token = token;
    }

    @Override
    protected void onConnect() {
        super.onConnect();

        VKCommandData data = new VKCommandData(groupId, token);
        String jsonData = JsonParser.toJson(data);
        Command cmd = new Command("CONNECT_TO_VK", jsonData);

        Map<String, Object> response = sendRequest(cmd);

        hash = (String) response.get("hash");
        if (hash == null) {
            DTBotsAPI.getLogger().info("VKBot " + this.toString() + " - hash is null");
        }
    }

    protected void createCommand(String cmd, VkCommandExecutor executor) {
        commands.put(cmd, executor);
        Map<String, String> data = new HashMap<>();
        data.put("hash", hash);
        data.put("cmd", cmd);
        String jsonData = JsonParser.toJson(data);
        send(new Command("CREATE_VK_COMMAND", jsonData));
    }

    public VkCommandExecutor getCommand(String name) {
        return commands.get(name);
    }

    @Override
    protected void onClose() {

    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return name + " " + localIp + ":" + localPort + " " + this.hash;

    }

    public void sendVkMessage(int userId, int chatId, String message) {
        VkMessage msg = new VkMessage(userId, chatId, message);
        sendVkMessage(msg);
    }

    private void sendVkMessage(VkMessage msg) {
        String jsonMsg = JsonParser.toJson(msg);
        Command cmd = new Command("SEND_VK_MESSAGE", jsonMsg);
        this.send(cmd);
    }

    public void sendVkMessage(VkCommandData data, String message) {
        sendVkMessage(data.getUserId(), data.getChatId(), message);
    }

    private class VKCommandData{
        private String groupId;
        private String token;

        private VKCommandData(Integer groupId, String token) {
            this.groupId = groupId.toString();
            this.token = token;
        }
    }
}
