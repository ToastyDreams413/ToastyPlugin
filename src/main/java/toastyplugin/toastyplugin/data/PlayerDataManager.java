package toastyplugin.toastyplugin.data;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.*;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.items.weapons.CustomSword;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

public class PlayerDataManager {

    public static void handleNewPlayer(ToastyPlugin plugin, Player player, String uuid) {

        plugin.getConfig().set(uuid + ".joined", true);

        if (player.getName().equals("oClouded")) {
            plugin.getConfig().set(uuid + ".rank", "head admin");
        }
        else if (player.getName().equals("ToastyDreams")) {
            plugin.getConfig().set(uuid + ".rank", "admin");
        }
        else {
            plugin.getConfig().set(uuid + ".rank", "member");
        }
        plugin.getConfig().set(uuid + ".selectedClass", "Warrior");
        plugin.getConfig().set(uuid + ".level", 1);
        plugin.getConfig().set(uuid + ".exp", 0);
        plugin.getConfig().set(uuid + ".might", 0);
        plugin.getConfig().set(uuid + ".fortitude", 0);
        plugin.getConfig().set(uuid + ".reflex", 0);
        plugin.getConfig().set(uuid + ".health", 10);
        plugin.getConfig().set(uuid + ".mana", 10);
        plugin.getConfig().set(uuid + ".sorcery", 0);
        plugin.getConfig().set(uuid + ".swordsmanship", 0);
        plugin.getConfig().set(uuid + ".martialAegis", 0);
        plugin.getConfig().set(uuid + ".arcaneAegis", 0);
        plugin.getConfig().set(uuid + ".projectileAegis", 0);
        plugin.getConfig().set(uuid + ".shillings", 0);
        plugin.getConfig().set(uuid + ".cosmicCubes", 0);
        plugin.getConfig().set(uuid + ".luminas", 0);
        plugin.getConfig().set(uuid + ".obscuras", 0);
        plugin.getConfig().set(uuid + ".numUnlockedDungeons", 1);
        plugin.getConfig().set(uuid + ".unlockedDungeon1", "Chicken's Den");
        plugin.getConfig().set(uuid + ".numUnlockedClasses", 1);
        plugin.getConfig().set(uuid + ".unlockedClass1", "Warrior");

        plugin.saveConfig();

        player.sendMessage(ChatColor.LIGHT_PURPLE + "Welcome to Toasty's Server! Don't know what to do? Use /tutorial to go through a tutorial, or /wiki to view the in-game wiki.");
        ItemStack customSword = CustomSword.createCustomSword("Starter Sword");
        player.getInventory().addItem(customSword);

    }



    public static void handleReturningPlayer(ToastyPlugin plugin, Player player, String uuid) {

        // store data in the current metadata for immediate use in this playing session
        int health = plugin.getConfig().getInt(uuid + ".health");
        int mana = plugin.getConfig().getInt(uuid + ".mana");
        AttributeInstance maxHealthAttribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        maxHealthAttribute.setBaseValue(health);
        player.setHealth(health);

        player.setMetadata("maxHealth", new FixedMetadataValue(plugin, health));
        player.setMetadata("mana", new FixedMetadataValue(plugin, mana));
        player.setMetadata("maxMana", new FixedMetadataValue(plugin, mana));
        player.setMetadata("level", new FixedMetadataValue(plugin, plugin.getConfig().getInt(uuid + ".level")));
        player.setMetadata("exp", new FixedMetadataValue(plugin, plugin.getConfig().getInt(uuid + ".exp")));

        plugin.lootInventories.put(player.getUniqueId(), new Vector<>());
        plugin.stashItems.put(player.getUniqueId(), new LinkedList<>());

        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                player.setFoodLevel(20);
                int mana = (Integer) getMetadata(player, "mana");
                int maxMana = (Integer) getMetadata(player, "maxMana");
                String manaBar = ChatColor.AQUA + "" + ChatColor.BOLD + "Mana: " + mana + "/" + maxMana;
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(manaBar));

                ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
                Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
                Objective objective = scoreboard.registerNewObjective("shillings", "dummy", "Toasty Dungeons");

                objective.setDisplaySlot(DisplaySlot.SIDEBAR);

                Score empty1 = objective.getScore("   ");
                empty1.setScore(6);

                int shillings = plugin.getConfig().getInt(uuid + ".shillings");

                Score shillingsDisplay = objective.getScore("Shillings: " + shillings);
                shillingsDisplay.setScore(5);

                Score empty2 = objective.getScore("  ");
                empty2.setScore(4);

                int level = (Integer) getMetadata(player, "level");
                int exp = (Integer) getMetadata(player, "exp");
                int expToNextLevel = GeneralData.EXP_TO_NEXT_LEVEL.get(level);
                Score levelDisplay = objective.getScore("Level: " + level);
                levelDisplay.setScore(3);
                Score expDisplay = objective.getScore("Exp: " + exp + "/" + expToNextLevel);
                expDisplay.setScore(2);

                Score empty3 = objective.getScore(" ");
                empty3.setScore(1);

                player.setScoreboard(scoreboard);
            }
        }.runTaskTimer(plugin, 0L, 10L);
        plugin.playerTasks.put(player.getUniqueId(), task);

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



    public static Object getMetadata(Player player, String key) {
        List<MetadataValue> values = player.getMetadata(key);
        if (values.isEmpty()) {
            return null;
        }
        return values.get(0).value();
    }

}
