package net.arcanamod.blocks.multiblocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.Vec3i;

@MethodsReturnNonnullByDefault
public interface IStaticEnum extends IStringSerializable {

    Vec3i getOffset(Direction direction);

    Vec3i getInvert(Direction direction);
}
