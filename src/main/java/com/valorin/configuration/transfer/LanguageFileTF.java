package com.valorin.configuration.transfer;

import static com.valorin.configuration.DataFile.playerData;
import static com.valorin.configuration.DataFile.playerDataFile;
import static com.valorin.configuration.languagefile.MessageSender.sm;

import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.data.MySQL;

public class LanguageFileTF {
	public static void execute(Player p) {
		if (!playerDataFile.exists()) {
			sm("&7数据转移失败，原因：数据文件缺失", p);
			return;
		}
		
		MySQL mysql = Main.getInstance().getMySQL();
		for (String name : playerData.getKeys(false)) {
			mysql.setLanguageFile(name, playerData.getString(name + ".Language"));
		}
	}
}
