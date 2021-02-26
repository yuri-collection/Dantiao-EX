package com.valorin.event;

import com.valorin.teleport.ToLobby;
import com.valorin.teleport.ToLogLocation;
import com.valorin.util.ViaVersion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import static com.valorin.configuration.languagefile.MessageSender.gm;

public class InterruptAutoTeleport implements Listener {
    @EventHandler
    public void interrupt(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        if (ToLobby.timers.containsKey(playerName)) {
            ToLobby.timers.get(playerName).cancel();
            ToLobby.timers.remove(playerName);
            ToLobby.times.remove(playerName);
            ViaVersion.sendTitle(player, "§b!", gm("&7自动传送已取消...", player), 0,
                    40, 20);
        }
        if (ToLogLocation.timers.containsKey(playerName)) {
            ToLogLocation.timers.get(playerName).cancel();
            ToLogLocation.timers.remove(playerName);
            ToLogLocation.times.remove(playerName);
            ViaVersion.sendTitle(player, "§b!", gm("&7自动传送已取消...", player), 0,
                    40, 20);
        }
    }
}
