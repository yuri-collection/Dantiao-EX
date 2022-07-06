package com.valorin.event;

import com.valorin.Main;
import com.valorin.util.SwordChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class EventClickPlayer implements Listener {
    @EventHandler
    public void showDan(PlayerInteractEntityEvent e) {
        if (Main.getInstance().getServerVersionType() >= 2) {
            //对于1.9及以上版本，要忽略副手的点击
            if (!e.getHand().equals(org.bukkit.inventory.EquipmentSlot.HAND)) {
                return;
            }
        }
        Player player = e.getPlayer();
        Entity clickedPlayer = e.getRightClicked();
        if (!(clickedPlayer instanceof Player)) { // 被点击的若不是玩家则return
            return;
        }
        if (!player.isSneaking()) { // 点击的玩家应该潜行
            return;
        }
        if (!Main.getInstance().getConfigManager().isClickPlayerToSendRequestAllowed()) {
            return;
        }
        if (!SwordChecker.isHoldingSword(player)) {
            return;
        }
        Bukkit.dispatchCommand(player, "dantiao send " + clickedPlayer.getName());
    }
}
