package com.valorin.caches;

import com.valorin.Main;
import com.valorin.data.Data;
import com.valorin.data.encapsulation.ArenaInfo;
import com.valorin.util.Debug;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArenaInfoCache {
    private List<ArenaInfo> arenaInfoList = new ArrayList<>();

    public ArenaInfoCache() {
        try {
            List<String> arenaNameList = Data.getArenas();
            for (String editName : arenaNameList) {
                String displayName = Data.getArenaDisplayName(editName);
                Location pointA = Data.getArenaPointA(editName);
                Location pointB = Data.getArenaPointB(editName);
                List<String> commandList = Data.getArenaCommands(editName);
                Location watchingPoint = Data.getArenaWatchingPoint(editName);
                boolean isKitMode = Data.isArenaKitEnable(editName);
                List<ItemStack> kit = null;
                if (isKitMode) {
                    kit = Data.getArenaKit(editName);
                }
                ArenaInfo arenaInfo = new ArenaInfo(editName, displayName,
                        pointA, pointB, commandList, watchingPoint, kit);
                arenaInfoList.add(arenaInfo);
            }
            Debug.send("竞技场信息缓存已就绪",
                    "The Arena info cache has been initialized");
        } catch (Exception e) {
            Debug.send("§c竞技场信息缓存加载失败",
                    "§cThe Arena info cache failed to initialize");
            e.printStackTrace();
        }
    }

    public void saveArena(String editName, String displayName, Location pointA,
                          Location pointB) {
        ArenaInfo arenaInfo = new ArenaInfo(editName, displayName, pointA,
                pointB, null, null, null);
        arenaInfoList.add(arenaInfo);
        Main.getInstance().getMatchingHandler().addMatching(arenaInfo);
        Data.saveArena(editName, displayName, pointA, pointB);
    }

    public void deleteArena(String editName) {
        ArenaInfo arenaInfo = get(editName);
        if (arenaInfo != null) {
            arenaInfoList.remove(arenaInfo);
            Main.getInstance().getMatchingHandler().removeMatching(arenaInfo);
            Data.deleteArena(arenaInfo.getEditName());
        }
    }

    public ArenaInfo get(String editName) {
        for (ArenaInfo arenaInfo : arenaInfoList) {
            if (arenaInfo.getEditName().equals(editName)) {
                return arenaInfo;
            }
        }
        return null;
    }

    public int size() {
        return arenaInfoList.size();
    }

    public void setCommandList(String editName, List<String> commandList) {
        int index = -1;
        for (ArenaInfo arenaInfo : arenaInfoList) {
            index++;
            if (arenaInfo.getEditName().equals(editName)) {
                break;
            }
        }
        if (index != -1) {
            List<String> list = commandList;
            if (list == null) {
                list = new ArrayList<>();
            }
            arenaInfoList.get(index).setCommandList(list);
            Data.setArenaCommands(editName, list);
        }
    }

    public void setWatchingPoint(String editName, Location watchingPoint) {
        int index = -1;
        for (ArenaInfo arenaInfo : arenaInfoList) {
            index++;
            if (arenaInfo.getEditName().equals(editName)) {
                break;
            }
        }
        if (index != -1) {
            arenaInfoList.get(index).setWatchingPoint(watchingPoint);
            Data.setArenaWatchingPoint(editName, watchingPoint);
        }
    }

    public void setKit(String editName, List<ItemStack> kit) {
        int index = -1;
        for (ArenaInfo arenaInfo : arenaInfoList) {
            index++;
            if (arenaInfo.getEditName().equals(editName)) {
                break;
            }
        }
        if (index != -1) {
            arenaInfoList.get(index).setKit(kit);
            Data.setArenaKit(editName, kit);
        }
    }

    public void setKitEnable(String editName, boolean enable) {
        int index = -1;
        for (ArenaInfo arenaInfo : arenaInfoList) {
            index++;
            if (arenaInfo.getEditName().equals(editName)) {
                break;
            }
        }
        if (index != -1) {
            arenaInfoList.get(index).setKitMode(enable);
            Data.setArenaKitEnable(editName, enable);
        }
    }

    public List<ArenaInfo> getArenaInfoList() {
        return arenaInfoList;
    }
}
