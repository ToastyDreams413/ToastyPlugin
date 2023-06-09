package toastyplugin.toastyplugin.items.weapons;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import toastyplugin.toastyplugin.data.SwordData;

import java.util.ArrayList;
import java.text.DecimalFormat;

public class CustomSword {

    public static ItemStack createCustomSword(String swordName) {
        ItemStack customSword = new ItemStack(Material.WOODEN_SWORD, 1);
        ItemMeta meta = customSword.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setDisplayName(SwordData.SWORD_DISPLAY_NAME.get(swordName));
        ArrayList<String> swordLore = new ArrayList<>();
        swordLore.add("");
        swordLore.add(ChatColor.YELLOW + SwordData.SWORD_LORE.get(swordName));
        swordLore.add("");

        double damage = SwordData.SWORD_DAMAGE.get(swordName);
        DecimalFormat df = new DecimalFormat("0.#");
        String damageStr = df.format(damage);
        swordLore.add(ChatColor.AQUA + "damage: " + damageStr);

        meta.setLore(swordLore);
        meta.setCustomModelData(SwordData.SWORD_MODEL_NUMBER.get(swordName));
        customSword.setItemMeta(meta);
        return customSword;
    }
}
