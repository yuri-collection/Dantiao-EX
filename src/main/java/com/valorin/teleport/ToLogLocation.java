package com.valorin.teleport;

import static com.valorin.configuration.languagefile.MessageSender.gm;

import java.util.HashMap;
import java.util.Map;

import com.valorin.util.TeleportUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.valorin.Main;
import com.valorin.util.ViaVersion;

public class ToLogLocation {
    public static Map<String, BukkitTask> timers = new HashMap<>();
    public static Map<String, Integer> times = new HashMap<>();

    public static void to(Player winner, Player loser, Location winnerLocation,
                          Location loserLocation, boolean isWinnerNow) {
        if (winnerLocation == null || loserLocation == null || winner == null
                || loser == null) {
            return;
        }

        TeleportUtil.deal(loser, loserLocation);

        final int[] countDown = {Main.getInstance().getConfigManager()
                .getTeleportCountDown()};
        if (isWinnerNow || countDown[0] == 0) {
            TeleportUtil.deal(winner, winnerLocation);
            return;
        }

        BukkitTask timer = new BukkitRunnable() {
            @Override
            public void run() {
                if (countDown[0] < 5) {
                    countDown[0] = 5;
                }
                times.put(winner.getName(),
                        times.getOrDefault(winner.getName(), -1) + 1);

                int time = times.get(winner.getName());
                if (time >= 2) {
                    if (time == countDown[0]) {
                        this.cancel();
                        timers.remove(winner.getName());
                        times.remove(winner.getName());
                        Bukkit.getScheduler().runTask(
                                Main.getInstance(), () -> winner.teleport(winnerLocation));
                    } else {
                        ViaVersion
                                .sendTitle(
                                        winner,
                                        gm("&b&l即将传送", winner),
                                        gm("&f你将在 &7&l{time} &f秒后自动传送离开竞技场",
                                                winner,
                                                "time",
                                                new String[]{""
                                                        + (countDown[0] - time)}),
                                        0, 20, 20);
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
        timers.put(winner.getName(), timer);
        times.put(winner.getName(), 0);
    }
}
