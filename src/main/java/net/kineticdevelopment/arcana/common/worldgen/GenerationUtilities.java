package net.kineticdevelopment.arcana.common.worldgen;

import com.google.gson.internal.$Gson$Types;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;

/**
 * @author Mozaran
 *
 * Functions used to generate ArrayLists od BlockPos that form a given shape
 */
public class GenerationUtilities {
    public enum GenType {THICK, THIN, FULL}

    private static double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }

    private static double lengthSq(double x, double z) {
        return (x * x) + (z * z);
    }

    public static ArrayList<BlockPos> GenerateCyl(BlockPos origin, int xSize, int zSize, int height, GenType type) {
        ArrayList<BlockPos> blockPosList = new ArrayList<>();

        double xRadius = xSize / 2.0 + 0.5;
        double zRadius = zSize / 2.0 + 0.5;

        if (height == 0) {
            return blockPosList;
        } else if (height < 0) {
            origin = origin.add(0, height, 0);
            height = -height;
        }

        if (origin.getY() < 0) {
            origin = new BlockPos(origin.getX(), 0, origin.getZ());
        } else if (origin.getY() + height - 1 > 255) {
            height = 255 - origin.getY() + 1;
        }

        final double invRadiusX = 1 / xRadius;
        final double invRadiusZ = 1 / zRadius;

        final int ceilRadiusX = (int) Math.ceil(xRadius);
        final int ceilRadiusZ = (int) Math.ceil(zRadius);

        int lastX = 0;
        int lastZ = ceilRadiusZ - 1;
        boolean addThick = false;
        int maxZThick = 0;
        double nextXn = 0;
        forX: for (int x = 0; x <= ceilRadiusX; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextZn = 0;
            forZ: for (int z = 0; z <= ceilRadiusZ; ++z) {
                final double zn = nextZn;
                nextZn = (z + 1) * invRadiusZ;

                double distanceSq = lengthSq(xn, zn);
                if (distanceSq > 1) {
                    if (z == 0) {
                        break forX;
                    }
                    break forZ;
                }

                if (type != GenType.FULL) {
                    double d1 = lengthSq(nextXn, zn);
                    double d2 = lengthSq(xn, nextZn);
                    if (d1 <= 1 && d2 <= 1) {
                        continue;
                    }
                }


                for (int y = 0; y < height; ++y) {
                    blockPosList.add(origin.add(x, y, z));
                    blockPosList.add(origin.add(-x, y, z));
                    blockPosList.add(origin.add(x, y, -z));
                    blockPosList.add(origin.add(-x, y, -z));

                    if(type == GenType.THICK && lastX != x && lastZ != z) {
                        lastZ = z;
                        addThick = true;
                        maxZThick = z;
                    }
                }
            }
            lastX = x;
            if(addThick) {
                addThick = false;
                for (int y = 0; y < height; ++y) {
                    blockPosList.add(origin.add(x - 1, y, maxZThick));
                    blockPosList.add(origin.add(-x + 1, y, maxZThick));
                    blockPosList.add(origin.add(x - 1, y, -maxZThick));
                    blockPosList.add(origin.add(-x + 1, y, -maxZThick));
                }
            }
        }

        return blockPosList;
    }

    public static ArrayList<BlockPos> GenerateOval(BlockPos origin, int xSize, int ySize, GenType type) {
        return GenerateCyl(origin, xSize, ySize, 1, type);
    }

    public static ArrayList<BlockPos> GenerateCircle(BlockPos origin, int diameter, GenType type) {
        return GenerateOval(origin, diameter, diameter, type);
    }

    /**
     * @param center - Center of square
     * @param width - Must be odd, if even it will be made to closest smaller odd number
     * @return - Block Pos of all blocks in square
     */
    private static ArrayList<BlockPos> GenerateSquare(BlockPos center, int width) {
        ArrayList<BlockPos> blockPosList = new ArrayList<>();

        int adjWidth = width % 2 == 0 ? width - 1 : width;

        int size = (adjWidth - 1) / 2;

        for (int x = -size; x <= size; ++x) {
            for(int z = -size; z <= size; ++z) {
                blockPosList.add(center.add(x, 0, z));
            }
        }

        return blockPosList;
    }

    /**
     * @param start - Start coordinate
     * @param end - End coordinate
     * @param diameter - Diameter of trunk. Must be odd, if even it will be made to closest smaller odd number
     * @return - ArrayList of all blocks in trunk
     */
    public static ArrayList<BlockPos> GenerateTrunk(BlockPos start, BlockPos end, int diameter) {
        ArrayList<BlockPos> blockPosList = new ArrayList<>();
        blockPosList.add(start);

        int height = end.getY() - start.getY();
        int xTranslation = end.getX() - start.getX();
        int zTranslation = end.getZ() - start.getZ();

        if(xTranslation == 0 && zTranslation == 0) {
            // only y axis
            for(int y = 0; y <= height; ++y) {
                blockPosList.add(start.add(0, y, 0));
            }
        } else {
            // 3 axis trunk
            int dx = Math.abs(xTranslation);
            int dy = Math.abs(height);
            int dz = Math.abs(zTranslation);
            int xs = end.getX() > start.getX() ? 1 : -1;
            int ys = end.getY() > start.getY() ? 1 : -1;
            int zs = end.getZ() > start.getZ() ? 1 : -1;
            int x1 = start.getX();
            int x2 = end.getX();
            int y1 = start.getY();
            int y2 = end.getY();
            int z1 = start.getZ();
            int z2 = end.getZ();

            if (dx > dy && dx >= dz) {
                int p1 = 2 * dy - dx;
                int p2 = 2 * dz - dx;

                while (x1 != x2) {
                    boolean yzChange = false;
                    x1 += xs;
                    if (p1 >= 0) {
                        y1 += ys;
                        p1 -= 2 * dx;
                        yzChange = true;
                    }
                    if (p2 >= 0) {
                        z1 += zs;
                        p2 -= 2 * dx;
                        yzChange = true;
                    }
                    p1 += 2 * dy;
                    p2 += 2 * dz;
                    if (yzChange) blockPosList.add(new BlockPos(x1 - xs, y1, z1));
                    blockPosList.addAll(GenerationUtilities.GenerateSquare(new BlockPos(x1, y1, z1), diameter));
                }
            } else if (dy >= dx && dy >= dz) {
                int p1 = 2 * dx - dy;
                int p2 = 2 * dz - dy;
                while (y1 != y2) {
                    boolean xzChange = false;
                    y1 += ys;
                    if (p1 >= 0) {
                        x1 += xs;
                        p1 -= 2 * dy;
                        xzChange = true;
                    }
                    if (p2 >= 0) {
                        z1 += zs;
                        p2 -= 2 * dy;
                        xzChange = true;
                    }
                    p1 += 2 * dx;
                    p2 += 2 * dz;
                    if (xzChange) blockPosList.add(new BlockPos(x1, y1 - ys, z1));
                    blockPosList.addAll(GenerationUtilities.GenerateSquare(new BlockPos(x1, y1, z1), diameter));
                }
            } else {
                int p1 = 2 * dy - dz;
                int p2 = 2 * dx - dz;
                while (z1 != z2) {
                    boolean xyChange = false;
                    z1 += zs;
                    if (p1 >= 0) {
                        y1 += ys;
                        p1 -= 2 * dz;
                        xyChange = true;
                    }
                    if (p2 >= 0) {
                        x1 += xs;
                        p2 -= 2 * dz;
                        xyChange = true;
                    }
                    p1 += 2 * dy;
                    p2 += 2 * dx;
                    if (xyChange) blockPosList.add(new BlockPos(x1, y1, z1 - zs));
                    blockPosList.addAll(GenerationUtilities.GenerateSquare(new BlockPos(x1, y1, z1), diameter));
                }
            }
        }
        return blockPosList;
    }
}

