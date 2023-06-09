package toastyplugin.toastyplugin.dungeons;

public class DungeonDimensions {

    private int startX;
    private int startY;
    private int startZ;
    private int xSize;
    private int ySize;
    private int zSize;

    public DungeonDimensions(int startX, int startY, int startZ) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.xSize = -1;
        this.ySize = -1;
        this.zSize = -1;
    }

    public DungeonDimensions(int startX, int startY, int startZ, int xSize, int ySize, int zSize) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
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

    public int getXSize() {
        return xSize;
    }

    public int getYSize() {
        return ySize;
    }

    public int getZSize() {
        return zSize;
    }

}
