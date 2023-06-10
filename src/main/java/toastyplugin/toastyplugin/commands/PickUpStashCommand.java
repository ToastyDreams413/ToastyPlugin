package toastyplugin.toastyplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.custom_classes.Rank;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Vector;

public class PickUpStashCommand implements CommandExecutor {

    private final ToastyPlugin plugin;

    public PickUpStashCommand(ToastyPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        LinkedList<ItemStack> stash = plugin.stashItems.get(player.getUniqueId());
        if (stash.isEmpty()) {
            player.sendMessage("You don't have any items in your stash!");
        }
        else {
            int pickedUpAmount = 0;
            int leftoverAmount = 0;
            while (!stash.isEmpty()) {
                ItemStack currentItem = stash.remove();
                pickedUpAmount += currentItem.getAmount();
                HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(currentItem);
                if (!remainingItems.isEmpty()) {
                    int curLeftover = remainingItems.get(0).getAmount();
                    currentItem.setAmount(curLeftover);
                    pickedUpAmount -= curLeftover;
                    stash.add(new ItemStack(currentItem.getType(), currentItem.getAmount()));
                    break;
                }
            }
            for (ItemStack itemStack : stash) {
                leftoverAmount += itemStack.getAmount();
            }
            player.sendMessage("You got " + pickedUpAmount + " items from your stash! You still have " + leftoverAmount + " items in your stash!");
        }
        plugin.stashItems.put(player.getUniqueId(), stash);

        return true;
    }

}
