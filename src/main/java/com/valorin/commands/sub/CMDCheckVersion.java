package com.valorin.commands.sub;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.valorin.Main;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.AdminCommand;
import com.valorin.task.VersionChecker;

public class CMDCheckVersion extends SubCommand implements AdminCommand {

    public CMDCheckVersion() {
        super("checkv", "cv", "checkversion");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        VersionChecker versionChecker = new VersionChecker();
        versionChecker.setSend(sender);
        versionChecker.runTaskAsynchronously(Main.getInstance());
        return true;
    }

}