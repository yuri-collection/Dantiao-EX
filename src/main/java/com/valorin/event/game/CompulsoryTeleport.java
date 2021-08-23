package com.valorin.event.game;

import com.valorin.Main;
import com.valorin.teleport.ToLobby;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class CompulsoryTeleport implements Listener {
    public static Map<String, Location> players = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        back(player);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        back(player);
    }

    public static void back(Player player) {
        String playerName = player.getName();
        if (players.containsKey(playerName)) {
            new BukkitRunnable() {
                public void run() {
                    new BukkitRunnable() {
                        public void run() {
                            if (Main.getInstance().getCacheHandler().getArea().getLobby() != null) {
                                ToLobby.to(player, true);
                                if (!player.isDead()) {
                                    sm("&b已将你带回单挑大厅！", player);
                                }
                            } else {
                                player.teleport(players.get(playerName));
                            }
                            players.remove(playerName);
                        }
                    }.runTask(Main.getInstance());
                }
            }.runTaskLaterAsynchronously(Main.getInstance(), 20);
        }
    }
}
