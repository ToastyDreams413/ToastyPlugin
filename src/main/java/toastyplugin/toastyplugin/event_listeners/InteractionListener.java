package toastyplugin.toastyplugin.event_listeners;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
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
    private HashMap<UUID, Long> lastWandShoot = new HashMap<>();
    private static final HashMap<UUID, Set<UUID>> damagedEntities = new HashMap<>();

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

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {

            // chest looting
            Block block = event.getClickedBlock();
            if (block.getType() == Material.CHEST) {
                Location blockLocation = block.getLocation();
                UUID mobUniqueId = getMobUniqueIdFromLocation(blockLocation);
                if (mobUniqueId != null) {
                    event.setCancelled(true); // cancels the default chest interaction

                    UUID playerUniqueId = player.getUniqueId();

                    if (!didEnoughDamage(mobUniqueId, playerUniqueId, 0)) {
                        player.sendMessage(ChatColor.RED + "You did not deal enough damage to open this chest!");
                        return;
                    }

                    if (playerAlreadyLooted(mobUniqueId, playerUniqueId)) {
                        player.sendMessage(ChatColor.RED + "You have already looted this chest!");
                        return;
                    }
                    plugin.lootChests.get(mobUniqueId).add(playerUniqueId);

                    Inventory inv = Bukkit.createInventory(null, 9, "Loot Chest"); // creates an inventory with 9 slots
                    ItemStack reward = new ItemStack(Material.DIAMOND, 5); // replace with your item
                    inv.addItem(reward); // add item to the inventory

                    // schedule GUI opening one tick later to avoid issues with the event cancelling
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> event.getPlayer().openInventory(inv), 1L);
                }
            } // end chest looting


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

    public void customDamage(LivingEntity entity, double damage, Player damager) {
        EntityDamageByEntityEvent damageEvent = new EntityDamageByEntityEvent(damager, entity, EntityDamageEvent.DamageCause.CUSTOM, damage);
        Bukkit.getPluginManager().callEvent(damageEvent);

        if (!damageEvent.isCancelled()) {
            entity.setHealth(Math.max(0, entity.getHealth() - damage));
            entity.damage(0, damager);
        }
    }



    public Inventory createMainMenu() {
        Inventory menu = Bukkit.createInventory(new CustomInventoryHolder("Main Menu"), 54, "Menu");
        menu.setItem(20, new ItemStack(Material.DIAMOND, 1));
        menu.setItem(24, new ItemStack(Material.COMPASS, 1));
        return menu;
    }

}
