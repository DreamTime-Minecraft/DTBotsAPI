package su.dreamtime.dtbotsapi;

import net.md_5.bungee.api.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import su.dreamtime.dtbotsapi.bots.common.BotClient;
import su.dreamtime.dtbotsapi.plugin.MainBungee;
import su.dreamtime.dtbotsapi.plugin.MainPaper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public final class DTBotsAPI {
    public static final String DEF_IP = "localhost";
    public static final int DEF_PORT = 44896;
    private static Logger logger;
    private static String defaultIp = DEF_IP;
    private static int defaultPort = DEF_PORT;
    private static List<BotClient> clients = Collections.synchronizedList(new ArrayList<>());

    /**
     * НЕ ИСПОЛЬЗОВАТЬ ЭТОТ МЕТОД! Он только для API!
     */
    public static void init(Object parentPlugin) {
        if (parentPlugin == null) {
            logger = Logger.getGlobal();
        }
        try {
            if (parentPlugin instanceof MainBungee) {
                MainBungee bungee = (MainBungee) parentPlugin;
                logger = bungee.getLogger();
            } else {
                logger = Logger.getGlobal();
            }
        } catch (NoClassDefFoundError | Exception e) {
            try {
                if (parentPlugin instanceof MainPaper) {
                    MainPaper paper = (MainPaper) parentPlugin;
                    logger = paper.getLogger();
                } else {
                    logger = Logger.getGlobal();
                }
            } catch (NoClassDefFoundError | Exception ex) {
                logger = Logger.getGlobal();
            }
        }

    }

    /**
     * НЕ ИСПОЛЬЗОВАТЬ ЭТОТ МЕТОД! Он только для API!
     */
    public static void destroy() {
        clients.forEach((client)->client.close(false));
        clients.clear();
    }

    /**
     * Устанавливает стандартный адрес, к которому будет подключаться клиент
     * Если аргумент ip равен null, то будет установлено значение {@link DTBotsAPI#DEF_IP DTBotsAPI.DEF_IP};
     */
    static void setDefaultIp(String ip) {
        defaultIp = ip;
        if (defaultIp == null) {
            defaultIp = DEF_IP;
        }
    }

    /**
     * Устанавливает стандартный порт, по которому будет идти подключение <br>
     * Если port равен 0, то будет установлено значение {@link DTBotsAPI#DEF_IP DTBotsAPI.DEF_PORT};
     */
    public static void setDefaultPort(int port) {
        defaultPort = port;
        if (defaultPort == 0) {
            defaultPort = DEF_PORT;
        }
    }


    public static Logger getLogger() {
        return logger;
    }


    /**
     * НЕ ИСПОЛЬЗОВАТЬ ЭТОТ МЕТОД! Он только для API!
     */
    public static void addClient(BotClient client) {
        clients.add(client);
    }

    /**
     * НЕ ИСПОЛЬЗОВАТЬ ЭТОТ МЕТОД! Он только для API!
     */
    public static void removeClient(BotClient client) {
        clients.remove(client);
    }
}
