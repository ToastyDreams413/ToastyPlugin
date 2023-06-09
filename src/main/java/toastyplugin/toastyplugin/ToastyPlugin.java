package toastyplugin.toastyplugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitTask;
import toastyplugin.toastyplugin.commands.*;
import toastyplugin.toastyplugin.dungeons.DungeonEntity;
import toastyplugin.toastyplugin.dungeons.DungeonGenerator;
import toastyplugin.toastyplugin.event_listeners.*;
import toastyplugin.toastyplugin.items.weapons.CustomBow;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ToastyPlugin extends JavaPlugin implements CommandExecutor {

    private FileConfiguration config;

    // <entity UUID, <player UUID, total damage dealt>>
    public Map<UUID, HashMap<UUID, Double>> playerDamage = new ConcurrentHashMap<>();
    public Map<UUID, BukkitTask> playerTasks = new ConcurrentHashMap<>();

    // <player UUID, <entity death location, saved inventory (loot)>>
    public Map<UUID, Vector<HashMap<Location, Inventory>>> lootInventories = new ConcurrentHashMap<>();
    public Map<UUID, LinkedList<ItemStack>> stashItems = new ConcurrentHashMap<>();
    public Map<UUID, Vector<Location>> removeLootChestTasks = new ConcurrentHashMap<>();
    public Map<ArmorStand, Double> aliveMobs = new ConcurrentHashMap<>();
    public Map<ArmorStand, Vector<BukkitTask>> aliveMobTasks = new ConcurrentHashMap<>();
    public Vector<Player> joinedPlayers = new Vector<>();



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
        this.getCommand("pickupstash").setExecutor(new PickUpStashCommand(this));
        this.getCommand("clearblocks").setExecutor(new ClearBlocksCommand(this));
        this.getCommand("cleardungeons").setExecutor(new ClearDungeonsCommand(this));

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
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
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

        World world = Bukkit.getWorld("world");
        world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
        World world2 = Bukkit.getWorld("world_dungeons");
        world2.setGameRule(GameRule.NATURAL_REGENERATION, false);
        world2.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);

        getLogger().info("ToastyPlugin has been enabled!");
    }

    @Override
    public void onDisable() {
        // clear dungeons
        DungeonGenerator.clearDungeons();

        // remove all custom mobs
        for (ArmorStand armorStand : aliveMobs.keySet()) {
            armorStand.remove();
        }

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                // remove all custom armor stands that are used for text/timers/etc
                if (entity instanceof ArmorStand && entity.getCustomName() != null) {
                    entity.remove();
                }

                // remove custom projectiles
                if (entity instanceof FallingBlock) {
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
        }

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
