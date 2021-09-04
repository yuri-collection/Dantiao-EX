package com.valorin.commands.sub;

import com.valorin.Main;
import com.valorin.caches.DanCache;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.AdminCommand;
import com.valorin.util.PlayerSet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.sm;

public class CMDExp extends SubCommand implements AdminCommand {

    public CMDExp() {
        super("exp");
    }

    public void sendHelp(Player player) {
        sm("", player);
        sm("&3&lDan&b&l&oTiao &f&l>> &a管理员帮助：段位经验操作", player, false);
        sm("&b/dt exp add <玩家名> <数额> &f- &a为某玩家添加经验", player, false);
        sm("&b/dt exp take <玩家名> <数额> &f- &a扣除某玩家的经验", player, false);
        sm("&b/dt exp set <玩家名> <数额> &f- &a设定某玩家的经验", player, false);
        sm("&b/dt exp view <玩家名> &f- &a查看某玩家的经验", player, false);
        sm("", player);
    }

    public boolean isNum(String str) {
        if (str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
            return Double.parseDouble(str) >= 0;
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
        DanCache cache = getInstance().getCacheHandler().getDan();
        List<String> playerSet = PlayerSet.get();
        if (args[1].equalsIgnoreCase("add")) {
            if (args.length != 4) {
                sm("&7正确格式：/dt exp add <玩家名> <数额>", player);
                return true;
            }
            if (!isNum(args[3])) {
                sm("&c[x]请输入有效的且大于零的数字", player);
                return true;
            }
            String targetPlayerName = args[2];
            if (!playerSet.contains(targetPlayerName)) {
                sm("&c[x]该玩家不存在！", player);
                return true;
            }
            int now = cache.get(targetPlayerName);
            int value = Integer.parseInt(args[3]);
            cache.set(targetPlayerName, now + value);
            Main.getInstance().getDanHandler().refreshPlayerDan(targetPlayerName);
            sm("&a[v]经验增添成功", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("take")) {
            if (args.length != 4) {
                sm("&7正确格式：/dt exp take <玩家名> <数额>", player);
                return true;
            }
            if (!isNum(args[3])) {
                sm("&c[x]请输入有效的且大于零的数字", player);
                return true;
            }
            String targetPlayerName = args[2];
            if (!playerSet.contains(targetPlayerName)) {
                sm("&c[x]该玩家不存在！", player);
                return true;
            }
            int now = cache.get(targetPlayerName);
            int value = Integer.parseInt(args[3]);
            if (now < value) {
                cache.set(targetPlayerName, 0);
            } else {
                cache.set(targetPlayerName, now - value);
            }
            Main.getInstance().getDanHandler().refreshPlayerDan(targetPlayerName);
            sm("&a[v]经验扣除成功", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("set")) {
            if (args.length != 4) {
                sm("&7正确格式：/dt exp set <玩家名> <数额>", player);
                return true;
            }
            if (!isNum(args[3])) {
                sm("&c[x]请输入有效的且大于零的数字", player);
                return true;
            }
            String targetPlayerName = args[2];
            if (!playerSet.contains(targetPlayerName)) {
                sm("&c[x]该玩家不存在！", player);
                return true;
            }
            int value = Integer.parseInt(args[3]);
            cache.set(targetPlayerName, value);
            Main.getInstance().getDanHandler().refreshPlayerDan(targetPlayerName);
            sm("&a[v]经验设置成功", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("view")) {
            if (args.length != 3) {
                sm("&7正确格式：/dt exp view <玩家名>", player);
                return true;
            }
            String targetPlayerName = args[2];
            if (!playerSet.contains(targetPlayerName)) {
                sm("&c[x]该玩家不存在！", player);
                return true;
            }
            int value = cache.get(targetPlayerName);
            sm("&6玩家&e{player}&6的段位经验[right] {amount}", player, "player amount",
                    new String[]{args[2], "" + value});
            return true;
        }
        sendHelp(player);
        return true;
    }

}
