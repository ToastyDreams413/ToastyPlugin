package toastyplugin.toastyplugin.mobs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import toastyplugin.toastyplugin.ToastyPlugin;

import java.util.HashMap;
import java.util.UUID;

public class MotherHen {

    public static void spawnMotherHen(ToastyPlugin plugin, Location location, int range, double projectileRadius) {
        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setVisible(true);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
        // armorStand.setInvulnerable(true);

        ItemStack cobwebItem = new ItemStack(Material.COBWEB);
        ItemMeta meta = cobwebItem.getItemMeta();
        meta.setCustomModelData(1);
        cobwebItem.setItemMeta(meta);

        armorStand.getEquipment().setHelmet(cobwebItem);
        plugin.aliveMobs.put(armorStand, 100.0);

        BukkitTask facePlayerTask = new BukkitRunnable() {
            @Override
            public void run() {
                Player closestPlayer = null;
                double closestPlayerDistance = -1;
                for (Player player : plugin.joinedPlayers) {
                    double currentDistance = Math.abs(player.getLocation().distance(armorStand.getLocation()));
                    if (currentDistance < range && (currentDistance < closestPlayerDistance || closestPlayerDistance == -1)) {
                        closestPlayer = player;
                        closestPlayerDistance = currentDistance;
                    }
                }

                if (closestPlayer == null) {
                    return;
                }

                // calculate the direction the ArmorStand needs to face to look at the player
                Vector direction = closestPlayer.getLocation().toVector().subtract(armorStand.getLocation().toVector());

                // calculate the yaw and pitch
                float yaw = (float)Math.toDegrees(Math.atan2(direction.getZ(), direction.getX())) - 90;
                float pitch = (float)Math.toDegrees(Math.atan2(direction.getY(), Math.sqrt(Math.pow(direction.getX(), 2) + Math.pow(direction.getZ(), 2))));

                // set the ArmorStand's rotation to face the player
                armorStand.setRotation(yaw, -pitch);
            }

        }.runTaskTimer(plugin, 0L, 1L);



        BukkitTask shootTask = new BukkitRunnable() {
            @Override
            public void run() {
                // check for the closest player within 10 blocks
                Player closestPlayer = null;
                double closestPlayerDistance = -1;
                HashMap<UUID, Double> playerDistances = new HashMap<>();
                for (Player player : plugin.joinedPlayers) {
                    double currentDistance = Math.abs(player.getLocation().distance(armorStand.getLocation()));
                    if (currentDistance < range && (currentDistance < closestPlayerDistance || closestPlayerDistance == -1)) {
                        closestPlayer = player;
                        closestPlayerDistance = currentDistance;
                    }
                }

                if (closestPlayer == null) {
                    return;
                }

                // if the closest player is on the mob, don't actually shoot, just damage the player
                if (closestPlayer.getLocation().getBlockX() == armorStand.getLocation().getBlockX() && closestPlayer.getLocation().getBlockY() == armorStand.getLocation().getBlockY() && closestPlayer.getLocation().getBlockZ() == armorStand.getLocation().getBlockZ()) {
                    closestPlayer.damage(1.0);
                    return;
                }

                // shoot at the nearest player using a FallingBlock
                FallingBlock block = armorStand.getWorld().spawnFallingBlock(
                        armorStand.getLocation().add(0, 0.975, 0),
                        Material.DIAMOND_BLOCK.createBlockData()
                );

                // configure the FallingBlock
                block.setCustomName("projectile");
                block.setCustomNameVisible(false);
                block.setGravity(false);
                block.setDropItem(false);
                block.setHurtEntities(true);
                Vector velocity = closestPlayer.getLocation().add(0, 0.975, 0).subtract(armorStand.getLocation().add(0, 0.975, 0)).toVector().normalize().multiply(0.3);
                block.setVelocity(velocity);
                Location centeredBlockLocation = block.getLocation().add(0, 0.5, 0);

                // check for nearby players to damage them
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        block.setVelocity(velocity);

                        // move the armor stand randomly
                        double x = (Math.random() - 0.5) / 10;
                        double z = (Math.random() - 0.5) / 10;
                        armorStand.teleport(armorStand.getLocation().add(x, 0, z));

                        if (!block.isValid()) {
                            this.cancel();
                            return;
                        }

                        for (Player player : plugin.joinedPlayers) {
                            if (Math.abs(player.getLocation().add(0, 0.975, 0).distance(centeredBlockLocation)) < projectileRadius) {
                                player.damage(1.0);
                                block.remove();
                                this.cancel();
                            }
                        }

                        // if it is about to collide with something, remove it
                        Location nextLocation = centeredBlockLocation.add(block.getVelocity());
                        if (nextLocation.getBlock().getType() != Material.AIR || centeredBlockLocation.getBlock().getType() != Material.AIR) {
                            block.remove();
                            this.cancel();
                        }
                    }
                }.runTaskTimer(plugin, 0L, 1L);

                // check if reached range
                Location originalLocation = block.getLocation();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!block.isValid()) {
                            this.cancel();
                            return;
                        }
                        if (Math.abs(block.getLocation().distance(originalLocation)) > range) {
                            block.remove();
                            this.cancel();
                            return;
                        }
                    }
                }.runTaskTimer(plugin, 0L, 20L);
            }

        }.runTaskTimer(plugin, 0L, 20L);

        java.util.Vector<BukkitTask> curTasks = plugin.aliveMobTasks.getOrDefault(armorStand, new java.util.Vector<>());
        curTasks.add(facePlayerTask);
        curTasks.add(shootTask);
        plugin.aliveMobTasks.put(armorStand, curTasks);

    }

}
