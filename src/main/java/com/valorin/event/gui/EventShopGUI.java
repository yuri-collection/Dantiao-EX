package com.valorin.event.gui;

import com.valorin.Main;
import com.valorin.api.event.ShopEvent;
import com.valorin.caches.PointCache;
import com.valorin.caches.ShopCache;
import com.valorin.commands.sub.CMDShop;
import com.valorin.dan.CustomDan;
import com.valorin.dan.DanHandler;
import com.valorin.data.encapsulation.Good;
import com.valorin.inventory.INVShop;
import com.valorin.util.ItemGiver;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

import static com.valorin.configuration.languagefile.MessageSender.*;
import static com.valorin.inventory.INVShop.pages;

public class EventShopGUI implements Listener {
    public static void executeGoodCommands(Good good, Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (good.getCommands() != null) {
                    if (good.getCommands().size() != 0) {
                        List<String> commands = good.getCommands();
                        for (String command : commands) {
                            String way = command.split("\\|")[0];
                            String c = command.split("\\|")[1];
                            if (way.equalsIgnoreCase("op")) {
                                try {
                                    if (player.isOp()) {
                                        Bukkit.dispatchCommand(
                                                player,
                                                c.replace("{player}",
                                                        player.getName()));
                                    } else {
                                        player.setOp(true);
                                        Bukkit.dispatchCommand(
                                                player,
                                                c.replace("{player}",
                                                        player.getName()));
                                        player.setOp(false);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    sm("&c[x]??????&eOP&c?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????",
                                            player);
                                }
                            }
                            if (way.equalsIgnoreCase("player")) {
                                try {
                                    Bukkit.dispatchCommand(player,
                                            c.replace("{player}", player.getName()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    sm("&c[x]??????&e??????&c?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????",
                                            player);
                                }
                            }
                            if (way.equalsIgnoreCase("console")) {
                                try {
                                    Bukkit.dispatchCommand(
                                            Bukkit.getConsoleSender(),
                                            c.replace("{player}", player.getName()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    sm("&c[x]??????&e??????&c?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????",
                                            player);
                                }
                            }
                        }
                    }
                }
            }
        }.runTask(Main.getInstance());
    }

    @EventHandler
    public void page(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        String pn = p.getName();
        Inventory inv = e.getInventory();
        if (inv == null) {
            return;
        }
        if (e.getView().getTitle().equals(gm("&0&l???????????? &9&l[right]", p))) {
            e.setCancelled(true);
            if (e.getRawSlot() == 49) {
                int page = INVShop.pages.get(pn);
                int maxPage = INVShop.getMaxPage();
                if (e.getClick().equals(ClickType.LEFT)) {
                    if (page != maxPage) {
                        INVShop.loadItem(inv, pn, page + 1);
                        pages.put(pn, page + 1);
                        INVShop.loadPageItem(inv, pn, page + 1);
                    }
                }
                if (e.getClick().equals(ClickType.RIGHT)) {
                    if (page != 1) {
                        INVShop.loadItem(inv, pn, page - 1);
                        pages.put(pn, page - 1);
                        INVShop.loadPageItem(inv, pn, page - 1);
                    }
                }
            }
            if (e.getRawSlot() > 8 && e.getRawSlot() < 45) // ?????????
            {
                if (inv.getItem(e.getRawSlot()) == null) {
                    return;
                }
                int page = INVShop.pages.get(pn);
                int row;
                if ((e.getRawSlot() + 1) % 9 == 0) {
                    row = ((e.getRawSlot() + 1) - 9) / 9;
                } else {
                    row = ((e.getRawSlot() + 1) - 9) / 9 + 1;
                }
                int column;
                if ((e.getRawSlot() + 1) % 9 == 0) {
                    column = 9;
                } else {
                    column = (e.getRawSlot() - 9) - (row - 1) * 9 + 1;
                }
                int index = CMDShop.getNum(page, row, column);
                ShopCache shopCache = Main.getInstance().getCacheHandler()
                        .getShop();
                Good good = shopCache.getList().get(index);

                DanHandler dh = Main.getInstance().getDanHandler();
                if ((good.getDan() != null)
                        && (dh.getDanByName(good.getDan()) != null)) {
                    CustomDan danNeeded = dh.getDanByName(good.getDan());
                    CustomDan playerDan = dh.getPlayerDan(p.getName());
                    if (playerDan == null || playerDan.getNum() < danNeeded
                            .getNum()) {
                        sm("&c[x]???????????? {dan} &c???????????????????????????", p, "dan",
                                new String[]{danNeeded.getDisplayName()
                                        .replace("&", "??")});
                        return;
                    }
                }

                PointCache pointCache = Main.getInstance().getCacheHandler()
                        .getPoint();
                double price = good.getPrice();
                double points = pointCache.get(pn);
                if (points < price) {
                    sm("&c[x]????????????????????????????????????&e{price}&c?????????????????????&e{points}&c??????", p,
                            "price points", new String[]{"" + price,
                                    "" + points});
                    return;
                }
                ItemStack item = good.getItemStack();

                ShopEvent event = new ShopEvent(p, page, row, column, item);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) { // ?????????????????????
                    return;
                }

                ItemGiver ig = new ItemGiver(p, item);
                if (ig.getHasReceived()) {
                    pointCache.set(pn, points - price);
                    sml("&7========================================|&a[v]???????????????????????????????????????????????????|&7========================================",
                            p);
                    if (good.getBroadcast() != null) {
                        Bukkit.broadcastMessage(good.getBroadcast()
                                .replace("&", "??").replace("_", " ")
                                .replace("{player}", p.getName()));
                    }
                    int num = shopCache.getNumByIndex(index);
                    shopCache.updateSalesVolume(num);
                    INVShop.loadInv(pn, inv);

                    executeGoodCommands(good, p);
                }
            }
        }
    }
}
