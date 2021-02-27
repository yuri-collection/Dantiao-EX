package com.valorin.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;

import com.valorin.commands.sub.CMDAdminHelp;
import com.valorin.commands.sub.CMDArenaInfo;
import com.valorin.commands.sub.CMDArenaOP;
import com.valorin.commands.sub.CMDBlackList;
import com.valorin.commands.sub.CMDChangeLang;
import com.valorin.commands.sub.CMDDan;
import com.valorin.commands.sub.CMDEnergy;
import com.valorin.commands.sub.CMDExp;
import com.valorin.commands.sub.CMDGame;
import com.valorin.commands.sub.CMDLobby;
import com.valorin.commands.sub.CMDPlayerHelp;
import com.valorin.commands.sub.CMDPoints;
import com.valorin.commands.sub.CMDQuit;
import com.valorin.commands.sub.CMDRankingOP;
import com.valorin.commands.sub.CMDRankingPlayer;
import com.valorin.commands.sub.CMDRecords;
import com.valorin.commands.sub.CMDReload;
import com.valorin.commands.sub.CMDRequestAccept;
import com.valorin.commands.sub.CMDRequestDeny;
import com.valorin.commands.sub.CMDRequestSend;
import com.valorin.commands.sub.CMDRequestSendAll;
import com.valorin.commands.sub.CMDSeason;
import com.valorin.commands.sub.CMDShop;
import com.valorin.commands.sub.CMDStarting;
import com.valorin.commands.sub.CMDStop;
import com.valorin.commands.sub.CMDTimetable;
import com.valorin.commands.sub.CMDTransfer;
import com.valorin.commands.sub.CMDWatchGame;

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
