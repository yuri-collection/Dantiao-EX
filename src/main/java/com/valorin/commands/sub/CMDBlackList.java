package com.valorin.commands.sub;

import static com.valorin.configuration.languagefile.MessageSender.sm;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.caches.BlacklistCache;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.AdminCommand;

public class CMDBlackList extends SubCommand implements AdminCommand {

	public CMDBlackList() {
		super("blacklist", "blist", "bl", "b");
	}

	public void sendHelp(Player p) {
		sm("", p);
		sm("&3&lDan&b&l&oTiao &f&l>> &a管理员帮助：黑名单操作", p, false);
		sm("&b/dt blacklist(b) add <玩家名> &f- &a为黑名单添加一个玩家", p, false);
		sm("&b/dt blacklist(b) remove <玩家名> &f- &a为黑名单移除一个玩家", p, false);
		sm("&b/dt blacklist(b) list &f- &a查看黑名单的所有玩家", p, false);
		sm("&b/dt blacklist(b) clear &f- &a清空黑名单", p, false);
		sm("", p);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}
		if (args.length == 1) {
			sendHelp(p);
			return true;
		}
		BlacklistCache cache = Main.getInstance().getCacheHandler()
				.getBlacklist();
		List<String> list = cache.get();
		if (args[1].equalsIgnoreCase("add")) {
			if (args.length != 3) {
				sm("&7正确格式：/dt bl add <玩家名>", p);
				return true;
			}
			String targetPlayerName = args[2];
			if (list.contains(targetPlayerName)) {
				sm("&c[x]请勿重复添加！这个玩家已在黑名单中！", p);
			} else {
				cache.add(targetPlayerName);
				sm("&a[v]已成功添加{player}", p, "player",
						new String[] { targetPlayerName });
			}
			return true;
		}
		if (args[1].equalsIgnoreCase("remove")) {
			if (args.length != 3) {
				sm("&7正确格式：/dt bl remove <玩家名>", p);
				return true;
			}
			if (list.size() == 0) {
				sm("&c[x]黑名单为空！", p);
			} else {
				String targetPlayerName = args[2];
				if (!list.contains(targetPlayerName)) {
					sm("&c[x]黑名单中不包括玩家&e{player}&c！请检查输入", p, "player",
							new String[] { targetPlayerName });
				} else {
					cache.remove(targetPlayerName);
					sm("&a[v]已成功移除{player}", p, "player",
							new String[] { targetPlayerName });
				}
			}
			return true;
		}
		if (args[1].equalsIgnoreCase("list")) {
			if (list.size() == 0) {
				sm("&c[x]黑名单为空！", p);
				return true;
			} else {
				sm("&6黑名单如下 [right]", p);
				for (String pn : list) {
					sender.sendMessage("§e" + pn);
				}
				sm("&6共计 &f&l{amount} &6个", p, "amount", new String[] { ""
						+ list.size() });
			}
			return true;
		}
		if (args[1].equalsIgnoreCase("clear")) {
			if (list.size() == 0) {
				sm("&c[x]黑名单为空！", p);
				return true;
			} else {
				cache.clear();
				sm("&a[v]已全部清空{amount}个", p, "amount",
						new String[] { "" + list.size() });
			}
			return true;
		}
		sendHelp(p);
		return true;
	}

}
