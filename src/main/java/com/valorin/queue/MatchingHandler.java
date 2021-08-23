package com.valorin.queue;

import com.valorin.Main;
import com.valorin.arenas.StartGame;
import com.valorin.data.encapsulation.ArenaInfo;
import com.valorin.inventory.special.INVStart;
import com.valorin.itemstack.ArenaSelect;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class MatchingHandler {
    private final List<Matching> matchings = new ArrayList<>();
    private final Matching randomMatching;
    private final List<String> busyPlayers = new ArrayList<>();

    public MatchingHandler() {
        List<ArenaInfo> arenaInfoList = Main.getInstance().getCacheHandler()
                .getArenaInfo().getArenaInfoList();
        randomMatching = new Matching(null, null);
        for (ArenaInfo arenaInfo : arenaInfoList) {
            addMatching(arenaInfo);
        }
    }

    public Matching getArenaMatching(String arenaEditName) { // 通过竞技场编辑名获取matching
        if (arenaEditName == null) {
            return randomMatching;
        }
        for (Matching matching : matchings) {
            if (matching.getArenaEditName().equals(arenaEditName)) {
                return matching;
            }
        }
        return null;
    }

    public Matching getMatchingOfPlayer(String playerName) { // 通过玩家名获取matching
        if (randomMatching.isEnable()) {
            if (randomMatching.getWaiter().equals(playerName)) {
                return randomMatching;
            }
        }
        for (Matching matching : matchings) {
            if (matching.isEnable()) {
                if (matching.getWaiter().equals(playerName)) {
                    return matching;
                }
            }
        }
        return null;
    }

    public void addMatching(ArenaInfo arenaInfo) {
        String editName = arenaInfo.getEditName();
        String displayName = arenaInfo.getDisplayName();
        if (displayName != null) {
            displayName = displayName.replace("&0", "").replace("&1", "")
                    .replace("&2", "").replace("&3", "").replace("&4", "")
                    .replace("&5", "").replace("&6", "").replace("&7", "")
                    .replace("&8", "").replace("&9", "").replace("&a", "")
                    .replace("&b", "").replace("&c", "").replace("&d", "")
                    .replace("&e", "").replace("&f", "").replace("&o", "")
                    .replace("&m", "").replace("&n", "").replace("&k", "");
        } else {
            displayName = editName;
        }
        matchings.add(new Matching(arenaInfo.getEditName(), displayName));
    }

    public void removeMatching(ArenaInfo arenaInfo) {
        Matching matchingAboutToBeRemove = null;
        for (Matching matching : matchings) {
            if (matching.getArenaEditName().equals(arenaInfo.getEditName())) {
                matchingAboutToBeRemove = matching;
                break;
            }
        }
        if (matchingAboutToBeRemove != null) {
            matchings.remove(matchingAboutToBeRemove);
        }
    }

    public void start(String playerName) {
        ArenaSelect as = INVStart.arenaSelects.get(playerName);
        String arenaEditName = as.getSelectedArenaEditName();
        if (arenaEditName == null) {
            if (randomMatching.isEnable()) { // 随机时，与也在随机的玩家开局
                String waiter = randomMatching.getWaiter();
                if (waiter != null) {
                    randomMatching.disable(false);
                    StartGame.start(Bukkit.getPlayerExact(playerName),
                            Bukkit.getPlayerExact(waiter), null, null, 1);
                }
                return;
            }
            boolean success = false; // 是否找到可以马上开局的匹配
            for (Matching matching : matchings) { // 随机时，寻找正在等待的竞技场
                if (matching.isEnable()) {
                    arenaEditName = matching.getArenaEditName();
                    success = true;
                }
            }
            if (!success) {
                randomMatching.enable(playerName);
                as.cancel();
                busyPlayers.add(playerName);
                return;
            }
        }
        Matching matching = getArenaMatching(arenaEditName);
        if (matching != null) {
            if (matching.isEnable()) {
                String waiter = matching.getWaiter();
                if (waiter != null) {
                    matching.disable(false);
                    StartGame.start(Bukkit.getPlayerExact(playerName),
                            Bukkit.getPlayerExact(waiter), arenaEditName, null,
                            1);
                }
            } else {
                if (randomMatching.isEnable()) {
                    String waiter = randomMatching.getWaiter();
                    if (waiter != null) {
                        randomMatching.disable(false);
                        StartGame.start(Bukkit.getPlayerExact(playerName),
                                Bukkit.getPlayerExact(waiter), arenaEditName,
                                null, 1);
                    }
                    return;
                }
                matching.enable(playerName);
                as.cancel();
                busyPlayers.add(playerName);
            }
        }
    }

    public List<Matching> getMatchingList() {
        return matchings;
    }

    public List<String> getBusyPlayers() {
        return busyPlayers;
    }

    public void removeBusyPlayer(String playerName) {
        if (busyPlayers.contains(playerName)) {
            busyPlayers.remove(playerName);
        }
    }

    public boolean isPlayerBusy(String playerName) {
        return busyPlayers.contains(playerName);
    }
}
