package com.valorin.caches;

import com.valorin.Main;
import com.valorin.data.Data;
import com.valorin.data.encapsulation.Record;
import com.valorin.util.Debug;
import com.valorin.util.ViaVersion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordCache {
    private final Map<String, List<Record>> recordMap = new HashMap<>();
    private final Map<String, Integer> winsMap = new HashMap<>();
    private final Map<String, Integer> losesMap = new HashMap<>();
    private final Map<String, Integer> drawsMap = new HashMap<>();
    private final Map<String, Integer> winningStreakTimesMap = new HashMap<>();
    private final Map<String, Integer> maxWinningStreakTimesMap = new HashMap<>();

    private int serverTotalGameTimes = -1;

    public RecordCache() {
        try {
            for (Player player : ViaVersion.getOnlinePlayers()) {
                load(player.getName(), null);
            }
            Bukkit.getScheduler()
                    .runTaskAsynchronously(
                            Main.getInstance(),
                            () -> {
                                int serverTotalGameTimes = 0;
                                for (Player player : ViaVersion
                                        .getOnlinePlayers()) {
                                    String name = player.getName();
                                    int wins = Data.getWins(name);
                                    int loses = Data.getLoses(name);
                                    int draws = Data.getDraws(name);
                                    int playerTotalGameTime = wins + loses
                                            + draws;
                                    serverTotalGameTimes = serverTotalGameTimes
                                            + playerTotalGameTime;
                                }

                                for (OfflinePlayer player : Bukkit
                                        .getOfflinePlayers()) {
                                    String name = player.getName();
                                    boolean b = false;
                                    for (Player onlinePlayer : ViaVersion
                                            .getOnlinePlayers()) {
                                        if (onlinePlayer.getName().equals(name)) {
                                            b = true;
                                            break;
                                        }
                                    }
                                    if (b) {
                                        continue;
                                    }
                                    int wins = Data.getWins(name);
                                    int loses = Data.getLoses(name);
                                    int draws = Data.getDraws(name);
                                    int playerTotalGameTime = wins + loses
                                            + draws;
                                    serverTotalGameTimes = serverTotalGameTimes
                                            + playerTotalGameTime;
                                }

                                this.serverTotalGameTimes = serverTotalGameTimes / 2;
                            });
            Debug.send("比赛记录数据缓存已就绪", "The record cache has been initialized");
        } catch (Exception e) {
            e.printStackTrace();
            Debug.send("§c比赛记录数据缓存未能加载",
                    "§cThe record cache failed to initialize");
        }
    }

    public List<Record> getRecords(String name) {
        return recordMap.get(name);
    }

    public int getWins(String name) {
        try {
            return winsMap.get(name);
        } catch (Exception e) {
            return 0;
        }
    }

    public int getLoses(String name) {
        try {
            return losesMap.get(name);
        } catch (Exception e) {
            return 0;
        }
    }

    public int getDraws(String name) {
        try {
            return drawsMap.get(name);
        } catch (Exception e) {
            return 0;
        }
    }

    public int getGameTimes(String name) {
        try {
            return recordMap.get(name).size();
        } catch (Exception e) {
            return 0;
        }
    }

    public int getWinningStreakTimes(String name) {
        try {
            return winningStreakTimesMap.get(name);
        } catch (Exception e) {
            return 0;
        }
    }

    public int getMaxWinningStreakTimes(String name) {
        try {
            return maxWinningStreakTimesMap.get(name);
        } catch (Exception e) {
            return 0;
        }
    }

    public Record getLast(String name) {
        try {
            if (getGameTimes(name) == 0) {
                return null;
            }
            return recordMap.get(name).get(getGameTimes(name) - 1);
        } catch (Exception e) {
            return new Record("Loading...", "Loading...", "Loading...",
                    "Loading...", 0, 0, 0, 0, 0, 0, "Loading...");
        }
    }

    public void addWins(String name) {
        int value = winsMap.get(name) + 1;
        winsMap.put(name, value);
        Data.setWins(name, value);
    }

    public void addLoses(String name) {
        int value = losesMap.get(name) + 1;
        losesMap.put(name, value);
        Data.setLoses(name, value);
    }

    public void addDraws(String name) {
        int value = drawsMap.get(name) + 1;
        drawsMap.put(name, value);
        Data.setDraws(name, value);
    }

    public void addWinningStreakTimes(String name) {
        int value = winningStreakTimesMap.get(name) + 1;
        winningStreakTimesMap.put(name, value);
        Data.setWinningStreakTimes(name, value);
    }

    public void addMaxWinningStreakTimes(String name) {
        int value = maxWinningStreakTimesMap.get(name) + 1;
        maxWinningStreakTimesMap.put(name, value);
        Data.setMaxWinningStreakTimes(name, value);
    }

    public void clearWinningStreakTimes(String name) {
        winningStreakTimesMap.put(name, 0);
        Data.setWinningStreakTimes(name, 0);
    }

    public void load(String name, Action action) {
        Bukkit.getScheduler().runTaskAsynchronously(
                Main.getInstance(),
                () -> {
                    if (!recordMap.containsKey(name)) {
                        List<Record> recordList = Data.getRecordList(name);
                        int wins = Data.getWins(name);
                        int loses = Data.getLoses(name);
                        int draws = Data.getDraws(name);
                        int winningStreakTimes = Data
                                .getWinningStreakTimes(name);
                        int maxWinningStreakTimes = Data
                                .getMaxWinningStreakTimes(name);
                        recordMap.put(name, recordList);
                        winsMap.put(name, wins);
                        losesMap.put(name, loses);
                        drawsMap.put(name, draws);
                        winningStreakTimesMap.put(name, winningStreakTimes);
                        maxWinningStreakTimesMap.put(name,
                                maxWinningStreakTimes);

                        Data.initialRecordData(name);
                    }
                    Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
                        if (action == null) {
                            return;
                        }
                        action.run();
                    });
                });
    }

    public void unload(String name) {
        recordMap.remove(name);
        winsMap.remove(name);
        losesMap.remove(name);
        drawsMap.remove(name);
        winningStreakTimesMap.remove(name);
        maxWinningStreakTimesMap.remove(name);
    }

    public void add(String name, String date, String opponent, String server,
                    int time, double damage, double maxDamage, int result,
                    int startWay, int expChange, String arenaEditName) {
        if (recordMap.containsKey(name)) {
            recordMap.get(name).add(
                    new Record(name, date, opponent, server, time, damage,
                            maxDamage, result, startWay, expChange,
                            arenaEditName));
            Data.addRecord(name, date, opponent, server, time, damage,
                    maxDamage, result, startWay, expChange, arenaEditName);
        }
    }

    public int getServerTotalGameTimes() {
        return serverTotalGameTimes;
    }

    public void refreshServerTotalGameTimes() {
        serverTotalGameTimes++;
    }

    public interface Action {
        void run();
    }
}
