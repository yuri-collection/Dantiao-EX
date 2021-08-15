package com.valorin.configuration.languagefile;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageBuilder.gmLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class MessageSender {

    // 完整的
    public static void sm(String Chinese, Player player, String v, String[] vl,
                          Boolean prefix) {
        if (player == null) {
            Bukkit.getConsoleSender().sendMessage(
                    gmLog(Chinese, null, v, vl, prefix, false));
        } else {
            player.sendMessage(gmLog(Chinese, player, v, vl, prefix, false));
        }
    }

    // 常用：无变量，默认有前缀
    public static void sm(String Chinese, Player player) {
        if (player == null) {
            if (Chinese.length() == 0) {
                Bukkit.getConsoleSender().sendMessage(
                        gmLog(Chinese, null, null, null, false, false));
            } else {
                Bukkit.getConsoleSender().sendMessage(
                        gmLog(Chinese, null, null, null, true, false));
            }
        } else {
            if (Chinese.length() == 0) {
                player.sendMessage(gmLog(Chinese, player, null, null, false, false));
            } else {
                player.sendMessage(gmLog(Chinese, player, null, null, true, false));
            }
        }
    }

    // 常用：无变量
    public static void sm(String Chinese, Player player, boolean prefix) {
        if (player == null) {
            Bukkit.getConsoleSender().sendMessage(
                    gmLog(Chinese, null, null, null, false, prefix));
        } else {
            player.sendMessage(gmLog(Chinese, player, null, null, false, prefix));
        }
    }

    // 专用：无变量，默认有前缀，仅用于双向发送
    public static void sm(String Chinese, Player player1, Player player2) {
        player1.sendMessage(gmLog(Chinese, player1, null, null, true, false));
        player2.sendMessage(gmLog(Chinese, player2, null, null, true, false));
    }

    // 常用：有变量，默认有前缀，无论prefix如何都返回有前缀
    public static void sm(String Chinese, Player player, String v, String[] vl) {
        if (player == null) {
            Bukkit.getConsoleSender().sendMessage(
                    gmLog(Chinese, null, v, vl, true, false));
        } else {
            player.sendMessage(gmLog(Chinese, player, v, vl, true, false));
        }
    }

    // 不常用：发送多行消息，无变量，默认无前缀
    public static void sml(String Chinese, Player player) {
        if (gmLog(Chinese, player, null, null, false, false) == null) {
            return;
        }
        for (String str : gmLog(Chinese, player, null, null, false, false).split(
                "\\|")) {
            player.sendMessage(str);
        }
    }

    // 不常用：发送多行消息，有变量，默认无前缀
    public static void sml(String Chinese, Player player, String v, String[] vl) {
        if (gmLog(Chinese, player, null, null, false, false) == null) {
            return;
        }
        for (String str : gmLog(Chinese, player, v, vl, false, false).split("\\|")) {
            player.sendMessage(str);
        }
    }

    // 常用：无变量，默认无前缀
    public static String gm(String Chinese, Player player) {
        return gmLog(Chinese, player, null, null, false, false);
    }

    // 常用：有变量，默认无前缀
    public static String gm(String Chinese, Player player, String v, String[] vl) {
        return gmLog(Chinese, player, v, vl, false, false);
    }

    // 不常用：用于获取各个语言文件的Tag，antiClashTag项仅用于防止方法冲突，随便输，无任何其他作用
    public static String gm(String Chinese, File file, int antiClashTag) {
        LanguageFileLoader languageFileLoader = getInstance().getLanguageFileLoader();
        String finalMessage = "";
        int loc = -1;
        for (int i = 0; i < languageFileLoader.getDefaultLang().size(); i++) {
            if (languageFileLoader.getDefaultLang().get(i).equals(Chinese)) {
                loc = i;
            }
        }
        if (loc == -1) {
            return finalMessage;
        }
        List<String> fileList = languageFileLoader.getLang().get(file);
        if (fileList.size() <= loc) {
            return finalMessage;
        }
        finalMessage = fileList.get(loc);
        return finalMessage;
    }

    // 不常用：仅用于必须使用默认语言文件的文本
    public static String gm(String Chinese) {
        return gmLog(Chinese, null, null, null, false, true);
    }

    // 常用：多行消息，常用于Lores，无变量，默认无前缀
    public static List<String> gml(String Chinese, Player p) {
        if (gmLog(Chinese, p, null, null, false, false) == null) {
            return null;
        }
        return new ArrayList<>(Arrays.asList(gmLog(Chinese, p, null, null, false, false).split(
                "\\|")));
    }

    // 不常用：多行消息，常用于Lores，有变量，默认无前缀
    public static List<String> gml(String Chinese, Player p, String v,
                                   String[] vl) {
        if (gmLog(Chinese, p, v, vl, false, false) == null) {
            return null;
        }
        List<String> list = new ArrayList<>();
        Collections.addAll(list, gmLog(Chinese, p, v, vl, false, false).split("\\|"));
        return list;
    }
}
