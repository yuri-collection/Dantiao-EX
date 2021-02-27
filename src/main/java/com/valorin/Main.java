package com.valorin;

import com.valorin.arenas.ArenaCreatorHandler;
import com.valorin.arenas.ArenaManager;
import com.valorin.caches.CacheHandler;
import com.valorin.commands.CommandHandler;
import com.valorin.configuration.ConfigManager;
import com.valorin.configuration.DataFile;
import com.valorin.configuration.languagefile.LanguageFileLoader;
import com.valorin.configuration.languagefile.SymbolLoader;
import com.valorin.configuration.update.ConfigUpdate;
import com.valorin.dan.DanHandler;
import com.valorin.data.MySQL;
import com.valorin.event.EventRegister;
import com.valorin.network.Sign;
import com.valorin.papi.RegPAPI;
import com.valorin.queue.MatchingHandler;
import com.valorin.ranking.HD;
import com.valorin.ranking.Ranking;
import com.valorin.request.RequestsHandler;
import com.valorin.stats.Stats;
import com.valorin.stats.data.SingleLineChartData;
import com.valorin.task.GlobalGameTimes;
import com.valorin.timetable.Timetable;
import com.valorin.util.ViaVersion;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;

public class Main extends JavaPlugin {
    /**
     * Dantiao Plugin
     *
     * @author Valorin（创始、初代开发、更新、维护）&Jonjs（仅1.0初代开发）
     * @date 2018/8（Version:1.0，初代发布）2020/2（Version:2.0，二代发布，代码全部重写）
     */
    private static String version;
    private static Main instance;
    private CommandHandler commandsHandler;
    private ArenaManager arenaManager;
    private ArenaCreatorHandler arenaCreatorHandler;
    private Timetable timeTable;
    private RequestsHandler requestHandler;
    private Ranking ranking;
    private HD hd;
    private DanHandler danHandler;
    private SymbolLoader symbolLoader;
    private MatchingHandler matchingHandler;
    private LanguageFileLoader languageFileLoader;
    private SingleLineChartData singleLineChartData;
    private MySQL mysql;
    private CacheHandler cacheHandler;
    private GlobalGameTimes globalGameTimes;
    private ConfigManager configManager;
    private RegPAPI regPAPI;
    private String serverVersion;
    private int serverVersionType; // 0代表1.7 1代表1.8~1.9 2代表1.9+

    public static String getVersion() {
        return version;
    }

    public static Main getInstance() {
        return instance;
    }

    public CommandHandler getCommandHandler() {
        return commandsHandler;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public ArenaCreatorHandler getACS() {
        return arenaCreatorHandler;
    }

    public Timetable getTimeTable() {
        return timeTable;
    }

    public void reloadTimeTable() {
        timeTable.close();
        timeTable = new Timetable();
    }

    public RequestsHandler getRequestsHandler() {
        return requestHandler;
    }

    public Ranking getRanking() {
        return ranking;
    }

    public HD getHD() {
        return hd;
    }

    public DanHandler getDanHandler() {
        return danHandler;
    }

    public SymbolLoader getSymbolLoader() {
        return symbolLoader;
    }

    public MatchingHandler getMatchingHandler() {
        return matchingHandler;
    }

    public LanguageFileLoader getLanguageFileLoader() {
        return languageFileLoader;
    }

    public void reloadLanguageFileLoad() {
        languageFileLoader.close();
        languageFileLoader = new LanguageFileLoader();
    }

    public SingleLineChartData getSingleLineChartData() {
        return singleLineChartData;
    }

    public MySQL getMySQL() {
        return mysql;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CacheHandler getCacheHandler() {
        return cacheHandler;
    }

    public GlobalGameTimes getGlobalGameTimes() {
        return globalGameTimes;
    }

    public String getServerVersion() {
        return serverVersion;
    }

    public int getServerVersionType() {
        return serverVersionType;
    }

    @Override
    public void onEnable() {
        instance = this;
        Sign.theOriginalCheck(nil -> {

            ConsoleCommandSender console = Bukkit.getConsoleSender();
            version = getDescription().getVersion();
            console.sendMessage("§b██████╗ ████████╗");
            console.sendMessage("§b██╔══██╗╚══██╔══╝");
            console.sendMessage("§b██║  ██║   ██║   ");
            console.sendMessage("§b██║  ██║   ██║   ");
            console.sendMessage("§b██████╔╝   ██║   ");
            console.sendMessage("§b╚═════╝    ╚═╝§eEnabling...");
            console.sendMessage("§fThis is Dantiao-EX plugin V" + version
                    + " developed by Valorin");

            DataFile.loadData();
            DataFile.saveAreas();
            DataFile.saveBlackList();
            DataFile.savepd();
            DataFile.saveRanking();
            DataFile.saveRecords();
            DataFile.saveShop();
            DataFile.saveSymbols();

            new SymbolLoader();
            ConfigUpdate.execute();
            configManager = new ConfigManager();
            mysql = new MySQL();
            mysql.connect();
            cacheHandler = new CacheHandler(
                    () -> {
                        languageFileLoader = new LanguageFileLoader();
                        symbolLoader = new SymbolLoader();
                        arenaCreatorHandler = new ArenaCreatorHandler();
                        arenaManager = new ArenaManager();
                        timeTable = new Timetable();
                        requestHandler = new RequestsHandler();
                        ranking = new Ranking();
                        hd = new HD();
                        danHandler = new DanHandler();
                        matchingHandler = new MatchingHandler();
                        commandsHandler = new CommandHandler("dt");
                        singleLineChartData = new SingleLineChartData();
                        EventRegister.registerEvents();
                        if (Bukkit.getPluginManager().isPluginEnabled(
                                "PlaceholderAPI")) {
                            if (!me.clip.placeholderapi.PlaceholderAPI
                                    .isRegistered("dantiao")) {
                                regPAPI = new RegPAPI();
                                regPAPI.register();
                            }
                        } else {
                            console.sendMessage("§8§l[§bDantiao§8§l]");
                            console.sendMessage("§f- §c未发现PlaceholderAPI变量插件，将无法使用PAPI的相关功能，若您刚安装，请尝试重启服务器");
                            console.sendMessage("§f- §cPlugin PlaceholderAPI is not found!");
                        }
                    });

            globalGameTimes = new GlobalGameTimes();
            globalGameTimes.runTaskTimerAsynchronously(instance, 200L, 36000L);
            new Stats();
            serverVersion = Bukkit.getServer().getClass().getPackage()
                    .getName().split("\\.")[3];
            try {
                int serverVersionInt = Integer.parseInt(serverVersion
                        .split("_")[1]);
                if (serverVersionInt <= 7) {
                    serverVersionType = 0;
                }
                if (serverVersionInt >= 8 && serverVersionInt <= 9) {
                    serverVersionType = 1;
                }
                if (serverVersionInt >= 10) {
                    serverVersionType = 2;
                }
            } catch (NumberFormatException e) {
                serverVersionType = 0;
                e.printStackTrace();
            }
            ViaVersion.getAllClass();
        });
    }

    @Override
    public void onDisable() {
        hd.unload(0);
        languageFileLoader.close();
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            if (me.clip.placeholderapi.PlaceholderAPI
                    .isRegistered("dantiao")) {
                unRegisterPAPI();
            }
        }
        cacheHandler.unload();
        Bukkit.getScheduler().cancelTasks(instance);
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        console.sendMessage("§8§l[§bDantiao§8§l]");
        console.sendMessage("§f- §7单挑插件EX已关闭，感谢你的使用");
        console.sendMessage("§f- §7Dantiao-EX by:Valorin");
    }

    public void unRegisterPAPI() {
        try {
            regPAPI.unregister();
        } catch (Exception e) {
            try {
                Method method = Class.forName("me.clip.placeholderapi.PlaceholderAPI").getMethod("unregisterExpansion");
                method.invoke(regPAPI);
            } catch (Exception e1) {
            }
        }
    }
}
