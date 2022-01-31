package com.valorin.teleport;

import java.util.HashMap;
import java.util.Map;

import static com.valorin.configuration.languagefile.MessageSender.gm;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.valorin.Main;
import com.valorin.util.ViaVersion;

public class ToLobby {
    public static Map<String, BukkitTask> timers = new HashMap<>();
    public static Map<String, Integer> times = new HashMap<>();

    public static void to(Player player, boolean isNow) {
        if (Main.getInstance().getCacheHandler().getArea().getLobby() == null) {
            return;
        }
        if (player == null) {
            return;
        }
        final int[] countDown = {Main.getInstance().getConfigManager()
                .getTeleportCountDown()};
        if (isNow || countDown[0] == 0) {
            Bukkit.getScheduler().runTask(Main.getInstance(), () -> Bukkit.dispatchCommand(player, "dantiao lobby"));
            return;
        }

        BukkitTask timer = new BukkitRunnable() {
            @Override
            public void run() {
                if (countDown[0] < 5) {
                    countDown[0] = 5;
                }
                times.put(player.getName(), times.getOrDefault(player.getName(), -1) + 1);

                int time = times.get(player.getName());
                if (time >= 3) {
                    if (time == countDown[0]) {
                        Bukkit.getScheduler().runTask(
                                Main.getInstance(),
                                () -> Bukkit.dispatchCommand(player,
                                        "dantiao lobby"));
                        this.cancel();
                        timers.remove(player.getName());
                        times.remove(player.getName());
                    } else {
                        ViaVersion
                                .sendTitle(
                                        player,
                                        gm("&b&l即将传送", player),
                                        gm("&f你将在 &7&l{time} &f秒后自动传送离开竞技场",
                                                player,
                                                "time",
                                                new String[]{""
                                                        + (countDown[0] - time)}),
                                        0, 20, 20);
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
        timers.put(player.getName(), timer);
        times.put(player.getName(), 0);
    }
}
