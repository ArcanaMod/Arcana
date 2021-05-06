package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.*;
import net.arcanamod.items.PhialItem;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.DispenserContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AspectBookshelfTileEntity extends LockableLootTileEntity implements ITickableTileEntity, VisShareable{
	private NonNullList<ItemStack> stacks = NonNullList.withSize(9, ItemStack.EMPTY);
	AspectBattery vis = new AspectBattery(9, 8);
	private double lastVis;
	public Direction rotation;
	
	public AspectBookshelfTileEntity(){
		super(ArcanaTiles.ASPECT_SHELF_TE.get());
	}
	
	public AspectBookshelfTileEntity(Direction rotation){
		super(ArcanaTiles.ASPECT_SHELF_TE.get());
		this.rotation = rotation;
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack){
		return this.getStackInSlot(index).isEmpty();
	}
	
	public int getVisTotal(){
		int vis = 0;
		for(ItemStack stack : stacks){
			if(stack.getItem() instanceof PhialItem){
				vis += ((PhialItem)stack.getItem()).getAspectStacks(stack).get(0).getAmount();
			}
		}
		return vis;
	}
	
	public int getSizeInventory(){
		return 9;
	}
	
	public int getInventoryStackLimit(){
		return 1;
	}
	
	protected ITextComponent getDefaultName(){
		return new TranslationTextComponent("container.aspectbookshelf");
	}
	
	protected Container createMenu(int id, PlayerInventory player){
		return new DispenserContainer(id, player, this);
	}
	
	public int getRedstoneOut(){
		float vis;
		vis = getVisTotal();
		return (int)((vis / 72F) * 15);
	}
	
	protected NonNullList<ItemStack> getItems(){
		return this.stacks;
	}
	
	protected void setItems(NonNullList<ItemStack> itemsIn){
		this.stacks = itemsIn;
	}
	
	@Override
	public void tick(){
		double newVis = vis.getHoldersAmount();
		if(world != null && lastVis != newVis && !world.isRemote){
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
		}
		lastVis = newVis;
	}
	
	public CompoundNBT write(CompoundNBT compound){
		super.write(compound);
		CompoundNBT aspectsNbt = vis.serializeNBT();
		compound.put("aspects", aspectsNbt);
		if(!this.checkLootAndWrite(compound)){
			ItemStackHelper.saveAllItems(compound, this.stacks);
		}
		return compound;
	}
	
	public void read(BlockState state, CompoundNBT compound){
		vis.deserializeNBT(compound.getCompound("aspects"));
		this.stacks = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
		if(!this.checkLootAndRead(compound)){
			ItemStackHelper.loadAllItems(compound, this.stacks);
		}
		super.read(state, compound);
	}
	
	public AspectBattery updateBatteryAndReturn(){
		for(int i = 0; i < stacks.size(); i++){
			if(stacks.get(i).getItem() instanceof PhialItem){
				AspectBattery aspectBattery = (AspectBattery)IAspectHandler.getFrom(stacks.get(i));
				IAspectHolder target;
				if(aspectBattery != null){
					target = aspectBattery.getHolder(0);
					vis.setCellAtIndex(i, (AspectCell)target);
				}
			}else{
				if(vis.exist(i)){
					vis.deleteCell(i);
				}
			}
		}
		return vis;
	}
	
	
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap){
		if(cap == AspectHandlerCapability.ASPECT_HANDLER){
			return updateBatteryAndReturn().getCapability(AspectHandlerCapability.ASPECT_HANDLER).cast();
		}else{
			return updateBatteryAndReturn().getCapability(null, null);
		}
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side){
		return this.getCapability(cap);
	}
	
	public boolean addPhial(ItemStack stack, int slot){
		if(this.stacks.get(slot).isEmpty()){
			stack = stack.copy();
			stack.setCount(1);
			this.setInventorySlotContents(slot, stack);
			return true;
		}
		return false;
	}
	
	public ItemStack removePhial(int slot){
		if(!this.stacks.get(slot).isEmpty()){
			ItemStack removedPhial = this.stacks.get(slot);
			this.stacks.set(slot, ItemStack.EMPTY);
			return removedPhial;
		}
		return ItemStack.EMPTY;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack){
		this.markDirty();
		this.getWorld().notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 3);
		super.setInventorySlotContents(index, stack);
	}
	
	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket(){
		CompoundNBT nbtTagCompound = new CompoundNBT();
		write(nbtTagCompound);
		return new SUpdateTileEntityPacket(pos, -1, nbtTagCompound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
		read(getBlockState(), pkt.getNbtCompound());
	}
	
	@Override
	public CompoundNBT getUpdateTag(){
		CompoundNBT nbtTagCompound = new CompoundNBT();
		write(nbtTagCompound);
		return nbtTagCompound;
	}
	
	@Override
	public void handleUpdateTag(BlockState state, CompoundNBT tag){
		this.read(state, tag);
	}
	
	@Override
	public boolean isVisShareable(){
		return true;
	}
	
	@Override
	public boolean isManual(){
		return true;
	}
	
	@Override
	public boolean isSecure(){
		return false;
	}
}