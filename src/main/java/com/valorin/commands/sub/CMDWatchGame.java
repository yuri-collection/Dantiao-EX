package com.valorin.commands.sub;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.sm;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import com.valorin.caches.ArenaInfoCache;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.InServerCommand;
import com.valorin.data.Data;
import com.valorin.teleport.ToWatchingPoint;

public class CMDWatchGame extends SubCommand implements InServerCommand {

	public CMDWatchGame() {
		super("watch", "w");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		Player p = (Player) sender;
		if (args.length != 2) {
			sm("&7正确用法：/dt watch <竞技场名称>", p);
			return true;
		}
		if (getInstance().getArenaManager().isPlayerBusy(p.getName())) {// OP比赛时输入
			return true;
		}
		String arenaName = args[1];
		if (!Data.getArenas().contains(arenaName)) {
			sm("&c[x]不存在的竞技场，请检查输入", p);
			return true;
		}
		ArenaManager ah = Main.getInstance().getArenaManager();
		Arena arena = ah.getArena(arenaName);
		ArenaInfoCache cache = Main.getInstance().getCacheHandler().getArenaInfo();
		String editName = args[1];
		if (arena.getEnable()) {
			if (cache.get(editName).getWatchingPoint() != null) {
				arena.addWatcher(p.getName());
				ToWatchingPoint.to(p, arenaName);
				sm("&b现在正在观战竞技场&e{arena}", p, "arena",
						new String[] { arenaName });
			} else {
				sm("&c[x]这个竞技场不允许观战！", p);
			}
		} else {
			sm("&c[x]这个竞技场还未开始比赛，无法观战！", p);
		}
		return true;
	}
}
