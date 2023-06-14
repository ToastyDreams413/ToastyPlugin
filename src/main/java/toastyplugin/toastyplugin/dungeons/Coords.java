package toastyplugin.toastyplugin.dungeons;

public class Coords {

    private int x;
    private int z;

    public Coords(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coords)) {
            return false;
        }
        Coords other = (Coords) obj;
        return x == other.x && z == other.z;
    }
}
