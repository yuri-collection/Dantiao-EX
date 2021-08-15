package com.valorin.commands.sub;

import static com.valorin.configuration.languagefile.MessageSender.sm;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.caches.ArenaInfoCache;
import com.valorin.data.Data;

public class CMDArenaOP_Commands {

    public static void sendHelp(Player player) {
        sm("&b/dt arena(a) commands add <竞技场编辑名> <执行方式(player/op/console)> <内容> &f- &a添加一条开赛时执行的指令",
                player);
        sm("&b/dt arena(a) commands clear <竞技场编辑名> &f- &a清空所有已添加的指令", player);
        sm("&b/dt arena(a) commands list <竞技场编辑名> &f- &a查看所有已添加的指令", player);
    }

    public static boolean onCommand(CommandSender sender, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        List<String> arenasNameList = Data.getArenas();
        if (args.length == 2) {
            sendHelp(player);
            return true;
        }
        ArenaInfoCache cache = Main.getInstance().getCacheHandler().getArenaInfo();
        if (args[2].equalsIgnoreCase("list")) {
            if (args.length != 4) {
                sm("&7正确格式：/dt a commands list <竞技场编辑名>", player);
                return true;
            }
            String editName = args[3];
            if (!arenasNameList.contains(editName)) {
                sm("&c[x]不存在的竞技场，请检查输入", player);
                return true;
            }
            List<String> commandsList = Main.getInstance().getCacheHandler().getArenaInfo().get(editName).getCommandList();
            if (commandsList.size() == 0) {
                sm("&c[x]该竞技场不存在任何指令", player);
                return true;
            }
            sm("&6指令如下 [right]", player);
            for (String command : commandsList) {
                sender.sendMessage("§e" + command.replace("|", " "));
            }
            sm("&6共计 &f&l{amount} &6条", player, "amount", new String[]{"" + commandsList.size()});
            return true;
        }
        if (args[2].equalsIgnoreCase("add")) {
            if (args.length != 6) {
                sm("&7正确格式：/dt a commands add <竞技场编辑名> <执行方式> <内容>", player);
                return true;
            }
            String editName = args[3];
            if (!arenasNameList.contains(editName)) {
                sm("&c[x]不存在的竞技场，请检查输入", player);
                return true;
            }
            String way = args[4];
            String command = args[5];
            if (!way.equalsIgnoreCase("op") && !way.equalsIgnoreCase("player")
                    && !way.equalsIgnoreCase("console")) {
                sm("&c[x]执行方式请输入op/player/console(不区分大小写)，OP即以管理员身份执行，Player即以玩家自己的身份执行，Console即以后台身份执行",
                        player);
                return true;
            }
            command = way + "|" + command.replace("_", " ");
            if (cache.get(editName).getCommandList().size() == 0) {
                List<String> newCommands = new ArrayList<>();
                newCommands.add(command);
                Main.getInstance().getCacheHandler().getArenaInfo().setCommandList(editName, newCommands);
            } else {
                List<String> commands = cache.get(editName).getCommandList();
                commands.add(command);
                Main.getInstance().getCacheHandler().getArenaInfo().setCommandList(editName, commands);
            }
            sm("&a[v]添加成功 {command}", player, "command", new String[]{command});
            return true;
        }
        if (args[2].equalsIgnoreCase("clear")) {
            if (args.length != 4) {
                sm("&7正确格式：/dt a commands clear <竞技场编辑名>", player);
                return true;
            }
            String editName = args[3];
            if (!arenasNameList.contains(editName)) {
                sm("&c[x]不存在的竞技场，请检查输入", player);
                return true;
            }
            if (cache.get(editName).getCommandList().size() == 0) {
                sm("&c[x]该竞技场不存在任何指令", player);
                return true;
            }
            Main.getInstance().getCacheHandler().getArenaInfo().setCommandList(editName, null);
            sm("&a[v]竞技场指令清空完毕", player);
            return true;
        }
        sendHelp(player);
        return true;
    }
}
