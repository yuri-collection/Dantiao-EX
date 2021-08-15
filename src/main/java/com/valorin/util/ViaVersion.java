package com.valorin.util;

import com.valorin.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ViaVersion {
    private static Class<?> iChatBaseComponent;
    private static Class<?> chatComponentText;
    private static Class<?> packet;
    private static Class<?> packetPlayOutTitle;
    private static Class<?> enumTitleAction;

    public static ItemStack getGlassPane(int type) {
        Material material;
        short realType = (short) type;
        if (Material.getMaterial("STAINED_GLASS_PANE") != null) {
            material = Material.getMaterial("STAINED_GLASS_PANE");
        } else {
            realType = 0;
            String materialName = "WHITE_STAINED_GLASS_PANE";
            switch (type) {
                case 1:
                    materialName = "ORANGE_STAINED_GLASS_PANE";
                    break;
                case 2:
                    materialName = "MAGENTA_STAINED_GLASS_PANE";
                    break;
                case 3:
                    materialName = "LIGHT_BLUE_STAINED_GLASS_PANE";
                    break;
                case 4:
                    materialName = "YELLOW_STAINED_GLASS_PANE";
                    break;
                case 5:
                    materialName = "LIME_STAINED_GLASS_PANE";
                    break;
                case 6:
                    materialName = "PINK_STAINED_GLASS_PANE";
                    break;
                case 7:
                    materialName = "GRAY_STAINED_GLASS_PANE";
                    break;
                case 8:
                    materialName = "LIGHT_GRAY_STAINED_GLASS_PANE";
                    break;
                case 9:
                    materialName = "CYAN_STAINED_GLASS_PANE";
                    break;
                case 10:
                    materialName = "PURPLE_STAINED_GLASS_PANE";
                    break;
                case 11:
                    materialName = "BLUE_STAINED_GLASS_PANE";
                    break;
                case 12:
                    materialName = "BROWN_STAINED_GLASS_PANE";
                    break;
                case 13:
                    materialName = "GREEN_STAINED_GLASS_PANE";
                    break;
                case 14:
                    materialName = "RED_STAINED_GLASS_PANE";
                    break;
                case 15:
                    materialName = "BLACK_STAINED_GLASS_PANE";
                    break;
            }
            material = Material.getMaterial(materialName);
        }
        ItemStack itemStack = new ItemStack(material, 1, realType);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(" ");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static Material getMapMaterial() {
        if (Material.getMaterial("EMPTY_MAP") != null) {
            return Material.getMaterial("EMPTY_MAP");
        } else {
            return Material.getMaterial("MAP");
        }
    }

    public static Material getGoldenAxeMaterial() {
        if (Material.getMaterial("GOLD_AXE") != null) {
            return Material.getMaterial("GOLD_AXE");
        } else {
            return Material.getMaterial("GOLDEN_AXE");
        }
    }

    public static Material getGoldenSwordMaterial() {
        if (Material.getMaterial("GOLD_SWORD") != null) {
            return Material.getMaterial("GOLD_SWORD");
        } else {
            return Material.getMaterial("GOLDEN_SWORD");
        }
    }

    public static Material getWoodenSwordMaterial() {
        if (Material.getMaterial("WOOD_SWORD") != null) {
            return Material.getMaterial("WOOD_SWORD");
        } else {
            return Material.getMaterial("WOODEN_SWORD");
        }
    }

    public static boolean isHelmet(Material material) {
        boolean isHelmetPlate = false;
        switch (material.toString()) {
            case "LEATHER_HELMET":
            case "CHAINMAIL_HELMET":
            case "IRON_HELMET":
            case "DIAMOND_HELMET":
            case "GOLD_HELMET":
            case "GOLDEN_HELMET":
                isHelmetPlate = true;
                break;
        }
        return isHelmetPlate;
    }

    public static boolean isChestPlate(Material material) {
        boolean isChestPlate = false;
        switch (material.toString()) {
            case "LEATHER_CHESTPLATE":
            case "IRON_CHESTPLATE":
            case "CHAINMAIL_CHESTPLATE":
            case "DIAMOND_CHESTPLATE":
            case "GOLD_CHESTPLATE":
            case "GOLDEN_CHESTPLATE":
                isChestPlate = true;
                break;
        }
        return isChestPlate;
    }

    public static boolean isLeggings(Material material) {
        boolean isLeggings = false;
        switch (material.toString()) {
            case "LEATHER_LEGGINGS":
            case "CHAINMAIL_LEGGINGS":
            case "IRON_LEGGINGS":
            case "DIAMOND_LEGGINGS":
            case "GOLD_LEGGINGS":
            case "GOLDEN_LEGGINGS":
                isLeggings = true;
                break;
        }
        return isLeggings;
    }

    public static boolean isBoots(Material material) {
        boolean isBoots = false;
        switch (material.toString()) {
            case "LEATHER_BOOTS":
            case "CHAINMAIL_BOOTS":
            case "IRON_BOOTS":
            case "DIAMOND_BOOTS":
            case "GOLD_BOOTS":
            case "GOLDEN_BOOTS":
                isBoots = true;
                break;
        }
        return isBoots;
    }

    public static List<Player> getOnlinePlayers() {
        List<Player> players = null;
        try {
            Class<?> clazz = Class.forName("org.bukkit.Bukkit");
            Method method = clazz.getMethod("getOnlinePlayers");
            if (method.getReturnType().equals(Collection.class)) {
                Collection<?> rawPlayers = (Collection<?>) (method
                        .invoke(Bukkit.getServer()));
                players = new ArrayList<>();
                for (Object o : rawPlayers) {
                    if (o instanceof Player) {
                        players.add((Player) o);
                    }
                }
            } else {
                players = Arrays.asList((Player[]) method.invoke(Bukkit
                        .getServer()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return players;
    }

    public static ItemStack getItemInMainHand(Player player) {
        ItemStack item = null;
        try {
            Class<?> playerClass = Class.forName("org.bukkit.entity.Player");
            Class<?> playerInventoryClass = Class
                    .forName("org.bukkit.inventory.PlayerInventory");
            try {
                Method method = playerClass.getMethod("getInventory");
                PlayerInventory inventory = (PlayerInventory) method
                        .invoke(player);
                Method method2 = playerInventoryClass
                        .getMethod("getItemInMainHand");
                item = (ItemStack) method2.invoke(inventory);
            } catch (NoSuchMethodException e) {
                Method method = playerClass.getMethod("getItemInHand");
                item = (ItemStack) method.invoke(player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    public static void setItemInMainHand(Player player, ItemStack item) {
        try {
            Class<?> playerClass = Class.forName("org.bukkit.entity.Player");
            Class<?> itemStackClass = Class
                    .forName("org.bukkit.inventory.ItemStack");
            Class<?> playerInventoryClass = Class
                    .forName("org.bukkit.inventory.PlayerInventory");
            try {
                Method method2 = playerClass.getMethod("getInventory");
                PlayerInventory inventory = (PlayerInventory) method2
                        .invoke(player);
                Method method3 = playerInventoryClass.getMethod(
                        "setItemInMainHand", itemStackClass);
                method3.invoke(inventory, item);
            } catch (NoSuchMethodException e) {
                Method method = playerClass.getMethod("setItemInHand",
                        itemStackClass);
                method.invoke(player, item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ProjectileSource getProjectileSource(Entity entity,
                                                       String entityName) {
        ProjectileSource ps = null;
        try {
            Class<?> clazz = Class.forName("org.bukkit.entity." + entityName);
            Method method = clazz.getMethod("getShooter");
            ps = (ProjectileSource) (method.invoke(entity));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ps;
    }

    public static boolean isHasItemFlagMethod() {
        Class<?> clazz;
        try {
            clazz = Class.forName("org.bukkit.inventory.meta.ItemMeta");
            for (Method method : clazz.getMethods()) {
                if (method.getName().equals("addItemFlags")) {
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isHasOffHandMethod() {
        Class<?> clazz;
        try {
            clazz = Class.forName("org.bukkit.inventory.PlayerInventory");
            for (Method method : clazz.getMethods()) {
                if (method.getName().equals("getItemInOffHand")) {
                    return true;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ItemStack getItemInOffHand(Player player) {
        ItemStack item = null;
        try {
            Class<?> clazz = Class.forName("org.bukkit.entity.Player");
            Method method = clazz.getMethod("getInventory");
            PlayerInventory inventory = (PlayerInventory) method.invoke(player);
            Class<?> clazz2 = Class
                    .forName("org.bukkit.inventory.PlayerInventory");
            Method method2 = clazz2.getMethod("getItemInOffHand");
            item = (ItemStack) method2.invoke(inventory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

    public static void setItemInOffHand(Player player, ItemStack item) {
        try {
            Class<?> clazzPlayer = Class.forName("org.bukkit.entity.Player");
            Class<?> clazzPlayerInventory = Class
                    .forName("org.bukkit.inventory.PlayerInventory");
            Class<?> itemStackClass = Class
                    .forName("org.bukkit.inventory.ItemStack");
            Method method = clazzPlayer.getMethod("getInventory");
            PlayerInventory inventory = (PlayerInventory) method.invoke(player);
            Method method2 = clazzPlayerInventory.getMethod("setItemInOffHand",
                    itemStackClass);
            method2.invoke(inventory, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getCraftSplashPotion() {
        try {
            return getCbClass("entity.CraftSplashPotion");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getCraftProjectile() {
        try {
            return getCbClass("entity.CraftProjectile");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getCraftProjectileSource(Entity entity,
                                                  String entityName) {
        Object ps = null;
        try {
            Class<?> clazz = getCbClass("entity." + entityName);
            Method method = clazz.getMethod("getShooter");
            ps = method.invoke(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ps;
    }

    private static Class<?> getNmsClass(String name)
            throws ClassNotFoundException {
        int serverVersionType = Main.getInstance().getServerVersionType();
        if (serverVersionType == 3) {
            return Class.forName("net.minecraft.server." + name);
        } else {
            return Class.forName("net.minecraft.server."
                    + Main.getInstance().getServerVersion() + "." + name);
        }
    }

    private static Class<?> getCbClass(String name)
            throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit."
                + Main.getInstance().getServerVersion() + "." + name);
    }

    public static void getAllClass() {
        if (Main.getInstance().getServerVersionType() == 1) {
            try {
                iChatBaseComponent = getNmsClass("IChatBaseComponent");
                chatComponentText = getNmsClass("ChatComponentText");
                packet = getNmsClass("Packet");
                packetPlayOutTitle = getNmsClass("PacketPlayOutTitle");
                enumTitleAction = getNmsClass("PacketPlayOutTitle$EnumTitleAction");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendTitle(Player player, String title, String subTitle,
                                 int fadeIn, int stay, int fadeOut) {
        if (Main.getInstance().getServerVersionType() == 0) {
            return;
        }
        try {
            if (Main.getInstance().getServerVersionType() == 1) {
                Enum<?>[] enumConstants = (Enum<?>[]) enumTitleAction
                        .getEnumConstants();
                Object enumTITLE = null;
                Object enumSUBTITLE = null;
                Object enumTIMES = null;
                for (Enum<?> enum1 : enumConstants) {
                    String name = enum1.name();
                    if (name.equals("TITLE")) {
                        enumTITLE = enum1;
                    }
                    if (name.equals("SUBTITLE")) {
                        enumSUBTITLE = enum1;
                    }
                    if (name.equals("TIMES")) {
                        enumTIMES = enum1;
                    }
                }
                Object chatComponentTextTitleInstance = chatComponentText
                        .getConstructor(String.class).newInstance(title);
                Object chatComponentTextSubTitleInstance = chatComponentText
                        .getConstructor(String.class).newInstance(subTitle);
                Object packetPlayOutTitleTitleInstance = packetPlayOutTitle
                        .getConstructor(enumTitleAction, iChatBaseComponent)
                        .newInstance(enumTITLE, chatComponentTextTitleInstance);
                Object packetPlayOutTitleSubTitleInstance = packetPlayOutTitle
                        .getConstructor(enumTitleAction, iChatBaseComponent)
                        .newInstance(enumSUBTITLE,
                                chatComponentTextSubTitleInstance);
                Object packetPlayOutTitleTimeInstance = packetPlayOutTitle
                        .getConstructor(enumTitleAction, iChatBaseComponent,
                                int.class, int.class, int.class).newInstance(
                                enumTIMES, null, fadeIn, stay, fadeOut);
                Object craftPlayer = getCbClass("entity.CraftPlayer").cast(
                        player);
                Object entityPlayer = craftPlayer.getClass()
                        .getMethod("getHandle").invoke(craftPlayer);
                Object playerConnection = entityPlayer.getClass()
                        .getField("playerConnection").get(entityPlayer);
                playerConnection
                        .getClass()
                        .getMethod("sendPacket", packet)
                        .invoke(playerConnection,
                                packetPlayOutTitleTitleInstance);
                playerConnection
                        .getClass()
                        .getMethod("sendPacket", packet)
                        .invoke(playerConnection,
                                packetPlayOutTitleSubTitleInstance);
                playerConnection
                        .getClass()
                        .getMethod("sendPacket", packet)
                        .invoke(playerConnection,
                                packetPlayOutTitleTimeInstance);
            }
            if (Main.getInstance().getServerVersionType() >= 2) {
                Class<?> clazz = Class.forName("org.bukkit.entity.Player");
                Method method = clazz.getMethod("sendTitle", String.class,
                        String.class, int.class, int.class, int.class);
                method.invoke(player, title, subTitle, fadeIn, stay, fadeOut);
            }
        } catch (Exception ignored) {
        }
    }

    public static void sendActionBar(Player player, String actionbar) {
        if (Main.getInstance().getServerVersionType() == 0) {
            return;
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(actionbar));
    }

    public static Sound getSound(String... soundNames) {
        try {
            Class<?> soundClass = Class.forName("org.bukkit.Sound");
            Enum<?>[] enumConstants = (Enum<?>[]) soundClass.getEnumConstants();
            Sound enumACTIONBAR = null;
            for (String soundName : soundNames) {
                for (Enum<?> enum1 : enumConstants) {
                    if (enum1.name().equals(soundName)) {
                        enumACTIONBAR = (Sound) enum1;
                        break;
                    }
                }
            }
            return enumACTIONBAR;
        } catch (ClassNotFoundException ignored) {
        }
        return null;
    }
}
