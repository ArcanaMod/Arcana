package net.kineticdevelopment.arcana.common.worldgen;

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
}

