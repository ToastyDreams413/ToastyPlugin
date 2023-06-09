package toastyplugin.toastyplugin;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;
import toastyplugin.toastyplugin.commands.*;
import toastyplugin.toastyplugin.dungeons.DungeonGenerator;
import toastyplugin.toastyplugin.event_listeners.*;
import toastyplugin.toastyplugin.items.weapons.CustomBow;

import java.util.*;

public final class ToastyPlugin extends JavaPlugin implements CommandExecutor {

    private FileConfiguration config;

    public HashMap<UUID, Vector<UUID>> lootChests = new HashMap<>();
    public HashMap<UUID, HashMap<UUID, Double>> playerDamage = new HashMap<>();
    public HashMap<Location, UUID> mobDeathLocations = new HashMap<>();
    public HashMap<UUID, BukkitTask> playersActionBarTasks = new HashMap<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();

        // commands
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        this.getCommand("warp").setExecutor(new WarpCommand(this));
        this.getCommand("spawnmob").setExecutor(new SpawnMobCommand(this));
        this.getCommand("setrank").setExecutor(new SetRankCommand(this));
        this.getCommand("giveitem").setExecutor(new GiveItemCommand(this));
        this.getCommand("dungeon").setExecutor(new GenerateDungeonCommand(this));

        // event listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        getServer().getPluginManager().registerEvents(new MobDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new InteractionListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityDamagedListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldSettingListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new CustomBow(this), this);

        if (Bukkit.getWorld("world_dungeons") == null) {
            // The world doesn't exist, so you can create it
            WorldCreator worldCreator = new WorldCreator("world_dungeons");
            worldCreator.environment(World.Environment.NORMAL);
            worldCreator.type(WorldType.FLAT);
            worldCreator.generateStructures(false);
            Bukkit.createWorld(worldCreator);
        }

        getLogger().info("ToastyPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                // remove all custom armor stands that are used for text/timers/etc
                if (entity instanceof ArmorStand && entity.getCustomName() != null) {
                    entity.remove();
                }

                // remove all dropped items
                if (entity instanceof Item) {
                    entity.remove();
                }

                // remove all arrows
                if (entity instanceof Arrow) {
                    entity.remove();
                }
            }

            // remove all loot chests
            for (Location chestLocation : mobDeathLocations.keySet()) {
                if (chestLocation.getBlock().getType() == Material.CHEST) {
                    chestLocation.getBlock().setType(Material.AIR);
                }
            }
        }

        DungeonGenerator.clearDungeons();

        getLogger().info("ToastyPlugin has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("hello")) {
            sender.sendMessage("Hello, Minecraft!");
            return true;
        }
        return false;
    }
}
