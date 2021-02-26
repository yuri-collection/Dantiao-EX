package com.valorin.data.encapsulation;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArenaInfo {
    private String editName;
    private String displayName;
    private Location pointA;
    private Location pointB;
    private List<String> commandList;
    private Location watchingPoint;
    private boolean isKitMode;
    private List<ItemStack> kit;

    public ArenaInfo(String editName, String displayName, Location pointA,
                     Location pointB, List<String> commandList, Location watchingPoint, List<ItemStack> kit) {
        this.editName = editName;
        this.displayName = displayName;
        this.pointA = pointA;
        this.pointB = pointB;
        if (commandList == null) {
            this.commandList = new ArrayList<String>();
        } else {
            this.commandList = commandList;
        }
        this.watchingPoint = watchingPoint;
        if (kit == null) {
            isKitMode = false;
        } else {
            isKitMode = true;
            this.kit = kit;
        }
    }

    public String getEditName() {
        return editName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Location getPointA() {
        return pointA;
    }

    public Location getPointB() {
        return pointB;
    }

    public List<String> getCommandList() {
        return commandList;
    }

    public void setCommandList(List<String> commandList) {
        if (commandList == null) {
            this.commandList = new ArrayList<String>();
        } else {
            this.commandList = commandList;
        }
    }

    public Location getWatchingPoint() {
        return watchingPoint;
    }

    public void setWatchingPoint(Location watchingPoint) {
        this.watchingPoint = watchingPoint;
    }

    public boolean isKitMode() {
        return isKitMode;
    }

    public void setKitMode(boolean isKitMode) {
        this.isKitMode = isKitMode;
    }

    public List<ItemStack> getKit() {
        return kit;
    }

    public void setKit(List<ItemStack> kit) {
        if (kit == null) {
            this.kit = new ArrayList<ItemStack>();
        } else {
            this.kit = kit;
        }
    }
}
