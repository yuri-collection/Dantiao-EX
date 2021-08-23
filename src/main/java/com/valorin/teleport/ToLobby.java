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
		BukkitTask timer = new BukkitRunnable() {
			@Override
			public void run() {
				if (isNow) {
					Bukkit.getScheduler().runTask(Main.getInstance(), () -> Bukkit.dispatchCommand(player, "dt lobby"));
					this.cancel();
				} else {
					int countDown = Main.getInstance().getConfigManager()
							.getTeleportCountDown();
					if (countDown == 0) {
						Bukkit.getScheduler().runTask(Main.getInstance(),
								() -> Bukkit.dispatchCommand(player, "dt lobby"));
						this.cancel();
					} else {
						if (countDown < 5) {
							countDown = 5;
						}
						times.put(player.getName(), times.get(player.getName()) + 1);

						int time = times.get(player.getName());
						if (time >= 3) {
							if (time == countDown) {
								Bukkit.getScheduler().runTask(
										Main.getInstance(),
										() -> Bukkit.dispatchCommand(player,
												"dt lobby"));
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
														new String[] { ""
																+ (countDown - time) }),
												0, 20, 20);
							}
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
		if (!isNow) {
			timers.put(player.getName(), timer);
			times.put(player.getName(), 0);
		}
	}
}
