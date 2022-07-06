package com.valorin.event.ranking;

import com.valorin.Main;
import com.valorin.data.encapsulation.RankingSkull;
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

public class EventClickRankingSkull implements Listener {
    @EventHandler
    public void onClickSkull(PlayerInteractEvent e) {
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
        if (!ViaVersion.isPlayerSkull(block.getType())) {
            return;
        }
        List<RankingSkull> rankingSkullList = Main.getInstance().getCacheHandler().getArea().getRankingSkullList();
        Location location = block.getLocation();
        for (RankingSkull rankingSkull : rankingSkullList) {
            if (rankingSkull.getLocation() != null) {
                if (rankingSkull.getLocation().equals(location)) {
                    e.setCancelled(true);
                    Player player = e.getPlayer();
                    if (player.isOp()) {
                        sm("&a这个排行头颅的编辑名是 {editname}，若要拆除，请先输入/dt rsk remove {editname}", player, "editname", new String[]{rankingSkull.getEditName()});
                    }
                    break;
                }
            }
        }
    }
}
