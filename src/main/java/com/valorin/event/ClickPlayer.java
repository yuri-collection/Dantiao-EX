package com.valorin.event;

import com.valorin.Main;
import com.valorin.util.SwordChecker;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class ClickPlayer implements Listener {
    @EventHandler
    public void showDan(PlayerInteractEntityEvent e) {
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
        Bukkit.dispatchCommand(player, "dt send " + ((Player) clickedPlayer).getName());
    }
}
