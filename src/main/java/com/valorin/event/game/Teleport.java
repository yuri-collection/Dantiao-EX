package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class Teleport implements Listener {
    @EventHandler
    public void onLeaveGameWorld(PlayerTeleportEvent e) {// 突然传送到别的世界去了
        Player p = e.getPlayer();
        String pn = p.getName();
        ArenaManager ah = Main.getInstance().getArenaManager();
        if (pn == null) {
            return;
        }
        if (ah.isPlayerBusy(pn)) {
            Arena arena = ah.getArena(ah.getPlayerOfArena(pn));
            if (!arena.canTeleport()) {
                if (!e.getTo().getWorld().equals(p.getLocation().getWorld())) {
                    e.setCancelled(true);
                    sm("&c[x]发生非法传送，已制止", p);
                }
            }
        }
    }

    @EventHandler
    public void onTpToGamer(PlayerTeleportEvent e) {// 场外玩家企图传送到场内玩家身边给TA武器什么的
        Player p = e.getPlayer();
        String pn = p.getName();
        ArenaManager ah = Main.getInstance().getArenaManager();
        if (pn == null) {
            return;
        }
        Location to = e.getTo();
        for (String arenaEditName : ArenaManager.busyArenasName) {
            Arena arena = ah.getArena(arenaEditName);
            if (p.equals(Bukkit.getPlayerExact(arena.getp1())) ||
                    p.equals(Bukkit.getPlayerExact(arena.getp2()))) {
                continue;
            }
            Location player1Location = Bukkit.getPlayerExact(arena.getp1())
                    .getLocation();
            Location player2Location = Bukkit.getPlayerExact(arena.getp2())
                    .getLocation();
            if ((Math.abs(player1Location.getBlockX() - to.getBlockX()) <= 2
                    && Math.abs(player1Location.getBlockY() - to.getBlockY()) <= 2 && Math
                    .abs(player1Location.getBlockZ() - to.getBlockZ()) <= 2)
                    || (Math.abs(player2Location.getBlockX() - to.getBlockX()) <= 2
                    && Math.abs(player2Location.getBlockY()
                    - to.getBlockY()) <= 2 && Math
                    .abs(player2Location.getBlockZ() - to.getBlockZ()) <= 2)) {
                e.setCancelled(true);
                sm("&c[x]发生非法传送，已制止", p);
            }
        }
    }
}
