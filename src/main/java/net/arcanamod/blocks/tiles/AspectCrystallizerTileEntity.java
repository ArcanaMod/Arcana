package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.*;
import net.arcanamod.containers.AspectCrystallizerContainer;
import net.arcanamod.items.CrystalItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectCrystallizerTileEntity extends LockableTileEntity implements ITickableTileEntity, ISidedInventory{
	
	protected NonNullList<ItemStack> items = NonNullList.withSize(1, ItemStack.EMPTY);
	private static final int[] SLOT_OTHER = new int[]{};
	private static final int[] SLOTS_DOWN = new int[]{0};
	
	public static final int MAX_PROGRESS = 80;
	
	public AspectBattery vis = new AspectBattery(1, 100);
	public int progress = 0;
	
	public AspectCrystallizerTileEntity(){
		super(ArcanaTiles.ASPECT_CRYSTALLIZER_TE.get());
	}
	
	public void tick(){
		IAspectHolder holder = vis.getHolder(0);
		if(holder.getCurrentVis() > 0
				&& ((getStackInSlot(0).getItem() instanceof CrystalItem && ((CrystalItem)getStackInSlot(0).getItem()).aspect == holder.getContainedAspect() && getStackInSlot(0).getCount() < 64)
				|| ((getStackInSlot(0).isEmpty())))){
			if(progress >= MAX_PROGRESS){
				progress = 0;
				if(getStackInSlot(0).isEmpty())
					setInventorySlotContents(0, new ItemStack(AspectUtils.aspectCrystalItems.get(holder.getContainedAspect())));
				else
					getStackInSlot(0).grow(1);
				holder.drain(new AspectStack(holder.getContainedAspect(), 1), false);
			}
			progress++;
		}else if(progress > 0)
			progress--;
	}
	
	public CompoundNBT write(CompoundNBT compound){
		ItemStackHelper.saveAllItems(compound, items);
		CompoundNBT aspectsNbt = vis.serializeNBT();
		compound.put("aspects", aspectsNbt);
		compound.putInt("progress", progress);
		return super.write(compound);
	}
	
	public void read(BlockState state, CompoundNBT compound){
		ItemStackHelper.loadAllItems(compound, items);
		vis.deserializeNBT(compound.getCompound("aspects"));
		progress = compound.getInt("progress");
		super.read(state, compound);
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap){
		if(cap == AspectHandlerCapability.ASPECT_HANDLER)
			return vis.getCapability(AspectHandlerCapability.ASPECT_HANDLER).cast();
		return LazyOptional.empty();
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
		return getCapability(cap);
	}
	
	protected ITextComponent getDefaultName(){
		return new TranslationTextComponent("container.aspect_crystallizer");
	}
	
	protected Container createMenu(int id, PlayerInventory player){
		return new AspectCrystallizerContainer(id, this, player);
	}
	
	public int getSizeInventory(){
		return 3;
	}
	
	public boolean isEmpty(){
		return items.stream().allMatch(ItemStack::isEmpty);
	}
	
	public ItemStack getStackInSlot(int index){
		return items.get(index);
	}
	
	public ItemStack decrStackSize(int index, int count){
		return ItemStackHelper.getAndSplit(items, index, count);
	}
	
	public ItemStack removeStackFromSlot(int index){
		return ItemStackHelper.getAndRemove(items, index);
	}
	
	public void setInventorySlotContents(int index, ItemStack stack){
		items.set(index, stack);
	}
	
	public boolean isUsableByPlayer(PlayerEntity player){
		if(world == null || world.getTileEntity(pos) != this)
			return false;
		return player.getDistanceSq(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5) <= 64;
	}
	
	public void clear(){
		items.clear();
	}
	
	public int[] getSlotsForFace(Direction side){
		return side == Direction.DOWN ? SLOTS_DOWN : SLOT_OTHER;
	}
	
	public boolean canInsertItem(int index, ItemStack itemStack, @Nullable Direction direction){
		return isItemValidForSlot(index, itemStack);
	}
	
	public boolean canExtractItem(int index, ItemStack stack, Direction direction){
		return index == 0;
	}
	
	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket(){
		CompoundNBT compound = new CompoundNBT();
		compound.put("aspects", vis.serializeNBT());
		return new SUpdateTileEntityPacket(pos, -1, compound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
		vis.deserializeNBT(pkt.getNbtCompound().getCompound("aspects"));
	}
	
	@Override
	public CompoundNBT getUpdateTag(){
		CompoundNBT compound = super.getUpdateTag();
		compound.put("aspects", vis.serializeNBT());
		return compound;
	}
	
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag){
		this.read(state, tag);
	}
}