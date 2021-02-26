package com.valorin.commands.sub;

import com.valorin.arenas.FinishGame;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.AdminCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class CMDStop extends SubCommand implements AdminCommand {

    public CMDStop() {
        super("stop");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }
        if (args.length != 2) {
            sm("&7正确格式：/dt stop <竞技场名称>", p);
            return true;
        }
        FinishGame.compulsoryEnd(args[1], p);
        return true;
    }

}
