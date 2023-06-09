package toastyplugin.toastyplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.custom_classes.Rank;
import toastyplugin.toastyplugin.data.ItemTypes;
import toastyplugin.toastyplugin.dungeons.DungeonGenerator;
import toastyplugin.toastyplugin.items.CustomItem;

import java.util.ArrayList;
import java.util.List;

public class GenerateDungeonCommand implements CommandExecutor, TabCompleter {

    private final ToastyPlugin plugin;

    public GenerateDungeonCommand(ToastyPlugin plugin) {
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
            player.sendMessage(ChatColor.RED + "You do not have permission to give items!");
            return true;
        }

        if (args.length >= 1) {
            String dungeonName = "";
            for (int i = 0; i < args.length; i++) {
                dungeonName += args[i];
                if (i != args.length - 1) {
                    dungeonName += " ";
                }
            }
            Location playerSpawnLocation = DungeonGenerator.generateDungeon(plugin, dungeonName);
            if (playerSpawnLocation == null) {
                player.sendMessage("That's not a valid dungeon!");
                return true;
            }
            player.sendMessage("Sending you to " + dungeonName);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    player.teleport(playerSpawnLocation);
                }
            }, 20L);
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length >= 1) {
            List<String> dungeonNames = new ArrayList<>();
            dungeonNames.add("Chicken's Den");
            return dungeonNames;
        }
        return null;
    }

}
