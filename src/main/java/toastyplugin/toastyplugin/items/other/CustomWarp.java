package toastyplugin.toastyplugin.items.other;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import toastyplugin.toastyplugin.data.WarpData;

public class CustomWarp {

    public static ItemStack createCustomWarpItem(String warpName) {
        ItemStack warpPaper = new ItemStack(Material.PAPER, 1);
        ItemMeta meta = warpPaper.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setCustomModelData(WarpData.WARP_MODEL_NUMBER.get(warpName));
        meta.setDisplayName(warpName);
        warpPaper.setItemMeta(meta);
        return warpPaper;
    }

}
