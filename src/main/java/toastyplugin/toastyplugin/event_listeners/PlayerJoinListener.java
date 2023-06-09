package toastyplugin.toastyplugin.event_listeners;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;
import toastyplugin.toastyplugin.custom_classes.Rank;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.items.weapons.CustomBow;
import toastyplugin.toastyplugin.items.weapons.CustomSword;
import toastyplugin.toastyplugin.items.weapons.CustomWand;
import toastyplugin.toastyplugin.mobs.CustomZombie;

import java.util.List;

public class PlayerJoinListener implements Listener {

    private final ToastyPlugin plugin;

    public PlayerJoinListener(ToastyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String uuid = "" + player.getUniqueId();

        // if it is not their first time joining
        if (plugin.getConfig().isSet(uuid + ".firstJoin")) {
            plugin.getConfig().set(uuid + ".firstJoin", false);
            // do something
        }
        else {
            plugin.getConfig().set(uuid + ".firstJoin", true);
            if (player.getName().equals("oClouded")) {
                plugin.getConfig().set(uuid + ".rank", "head admin");
            }
            else if (player.getName().equals("ToastyDreams")) {
                plugin.getConfig().set(uuid + ".rank", "wifey");
            }
            else {
                plugin.getConfig().set(uuid + ".rank", "member");
            }
            plugin.getConfig().set(uuid + ".shillings", 0);
            plugin.getConfig().set(uuid + ".maxHealth", 20);
            plugin.getConfig().set(uuid + ".maxMana", 10);
            player.getInventory().clear();
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Welcome to Toasty's Server! Don't know what to do? Use /tutorial to go through a tutorial, or /wiki to view the in-game wiki.");
            ItemStack customSword = CustomSword.createCustomSword("Starter Sword");
            player.getInventory().addItem(customSword);
            ItemStack customWand = CustomWand.createCustomWand("Starter Wand");
            player.getInventory().addItem(customWand);
            ItemStack customBow = CustomBow.createCustomBow("Starter Bow");
            player.getInventory().addItem(customBow);
        }

        player.setMetadata("health", new FixedMetadataValue(plugin, plugin.getConfig().getInt(uuid + ".maxHealth")));
        player.setMetadata("mana", new FixedMetadataValue(plugin, plugin.getConfig().getInt(uuid + ".maxMana")));

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                List<MetadataValue> values = player.getMetadata("mana");
                int mana = values.get(0).asInt();
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.BLUE + "Mana: " + mana));

                ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
                Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
                Objective objective = scoreboard.registerNewObjective("shillings", "dummy", "Toasty Dungeons");

                objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                Score topPadding = objective.getScore("   ");
                topPadding.setScore(3);

                int shillings = plugin.getConfig().getInt(uuid + ".shillings");

                Score score = objective.getScore("Shillings: " + shillings);
                score.setScore(2);

                Score bottomPadding = objective.getScore(" ");
                bottomPadding.setScore(1); // Set score to control order

                player.setScoreboard(scoreboard);
            }
        }.runTaskTimer(plugin, 0L, 20L); // 20 ticks = 1 second
        plugin.playersActionBarTasks.put(player.getUniqueId(), task);

        if (plugin.getConfig().isSet("world.spawn")) {
            World world = Bukkit.getWorld("world");
            double x = plugin.getConfig().getDouble("world.spawn.x");
            double y = plugin.getConfig().getDouble("world.spawn.y");
            double z = plugin.getConfig().getDouble("world.spawn.z");
            float yaw = (float) plugin.getConfig().getDouble("world.spawn.yaw");
            float pitch = (float) plugin.getConfig().getDouble("world.spawn.pitch");

            Location location = new Location(world, x, y, z, yaw, pitch);
            player.teleport(location);
        }
    }

}