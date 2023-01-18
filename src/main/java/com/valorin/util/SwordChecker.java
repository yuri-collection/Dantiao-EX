package com.valorin.util;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SwordChecker {
    public static boolean isHoldingSword(Player player) {
        ItemStack itemInHand = ViaVersion.getItemInMainHand(player);
        if (itemInHand == null) {
            return false;
        }
        Material type = itemInHand.getType();
        if (type.equals(Material.AIR)) {
            return false;
        }
        return type.equals(ViaVersion.getWoodenSwordMaterial())
                || type.equals(Material.STONE_SWORD)
                || type.equals(ViaVersion.getGoldenSwordMaterial())
                || type.equals(Material.IRON_SWORD)
                || type.equals(Material.DIAMOND_SWORD)
                || type.equals(Material.getMaterial("NETHERITE_SWORD"));
    }
}
