package com.valorin.commands.sub;

import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaManager;
import com.valorin.arenas.FinishGame;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.InServerCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.sm;

public class CMDQuit extends SubCommand implements InServerCommand {

    public CMDQuit() {
        super("quit", "q");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        Player p = (Player) sender;
        ArenaManager ah = getInstance().getArenaManager();
        String arenaname = ah.getPlayerOfArena(p.getName());
        Arena a = ah.getArena(arenaname);
        if (arenaname != null) {
            Player theother = Bukkit.getPlayerExact(a.getTheOtherPlayer(p
                    .getName()));
            sm("&b对方向你认输了！", theother);
            FinishGame.normalEnd(arenaname, getInstance().getArenaManager()
                    .getTheOtherPlayer(p.getName()), p.getName(), false);
        } else {
            sm("&c[x]你现在不在任何一场比赛中", p);
        }
        return true;
    }

}
