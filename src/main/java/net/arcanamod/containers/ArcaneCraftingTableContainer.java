package net.arcanamod.containers;

import net.arcanamod.containers.slots.WandSlot;
import net.arcanamod.util.inventories.AspectCraftingInventory;
import net.arcanamod.util.recipes.ArcanaRecipes;
import net.arcanamod.util.recipes.IArcaneCraftingRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;

public class ArcaneCraftingTableContainer extends RecipeBookContainer<AspectCraftingInventory> {

	public final IInventory inventory;
	private final PlayerInventory playerInventory;
	private final AspectCraftingInventory craftMatrix;
	private final CraftResultInventory craftResult = new CraftResultInventory();

	public ArcaneCraftingTableContainer(@Nullable ContainerType<?> type, int id, PlayerInventory playerInventory, IInventory inventory){
		super(type, id);
		this.inventory = inventory;
		this.playerInventory = playerInventory;
		WandSlot wandSlot = new WandSlot(inventory, 1, 160, 18);
		this.craftMatrix = new AspectCraftingInventory(this,wandSlot, 3, 3);
		this.addSlot(new CraftingResultSlot(playerInventory.player, this.craftMatrix, this.craftResult, 0, 160, 64));
		this.addSlot(wandSlot);
		for(int i = 0; i < 3; ++i) {
			for(int j = 0; j < 3; ++j) {
				this.addSlot(new Slot(craftMatrix,j + i * 3, 42 + j * 23, 41 + i * 23));
			}
		}
		addPlayerSlots(playerInventory);
	}

	public ArcaneCraftingTableContainer(int id, PlayerInventory playerInventory, IInventory inventory){
		this(ArcanaContainers.ARCANE_CRAFTING_TABLE.get(), id, playerInventory, inventory);
	}

	public ArcaneCraftingTableContainer(int i, PlayerInventory playerInventory, PacketBuffer packetBuffer){
		this(ArcanaContainers.ARCANE_CRAFTING_TABLE.get(), i, playerInventory, new Inventory(2));
	}

	protected static void craft(int windowIdIn, World world, PlayerEntity playerEntity, AspectCraftingInventory craftingInventory, CraftResultInventory resultInventory){
		if (!world.isRemote) {
			ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)playerEntity;
			ItemStack itemstack = ItemStack.EMPTY;
			// look for arcane crafting
			Optional<IArcaneCraftingRecipe> optional = world.getServer().getRecipeManager().getRecipe(ArcanaRecipes.Types.ARCANE_CRAFTING_SHAPED, craftingInventory, world);
			if (optional.isPresent()) {
				IArcaneCraftingRecipe iarcanecraftingrecipe = optional.get();
				if (resultInventory.canUseRecipe(world, serverplayerentity, iarcanecraftingrecipe)) {
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
			serverplayerentity.connection.sendPacket(new SSetSlotPacket(windowIdIn, 0, itemstack));
		}
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	public void onCraftMatrixChanged(IInventory inventoryIn){
		craft(this.windowId, this.playerInventory.player.world, this.playerInventory.player, this.craftMatrix, this.craftResult);
	}

	/**
	 * Determines whether supplied player can use this container
	 *
	 * @param playerIn
	 */
	@Override
	public boolean canInteractWith(PlayerEntity playerIn){
		return this.inventory == null || this.inventory.isUsableByPlayer(playerIn);
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
	public void fillStackedContents(RecipeItemHelper itemHelperIn){
		this.craftMatrix.fillStackedContents(itemHelperIn);
	}

	@Override
	public void clear(){
		this.inventory.clear();
	}

	@Override
	public boolean matches(IRecipe<? super AspectCraftingInventory> recipeIn){
		return recipeIn.matches(this.craftMatrix, this.playerInventory.player.world);
	}

	@Override
	public int getOutputSlot(){
		return 0;
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

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	// TODO: Pls someone can fix that?????????
	public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (index == 0) {
				itemstack1.getItem().onCreated(itemstack1, playerIn.world, playerIn);
				if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			} else if (index >= 10 && index < 46) {
				if (!this.mergeItemStack(itemstack1, 1, 10, false)) {
					if (index < 37) {
						if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
							return ItemStack.EMPTY;
						}
					} else if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
						return ItemStack.EMPTY;
					}
				}
			} else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
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

			ItemStack itemstack2 = slot.onTake(playerIn, itemstack1);
			if (index == 0) {
				playerIn.dropItem(itemstack2, false);
			}
		}

		return itemstack;
	}
}
