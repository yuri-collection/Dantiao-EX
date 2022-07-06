package com.valorin.commands.sub;

import com.valorin.Main;
import com.valorin.caches.ShopCache;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.InServerCommand;
import com.valorin.inventory.INVShop;
import com.valorin.util.ViaVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.sm;

public class CMDShop extends SubCommand implements InServerCommand {

    public CMDShop() {
        super("shop", "s");
    }

    public static int getNum(int page, int row, int column) {
        return (page - 1) * 36 + (row - 1) * 9 + (column - 1);
    }

    public static boolean isOutOfRange(int page, int row, int column,
                                       String pageArg, String rowArg, String columnArg, Player p) {
        boolean result = false;
        page = Integer.parseInt(pageArg);
        if (page > INVShop.getMaxPage() || page == 0) {
            sm("&c[x]页码超出值域，请检查是否存在此页", p);
            result = true;
        }
        row = Integer.parseInt(rowArg);
        if (row > 4 || row == 0) {
            sm("&c[x]行数超出值域，请输入1~4", p);
            result = true;
        }
        column = Integer.parseInt(columnArg);
        if (column > 9 || column == 0) {
            sm("&c[x]列数超出值域，请输入1~9", p);
            result = true;
        }
        return result;
    }

    public static boolean isInt(String... nl) {
        boolean result = true;
        for (String n : nl) {
            Pattern pattern = Pattern.compile("[0-9]+");
            Matcher matcher = pattern.matcher(n);
            if (!matcher.matches()) {
                result = false;
            }
        }
        return result;
    }

    public void sendHelp(Player p) {
        sm("", p);
        sm("&3&lDan&b&l&oTiao &f&l>> &a管理员帮助：商城操作", p, false);
        sm("&b/dt shop(s) add <价格> &f- &a上架手中的物品作为商品", p, false);
        sm("&b/dt shop(s) remove <页数> <行> <列> &f- &a下架某个商品", p, false);
        sm("&b/dt shop(s) des <页数> <行> <列> <内容>&f- &a为已有商品添加备注，支持颜色代码", p,
                false);
        sm("&b/dt shop(s) rdes <页数> <行> <列> &f- &a删除商品备注", p, false);
        sm("&b/dt shop(s) bc <页数> <行> <列> <内容> &f- &a设置玩家购买成功后发送的全服公告，支持颜色代码，以{player}代表玩家名",
                p, false);
        sm("&b/dt shop(s) rbc <页数> <行> <列> &f- &a删除购买成功后发送的全服公告", p, false);
        sm("&b/dt shop(s) dan <页数> <行> <列> <段位的编辑名> &f- &a为已有商品设置购买的段位限制", p,
                false);
        sm("&b/dt shop(s) rdan <页数> <行> <列> &f- &a取消段位限制", p,
                false);
        sm("&b/dt shop(s) commands add <页数> <行> <列> <执行方式(player/op/console)> <内容> &f- &a添加一条购买后执行的指令",
                p);
        sm("&b/dt shop(s) commands clear <页数> <行> <列> &f- &a清空所有已添加的指令", p);
        sm("&b/dt shop(s) commands list <页数> <行> <列> &f- &a查看所有已添加的指令", p);
        sm("", p);
    }

    public Integer[] getLocation(int num) {
        int page;
        int row;
        int column;
        if (num % 36 != 0) {
            page = num / 36 + 1;
        } else {
            page = num / 36;
        }
        if ((num - (page - 1)) % 9 != 0) {
            row = (num - (page - 1)) / 9 + 1;
            column = (num - page * 36 - row * 9) + 1;
        } else {
            row = (num - (page - 1)) / 9;
            column = 9;
        }
        return new Integer[]{page, row, column};
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (args.length == 1) {
            Inventory inv = Bukkit.createInventory(null, 54,
                    gm("&0&l积分商城 &9&l[right]", player));
            INVShop.loadInv(player.getName(), inv);
            sm("&a[v]欢迎光临单挑积分商城！", player);
            return true;
        }
        if (!player.hasPermission("dt.admin")) {
            sm("&c[x]无权限！", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("help")) {
            sendHelp(player);
            return true;
        }
        if (args[1].equalsIgnoreCase("commands")) {
            return CMDShop_Commands.onCommand(sender, args);
        }
        ShopCache cache = Main.getInstance().getCacheHandler().getShop();
        if (args[1].equalsIgnoreCase("add")) {
            if (args.length != 3) {
                sm("&7正确用法：/dt s add <价格>，注意请将要上架的物品拿在手上", player);
                return true;
            }
            ItemStack now = ViaVersion.getItemInMainHand(player);
            if (now.equals(new ItemStack(Material.AIR))) {
                sm("&c[x]请将要上架的物品拿在手中！", player);
                return true;
            }
            if (!isInt(args[2])) {
                sm("&c[x]请输入有效的阿拉伯数字！", player);
                return true;
            }
            int price = Integer.parseInt(args[2]);
            if (price <= 0) {
                sm("&c[x]请输入大于零的阿拉伯数字！", player);
                return true;
            }
            cache.add(ViaVersion.getItemInMainHand(player), price);
            sm("&a[v]商品上架成功！", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("remove")) {
            if (args.length != 5) {
                sm("&7正确用法：/dt s remove <页数> <行> <列>，行和列不考虑GUI顶部和底部的玻璃板", player);
                return true;
            }
            if (!isInt(args[2], args[3], args[4])) {
                sm("&c[x]请输入有效的阿拉伯数字！", player);
                return true;
            }
            int page = Integer.parseInt(args[2]);
            int row = Integer.parseInt(args[3]);
            int column = Integer.parseInt(args[4]);
            int index = getNum(page, row, column);
            if (index >= cache.size()) {
                sm("&c[x]不存在这个商品", player);
                return true;
            }
            cache.remove(cache.getNumByIndex(index));
            sm("&a[v]商品下架完毕！", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("des") || args[1].equalsIgnoreCase("bc") || args[1].equalsIgnoreCase("dan")) {
            if (args.length != 6) {
                if (args[1].equalsIgnoreCase("des")) {
                    sm("&7正确用法：/dt s des <页数> <行> <列> <内容>，行和列不考虑GUI顶部和底部的玻璃板，支持颜色代码",
                            player);
                }
                if (args[1].equalsIgnoreCase("bc")) {
                    sm("&7正确用法：/dt s bc <页数> <行> <列> <内容>，行和列不考虑GUI顶部和底部的玻璃板，支持颜色代码，可以用{player}代替玩家名称",
                            player);
                }
                if (args[1].equalsIgnoreCase("dan")) {
                    sm("&7正确用法：/dt s dan <页数> <行> <列> <段位的编辑名>，行和列不考虑GUI顶部和底部的玻璃板",
                            player);
                }
                return true;
            }
            if (!isInt(args[2], args[3], args[4])) {
                sm("&c[x]请输入有效的阿拉伯数字！", player);
                return true;
            }
            int page = Integer.parseInt(args[2]);
            int row = Integer.parseInt(args[3]);
            int column = Integer.parseInt(args[4]);
            if (isOutOfRange(page, row, column, args[2], args[3], args[4], player)) {
                return true;
            }

            int index = getNum(page, row, column);
            if (index >= cache.size()) {
                sm("&c[x]不存在这个商品", player);
                return true;
            }
            if (args[1].equalsIgnoreCase("des")) {
                String description = args[5];
                cache.setDescription(cache.getNumByIndex(index), description);
                sm("&a[v]备注设置完毕！", player);
            }
            if (args[1].equalsIgnoreCase("bc")) {
                String broadcast = args[5];
                cache.setBroadcast(cache.getNumByIndex(index), broadcast);
                sm("&a[v]公告设置完毕！", player);
            }
            if (args[1].equalsIgnoreCase("dan")) {
                String dan = args[5];
                cache.setDan(cache.getNumByIndex(index), dan);
                sm("&a[v]段位限制设置完毕！", player);
            }
            return true;
        }
        if (args[1].equalsIgnoreCase("rdes") || args[1].equalsIgnoreCase("rbc") || args[1].equalsIgnoreCase("rdan")) {
            if (args.length != 5) {
                if (args[1].equalsIgnoreCase("rdes")) {
                    sm("&7正确用法：/dt s rdes <页数> <行> <列>，行和列不考虑GUI顶部和底部的玻璃板", player);
                }
                if (args[1].equalsIgnoreCase("rbc")) {
                    sm("&7正确用法：/dt s rbc <页数> <行> <列>，行和列不考虑GUI顶部和底部的玻璃板", player);
                }
                if (args[1].equalsIgnoreCase("rdan")) {
                    sm("&7正确用法：/dt s rdan <页数> <行> <列>，行和列不考虑GUI顶部和底部的玻璃板", player);
                }
                return true;
            }
            if (!isInt(args[2], args[3], args[4])) {
                sm("&c[x]请输入有效的阿拉伯数字！", player);
                return true;
            }
            int page = Integer.parseInt(args[2]);
            int row = Integer.parseInt(args[3]);
            int column = Integer.parseInt(args[4]);
            if (isOutOfRange(page, row, column, args[2], args[3], args[4], player)) {
                return true;
            }

            int index = getNum(page, row, column);
            if (index >= cache.size()) {
                sm("&c[x]不存在这个商品", player);
                return true;
            }
            if (args[1].equalsIgnoreCase("rdes")) {
                cache.setDescription(cache.getNumByIndex(index), null);
                sm("&a[v]备注删除完毕！", player);
            }
            if (args[1].equalsIgnoreCase("rbc")) {
                cache.setBroadcast(cache.getNumByIndex(index), null);
                sm("&a[v]公告删除完毕！", player);
            }
            if (args[1].equalsIgnoreCase("rdan")) {
                cache.setDan(cache.getNumByIndex(index), null);
                sm("&a[v]段位限制取消完毕！", player);
            }
            return true;
        }
        sendHelp(player);
        return true;
    }
}
