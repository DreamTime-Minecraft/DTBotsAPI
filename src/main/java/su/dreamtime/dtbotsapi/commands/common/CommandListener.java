package su.dreamtime.dtbotsapi.commands.common;

import su.dreamtime.dtbotsapi.DTBotsAPI;
import su.dreamtime.dtbotsapi.bots.common.BotClient;
import su.dreamtime.dtbotsapi.bots.vk.VKBot;
import su.dreamtime.dtbotsapi.bots.vk.VkCommandData;
import su.dreamtime.dtbotsapi.bots.vk.VkCommandExecutor;
import su.dreamtime.dtbotsapi.util.JsonParser;
import su.dreamtime.dtbotsapi.util.Util;

import java.util.Arrays;

public class CommandListener
{
    public static void execute(BotClient client, Command command) {
        switch (command.getName()) {
            case "EXEC_VK_COMMAND": {
                if (client instanceof VKBot) {
                    VKBot bot = (VKBot) client;

                    VkCommandData cmdData = JsonParser.parseJson(command.getData(), VkCommandData.class);
                    cmdData.setLine(Util.removeSpaces(cmdData.getLine()));
                    String[] splittedLine = cmdData.getLine().split(" ");
                    String cmdName = splittedLine[0];
                    VkCommandExecutor executor = bot.getCommand(cmdName);
                    if (executor != null) {
                        String[] args = new String[0];
                        if (splittedLine.length > 1) {
                            args = Arrays.copyOfRange(splittedLine, 0, splittedLine.length);

                        }
                        executor.execute(cmdName, bot, args, cmdData);
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
