package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.ArenaManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventOpenChest implements Listener {
    @EventHandler
    public void onOpenChest(PlayerInteractEvent e) {
        if (e.getPlayer().isOp()) {
            return;
        }
        if (e.getClickedBlock() == null) {
            return;
        }
        if (e.getClickedBlock().getType() == null) {
            return;
        }
        if (!(e.getClickedBlock().getType().equals(Material.CHEST) || e.getClickedBlock().getType().equals(Material.ENDER_CHEST))) {
            return;
        }
        Player p = e.getPlayer();
        String pn = p.getName();
        ArenaManager ah = Main.getInstance().getArenaManager();
        if (ah.isPlayerBusy(pn)) {
            if (e.getClickedBlock().getType().equals(Material.CHEST)) {
                if (!Main.getInstance().getConfigManager().isOpeningChestAllowed()) {
                    e.setCancelled(true);
                    return;
                }
            }
            if (e.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
                if (!Main.getInstance().getConfigManager().isOpeningEnderChestAllowed()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void test(InventoryCloseEvent e) {

    }
}
