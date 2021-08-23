package com.valorin.arenas;

import com.valorin.data.Data;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager {

    public static List<Arena> arenas = new ArrayList<>();
    public static List<String> busyArenasName = new ArrayList<>();

    public ArenaManager() {
        if (Data.getArenas().size() == 0) {
            return;
        }
        for (String arena : Data.getArenas()) {
            arenas.add(new Arena(arena));
        }
    }

    public Arena getArena(String name) {
        if (name == null) {
            return null;
        }
        if (arenas.size() == 0) {
            return null;
        }
        for (Arena arena : arenas) {
            if (arena.getName().equals(name)) {
                return arena;
            }
        }
        return null;
    }

    public void addArena(String name) {
        arenas.add(new Arena(name));
    }

    public void removeArena(String name) {
        arenas.remove(getArena(name));
    }

    public String getPlayerOfArena(String pn) {
        if (arenas.size() == 0) {
            return null;
        }
        for (Arena arena : arenas) {
            if (arena.getEnable()) {
                if (arena.getp1().equals(pn) || arena.getp2().equals(pn)) {
                    return arena.getName();
                }
            }
        }
        return null;
    }

    public String getWatcherOfArena(String pn) {
        if (arenas.size() == 0) {
            return null;
        }
        for (Arena arena : arenas) {
            if (arena.getEnable()) {
                if (arena.getWatchers().contains(pn)) {
                    return arena.getName();
                }
            }
        }
        return null;
    }

    public String getTheOtherPlayer(String pn) {
        String arenaName = getPlayerOfArena(pn);
        if (arenaName != null) {
            Arena arena = getArena(arenaName);
            if (arena.getp1().equals(pn)) {
                return arena.getp2();
            } else {
                return arena.getp1();
            }
        }
        return null;
    }

    public boolean isContainPlayer(String arenaName, String pn) {// 某个指定竞技场是否包括某玩家
        if (getArena(arenaName) == null) {
            return false;
        }
        if (getArena(arenaName).getEnable()) {
            return getArena(arenaName).getp1().equals(pn)
                    || getArena(arenaName).getp2().equals(pn);
        }
        return false;
    }

    public boolean isPlayerBusy(String pn) {// 某玩家是否在比赛
        if (arenas.size() == 0) {
            return false;
        }
        for (Arena arena : arenas) {
            if (isContainPlayer(arena.getName(), pn)) {
                return true;
            }
        }
        return false;
    }

    public int size() { //获取竞技场数量
        return arenas.size();
    }
}
