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

    private static final int numRoomTypes = 4;
    private static final Map<Integer, String> numToDir = new HashMap<Integer, String>() {{
        put(0, "U");
        put(1, "D");
        put(2, "L");
        put(3, "R");
    }};

    private static final Map<String, Coords> dirToAdd = new HashMap<String, Coords>() {{
        put("U", new Coords(0, 1));
        put("D", new Coords(0, -1));
        put("L", new Coords(1, 0));
        put("R", new Coords(-1, 0));
    }};

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
    private HashMap<Coords, RoomInfo> roomInfo = new HashMap<>();



    // create a new Chicken's Den dungeon
    public ChickenDungeon(ToastyPlugin plugin, int startX, int startY, int startZ) {

        this.plugin = plugin;

        startCoords = new DungeonDimensions(startX, startY, startZ);

        roomsCreated = new boolean[numRoomTypes];
        for (int i = 0; i < numRoomTypes; i++) {
            roomsCreated[i] = false;
        }

        lengthToBoss = (int) (Math.random() * 2) + 6;
        initializeRooms();
        // String correctPath = generateCorrectPathString(lengthToBoss);
        // System.out.println("Correct Path: " + correctPath);
        // createDungeon(correctPath);
        generateRoomsInfov2(10, lengthToBoss, 2);
        //createDungeon();

    }



    public void createDungeon() {
        for (RoomInfo curRoomInfo : roomInfo.values()) {
            createRoom(curRoomInfo.getRoomTypeNum(), " ".charAt(0), curRoomInfo.getStartX(), curRoomInfo.getStartY(), curRoomInfo.getStartZ());
        }
    }



    /*
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
    public String generateCorrectPathString(int numRooms) {

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
    */



    public void generateRoomsInfo(int totalRooms, int numMainPathRooms, int numSplits) {

        String correctPathString = "";

        // first, generate the main path
        // room coordinates
        Coords curCoords = new Coords(0, 0);

        // world coordinates
        int curX = 0;
        int curY = 0;
        int curZ = 0;
        int prevRoomTypeNum = -1;
        String curPath = "";
        for (int i = 0; i < numMainPathRooms; i++) {

            System.out.println("Current World Coordinates: (" + curX + ", " + curY + ", " + curZ + ")");
            Coords newCoords = new Coords(curCoords.getX(), curCoords.getZ());
            int newRoomNum = 0;
            int newDirNum = (int) (Math.random() * 4);
            String newDir = numToDir.get(newDirNum);

            // if we are not on the first room, make sure this direction and room work
            // if it doesn't work, keep regenerating until it does
            if (i != 0) {
                boolean foundWorkingRoom = false;
                if (i == numMainPathRooms - 1) {
                    newRoomNum = 1;
                    foundWorkingRoom = placeRoomWorks(newRoomNum, prevRoomTypeNum, newDir, newCoords, curX, curZ);
                }
                else {
                    newRoomNum = (int) (Math.random() * (numRoomTypes - 2)) + 2;

                    // make sure this room type works for this direction
                    foundWorkingRoom = placeRoomWorks(newRoomNum, prevRoomTypeNum, newDir, newCoords, curX, curZ);
                    int startingRoomNum = newRoomNum;
                    while (!foundWorkingRoom) {
                        newRoomNum++;
                        if (newRoomNum == numRoomTypes) {
                            newRoomNum = 2;
                        }
                        if (newRoomNum == startingRoomNum) {
                            break;
                        }
                        foundWorkingRoom = placeRoomWorks(newRoomNum, prevRoomTypeNum, newDir, newCoords, curX, curZ);
                    }
                }

                // make sure new direction works
                int c = 1;
                if (!foundWorkingRoom) {
                    newDir = numToDir.get((newDirNum + c) % 4);
                    c++;
                    foundWorkingRoom = placeRoomWorks(newRoomNum, prevRoomTypeNum, newDir, newCoords, curX, curZ);
                }

                // if we somehow have not found a working room, output it because this is a fatal error
                // logically speaking, this should never happen
                if (!foundWorkingRoom) {
                    System.out.println("FATAL ERROR: Chicken's Den room generation did not work.");
                    System.out.println("Resulting path: " + curPath);
                    return;
                }
            }

            curPath += newDir;

            // save the data we just calculated for the room
            RoomInfo newRoomInfo = new RoomInfo(newRoomNum, newDir, curX, curY, curZ);
            roomInfo.put(newCoords, newRoomInfo);

            // modify the values for the next iteration
            curCoords.setX(curCoords.getX() + dirToAdd.get(newDir).getX());
            curCoords.setZ(curCoords.getZ() + dirToAdd.get(newDir).getZ());

            int xSize = xSizes.get(newRoomNum);
            int zSize = zSizes.get(newRoomNum);
            int prevXSize = -1;
            int prevZSize = -1;

            if (prevRoomTypeNum != -1) {
                prevXSize = xSizes.get(prevRoomTypeNum);
                prevZSize = zSizes.get(prevRoomTypeNum);
            }
            prevRoomTypeNum = newRoomNum;

            if (newDir.equals("U")) {
                curX = curX + prevXSize / 2 - xSize / 2;
                curZ += prevZSize;
            }
            else if (newDir.equals("D")) {
                curX = curX + prevXSize / 2 - xSize / 2;
                curZ -= zSize;
            }
            else if (newDir.equals("L")) {
                curX += prevXSize;
                curZ = curZ + prevZSize / 2 - zSize / 2;
            }
            else if (newDir.equals("R")) {
                curX -= xSize;
                curZ = curZ + prevZSize / 2 - zSize / 2;
            }

        } // end main path rooms loop

    }



    // try #2
    public void generateRoomsInfov2(int totalRooms, int numMainPathRooms, int numSplits) {

        System.out.println("Starting to generate Chicken's Den with main path length " + numMainPathRooms + "!");

        String correctPathString = "";
        String roomsString = "0";

        // first, generate the main path
        // room coordinates
        Coords curCoords = new Coords(0, 0);

        // world coordinates
        int curX = 0;
        int curY = 0;
        int curZ = 0;

        int nextRoomNum = 0;
        int nextDirNum = (int) (Math.random() * 4);
        String nextDir = numToDir.get(nextDirNum);
        correctPathString += nextDir;

        RoomInfo firstRoomInfo = new RoomInfo(0, nextDir, curX, curY, curZ);
        System.out.println("Put first room in data!");
        roomInfo.put(new Coords(curCoords.getX(), curCoords.getZ()), firstRoomInfo);

        for (int i = 0; i < numMainPathRooms - 1; i++) {

            boolean bossRoom = false;
            if (i == numMainPathRooms - 2) {
                bossRoom = true;
            }

            System.out.println("Current World Coordinates: (" + curX + ", " + curY + ", " + curZ + ")");
            curCoords.setX(curCoords.getX() + dirToAdd.get(nextDir).getX());
            curCoords.setZ(curCoords.getZ() + dirToAdd.get(nextDir).getZ());

            Coords newCoords = new Coords(curCoords.getX() , curCoords.getZ());
            nextDirNum = (int) (Math.random() * 4);
            nextDir = numToDir.get(nextDirNum);
            if (bossRoom) {
                nextRoomNum = 1;
            }
            else {
                nextRoomNum = (int) (Math.random() * (numRoomTypes - 2)) + 2;
            }
            int prevRoomNum = roomsString.charAt(i - 1) - '0';

            int c = 0;
            System.out.println("Testing nextRoom roomNum: " + nextRoomNum + " nextDir: " + nextDir);
            boolean foundWorkingRoom = placeRoomWorks(nextRoomNum, prevRoomNum, nextDir, newCoords, curX, curZ);
            String startingDir = nextDir;
            while (!foundWorkingRoom) {
                nextDir = numToDir.get((nextDirNum + c) % 4);
                int startingRoomNum = nextRoomNum;
                while (!foundWorkingRoom) {
                    if (!bossRoom) {
                        nextRoomNum++;
                    }
                    if (nextRoomNum == numRoomTypes) {
                        nextRoomNum = 2;
                    }
                    if (nextRoomNum == startingRoomNum) {
                        break;
                    }
                    foundWorkingRoom = placeRoomWorks(nextRoomNum, prevRoomNum, nextDir, newCoords, curX, curZ);
                }
                c++;
                if (numToDir.get((nextDirNum + c) % 4).equals(startingDir)) {
                    break;
                }
            }

            if (!foundWorkingRoom) {
                System.out.println("FATAL ERROR: Chicken's Den room generation did not work.");
                System.out.println("Resulting path before failure: " + correctPathString);
                System.out.println("Room per path segment before failure: " + roomsString);
                return;
            }

            correctPathString += nextDir;

            int xSize = xSizes.get(nextRoomNum);
            int zSize = zSizes.get(nextRoomNum);
            int prevXSize = xSizes.get(prevRoomNum);
            int prevZSize = zSizes.get(prevRoomNum);

            if (nextDir.equals("U")) {
                curX = curX + prevXSize / 2 - xSize / 2;
                curZ += prevZSize;
            }
            else if (nextDir.equals("D")) {
                curX = curX + prevXSize / 2 - xSize / 2;
                curZ -= zSize;
            }
            else if (nextDir.equals("L")) {
                curX += prevXSize;
                curZ = curZ + prevZSize / 2 - zSize / 2;
            }
            else {
                curX -= xSize;
                curZ = curZ + prevZSize / 2 - zSize / 2;
            }

            // save the data we just calculated for the room
            RoomInfo newRoomInfo = new RoomInfo(nextRoomNum, nextDir, curX, curY, curZ);
            roomInfo.put(newCoords, newRoomInfo);

            System.out.println("Put room #" + i + " in data!");

            roomsString += nextRoomNum;

        } // end main path rooms loop

        System.out.println("RESULT:\n" + "Path: " + correctPathString + "\nRooms: " + roomsString);

    }



    public boolean placeRoomWorks(int roomTypeNum, int prevRoomTypeNum, String direction, Coords curCoords, int startX, int startZ) {
        int xSize = xSizes.get(roomTypeNum);
        int zSize = zSizes.get(roomTypeNum);
        int prevXSize = xSizes.get(prevRoomTypeNum);
        int prevZSize = zSizes.get(prevRoomTypeNum);
        if (direction.equals("U")) {
            startX = startX + prevXSize / 2 - xSize / 2;
            startZ += prevZSize;
        }
        else if (direction.equals("D")) {
            startX = startX + prevXSize / 2 - xSize / 2;
            startZ -= zSize;
        }
        else if (direction.equals("L")) {
            startX += prevXSize;
            startZ = startZ + prevZSize / 2 - zSize / 2;
        }
        else if (direction.equals("R")) {
            startX -= xSize;
            startZ = startZ + prevZSize / 2 - zSize / 2;
        }
        int endX = startX + xSizes.get(roomTypeNum) - 1;
        int endZ = startZ + zSizes.get(roomTypeNum) - 1;
        System.out.println("PlaceRoomWorks calc starts and ends --> (" + startX + ", " + startZ + ") U (" + endX + ", " + endZ + ")");
        int targetX = curCoords.getX() + dirToAdd.get(direction).getX();
        int targetZ = curCoords.getZ() + dirToAdd.get(direction).getZ();
        int numSurroundingRooms = 0;
        if (roomInfo.containsKey(new Coords(targetX + 1, targetZ))) {
            numSurroundingRooms++;
        }
        if (roomInfo.containsKey(new Coords(targetX - 1, targetZ))) {
            numSurroundingRooms++;
        }
        if (roomInfo.containsKey(new Coords(targetX, targetZ + 1))) {
            numSurroundingRooms++;
        }
        if (roomInfo.containsKey(new Coords(targetX, targetZ - 1))) {
            numSurroundingRooms++;
        }
        if (numSurroundingRooms >= 3) {
            return false;
        }

        for (RoomInfo curRoomInfo : roomInfo.values()) {
            int otherStartX = curRoomInfo.getStartX();
            int otherStartZ = curRoomInfo.getStartZ();
            int otherEndX = otherStartX + xSizes.get(curRoomInfo.getRoomTypeNum()) - 1;
            int otherEndZ = otherStartZ + zSizes.get(curRoomInfo.getRoomTypeNum()) - 1;
            System.out.println("PlaceRoomWorks calc inside ORIGINAL starts and ends --> (" + startX + ", " + startZ + ") U (" + endX + ", " + endZ + ")");
            System.out.println("PlaceRoomWorks calc inside starts and ends --> (" + otherStartX + ", " + otherStartZ + ") U (" + otherEndX + ", " + otherEndZ + ")");
            if (!(startX > otherEndX || otherStartX > endX || startZ > otherEndZ || otherStartZ > endZ)) {
                System.out.println("FAILED!");
                return false;
            }
            System.out.println("PASSED!");
        }
        return true;
    }



    private void initializeRooms() {

        for (int roomTypeNum = 0; roomTypeNum < numRoomTypes; roomTypeNum++) {

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
            } else if (roomTypeNum == 1 && !roomsCreated[1]) {
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
                        } else {
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
                        } else {
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
            } else if (roomTypeNum == 2 && !roomsCreated[2]) {

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
            } else if (roomTypeNum == 3 && !roomsCreated[3]) {
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
                        } else {
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
                        } else {
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
        }
    }



    // creates a room based on a room type number, direction came from, and start coordinates (close bottom right corner)
    public void createRoom(int roomTypeNum, char direction, int startX, int startY, int startZ) {

        final int realRoomNum = roomTypeNum;

        World world = Bukkit.getWorld("world_dungeons");

        // using the room map, generate the actual room
        // one layer at a time, from bottom y to top y
        // to prevent lag spikes / crashing from generating many blocks at once
        new BukkitRunnable() {
            private int y = 0;
            @Override
            public void run() {
                int ySize = ySizes.get(realRoomNum);
                int xSize = xSizes.get(realRoomNum);
                int zSize = zSizes.get(realRoomNum);
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

}
