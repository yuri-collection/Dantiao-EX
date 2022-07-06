package com.valorin.event.requests;

import com.valorin.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventLeaveServer implements Listener {
    @EventHandler
    public void onDTLeaveGame(PlayerQuitEvent e) {
        String playerName = e.getPlayer().getName();
        Main.getInstance().getRequestsHandler().clearRequests(playerName, 1, null);
    }
}
