package com.valorin.mailbox;

import com.valorin.Main;
import com.valorin.dan.CustomDan;
import com.valorin.data.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.valorin.configuration.languagefile.MessageSender.gm;

public class SendPersonMail_2 {
    public static void sendMail(String playerName, String danEditName,
                          Method methodCreateBaseFileMail,
                          Method methodSetItemList,
                          Method methodSetCommandDescription,
                          Method methodSetCommandList,
                          Method methodSetRecipient,
                          Method methodSend,
                          Class classMailPlayer) {
        if (!Data.isSeasonDanEnable(danEditName)) {
            return;
        }
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(cal.getTime());
        try {

            Object fm = methodCreateBaseFileMail.invoke(null, "player", Main
                            .getInstance().getConfigManager().getServerName(), gm("赛季结束"),
                    Data.getSeasonDanMessage(danEditName), date);
            methodSetItemList.invoke(fm, Data.getSeasonDanItemStacks(danEditName));
            int points = Data.getSeasonDanPoints(danEditName);
            Player player = Bukkit.getPlayerExact(playerName);
            CustomDan dan = Main.getInstance().getDanHandler()
                    .getDanByName(danEditName);
            if (points != 0) {
                List<String> commandDescriptions = new ArrayList<>();
                commandDescriptions.add(gm("&f领取 &r{dan} &f段位的奖励： &b{point} &f积分",
                        player, "dan point",
                        new String[]{dan.getDisplayName().replace("&", "§"),
                                points + ""}));
                List<String> commands = new ArrayList<>();
                commands.add("dantiao point add " + playerName + " " + points);
                methodSetCommandDescription.invoke(fm, commandDescriptions);
                methodSetCommandList.invoke(fm, commands);
            }
            Object fmMailPlayer = classMailPlayer.cast(fm);
            methodSetRecipient.invoke(fmMailPlayer, Collections.singletonList(playerName));
            methodSend.invoke(fmMailPlayer,Bukkit.getConsoleSender(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
