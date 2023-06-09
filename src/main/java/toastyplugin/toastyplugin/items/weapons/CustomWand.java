package toastyplugin.toastyplugin.items.weapons;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import toastyplugin.toastyplugin.data.SwordData;
import toastyplugin.toastyplugin.data.WandData;

import java.util.ArrayList;
import java.text.DecimalFormat;

public class CustomWand {

    public static ItemStack createCustomWand(String wandName) {
        ItemStack customWand = new ItemStack(Material.STONE_SWORD, 1);
        ItemMeta meta = customWand.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setDisplayName(WandData.WAND_DISPLAY_NAME.get(wandName));
        ArrayList<String> wandLore = new ArrayList<>();
        wandLore.add("");
        wandLore.add(ChatColor.YELLOW + WandData.WAND_LORE.get(wandName));
        wandLore.add("");

        double damage = WandData.WAND_DAMAGE.get(wandName);
        DecimalFormat df = new DecimalFormat("0.#");
        String damageStr = df.format(damage);
        wandLore.add(ChatColor.AQUA + "damage: " + damageStr);

        double cooldown = WandData.WAND_COOLDOWN.get(wandName);
        String cooldownStr = df.format(cooldown / 1000);
        wandLore.add(ChatColor.AQUA + "cooldown: " + cooldownStr);

        double range = WandData.WAND_RANGE.get(wandName);
        String rangeStr = df.format(range);
        wandLore.add(ChatColor.AQUA + "range: " + rangeStr);

        double velocity = WandData.WAND_VELOCITY.get(wandName);
        String velocityStr = df.format(velocity);
        wandLore.add(ChatColor.AQUA + "velocity: " + velocityStr);

        meta.setLore(wandLore);
        meta.setCustomModelData(WandData.WAND_MODEL_NUMBER.get(wandName));
        customWand.setItemMeta(meta);
        return customWand;
    }
}
