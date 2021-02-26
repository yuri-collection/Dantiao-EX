package com.valorin.network;

import com.google.common.base.Charsets;
import com.valorin.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author hs
 * @Description: {签名验证}
 * @date 2020/7/17 15:03
 */
public class Sign {
    private final static String HTTP_DOMAIN = "http://mcadmin.ljxmc.top";
    private final static String VERIFY_SIGN = "/api/public/cdk/verifySign";
    /**
     * 平台上注册的插件名称
     */
    private final static String PLUGIN_NAME = "Dantiao";
    /**
     * 平台上获取的插件对应秘钥
     */
    private final static String SECRET_KEY = "1330149306879053824";

    public static void theOriginalCheck(Action action) {
        String keyContext = null;
        if (!Main.getInstance().getDataFolder().exists()) {
            Bukkit.getConsoleSender().sendMessage(
                    "§e[Dantiao-EX] §7正在尝试从jar中获取激活码...");
            FileConfiguration config = new YamlConfiguration();
            try {
                config.load(new BufferedReader(new InputStreamReader(Main
                        .getInstance().getResource("config.yml"),
                        Charsets.UTF_8)));
            } catch (Exception e) {
                try {
                    config.load(new BufferedReader(new InputStreamReader(Main
                            .getInstance().getResource("config.yml"))));
                } catch (Exception e2) {
                    e.printStackTrace();
                }
            }
            keyContext = config.getString("Key");
        } else {
            Bukkit.getConsoleSender().sendMessage(
                    "§e[Dantiao-EX] §7正在尝试从数据文件夹中获取激活码...");
            FileConfiguration config = new YamlConfiguration();
            try {
                config.load(new File("plugins/Dantiao/config.yml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            keyContext = config.getString("Key");
        }
        String key = keyContext;
        if (key != null) {
            // 获取服务器端口
            int port = Main.getInstance().getServer().getPort();
            // 进行校验
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        String urlStr = HTTP_DOMAIN + VERIFY_SIGN + "?sign="
                                + key + "&port=" + port + "&pluginName="
                                + PLUGIN_NAME + "&secretKey=" + SECRET_KEY;
                        URL url = new URL(urlStr);
                        HttpURLConnection connection = (HttpURLConnection) url
                                .openConnection();
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                        connection.getInputStream(),
                                        StandardCharsets.UTF_8));
                        StringBuilder stringBuilder = new StringBuilder();
                        String current;
                        while ((current = in.readLine()) != null) {
                            stringBuilder.append(current);
                        }
                        String verifySign = stringBuilder.toString();
                        if ("true".equals(verifySign)) {
                            Bukkit.getConsoleSender().sendMessage(
                                    "§e[Dantiao-EX] §aCDK验证成功");
                            Main.signAccess = true;
                            action.run();
                        } else {
                            Bukkit.getConsoleSender()
                                    .sendMessage(
                                            "§e[Dantiao-EX] §cCDK验证失败，请检查配置文件中的CDK是否填写正确，然后重启服务器");
                            Main.getInstance().getPluginLoader()
                                    .disablePlugin(Main.getInstance());
                        }
                        this.cancel();
                    } catch (IOException e) {
                        Bukkit.getConsoleSender().sendMessage(
                                "§e[Dantiao-EX] §f网络出现问题，请重启服务器以重新验证");
                    } catch (IllegalPluginAccessException e) {
                    }
                }
            }.runTaskAsynchronously(Main.getInstance());
        } else {
            Bukkit.getConsoleSender().sendMessage(
                    "§e[Dantiao-EX] §cCDK验证失败，您可能未填写激活码，请按文档要求正确地填写激活码，然后重启服务器");
            Main.getInstance().getPluginLoader()
                    .disablePlugin(Main.getInstance());
        }
    }

    /**
     * 正版签名验证
     */

    public static interface Action {
        public void run();
    }

}