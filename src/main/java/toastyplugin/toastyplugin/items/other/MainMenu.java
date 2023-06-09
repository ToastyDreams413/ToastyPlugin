package toastyplugin.toastyplugin.items.other;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainMenu {

    public static ItemStack createOtherItem(String itemName) {
        if (itemName.equals("Main Menu")) {
            ItemStack mainMenu = new ItemStack(Material.MAP, 1);
            ItemMeta meta = mainMenu.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
            meta.setUnbreakable(true);
            meta.setCustomModelData(1);
            meta.setDisplayName("Main Menu");
            mainMenu.setItemMeta(meta);
            return mainMenu;
        }
        return null;
    }

}
