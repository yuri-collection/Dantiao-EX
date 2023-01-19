package com.valorin.task;

import com.valorin.Main;
import com.valorin.event.EventCheckVersion;
import com.valorin.network.Update;
import com.valorin.network.Update.UpdateState;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class VersionChecker extends BukkitRunnable {
    boolean send = false;
    CommandSender receiver;

    public void run() {
        Update update = Main.getInstance().getUpdate();
        HttpURLConnection conn = null;
        try {
            URL url = new URL(
                    "https://gitee.com/valorin/dantiao-ex/raw/master/src/main/java/com/valorin/task/version.txt");
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            InputStream inStream = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(inStream, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);
            List<String> context = new ArrayList<>();
            String str;
            int i = 0;
            int version = 0;
            while ((str = br.readLine()) != null) {
                if (i == 0)
                    version = Integer.parseInt(str);
                else
                    context.add(str);
                i++;
            }
            update.setVersion(version);
            update.setContext(context);
            update.setState(Update.UpdateState.SUCCESS);

            if (send) {
                EventCheckVersion.sendUpdateInfo(update, receiver);
            }
        } catch (SocketTimeoutException exception) {
            update.setState(UpdateState.FAILURE_TIMEOUT);
        } catch (Exception exception) {
            update.setState(UpdateState.FAILURE_OTHER);
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
