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
    public enum GenType {THIN, THICK, FULL}
    private enum Quad {LB, RB, LT, RT}

    private static double distance(double x, double y, double ratio) {
        double ySq = Math.pow(y * ratio, 2);
        double xSq = Math.pow(x, 2);
        return Math.sqrt(ySq + xSq);
    }

    private static boolean filled(double x, double y, double radius, double ratio) {
        double distance = distance(x, y, ratio);
        double floorDistance = Math.floor(distance * 100) / 100;
        // return floorDistance - radius <= 0.05;
        return floorDistance <= radius;
    }

    private static boolean fat(double x, double y, double radius, double ratio) {
        return filled(x, y, radius, ratio) &&
                !(filled(x + 1, y, radius, ratio) &&
                filled(x - 1, y, radius, ratio) &&
                filled(x, y + 1, radius, ratio) &&
                filled(x, y - 1, radius, ratio) &&
                filled(x + 1, y + 1, radius, ratio) &&
                filled(x + 1, y - 1, radius, ratio) &&
                filled(x - 1, y - 1, radius, ratio) &&
                filled(x - 1, y + 1, radius, ratio)
        );
    }

    private static ArrayList<BlockPos> GenQuadrant(BlockPos origin, Quad quadrant, int xSize, int zSize,
                                            double radius, double ratio, GenType type) {
        ArrayList<BlockPos> blockPosList = new ArrayList<>();

        int lowX;
        int highX;
        int lowZ;
        int highZ;
        if(quadrant == Quad.LB) {
            lowX = -xSize;
            highX = 0;
            lowZ = -zSize;
            highZ = 0;
        } else if(quadrant == Quad.RB) {
            lowX = 0;
            highX = xSize;
            lowZ = -zSize;
            highZ = 0;
        } else if(quadrant == Quad.LT) {
            lowX = -xSize;
            highX = 0;
            lowZ = 0;
            highZ = zSize;
        } else {
            // Quad.RT
            lowX = 0;
            highX = xSize;
            lowZ = 0;
            highZ = zSize;
        }

        for (int z = lowZ; z <= highZ; z++) {
            for (int x = lowX; x <= highX; x++) {
                boolean filled;

                if (type == GenType.THICK) {
                    filled = fat(x, z, radius, ratio);
                } else if (type == GenType.THIN) {
                    filled = fat(x, z, radius, ratio)
                            && !(fat(x + (x > 0 ? 1 : -1), z, radius, ratio)
                            && fat(x , z + (z > 0 ? 1 : -1), radius, ratio));
                } else {
                    filled = filled(x, z, radius, ratio);
                }
                if (filled) {
                    blockPosList.add(origin.add(x, 0, z));
                }
            }
        }
        return blockPosList;
    }

    public static ArrayList<BlockPos> GenerateOval(BlockPos origin, int width, int height, GenType type) {
        ArrayList<BlockPos> blockPosList = new ArrayList<>();

        double widthRadius = width / 2.0;
        double heightRadius = height / 2.0;
        double radiusRatio = widthRadius < heightRadius ? widthRadius / heightRadius : heightRadius / widthRadius;
        double ovalRatio = widthRadius / heightRadius;

        int maxBlocksX;
        int maxBlocksZ;

        if(width % 2 == 0) {
            maxBlocksX = width / 2 - 1;
        } else {
            maxBlocksX = (width - 1) / 2;
        }

        if(height % 2 == 0) {
            maxBlocksZ = height / 2 - 1;
        } else {
            maxBlocksZ = (height - 1) / 2;
        }

        BlockPos originLB;
        BlockPos originRB;
        BlockPos originLT;
        BlockPos originRT;
        double radius;
        if(width % 2 == 0 && height % 2 == 0) {
            originLB = origin;
            originRB = origin.add(1,0,0);
            originLT = origin.add(0,0,1);
            originRT = origin.add(1, 0, 1);
            radius = widthRadius - distance(.5,.5, radiusRatio);
        } else if(width % 2 == 1 && height % 2 == 0) {
            originLB = origin;
            originLT = origin.add(0,0,1);
            originRB = originLB;
            originRT = originLT;
            radius = widthRadius - distance(0,0.5, radiusRatio);
        } else if(width % 2 == 0 && height % 2 == 1){
            originLB = origin;
            originRB = origin.add(1,0,0);
            originLT = originLB;
            originRT = originRB;
            radius = widthRadius - distance(0, 0.5, radiusRatio);

        } else {
            originLB = origin;
            originRB = origin;
            originLT = origin;
            originRT = origin;
            radius = widthRadius;

        }

        blockPosList.addAll(GenQuadrant(originLB, Quad.LB, maxBlocksX, maxBlocksZ, radius, ovalRatio, type));
        blockPosList.addAll(GenQuadrant(originRB, Quad.RB, maxBlocksX, maxBlocksZ, radius, ovalRatio, type));
        blockPosList.addAll(GenQuadrant(originLT, Quad.LT, maxBlocksX, maxBlocksZ, radius, ovalRatio, type));
        blockPosList.addAll(GenQuadrant(originRT, Quad.RT, maxBlocksX, maxBlocksZ, radius, ovalRatio, type));

        return blockPosList;
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
        } else if(xTranslation != 0 && zTranslation != 0) {
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

            if(dx > dy && dx >= dz) {
                int p1 = 2 * dy - dx;
                int p2 = 2 * dz - dx;

                while (x1 != x2) {
                    boolean yzChange = false;
                    x1 += xs;
                    if(p1 >= 0) {
                        y1 += ys;
                        p1 -= 2 * dx;
                        yzChange = true;
                    }
                    if(p2 >= 0) {
                        z1 += zs;
                        p2 -= 2 * dx;
                        yzChange = true;
                    }
                    p1 += 2 * dy;
                    p2 += 2 * dz;
                    if(yzChange) blockPosList.add(new BlockPos(x1 - xs, y1, z1));
                    blockPosList.addAll(GenerationUtilities.GenerateSquare(new BlockPos(x1, y1, z1),diameter));
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
                    if(xzChange) blockPosList.add(new BlockPos(x1, y1 - ys, z1));
                    blockPosList.addAll(GenerationUtilities.GenerateSquare(new BlockPos(x1, y1, z1),diameter));
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
                    if(xyChange) blockPosList.add(new BlockPos(x1, y1, z1 - zs));
                    blockPosList.addAll(GenerationUtilities.GenerateSquare(new BlockPos(x1, y1, z1),diameter));
                }
            }
        } else {
            // 2 Axis Trunk
            boolean isXAxis = xTranslation != 0;

            if(isXAxis) {
                int slope = (int) Math.ceil((double) height / Math.abs(xTranslation));
                for(int x = 0; x <= Math.abs(xTranslation); ++x) {
                    for(int y = 0; y < slope; ++y) {
                        int xOffset = xTranslation < 0 ? -x : x;
                        int yOffset =  (x * slope) + y - x;
                        if(yOffset > height) break;
                        blockPosList.addAll(GenerationUtilities.GenerateSquare(start.add(xOffset, yOffset, 0), diameter));
                    }
                }
            } else {
                int slope = (int) Math.ceil((double) height / Math.abs(zTranslation));
                for(int z = 0; z <= Math.abs(zTranslation); ++z) {
                    for(int y = 0; y < slope; ++y) {
                        int zOffset = zTranslation < 0 ? -z : z;
                        int yOffset =  (z * slope) + y - z;
                        if(yOffset > height) break;
                        blockPosList.addAll(GenerationUtilities.GenerateSquare(start.add(0, yOffset, zOffset), diameter));
                    }
                }
            }
        }
        return blockPosList;
    }
}

