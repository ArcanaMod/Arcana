package net.arcanamod.containers;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.blocks.tiles.FociForgeTileEntity;
import net.arcanamod.client.gui.FociForgeScreen;
import net.arcanamod.containers.slots.AspectSlot;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.ArcanaTags;
import net.arcanamod.items.MagicDeviceItem;
import net.arcanamod.systems.research.Puzzle;
import net.arcanamod.systems.research.ResearchBooks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
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
import java.util.*;
import java.util.function.Supplier;

public class FociForgeContainer extends AspectContainer {
    protected FociForgeContainer(@Nullable ContainerType<?> type, int id){
        super(type, id);
    }

    public FociForgeTileEntity te;
    public List<AspectSlot> scrollableSlots = new ArrayList<>();

    public IInventory puzzleInventorySlots;
    PlayerEntity lastClickPlayer;

    public FociForgeContainer(ContainerType type, int id, IInventory playerInventory, FociForgeTileEntity te){
        super(type,id);
        this.te = te;
        addOwnSlots(playerInventory);
        addPlayerSlots(playerInventory);
        addAspectSlots(playerInventory);
    }

    @SuppressWarnings("ConstantConditions")
    public FociForgeContainer(int i, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        this(ArcanaContainers.FOCI_FORGE.get(),i,playerInventory,(FociForgeTileEntity) playerInventory.player.getEntityWorld().getTileEntity(packetBuffer.readBlockPos()));
    }

    private void addPlayerSlots(IInventory playerInventory){
        int baseX = 139, baseY = FociForgeScreen.HEIGHT - 61;
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

    private void addOwnSlots(IInventory playerInventory){
        @SuppressWarnings("ConstantConditions")
        IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
        // 137, 11
        addSlot(new SlotItemHandler(itemHandler, 0, 137, 11){
            public boolean isItemValid(@Nonnull ItemStack stack){
                // only ink
                return super.isItemValid(stack) && stack.getItem() instanceof MagicDeviceItem;
            }

            public void onSlotChanged(){
                super.onSlotChanged();
            }
        });
        // 155, 11
        addSlot(new SlotItemHandler(itemHandler, 1, 155, 11){
            public boolean isItemValid(@Nonnull ItemStack stack){
                // only notes
                return super.isItemValid(stack) && stack.getItem() == ArcanaItems.FOCUS_PARTS.get() || stack.getItem() == ArcanaItems.DEFAULT_FOCUS.get();
            }

            public void onSlotChanged(){
                super.onSlotChanged();
            }
        });
    }

    public ItemStack slotClick(int slot, int dragType, ClickType clickType, PlayerEntity player){
        lastClickPlayer = player;
        ItemStack stack = super.slotClick(slot, dragType, clickType, player);
        return stack;
    }

    public void onContainerClosed(@Nonnull PlayerEntity player){
        super.onContainerClosed(player);
        if(puzzleInventorySlots != null)
            if(!player.world.isRemote)
                clearContainer(player, player.world, puzzleInventorySlots);
    }

    protected void addAspectSlots(IInventory playerInventory){
        Aspect[] values = (Aspect[]) Aspects.getWithoutEmpty().toArray();
        Supplier<IAspectHandler> table = () -> IAspectHandler.getFrom(te);
        for(int i = 0; i < values.length; i++){
            Aspect aspect = values[i];
            int yy = i / 6;
            int xx = i % 6;
            boolean visible = true;
            if(yy >= 6){
                visible = false;
                // wrap
                yy %= 6;
            }
            int x = 11 + 20 * xx;
            int y = 32 + 21 * yy;
            if(xx % 2 == 0)
                y += 5;
            AspectSlot slot = new AspectSlot(aspect, table, x, y);
            slot.visible = visible;
            getAspectSlots().add(slot);
            scrollableSlots.add(slot);
        }
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index){
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if(slot != null && slot.getHasStack()){
            if(index != 1){
                ItemStack itemstack1 = slot.getStack();
                itemstack = itemstack1.copy();

                if(index < 2){
                    if(!this.mergeItemStack(itemstack1, 2, inventorySlots.size(), true))
                        return ItemStack.EMPTY;
                }else if(!this.mergeItemStack(itemstack1, 0, 2, false))
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
    public List<IAspectHandler> getOpenHandlers(){
        return Collections.singletonList(IAspectHandler.getFrom(te));
    }
}