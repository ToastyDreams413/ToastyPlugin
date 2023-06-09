package toastyplugin.toastyplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.custom_classes.Rank;
import toastyplugin.toastyplugin.data.ItemTypes;
import toastyplugin.toastyplugin.mobs.CustomZombie;

import java.util.ArrayList;
import java.util.List;

public class SpawnMobCommand implements CommandExecutor, TabCompleter {

    private final ToastyPlugin plugin;

    public SpawnMobCommand(ToastyPlugin plugin) {
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
            player.sendMessage(ChatColor.RED + "You do not have permission to spawn mobs!");
            return true;
        }

        if (args.length == 1) {
            String mobName = args[0];
            if (mobName.equals("zombie1")) {
                CustomZombie.spawnCustomZombie(player.getLocation());
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> mobNames = new ArrayList<>();
            mobNames.add("zombie1");
            return mobNames;
        }
        return null;
    }

}
