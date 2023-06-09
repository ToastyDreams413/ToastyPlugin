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
import toastyplugin.toastyplugin.items.CustomItem;
import toastyplugin.toastyplugin.items.weapons.CustomSword;
import toastyplugin.toastyplugin.items.weapons.CustomWand;

import java.util.ArrayList;
import java.util.List;

public class GiveItemCommand implements CommandExecutor, TabCompleter {

    private final ToastyPlugin plugin;

    public GiveItemCommand(ToastyPlugin plugin) {
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

        if (args.length >= 2) {
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            if (targetPlayer != null) {
                String itemName = "";
                for (int i = 1; i < args.length; i++) {
                    itemName += args[i];
                    if (i != args.length - 1) {
                        itemName += " ";
                    }
                }
                ItemStack customItem = CustomItem.createCustomItem(itemName);
                if (customItem != null) {
                    player.getInventory().addItem(customItem);
                    sender.sendMessage(ChatColor.GREEN + "You have given " + args[0] + " a " + itemName);
                }
                else {
                    sender.sendMessage( ChatColor.RED + "Item not found.");
                }
            }
            else {
                sender.sendMessage( ChatColor.RED + "Player not found.");
            }
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) {
            List<String> itemNames = new ArrayList<>(ItemTypes.ITEM_TYPE.keySet());
            return itemNames;
        }
        return null;
    }

}