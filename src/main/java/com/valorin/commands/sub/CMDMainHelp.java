package com.valorin.commands.sub;

import static com.valorin.configuration.languagefile.MessageSender.sm;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.specialtext.ClickableText;

public class CMDMainHelp {
	public boolean onCommand(CommandSender sender) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		sm("", player);
		ClickableText.sendHeadInfo(sender);
		sender.sendMessage("§e=============================================");
		sm("", player);
		sm("  &f&l>> &6/dt help(h) &f查看玩家帮助&a[v]", player, false);
		sm("  &f&l>> &6/dt adminhelp(ah) &f查看管理员帮助&a[v]", player, false);
		sm("", player);
		sender.sendMessage("§e=============================================");
		ClickableText.sendWebsiteInfo(sender);
		sm("", player);
		return true;
	}
}
