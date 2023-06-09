package toastyplugin.toastyplugin.event_listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        // Get the spawn location, replace "worldName" with the name of your world
        World world = Bukkit.getWorld("world");
        Location spawnLocation = world.getSpawnLocation();
        spawnLocation.add(0.5, 0, 0.5);

        // Set the respawn location
        event.setRespawnLocation(spawnLocation);
    }

}
