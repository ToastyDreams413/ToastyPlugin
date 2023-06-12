package toastyplugin.toastyplugin.mobs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import toastyplugin.toastyplugin.ToastyPlugin;

public class MotherHen {

    public static void spawnMotherHen(ToastyPlugin plugin, Location location) {
        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setInvulnerable(true);

        ItemStack cobwebItem = new ItemStack(Material.COBWEB);
        ItemMeta meta = cobwebItem.getItemMeta();
        meta.setCustomModelData(1);
        cobwebItem.setItemMeta(meta);

        armorStand.getEquipment().setHelmet(cobwebItem);
        plugin.aliveMobs.add(armorStand);

        // create a new BukkitRunnable to control the armor stand
        new BukkitRunnable() {
            @Override
            public void run() {
                // Move the armor stand in a random direction
                double x = Math.random() - 0.5;
                double z = Math.random() - 0.5;
                armorStand.teleport(armorStand.getLocation().add(x, 0, z));

                // Check for players within 10 blocks
                for (Entity entity : armorStand.getNearbyEntities(10, 10, 10)) {
                    if (entity instanceof Player) {
                        // Create a mini block at the armor stand's location
                        FallingBlock block = armorStand.getWorld().spawnFallingBlock(
                                armorStand.getLocation(),
                                Material.DIAMOND_BLOCK.createBlockData()  // Use any material you want here
                        );

                        // configure the FallingBlock
                        block.setGravity(false);
                        block.setDropItem(false);
                        block.setHurtEntities(true);
                        Vector velocity = entity.getLocation().subtract(armorStand.getLocation()).toVector().normalize().multiply(0.3);

                        block.setVelocity(velocity);

                        // check for nearby players to damage them
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!block.isValid()) {
                                    this.cancel();
                                    return;
                                }

                                for (Entity entity : block.getNearbyEntities(0.1, 0.1, 0.1)) {
                                    if (entity instanceof Player) {
                                        ((Player) entity).damage(1.0);
                                        block.remove();
                                        this.cancel();
                                    }
                                }

                                // if it is about to collide with something, remove it
                                Location nextLocation = block.getLocation().add(block.getVelocity());
                                for (int x = -1; x <= 1; x++) {
                                    for (int y = -1; y <= 1; y++) {
                                        for (int z = -1; z <= 1; z++) {
                                            Material adjacentBlockMaterial = nextLocation.clone().add(x, y, z).getBlock().getType();

                                            if (adjacentBlockMaterial != Material.AIR) {
                                                // The FallingBlock is about to collide with a non-air block, remove it.
                                                block.remove();
                                                this.cancel();
                                                return;
                                            }
                                        }
                                    }
                                }
                            }
                        }.runTaskTimer(plugin, 0L, 1L);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!block.isValid()) {
                                    this.cancel();
                                    return;
                                }
                                block.setVelocity(velocity);
                            }
                        }.runTaskTimer(plugin, 1L, 1L);  // Run every tick (20 ticks = 1 second)

                        // check if reached range
                        Location originalLocation = block.getLocation();
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!block.isValid()) {
                                    this.cancel();
                                }
                                if (block.getLocation().distance(originalLocation) > 10.0) {
                                    block.remove();
                                    this.cancel();
                                }
                            }
                        }.runTaskTimer(plugin, 0L, 20L);
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);

    }

}
