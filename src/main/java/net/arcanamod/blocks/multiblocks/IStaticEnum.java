package net.arcanamod.blocks.multiblocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.vector.Vector3i;

@MethodsReturnNonnullByDefault
public interface IStaticEnum extends IStringSerializable {

    Vector3i getOffset(Direction direction);

    Vector3i getInvert(Direction direction);
}
