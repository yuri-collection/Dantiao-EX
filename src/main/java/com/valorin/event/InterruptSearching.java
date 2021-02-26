package com.valorin.event;

import com.valorin.Main;
import com.valorin.queue.Matching;
import com.valorin.queue.MatchingHandler;
import com.valorin.util.ViaVersion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import static com.valorin.configuration.languagefile.MessageSender.gm;

public class InterruptSearching implements Listener {
    @EventHandler
    public void onQuitServerWhileSearching(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        String pn = p.getName();
        MatchingHandler mh = Main.getInstance().getMatchingHandler();
        if (mh.isPlayerBusy(pn)) {
            Matching matching = mh.getMatchingOfPlayer(pn);
            matching.disable(false);
        }
    }

    @EventHandler
    public void onTeleportWhileSearching(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        String pn = p.getName();
        MatchingHandler mh = Main.getInstance().getMatchingHandler();
        if (mh.isPlayerBusy(pn)) {
            Matching matching = mh.getMatchingOfPlayer(pn);
            matching.disable(false);
            ViaVersion.sendTitle(p, "§b!", gm("&7发生传送，匹配中断...", p), 0, 40, 20);
        }
    }
}
