package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.ItemAspectRegistry;
import net.arcanamod.blocks.CrucibleBlock;
import net.arcanamod.items.recipes.AlchemyInventory;
import net.arcanamod.items.recipes.AlchemyRecipe;
import net.arcanamod.world.AuraView;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.util.Constants;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.arcanamod.blocks.CrucibleBlock.FULL;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrucibleTileEntity extends TileEntity implements ITickableTileEntity{
	
	// Not an aspect handler; cannot be drawn directly from, and has infinite size
	// Should decay or something - to avoid very large NBT, at least.
	Map<Aspect, AspectStack> aspectStackMap = new HashMap<>();
	boolean boiling = false;
	
	public CrucibleTileEntity(){
		super(ArcanaTiles.CRUCIBLE_TE.get());
	}
	
	public Map<Aspect, AspectStack> getAspectStackMap(){
		return aspectStackMap;
	}
	
	public void tick(){
		BlockState below = world.getBlockState(pos.down());
		IFluidState fluidState = world.getFluidState(pos.down());
		boiling = hasWater() && (below.getBlock() == Blocks.FIRE || below.getBlock() == Blocks.MAGMA_BLOCK || fluidState.getFluid() == Fluids.FLOWING_LAVA || fluidState.getFluid() == Fluids.LAVA);
		
		// check for items
		// if there are items that have aspects, boil them :)
		if(!world.isRemote() && isBoiling()){
			List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, CrucibleBlock.INSIDE.getBoundingBox().offset(getPos()));
			for(ItemEntity item : items){
				ItemStack stack = item.getItem();
				// check if that item makes a recipe
				// requires a player; droppers and such will never craft alchemy recipes
				// yet; we could make a knowledgeable dropper or such later
				boolean melt = true;
				if(item.getThrowerId() != null && world.getPlayerByUuid(item.getThrowerId()) != null){
					PlayerEntity thrower = world.getPlayerByUuid(item.getThrowerId());
					AlchemyInventory inventory = new AlchemyInventory(this, thrower);
					inventory.setInventorySlotContents(0, stack);
					Optional<AlchemyRecipe> optionalRecipe = world.getRecipeManager().getRecipe(AlchemyRecipe.ALCHEMY, inventory, world);
					if(optionalRecipe.isPresent()){
						melt = false;
						AlchemyRecipe recipe = optionalRecipe.get();
						if(stack.getCount() == 1)
							item.remove();
						else
							stack.shrink(1);
						ItemStack result = recipe.getCraftingResult(inventory);
						if(!thrower.addItemStackToInventory(result))
							thrower.dropItem(result, false);
						for(AspectStack aspectStack : recipe.getAspects()){
							Aspect aspect = aspectStack.getAspect();
							AspectStack newStack = new AspectStack(aspect, aspectStackMap.get(aspect).getAmount() - aspectStack.getAmount());
							if(!newStack.isEmpty())
								aspectStackMap.put(aspect, newStack);
							else
								aspectStackMap.remove(aspect);
						}
						markDirty();
						world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
					}
				}
				if(melt){
					List<AspectStack> aspects = ItemAspectRegistry.get(stack);
					if(aspects.size() > 0){
						item.remove();
						world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1.0F, 1.0F);
						markDirty();
						world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
						for(AspectStack aspect : aspects)
							aspectStackMap.put(aspect.getAspect(), new AspectStack(aspect.getAspect(), aspect.getAmount() + (aspectStackMap.containsKey(aspect.getAspect()) ? aspectStackMap.get(aspect.getAspect()).getAmount() : 0)));
					}
				}
			}
		}
	}
	
	public void empty(){
		// release aspects as flux :)
		// half as much as there are aspects
		AuraView.SIDED_FACTORY.apply(world).addTaintAt(getPos(), aspectStackMap.values().stream().mapToInt(AspectStack::getAmount).sum() / 2);
		aspectStackMap.clear();
		// block handles changing state
	}
	
	private boolean hasWater(){
		return getWorld().getBlockState(getPos()).get(FULL);
	}
	
	public boolean isBoiling(){
		return boiling;
	}
	
	public void read(CompoundNBT compound){
		super.read(compound);
		ListNBT aspects = compound.getList("aspects", Constants.NBT.TAG_COMPOUND);
		aspectStackMap.clear();
		for(INBT inbt : aspects){
			CompoundNBT aspectNbt = ((CompoundNBT)inbt);
			Aspect aspect = AspectUtils.getAspectByName(aspectNbt.getString("aspect"));
			int amount = aspectNbt.getInt("amount");
			aspectStackMap.put(aspect, new AspectStack(aspect, amount));
		}
	}
	
	public CompoundNBT write(CompoundNBT compound){
		super.write(compound);
		ListNBT aspects = new ListNBT();
		for(AspectStack stack : aspectStackMap.values()){
			CompoundNBT stackNbt = new CompoundNBT();
			stackNbt.putString("aspect", stack.getAspect().name());
			stackNbt.putInt("amount", stack.getAmount());
			aspects.add(stackNbt);
		}
		compound.put("aspects", aspects);
		return compound;
	}
	
	public CompoundNBT getUpdateTag(){
		return write(new CompoundNBT());
	}
	
	public void handleUpdateTag(CompoundNBT tag){
		read(tag);
	}
	
	public SUpdateTileEntityPacket getUpdatePacket(){
		return new SUpdateTileEntityPacket(pos, -1, getUpdateTag());
	}
	
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
		handleUpdateTag(pkt.getNbtCompound());
	}
}