package toastyplugin.toastyplugin.dungeons;

import org.bukkit.*;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.scheduler.BukkitRunnable;
import toastyplugin.toastyplugin.ToastyPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class ChickenDungeon {

    private static boolean[] roomsCreated;
    private static Map<Integer, BlockInfo[][][]> roomMaps = new HashMap<>();
    private static Map<Integer, Vector<DungeonEntity>> roomEntities = new HashMap<>();
    private static Map<Integer, Integer> xSizes = new HashMap<>();
    private static Map<Integer, Integer> ySizes = new HashMap<>();
    private static Map<Integer, Integer> zSizes = new HashMap<>();

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
        // System.out.println("Correct Path: " + correctPath);
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
            int roomType = 0;
            if (i != 0) {
                roomType = (int) (Math.random() * 2);
            }
            int[] currentDimensions = getDimensions(roomType);
            if (i != 0) {
                int[] prevDimensions = getDimensions(prevRoomType);
                // System.out.println("curDimensions: (" + currentDimensions[0] + ", " + currentDimensions[1] + ", " + currentDimensions[2] + ")");
                // System.out.println("prevDimensions: (" + prevDimensions[0] + ", " + prevDimensions[1] + ", " + prevDimensions[2] + ")");
                if (path.charAt(i) == 'U') {
                    curX = curX + prevDimensions[0] / 2 - currentDimensions[0] / 2;
                    curZ += prevDimensions[2];
                }
                else if (path.charAt(i) == 'D') {
                    curX = curX + prevDimensions[0] / 2 - currentDimensions[0] / 2;
                    curZ -= currentDimensions[2];
                }
                else if (path.charAt(i) == 'L') {
                    curX += prevDimensions[0];
                    curZ = curZ + prevDimensions[2] / 2 - currentDimensions[2] / 2;
                }
                else if (path.charAt(i) == 'R') {
                    curX -= currentDimensions[0];
                    curZ = curZ + prevDimensions[2] / 2 - currentDimensions[2] / 2;
                }
            }
            // System.out.println("Starting coords: (" + curX + ", " + curY + ", " + curZ + ")");
            createRoom(roomType, path.charAt(i), curX, curY, curZ);
            prevRoomType = roomType;
            roomDimensions.add(new DungeonDimensions(curX, curY, curZ, currentDimensions[0], currentDimensions[1], currentDimensions[2]));
        }
        DungeonGenerator.dungeonsToClear.get("Chicken's Den").add(roomDimensions);
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



    public String generateCorrectPathWithRooms(int numRooms) {

        String correctPathString = "";
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

            if (i == 0) {
                correctPathString += "0";
            }

            int roomTypeNum = (int) (Math.random() * 2);

            // should be renamed to curDirNum
            int curRoomNum = (int) (Math.random() * 4);
            if (numToDir.get(curRoomNum) == oppositeDirs.get(correctPathString.charAt(correctPathString.length() - 1))) {
                curRoomNum += (int) (Math.random() * 3) + 1;
                curRoomNum %= 4;
            }
            correctPathString += numToDir.get(curRoomNum);
        }

        return correctPathString;

    }



    private boolean intersects(int startX, int startZ, int roomTypeNum, Vector<TwoDimensionalRoomCoords> roomCoords) {
        int x1 = startX;
        int x2 = startX + getDimensions(roomTypeNum)[0];
        int z1 = startZ;
        int z2 = startZ + getDimensions(roomTypeNum)[2];
        for (TwoDimensionalRoomCoords curRoomCoords : roomCoords) {
            if (!(x1 > curRoomCoords.getX2() || curRoomCoords.getX1() > x2 || z1 > curRoomCoords.getZ2() || curRoomCoords.getZ1() > z2)) {
                return true;
            }
        }
        return false;
    }



    // creates a room based on a room type number, direction came from, and start coordinates (close bottom right corner)
    public void createRoom(int roomTypeNum, char direction, int startX, int startY, int startZ) {

        final int realRoomNum = roomTypeNum;

        World world = Bukkit.getWorld("world_dungeons");

        // for each room, set the room size
        // then check if we have generated the map for this room before
        // if not, then generate it for the first time
        // otherwise, use the previously generated map to optimize runtime
        if (roomTypeNum == 0 && !roomsCreated[0]) {

            int xSize = 10;
            int ySize = 6;
            int zSize = 10;

            xSizes.put(roomTypeNum, xSize);
            ySizes.put(roomTypeNum, ySize);
            zSizes.put(roomTypeNum, zSize);

            BlockInfo[][][] room = new BlockInfo[xSize][ySize][zSize];

            // initialize everything as air
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    for (int z = 0; z < zSize; z++) {
                        room[x][y][z] = new BlockInfo(Material.AIR);
                    }
                }
            }

            // set floor and ceiling of the room
            for (int x = 0; x < xSize; x++) {
                for (int z = 0; z < zSize; z++) {
                    room[x][0][z] = new BlockInfo(Material.GRASS_BLOCK);
                    room[x][ySize - 1][z] = new BlockInfo(Material.ACACIA_PLANKS);
                }
            }

            // set walls of the room
            for (int y = 1; y < ySize - 1; y++) {
                for (int x = 0; x < xSize; x++) {
                    room[x][y][0] = new BlockInfo(Material.DIRT); // this will set the wall to the RIGHT of the player
                    room[x][y][zSize - 1] = new BlockInfo(Material.DIRT); // this will set the wall to the LEFT of the player
                }
                for (int z = 0; z < zSize; z++) {
                    room[0][y][z] = new BlockInfo(Material.DIRT); // this will set the wall in FRONT of the player
                    room[xSize - 1][y][z] = new BlockInfo(Material.DIRT); // this will set the wall BEHIND of the player
                }
            }

            // add some extra blocks for decoration
            room[8][4][8] = new BlockInfo(Material.ACACIA_LEAVES);
            room[7][4][8] = new BlockInfo(Material.ACACIA_LEAVES);
            room[8][3][8] = new BlockInfo(Material.ACACIA_LEAVES);

            // add torches for lighting
            room[1][1][1] = new BlockInfo(Material.TORCH);
            room[xSize - 2][1][1] = new BlockInfo(Material.TORCH);
            room[1][1][zSize - 2] = new BlockInfo(Material.TORCH);
            room[xSize - 2][1][zSize - 2] = new BlockInfo(Material.TORCH);

            roomMaps.put(roomTypeNum, room);

            Vector<DungeonEntity> dungeonEntities = new Vector<>();
            dungeonEntities.add(new DungeonEntity());
            roomEntities.put(roomTypeNum, dungeonEntities);

            roomsCreated[roomTypeNum] = true;
        }

        else if (roomTypeNum == 1 && !roomsCreated[1]) {
            int xSize = 16;
            int ySize = 10;
            int zSize = 16;

            xSizes.put(roomTypeNum, xSize);
            ySizes.put(roomTypeNum, ySize);
            zSizes.put(roomTypeNum, zSize);

            BlockInfo[][][] room = new BlockInfo[xSize][ySize][zSize];

            // initialize everything as air
            for (int x = 0; x < xSize; x++) {
                for (int y = 0; y < ySize; y++) {
                    for (int z = 0; z < zSize; z++) {
                        room[x][y][z] = new BlockInfo(Material.AIR);
                    }
                }
            }

            // set floor and ceiling of the room
            for (int x = 0; x < xSize; x++) {
                for (int z = 0; z < zSize; z++) {
                    int randomNum = (int) (Math.random() * 6);
                    if (randomNum == 0) {
                        room[x][0][z] = new BlockInfo(Material.COBBLESTONE);
                    }
                    else {
                        room[x][0][z] = new BlockInfo(Material.GRASS_BLOCK);
                    }
                    room[x][ySize - 1][z] = new BlockInfo(Material.ACACIA_PLANKS);
                }
            }

            // set walls of the room
            for (int y = 1; y < ySize - 1; y++) {
                for (int x = 0; x < xSize; x++) {
                    room[x][y][0] = new BlockInfo(Material.DIRT); // this will set the wall to the RIGHT of the player
                    room[x][y][zSize - 1] = new BlockInfo(Material.DIRT); // this will set the wall to the LEFT of the player
                }
                for (int z = 0; z < zSize; z++) {
                    room[0][y][z] = new BlockInfo(Material.DIRT); // this will set the wall in FRONT of the player
                    room[xSize - 1][y][z] = new BlockInfo(Material.DIRT); // this will set the wall BEHIND of the player
                }
            }

            // add decorations
            for (int x = 0; x < xSize; x++) {
                for (int z = 0; z < zSize; z++) {
                    int randomNum = (int) (Math.random() * 3);
                    if (randomNum == 0) {
                        room[x][ySize - 2][z] = new BlockInfo(Material.OAK_LEAVES);
                    }
                    else {
                        room[x][ySize - 2][z] = new BlockInfo(Material.ACACIA_LEAVES);
                    }
                    randomNum = (int) (Math.random() * 6);
                    if (randomNum == 0) {
                        room[x][ySize - 3][z] = new BlockInfo(Material.OAK_LEAVES);
                    }
                }
            }

            // add torches for lighting
            room[1][1][1] = new BlockInfo(Material.TORCH);
            room[xSize - 2][1][1] = new BlockInfo(Material.TORCH);
            room[1][1][zSize - 2] = new BlockInfo(Material.TORCH);
            room[xSize - 2][1][zSize - 2] = new BlockInfo(Material.TORCH);

            roomMaps.put(roomTypeNum, room);

            Vector<DungeonEntity> dungeonEntities = new Vector<>();
            dungeonEntities.add(new DungeonEntity("Test Zombie", 8, 1, 8));
            roomEntities.put(roomTypeNum, dungeonEntities);

            roomsCreated[roomTypeNum] = true;
        }

        // using the room map, generate the actual room
        // one layer at a time, from bottom y to top y
        // to prevent lag spikes / crashing from generating many blocks at once
        new BukkitRunnable() {
            private int y = 0;
            @Override
            public void run() {
                int ySize = ySizes.get(roomTypeNum);
                int xSize = xSizes.get(roomTypeNum);
                int zSize = zSizes.get(roomTypeNum);
                if (y < ySize) {
                    BlockInfo[][][] roomLayout = roomMaps.get(realRoomNum);
                    for (int x = 0; x < xSize; x++) {
                        for (int z = 0; z < zSize; z++) {
                            Location blockLocation = new Location(world, startX + x, startY + y, startZ + z);
                            blockLocation.getBlock().setType(roomLayout[x][y][z].getMaterial());

                            // set log axis
                            if (roomLayout[x][y][z].getLogAxis() != null) {
                                Orientable orientable = (Orientable) blockLocation.getBlock().getBlockData();
                                orientable.setAxis(roomLayout[x][y][z].getLogAxis());
                                blockLocation.getBlock().setBlockData(orientable);
                            }

                            // set stairs direction and shape
                            else if (roomLayout[x][y][z].getStairsDirection() != null) {
                                Stairs stairs = (Stairs) blockLocation.getBlock().getBlockData();
                                stairs.setFacing(roomLayout[x][y][z].getStairsDirection());
                                stairs.setShape(roomLayout[x][y][z].getStairsShape());
                                stairs.setHalf(roomLayout[x][y][z].getStairsBisectedHalf());
                                blockLocation.getBlock().setBlockData(stairs);
                            }

                            // prevent leaves from decaying
                            else if (blockLocation.getBlock().getType() == Material.ACACIA_LEAVES || blockLocation.getBlock().getType() == Material.OAK_LEAVES) {
                                Leaves leaves = (Leaves) blockLocation.getBlock().getBlockData();
                                leaves.setPersistent(true);
                                blockLocation.getBlock().setBlockData(leaves);
                            }
                        }
                    }
                    y++;
                }

                else {

                    /*
                    for (DungeonEntity dungeonEntity : roomEntities.get(roomTypeNum)) {
                        dungeonEntity.spawnMob();
                    }
                    */

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
        else if (roomType == 1) {
            res[0] = 16;
            res[1] = 10;
            res[2] = 16;
        }
        return res;
    }

}
