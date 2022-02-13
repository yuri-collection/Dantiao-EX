package com.valorin.commands.sub;

import com.valorin.Main;
import com.valorin.caches.DanCache;
import com.valorin.caches.RankingCache;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.AdminCommand;
import com.valorin.commands.way.InServerCommand;
import com.valorin.dan.CustomDan;
import com.valorin.dan.DanHandler;
import com.valorin.data.Data;
import com.valorin.util.ViaVersion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.sm;

public class CMDSeason extends SubCommand implements InServerCommand,
        AdminCommand {

    public CMDSeason() {
        super("season", "ss");
    }

    public void sendHelp(Player player) {
        sm("", player);
        sm("&3&lDan&b&l&oTiao &f&l>> &a管理员帮助：赛季操作", player, false);
        sm("&b/dt season(ss) setmessage <段位名> <内容> &f- &a为某段位设置赛季结束后的致语（邮件发送）",
                player, false);
        sm("&b/dt season(ss) setitem <段位名> &f- &a打开一个面板，在里面放置赛季结束后某段位的奖励物品（邮件发送）",
                player, false);
        sm("&b/dt season(ss) setpoint <段位名> <数额> &f- &a为某段位设置赛季结束后的积分奖励（直接发送）",
                player, false);
        sm("&b/dt season(ss) enable <段位名> &f- &a为某段位启用告示功能，启用后该段位的玩家才能在赛季结束后收到相关邮件和积分奖励",
                player, false);
        sm("&b/dt season(ss) disable <段位名> &f- &a为某段位关闭告示功能，关闭后该段位的玩家将不会在赛季结束后收到任何邮件告示和积分奖励",
                player, false);
        sm("&b/dt season(ss) restart &f- &a开启新赛季并结算奖励", player, false);
        sm("", player);
    }

    public boolean isNum(String str) {
        if (str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
            return !(Double.parseDouble(str) < 0);
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (args.length == 1) {
            sendHelp(player);
            return true;
        }
        DanHandler dh = Main.getInstance().getDanHandler();
        if (args[1].equalsIgnoreCase("setmessage")) {
            if (args.length != 4) {
                sm("&7正确格式：/dt ss setmessage <段位名> <内容>", player);
                return true;
            }
            String danEditName = args[2];
            if (dh.getDanByName(danEditName) == null) {
                sm("&c[x]该段位不存在！", player);
                return true;
            }
            String message = args[3].replace("_", " ");
            Data.setSeasonDanMessage(danEditName, message);
            sm("&a[v]成功设置致语", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("setitem")) {
            if (args.length != 3) {
                sm("&7正确格式：/dt ss setitem <段位名>", player);
                return true;
            }
            String danEditName = args[2];
            if (dh.getDanByName(danEditName) == null) {
                sm("&c[x]该段位不存在！", player);
                return true;
            }
            Inventory inv = Bukkit.createInventory(null, 36, danEditName + ":"
                    + gm("请将奖励物品放进来"));
            List<ItemStack> itemStacks = Data
                    .getSeasonDanItemStacks(danEditName);
            int i = 0;
            for (ItemStack itemStack : itemStacks) {
                inv.setItem(i, itemStack);
                i++;
            }
            player.openInventory(inv);
            sm("&a[v]已打开面板，请将奖励物品放到里面", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("setpoint")) {
            if (args.length != 4) {
                sm("&7正确格式：/dt ss setpoint <段位名> <数额>", player);
                return true;
            }
            String danEditName = args[2];
            if (dh.getDanByName(danEditName) == null) {
                sm("&c[x]该段位不存在！", player);
                return true;
            }
            if (!isNum(args[3])) {
                sm("&c[x]请输入有效的且大于零的数字", player);
                return true;
            }
            int points = Integer.parseInt(args[3]);
            Data.setSeasonDanPoints(danEditName, points);
            sm("&a[v]成功设置奖励积分", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("enable")) {
            if (args.length != 3) {
                sm("&7正确格式：/dt ss enable <段位名>", player);
                return true;
            }
            String danEditName = args[2];
            if (dh.getDanByName(danEditName) == null) {
                sm("&c[x]该段位不存在！", player);
                return true;
            }
            if (Data.getSeasonDanMessage(danEditName) == null) {
                sm("&c[x]必设选项致语未设置，请先设置后再启用告示功能", player);
                return true;
            }
            Data.setSeasonDanEnable(danEditName, true);
            sm("&a[v]告示功能开启成功", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("disable")) {
            if (args.length != 3) {
                sm("&7正确格式：/dt ss disable <段位名>", player);
                return true;
            }
            String danEditName = args[2];
            if (dh.getDanByName(danEditName) == null) {
                sm("&c[x]该段位不存在！", player);
                return true;
            }
            Data.setSeasonDanEnable(danEditName, false);
            sm("&a[v]告示功能关闭成功", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("restart")) {
            Plugin pluginMailBox = Bukkit.getPluginManager().getPlugin("MailBox");
            if (pluginMailBox == null) {
                sm("&c[x]请先按照文档指引安装MailBox插件，否则无法发送邮件", player);
                return true;
            }
            if (!pluginMailBox.isEnabled()) {
                sm("&c[x]MailBox插件未加载", player);
                return true;
            }
            sm("&a[v]赛季已重启！排行榜数据和段位数据正在清空，同时段位奖励正在发放", player);
/*            Bukkit.getScheduler().runTaskAsynchronously(
                    Main.getInstance(),
                    () -> {*/
            RankingCache rankingCache = Main.getInstance()
                    .getCacheHandler().getRanking();
            rankingCache.setWin(new ArrayList<>());
            rankingCache.setKD(new ArrayList<>());
            DanCache danCache = Main.getInstance()
                    .getCacheHandler().getDan();

            Method methodCreateBaseFileMail = null;
            Method methodSetItemList = null;
            Method methodSetCommandDescription = null;
            Method methodSetCommandList = null;
            Method methodSetRecipient = null;
            Method methodSend = null;
            Class classMailPlayer = null;
            if (pluginMailBox.getDescription().getVersion().startsWith("2")) {
                try {
                    Class<?> classMailBoxAPI = Class.forName("com.tripleying.qwq.MailBox.API.MailBoxAPI");
                    methodCreateBaseFileMail = classMailBoxAPI.getMethod("createBaseFileMail", String.class, String.class, String.class, String.class, String.class);

                    Class<?> classBaseFileMail = Class.forName("com.tripleying.qwq.MailBox.Mail.BaseFileMail");
                    methodSetItemList = classBaseFileMail.getMethod("setItemList", List.class);
                    methodSetCommandDescription = classBaseFileMail.getMethod("setCommandDescription", List.class);
                    methodSetCommandList = classBaseFileMail.getMethod("setCommandList", List.class);
                    methodSend = classBaseFileMail.getMethod("Send", CommandSender.class, ConversationContext.class);

                    classMailPlayer = Class.forName("com.tripleying.qwq.MailBox.Mail.MailPlayer");
                    methodSetRecipient = classMailPlayer.getMethod("setRecipient", List.class);

                } catch (ClassNotFoundException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            boolean isVersion2x = pluginMailBox.getDescription().getVersion().startsWith("2");

            for (Player onlinePlayer : ViaVersion
                    .getOnlinePlayers()) {
                String playerName = onlinePlayer.getName();
                CustomDan dan = dh.getPlayerDan(playerName);
                if (dan != null) {
                    String danEditName = dan.getEditName();
                    if (isVersion2x) {
                        com.valorin.mailbox.SendPersonMail_2.sendMail(onlinePlayer.getName(), danEditName,
                                methodCreateBaseFileMail,
                                methodSetItemList,
                                methodSetCommandDescription,
                                methodSetCommandList,
                                methodSetRecipient,
                                methodSend,
                                classMailPlayer);
                    } else {
                        com.valorin.mailbox.SendPersonMail_3.sendOnlinePlayerMail(onlinePlayer, danEditName);
                    }
                }
                danCache.set(playerName, 0);
            }
            for (OfflinePlayer offlinePlayer : Bukkit
                    .getOfflinePlayers()) {
                String playerName = offlinePlayer.getName();
                CustomDan dan = dh.getPlayerDan(playerName);
                if (dan != null) {
                    String danEditName = dan.getEditName();
                    if (isVersion2x) {
                        com.valorin.mailbox.SendPersonMail_2.sendMail(offlinePlayer.getName(), danEditName,
                                methodCreateBaseFileMail,
                                methodSetItemList,
                                methodSetCommandDescription,
                                methodSetCommandList,
                                methodSetRecipient,
                                methodSend,
                                classMailPlayer);
                    } else {
                        com.valorin.mailbox.SendPersonMail_3.sendOfflinePersonMail(offlinePlayer, danEditName);
                    }
                }
                danCache.set(playerName, 0);
            }
            /*});*/
            return true;
        }
        sendHelp(player);
        return true;
    }
}
