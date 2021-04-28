package net.arcanamod.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3i;

public class ShapeUtils {
    public static Vector3i fromNorth(Vector3i vec, Direction direction) {
        switch (direction) {
            case WEST:
                return new Vector3i( vec.getZ(), vec.getY(), -vec.getX());
            case SOUTH:
                return new Vector3i(-vec.getX(), vec.getY(), -vec.getZ());
            case EAST:
                return new Vector3i(-vec.getZ(), vec.getY(),  vec.getX());
            case NORTH:
            default:
                return new Vector3i( vec.getX(), vec.getY(),  vec.getZ());
        }
    }

    // Basic logic: rotating a shape around its center point (not 0,0,0)
    public static VoxelShape rotate(VoxelShape shape, Direction fromNorth) {
        // IntelliJ says this needed to be an array because of the lambda function
        VoxelShape[] tmp = {VoxelShapes.empty()};
        switch (fromNorth) {
            case EAST:
                shape.forEachBox((minX, minY, minZ, maxX, maxY, maxZ)
                        -> tmp[0] = VoxelShapes.or(tmp[0], VoxelShapes.create(1-maxZ, minY, minX, 1-minZ, maxY, maxX)));
                break;
            case SOUTH:
                shape.forEachBox((minX, minY, minZ, maxX, maxY, maxZ)
                        -> tmp[0] = VoxelShapes.or(tmp[0], VoxelShapes.create(1-maxX, minY, 1-maxZ, 1-minX, maxY, 1-minZ)));
                break;
            case WEST:
                shape.forEachBox((minX, minY, minZ, maxX, maxY, maxZ)
                        -> tmp[0] = VoxelShapes.or(tmp[0], VoxelShapes.create(minZ, minY, 1-maxX, maxZ, maxY, 1-minX)));
                break;
            case NORTH:
            default:
                tmp[0] = shape;
                break;
        }
        return tmp[0];
    }
}
