package toastyplugin.toastyplugin.event_listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.data.ClassData;
import toastyplugin.toastyplugin.data.SwordData;
import toastyplugin.toastyplugin.data.WandData;
import toastyplugin.toastyplugin.gui.CustomInventoryHolder;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.*;

public class InteractionListener implements Listener {
    private final ToastyPlugin plugin;
    private Map<UUID, Long> lastWandShoot = new HashMap<>();
    private static final Map<UUID, Set<UUID>> damagedEntities = new HashMap<>();

    private int hitCount = 0;

    public InteractionListener(ToastyPlugin plugin) {
        this.plugin = plugin;
    }



    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        ItemStack mainHandItem = player.getInventory().getItemInMainHand();
        ItemStack offHandItem = player.getInventory().getItemInOffHand();

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {

            // System.out.println("RIGHT CLICKED!");

            Location location = event.getClickedBlock().getLocation();

            // chest looting
            if (plugin.lootInventories.containsKey(player.getUniqueId())) {
                for (HashMap<Location, Inventory> currentLoot : plugin.lootInventories.get(player.getUniqueId())) {
                    if (currentLoot.containsKey(location)) {

                        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                            @Override
                            public void run() {
                                PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.BLOCK_CHANGE);
                                packet.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
                                BlockData blockData = Material.BLACK_WOOL.createBlockData();
                                packet.getBlockData().write(0, WrappedBlockData.createData(blockData));
                                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
                            }
                        }, 1L);

                        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            event.getPlayer().openInventory(currentLoot.get(event.getClickedBlock().getLocation()));
                        }
                    }
                }
            }

        }

        // check if they are right-clicking
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            // check if they are using a wand
            if (mainHandItem != null && mainHandItem.getType() == Material.STONE_SWORD && mainHandItem.getItemMeta().hasCustomModelData()) {
                shootWand(player, ChatColor.stripColor(mainHandItem.getItemMeta().getDisplayName()));
            }

            // check if they are using a main menu
            if (mainHandItem != null && mainHandItem.getType() == Material.PAPER && mainHandItem.getItemMeta().hasCustomModelData()) {
                player.openInventory(createMainMenu());
                event.setCancelled(true);
            }
        }
    }



    @EventHandler
    public void onPlayerAnimation(PlayerAnimationEvent event) {
        if (event.getAnimationType() == PlayerAnimationType.ARM_SWING) {
            Player player = event.getPlayer();
            ItemStack mainHandItem = player.getInventory().getItemInMainHand();
            if (mainHandItem != null && mainHandItem.getType() == Material.WOODEN_SWORD && mainHandItem.getItemMeta().hasCustomModelData()) {
                checkSwordDamageCustomMob(player, ChatColor.stripColor(mainHandItem.getItemMeta().getDisplayName()));
            }
        }
    }



    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock) {
            if (event.getEntity().getCustomName() != null && event.getEntity().getCustomName().equals("projectile")) {
                event.setCancelled(true);
            }
        }
    }



    private void shootWand(Player player, String name) {
        String className = plugin.getConfig().getString(player.getUniqueId() + ".selectedClass");
        if (!ClassData.CLASS_WEAPONS.get(className).equals("Wand")) {
            player.sendMessage(ChatColor.RED + "Your selected class does not allow you to use wands!");
            return;
        }

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
        Color shotColor = WandData.WAND_SHOT_COLOR.get(name);

        Vector initialOffset = player.getLocation().getDirection().multiply(0.3);
        Vector direction = player.getLocation().getDirection().multiply(velocity);
        Location location = player.getEyeLocation().add(initialOffset);
        location.subtract(0, 0.5, 0);

        Particle.DustOptions dustOptions = new Particle.DustOptions(shotColor, 1);

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
                for (ArmorStand armorStand : plugin.aliveMobs.keySet()) {
                    if (armorStand.getLocation().distance(location) < 2) {
                        damageCustomMob(player, armorStand, damage);
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



    private void checkSwordDamageCustomMob(Player player, String swordName) {
        double range = SwordData.SWORD_RANGE.get(swordName);
        double damage = SwordData.SWORD_DAMAGE.get(swordName);
        String className = plugin.getConfig().getString(player.getUniqueId() + ".selectedClass");
        if (!ClassData.CLASS_WEAPONS.get(className).equals("Sword")) {
            player.sendMessage(ChatColor.RED + "Your selected class does not allow you to use swords!");
            return;
        }
        for (ArmorStand armorStand : plugin.aliveMobs.keySet()) {
            if (armorStand.getLocation().distance(player.getLocation()) <= range) {
                Vector angleToMob = armorStand.getLocation().add(0, 1, 0).toVector().subtract(player.getEyeLocation().toVector());
                // player.sendMessage("Your current normalized angle vector value: " + angleToMob.normalize().dot(player.getEyeLocation().getDirection()));
                if (angleToMob.normalize().dot(player.getEyeLocation().getDirection()) > 0.985) {
                    // player.sendMessage("your attack cooldown: " + player.getAttackCooldown());
                    if (player.getAttackCooldown() < 1) {
                        damage *= player.getAttackCooldown() * 0.5;
                    }
                    // player.sendMessage("your attack cooldown: " + player.getAttackCooldown() + " damage: " + damage);
                    DecimalFormat df = new DecimalFormat("#.##");
                    damage = Double.valueOf(df.format(damage));
                    damageCustomMob(player, armorStand, damage);
                    return;
                }
            }
        }
    }



    private void damageCustomMob(Player player, ArmorStand mob, double damage) {
        Location mobLocation = mob.getLocation();
        Location holoLocation = mobLocation.clone().add(0, 0.5, 0);
        ArmorStand damageHolo = holoLocation.getWorld().spawn(holoLocation, ArmorStand.class, (ArmorStand hologram) -> {
            hologram.setGravity(false);
            hologram.setVisible(false);
            hologram.setCustomName(ChatColor.RED + "-" + damage);
            hologram.setCustomNameVisible(true);
        });

        new BukkitRunnable() {
            private int c = 0;
            @Override
            public void run() {
                damageHolo.teleport(damageHolo.getLocation().add(0, 0.05, 0));
                c++;
                if (c >= 10) {
                    damageHolo.remove();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);

        double newHealth = plugin.aliveMobs.get(mob) - damage;
        if (newHealth <= 0) {
            plugin.aliveMobs.remove(mob);
            for (BukkitTask task : plugin.aliveMobTasks.get(mob)) {
                task.cancel();
            }
            plugin.aliveMobTasks.remove(mob);
            mob.remove();
        }
        else {
            plugin.aliveMobs.put(mob, newHealth);
        }

        player.sendMessage("Mob health after damage: " + newHealth);
    }

}
