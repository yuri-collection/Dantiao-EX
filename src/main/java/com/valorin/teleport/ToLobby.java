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
	public static Map<String, BukkitTask> timers = new HashMap<String, BukkitTask>();
	public static Map<String, Integer> times = new HashMap<String, Integer>();

	public static void to(Player p, boolean isNow) {
		if (Main.getInstance().getCacheHandler().getArea().getLobby() == null) {
			return;
		}
		if (p == null) {
			return;
		}
		BukkitTask timer = new BukkitRunnable() {
			@Override
			public void run() {
				if (isNow) {
					Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
						Bukkit.dispatchCommand(p, "dt lobby");
					});
					this.cancel();
				} else {
					int countDown = Main.getInstance().getConfigManager()
							.getTeleportCountDown();
					if (countDown == 0) {
						Bukkit.getScheduler().runTask(Main.getInstance(),
								() -> {
									Bukkit.dispatchCommand(p, "dt lobby");
								});
						this.cancel();
					} else {
						if (countDown < 5) {
							countDown = 5;
						}
						times.put(p.getName(), times.get(p.getName()) + 1);

						int time = times.get(p.getName());
						if (time >= 3) {
							if (time == countDown) {
								Bukkit.getScheduler().runTask(
										Main.getInstance(),
										() -> {
											Bukkit.dispatchCommand(p,
													"dt lobby");
										});
								this.cancel();
								timers.remove(p.getName());
								times.remove(p.getName());
							} else {
								ViaVersion
										.sendTitle(
												p,
												gm("&b&l即将传送", p),
												gm("&f你将在 &7&l{time} &f秒后自动传送离开竞技场",
														p,
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
			timers.put(p.getName(), timer);
			times.put(p.getName(), 0);
		}
	}
}
