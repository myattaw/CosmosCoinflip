package me.rages.cosmosconflip.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class Util {

    public static final Map<Material, String> ITEM_NAMES = ImmutableMap.<Material, String>builder()
            .put(Material.PURPLE_CONCRETE, "&5&lPURPLE")
            .put(Material.BLUE_CONCRETE, "&b&lBLUE")
            .put(Material.CYAN_CONCRETE, "&b&lLIGHT BLUE")
            .put(Material.RED_CONCRETE, "&c&lRED")
            .put(Material.LIME_CONCRETE, "&a&lGREEN")
            .put(Material.YELLOW_CONCRETE, "&e&lYELLOW")
            .put(Material.ORANGE_CONCRETE, "&6&lORANGE")
            .put(Material.WHITE_CONCRETE, "&f&lWHITE")
            .put(Material.GRAY_CONCRETE, "&8&lGRAY")
            .build();
    public static final Map<Material, String> COLOR_CODE_MAP = ImmutableMap.<Material, String>builder()
            .put(Material.PURPLE_CONCRETE, "&5")
            .put(Material.BLUE_CONCRETE, "&9")
            .put(Material.LIGHT_BLUE_CONCRETE, "&b")
            .put(Material.RED_CONCRETE, "&c")
            .put(Material.LIME_CONCRETE, "&a")
            .put(Material.YELLOW_CONCRETE, "&e")
            .put(Material.ORANGE_CONCRETE, "&6")
            .put(Material.WHITE_CONCRETE, "&f")
            .put(Material.GRAY_CONCRETE, "&8")
            .build();

    private static final List<Map.Entry<Long, String>> CURRENCY_SUFFIXESS = Arrays.asList(
            Maps.immutableEntry(1000000000000000L, "Q"),
            Maps.immutableEntry(1000000000000L, "T"),
            Maps.immutableEntry(1000000000L, "B"),
            Maps.immutableEntry(1000000L, "M"),
            Maps.immutableEntry(1000L, "K")
    );

    public static String getAbbreviatedCurrency(double amount, boolean twoDp) {
        if (amount < 10000.0) {
            return String.format("%,d", (long) amount);
        }
        for (Map.Entry<Long, String> entry : CURRENCY_SUFFIXESS) {
            double value = amount / (double) entry.getKey().longValue();
            if (!(value >= 1.0)) continue;
            return twoDp ? String.format("%,.2f%s", value, entry.getValue()) : String.format("%,d%s", (long) Math.floor(value), entry.getValue());
        }
        return String.format("%,d", (long) amount);
    }

    public static long getValueFromAbbreviatedCurrency(String abbreviated) {
        try {
            char multiplier = Character.toUpperCase(abbreviated.charAt(abbreviated.length() - 1));
            if (Character.isDigit(multiplier)) {
                try {
                    return Long.parseLong(abbreviated);
                } catch (NumberFormatException ex) {
                    return -1L;
                }
            }
            String multiString = String.valueOf(multiplier);
            for (Map.Entry<Long, String> entry : CURRENCY_SUFFIXESS) {
                if (!entry.getValue().equals(multiString)) continue;
                return (long) (Double.parseDouble(abbreviated.substring(0, abbreviated.length() - 1).replace(",", "").replace("$", "")) * (double) entry.getKey().longValue());
            }
            return -1L;
        } catch (Throwable ex) {
            return -1L;
        }
    }

    public static ItemStack setItemNameAndLore(ItemStack itemStack, String displayName, String... lore) {
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',displayName));
            if (lore != null && lore.length > 0) {
                List<String> strings = Arrays.stream(lore)
                        .map(l -> ChatColor.translateAlternateColorCodes('&', l))
                        .collect(Collectors.toCollection(() -> new ArrayList<>(lore.length)));
                meta.setLore(strings);
            }
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    public static ItemStack setItemNameAndLore(ItemStack itemStack, String displayName, Map.Entry<String, String> placeholder, String... lore) {
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
            if (lore != null && lore.length > 0) {
                List<String> strings = new ArrayList<>(lore.length);
                Arrays.stream(lore).map(l -> ChatColor.translateAlternateColorCodes('&', l))
                        .forEach(s -> {
                            if (s.contains(placeholder.getKey())) {
                                s = s.replace(placeholder.getKey(), placeholder.getValue());
                            }
                            strings.add(s);
                        });
                meta.setLore(strings);
            }
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

}
