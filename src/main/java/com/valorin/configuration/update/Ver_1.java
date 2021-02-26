package com.valorin.configuration.update;

import java.io.File;

import com.valorin.Main;

public class Ver_1 {
	public static void execute() {
		File configFile = new File(Main.getInstance().getDataFolder(), "config.yml");
        configFile.delete();
        Main.getInstance().saveResource("config.yml", false);
	}
}
