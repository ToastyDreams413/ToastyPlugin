package toastyplugin.toastyplugin.items.weapons;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.data.BowData;

import java.util.ArrayList;
import java.text.DecimalFormat;

public class CustomBow implements Listener {

    private final ToastyPlugin plugin;

    public CustomBow(ToastyPlugin plugin) {
        this.plugin = plugin;
    }

    public static ItemStack createCustomBow(String bowName) {
        ItemStack customBow = new ItemStack(Material.BOW, 1);
        ItemMeta meta = customBow.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        meta.setUnbreakable(true);
        meta.setDisplayName(BowData.BOW_DISPLAY_NAME.get(bowName));
        ArrayList<String> bowLore = new ArrayList<>();
        bowLore.add("");
        bowLore.add(ChatColor.YELLOW + BowData.BOW_LORE.get(bowName));
        bowLore.add("");

        double damage = BowData.BOW_DAMAGE.get(bowName);
        DecimalFormat df = new DecimalFormat("0.#");
        String damageStr = df.format(damage);
        bowLore.add(ChatColor.AQUA + "damage: " + damageStr);

        meta.setLore(bowLore);
        meta.setCustomModelData(BowData.BOW_MODEL_NUMBER.get(bowName));
        customBow.setItemMeta(meta);
        return customBow;
    }

    @EventHandler
    public void onBowUse(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        ItemStack item = event.getBow();
        if (item == null) return;

        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            Arrow arrow = (Arrow) event.getProjectile();
            double arrowDamage = BowData.BOW_DAMAGE.get(ChatColor.stripColor(item.getItemMeta().getDisplayName()));
            arrow.setMetadata("Custom Damage", new FixedMetadataValue(plugin, arrowDamage));
            arrow.setMetadata("Player UUID", new FixedMetadataValue(plugin, player.getUniqueId()));
        }
    }
}