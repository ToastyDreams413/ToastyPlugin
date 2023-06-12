package toastyplugin.toastyplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import toastyplugin.toastyplugin.ToastyPlugin;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {

    private final ToastyPlugin plugin;

    public WarpCommand(ToastyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length >= 1) {
            String targetLocation = "";
            for (int i = 0; i < args.length; i++) {
                targetLocation += args[i];
                if (i != args.length - 1) {
                    targetLocation += " ";
                }
            }
            if (targetLocation.equals("spawn")) {
                if (plugin.getConfig().isSet("world.spawn")) {
                    String worldName = player.getWorld().getName();
                    World world = Bukkit.getWorld("world");
                    double x = plugin.getConfig().getDouble("world.spawn.x");
                    double y = plugin.getConfig().getDouble("world.spawn.y");
                    double z = plugin.getConfig().getDouble("world.spawn.z");
                    float yaw = (float) plugin.getConfig().getDouble("world.spawn.yaw");
                    float pitch = (float) plugin.getConfig().getDouble("world.spawn.pitch");

                    Location location = new Location(world, x, y, z, yaw, pitch);
                    player.teleport(location);

                    player.sendMessage("Warped to spawn!");
                } else {
                    player.sendMessage("The spawn location is not set!");
                }
            }
            else if (targetLocation.equals("dungeons")) {
                sender.sendMessage("Warped to the dungeons world!");
                World dungeonsWorld = Bukkit.getWorld("world_dungeons");
                player.teleport(dungeonsWorld.getSpawnLocation().add(0.5, 0, 0.5));
            }
            else {
                sender.sendMessage("Please specify a valid target location to warp to!");
            }
        }
        else {
            sender.sendMessage("Please specify a valid target location to warp to!");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> warpNames = new ArrayList<>();
            warpNames.add("spawn");
            warpNames.add("dungeons");
            return warpNames;
        }
        return null;
    }
}