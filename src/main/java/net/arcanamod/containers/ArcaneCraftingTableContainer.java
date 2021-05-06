package net.arcanamod.containers;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.containers.slots.AspectCraftingResultSlot;
import net.arcanamod.containers.slots.IWandSlotListener;
import net.arcanamod.containers.slots.WandSlot;
import net.arcanamod.items.recipes.AspectCraftingInventory;
import net.arcanamod.items.recipes.ArcanaRecipes;
import net.arcanamod.items.recipes.IArcaneCraftingRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ArcaneCraftingTableContainer extends RecipeBookContainer<AspectCraftingInventory> implements IWandSlotListener {

	public final IInventory inventory;
	public final PlayerInventory playerInventory;
	public final AspectCraftingInventory craftMatrix;
	public final CraftResultInventory craftResult = new CraftResultInventory();
	public final int craftResultSlot;

	public ArcaneCraftingTableContainer(ContainerType<?> type, int id, PlayerInventory playerInventory, IInventory inventory){
		super(type, id);
		this.inventory = inventory;
		this.playerInventory = playerInventory;
		WandSlot wandSlot = new WandSlot(this, inventory, 10, 160, 18);
		this.craftMatrix = new AspectCraftingInventory(this, wandSlot, 3, 3, inventory);
		this.addSlot(new AspectCraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 160, 64));
		this.craftResultSlot = 1;
		this.addSlot(wandSlot);
		for(int i = 0; i < 3; ++i)
			for(int j = 0; j < 3; ++j)
				this.addSlot(new Slot(craftMatrix, j + i * 3, 42 + j * 23, 41 + i * 23));
		addPlayerSlots(playerInventory);
		// guarantee craft() called on serverside
		craft(this.windowId, this.playerInventory.player.world, this.playerInventory.player, this.craftMatrix, this.craftResult);
	}

	public ArcaneCraftingTableContainer(int id, PlayerInventory playerInventory, IInventory inventory){
		this(ArcanaContainers.ARCANE_CRAFTING_TABLE.get(), id, playerInventory, inventory);
	}

	public ArcaneCraftingTableContainer(int i, PlayerInventory playerInventory){
		this(ArcanaContainers.ARCANE_CRAFTING_TABLE.get(), i, playerInventory, new Inventory(10));
	}

	protected static void craft(int windowId, World world, PlayerEntity playerEntity, AspectCraftingInventory craftingInventory, CraftResultInventory resultInventory){
		if (!world.isRemote) {
			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)playerEntity;
			ItemStack itemstack = ItemStack.EMPTY;
			// look for arcane crafting
			if(world.getServer() != null){
				Optional<IArcaneCraftingRecipe> optional = world.getServer().getRecipeManager().getRecipe(ArcanaRecipes.Types.ARCANE_CRAFTING_SHAPED, craftingInventory, world);
				if(optional.isPresent()){
					IArcaneCraftingRecipe iarcanecraftingrecipe = optional.get();
					if(resultInventory.canUseRecipe(world, serverplayerentity, iarcanecraftingrecipe)){
						itemstack = iarcanecraftingrecipe.getCraftingResult(craftingInventory);
					}
				}
				// if arcane crafting is not possible, look for regular crafting
				else{
					Optional<ICraftingRecipe> craftingOptional = world.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftingInventory, world);
					if(craftingOptional.isPresent()){
						ICraftingRecipe recipe = craftingOptional.get();
						if(resultInventory.canUseRecipe(world, serverplayerentity, recipe))
							itemstack = recipe.getCraftingResult(craftingInventory);
					}
				}
				resultInventory.setInventorySlotContents(0, itemstack);
				serverplayerentity.connection.sendPacket(new SSetSlotPacket(windowId, 0, itemstack));
			}
		}
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	public void onCraftMatrixChanged(IInventory inventory){
		craft(this.windowId, this.playerInventory.player.world, this.playerInventory.player, this.craftMatrix, this.craftResult);
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean canInteractWith(PlayerEntity player){
		return inventory == null || inventory.isUsableByPlayer(player);
	}

	private void addPlayerSlots(IInventory playerInventory){
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 16 + j * 18, 151 + i * 18));
			}
		}

		for(int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 16 + k * 18, 209));
		}
	}

	@Override
	public void fillStackedContents(RecipeItemHelper itemHelper){
		craftMatrix.fillStackedContents(itemHelper);
	}

	@Override
	public void clear(){
		this.inventory.clear();
	}

	@Override
	public boolean matches(IRecipe<? super AspectCraftingInventory> recipe){
		return recipe.matches(craftMatrix, playerInventory.player.world);
	}

	@Override
	public int getOutputSlot(){
		return craftResultSlot;
	}

	@Override
	public int getWidth(){
		return 3;
	}

	@Override
	public int getHeight(){
		return 3;
	}

	@Override
	public int getSize(){
		return craftMatrix.getSizeInventory();
	}
	
	public RecipeBookCategory func_241850_m(){
		return RecipeBookCategory.CRAFTING;
	}
	
	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	public ItemStack transferStackInSlot(PlayerEntity player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index == 0) {
				itemstack1.getItem().onCreated(itemstack1, player.world, player);
				if (!mergeItemStack(itemstack1, 11, 47, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 11 && index < 47) {
				if (!mergeItemStack(itemstack1, 1, 11, false)) {
					if (index < 38) {  // prioritize hotbar
						if (!mergeItemStack(itemstack1, 38, 47, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!mergeItemStack(itemstack1, 11, 38, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!mergeItemStack(itemstack1, 11, 47, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			ItemStack itemstack2 = slot.onTake(player, itemstack1);
			if (index == 0) {
				player.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}

	@Override
	public void onWandSlotUpdate() {
		craft(windowId, playerInventory.player.world, playerInventory.player, craftMatrix, craftResult);
	}
	
	public void onContainerClosed(PlayerEntity player){
		super.onContainerClosed(player);
	}
}