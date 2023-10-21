package me.rages.cosmosconflip.menu.impl;

import me.rages.cosmosconflip.CoinflipPlugin;
import me.rages.cosmosconflip.menu.MenuBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class CFMainMenu extends MenuBuilder {

    private CoinflipPlugin plugin;

    public CFMainMenu(CoinflipPlugin plugin) {
        super("Coin Flip Matches");
        this.plugin = plugin;
    }

    @Override
    public CFMainMenu init() {

        return this;
    }

    public void redraw() {
        int i = 0;
        for (CoinflipPlugin.CoinFlipMatch match : plugin.coinFlipMatchList) {
            ItemStack skullItem = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) skullItem.getItemMeta();
            skullMeta.setOwningPlayer(match.getCreatorMenu().getUser());
            skullItem.setItemMeta(skullMeta);
            inventory.setItem(i++, skullItem);
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        int slot = event.getSlot();
        if (slot < plugin.coinFlipMatchList.size()) {
            Player player = (Player) event.getWhoClicked();
            CoinflipPlugin.CoinFlipMatch match = plugin.coinFlipMatchList.get(slot);
            if (match.getCreatorMenu().getUser() != player) {
                CFColorMenu.start("Choose a Color", match.getAmount(), match.getIgnored(), match).init().open(player);
            } else {
                player.sendMessage(ChatColor.RED + "You cannot start a match with yourself.");
            }

        }

        System.out.println(event.getSlot());
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {

    }

    @Override
    public void onInventoryOpen(InventoryOpenEvent event) {

    }
}
