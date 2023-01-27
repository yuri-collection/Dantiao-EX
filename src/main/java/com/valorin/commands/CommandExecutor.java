package com.valorin.commands;

import static com.valorin.configuration.languagefile.MessageSender.sm;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.commands.sub.CMDMainHelp;
import com.valorin.commands.way.AdminCommand;
import com.valorin.commands.way.InServerCommand;

public class CommandExecutor implements TabExecutor {
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		if (args.length == 0) {
			return new CMDMainHelp().onCommand(sender);
		} else {
			SubCommand subCommand = Main.getInstance().getCommandHandler()
					.getSubCommand(args[0]);
			if (subCommand == null) {
				String similarLabel = SimilarityComparer
						.getMostSimilarSubCommand(args[0]);
				if (similarLabel == null) {
					sm("&c不存在此则子指令，请检查输入", player);
				} else {
					sm("&c不存在此则子指令，你是想输入 &e/dt {subcommand} &c吗？", player,
							"subcommand", new String[] { similarLabel });
				}
				return true;
			}
			if (subCommand instanceof AdminCommand) {
				if (!sender.hasPermission("dt.admin")) {
					sm("&c[x]无权限！", player);
					return true;
				}
				return subCommand.onCommand(sender, command, label, args);
			} else if (subCommand instanceof InServerCommand) {
				if (!(sender instanceof Player)) {
					sm("&c[x]这条指令只能由服务器内的玩家执行！后台无法使用！", player);
					return true;
				}
				return subCommand.onCommand(sender, command, label, args);
			} else {
				return subCommand.onCommand(sender, command, label, args);
			}
		}
	}

	private final String[] SUBCOMMANDS_ARENAOP = { "mode", "create", "remove",
			"list", "sw", "rw", "commands" };
	private final String[] SUBCOMMANDS_ARENAOP_COMMANDS = { "add", "clear",
			"list" };
	private final String[] SUBCOMMANDS_BLACKLIST = { "add", "remove", "list",
			"clear" };
	private final String[] SUBCOMMANDS_SHOP = { "add", "remove", "des", "rdes",
			"bc", "rbc" };
	private final String[] SUBCOMMANDS_EXP = { "add", "take", "set" };
	private final String[] SUBCOMMANDS_POINTS = { "add", "set", "view" };
	private final String[] SUBCOMMANDS_ENERGY = { "add", "set" };
	private final String[] SUBCOMMANDS_HD = { "win", "winremove", "kd",
			"kdremove", "refresh" };

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
			String alias, String[] args) {
		if (args.length == 1) {
			return Arrays.stream(SimilarityComparer.SUBCOMMANDS)
					.filter(s -> s.startsWith(args[0]))
					.collect(Collectors.toList());
		} else {
			if (args[0].equalsIgnoreCase("send")) {
				if (!(sender instanceof Player)) {
					return new ArrayList<>();
				}
				List<String> playerList = new ArrayList<>();
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (!player.getName().equals(sender.getName())) {
						playerList.add(player.getName());
					}
				}
				return Arrays
						.stream(playerList.toArray(new String[0]))
						.filter(s -> s.startsWith(args[1]))
						.collect(Collectors.toList());
			}

			if (args[0].equalsIgnoreCase("changelang") || args[0].equalsIgnoreCase("cl")) {
				if (!(sender instanceof Player)) {
					return new ArrayList<>();
				}
				List<File> fileList = Main.getInstance()
						.getLanguageFileLoader().getLanguagesList();
				List<String> fileNameList = new ArrayList<>();
				for (File file : fileList) {
					fileNameList.add(file.getName().replace(".txt", ""));
				}
				return fileNameList;
			}
			if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("watch") || args[0].equalsIgnoreCase("w")) {
				if (!(sender instanceof Player)) {
					return new ArrayList<>();
				}
				List<Arena> arenaList = ArenaManager.arenas;
				List<String> arenaNameList = new ArrayList<>();
				for (Arena arena : arenaList) {
					arenaNameList.add(arena.getName());
				}
				return arenaNameList;
			}

			if (!sender.hasPermission("dt.admin")) {
				return new ArrayList<>();
			}
			if (args[0].equalsIgnoreCase("a")
					|| args[0].equalsIgnoreCase("arena")) {
				if (args.length == 3) {
					if (args[1].equalsIgnoreCase("commands")) {
						return Arrays.stream(SUBCOMMANDS_ARENAOP_COMMANDS)
								.filter(s -> s.startsWith(args[2]))
								.collect(Collectors.toList());
					} else {
						return new ArrayList<>();
					}
				}
				return Arrays.stream(SUBCOMMANDS_ARENAOP)
						.filter(s -> s.startsWith(args[1]))
						.collect(Collectors.toList());
			}
			if (args[0].equalsIgnoreCase("b")
					|| args[0].equalsIgnoreCase("blacklist")) {
				return Arrays.stream(SUBCOMMANDS_BLACKLIST)
						.filter(s -> s.startsWith(args[1]))
						.collect(Collectors.toList());
			}
			if (args[0].equalsIgnoreCase("s")
					|| args[0].equalsIgnoreCase("shop")) {
				return Arrays.stream(SUBCOMMANDS_SHOP)
						.filter(s -> s.startsWith(args[1]))
						.collect(Collectors.toList());
			}
			if (args[0].equalsIgnoreCase("p")
					|| args[0].equalsIgnoreCase("points")) {
				return Arrays.stream(SUBCOMMANDS_POINTS)
						.filter(s -> s.startsWith(args[1]))
						.collect(Collectors.toList());
			}
			if (args[0].equalsIgnoreCase("exp")) {
				return Arrays.stream(SUBCOMMANDS_EXP)
						.filter(s -> s.startsWith(args[1]))
						.collect(Collectors.toList());
			}
			if (args[0].equalsIgnoreCase("e")
					|| args[0].equalsIgnoreCase("energy")) {
				return Arrays.stream(SUBCOMMANDS_ENERGY)
						.filter(s -> s.startsWith(args[1]))
						.collect(Collectors.toList());
			}
			if (args[0].equalsIgnoreCase("hd")) {
				return Arrays.stream(SUBCOMMANDS_HD)
						.filter(s -> s.startsWith(args[1]))
						.collect(Collectors.toList());
			}
			return new ArrayList<>();
		}
	}
}
