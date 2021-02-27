package com.valorin.commands.sub;

import static com.valorin.configuration.languagefile.MessageSender.sm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.commands.SubCommand;
import com.valorin.commands.way.AdminCommand;
import com.valorin.specialtext.ClickableText;

public class CMDAdminHelp extends SubCommand implements AdminCommand {

	public CMDAdminHelp() {
		super("adminhelp", "ah", "admin");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}
		sm("", p);
		sm("&3&lDan&b&l&oTiao &f&l>> &a管理员帮助", p, false);
		sm("&b&l/dt arenas(a) &f- &a查看竞技场操作的相关帮助", p, false);
		sm("&b&l/dt shop(s) help &f- &a查看单挑积分&n商城&a操作的相关帮助", p, false);
		sm("&b&l/dt points(p) &f- &a查看单挑积分操作的相关帮助", p, false);
		sm("&b&l/dt blacklist(b) &f- &a查看黑名单操作的相关帮助", p, false);
		sm("&b&l/dt energy(e) &f- &a查看精力值操作的相关帮助", p, false);
		sm("&b&l/dt season(ss) &f- &a查看赛季操作的相关帮助", p, false);
		sm("&b&l/dt hd &f- &a查看单挑排行榜操作的相关帮助", p, false);
		sm("&b&l/dt exp &f- &a查看段位经验操作的相关帮助", p, false);
		sm("&b/dt stop <竞技场名称> &f- &a强制结束某个竞技场的比赛", p, false);
		sm("&b/dt lobby(l) set &f- &a设置服务器的单挑大厅传送点", p, false);
		sm("&b/dt lobby(l) delete &f- &a取消大厅传送点", p, false);
		sm("&b/dt game <竞技场名称> <玩家1> <玩家2> &f- &a强行开启一场比赛", p, false);
		sm("&b/dt sendall(sa) getitem &f- &a获取一个单挑全服邀请函道具", p, false);
		sm("&b/dt transfer(tf) <模块> &f- &a将yml文件中的数据转移到数据库中", p, false);
		ClickableText.sendDocumentInfo(sender);
		sm("", p);
		return true;
	}

}
