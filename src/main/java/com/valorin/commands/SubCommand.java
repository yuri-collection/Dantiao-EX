package com.valorin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    private String[] names;
    
    public SubCommand(String...names) {
        this.names = names;
    }

    public abstract boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);

    public String[] getNames() {
        return names;
    }
}
