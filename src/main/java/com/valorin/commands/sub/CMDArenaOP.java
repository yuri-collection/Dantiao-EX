package com.valorin.commands.sub;

import static com.valorin.Main.getInstance;
import static com.valorin.configuration.languagefile.MessageSender.gm;
import static com.valorin.configuration.languagefile.MessageSender.sm;
import static com.valorin.configuration.languagefile.MessageSender.sml;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.valorin.Main;
import com.valorin.arenas.Arena;
import com.valorin.arenas.ArenaCreator;
import com.valorin.arenas.ArenaCreatorHandler;
import com.valorin.arenas.ArenaManager;
import com.valorin.caches.ArenaInfoCache;
import com.valorin.commands.SubCommand;
import com.valorin.commands.way.AdminCommand;
import com.valorin.data.Data;
import com.valorin.itemstack.PlayerItems;
import com.valorin.util.ItemGiver;

public class CMDArenaOP extends SubCommand implements AdminCommand {

    public CMDArenaOP() {
        super("arena", "a");
    }

    public void sendHelp(Player player) {
        sm("", player);
        sm("&3&lDan&b&l&oTiao &f&l>> &a管理员帮助：竞技场操作", player, false);
        sm("&b/dt arena(a) mode &f- &a进入/退出竞技场设置模式", player, false);
        sm("&b/dt arena(a) create <竞技场编辑名> <竞技场名称(支持颜色符号)> &f- &a创造一个已设置好的竞技场",
                player, false);
        sm("&b/dt arena(a) remove <竞技场编辑名> &f- &a删除一个已创建的竞技场", player, false);
        sm("&b/dt arena(a) list &f- &a查看所有已创建的竞技场", player, false);
        sm("&b/dt arena(a) sw <竞技场编辑名> &f- &a启用观战功能并设置观战点", player, false);
        sm("&b/dt arena(a) rw <竞技场编辑名> &f- &a取消观战功能并移除观战点", player, false);
        sm("&b/dt arena(a) commands add <竞技场编辑名> <执行方式(player/op/console)> <内容> &f- &a添加一条开赛时执行的指令",
                player, false);
        sm("&b/dt arena(a) commands clear <竞技场编辑名> &f- &a清空所有已添加的指令", player, false);
        sm("&b/dt arena(a) commands list <竞技场编辑名> &f- &a查看所有已添加的指令", player, false);
        sm("&b/dt arena(a) setkit <竞技场编辑名> &f- &a为某个竞技场设置开局给予玩家的固定装备(KitPVP模式)", player, false);
        sm("&b/dt arena(a) enablekit <竞技场编辑名> &f- &a设置好固定装备后，输出该指令以将某个竞技场设置为KitPVP模式", player, false);
        sm("&b/dt arena(a) disablekit <竞技场编辑名> &f- &a取消某个竞技场的KitPVP模式", player, false);
        sm("", player);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label,
                             String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (args.length == 1) {
            sendHelp(player);
            return true;
        }
        if (args[1].equalsIgnoreCase("commands")) {
            return CMDArenaOP_Commands.onCommand(sender, args);
        }
        ArenaInfoCache cache = getInstance().getCacheHandler().getArenaInfo();
        if (args[1].equalsIgnoreCase("remove")) {
            String editName = args[2];
            if (!Data.getArenas().contains(editName)) {
                sm("&c[x]不存在的竞技场，请检查输入", player);
                return true;
            }
            getInstance().getArenaManager().removeArena(editName);
            cache.deleteArena(editName);
            sm("&a[v]竞技场删除完毕！玩家将无法再进入这个竞技场", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("list")) {
            List<Arena> arenaList = ArenaManager.arenas;
            if (arenaList.size() == 0) {
                sm("&c[x]未设置任何竞技场", player);
                return true;
            } else {
                sm("&6竞技场如下 [right]", player);
                for (Arena arena : arenaList) {
                    String editName = arena.getName();
                    String displayName = cache.get(editName).getDisplayName();
                    sender.sendMessage("§e"
                            + editName
                            + "§7("
                            + displayName
                            .replace("&", "§") + "§7)");
                }
                sm("&6共计 &f&l{amount} &6个", player, "amount", new String[]{""
                        + arenaList.size()});
            }
            return true;
        }
        if (args[1].equalsIgnoreCase("rw")) {
            if (args.length != 3) {
                sm("&7正确用法：/dt a rw <竞技场编辑名>", player);
                return true;
            }
            String editName = args[2];
            if (!Data.getArenas().contains(editName)) {
                sm("&c[x]不存在的竞技场，请检查输入", player);
                return true;
            }
            if (Data.getArenaWatchingPoint(editName) == null) {
                sm("&c[x]这个竞技场本来就没有设置观战点", player);
                return true;
            }
            cache.setWatchingPoint(editName, null);
            sm("&a[v]观战点已移除", player);
            return true;
        }
        if (player == null) {
            sm("&c[x]这条指令只能由服务器内的玩家执行！后台无法使用！", null);
            return true;
        }
        if (args[1].equalsIgnoreCase("mode")) {
            ArenaCreatorHandler acs = Main.getInstance().getACS();
            if (acs.getCreators().contains(player.getName())) {
                acs.removeAC(player.getName());
                sm("&a[v]已退出竞技场创建模式", player);
            } else {
                ItemGiver ig = new ItemGiver(player, PlayerItems.getCreator(player));
                if (!ig.getIsReceive()) {
                    return true;
                }
                acs.addAC(player.getName());
                sml("&a[v]现在进入竞技场创建模式&2竞技场快捷创建工具已发送至你的背包，请按如下步骤操作：| | &6使用&f[&e&n左键&f]&6点击空气选择&b&lA&6点| &6使用&f[&e&n右键&f]&6点击空气选择&d&lB&6点| &d使用  &f/dt a create <竞技场编辑名> <竞技场名称(支持颜色符号)> &d完成创建| |&a最后记得再次输入/dt a mode退出创建模式",
                        player);
            }
            return true;
        }
        if (args[1].equalsIgnoreCase("create")) {
            if (args.length != 4) {
                sml("&7正确用法：/dt a create <竞技场编辑名> <竞技场名称(支持颜色符号)>|&7如：/dt a create test 初级竞技场",
                        player);
                return true;
            }
            ArenaCreatorHandler acs = Main.getInstance().getACS();
            if (!acs.getCreators().contains(player.getName())) {
                sm("&c[x]请先进入竞技场创建模式！", player);
                return true;
            }
            ArenaCreator ac = acs.getAC(player.getName());
            if (ac.getPointA() == null) {
                sm("&c[x]未设置A点！", player);
                return true;
            }
            if (ac.getPointB() == null) {
                sm("&c[x]未设置B点！", player);
                return true;
            }
            if (Data.getArenas().contains(args[2])) {
                sm("&c[x]数据文件中已有竞技场&e{editname}&c了，请换一个编辑名！", player, "editname",
                        new String[]{args[2]});
                return true;
            }

            Location pointA = ac.getPointA(), pointB = ac.getPointB();

            cache.saveArena(args[2], args[3], pointA, pointB);

            getInstance().getArenaManager().addArena(args[2]);
            sm("&a[v]创建完成！现在玩家可以进入这个竞技场比赛了！现在你可以选择输入/dt a mode退出创建模式，也可以继续进行创建操作！",
                    player);
            return true;
        }
        if (args[1].equalsIgnoreCase("sw")) {
            if (args.length != 3) {
                sm("&7正确用法：/dt a sw <竞技场编辑名>", player);
                return true;
            }
            String editName = args[2];
            if (!Data.getArenas().contains(editName)) {
                sm("&c[x]不存在的竞技场，请检查输入", player);
                return true;
            }
            Location location = player.getLocation();

            String arenaWorldName = cache.get(editName).getPointA()
                    .getWorld().getName();
            if (!arenaWorldName.equals(location.getWorld().getName())) {
                sm("&c[x]观战点所处的世界应该与其对应的竞技场一致！", player);
                return true;
            }

            cache.setWatchingPoint(editName, location);
            sm("&a[v]观战点设置完毕", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("setkit")) {
            if (args.length != 3) {
                sm("&7正确用法：/dt a setkit <竞技场编辑名>", player);
                return true;
            }
            String editName = args[2];
            if (!Data.getArenas().contains(editName)) {
                sm("&c[x]不存在的竞技场，请检查输入", player);
                return true;
            }

            Inventory inv = Bukkit.createInventory(null, 36, editName + ":"
                    + gm("请将装备放进来"));
            List<ItemStack> itemStacks = Data.getArenaKit(editName);
            int i = 0;
            for (ItemStack itemStack : itemStacks) {
                inv.setItem(i, itemStack);
                i++;
            }
            player.openInventory(inv);

            sm("&a[v]已打开面板，请将装备放到里面", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("enablekit")) {
            if (args.length != 3) {
                sm("&7正确用法：/dt a enablekit <竞技场编辑名>", player);
                return true;
            }
            String editName = args[2];
            if (!Data.getArenas().contains(editName)) {
                sm("&c[x]不存在的竞技场，请检查输入", player);
                return true;
            }
            cache.setKitEnable(editName, true);
            Data.setArenaKitEnable(editName, true);
            sm("&a[v]KitPVP模式开启成功！在这个竞技场比赛的玩家会在比赛开始后临时使用你设定的Kit装备", player);
            return true;
        }
        if (args[1].equalsIgnoreCase("disablekit")) {
            if (args.length != 3) {
                sm("&7正确用法：/dt a disablekit <竞技场编辑名>", player);
                return true;
            }
            String editName = args[2];
            if (!Data.getArenas().contains(editName)) {
                sm("&c[x]不存在的竞技场，请检查输入", player);
                return true;
            }
            cache.setKitEnable(editName, false);
            Data.setArenaKitEnable(editName, false);
            sm("&a[v]KitPVP模式关闭成功！", player);
            return true;
        }
        sendHelp(player);
        return true;
    }
}
