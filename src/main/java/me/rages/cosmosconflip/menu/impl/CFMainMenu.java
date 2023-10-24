package me.rages.cosmosconflip.menu.impl;

import me.rages.cosmosconflip.CoinflipPlugin;
import me.rages.cosmosconflip.menu.MenuBuilder;
import me.rages.cosmosconflip.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class CFMainMenu extends MenuBuilder {

    private CoinflipPlugin plugin;

    private String displayName;
    private List<String> lore;


    public CFMainMenu(CoinflipPlugin plugin) {
        super("Coin Flip Matches");
        this.plugin = plugin;
    }

    @Override
    public CFMainMenu init() {
        this.displayName = plugin.getConfig().getString("guis.main-gui.item-name");
        this.lore = plugin.getConfig().getStringList("guis.main-gui.item-lore");
        return this;
    }

    public void redraw() {
        int i = 0;
        inventory.clear();
        for (CoinflipPlugin.CoinFlipMatch match : plugin.coinFlipMatchList) {
            ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
            skullMeta.setOwningPlayer(match.getCreatorMenu().getUser());
            skullMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                    displayName.replace("%player%", match.getCreator().getName())
                            .replace("%color%", Util.COLOR_CODE_MAP.get(match.getCreatorMaterial())))
            );
            List<String> newLore = new ArrayList<>(lore.size());
            for (String l : lore) {
                newLore.add(
                        ChatColor.translateAlternateColorCodes(
                                '&',
                                l.replace("%amount%", Util.getAbbreviatedCurrency(match.getAmount(), true))
                                        .replace("%color_name%", Util.ITEM_NAMES.get(match.getCreatorMaterial()))
                                        .replace("%ratio%", "")
                        )
                );
            }
            skullMeta.setLore(newLore);
            skullItem.setItemMeta(skullMeta);

            inventory.setItem(i++, skullItem);
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);
        int slot = event.getSlot();
        if (slot >= 0 && slot < plugin.coinFlipMatchList.size()) {
            Player player = (Player) event.getWhoClicked();
            CoinflipPlugin.CoinFlipMatch match = plugin.coinFlipMatchList.get(slot);
            if (match.getCreatorMenu().getUser() != player) {
                CFColorMenu.start("Choose a Color", match.getAmount(), match.getIgnored(), match).init().open(player);
            } else {
                player.sendMessage(ChatColor.RED + "You cannot start a match with yourself.");
            }

        }
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {

    }

    @Override
    public void onInventoryOpen(InventoryOpenEvent event) {

    }
}
