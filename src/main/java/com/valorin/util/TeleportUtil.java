package com.valorin.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.List;

public class TeleportUtil {
    public static List<String> legalTeleportPlayer = new ArrayList<>();

    public static void deal(Player player, Location location) {
        String playerName = player.getName();
        legalTeleportPlayer.add(playerName);
        player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        legalTeleportPlayer.remove(playerName);
    }
}
