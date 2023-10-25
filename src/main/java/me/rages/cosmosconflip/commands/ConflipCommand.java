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

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (args.length > 1 && getValueFromAbbreviatedCurrency(args[1]) != -1) {

                        for (CoinflipPlugin.CoinFlipMatch match : plugin.coinFlipMatchList) {
                            if (match.getCreator().getUniqueId().equals(player.getUniqueId())) {
                                player.sendMessage(ChatColor.RED + "You can only have 1 coin flip at a time.");
                                return false;
                            }
                        }

                        double amount = getValueFromAbbreviatedCurrency(args[1]);
                        CFColorMenu.create("Choose a Color", amount,-1).init().open(player);
                    } else {
                        player.sendMessage(ChatColor.RED + "Usage: /coinflip create [$amount]");
                    }
                } else if (args[0].equalsIgnoreCase("remove")) {

                    for (CoinflipPlugin.CoinFlipMatch match : plugin.coinFlipMatchList) {
                        if (match.getCreator().getUniqueId().equals(player.getUniqueId())) {
                            player.sendMessage(ChatColor.RED + "You have removed your coin flip.");
                            double amount = match.getAmount();
                            plugin.coinFlipMatchList.remove(match);
                            CoinflipPlugin.getEconomy().depositPlayer(player, amount);
                            return false;
                        }
                    }

                }
            } else {
                // display menu off current coinflips
                plugin.getCfMainMenu().redraw();
                plugin.getCfMainMenu().open(player);
            }

            return true;
        }

        return false;
    }

}
