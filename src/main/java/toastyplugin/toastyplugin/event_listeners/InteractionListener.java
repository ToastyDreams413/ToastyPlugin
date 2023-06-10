package toastyplugin.toastyplugin.event_listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.data.WandData;
import toastyplugin.toastyplugin.gui.CustomInventoryHolder;

import java.util.*;

public class InteractionListener implements Listener {
    private final ToastyPlugin plugin;
    private Map<UUID, Long> lastWandShoot = new HashMap<>();
    private static final Map<UUID, Set<UUID>> damagedEntities = new HashMap<>();

    public InteractionListener(ToastyPlugin plugin) {
        this.plugin = plugin;
    }

    private UUID getMobUniqueIdFromLocation(Location location) {
        for (Location storedLocation : plugin.mobDeathLocations.keySet()) {
            if (storedLocation.getBlockX() == location.getBlockX()
                    && storedLocation.getBlockY() == location.getBlockY()
                    && storedLocation.getBlockZ() == location.getBlockZ()) {
                return plugin.mobDeathLocations.get(storedLocation);
            }
        }
        return null;
    }

    public boolean didEnoughDamage(UUID entityUniqueId, UUID playerUniqueId, int damageThreshold) {
        if (plugin.playerDamage.containsKey(entityUniqueId)) {
            if (plugin.playerDamage.get(entityUniqueId).containsKey(playerUniqueId)) {
                return plugin.playerDamage.get(entityUniqueId).get(playerUniqueId) > damageThreshold;
            }
        }
        return false;
    }

    public boolean playerAlreadyLooted(UUID entityUniqueId, UUID playerUniqueId) {
        for (UUID storedUniqueId : plugin.lootChests.get(entityUniqueId)) {
            if (storedUniqueId.equals(playerUniqueId)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {

            // chest looting
            if (plugin.lootInventories.containsKey(player.getUniqueId())) {
                for (HashMap<Location, Inventory> currentLoot : plugin.lootInventories.get(player.getUniqueId())) {
                    if (currentLoot.containsKey(event.getClickedBlock().getLocation())) {
                        event.getPlayer().openInventory(currentLoot.get(event.getClickedBlock().getLocation()));
                    }
                }
            }

        }

        // check if they are right clicking
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // check if they are using a wand
            if (item != null && item.getType() == Material.STONE_SWORD && item.getItemMeta().hasCustomModelData()) {
                shootWand(player, ChatColor.stripColor(item.getItemMeta().getDisplayName()));
            }

            // check if they are using a main menu
            if (item != null && item.getType() == Material.MAP && item.getItemMeta().hasCustomModelData()) {
                player.openInventory(createMainMenu());
                event.setCancelled(true);
            }
        }
    }



    private void shootWand(Player player, String name) {
        int cooldown = WandData.WAND_COOLDOWN.get(name);
        UUID playerUUID = player.getUniqueId();

        // check if the player is still on cooldown for firing the wand
        if (lastWandShoot.containsKey(playerUUID)) {
            long lastUseTime = lastWandShoot.get(playerUUID);
            if (System.currentTimeMillis() - lastUseTime < cooldown) {
                return;
            }
        }

        lastWandShoot.put(playerUUID, System.currentTimeMillis());

        double damage = WandData.WAND_DAMAGE.get(name);
        double velocity = WandData.WAND_VELOCITY.get(name);
        double range = WandData.WAND_RANGE.get(name);

        Vector initialOffset = player.getLocation().getDirection().multiply(0.3);
        Vector direction = player.getLocation().getDirection().multiply(velocity);
        Location location = player.getEyeLocation().add(initialOffset);
        location.subtract(0, 0.5, 0);

        Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 1);

        int numIters = (int) (range / velocity);
        new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i++ >= numIters) {
                    this.cancel();
                    return;
                }

                location.add(direction);
                player.getWorld().spawnParticle(Particle.REDSTONE, location, 5, dustOptions);

                // check if the particles hit a block
                if (!location.getBlock().getType().isAir()) {
                    this.cancel();
                    return;
                }

                // check for nearby mobs
                Collection<Entity> nearbyEntities = location.getWorld().getNearbyEntities(location, 1, 1, 1);
                for (Entity entity : nearbyEntities) {
                    if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                        ((LivingEntity) entity).damage(damage);
                        ((LivingEntity) entity).setNoDamageTicks(0);
                        EntityDamagedListener.addDamage(plugin, entity.getUniqueId(), player.getUniqueId(), damage);
                        this.cancel();
                        return;
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }



    public Inventory createMainMenu() {
        Inventory menu = Bukkit.createInventory(new CustomInventoryHolder("Main Menu"), 54, "Menu");
        menu.setItem(20, new ItemStack(Material.DIAMOND, 1));
        menu.setItem(24, new ItemStack(Material.COMPASS, 1));
        return menu;
    }

}
