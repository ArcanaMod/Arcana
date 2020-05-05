package net.arcanamod.blocks.tiles;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.blocks.BlockNormalNode;
import net.arcanamod.client.particles.EnumArcanaParticles;
import net.arcanamod.client.particles.ParticleSpawner;
import net.arcanamod.util.NodeHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class NodeTileEntity extends TileEntity implements ITickable{
	
	public Map<Aspect, Integer> storedAspects = new HashMap<>();
	
	public boolean isOn = false;
	
	public boolean canInteractWith(PlayerEntity playerIn){
		// If we are too far away from this tiles entity you cannot use it
		return !isInvalid() && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
	}
	
	@Override
	public void update(){
		if(this.world.isRemote){
			switch(NodeHelper.getGogglePriority()){
				case SHOW_NONE:
					this.getNode().hitboxOff();
					break;
				case SHOW_NODE:
					this.showNodes();
					break;
				case SHOW_ASPECTS:
					this.showNodes();
					//                    if(ArcanaHelper.isNodeOnCursor(this)) {
					//                        this.showAspects();
					//                    }
					break;
				default:
					break;
			}
		}
	}
	
	public void showNodes(){
		this.getNode().hitboxOn();
		ParticleSpawner.spawnParticle(EnumArcanaParticles.NORMAL_NODE, this.pos.getX() + 0.5f, this.pos.getY() + 0.5f, this.pos.getZ() + 0.5f);
	}
	
	public BlockNormalNode getNode(){
		return ((BlockNormalNode)this.getBlockType());
	}
	
	@Override
	public void readFromNBT(CompoundNBT compound){
		super.readFromNBT(compound);
		
		ListNBT aspects = compound.getTagList("aspects", Constants.NBT.TAG_COMPOUND);
		for(NBTBase aspect : aspects){
			CompoundNBT aspectCompound = (CompoundNBT)aspect;
			storedAspects.put(Aspect.valueOf(aspectCompound.getString("type")), aspectCompound.getInteger("amount"));
		}
		
	}
	
	@Override
	public CompoundNBT writeToNBT(CompoundNBT compound){
		ListNBT aspects = new ListNBT();
		for(Aspect aspect : storedAspects.keySet()){
			CompoundNBT tag = new CompoundNBT();
			tag.setString("type", aspect.toString());
			tag.setInteger("amount", storedAspects.get(aspect));
			aspects.appendTag(tag);
		}
		compound.setTag("aspects", aspects);
		return super.writeToNBT(compound);
	}
}
