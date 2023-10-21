package me.rages.cosmosconflip.menu.impl;

import me.rages.cosmosconflip.CoinflipPlugin;
import me.rages.cosmosconflip.menu.MenuBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.AbstractMap;

import static me.rages.cosmosconflip.util.Util.COLOR_CODE_MAP;
import static me.rages.cosmosconflip.util.Util.setItemNameAndLore;

public class CFColorMenu extends MenuBuilder {

    private int ignored;
    private double amount;

    private static final String[] CREATE_LORE = {
            "&7Click to select this color", "&7and create the coin flip for", "&a$%amount%"
    };

    private static final String[] START_LORE = {
            "&7Click to select this color", "&7and start the coin flip for", "&a$%amount%"
    };

    private static final String[] ITEM_NAMES = {
            "&5&lPURPLE",
            "&b&lLIGHT BLUE",
            "&c&lRED",
            "&a&lGREEN",
            "&e&lYELLOW",
            "&6&lORANGE",
            "&f&lWHITE",
            "&8&lGRAY",
            "&0&lBLACK"
    };


    private CFColorMenu(String title, double amount, int ignored) {
        super(title, InventoryType.DROPPER);
        this.ignored = ignored;
        this.amount = amount;
    }

    public static CFColorMenu create(String title, double amount, int ignored) {
        return new CFColorMenu(title, amount, ignored);
    }

    @Override
    public CFColorMenu init() {
        int i = 0;
        for (Material material : COLOR_CODE_MAP.keySet()) {
            if (i != ignored) {
                inventory.setItem(i,
                        setItemNameAndLore(
                                new ItemStack(material),
                                ITEM_NAMES[i],
                                new AbstractMap.SimpleEntry<>("%amount%", String.format("%,.2f", amount)),
                                ignored == -1 ? CREATE_LORE : START_LORE
                        )
                );
            }
            i++;
        }
        return this;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        // Take money from player
        // if player does not have enough money cancel
        event.setCancelled(true);
        ItemStack itemStack = event.getCurrentItem();

        Player player = (Player) event.getWhoClicked();

        if (COLOR_CODE_MAP.containsKey(itemStack.getType())) {
            CoinflipPlugin.plugin.coinFlipMatchList.add(
                    CoinflipPlugin.CoinFlipMatch.create(
                            player,
                            new CFViewMenu(player, amount, itemStack.getType())
                    )
            );
            player.closeInventory();
        }

        Bukkit.broadcastMessage("test: " + CoinflipPlugin.plugin.coinFlipMatchList.size());

    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
    }

    @Override
    public void onInventoryOpen(InventoryOpenEvent event) {
    }
}
