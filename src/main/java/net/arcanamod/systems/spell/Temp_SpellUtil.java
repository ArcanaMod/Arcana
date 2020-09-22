package net.arcanamod.systems.spell;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

// This class is temporary, It will be removed soon!
@Deprecated
public class Temp_SpellUtil {
    @Deprecated
    public static <T extends Entity>List<T> rayTraceEntities(World w, Vec3d pos, Vec3d ray, Optional<Predicate<T>> entityFilter, Class<T> entityClazz)
    {
        Vec3d end = pos.add(new Vec3d(1, 1, 1));
        AxisAlignedBB aabb = new AxisAlignedBB(pos.x, pos.y, pos.z, end.x, end.y, end.z).expand(ray.x, ray.y, ray.z);
        Vec3d checkVec = pos.add(ray);
        List<T> ret = Lists.newArrayList();
        for (T t : w.getEntitiesWithinAABB(entityClazz, aabb, entityFilter.orElse(Predicates.alwaysTrue())))
        {
            AxisAlignedBB entityBB = t.getBoundingBox();
            if (entityBB == null)
            {
                continue;
            }

            if (entityBB.intersects(Math.min(pos.x, checkVec.x), Math.min(pos.y, checkVec.y), Math.min(pos.z, checkVec.z), Math.max(pos.x, checkVec.x), Math.max(pos.y, checkVec.y), Math.max(pos.z, checkVec.z)))
            {
                ret.add(t);
            }
        }

        return ret;
    }
}
