package toastyplugin.toastyplugin.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class CustomInventoryHolder implements InventoryHolder {
    private String name;

    public CustomInventoryHolder(String name) {
        this.name = name;
    }

    @Override
    public Inventory getInventory() {
        return null; // this could return a specific inventory if needed
    }

    public String getName() {
        return name;
    }
}