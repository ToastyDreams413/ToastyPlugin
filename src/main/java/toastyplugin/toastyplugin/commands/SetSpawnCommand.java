package toastyplugin.toastyplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.custom_classes.Rank;

public class SetSpawnCommand implements CommandExecutor {

    private final ToastyPlugin plugin;

    public SetSpawnCommand(ToastyPlugin plugin) {
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
            player.sendMessage(ChatColor.RED + "You do not have permission to set the spawn!");
            return true;
        }

        Location location = player.getLocation();
        location.getWorld().setSpawnLocation(location);
        String worldName = location.getWorld().getName();

        plugin.getConfig().set(worldName + ".spawn", location.getWorld().getName());
        plugin.getConfig().set(worldName + ".spawn.x", location.getX());
        plugin.getConfig().set(worldName + ".spawn.y", location.getY());
        plugin.getConfig().set(worldName + ".spawn.z", location.getZ());
        plugin.getConfig().set(worldName + ".spawn.yaw", location.getYaw());
        plugin.getConfig().set(worldName + ".spawn.pitch", location.getPitch());
        plugin.saveConfig();

        player.sendMessage("Spawn location set!");

        return true;
    }

}