package com.valorin.commands.sub;

import static com.valorin.configuration.languagefile.MessageSender.sm;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.commands.SubCommand;
import com.valorin.configuration.ConfigManager;

public class CMDTimetable extends SubCommand {

	public CMDTimetable() {
		super("timetable", "tt");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		ConfigManager configManager = Main.getInstance().getConfigManager();
		List<String> ts = configManager.getSearchingTimeTable();
		List<String> ti = configManager.getInviteTimeTable();
		if (ts.size() != 0) {
			sm("&b全服匹配开放时间：", player);
			for (String s : ts) {
				sender.sendMessage(s);
			}
		} else {
			sm("&b全服匹配开放时间：全天无限制", player);
		}

		if (ti.size() != 0) {
			sm("&b邀请功能开放时间：", player);
			for (String s : ti) {
				sender.sendMessage(s);
			}
		} else {
			sm("&b邀请功能开放时间：全天无限制", player);
		}
		return true;
	}
}
