package com.valorin.event.gui;

import com.valorin.Main;
import com.valorin.inventory.special.INVStart;
import com.valorin.inventory.special.StartInvHolder;
import com.valorin.itemstack.ArenaSelect;
import com.valorin.queue.Matching;
import com.valorin.queue.MatchingHandler;
import com.valorin.util.ViaVersion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class EventStartGUI implements Listener {

    @EventHandler
    public void start(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        String playerName = player.getName();
        Inventory inventory = e.getInventory();
        if (inventory == null) {
            return;
        }
        if (inventory.getHolder() == null) {
            return;
        }
        if (!(inventory.getHolder() instanceof StartInvHolder)) {
            return;
        }
        e.setCancelled(true);
        if (e.getRawSlot() != 13 && e.getRawSlot() != 1) {
            return;
        }
        MatchingHandler mh = Main.getInstance().getMatchingHandler();
        if (!mh.isPlayerBusy(playerName)) {
            ClickType click = e.getClick();
            if (click.equals(ClickType.LEFT)) {
                mh.start(playerName);
                Matching matching = mh.getMatchingOfPlayer(playerName);
                if (matching != null) {
                    if (matching.getArenaEditName() == null) {
                        if (Main.getInstance().getCacheHandler().getArenaInfo()
                                .size() == 0) {
                            sm("&c服务器内没有设置任何竞技场！请联系管理员！", player);
                            matching.disable(true);
                            player.closeInventory();
                            return;
                        }
                    }

                    matching.setInventory(inventory);
                    for (int i = 0; i < 27; i++) {
                        if (i != 13) {
                            inventory.setItem(i, ViaVersion.getGlassPane(15));
                        }
                    }
                    player.playSound(player.getLocation(), ViaVersion.getSound(
                            "BLOCK_ANVIL_PLACE", "ANVIL_PLACE"), 1, 0);
                }
            }
            if (click.equals(ClickType.RIGHT)) {
                INVStart.arenaSelects.get(playerName).next(player, inventory);
                player.playSound(player.getLocation(), ViaVersion.getSound(
                        "BLOCK_NOTE_XYLOPHONE", "NOTE_XYLOPHONE"), 1, 0);
            }
            /*
             * SearchingQueue queue = Main.getInstance().getSearchingQueue();
             * String waiter_name = queue.getWaiter(); if (waiter_name == null)
             * { queue.setWaiter(opener_name); INVStart.startSearch(holder); }
             * else { queue.setWaiter(null); Player waiter =
             * Bukkit.getPlayerExact(waiter_name); new StartGame(opener, waiter,
             * null, null, 1); }
             */
        } else {
            Matching matching = mh.getMatchingOfPlayer(playerName);
            matching.disable(true);
            player.closeInventory();
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Inventory inventory = e.getInventory();
        if (inventory == null) {
            return;
        }
        if (inventory.getHolder() == null) {
            return;
        }
        if (!(inventory.getHolder() instanceof StartInvHolder)) {
            return;
        }
        Player player = (Player) e.getPlayer();
        String playerName = player.getName();
        MatchingHandler mh = Main.getInstance().getMatchingHandler();
        if (mh.isPlayerBusy(playerName)) {
            Matching matching = mh.getMatchingOfPlayer(playerName);
            matching.setInventory(null);
        } else {
            if (INVStart.arenaSelects.containsKey(playerName)) {
                ArenaSelect arenaSelect = INVStart.arenaSelects.get(playerName);
                arenaSelect.cancel();
                INVStart.arenaSelects.remove(playerName);
            }
        }
        /*
         * SearchingQueue queue = Main.getInstance().getSearchingQueue(); if
         * (holder.getStatus() == 1) { queue.setWaiter(null);
         * INVStart.finishSearch(holder, true); }
         */
    }
}
