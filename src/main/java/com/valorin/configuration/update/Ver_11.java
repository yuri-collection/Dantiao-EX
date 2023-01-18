package com.valorin.configuration.update;

import com.valorin.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Ver_11 {
    public static void execute() {
        try {
            File configFile = new File(Main.getInstance().getDataFolder(),
                    "config.yml");

            List<String> texts = ConfigUpdate.readTexts(configFile);

            List<String> newTexts = new ArrayList<>();

            for (String text : texts) {
                if (text.startsWith("ConfigVersion:")) {
                    newTexts.add("ConfigVersion: 11");
                    continue;
                }
                if (text.startsWith("  Process:")) {
                    newTexts.add("      DisplayName:");
                    newTexts.add("      - '&aTest Display Name'");
                    newTexts.add(text);
                    continue;
                }
                if (text.equals("    # 设置禁止带入竞技场的物品。Material代表禁止指定材质的物品；Lore代表禁止含有指定描述文字的物品")) {
                    newTexts.add("    # 设置禁止带入竞技场的物品。Material代表禁止指定类型的物品；Lore代表禁止描述中含有指定文字的物品；DisplayName代表禁止名称中含有指定文字的物品");
                    newTexts.add("    # 你可以通过分行的方式设置多个禁止的物品类型或识别文字。对于识别文字，只要物品描述的任意一行或者物品名称含有包含这些识别文字，即会被判定为禁带物品");
                    continue;
                }
                newTexts.add(text);
            }

            ConfigUpdate.writeTexts(configFile, newTexts);

            Main.getInstance().saveResource("config.yml", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
