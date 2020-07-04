package su.dreamtime.dtbotsapi.commands.common;

import su.dreamtime.dtbotsapi.DTBotsAPI;
import su.dreamtime.dtbotsapi.bots.common.BotClient;
import su.dreamtime.dtbotsapi.bots.vk.VKBot;
import su.dreamtime.dtbotsapi.bots.vk.VkCommandData;
import su.dreamtime.dtbotsapi.bots.vk.VkCommandExecutor;
import su.dreamtime.dtbotsapi.util.JsonParser;
import su.dreamtime.dtbotsapi.util.Util;

import java.util.Arrays;
import java.util.Map;

public class CommandListener
{
    public static void execute(BotClient client, Command command) {
        switch (command.getName()) {
            case "EXEC_VK_COMMAND": {
                if (client instanceof VKBot) {
                    VKBot bot = (VKBot) client;

                    VkCommandData cmdData = JsonParser.parseJson(command.getData(), VkCommandData.class);
                    cmdData.setLine(Util.removeSpaces(cmdData.getLine()));
                    Map.Entry<String, VkCommandExecutor> entry = bot.getCommand(cmdData.getLine());
                    String cmdName = entry.getKey();
                    if (entry != null) {
                        VkCommandExecutor executor = entry.getValue();

                        String argsLine = cmdData.getLine().substring(cmdName.length());
                        if (argsLine.startsWith(" ")) {
                            argsLine = argsLine.substring(1);
                        }
                        String[] args = argsLine.split(" ");
                        executor.execute(entry.getKey(), bot, args, cmdData);
                    }
                }
                break;
            }
            default: {
                DTBotsAPI.getLogger().info("Command \"" + command.getName() + "\" want to be executed but has no implementation.");
                break;
            }
        }
    }
}
