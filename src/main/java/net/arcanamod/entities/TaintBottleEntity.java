package net.arcanamod.entities;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.blocks.DelegatingBlock;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.systems.taint.Taint;
import net.arcanamod.world.AuraView;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.arcanamod.systems.taint.Taint.UNTAINTED;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class TaintBottleEntity extends ProjectileItemEntity{
	
	public TaintBottleEntity(EntityType<? extends ProjectileItemEntity> type, World world){
		super(type, world);
	}
	
	protected Item getDefaultItem(){
		return ArcanaItems.TAINT_IN_A_BOTTLE.get();
	}
	
	public TaintBottleEntity(LivingEntity thrower, World world){
		super(ArcanaEntities.TAINT_BOTTLE.get(), thrower, world);
	}
	
	protected void onImpact(RayTraceResult result){
		if(!world.isRemote()){
			// pick some blocks and taint them
			// aim to taint 6 blocks within a 5x3x5 area, fail after 12 attempts
			int tainted = 0;
			BlockPos.Mutable pos = new BlockPos.Mutable();
			for(int tries = 0; tries < 12 && tainted < 6; tries++){
				pos.setPos(this.getPosition()).move(world.rand.nextInt(5) - 2, world.rand.nextInt(3) - 1, world.rand.nextInt(5) - 2);
				BlockState state = world.getBlockState(pos);
				if(!state.isAir(world, pos) && !Taint.isTainted(state.getBlock()) && !Taint.isBlockProtectedByPureNode(world, pos)){
					Block to = Taint.getTaintedOfBlock(state.getBlock());
					if(to != null){
						world.setBlockState(pos, DelegatingBlock.switchBlock(state, to).with(UNTAINTED, false));
						tainted++;
					}
				}
			}
			// add some flux too
			AuraView.SIDED_FACTORY.apply(world).addFluxAt(getPosition(), world.rand.nextInt(3) + 3 + (6 - tainted));
			// add some particles
			world.playEvent(2007, new BlockPos(this.getPosition()), 0xa200ff);
			// and die
			this.remove();
		}
	}
	
	@Override
	public IPacket<?> createSpawnPacket(){
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}