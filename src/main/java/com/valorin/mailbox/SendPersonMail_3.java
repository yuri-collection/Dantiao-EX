package com.valorin.mailbox;

import com.tripleying.dogend.mailbox.api.data.Data;
import com.tripleying.dogend.mailbox.api.data.DataType;
import com.tripleying.dogend.mailbox.api.mail.CustomData;
import com.tripleying.dogend.mailbox.api.mail.PersonMail;
import com.tripleying.dogend.mailbox.api.mail.attach.AttachCommand;
import com.tripleying.dogend.mailbox.api.mail.attach.AttachFile;
import com.tripleying.dogend.mailbox.manager.DataManager;
import com.tripleying.dogend.mailbox.manager.MailManager;
import com.tripleying.dogend.mailbox.util.TimeUtil;
import com.valorin.Main;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import static com.valorin.configuration.languagefile.MessageSender.gm;

public class SendPersonMail_3 {

    /**
     * 为在线玩家发送邮件
     *
     * @param player Player
     */
    public static void sendOnlinePlayerMail(Player player, String danEditName) {
        if (!com.valorin.data.Data.isSeasonDanEnable(danEditName)) {
            return;
        }
        String playerName = player.getName();
        PersonMail pm = new PersonMail(initMail(playerName, danEditName));
        pm.setReceiver(player);
        int id = pm.hashCode();
        while (DataManager.getDataManager().getPersonMail(player, id, "个人邮件") != null) {
            id = new Random().nextInt();
        }
        pm.setId(id);
        MailManager.getMailManager().sendPersonMail(pm, player);
    }

    public static void sendOfflinePersonMail(OfflinePlayer offlinePlayer, String danEditName) {
        if (!com.valorin.data.Data.isSeasonDanEnable(danEditName)) {
            return;
        }
        String uuid = offlinePlayer.getUniqueId().toString();
        String playerName = offlinePlayer.getName();
        YamlConfiguration yml = initMail(playerName, danEditName);
        yml.set("uuid", uuid);
        PersonMailData pmd = (PersonMailData) new PersonMailData().loadFromYamlConfiguration(yml);
        int id;
        LinkedHashMap<String, Object> selid = new LinkedHashMap<>();
        selid.put("uuid", uuid);
        selid.put("type", "个人邮件");
        selid.put("id", 0);
        do {
            id = new Random().nextInt();
            selid.replace("id", id);
        } while (!pmd.selectCustomData(selid).isEmpty());
        pmd.id = id;
        pmd.insertCustomData();
    }

    public static YamlConfiguration initMail(String playerName, String danEditName) {
        String messageLog = com.valorin.data.Data.getSeasonDanMessage(danEditName);
        List<String> body = new ArrayList<>();
        for  (String message : messageLog.split("\\|")) {
            body.add(message.replace("&","§").replace("{player}",playerName));
        }

        AttachFile attach = new AttachFile();
        List<ItemStack> itemStacks = com.valorin.data.Data.getSeasonDanItemStacks(danEditName);
        for (ItemStack itemStack : itemStacks) {
            attach.addItemStack(itemStack);
        }

        int points = com.valorin.data.Data.getSeasonDanPoints(danEditName);
        AttachCommand cmd = attach.getCommands();
        cmd.addConsoleCommand("dt point add " + playerName + " " + points);

        YamlConfiguration yml = new YamlConfiguration();
        yml.set("id", 0);
        yml.set("type", "个人邮件");
        yml.set("title", gm("赛季结束"));
        yml.set("body", body);
        yml.set("sender", Main
                .getInstance().getConfigManager().getServerName());
        yml.set("sendtime", TimeUtil.currentTimeString());
        yml.set("attach", attach);
        yml.set("received", false);
        yml.set("uuid", "");
        return yml;
    }

}

class PersonMailData extends CustomData {

    @Data(type = DataType.Integer)
    protected int id;
    @Data(type = DataType.String)
    protected String type;
    @Data(type = DataType.String)
    protected String title;
    @Data(type = DataType.YamlString)
    protected List<String> body;
    @Data(type = DataType.String)
    protected String sender;
    @Data(type = DataType.String)
    protected String sendtime;
    @Data(type = DataType.YamlString)
    protected AttachFile attach;
    @Data(type = DataType.Boolean)
    protected boolean received;
    @Data(type = DataType.String)
    protected String uuid;

    public PersonMailData() {
        super("mailbox_person_mail");
    }

    @Override
    public CustomData loadFromYamlConfiguration(YamlConfiguration yml) {
        this.id = yml.getInt("id");
        this.title = yml.getString("title");
        this.type = yml.getString("type");
        this.body = yml.getStringList("body");
        this.sender = yml.getString("sender");
        this.sendtime = yml.getString("sendtime");
        this.attach = (AttachFile) yml.get("attach");
        this.received = yml.getBoolean("received");
        this.uuid = yml.getString("uuid");
        return this;
    }

}