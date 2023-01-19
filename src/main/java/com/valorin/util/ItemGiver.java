package com.valorin.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class ItemGiver {
    private final boolean hasReceived;
    private int freeSlot;

    public ItemGiver(Player p, ItemStack item) {
        if (isFree(p)) {
            p.getInventory().setItem(freeSlot, item);
            hasReceived = true;
            sm("&a[v]物品已发送", p);
        } else {
            hasReceived = false;
            sm("&c[x]背包满了，无法获取物品，请先为你的背包留出空位！", p);
        }
    }

    public boolean getHasReceived() {
        return hasReceived;
    }

    public boolean isFree(Player p) {
        Inventory inv = p.getInventory();
        for (int i = 0; i < 36; i++) {
            if (inv.getItem(i) == null || inv.getItem(i).getType().equals(Material.AIR)) {
                freeSlot = i;
                return true;
            }
        }
        return false;
    }
}
