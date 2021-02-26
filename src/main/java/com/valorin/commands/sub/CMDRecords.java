package com.valorin.commands.sub;

import static com.valorin.configuration.languagefile.MessageSender.sm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.commands.SubCommand;
import com.valorin.commands.way.InServerCommand;
import com.valorin.inventory.INVRecords;

public class CMDRecords extends SubCommand implements InServerCommand {

	public CMDRecords() {
		super("records", "r");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player p = (Player) sender;
		String name = p.getName();
		INVRecords.openInv(name);
		sm("&a[v]记录面板已打开，查看你的每一次精彩表现！", p);
		return true;
	}
}
