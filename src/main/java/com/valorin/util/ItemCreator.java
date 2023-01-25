package com.valorin.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemCreator {
    private final ItemStack itemStack;

    /*
     * material必填 displayName,lore,mark选填 但要同时存在 用于背包物品
     */
    public ItemCreator(Material material, int amount, short s, String displayName,
                       List<String> lore, String mark, Enchantment enchantment, boolean hideFlag) {
        ItemStack itemStack = new ItemStack(material, amount, s);
        ItemMeta im = itemStack.getItemMeta();
        if (displayName != null) {
            im.setDisplayName(displayName);
            itemStack.setItemMeta(im);
        }
        if (lore != null) {
            List<String> list = new ArrayList<>();
            if (mark != null) {
                list.add(mark);
            }
            list.addAll(lore);
            im.setLore(list);
            itemStack.setItemMeta(im);
        }
        if (enchantment != null) {
            im.addEnchant(enchantment, 1, true);
            itemStack.setItemMeta(im);
        }
        if (hideFlag) {
            if (ViaVersion.isHasItemFlagMethod()) {
                im.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
                im.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ATTRIBUTES);
            }
            itemStack.setItemMeta(im);
        }
        this.itemStack = itemStack;
    }


    public ItemStack get() {
        return itemStack;
    }
}
