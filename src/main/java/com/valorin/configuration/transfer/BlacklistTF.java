package com.valorin.configuration.transfer;

import static com.valorin.configuration.DataFile.blacklist;
import static com.valorin.configuration.DataFile.blacklistFile;
import static com.valorin.configuration.languagefile.MessageSender.sm;

import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.data.MySQL;

public class BlacklistTF {
    public static void execute(Player p) {
    	if (!blacklistFile.exists()) {
			sm("&7数据转移失败，原因：数据文件缺失", p);
			return;
		}
    	MySQL mysql = Main.getInstance().getMySQL();
    	mysql.setBlacklist(blacklist.getStringList("BlackList"));
    }
}
