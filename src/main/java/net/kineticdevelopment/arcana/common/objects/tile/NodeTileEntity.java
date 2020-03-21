package net.kineticdevelopment.arcana.common.objects.tile;

import net.kineticdevelopment.arcana.client.particles.EnumArcanaParticles;
import net.kineticdevelopment.arcana.client.particles.ParticleSpawner;
import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.minecraft.block.BlockGlass;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NodeTileEntity extends TileEntity implements ITickable {

    public Map<Aspect.AspectType, Integer> storedAspects = new HashMap<>();

    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it
        return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }


    @Override
    public void update() {
        ParticleSpawner.spawnParticle(EnumArcanaParticles.NORMAL_NODE, this.pos.getX() + 0.5f, this.pos.getY() + 0.5f, this.pos.getZ() + 0.5f);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        NBTTagList aspects = compound.getTagList("aspects", Constants.NBT.TAG_COMPOUND);
         for (NBTBase aspect : aspects) {
            NBTTagCompound aspectCompound = (NBTTagCompound) aspect;
            storedAspects.put(Aspect.AspectType.valueOf(aspectCompound.getString("type")), aspectCompound.getInteger("amount"));
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList aspects = new NBTTagList();
        for(Aspect.AspectType aspectType : storedAspects.keySet()) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("type", aspectType.toString());
            tag.setInteger("amount", storedAspects.get(aspectType));
            aspects.appendTag(tag);
        }
        compound.setTag("aspects", aspects);
        return super.writeToNBT(compound);
    }
}
