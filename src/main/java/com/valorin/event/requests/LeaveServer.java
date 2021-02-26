package com.valorin.event.requests;

import com.valorin.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveServer implements Listener {
    @EventHandler
    public void onDTLeaveGame(PlayerQuitEvent e) {
        String pn = e.getPlayer().getName();
        Main.getInstance().getRequestsHandler().clearRequests(pn, 1, null);
    }
}
