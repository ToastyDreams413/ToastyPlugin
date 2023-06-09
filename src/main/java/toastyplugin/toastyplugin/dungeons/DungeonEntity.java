package toastyplugin.toastyplugin.dungeons;

import org.bukkit.Location;
import toastyplugin.toastyplugin.mobs.CustomZombie;

public class DungeonEntity {

    private Location location;
    private String name;

    public DungeonEntity() {
        name = "";
        location = null;
    }


    public DungeonEntity(Location location, String name) {
        this.location = location;
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void spawnMob() {
        if (name.equals("Test Zombie")) {
            CustomZombie.spawnCustomZombie(location);
        }
    }

}
