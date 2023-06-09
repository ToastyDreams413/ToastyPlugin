package toastyplugin.toastyplugin.event_listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import toastyplugin.toastyplugin.gui.CustomInventoryHolder;
import toastyplugin.toastyplugin.items.other.CustomWarp;

public class InventoryClickListener implements Listener {

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

                if (((CustomInventoryHolder) inventory.getHolder()).getName().equals("Main Menu")) {
                    if (clickedItem.getType() == Material.DIAMOND) {
                        Inventory classMenu = Bukkit.createInventory(new CustomInventoryHolder("Class Menu"), 54, "Class Menu");
                        player.openInventory(classMenu);
                    } else if (clickedItem.getType() == Material.COMPASS) {
                        Inventory warpMenu = Bukkit.createInventory(new CustomInventoryHolder("Warp Menu"), 54, "Warp Menu");
                        warpMenu.setItem(38, CustomWarp.createCustomWarpItem("Spawn"));
                        warpMenu.setItem(42, CustomWarp.createCustomWarpItem("Dungeon Hub"));
                        player.openInventory(warpMenu);
                    }
                }

                else if (((CustomInventoryHolder) inventory.getHolder()).getName().equals("Warp Menu")) {
                    player.closeInventory();
                    if (clickedItem.getItemMeta().getCustomModelData() == 1) {
                        player.performCommand("warp spawn");
                    }
                    else if (clickedItem.getItemMeta().getCustomModelData() == 2) {
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
