package net.arcanamod.blocks.tiles;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.*;
import net.arcanamod.blocks.BlockAspectBookshelf;
import net.arcanamod.util.Pair;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class AspectBookshelfTileEntity extends TileEntity implements ITickableTileEntity, IVisShareable
{
	AspectBattery aspectBattery = new AspectBattery(9,8);

	public AspectBookshelfTileEntity() {
		super(ArcanaTiles.ASPECT_SHELF_TE.get());
	}

	protected NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);

	public NonNullList<ItemStack> getItems() {
		return items;
	}

	@Override
	public void tick() {
		if (getBlockState().get(BlockAspectBookshelf.LEVEL_0_9)!=getNonEmptyItemsStoredCount()) {
			if (!world.isRemote)
			world.setBlockState(pos,getBlockState().with(BlockAspectBookshelf.LEVEL_0_9,getNonEmptyItemsStoredCount()));
		}
	}

	@Override
	public CompoundNBT write(CompoundNBT compound) {
		super.write(compound);
		ItemStackHelper.saveAllItems(compound, this.items);
		return compound;
	}

	@Override
	public void read(CompoundNBT compound) {
		this.items = NonNullList.withSize(9, ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.items);
		super.read(compound);
	}

	private int getNonEmptyItemsStoredCount() {
		int count = 0;
		for (ItemStack stack : items)
		{
			if (!stack.isEmpty())
				count++;
		}
		return count;
	}

	public AspectBattery UpdateBatteryAndReturn()
	{
		for (int i = 0; i < items.size(); i++) {
			if (!items.get(i).isEmpty()) {
				AspectBattery vis = (AspectBattery) IAspectHandler.getFrom(items.get(i));
				IAspectHolder target = vis.getHolder(0);
				aspectBattery.setCellAtIndex(i,(AspectCell)target);
			} else {
				if (aspectBattery.exist(i))
					aspectBattery.deleteCell(i);
			}
		}

		return aspectBattery;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
		if (cap == AspectHandlerCapability.ASPECT_HANDLER)
			return UpdateBatteryAndReturn().getCapability(AspectHandlerCapability.ASPECT_HANDLER).cast();
		else return null;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return this.getCapability(cap);
	}

	/**
	 * Add phial to shelf
	 * @param toAdd ItemStack of phial to add
	 * @return returns true if success
	 */
	public boolean addPhial(ItemStack toAdd)
	{
		if (getNonEmptyItemsStoredCount() < 9 && toAdd != ItemStack.EMPTY)
		{
			AspectBattery vis = (AspectBattery) toAdd.getCapability(AspectHandlerCapability.ASPECT_HANDLER).orElse(null);
			if (vis!=null&&(Arcana.debug||vis.getHolder(0).getCurrentVis()==0))
			{
				ItemStack stack = toAdd.copy();
				stack.setCount(1);
				for (int i = 0; i < items.size(); i++) {
					if (items.get(i)==ItemStack.EMPTY)
					{
						items.set(getNonEmptyItemsStoredCount(),stack);
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Removes phial from shelf
	 * @return removed phial stack with vis inside
	 */
	public ItemStack removePhial()
	{
		Pair<ItemStack,Integer> id_stack = new Pair<>(ItemStack.EMPTY, 0);
		ItemStack empty_phial = ItemStack.EMPTY;
		int i = -1;
		if (getNonEmptyItemsStoredCount() > 0)
		{
			for (ItemStack stack : items)
			{
				i++;
				if (!stack.isEmpty())
				{
					id_stack.setFirst(stack.copy());
					id_stack.setSecond(i);
					AspectBattery vis = (AspectBattery) IAspectHandler.getFrom(stack);
					//Remove empty phials
					if (vis.getHolder(0)!=null)
					{
						if (vis.getHolder(0).getCurrentVis()==0) {
							items.set(i, ItemStack.EMPTY);
							empty_phial = stack.copy();
							aspectBattery.setCellAtIndex(i,new AspectCell(8));
							return empty_phial;
						}
					}
				}
			}
		}
		if (id_stack.getSecond() != -1)
		{
			items.set(id_stack.getSecond(),ItemStack.EMPTY);
			aspectBattery.setCellAtIndex(id_stack.getSecond(),new AspectCell(8));
		}
		return id_stack.getFirst();
	}

	//  When the world loads from disk, the server needs to send the TileEntity information to the client
	//  it uses getUpdatePacket(), getUpdateTag(), onDataPacket(), and handleUpdateTag() to do this:
	//  getUpdatePacket() and onDataPacket() are used for one-at-a-time TileEntity updates
	//  getUpdateTag() and handleUpdateTag() are used by vanilla to collate together into a single chunk update packet
	//  Not really required for this example since we only use the timer on the client, but included anyway for illustration
	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		CompoundNBT nbtTagCompound = new CompoundNBT();
		write(nbtTagCompound);
		int tileEntityType = ArcanaTiles.ASPECT_SHELF_TE.hashCode();
		return new SUpdateTileEntityPacket(this.pos, tileEntityType, nbtTagCompound);
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		read(pkt.getNbtCompound());
	}

	/* Creates a tag containing all of the TileEntity information, used by vanilla to transmit from server to client */
	@Override
	public CompoundNBT getUpdateTag()
	{
		CompoundNBT nbtTagCompound = new CompoundNBT();
		write(nbtTagCompound);
		return nbtTagCompound;
	}

	/* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client */
	@Override
	public void handleUpdateTag(CompoundNBT tag)
	{
		this.read(tag);
	}


	@Override
	public boolean isVisShareable() {
		return true;
	}
}
