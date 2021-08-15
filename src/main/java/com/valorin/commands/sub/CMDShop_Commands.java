package com.valorin.commands.sub;

import static com.valorin.configuration.languagefile.MessageSender.sm;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.caches.ShopCache;

public class CMDShop_Commands {

	public static void sendHelp(Player p) {
		sm("&b/dt shop(s) commands add <页数> <行> <列> <执行方式(player/op/console)> <内容> &f- &a添加一条购买后执行的指令",
				p);
		sm("&b/dt shop(s) commands clear <页数> <行> <列> &f- &a清空所有已添加的指令", p);
		sm("&b/dt shop(s) commands list <页数> <行> <列> &f- &a查看所有已添加的指令", p);
	}

	public static boolean onCommand(CommandSender sender, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		if (args.length == 2) {
			sendHelp(player);
			return true;
		}
		ShopCache cache = Main.getInstance().getCacheHandler().getShop();
		if (args[2].equalsIgnoreCase("list")) {
			if (args.length != 6) {
				sm("&7正确格式：/dt s commands list <页数> <行> <列>，行和列不考虑GUI顶部和底部的玻璃板", player);
				return true;
			}
			if (!CMDShop.isInt(args[3], args[4], args[5])) {
				sm("&c[x]请输入有效的阿拉伯数字！", player);
				return true;
			}
			int page = Integer.parseInt(args[3]);
			int row = Integer.parseInt(args[4]);
			int column = Integer.parseInt(args[5]);
			int index = CMDShop.getNum(page, row, column);
			if (index >= cache.size()) {
				sm("&c[x]不存在这个商品", player);
				return true;
			}
			List<String> commands = cache.get(cache.getNumByIndex(index)).getCommands();
			if (commands.size() == 0) {
				sm("&c[x]该商品不存在任何指令", player);
				return true;
			}
			sm("&6指令如下 [right]", player);
			for (String command : commands) {
				sender.sendMessage("§e" + command.replace("|", " "));
			}
			sm("&6共计 &f&l{amount} &6条", player, "amount", new String[] { ""+commands.size() });
			return true;
		}
		if (args[2].equalsIgnoreCase("add")) {
			if (args.length != 8) {
				sm("&7正确格式：/dt s commands add <页数> <行> <列> <执行方式(player/op/console)> <内容>，行和列不考虑GUI顶部和底部的玻璃板", player);
				return true;
			}
			if (!CMDShop.isInt(args[3], args[4], args[5])) {
				sm("&c[x]请输入有效的阿拉伯数字！", player);
				return true;
			}
			int page = Integer.parseInt(args[3]);
			int row = Integer.parseInt(args[4]);
			int column = Integer.parseInt(args[5]);
			int index = CMDShop.getNum(page, row, column);
			if (index >= cache.size()) {
				sm("&c[x]不存在这个商品", player);
				return true;
			}
			String way = args[6];
			String command = args[7];
			if (!way.equalsIgnoreCase("op") && !way.equalsIgnoreCase("player")
					&& !way.equalsIgnoreCase("console")) {
				sm("&c[x]执行方式请输入op/player/console(不区分大小写)，OP即以管理员身份执行，Player即以玩家自己的身份执行，Console即以后台身份执行",
						player);
				return true;
			}
			command = way + "|" + command.replace("_", " ");
			List<String> commands = cache.get(cache.getNumByIndex(index)).getCommands();
			if (commands == null || commands.size() == 0) {
				List<String> newCommands = new ArrayList<>();
				newCommands.add(command);
				cache.setCommands(cache.getNumByIndex(index), newCommands);
			} else {
				commands.add(command);
				cache.setCommands(cache.getNumByIndex(index), commands);
			}
			sm("&a[v]添加成功 {command}", player, "command", new String[] { command });
			return true;
		}
		if (args[2].equalsIgnoreCase("clear")) {
			if (args.length != 6) {
				sm("&7正确格式：/dt s commands clear <页数> <行> <列>，行和列不考虑GUI顶部和底部的玻璃板", player);
				return true;
			}
			if (!CMDShop.isInt(args[3], args[4], args[5])) {
				sm("&c[x]请输入有效的阿拉伯数字！", player);
				return true;
			}
			int page = Integer.parseInt(args[3]);
			int row = Integer.parseInt(args[4]);
			int column = Integer.parseInt(args[5]);
			int index = CMDShop.getNum(page, row, column);
			if (index >= cache.size()) {
				sm("&c[x]不存在这个商品", player);
				return true;
			}
			List<String> commands = cache.get(cache.getNumByIndex(index)).getCommands();
			if (commands.size() == 0) {
				sm("&c[x]该商品不存在任何指令", player);
				return true;
			}
			cache.get(cache.getNumByIndex(index)).setCommands(new ArrayList<>());
			sm("&a[v]商品指令清空完毕", player);
			return true;
		}
		sendHelp(player);
		return true;
	}
}
