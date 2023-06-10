package toastyplugin.toastyplugin.dungeons;

import org.bukkit.Location;
import toastyplugin.toastyplugin.mobs.CustomZombie;

public class DungeonEntity {

    private Location location;
    private String name;
    private int xOffset;
    private int yOffset;
    private int zOffset;

    public DungeonEntity() {
        name = "";
        location = null;
    }

    public DungeonEntity(String name) {
        this.name = name;
        location = null;
    }

    public DungeonEntity(String name, int xOffset, int yOffset, int zOffset) {
        this.name = name;
        location = null;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }

    public DungeonEntity(Location location, String name) {
        this.location = location;
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public int getXOffset() {
        return xOffset;
    }

    public int getYOffset() {
        return yOffset;
    }

    public int getZOffset() {
        return zOffset;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void spawnMob() {
        if (name.equals("Test Zombie")) {
            CustomZombie.spawnCustomZombie(location);
        }
    }

}
