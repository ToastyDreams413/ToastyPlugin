package toastyplugin.toastyplugin.dungeons;

import java.util.Vector;

public class RoomInfo {

    private int roomTypeNum;
    private Vector<String> directions;
    private Vector<String> cameFromDirections;
    private int startX;
    private int startY;
    private int startZ;

    public RoomInfo(int roomTypeNum) {
        this.roomTypeNum = roomTypeNum;
        directions = new Vector<>();
        cameFromDirections = new Vector<>();
    }

    public RoomInfo(int roomTypeNum, String direction, int startX, int startY, int startZ) {
        this.roomTypeNum = roomTypeNum;
        directions = new Vector<>();
        directions.add(direction);
        cameFromDirections = new Vector<>();
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
    }

    public void addDirection(String direction) {
        directions.add(direction);
    }

    public int getRoomTypeNum() {
        return roomTypeNum;
    }

    public Vector<String> getDirections() {
        return directions;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getStartZ() {
        return startZ;
    }

    public void setStartX(int x) {
        startX = x;
    }

    public void setStartY(int y) {
        startX = y;
    }

    public void setStartZ(int z) {
        startX = z;
    }

}
