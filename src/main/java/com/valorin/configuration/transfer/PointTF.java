package com.valorin.configuration.transfer;

import static com.valorin.configuration.DataFile.pd;
import static com.valorin.configuration.DataFile.pdFile;
import static com.valorin.configuration.languagefile.MessageSender.sm;

import org.bukkit.entity.Player;

import com.valorin.Main;
import com.valorin.data.MySQL;

public class PointTF {
	public static void execute(Player p) {
		if (!pdFile.exists()) {
			sm("&7数据转移失败，原因：数据文件缺失", p);
			return;
		}
		
		MySQL mysql = Main.getInstance().getMySQL();
		for (String name : pd.getKeys(false)) {
			mysql.setPoint(name, pd.getDouble(name + ".Points"));
		}
	}
}
