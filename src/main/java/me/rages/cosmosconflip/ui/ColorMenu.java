package me.rages.cosmosconflip.ui;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.AbstractMap;

import static me.rages.cosmosconflip.util.Util.COLOR_CODE_MAP;
import static me.rages.cosmosconflip.util.Util.setItemNameAndLore;

public class ColorMenu extends MenuBuilder {

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


    public ColorMenu(String title, double amount, int ignored) {
        super(title, InventoryType.DROPPER);
        this.ignored = ignored;
        this.amount = amount;
    }

    @Override
    public ColorMenu init() {
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

    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {

    }

    @Override
    public void onInventoryOpen(InventoryOpenEvent event) {

    }
}
