package com.valorin.configuration.transfer;

import static com.valorin.configuration.DataFile.shop;
import static com.valorin.configuration.DataFile.shopFile;
import static com.valorin.configuration.languagefile.MessageSender.sm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.valorin.Main;
import com.valorin.data.MySQL;

public class PointShopTF {
	public static void execute(Player p) {
		if (!shopFile.exists()) {
			sm("&7数据转移失败，原因：数据文件缺失", p);
			return;
		}
		
		MySQL mysql = Main.getInstance().getMySQL();
		Bukkit.getScheduler()
				.runTaskAsynchronously(
						Main.getInstance(),
						() -> {
							for (String key : shop.getKeys(false)) {
								if (!key.equals("Num")) {
									int num = Integer.parseInt(key.replace("n",
											""));
									ItemStack item = shop.getItemStack(key
											+ ".Item");
									double price = shop.getDouble(key
											+ ".Price");
									mysql.addGood(num, item, price);

									String broadcast = shop.getString(
											key + ".Broadcast").replace("&",
											"§");
									String description = shop.getString(
											key + ".Description").replace("&",
											"§");
									int salesVolumn = shop.getInt(key
											+ ".SalesVolume");
									mysql.setBroadcastForGood(num, broadcast);
									mysql.setDescriptionForGood(num, description);
									setSalesVolumnForGood(num, salesVolumn);
								}
							}
						});
	}

	public static void setSalesVolumnForGood(int num, int salesVolumn) {
		try {
			Connection connection = Main.getInstance().getMySQL()
					.getConnection();
			PreparedStatement ps = connection
					.prepareStatement("select * from dantiao_pointshop where name = ? limit 1;");
			ps.setString(1, "n" + num);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				PreparedStatement ps2 = connection
						.prepareStatement("update dantiao_pointshop set salesvolume = ? where name = ?;");
				ps2.setInt(1, salesVolumn);
				ps2.setString(2, "n" + num);
				ps2.executeUpdate();
				ps2.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
