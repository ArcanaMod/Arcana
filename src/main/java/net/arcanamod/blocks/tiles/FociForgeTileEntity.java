package net.arcanamod.blocks.tiles;

import io.netty.buffer.Unpooled;
import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.AspectBattery;
import net.arcanamod.aspects.AspectHandlerCapability;
import net.arcanamod.containers.FociForgeContainer;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.systems.spell.Spell;
import net.arcanamod.systems.spell.SpellState;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.UUID;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FociForgeTileEntity extends LockableTileEntity{

	public Spell currentSpell = null;
	public SpellState spellState = new SpellState();
	public FociForgeTileEntity(){
		super(ArcanaTiles.FOCI_FORGE_TE.get());
	}

	protected ItemStackHandler items = new ItemStackHandler(2){
		protected void onContentsChanged(int slot){
			super.onContentsChanged(slot);
			markDirty();
		}
	};

	// Read from file
	// wipes the floating module list, resets spellState sequence
	@Override
	public void read(BlockState state, CompoundNBT compound){
		super.read(state, compound);
		if (compound.contains("items")) {
			items.deserializeNBT(compound.getCompound("items"));
		}
		if (compound.contains("spellstate")) {
			spellState = SpellState.fromNBT(compound.getCompound("spellstate"));
			spellState.sequence = 0;
			spellState.floating.clear();
		}
	}

	// Read from packet
	// update spellState sequence, adjust current module
	public void readPacket(CompoundNBT compound){
		super.read(getBlockState(), compound);
		if (compound.contains("items")) {
			items.deserializeNBT(compound.getCompound("items"));
		}
		if (compound.contains("spellstate")) {
			CompoundNBT state = compound.getCompound("spellstate");
			spellState = SpellState.fromNBT(state);
			spellState.sequence = state.getInt("sequence");
			UUID player = Arcana.proxy.getPlayerOnClient().getUniqueID();
			if (spellState.floating.containsValue(player)) {
				spellState.activeModule = spellState.floating.inverse().get(player);
				spellState.activeModuleIndex = -1;
			}
		}
	}

	// write spellState to NBT
	@Override
	public CompoundNBT write(CompoundNBT compound){
		super.write(compound);
		compound.put("items", items.serializeNBT());
		compound.put("spellstate", spellState.toNBT(new CompoundNBT()));
		return compound;
	}

	// On server send, create tag for initial chunk load
	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT state = write(new CompoundNBT());
		state.getCompound("spellstate").putInt("sequence", spellState.sequence);
		return state;
	}

	// On server send, update tile already loaded
	// just send the entire block again
	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbt = this.write(new CompoundNBT());
		return new SUpdateTileEntityPacket(this.getPos(), 0, nbt);
	}

	// On client receive, update tile already loaded
	// just recreate the entire block again, nothing lost
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
		this.readPacket(packet.getNbtCompound());
	}
	
	@Override
	protected ITextComponent getDefaultName(){
		return new TranslationTextComponent("container.arcana.foci_forge");
	}
	
	@Override
	protected Container createMenu(int id, PlayerInventory player) {
		PacketBuffer buffer = new PacketBuffer(Unpooled.buffer(8,8));
		buffer.writeBlockPos(pos);
		return new FociForgeContainer(id,player,buffer);
	}

	// TODO: Required to show aspects on screen!
	@Nonnull
	public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing){
		if(capability == AspectHandlerCapability.ASPECT_HANDLER) {
			AspectBattery battery = new AspectBattery(0, 0);
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

	public ItemStack focus(){
		return items.getStackInSlot(1);
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
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
		return this.items.getStackInSlot(index);
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
		if(stack.getCount() > getInventoryStackLimit()){
			stack.setCount(getInventoryStackLimit());
		}
	}
	
	@Override
	public boolean isUsableByPlayer(PlayerEntity player){
		if(world.getTileEntity(pos) != this){
			return false;
		}else{
			return player.getDistanceSq(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5) <= 64;
		}
	}
	
	@Override
	public void clear(){
		for(int i = 0; i < items.getSlots() - 1; i++){
			items.getStackInSlot(i).setCount(0);
		}
	}

	public void replaceSpell(@Nonnull Spell newSpell) {
		currentSpell = newSpell;
		spellState.replaceSpell(currentSpell);
		markDirty();
	}
}
