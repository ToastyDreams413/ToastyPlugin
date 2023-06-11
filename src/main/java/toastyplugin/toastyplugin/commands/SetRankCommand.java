package toastyplugin.toastyplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.custom_classes.Rank;

public class SetRankCommand implements CommandExecutor {

    private final ToastyPlugin plugin;

    public SetRankCommand(ToastyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;
        Rank rank = new Rank(plugin.getConfig().getString(player.getUniqueId() + ".rank"));
        if (rank.getValue() < 10) {
            player.sendMessage(ChatColor.RED + "You do not have permission to set ranks!");
            return true;
        }

        if (args.length >= 2) {
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer != null) {
                String rankName = "";
                for (int i = 1; i < args.length; i++) {
                    rankName += args[i];
                    if (i != args.length - 1) {
                        rankName += " ";
                    }
                }
                plugin.getConfig().set(targetPlayer.getUniqueId() + ".rank", rankName);
                Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                    @Override
                    public void run() {
                        plugin.saveConfig();
                    }
                });
                sender.sendMessage(ChatColor.GREEN + "Player " + args[0] + "'s rank has been set to " + rankName);
            }
            else {
                sender.sendMessage( ChatColor.RED + "Player not found.");
            }
            return true;
        }

        return true;
    }
}