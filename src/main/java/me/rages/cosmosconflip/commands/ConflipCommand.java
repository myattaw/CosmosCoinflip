package me.rages.cosmosconflip.commands;

import me.rages.cosmosconflip.CoinflipPlugin;
import me.rages.cosmosconflip.ui.CFMenu;
import me.rages.cosmosconflip.ui.ColorMenu;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

public class ConflipCommand implements CommandExecutor {

    public CoinflipPlugin plugin;

    public ConflipCommand(CoinflipPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("coinflip").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
//            player.openInventory(new ColorMenu("Choose a Color", InventoryType.DISPENSER).init().getInventory());

            CoinflipPlugin.CoinFlipMatch match = CoinflipPlugin.CoinFlipMatch.create(
                    player, new CFMenu("cf", Material.PURPLE_CONCRETE).init().open(player)
            );

            match.startGame(player, new CFMenu("cf", Material.RED_CONCRETE).init());

            return true;
        }

        return false;
    }

}
