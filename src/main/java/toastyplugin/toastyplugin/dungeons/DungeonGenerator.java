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

    private static Map<String, Vector<Vector<DungeonDimensions>>> dungeonsToClear = new HashMap<>();

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
        dungeonsToClear.get("Chicken's Den").add(chickenDungeon.getRoomDimensions());
        return new Location(Bukkit.getWorld("world_dungeons"), startX + 2, startY + 2, startZ + 2);

    }

    public static void clearDungeons() {

        World world = Bukkit.getWorld("world_dungeons");

        for (Vector<Vector<DungeonDimensions>> dungeonVector : dungeonsToClear.values()) {
            for (Vector<DungeonDimensions> roomVector : dungeonVector) {
                for (DungeonDimensions room : roomVector) {
                    for (int x = room.getStartX(); x < room.getXSize(); x++) {
                        for (int y = room.getStartY(); y < room.getYSize(); y++) {
                            for (int z = room.getStartZ(); z < room.getZSize(); z++) {
                                Location blockLocation = new Location(world, x, y, z);
                                blockLocation.getBlock().setType(Material.AIR);
                            }
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
