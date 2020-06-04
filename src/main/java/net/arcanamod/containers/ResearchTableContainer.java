package net.arcanamod.containers;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.VisHandler;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
import net.arcanamod.client.gui.ResearchTableScreen;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.research.Puzzle;
import net.arcanamod.research.ResearchBooks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ResearchTableContainer extends AspectContainer{
	
	protected ResearchTableContainer(@Nullable ContainerType<?> type, int id){
		super(type, id);
	}

	public static final int WIDTH = 376;
	public static final int HEIGHT = 280;
	
	public ResearchTableTileEntity te;
	public List<AspectSlot> scrollableSlots = new ArrayList<>();
	
	// combination slots
	protected AspectSlot leftStoreSlot, rightStoreSlot;
	
	private ItemStack note, ink;
	public final List<AspectSlot> puzzleSlots = new ArrayList<>();
	public final List<Slot> puzzleItemSlots = new ArrayList<>();
	public IInventory puzzleInventorySlots;
	PlayerEntity lastClickPlayer;
	
	public ResearchTableContainer(ContainerType type, int id, IInventory playerInventory, ResearchTableTileEntity te){
		super(type,id);
		this.te = te;
		addOwnSlots();
		addPlayerSlots(playerInventory);
		addAspectSlots();
	}

	@SuppressWarnings("ConstantConditions")
	public ResearchTableContainer(int i, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
		this(ArcanaContainers.REASERCH_TABLE.get(),i,playerInventory,(ResearchTableTileEntity) playerInventory.player.getEntityWorld().getTileEntity(packetBuffer.readBlockPos()));
	}

	private void addPlayerSlots(IInventory playerInventory){
		int baseX = 139, baseY = ResearchTableScreen.HEIGHT - 61;
		// Slots for the main inventory
		for(int row = 0; row < 3; row++)
			for(int col = 0; col < 9; col++){
				int x = baseX + col * 18;
				int y = row * 18 + baseY;
				addSlot(new Slot(playerInventory, col + row * 9 + 9, x, y));
			}
		
		for(int row = 0; row < 3; ++row)
			for(int col = 0; col < 3; ++col){
				int x = 79 + col * 18;
				int y = row * 18 + baseY;
				addSlot(new Slot(playerInventory, col + row * 3, x, y));
			}
	}
	
	private void addOwnSlots(){
		IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
		note = te.note();
		ink = te.ink();
		// 137, 11
		addSlot(new SlotItemHandler(itemHandler, 1, 137, 11){
			public boolean isItemValid(@Nonnull ItemStack stack){
				// only ink
				return super.isItemValid(stack) && stack.getItem() == ArcanaItems.SCRIBING_TOOLS.get();
			}
			
			public void onSlotChanged(){
				super.onSlotChanged();
				if((ink.isEmpty() && !te.ink().isEmpty()) || (!ink.isEmpty() && te.ink().isEmpty())){
					ink = te.ink();
					refreshPuzzleSlots();
				}
			}
		});
		// 155, 11
		addSlot(new SlotItemHandler(itemHandler, 2, 155, 11){
			public boolean isItemValid(@Nonnull ItemStack stack){
				// only notes
				return super.isItemValid(stack) && stack.getItem() == ArcanaItems.RESEARCH_NOTE.get() || stack.getItem() == ArcanaItems.RESEARCH_NOTE_COMPLETE.get();
			}
			
			public void onSlotChanged(){
				super.onSlotChanged();
				if(!ItemStack.areItemStacksEqual(note, te.note())){
					note = te.note();
					// remove added slots & aspect slots
					refreshPuzzleSlots();
				}
			}
		});
	}
	
	public ItemStack slotClick(int slot, int dragType, ClickType clickType, PlayerEntity player){
		lastClickPlayer = player;
		ItemStack stack = super.slotClick(slot, dragType, clickType, player);
		if(!ink.isEmpty())
			validate();
		return stack;
	}
	
	private void refreshPuzzleSlots(){
		aspectSlots.removeAll(puzzleSlots);
		puzzleSlots.forEach(AspectSlot::onClose);
		puzzleSlots.clear();
		
		if(puzzleInventorySlots != null)
			if(!lastClickPlayer.world.isRemote)
				clearContainer(lastClickPlayer, lastClickPlayer.world, puzzleInventorySlots);
		
		for(int i = puzzleItemSlots.size() - 1; i >= 0; i--){
			Slot slot = puzzleItemSlots.get(i);
			//inventoryItemStacks.remove(slot.slotNumber);
			inventorySlots.remove(slot);
		}
		puzzleItemSlots.clear();
		
		getFromNote().ifPresent(puzzle -> {
			if(!ink.isEmpty())
				if(note.getItem() == ArcanaItems.RESEARCH_NOTE.get()){
					for(AspectSlot slot : puzzle.getAspectSlots(() -> VisHandler.getFrom(te))){
						puzzleSlots.add(slot);
						aspectSlots.add(slot);
					}
					List<Puzzle.SlotInfo> locations = puzzle.getItemSlotLocations(lastClickPlayer);
					int size = locations.size();
					/*puzzleInventorySlots = new Inventory("", false, size){
						public void markDirty(){
							super.markDirty();
							ResearchTableContainer.this.onCraftMatrixChanged(this);
						}
					};*/
					for(int i = 0; i < locations.size(); i++){
						Puzzle.SlotInfo slotInfo = locations.get(i);
						Slot slot = new Slot(puzzleInventorySlots, i, slotInfo.x, slotInfo.y){
							public int getSlotStackLimit(){
								return slotInfo.max != -1 ? slotInfo.max : super.getSlotStackLimit();
							}
						};
						//if(slotInfo.bg_name != null)
						//	slot.setBackgroundName(slotInfo.bg_name);
						addSlot(slot);
						puzzleItemSlots.add(slot);
					}
				}
		});
	}
	
	public void onContainerClosed(@Nonnull PlayerEntity player){
		super.onContainerClosed(player);
		if(puzzleInventorySlots != null)
			if(!player.world.isRemote)
				clearContainer(player, player.world, puzzleInventorySlots);
	}
	
	protected void addAspectSlots(){
		Supplier<VisHandler> aspects = () -> VisHandler.getFrom(te.visItem());
		for(int i = 0; i < Aspects.primalAspects.length; i++){
			Aspect primal = Aspects.primalAspects[i];
			int x = 31 + 16 * i;
			int y = 14;
			if(i % 2 == 0)
				y += 5;
			getAspectSlots().add(new AspectSlot(primal, aspects, x, y));
		}
		Aspect[] values = (Aspect[]) Aspect.aspects.toArray();
		Supplier<VisHandler> table = () -> VisHandler.getFrom(te);
		for(int i = 0; i < values.length; i++){
			Aspect aspect = values[i];
			int yy = i / 6;
			int xx = i % 6;
			boolean visible = true;
			if(yy >= 5){
				visible = false;
				// wrap
				yy %= 5;
			}
			int x = 11 + 20 * xx;
			int y = 65 + 21 * yy;
			if(xx % 2 == 0)
				y += 5;
			AspectSlot slot = new AspectSlot(aspect, table, x, y);
			slot.visible = visible;
			getAspectSlots().add(slot);
			scrollableSlots.add(slot);
		}
		// combinator slots
		aspectSlots.add(leftStoreSlot = new AspectStoreSlot(table, 30, 179));
		aspectSlots.add(rightStoreSlot = new AspectStoreSlot(table, 92, 179));
		aspectSlots.add(new CombinatorAspectSlot(leftStoreSlot, rightStoreSlot, 61, 179));
		
		refreshPuzzleSlots();
	}
	
	@Override
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index){
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack()){
			if(note.isEmpty() || index != 2){
				ItemStack itemstack1 = slot.getStack();
				itemstack = itemstack1.copy();
				
				if(index < 3){
					if(!this.mergeItemStack(itemstack1, 3, inventorySlots.size(), true))
						return ItemStack.EMPTY;
				}else if(!this.mergeItemStack(itemstack1, 0, 3, false))
					return ItemStack.EMPTY;
				
				if(itemstack1.isEmpty())
					slot.putStack(ItemStack.EMPTY);
				else
					slot.onSlotChanged();
			}
		}
		
		return itemstack;
	}
	
	public void onAspectSlotChange(){
		super.onAspectSlotChange();
		if(!ink.isEmpty())
			validate();
	}
	
	public void validate(){
		if(getFromNote().map(puzzle -> puzzle.validate(puzzleSlots, puzzleItemSlots, lastClickPlayer, this)).orElse(false)){
			ItemStack complete = new ItemStack(ArcanaItems.RESEARCH_NOTE_COMPLETE.get());
			CompoundNBT data = note.getTag();
			
			// I might store e.g. chemistry data for display in the future.
			complete.setTag(data);
			
			// Don't close them, because that will move aspects over to the main table.
			// This will need changing if slots need to be able to clean up arbitrary resources.
			aspectSlots.removeAll(puzzleSlots);
			puzzleSlots.clear();
			
			for(int i = puzzleItemSlots.size() - 1; i >= 0; i--){
				Slot slot = puzzleItemSlots.get(i);
				//inventoryItemStacks.remove(slot.slotNumber);
				inventorySlots.remove(slot);
			}
			puzzleItemSlots.clear();
			puzzleInventorySlots = null;
			
			te.ink().damageItem(1, lastClickPlayer,this::onInkBreak);
			
			IItemHandler capability = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
			if(capability != null){
				capability.extractItem(2, 64, false);
				capability.insertItem(2, complete, false);
			}
		}
	}
	
	public Optional<Puzzle> getFromNote(){
		if(!note.isEmpty() && note.getTag() != null && note.getTag().contains("puzzle"))
			return Optional.ofNullable(ResearchBooks.puzzles.get(new ResourceLocation(note.getTag().getString("puzzle"))));
		else
			return Optional.empty();
	}

	public void onInkBreak(@Nonnull PlayerEntity player) {
	}
	
	public boolean canInteractWith(PlayerEntity player){
		return true;
	}

	/**
	 * Gets a list of every aspect handler that's open; i.e. can be modified in this GUI and might need syncing.
	 * The contents of this list must be the same on the server and client side.
	 *
	 * @return A list containing all open AspectHandlers.
	 */
	public List<VisHandler> getOpenHandlers(){
		VisHandler item = VisHandler.getFrom(te.visItem());
		if(item != null)
			return Arrays.asList(VisHandler.getFrom(te), item);
		else
			return Collections.singletonList(VisHandler.getFrom(te));
	}
}