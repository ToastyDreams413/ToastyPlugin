package toastyplugin.toastyplugin.event_listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.data.PlayerDataManager;

public class PlayerJoinListener implements Listener {

    private final ToastyPlugin plugin;

    public PlayerJoinListener(ToastyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String uuid = player.getUniqueId().toString();

        if (!plugin.getConfig().isSet(uuid + ".joined")) {
            PlayerDataManager.handleNewPlayer(plugin, player, uuid);
        }
        PlayerDataManager.handleReturningPlayer(plugin, player, uuid);
    }

}