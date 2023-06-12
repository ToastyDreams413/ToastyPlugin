package toastyplugin.toastyplugin.dungeons;

public class TwoDimensionalRoomCoords {

    private int x1;
    private int x2;
    private int z1;
    private int z2;

    public TwoDimensionalRoomCoords(int x1, int z1, int x2, int z2) {
        this.x1 = x1;
        this.z1 = z1;
        this.x2 = x2;
        this.z2 = z2;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getZ1() {
        return z1;
    }

    public int getZ2() {
        return z2;
    }
}
