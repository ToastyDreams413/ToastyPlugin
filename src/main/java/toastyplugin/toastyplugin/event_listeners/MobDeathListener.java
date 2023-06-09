package toastyplugin.toastyplugin.event_listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import toastyplugin.toastyplugin.ToastyPlugin;

import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

public class MobDeathListener implements Listener {

    private final ToastyPlugin plugin;

    public MobDeathListener(ToastyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onZombieDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getEntity();
            if (zombie.getCustomName() != null && zombie.getCustomName().equals("Test Zombie")) {
                event.getDrops().clear();

                Location deathLocation = event.getEntity().getLocation();

                deathLocation.getBlock().setType(Material.CHEST);

                Vector<UUID> uuidVector = new Vector<>();
                UUID mobUniqueId = event.getEntity().getUniqueId();

                plugin.lootChests.put(mobUniqueId, uuidVector);
                plugin.mobDeathLocations.put(deathLocation, event.getEntity().getUniqueId());

                // Schedule a task to remove the chest after 30 seconds
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    if (deathLocation.getBlock().getType() == Material.CHEST) {
                        deathLocation.getBlock().setType(Material.AIR);
                        plugin.playerDamage.remove(mobUniqueId);
                        plugin.lootChests.remove(mobUniqueId);
                        plugin.mobDeathLocations.remove(deathLocation);
                    }
                }, 600L); // 30 seconds * 20 ticks/second
            }
        }
    }

}
