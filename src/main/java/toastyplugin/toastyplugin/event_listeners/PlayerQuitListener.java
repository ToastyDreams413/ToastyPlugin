package toastyplugin.toastyplugin.event_listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitTask;
import toastyplugin.toastyplugin.custom_classes.Rank;
import toastyplugin.toastyplugin.ToastyPlugin;

import java.util.List;

public class PlayerQuitListener implements Listener {

    private final ToastyPlugin plugin;

    public PlayerQuitListener(ToastyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BukkitTask task = plugin.playerTasks.get(player.getUniqueId());

        // Cancel the task
        if (task != null) {
            task.cancel();
            plugin.playerTasks.remove(player.getUniqueId());
        }

    }
}