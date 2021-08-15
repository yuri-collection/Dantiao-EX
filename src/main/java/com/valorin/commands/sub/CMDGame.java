package com.valorin.commands.sub;

import com.valorin.arenas.StartGame;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.AdminCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class CMDGame extends SubCommand implements AdminCommand {

    public CMDGame() {
        super("game");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (args.length != 4) {
            sm("&7正确格式：/dt game <竞技场名称> <玩家1> <玩家2>", player);
            return true;
        }
        String playerName1 = args[2];
        String playerName2 = args[3];
        if (Bukkit.getPlayerExact(playerName1) == null
                || Bukkit.getPlayerExact(playerName2) == null) {
            sm("&c[x]玩家名输入有误！请检查两个玩家是否都在线！", player);
            return true;
        }
        if (playerName1.equals(playerName2)) {
            sm("&c[x]请输入两个不同的玩家名", player);
            return true;
        }
        new StartGame(Bukkit.getPlayerExact(playerName1), Bukkit.getPlayerExact(playerName2),
                args[1], player, 3);
        sm("&a[v]已强制开始", player);
        return true;
    }
}
