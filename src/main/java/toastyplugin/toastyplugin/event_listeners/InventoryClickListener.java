package toastyplugin.toastyplugin.event_listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.data.ClassData;
import toastyplugin.toastyplugin.gui.CustomInventoryHolder;
import toastyplugin.toastyplugin.items.other.CustomClassMenuItem;
import toastyplugin.toastyplugin.items.other.CustomWarp;

import java.util.*;

public class InventoryClickListener implements Listener {

    private final ToastyPlugin plugin;

    public InventoryClickListener(ToastyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        Inventory inventory = event.getInventory();

        // check if it's a custom inventory
        if (inventory.getHolder() instanceof CustomInventoryHolder) {

            if (event.getClick() == ClickType.NUMBER_KEY) {
                event.setCancelled(true); // prevents taking the item
                return;
            }

            // make sure it's not a null item
            if (clickedItem != null) {

                event.setCancelled(true); // prevents taking the item

                if (((CustomInventoryHolder) inventory.getHolder()).getName().equals("Loot")) {

                    if (event.getClickedInventory() == player.getInventory()) {
                        return;
                    }

                    HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(clickedItem);
                    LinkedList<ItemStack> stashedItems = plugin.stashItems.getOrDefault(player.getUniqueId(), new LinkedList<>());

                    if (remainingItems.isEmpty()) {
                        event.setCurrentItem(null);
                    }
                    else {
                        player.sendMessage("You didn't have enough space, so some items were added to your stash!");
                        for (ItemStack itemStack : remainingItems.values()) {
                            stashedItems.add(new ItemStack(itemStack.getType(), itemStack.getAmount()));
                        }
                        clickedItem.setAmount(0);
                    }
                    plugin.stashItems.put(player.getUniqueId(), stashedItems);
                }

                else if (((CustomInventoryHolder) inventory.getHolder()).getName().equals("Main Menu")) {
                    if (clickedItem.getType() == Material.DIAMOND) {
                        Inventory classMenu = Bukkit.createInventory(new CustomInventoryHolder("Class Menu"), 54, "Class Menu");
                        for (String className : ClassData.CLASS_NAMES) {
                            classMenu.addItem(CustomClassMenuItem.createCustomClassMenuItem(className));
                        }
                        player.openInventory(classMenu);
                    } else if (clickedItem.getType() == Material.COMPASS) {
                        Inventory warpMenu = Bukkit.createInventory(new CustomInventoryHolder("Warp Menu"), 54, "Warp Menu");
                        warpMenu.setItem(38, CustomWarp.createCustomWarpItem("Spawn"));
                        warpMenu.setItem(42, CustomWarp.createCustomWarpItem("Dungeon Hub"));
                        player.openInventory(warpMenu);
                    }
                }

                else if (((CustomInventoryHolder) inventory.getHolder()).getName().equals("Class Menu")) {
                    player.closeInventory();
                    String className = ClassData.MENU_MODEL_NUMBER_TO_CLASS.get(clickedItem.getItemMeta().getCustomModelData());
                    plugin.getConfig().set(player.getUniqueId() + ".selectedClass", className);
                }

                else if (((CustomInventoryHolder) inventory.getHolder()).getName().equals("Warp Menu")) {
                    player.closeInventory();
                    if (clickedItem.getItemMeta().getCustomModelData() == 2) {
                        player.performCommand("warp spawn");
                    }
                    else if (clickedItem.getItemMeta().getCustomModelData() == 3) {
                        player.performCommand("warp dungeon hub");
                    }
                }

            }

        }
    }

    @EventHandler
    public void onMenuDrag(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();

        // Check if it's a custom inventory
        if (inventory.getHolder() instanceof CustomInventoryHolder) {
            event.setCancelled(true); // Prevents moving the item
        }
    }

    @EventHandler
    public void onCreativeInventoryAction(InventoryCreativeEvent event) {
        Inventory inventory = event.getInventory();

        // Check if it's a custom inventory
        if (inventory.getHolder() instanceof CustomInventoryHolder) {
            event.setCancelled(true); // Prevents moving the item
        }
    }

}
