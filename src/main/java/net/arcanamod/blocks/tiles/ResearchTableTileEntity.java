package net.arcanamod.blocks.tiles;

import io.netty.buffer.Unpooled;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.AspectBattery;
import net.arcanamod.aspects.AspectHandlerCapability;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.aspects.VisShareable;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.containers.ResearchTableContainer;
import net.minecraft.block.BlockState;
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

import static net.arcanamod.blocks.multiblocks.research_table.ResearchTableComponentBlock.PAPER;
import static net.arcanamod.blocks.multiblocks.research_table.ResearchTableCoreBlock.FACING;
import static net.arcanamod.blocks.multiblocks.research_table.ResearchTableCoreBlock.INK;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ResearchTableTileEntity extends LockableTileEntity{
	ArrayList<BlockPos> visContainers = new ArrayList<>();
	AspectBattery battery = new AspectBattery(Integer.MAX_VALUE, 100);

	public ResearchTableTileEntity(){
		super(ArcanaTiles.RESEARCH_TABLE_TE.get());
	}

	// Three slots for wand (OR any AspectHandler, but research games can only be performed with a wand), ink, note
	// up to 9 for crafting guesswork for arcane crafting
	// up to, idk, 12 for arcane infusion
	// golemancy will be weird
	// so its ~15 max?

	// slots 0-2 are always there, the rest are reserved for the games themselves

	protected ItemStackHandler items = new ItemStackHandler(14) {
		protected void onContentsChanged(int slot){
			super.onContentsChanged(slot);
			markDirty();
		}
	};

	public AspectBattery getVisBattery(){
		return getVisShareablesAsBattery();
	}

	//TODO: There is better way to do it
	private AspectBattery getVisShareablesAsBattery() {
		battery.clear();
		BlockPos.getAllInBox(getPos().north(4).east(4).up(4), getPos().south(4).west(4).down(2)).forEach(blockPos -> {
			if(world.getBlockState(blockPos).hasTileEntity()){
				TileEntity tileEntityInBox = world.getTileEntity(blockPos);
				if(tileEntityInBox != null)
					if(tileEntityInBox instanceof VisShareable)
						if(((VisShareable)tileEntityInBox).isVisShareable() && ((VisShareable)tileEntityInBox).isManual()){
							AspectBattery vis = (AspectBattery)IAspectHandler.getFrom(tileEntityInBox);
							if(vis != null){
								visContainers.add(new BlockPos(blockPos)); // Removing reference
								AspectBattery.merge(battery, vis);
							}
						}
			}
		});
		return battery;
	}

	@Override
	public void read(BlockState state, CompoundNBT compound){
		super.read(state, compound);
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
	protected ITextComponent getDefaultName(){
		return new StringTextComponent("research_table");
	}

	@Override
	protected Container createMenu(int id, PlayerInventory player){
		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer(8, 8));
		buffer.writeBlockPos(pos);
		return new ResearchTableContainer(id, player, buffer);
	}

	@Nonnull
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing){
		if(capability == AspectHandlerCapability.ASPECT_HANDLER){
			AspectBattery battery = getVisBattery();
			return battery.getCapability(AspectHandlerCapability.ASPECT_HANDLER).cast();
		}
		return super.getCapability(capability, facing).cast();
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap){
		return this.getCapability(cap, null);
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

	@Override
	public int getSizeInventory(){
		return items.getSlots();
	}

	@Override
	public boolean isEmpty(){
		return false;
	}

	@Override
	public ItemStack getStackInSlot(int index){
		return items.getStackInSlot(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count){
		ItemStack stack0 = items.getStackInSlot(index);
		ItemStack stack1 = items.getStackInSlot(index).copy();
		stack0.shrink(count);
		stack1.setCount(count);
		return stack1; //TODO: Check of works fine (custom impl)
	}

	@Override
	public ItemStack removeStackFromSlot(int index){
		ItemStack stack = this.items.getStackInSlot(index).copy();
		items.getStackInSlot(index).setCount(0);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack){
		items.setStackInSlot(index, stack);
		if(stack.getCount() > getInventoryStackLimit())
			stack.setCount(getInventoryStackLimit());
		if (world != null) {
			world.setBlockState(pos, world.getBlockState(pos).with(INK, !ink().isEmpty()));
			Direction facing = world.getBlockState(pos).get(FACING);
			BlockPos componentPos = pos.add(-facing.getZOffset(), facing.getYOffset(), facing.getXOffset());
			if (world.getBlockState(componentPos).getBlock() == ArcanaBlocks.RESEARCH_TABLE_COMPONENT.get()) {
				world.setBlockState(componentPos, world.getBlockState(componentPos).with(PAPER, !note().isEmpty()));
			}
		}
	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player){
		if(world.getTileEntity(pos) != this)
			return false;
		return player.getDistanceSq(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5) <= 64;
	}

	@Override
	public void clear(){
		for(int i = 0; i < items.getSlots() - 1; i++)
			items.getStackInSlot(i).setCount(0);
	}
}