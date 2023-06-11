package toastyplugin.toastyplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.custom_classes.Rank;
import toastyplugin.toastyplugin.dungeons.DungeonGenerator;

public class ClearBlocksCommand implements CommandExecutor {

    private final ToastyPlugin plugin;

    public ClearBlocksCommand(ToastyPlugin plugin) {
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
            player.sendMessage(ChatColor.RED + "You do not have permission to clear blocks!");
            return true;
        }

        if (args.length >= 1) {
            int radius = Integer.parseInt(args[0]);

            for (int x = 0; x < radius; x++) {
                for (int y = 0; y < radius; y++) {
                    for (int z = 0; z < radius; z++) {
                        Location blockLocation = new Location(player.getWorld(), player.getLocation().getBlockX() + x, player.getLocation().getBlockY() + y, player.getLocation().getBlockZ() + z);
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(player.getWorld(), player.getLocation().getBlockX() - x, player.getLocation().getBlockY() - y, player.getLocation().getBlockZ() - z);
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(player.getWorld(), player.getLocation().getBlockX() + x, player.getLocation().getBlockY() - y, player.getLocation().getBlockZ() - z);
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(player.getWorld(), player.getLocation().getBlockX() - x, player.getLocation().getBlockY() + y, player.getLocation().getBlockZ() - z);
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(player.getWorld(), player.getLocation().getBlockX() - x, player.getLocation().getBlockY() - y, player.getLocation().getBlockZ() + z);
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(player.getWorld(), player.getLocation().getBlockX() + x, player.getLocation().getBlockY() + y, player.getLocation().getBlockZ() - z);
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(player.getWorld(), player.getLocation().getBlockX() + x, player.getLocation().getBlockY() - y, player.getLocation().getBlockZ() + z);
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(player.getWorld(), player.getLocation().getBlockX() - x, player.getLocation().getBlockY() + y, player.getLocation().getBlockZ() + z);
                        blockLocation.getBlock().setType(Material.AIR);
                    }
                }
            }
            player.sendMessage(ChatColor.GREEN + "Successfully cleared all blocks in a radius of " + radius + "!");
        }

        return true;
    }

}
