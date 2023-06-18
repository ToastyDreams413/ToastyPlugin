package toastyplugin.toastyplugin.items.other;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import toastyplugin.toastyplugin.data.ClassData;
import toastyplugin.toastyplugin.data.WarpData;

public class CustomClassMenuItem {

    public static ItemStack createCustomClassMenuItem(String className) {
        ItemStack warpPaper = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = warpPaper.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(ClassData.CLASS_TO_MENU_MODEL_NUMBER.get(className));
        meta.setDisplayName(className);
        warpPaper.setItemMeta(meta);
        return warpPaper;
    }

}
