package toastyplugin.toastyplugin.event_listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.gui.CustomInventoryHolder;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

public class MobDeathListener implements Listener {

    private final ToastyPlugin plugin;

    public MobDeathListener(ToastyPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onZombieDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Zombie) {
            Zombie zombie = (Zombie) event.getEntity();
            if (zombie.getCustomName() != null && zombie.getCustomName().equals("Test Zombie")) {
                event.getDrops().clear();

                Location deathLocation = event.getEntity().getLocation();

                int c = 1;
                for (UUID playerUniqueId : plugin.playerDamage.get(event.getEntity().getUniqueId()).keySet()) {
                    if (plugin.playerDamage.get(event.getEntity().getUniqueId()).get(playerUniqueId) > 0) {
                        spawnChest(event.getEntity(), Bukkit.getServer().getPlayer(playerUniqueId), deathLocation);
                    }
                }

            }
        }
    }



    private void spawnChest(Entity entity, Player player, Location location) {

        LivingEntity killedMob = (LivingEntity) entity;
        double damageDone = plugin.playerDamage.get(killedMob.getUniqueId()).getOrDefault(player.getUniqueId(), 0.0);
        if (damageDone <= 0) {
            return;
        }

        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.BLOCK_CHANGE);

        // Set the location
        packet.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));

        // Set the new block data
        BlockData blockData = Material.BLACK_WOOL.createBlockData();
        packet.getBlockData().write(0, WrappedBlockData.createData(blockData));

        // Send the packet to the player
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);

        Inventory lootInventory = Bukkit.createInventory(new CustomInventoryHolder("Loot"), 27, "Loot");
        int numDiamonds = (int) (Math.random() * 10) + 1;
        lootInventory.addItem(new ItemStack(Material.DIAMOND, numDiamonds));

        Vector<HashMap<Location, Inventory>> currentLootVector = plugin.lootInventories.getOrDefault(player.getUniqueId(), new Vector<>());

        HashMap<Location, Inventory> currentLootInventory = new HashMap<>();
        Location toInteractLocation = new Location(player.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        currentLootInventory.put(toInteractLocation, lootInventory);
        currentLootVector.add(currentLootInventory);
        plugin.lootInventories.put(player.getUniqueId(), currentLootVector);

        Vector<Location> toRemoveLocations = plugin.removeLootChestTasks.getOrDefault(player.getUniqueId(), new Vector<>());
        toRemoveLocations.add(location);
        plugin.removeLootChestTasks.put(player.getUniqueId(), toRemoveLocations);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {
                PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.BLOCK_CHANGE);
                packet.getBlockPositionModifier().write(0, new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));

                // Set the new block data
                BlockData blockData = Material.AIR.createBlockData();
                packet.getBlockData().write(0, WrappedBlockData.createData(blockData));
                ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);

                // remove the stored location
                for (Iterator<Location> iterator = plugin.removeLootChestTasks.get(player.getUniqueId()).iterator(); iterator.hasNext();) {
                    Location curLocation = iterator.next();
                    if (curLocation.getWorld() == location.getWorld() && curLocation.getX() == location.getX() && curLocation.getY() == location.getY() && curLocation.getZ() == location.getZ()) {
                        iterator.remove();
                        break;
                    }
                }

            }
        }, 600L);

    }

}
