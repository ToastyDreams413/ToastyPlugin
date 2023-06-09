package toastyplugin.toastyplugin.dungeons;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.mobs.CustomZombie;

import java.util.HashMap;
import java.util.Vector;

public class DungeonGenerator {

    private static HashMap<String, Vector<Dungeon>> dungeonInfo = new HashMap<>();

    public static Location generateDungeon(ToastyPlugin plugin, String dungeonName) {
        if (dungeonName.equals("Chicken's Den")) {
            if (dungeonInfo.get(dungeonName) == null) {
                dungeonInfo.put(dungeonName, new Vector<>());
            }
            int startX = 60 * (dungeonInfo.get(dungeonName).size() + 1);
            int startY = -55;
            int startZ = 50;
            dungeonInfo.get(dungeonName).add(new Dungeon(startX, startY, startZ, 10, 10, 10));
            return generateChickenDen(plugin, startX, startY, startZ);
        }
        return null;
    }

    public static Location generateChickenDen(ToastyPlugin plugin, int startX, int startY, int startZ) {

        Material[][][] dungeonLayout = new Material[10][10][10];

        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                for (int z = 0; z < 10; z++) {
                    dungeonLayout[x][y][z] = Material.AIR;
                }
            }
        }
        for (int x = 0; x < 10; x++) {
            for (int z = 0; z < 10; z++) {
                dungeonLayout[x][0][z] = Material.OAK_PLANKS;
                dungeonLayout[x][9][z] = Material.OAK_PLANKS;
            }
        }
        for (int i = 0; i < 10; i++) {
            for (int y = 1; y < 9; y++) {
                dungeonLayout[i][y][0] = Material.OAK_PLANKS;
                dungeonLayout[i][y][9] = Material.OAK_PLANKS;
                dungeonLayout[0][y][i] = Material.OAK_PLANKS;
                dungeonLayout[9][y][i] = Material.OAK_PLANKS;
            }
        }

        World world = Bukkit.getWorld("world_dungeons");
        new BukkitRunnable() {
            private int y = 0;
            World world = Bukkit.getWorld("world_dungeons");

            @Override
            public void run() {
                for (int x = 0; x < 10; x++) {
                    for (int z = 0; z < 10; z++) {
                        Location blockLocation = new Location(world, startX + x, startY + y, startZ + z);
                        blockLocation.getBlock().setType(dungeonLayout[x][y][z]);
                    }
                }

                y++; // Increase y after every layer

                // If all layers have been placed, spawn the zombie and cancel the task
                if (y >= 10) {
                    Location spawnBossLocation = new Location(world, startX + 5, startY + 2, startZ + 5);
                    CustomZombie.spawnCustomZombie(spawnBossLocation);
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L); // Run every tick

        return new Location(Bukkit.getWorld("world_dungeons"), startX + 2, startY + 2, startZ + 2);

    }

    public static void clearDungeons() {

        World world = Bukkit.getWorld("world_dungeons");

        for (Vector<Dungeon> dungeonVector : dungeonInfo.values()) {
            for (Dungeon dungeon : dungeonVector) {
                for (int x = dungeon.getStartX(); x < dungeon.getXSize(); x++) {
                    for (int y = dungeon.getStartY(); y < dungeon.getYSize(); y++) {
                        for (int z = dungeon.getStartZ(); z < dungeon.getZSize(); z++) {
                            Location blockLocation = new Location(world, x, y, z);
                            blockLocation.getBlock().setType(Material.AIR);
                        }
                    }
                }
            }
        }

        for (Entity entity : world.getEntities()) {
            if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                entity.remove();
            }
        }

    }

}
