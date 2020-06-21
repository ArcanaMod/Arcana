package net.arcanamod.blocks.tiles;

import io.netty.buffer.Unpooled;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.*;
import net.arcanamod.containers.ResearchTableContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ResearchTableTileEntity extends LockableTileEntity implements IPreActionEventCallable {

	ArrayList<BlockPos> visContainers = new ArrayList<>();
	AspectBattery battery = new AspectBattery(Integer.MAX_VALUE,100);

	public ResearchTableTileEntity(){
		super(ArcanaTiles.RESEARCH_TABLE_TE.get());
	}
	
	// Three slots for wand (OR any AspectHandler, but research games can only be performed with a wand), ink, note
	// up to 9 for crafting guesswork for arcane crafting
	// up to, idk, 12 for arcane infusion
	// golemancy will be weird
	// so its ~15 max?
	
	// slots 0-2 are always there, the rest are reserved for the games themselves

	protected ItemStackHandler items = new ItemStackHandler(14){
		protected void onContentsChanged(int slot){
			super.onContentsChanged(slot);
			markDirty();
		}
	};

	public AspectBattery getVisBattery() {
		return getVisShareablesAsBattery();
	}

	//TODO: FIX PERFORMANCE (-20 FPS) Caching or something.
	private AspectBattery getVisShareablesAsBattery() {
		//if (world.isRemote) return null;
		//AtomicInteger j = new AtomicInteger();
		battery.clear();
		BlockPos.getAllInBox(getPos().north(4).east(4).up(4),getPos().south(4).west(4).down(2)).forEach(blockPos -> {
			if (world.getBlockState(blockPos).hasTileEntity()) {
				TileEntity teinbox = world.getTileEntity(blockPos);
				if (teinbox != null)
					if (teinbox instanceof IVisShareable)
						if (((IVisShareable) teinbox).isVisShareable()) {
							AspectBattery vis = (AspectBattery)IAspectHandler.getFrom(teinbox);
							if (vis != null) {
								visContainers.add(new BlockPos(blockPos)); // Removing reference
								AspectBattery.merge(battery, vis);
							}
						}
			}
		});
		return battery;
	}

	@Override
	public void read(CompoundNBT compound){
		super.read(compound);
		if(compound.contains("items"))
			items.deserializeNBT(compound.getCompound("items"));
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound){
		super.write(compound);
		compound.put("items", items.serializeNBT());
		return compound;
	}

	@Override
	protected ITextComponent getDefaultName() {
		return new StringTextComponent("research_table");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer(8,8));
		buffer.writeBlockPos(pos);
		return new ResearchTableContainer(id,player,buffer);
	}

	//net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
	//		net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);
	@Nullable
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing){
		/*if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return items.cast();
		else*/ if(capability == AspectHandlerCapability.ASPECT_HANDLER) {
			AspectBattery battery = getVisBattery();
			return battery.getCapability(AspectHandlerCapability.ASPECT_HANDLER).cast();
		}
		return super.getCapability(capability, facing).cast();
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
		return this.getCapability(cap,null);
	}

	public ItemStack visItem(){
		return items.getStackInSlot(0);
	}
	
	public ItemStack ink(){
		return items.getStackInSlot(1);
	}
	
	@Nonnull
	public ItemStack note(){
		return items.getStackInSlot(2);
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return items.getSlots();
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * Returns the stack in the given slot.
	 *
	 * @param index
	 */
	@Override
	public ItemStack getStackInSlot(int index) {
		return this.items.getStackInSlot(index);
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 *
	 * @param index
	 * @param count
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack stack0 = this.items.getStackInSlot(index);
		ItemStack stack1 = this.items.getStackInSlot(index).copy();
		stack0.shrink(count);
		stack1.setCount(count);
		return stack1; //TODO: Check of works fine (custom impl)
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 *
	 * @param index
	 */
	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack stack = this.items.getStackInSlot(index).copy();
		this.items.getStackInSlot(index).setCount(0);
		return stack;
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 *
	 * @param index
	 * @param stack
	 */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.items.getStackInSlot(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.items.setStackInSlot(index, stack);
		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 *
	 * @param player
	 */
	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void clear() {
		for (int i = 0; i < items.getSlots()-1; i++) {
			items.getStackInSlot(i).setCount(0);
		}
	}

	@Override
	public void onAction(Action action, @Nullable Object... args) {
		for (BlockPos visPos : visContainers) {
			TileEntity te = world.getTileEntity(visPos);
			if (te instanceof IPreActionEventCallable) {
				((IPreActionEventCallable)te).onAction(action,args);
			}
		}
	}
}