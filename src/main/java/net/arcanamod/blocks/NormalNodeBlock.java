package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.VisHandler;
import net.arcanamod.blocks.bases.GroupedBlock;
import net.arcanamod.blocks.tiles.NodeTileEntity;
import net.arcanamod.items.ItemWand;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class NormalNodeBlock extends GroupedBlock{
	
	private static VoxelShape BOUNDS = Block.makeCuboidShape(.25, .25, .25, .75, .75, .75);
	
	public boolean isOn = true;
	
	public NormalNodeBlock(Properties properties, ItemGroup group){
		super(properties, group);
	}
	
	public void hitboxOff(){
		isOn = false;
	}
	
	public void hitboxOn(){
		isOn = true;
	}
	
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world){
		NodeTileEntity entity = new NodeTileEntity();
		
		// TODO: seed this
		Random rand = new Random();
		
		for(int i = 0; i < rand.nextInt((5 - 2) + 1) + 5; i++){
			int randomAspect = rand.nextInt(6);
			entity.storedAspects.putIfAbsent(Aspects.primalAspects[randomAspect], rand.nextInt((80 - 30) + 1) + 30);
		}
		
		entity.markDirty();
		return entity;
	}
	
	public boolean hasTileEntity(BlockState state){
		return true;
	}
	
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return isOn ? BOUNDS : VoxelShapes.empty();
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state){
		return BlockRenderType.MODEL;
	}
	
	public boolean isNormalCube(BlockState state, IBlockReader worldIn, BlockPos pos){
		return false;
	}
	
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos){
		return 1;
	}
	
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return VoxelShapes.empty();
	}
	
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult p_225533_6_){
		if(world.isRemote)
			return ActionResultType.PASS;
		if(hand != Hand.MAIN_HAND)
			return ActionResultType.PASS;
		ItemStack itemActivated = player.getHeldItem(hand);
		//CoreType core = WandUtil.getCore(itemActivated);
		//CapType cap = WandUtil.getCap(itemActivated);
		// although other items can store aspects, only wands can draw them directly from nodes ATM
		if(itemActivated.getItem() instanceof ItemWand){
			TileEntity entity = world.getTileEntity(pos);
			if(entity instanceof NodeTileEntity){
				NodeTileEntity tileEntity = (NodeTileEntity)entity;
				ListNBT aspectList = itemActivated.getOrCreateTag().getList("aspects", Constants.NBT.TAG_COMPOUND);
				ListNBT newAspects = new ListNBT();
				for(Aspect coreAspect : Aspects.primalAspects){
					if(tileEntity.storedAspects.containsKey(coreAspect)){
						if(aspectList.isEmpty()){
							tileEntity.storedAspects.replace(coreAspect, tileEntity.storedAspects.get(coreAspect) - 1);
							CompoundNBT compound = new CompoundNBT();
							compound.putString("type", coreAspect.toString());
							compound.putInt("amount", 1);
							newAspects.add(compound);
						}else{
							for(INBT base : aspectList){
								if(base instanceof CompoundNBT){
									CompoundNBT compound = (CompoundNBT)base;
									if(coreAspect.toString().equals(compound.getString("type"))){
										tileEntity.storedAspects.replace(coreAspect, tileEntity.storedAspects.get(coreAspect) - 1);
										if(compound.getInt("amount") < /*core.getMaxVis()*/ 35){
											compound.putInt("amount", compound.getInt("amount") + 1);
											newAspects.add(compound);
										}
									}
								}
							}
						}
						CompoundNBT newTag = itemActivated.getTag();
						newTag.put("aspects", newAspects);
						itemActivated.setTag(newTag);
						// always transfer one for now -- this should work with more though
						final int draw = 1;
						tileEntity.storedAspects.replace(coreAspect, tileEntity.storedAspects.get(coreAspect) - (VisHandler.getFrom(itemActivated).insert(coreAspect, draw, false) - draw));
						tileEntity.markDirty();
					}
				}
			}
		}
		return ActionResultType.SUCCESS;
	}
}