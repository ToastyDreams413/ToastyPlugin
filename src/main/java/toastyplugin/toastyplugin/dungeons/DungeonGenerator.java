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
import java.util.Map;
import java.util.Vector;

public class DungeonGenerator {

    public static Map<String, Vector<Vector<DungeonDimensions>>> dungeonsToClear = new HashMap<>();

    public static Location generateDungeon(ToastyPlugin plugin, String dungeonName) {
        if (dungeonName.equals("Chicken's Den")) {
            if (dungeonsToClear.get(dungeonName) == null) {
                dungeonsToClear.put(dungeonName, new Vector<>());
            }
            int startX = 80 * (dungeonsToClear.get(dungeonName).size() + 1);
            int startY = -55;
            int startZ = 50;
            return generateChickenDen(plugin, startX, startY, startZ);
        }
        return null;
    }

    public static Location generateChickenDen(ToastyPlugin plugin, int startX, int startY, int startZ) {

        ChickenDungeon chickenDungeon = new ChickenDungeon(plugin, startX, startY, startZ);
        return new Location(Bukkit.getWorld("world_dungeons"), startX + 4.5, startY + 1, startZ + 4.5);

    }

    public static void clearDungeons() {

        World world = Bukkit.getWorld("world_dungeons");

        for (Vector<Vector<DungeonDimensions>> dungeonVector : dungeonsToClear.values()) {
            for (Vector<DungeonDimensions> roomVector : dungeonVector) {
                for (DungeonDimensions room : roomVector) {
                    for (int x = 0; x < room.getXSize(); x++) {
                        for (int y = 0; y < room.getYSize(); y++) {
                            for (int z = 0; z < room.getZSize(); z++) {
                                Location blockLocation = new Location(world, x + room.getStartX(), y + room.getStartY(), z + room.getStartZ());
                                blockLocation.getBlock().setType(Material.AIR);
                            }
                        }
                    }
                }
            }
            dungeonsToClear.remove(dungeonVector);
        }

        for (Entity entity : world.getEntities()) {
            if (entity instanceof LivingEntity && !(entity instanceof Player)) {
                entity.remove();
            }
        }

    }

}
