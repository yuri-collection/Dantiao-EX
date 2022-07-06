package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EventPVP implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void pvp(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            String playerName = player.getName();
            ArenaManager arenaManager = Main.getInstance().getArenaManager();
            Arena arena = arenaManager.getArena(arenaManager.getPlayerOfArena(playerName));
            if (!arenaManager.isPlayerBusy(playerName)) {
                return;
            }
            String theOther = arena.getTheOtherPlayer(playerName);
            if (e.getDamager() instanceof Player) {
                Player attacker = (Player) e.getDamager();
                if (attacker.getName().equals(theOther)) {// 锁定对打双方
                    boolean isp1 = arena.isp1(theOther);
                    arena.addHit(isp1);
                    if (arena.getHit(isp1) == 2) {
                        arena.addExp(isp1, 1);
                    }
                    if (arena.getHit(isp1) == 3) {
                        arena.addExp(isp1, 2);
                    }
                    if (arena.getHit(isp1) == 4) {
                        arena.addExp(isp1, 3);
                    }
                    if (arena.getHit(isp1) >= 5) {
                        arena.addExp(isp1, 4);
                    }

                    arena.addDamage(isp1, e.getDamage());
                    if (e.getDamage() > arena.getMaxDamage(isp1)) {
                        arena.setMaxDamage(isp1, e.getDamage());
                    }
                }
            }
        }
    }
}
