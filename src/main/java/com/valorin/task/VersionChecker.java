package com.valorin.task;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import com.valorin.Main;
import com.valorin.event.EventCheckVersion;
import com.valorin.network.Update;

public class VersionChecker extends BukkitRunnable {
    boolean send = false;
    CommandSender receiver;

    public void run() {
        Update update = Main.getInstance().getUpdate();
        String context;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(
                    "https://valorin.coding.net/p/VersionChecker/d/VersionChecker/git/raw/master/Dantiao-EX");
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            InputStream inStream = conn.getInputStream();
            final StringBuilder builder = new StringBuilder(255);
            int byteRead;
            while ((byteRead = inStream.read()) != -1) {
                builder.append((char) byteRead);
            }
            context = new String(builder.toString().getBytes(StandardCharsets.ISO_8859_1),
                    StandardCharsets.UTF_8);
            update.setState(1);
            String version = context.split("\\[change]")[0];
            String[] messageStringArray = (context.split("\\[change]")[1])
                    .split("\\[next]");
            String downloadUrl = context.split("\\[change]")[2];
            String password = context.split("\\[change]")[3];

            List<String> messageList = new ArrayList<>();
            for (String message : messageStringArray) {
                messageList.add(message.replace("&", "§"));
            }
            update.setVersion(version);
            update.setContext(messageList);
            update.setDownloadUrl(downloadUrl);
            update.setPassword(password);

            if (send) {
                EventCheckVersion.sendUpdateInfo(update, receiver);
            }
        } catch (SocketTimeoutException exception) {
            update.setState(2);
        } catch (Exception exception) {
            update.setState(3);
        }
        if (conn != null) {
            conn.disconnect();
        }
    }

    public void setSend(CommandSender receiver) {
        this.send = true;
        this.receiver = receiver;
    }
}
