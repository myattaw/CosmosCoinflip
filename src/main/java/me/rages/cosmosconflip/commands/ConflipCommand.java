package me.rages.cosmosconflip.commands;

import me.rages.cosmosconflip.CoinflipPlugin;
import me.rages.cosmosconflip.menu.impl.CFColorMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static me.rages.cosmosconflip.util.Util.getValueFromAbbreviatedCurrency;

public class ConflipCommand implements CommandExecutor {

    public CoinflipPlugin plugin;

    public ConflipCommand(CoinflipPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getCommand("coinflip").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String str, @NotNull String[] args) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
//            player.openInventory(new ColorMenu("Choose a Color", InventoryType.DISPENSER).init().getInventory());


            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (args.length > 1 && getValueFromAbbreviatedCurrency(args[1]) != -1) {
                        double amount = getValueFromAbbreviatedCurrency(args[1]);
                        CFColorMenu.create("Choose a Color", amount,-1).init().open(player);
                        Bukkit.broadcastMessage("created: " + amount);
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /coinflip create [$amount]");
                    }
                }
            } else {
                // display menu off current coinflips
            }

            System.out.println(args.length);

//            CoinflipPlugin.CoinFlipMatch match = CoinflipPlugin.CoinFlipMatch.create(
//                    player, new CFMenu(player, 1245750, Material.PURPLE_CONCRETE).init().open(player)
//            );
//
//
//            // target instead of player below
//            match.startGame(player, new CFMenu(player, 1245750, Material.RED_CONCRETE).init());
//
//            match.getCreatorMenu().setOpponent(
//                    match.getOpponentMenu().getUser(), match.getOpponentMenu().getUserMaterial()
//            );
//            match.getOpponentMenu().setOpponent(
//                    match.getCreatorMenu().getUser(), match.getCreatorMenu().getUserMaterial()
//            );


            return true;
        }

        return false;
    }

}
