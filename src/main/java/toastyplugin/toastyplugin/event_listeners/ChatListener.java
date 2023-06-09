package toastyplugin.toastyplugin.event_listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import toastyplugin.toastyplugin.custom_classes.Rank;
import toastyplugin.toastyplugin.ToastyPlugin;

import java.util.List;

public class ChatListener implements Listener {

    private final ToastyPlugin plugin;

    public ChatListener(ToastyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        String uuid = "" + player.getUniqueId();

        Rank rank = new Rank(plugin.getConfig().getString(uuid + ".rank"));

        if (rank.getName().equals("member")) {
            event.setFormat(player.getName() + ": " + event.getMessage());
        }
        else {
            String prefix = rank.getDisplayName() + ChatColor.RESET;
            event.setFormat(prefix + " " + player.getName() + ": " + event.getMessage());
        }
    }
}