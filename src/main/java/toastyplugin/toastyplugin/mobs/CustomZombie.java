package toastyplugin.toastyplugin.mobs;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;

public class CustomZombie {

    public static void spawnCustomZombie(Location location) {
        Zombie zombie = (Zombie) location.getWorld().spawnEntity(location, EntityType.ZOMBIE);

        zombie.setNoDamageTicks(0);

        zombie.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(200.0);
        zombie.setHealth(40.0);
        zombie.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.5); // This is quite fast!

        zombie.setCustomName("Test Zombie");
    }

}
