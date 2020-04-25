package net.kineticdevelopment.arcana.common.containers;

import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.client.gui.ResearchTableGUI;
import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.common.objects.tile.ResearchTableTileEntity;
import net.kineticdevelopment.arcana.core.aspects.Aspect;
import net.kineticdevelopment.arcana.core.aspects.AspectHandler;
import net.kineticdevelopment.arcana.core.aspects.AspectHandlerCapability;
import net.kineticdevelopment.arcana.core.aspects.Aspects;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.kineticdevelopment.arcana.core.research.ResearchBooks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ResearchTableContainer extends AspectContainer{
	
	public static final int WIDTH = 376;
	public static final int HEIGHT = 280;
	
	private ResearchTableTileEntity te;
	public List<AspectSlot> scrollableSlots = new ArrayList<>();
	
	// combination slots
	protected AspectSlot leftStoreSlot, rightStoreSlot;
	
	private ItemStack note;
	public final List<AspectSlot> puzzleSlots = new ArrayList<>();
	public final List<Slot> puzzleItemSlots = new ArrayList<>();
	public IInventory puzzleInventorySlots;
	EntityPlayer lastClickPlayer;
	
	public ResearchTableContainer(IInventory playerInventory, ResearchTableTileEntity te){
		this.te = te;
		addOwnSlots();
		addPlayerSlots(playerInventory);
		addAspectSlots();
	}
	
	private void addPlayerSlots(IInventory playerInventory){
		int baseX = 139, baseY = ResearchTableGUI.HEIGHT - 61;
		// Slots for the main inventory
		for(int row = 0; row < 3; row++)
			for(int col = 0; col < 9; col++){
				int x = baseX + col * 18;
				int y = row * 18 + baseY;
				addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
			}
		
		for(int row = 0; row < 3; ++row)
			for(int col = 0; col < 3; ++col){
				int x = 79 + col * 18;
				int y = row * 18 + baseY;
				addSlotToContainer(new Slot(playerInventory, col + row * 3, x, y));
			}
	}
	
	private void addOwnSlots(){
		IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		note = te.note();
		// 9, 10
		addSlotToContainer(new SlotItemHandler(itemHandler, 0, 9, 10){
			public boolean isItemValid(@Nonnull ItemStack stack){
				// only vis storages
				return super.isItemValid(stack) && stack.hasCapability(AspectHandlerCapability.ASPECT_HANDLER, null);
			}
		});
		// 137, 11
		addSlotToContainer(new SlotItemHandler(itemHandler, 1, 137, 11){
			public boolean isItemValid(@Nonnull ItemStack stack){
				// only ink
				return super.isItemValid(stack) && stack.getItem() == ItemInit.INK;
			}
		});
		// 155, 11
		addSlotToContainer(new SlotItemHandler(itemHandler, 2, 155, 11){
			public boolean isItemValid(@Nonnull ItemStack stack){
				// only notes
				return super.isItemValid(stack) && stack.getItem() == ItemInit.RESEARCH_NOTE || stack.getItem() == ItemInit.RESEARCH_NOTE_COMPLETE;
			}
			
			public void onSlotChanged(){
				super.onSlotChanged();
				if(!ItemStack.areItemStacksEqual(note, te.note())){
					note = te.note();
					// remove added slots * aspect slots
					refreshPuzzleSlots();
				}
			}
		});
	}
	
	public ItemStack slotClick(int slot, int dragType, ClickType clickType, EntityPlayer player){
		lastClickPlayer = player;
		return super.slotClick(slot, dragType, clickType, player);
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
			inventoryItemStacks.remove(slot.slotNumber);
			inventorySlots.remove(slot);
		}
		puzzleItemSlots.clear();
		
		getFromNote().ifPresent(puzzle -> {
			if(note.getItem() == ItemInit.RESEARCH_NOTE){
				for(AspectSlot slot : puzzle.getAspectSlots(() -> AspectHandler.getFrom(te))){
					puzzleSlots.add(slot);
					aspectSlots.add(slot);
				}
				List<Puzzle.SlotInfo> locations = puzzle.getItemSlotLocations(lastClickPlayer);
				int size = locations.size();
				puzzleInventorySlots = new InventoryBasic("", false, size){
					public void markDirty(){
						super.markDirty();
						ResearchTableContainer.this.onCraftMatrixChanged(this);
					}
				};
				for(int i = 0; i < locations.size(); i++){
					Puzzle.SlotInfo slotInfo = locations.get(i);
					Slot slot = new Slot(puzzleInventorySlots, i, slotInfo.x, slotInfo.y){
						public int getSlotStackLimit(){
							return slotInfo.max != -1 ? slotInfo.max : super.getSlotStackLimit();
						}
					};
					if(slotInfo.bg != null){
						slot.setBackgroundLocation(slotInfo.bg);
						slot.setBackgroundName(slotInfo.bg.toString());
					}
					addSlotToContainer(slot);
					puzzleItemSlots.add(slot);
				}
			}
		});
	}
	
	public void onContainerClosed(@Nonnull EntityPlayer player){
		super.onContainerClosed(player);
		if(puzzleInventorySlots != null)
			if(!player.world.isRemote)
				clearContainer(player, player.world, puzzleInventorySlots);
	}
	
	protected void addAspectSlots(){
		Supplier<AspectHandler> aspects = () -> AspectHandler.getFrom(te.visItem());
		for(int i = 0; i < Aspects.primalAspects.length; i++){
			Aspect primal = Aspects.primalAspects[i];
			int x = 31 + 16 * i;
			int y = 14;
			if(i % 2 == 0)
				y += 5;
			getAspectSlots().add(new AspectSlot(primal, aspects, x, y));
		}
		Aspect[] values = Aspect.values();
		Supplier<AspectHandler> table = () -> AspectHandler.getFrom(te);
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
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index){
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		
		if(slot != null && slot.getHasStack()){
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
		
		return itemstack;
	}
	
	public void onAspectSlotChange(){
		if(getFromNote().map(puzzle -> puzzle.validate(puzzleSlots, null)).orElse(false)){
			ItemStack complete = new ItemStack(ItemInit.RESEARCH_NOTE_COMPLETE);
			NBTTagCompound data = note.getTagCompound();
			
			// I might store e.g. chemistry data for display in the future.
			complete.setTagCompound(data);
			
			// Don't close them, because that will move aspects over to the main table.
			// This will need changing if slots need to be able to clean up arbitrary resources.
			aspectSlots.removeAll(puzzleSlots);
			puzzleSlots.clear();
			
			IItemHandler capability = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
			if(capability != null){
				capability.extractItem(2, 64, false);
				capability.insertItem(2, complete, false);
			}
		}
	}
	
	public Optional<Puzzle> getFromNote(){
		if(!note.isEmpty() && note.getTagCompound() != null && note.getTagCompound().hasKey("puzzle"))
			return Optional.ofNullable(ResearchBooks.puzzles.get(new ResourceLocation(note.getTagCompound().getString("puzzle"))));
		else
			return Optional.empty();
	}
	
	public boolean canInteractWith(EntityPlayer player){
		return true;
	}
	
	public List<AspectHandler> getOpenHandlers(){
		AspectHandler item = AspectHandler.getFrom(te.visItem());
		if(item != null)
			return Arrays.asList(AspectHandler.getFrom(te), item);
		else
			return Collections.singletonList(AspectHandler.getFrom(te));
	}
}
