package toastyplugin.toastyplugin.dungeons;

import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.type.Stairs;

public class BlockInfo {

    private Material material;
    private Axis logAxis;
    private BlockFace stairsDirection;
    private Stairs.Shape stairsShape;
    private Bisected.Half stairsBisectedHalf;

    public BlockInfo(Material material) {
        this.material = material;
        logAxis = null;
        stairsDirection = null;
        stairsShape = null;
        stairsBisectedHalf = null;
    }

    // constructor for logs with a set Axis
    public BlockInfo(Material material, Axis logAxis) {
        this.material = material;
        this.logAxis = logAxis;
        stairsDirection = null;
        stairsShape = null;
        stairsBisectedHalf = null;
    }

    // constructor for stairs with a set direction and shapeaxis
    public BlockInfo(Material material, BlockFace stairsDirection, Stairs.Shape stairsShape, Bisected.Half stairsBisectedHalf) {
        this.material = material;
        logAxis = null;
        this.stairsDirection = stairsDirection;
        this.stairsShape = stairsShape;
        this.stairsBisectedHalf = stairsBisectedHalf;
    }

    public Material getMaterial() {
        return material;
    }

    public Axis getLogAxis() {
        return logAxis;
    }

    public BlockFace getStairsDirection() {
        return stairsDirection;
    }

    public Stairs.Shape getStairsShape() {
        return stairsShape;
    }

    public Bisected.Half getStairsBisectedHalf() {
        return stairsBisectedHalf;
    }

}
