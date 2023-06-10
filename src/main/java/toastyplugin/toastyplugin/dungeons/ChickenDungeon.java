package toastyplugin.toastyplugin.dungeons;

import org.bukkit.*;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.scheduler.BukkitRunnable;
import toastyplugin.toastyplugin.ToastyPlugin;
import toastyplugin.toastyplugin.mobs.CustomZombie;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ChickenDungeon {

    private static boolean[] roomsCreated;
    private static Map<Integer, Material[][][]> roomMaps = new HashMap<>();
    private static Map<Integer, Vector<DungeonEntity>> roomEntities = new HashMap<>();

    private final ToastyPlugin plugin;

    private int lengthToBoss;
    private DungeonDimensions startCoords;
    private Vector<DungeonDimensions> roomDimensions = new Vector<>();



    // create a new Chicken's Den dungeon
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



    // creates the dungeon based on the correct path
    // first it generates random rooms following the correct path
    // then it creates other random rooms / paths
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



    // creates a string that represents the correct path
    // from the start to the end of the dungeon
    public String generateCorrectPath(int numRooms) {

        String correctPathString = " ";
        Map<Integer, Character> numToDir = new HashMap<Integer, Character>() {{
            put(0, 'U');
            put(1, 'D');
            put(2, 'L');
            put(3, 'R');
        }};

        Map<Character, Character> oppositeDirs = new HashMap<Character, Character>() {{
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



    // creates a room based on a room type number, direction came from, and start coordinates (close bottom right corner)
    public void createRoom(int roomTypeNum, char direction, int startX, int startY, int startZ) {

        final int xSize;
        final int ySize;
        final int zSize;

        World world = Bukkit.getWorld("world_dungeons");

        // for each room, set the room size
        // then check if we have generated the map for this room before
        // if not, then generate it for the first time
        // otherwise, use the previously generated map to optimize runtime
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

        // using the room map, generate the actual room
        // one layer at a time, from bottom y to top y
        // to prevent lag spikes / crashing from generating many blocks at once
        new BukkitRunnable() {
            private int y = 0;
            @Override
            public void run() {
                Material[][][] roomLayout = roomMaps.get(roomTypeNum);
                for (int x = 0; x < xSize; x++) {
                    for (int z = 0; z < zSize; z++) {
                        Location blockLocation = new Location(world, startX + x, startY + y, startZ + z);
                        blockLocation.getBlock().setType(roomLayout[x][y][z]);

                        // prevent leaves from decaying
                        if (blockLocation.getBlock().getType() == Material.ACACIA_LEAVES) {
                            Leaves leaves = (Leaves) blockLocation.getBlock().getBlockData();
                            leaves.setPersistent(true);
                            blockLocation.getBlock().setBlockData(leaves);
                        }
                    }
                }

                y++;

                if (y >= ySize) {
                    for (DungeonEntity dungeonEntity : roomEntities.get(roomTypeNum)) {
                        dungeonEntity.spawnMob();
                    }

                    // make the doorway based on the direction this room is entered from
                    if (direction == 'U') {
                        int thisX = startX + xSize / 2 - 1;
                        int thisY = startY + 1;
                        int thisZ = startZ - 1;
                        for (int a = 0; a < 2; a++) {
                            for (int b = 0; b < 3; b++) {
                                for (int c = 0; c < 2; c++) {
                                    Location blockLocation = new Location(world, thisX + a, thisY + b, thisZ + c);
                                    blockLocation.getBlock().setType(Material.AIR);
                                }
                            }
                        }
                    }
                    else if (direction == 'D') {
                        int thisX = startX + xSize / 2 - 1;
                        int thisY = startY + 1;
                        int thisZ = startZ + zSize - 1;
                        for (int a = 0; a < 2; a++) {
                            for (int b = 0; b < 3; b++) {
                                for (int c = 0; c < 2; c++) {
                                    Location blockLocation = new Location(world, thisX + a, thisY + b, thisZ + c);
                                    blockLocation.getBlock().setType(Material.AIR);
                                }
                            }
                        }
                    }
                    else if (direction == 'L') {
                        int thisX = startX - 1;
                        int thisY = startY + 1;
                        int thisZ = startZ + zSize / 2 - 1;
                        for (int a = 0; a < 2; a++) {
                            for (int b = 0; b < 3; b++) {
                                for (int c = 0; c < 2; c++) {
                                    Location blockLocation = new Location(world, thisX + a, thisY + b, thisZ + c);
                                    blockLocation.getBlock().setType(Material.AIR);
                                }
                            }
                        }
                    }
                    else if (direction == 'R') {
                        int thisX = startX + xSize - 1;
                        int thisY = startY + 1;
                        int thisZ = startZ + zSize / 2 - 1;
                        for (int a = 0; a < 2; a++) {
                            for (int b = 0; b < 3; b++) {
                                for (int c = 0; c < 2; c++) {
                                    Location blockLocation = new Location(world, thisX + a, thisY + b, thisZ + c);
                                    blockLocation.getBlock().setType(Material.AIR);
                                }
                            }
                        }
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
