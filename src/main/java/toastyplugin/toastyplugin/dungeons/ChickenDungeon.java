package toastyplugin.toastyplugin.dungeons;

import org.bukkit.*;
import org.bukkit.scheduler.BukkitRunnable;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.mobs.CustomZombie;

import java.util.HashMap;
import java.util.Vector;

public class ChickenDungeon {

    private static boolean[] roomsCreated;
    private static HashMap<Integer, Material[][][]> roomMaps = new HashMap<>();
    private static HashMap<Integer, Vector<DungeonEntity>> roomEntities = new HashMap<>();

    private final ToastyPlugin plugin;

    private int lengthToBoss;
    private DungeonDimensions startCoords;
    private Vector<DungeonDimensions> roomDimensions = new Vector<>();



    public ChickenDungeon(ToastyPlugin plugin, int startX, int startY, int startZ) {

        this.plugin = plugin;

        startCoords = new DungeonDimensions(startX, startY, startZ);
        roomsCreated = new boolean[6];
        for (int i = 0; i < 6; i++) {
            roomsCreated[i] = false;
        }

        lengthToBoss = (int) (Math.random() * 2) + 3;
        String correctPath = generateCorrectPath(lengthToBoss);
        System.out.println("Correct Path: " + correctPath);
        createDungeon(correctPath);

    }



    public void createDungeon(String path) {
        int curX = startCoords.getStartX();
        int curY = startCoords.getStartY();
        int curZ = startCoords.getStartZ();
        int prevRoomType = 0;
        for (int i = 0; i < path.length(); i++) {
            int roomType = (int) Math.random();
            int[] currentDimensions = getDimensions(roomType);
            if (i != 0) {
                int[] prevDimensions = getDimensions(prevRoomType);
                if (path.charAt(i) == 'U') {
                    curX = curX + prevDimensions[0] / 2 - currentDimensions[0] / 2;
                    curZ += prevDimensions[2];
                }
                else if (path.charAt(i) == 'D') {
                    curX = curX + prevDimensions[0] / 2 - currentDimensions[0] / 2;
                    curZ -= prevDimensions[2];
                }
                else if (path.charAt(i) == 'L') {
                    curX += prevDimensions[0];
                    curZ = curZ + prevDimensions[2] / 2 - currentDimensions[2] / 2;
                }
                else if (path.charAt(i) == 'R') {
                    curX -= prevDimensions[0];
                    curZ = curZ + prevDimensions[2] / 2 - currentDimensions[2] / 2;
                }
            }
            System.out.println("ITER: " + i + " curX: " + curX + " curY: " + curY + " curZ: " + curZ);
            createRoom(roomType, path.charAt(i), curX, curY, curZ);
            prevRoomType = roomType;
            roomDimensions.add(new DungeonDimensions(curX, curY, curZ, currentDimensions[0], currentDimensions[1], currentDimensions[2]));
        }
    }



    public String generateCorrectPath(int numRooms) {

        String correctPathString = " ";
        HashMap<Integer, Character> numToDir = new HashMap<Integer, Character>() {{
            put(0, 'U');
            put(1, 'D');
            put(2, 'L');
            put(3, 'R');
        }};

        HashMap<Character, Character> oppositeDirs = new HashMap<Character, Character>() {{
            put('D', 'U');
            put('U', 'D');
            put('R', 'L');
            put('L', 'R');
        }};

        for (int i = 0; i < numRooms; i++) {

            int curRoomNum = (int) (Math.random() * 4);
            if (numToDir.get(curRoomNum) == oppositeDirs.get(correctPathString.charAt(correctPathString.length() - 1))) {
                curRoomNum += (int) (Math.random() * 3) + 1;
                curRoomNum %= 4;
            }
            correctPathString += numToDir.get(curRoomNum);
        }

        return correctPathString;

    }



    public void createRoom(int roomTypeNum, char direction, int startX, int startY, int startZ) {

        final int xSize;
        final int ySize;
        final int zSize;

        World world = Bukkit.getWorld("world_dungeons");

        if (roomTypeNum == 0) {
            xSize = 10;
            ySize = 6;
            zSize = 10;
            if (!roomsCreated[0]) {
                Material[][][] room1 = new Material[10][6][10];

                // initialize everything as air
                for (int x = 0; x < xSize; x++) {
                    for (int y = 0; y < ySize; y++) {
                        for (int z = 0; z < zSize; z++) {
                            room1[x][y][z] = Material.AIR;
                        }
                    }
                }

                // set floor and ceiling of the room
                for (int x = 0; x < xSize; x++) {
                    for (int z = 0; z < zSize; z++) {
                        room1[x][0][z] = Material.GRASS_BLOCK;
                        room1[x][ySize - 1][z] = Material.ACACIA_PLANKS;
                    }
                }

                // set walls of the room
                for (int i = 0; i < 10; i++) {
                    for (int y = 1; y < ySize - 1; y++) {
                        room1[i][y][0] = Material.DIRT;
                        room1[i][y][zSize - 1] = Material.DIRT;
                        room1[0][y][i] = Material.DIRT;
                        room1[xSize - 1][y][i] = Material.DIRT;
                    }
                }

                // add some extra blocks for decoration
                room1[8][4][8] = Material.ACACIA_LEAVES;
                room1[7][4][8] = Material.ACACIA_LEAVES;
                room1[8][3][8] = Material.ACACIA_LEAVES;

                roomMaps.put(0, room1);

                Vector<DungeonEntity> dungeonEntities = new Vector<>();
                dungeonEntities.add(new DungeonEntity());
                roomEntities.put(0, dungeonEntities);

                roomsCreated[0] = true;
            }

        }
        else {
            xSize = 10;
            ySize = 6;
            zSize = 10;
        }

        new BukkitRunnable() {
            private int y = 0;
            @Override
            public void run() {
                Material[][][] roomLayout = roomMaps.get(roomTypeNum);
                for (int x = 0; x < xSize; x++) {
                    for (int z = 0; z < zSize; z++) {
                        Location blockLocation = new Location(world, startX + x, startY + y, startZ + z);
                        blockLocation.getBlock().setType(roomLayout[x][y][z]);
                    }
                }

                y++;

                if (y >= ySize) {
                    for (DungeonEntity dungeonEntity : roomEntities.get(roomTypeNum)) {
                        dungeonEntity.spawnMob();
                    }

                    if (direction == 'U') {
                        Location blockLocation = new Location(world, startX + xSize / 2, startY + 1, startZ);
                        // System.out.println("Direction: " + direction + " START: (" + startX + ", " + startY + ", " + startZ + ") zSize: " + zSize + "\nCurrent Doorway Locations:\n(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(world, startX + xSize / 2, startY + 1, startZ - 1);
                        // System.out.println("(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(world, startX + xSize / 2, startY + 2, startZ);
                        // System.out.println("(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(world, startX + xSize / 2, startY + 2, startZ - 1);
                        // System.out.println("(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                    }
                    else if (direction == 'D') {
                        Location blockLocation = new Location(world, startX + xSize / 2, startY + 1, startZ + zSize);
                        // System.out.println("Direction: " + direction + " START: (" + startX + ", " + startY + ", " + startZ + ") zSize: " + zSize + "\nCurrent Doorway Locations:\n(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(world, startX + xSize / 2, startY + 1, startZ + zSize - 1);
                        // System.out.println("(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(world, startX + xSize / 2, startY + 2, startZ + zSize);
                        // System.out.println("(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(world, startX + xSize / 2, startY + 2, startZ + zSize - 1);
                        // System.out.println("(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                    }
                    else if (direction == 'L') {
                        Location blockLocation = new Location(world, startX, startY + 1, startZ + zSize / 2);
                        // System.out.println("Direction: " + direction + " START: (" + startX + ", " + startY + ", " + startZ + ") zSize: " + zSize + "\nCurrent Doorway Locations:\n(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(world, startX - 1, startY + 1, startZ + zSize / 2);
                        // System.out.println("(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(world, startX, startY + 2, startZ + zSize / 2);
                        // System.out.println("(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(world, startX - 1, startY + 2, startZ + zSize / 2);
                        // System.out.println("(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                    }
                    else if (direction == 'R') {
                        Location blockLocation = new Location(world, startX + xSize, startY + 1, startZ + zSize / 2);
                        // System.out.println("Direction: " + direction + " START: (" + startX + ", " + startY + ", " + startZ + ") zSize: " + zSize + "\nCurrent Doorway Locations:\n(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(world, startX + xSize - 1, startY + 1, startZ + zSize / 2);
                        // System.out.println("(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(world, startX + xSize, startY + 2, startZ + zSize / 2);
                        // System.out.println("(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                        blockLocation = new Location(world, startX + xSize - 1, startY + 2, startZ + zSize / 2);
                        // System.out.println("(" + blockLocation.getBlockX() + ", " + blockLocation.getBlockY() + ", " + blockLocation.getBlockZ() + ")");
                        blockLocation.getBlock().setType(Material.AIR);
                    }

                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L); // Run every tick



    }



    public int[] getDimensions(int roomType) {
        int[] res = new int[3];
        if (roomType == 0) {
            res[0] = 10;
            res[1] = 6;
            res[2] = 10;
        }
        return res;
    }


    public Vector<DungeonDimensions> getRoomDimensions() {
        return roomDimensions;
    }

}
