package com.valorin.event;

import com.valorin.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.sm;

public class EventSaveArenaKitItems implements Listener {
    @EventHandler
    public void saveArenaKitItems(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        Inventory inv = e.getInventory();
        if (inv == null) {
            return;
        }
        try {
            String editName = e.getView().getTitle().split("\\:")[0];
            String title = e.getView().getTitle().split("\\:")[1];
            if (title.equals(gm("请将装备放进来", p))) {
                List<ItemStack> itemStacks = new ArrayList<>();
                for (ItemStack itemStack : inv.getContents()) {
                    if (itemStack != null) {
                        if (!itemStack.getType().equals(Material.AIR)) {
                            itemStacks.add(itemStack);
                        }
                    }
                }
                Main.getInstance().getCacheHandler().getArenaInfo().setKit(editName, itemStacks);
                sm("&a[v]Kit装备保存完毕", p);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }
}
