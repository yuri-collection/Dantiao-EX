package com.valorin.inventory.special;

import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.gml;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.valorin.Main;
import com.valorin.itemstack.ArenaSelect;
import com.valorin.itemstack.GUIItems;
import com.valorin.queue.Matching;
import com.valorin.queue.MatchingHandler;
import com.valorin.util.ViaVersion;

public class INVStart {

	public static Map<String, ArenaSelect> arenaSelects = new HashMap<>();

	public static void openInv(String opener_name) {
		Player opener = Bukkit.getPlayerExact(opener_name);
		StartInvHolder holder = new StartInvHolder(opener_name);
		Inventory inventory = Bukkit.createInventory(holder, 27,
				gm("&0&l全服匹配 &9&l[right]", opener));
		holder.setInventory(inventory);

		MatchingHandler mh = Main.getInstance().getMatchingHandler();
		if (mh.isPlayerBusy(opener_name)) {
			ItemStack glass = ViaVersion.getGlassPane(15);
			for (int i = 0; i < 27; i++) {
				if (i != 13) {
					holder.getInventory().setItem(i, glass);
				}
			}
			Matching matching = mh.getMatchingOfPlayer(opener_name);
			matching.setInventory(inventory);
			matching.refreshItem();
		} else {
			ItemStack glass = ViaVersion.getGlassPane(0);
			for (int i = 0; i < 27; i++) {
				if (i != 13) {
					inventory.setItem(i, glass);
				}
			}
			inventory.setItem(13, GUIItems.getStart(opener_name));

			int size = Objects.requireNonNull(gml(" |&e在线寻找对手|&f&l>> &a点击开始", opener)).size() + 2;
			arenaSelects.put(opener_name, new ArenaSelect(opener, Main
					.getInstance().getCacheHandler().getArenaInfo()
					.getArenaInfoList(), size, inventory));
		}
		opener.openInventory(inventory);
	}
	/*
	 * public static void startSearch(StartInvHolder holder) { if
	 * (holder.getTimer() != null) { return; } holder.setStatus(1);
	 * refresh(holder); BukkitTask timer = new BukkitRunnable() { public void
	 * run() { holder.setSec(holder.getSec() + 1); refresh(holder); }
	 * }.runTaskTimerAsynchronously(Main.getInstance(), 20, 20);
	 * holder.setTimer(timer); ItemStack glass = ViaVersion.getGlassPane(15);
	 * for (int i = 0; i < 27; i++) { if (i != 13) {
	 * holder.getInventory().setItem(i, glass); } } }
	 * 
	 * public static void finishSearch(StartInvHolder holder, boolean isActive)
	 * { if (holder.getTimer() == null) { return; } Player opener =
	 * holder.getOpener(); holder.getTimer().cancel(); holder.setTimer(null); if
	 * (isActive) { sm("&7已中断匹配...", opener); } }
	 * 
	 * public static void refresh(StartInvHolder holder) {
	 * holder.getInventory().setItem(13, GUIItems.updataStart()); }
	 */
}
