package com.valorin.commands.sub;

import static com.valorin.configuration.languagefile.MessageSender.sm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.commands.SubCommand;
import com.valorin.configuration.transfer.AreaTF;
import com.valorin.configuration.transfer.BlacklistTF;
import com.valorin.configuration.transfer.DanTF;
import com.valorin.configuration.transfer.LanguageFileTF;
import com.valorin.configuration.transfer.PointShopTF;
import com.valorin.configuration.transfer.PointTF;
import com.valorin.configuration.transfer.RecordTF;

public class CMDTransfer extends SubCommand {

	public CMDTransfer() {
		super("transfer", "tf");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (sender instanceof Player) {
			sm("&c[x]这条指令只能由后台执行！在服务器内无法使用！", (Player)sender);
			return true;
		}
		if (args.length != 2) {
			sm("&7正确用法：/dt transfer <Area/Blacklist/Dan/LanguageFile/Point/PointShop/Record>",
					null);
			return true;
		}
		if ((!args[1].equalsIgnoreCase("area"))
				&& (!args[1].equalsIgnoreCase("blacklist"))
				&& (!args[1].equalsIgnoreCase("dan"))
				&& (!args[1].equalsIgnoreCase("languagefile"))
				&& (!args[1].equalsIgnoreCase("point"))
				&& (!args[1].equalsIgnoreCase("pointshop"))
				&& (!args[1].equalsIgnoreCase("record"))) {
			sm("&c模块名输入错误，请输入Area/Blacklist/Dan/LanguageFile/Point/PointShop/Record",
					null);
			return true;
		}
		sm("&7开始转移数据，在此过程中可能会有卡顿现象发生....", null);
		switch (args[1].toLowerCase()) {
		case "area":
			AreaTF.execute(null);
			break;
		case "blacklist":
			BlacklistTF.execute(null);
			break;
		case "dan":
			DanTF.execute(null);
			break;
		case "languagefile":
			LanguageFileTF.execute(null);
			break;
		case "point":
			PointTF.execute(null);
			break;
		case "pointshop":
			PointShopTF.execute(null);
			break;
		case "record":
			RecordTF.execute(null);
			break;
		}
		return true;
	}
}
