package com.valorin.event.ranking;

import com.valorin.Main;
import com.valorin.data.encapsulation.RankingSign;
import com.valorin.util.ViaVersion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

import static com.valorin.configuration.languagefile.MessageSender.sm;

public class EventClickRankingSign implements Listener {
    @EventHandler
    public void onClickSign(PlayerInteractEvent e) {
        if (Main.getInstance().getServerVersionType() >= 2) {
            //对于1.9及以上版本，要忽略副手的点击
            if (e.getHand() != null) {
                if (!e.getHand().equals(org.bukkit.inventory.EquipmentSlot.HAND)) {
                    return;
                }
            }
        }
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }
        Material blockType = block.getType();
        if (!ViaVersion.isSignPostMaterial(blockType) && !ViaVersion.isWallSignMaterial(blockType)) {
            return;
        }
        List<RankingSign> rankingSignList = Main.getInstance().getCacheHandler().getArea().getRankingSignList();
        Location location = block.getLocation();
        for (RankingSign rankingSign : rankingSignList) {
            if (rankingSign.getLocation() != null) {
                if (rankingSign.getLocation().equals(location)) {
                    e.setCancelled(true);
                    Player player = e.getPlayer();
                    if (player.isOp()) {
                        sm("&a这个排行木牌的编辑名是 {editname}，若要拆除，请先输入/dt rsi remove {editname}", player, "editname", new String[]{rankingSign.getEditName()});
                    }
                    break;
                }
            }
        }
    }
}
