package com.valorin.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.valorin.commands.sub.*;
import org.bukkit.Bukkit;

public class CommandHandler {

	private Set<SubCommand> commands = new HashSet<>();

	public CommandHandler(String name) {
		commands.add(new CMDAdminHelp());
		commands.add(new CMDArenaOP());
		commands.add(new CMDBlackList());
		commands.add(new CMDChangeLang());
		commands.add(new CMDGame());
		commands.add(new CMDLobby());
		commands.add(new CMDPlayerHelp());
		commands.add(new CMDPoints());
		commands.add(new CMDQuit());
		commands.add(new CMDRankingOP());
		commands.add(new CMDRankingPlayer());
		commands.add(new CMDReload());
		commands.add(new CMDRequestAccept());
		commands.add(new CMDRequestDeny());
		commands.add(new CMDRequestSend());
		commands.add(new CMDStop());
		commands.add(new CMDTimetable());
		commands.add(new CMDDan());
		commands.add(new CMDRecords());
		commands.add(new CMDShop());
		commands.add(new CMDEnergy());
		commands.add(new CMDStarting());
		commands.add(new CMDArenaInfo());
		commands.add(new CMDWatchGame());
		commands.add(new CMDExp());
		commands.add(new CMDRequestSendAll());
		commands.add(new CMDTransfer());
		commands.add(new CMDSeason());
		commands.add(new CMDCheckVersion());
		Bukkit.getPluginCommand(name).setExecutor(new CommandExecutor());
	}

	public SubCommand getSubCommand(String cmd) {
		for (SubCommand command : commands) {
			if (Arrays.asList(command.getNames()).contains(cmd)) {
				return command;
			}
		}
		return null;
	}

	public Set<SubCommand> getCommands() {
		return commands;
	}
}
