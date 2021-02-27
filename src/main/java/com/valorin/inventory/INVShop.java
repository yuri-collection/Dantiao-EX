package com.valorin.inventory;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.valorin.Main;
import com.valorin.data.encapsulation.Good;
import com.valorin.itemstack.GUIItems;
import com.valorin.util.ViaVersion;

public class INVShop {
	public static HashMap<String, Integer> pages = new HashMap<String, Integer>();

	public static int getMaxPage() {
		int maxPage;
		int goodAmount = Main.getInstance().getCacheHandler().getShop().size();
		if (goodAmount % 36 != 0) {
			maxPage = goodAmount / 36 + 1;
		} else {
			maxPage = goodAmount / 36;
		}
		if (goodAmount == 0) {
			maxPage = 1;
		}
		return maxPage;
	}

	public static void loadInv(String pn, Inventory inv) {
		Player opener = Bukkit.getPlayerExact(pn);

		int[] glass = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 45, 46, 47, 48, 50, 51, 52,
				53 };
		for (int i : glass) {
			inv.setItem(i, ViaVersion.getGlassPane(0));
		}

		int page;
		if (!pages.containsKey(pn)) {
			page = 1;
			pages.put(pn, 1);
		} else {
			page = pages.get(pn);
		}

		int maxPage = getMaxPage();

		inv.setItem(49, GUIItems.getPage(pn, page, maxPage));

		loadItem(inv, pn, page);
		opener.openInventory(inv);
	}

	public static void loadPageItem(Inventory inv, String pn, int page) {
		int maxPage = getMaxPage();
		inv.setItem(49, GUIItems.getPage(pn, page, maxPage));
	}

	public static void loadItem(Inventory inv, String pn, int page) {
		List<Good> goodList = Main.getInstance().getCacheHandler().getShop()
				.getList();
		int goodNumber = goodList.size();
		int n;
		if (goodNumber - ((page - 1) * 36) > 36) {
			n = 36;
		} else {
			n = goodNumber - ((page - 1) * 36);
		}
		for (int i = 0; i < n; i++) {
			inv.setItem(
					i + 9,
					GUIItems.getGoodShowed(pn,
							goodList.get((page - 1) * 36 + i)));
		}
		for (int i = n; i < 36; i++) {
			inv.setItem(i + 9, new ItemStack(Material.AIR));
		}
	}
}
