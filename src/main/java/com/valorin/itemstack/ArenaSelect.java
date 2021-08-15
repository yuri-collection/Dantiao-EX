package com.valorin.itemstack;

import static com.valorin.configuration.languagefile.MessageSender.gm;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.valorin.Main;
import com.valorin.data.encapsulation.ArenaInfo;

public class ArenaSelect {
	private final BukkitTask timer;
	private String arenaEditName;
	private final int firstIndex;
	private final int endIndex;
	private int nowIndex;
	private final List<String> arenaEditNameList = new ArrayList<>();
	private final List<String> arenaDisplayNameList = new ArrayList<>();

	private int lastEditIndex;
	private boolean color;

	public ArenaSelect(Player player, List<ArenaInfo> arenaInfoList,
			int firstIndex, Inventory inventory) {
		this.firstIndex = firstIndex;
		this.endIndex = firstIndex + arenaInfoList.size();
		nowIndex = firstIndex;
		lastEditIndex = firstIndex;
		for (ArenaInfo arenaInfo : arenaInfoList) {
			arenaEditNameList.add(arenaInfo.getEditName());
			if (arenaInfo.getDisplayName() != null) {
				arenaDisplayNameList.add(arenaInfo.getDisplayName()
						.replace("&0", "").replace("&1", "").replace("&2", "")
						.replace("&3", "").replace("&4", "").replace("&5", "")
						.replace("&6", "").replace("&7", "").replace("&8", "")
						.replace("&9", "").replace("&a", "").replace("&b", "")
						.replace("&c", "").replace("&d", "").replace("&e", "")
						.replace("&f", "").replace("&o", "").replace("&m", "")
						.replace("&n", "").replace("&k", "").replace("&l", ""));
			} else {
				arenaDisplayNameList.add(arenaInfo.getDisplayName());
			}
		}

		timer = new BukkitRunnable() {
			public void run() {
				refreshItem(inventory, player);
			}
		}.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
	}

	public void next(Player player, Inventory inventory) {
		if (nowIndex < endIndex) {
			nowIndex++;
			arenaEditName = arenaEditNameList.get(nowIndex - (firstIndex + 1));
		} else {
			nowIndex = firstIndex;
			arenaEditName = null;
		}
		refreshItem(inventory, player);
	}

	public void cancel() {
		timer.cancel();
	}

	public String getSelectedArenaEditName() {
		return arenaEditName;
	}

	private void refreshItem(Inventory inventory, Player player) {
		ItemStack ItemStack = inventory.getItem(13);
		ItemMeta ItemMeta = ItemStack.getItemMeta();
		List<String> lore = ItemMeta.getLore();
		String prefix;
		if (color) {
			prefix = "§6➣ ";
			color = false;
		} else {
			prefix = "§f➣ ";
			color = true;
		}
		if (nowIndex != lastEditIndex) {
			if (lastEditIndex == firstIndex) {
				lore.set(lastEditIndex, "§7" + gm("随机", player));
			} else {
				lore.set(
						lastEditIndex,
						"§7"
								+ arenaDisplayNameList.get(lastEditIndex
										- (firstIndex + 1)));
			}
		}
		if (nowIndex == firstIndex) {
			lore.set(nowIndex, prefix + "§f" + gm("随机", player));
			arenaEditName = null;
		} else {
			lore.set(
					nowIndex,
					prefix
							+ "§f"
							+ arenaDisplayNameList.get(nowIndex
									- (firstIndex + 1)));
			arenaEditName = arenaEditNameList.get(nowIndex - (firstIndex + 1));
		}
		lastEditIndex = nowIndex;
		ItemMeta.setLore(lore);
		ItemStack.setItemMeta(ItemMeta);
		inventory.setItem(13, ItemStack);
	}
}
