package toastyplugin.toastyplugin.event_listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.data.SwordData;

import java.util.HashMap;
import java.util.UUID;

public class EntityDamagedListener implements Listener {

    private final ToastyPlugin plugin;

    public EntityDamagedListener(ToastyPlugin plugin) {
        this.plugin = plugin;
    }

    public static void addDamage(ToastyPlugin plugin, UUID entityUniqueId, UUID playerUniqueId, double damageAmount) {
        HashMap<UUID, Double> entityDamageMap = plugin.playerDamage.getOrDefault(entityUniqueId, new HashMap<>());
        double existingDamage = entityDamageMap.getOrDefault(playerUniqueId, 0.0);
        entityDamageMap.put(playerUniqueId, existingDamage + damageAmount);

        plugin.playerDamage.put(entityUniqueId, entityDamageMap);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            Player player = (Player) event.getDamager();
            LivingEntity entity = (LivingEntity) event.getEntity();
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getType() == Material.WOODEN_SWORD && item.getItemMeta().hasCustomModelData()) {
                event.setDamage(SwordData.SWORD_DAMAGE.get(ChatColor.stripColor(item.getItemMeta().getDisplayName())));
            }

            else if (item.getType() == Material.STONE_SWORD && item.getItemMeta().hasCustomModelData()) {
                event.setDamage(1);
            }

            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                @Override
                public void run() {
                    entity.setNoDamageTicks(0);
                }
            });
            addDamage(plugin, entity.getUniqueId(), player.getUniqueId(), event.getFinalDamage());
            return;
        }

        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.hasMetadata("Custom Damage")) {
                event.setDamage(arrow.getMetadata("Custom Damage").get(0).asDouble());
                addDamage(plugin, event.getEntity().getUniqueId(), UUID.fromString(arrow.getMetadata("Player UUID").get(0).asString()), event.getFinalDamage());
            }
            return;
        }
    }

}
