package net.arcanamod.containers;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.blocks.tiles.FociForgeTileEntity;
import net.arcanamod.client.gui.FociForgeScreen;
import net.arcanamod.containers.slots.AspectSlot;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.MagicDeviceItem;
import net.arcanamod.items.attachment.FocusItem;
import net.arcanamod.network.Connection;
import net.arcanamod.systems.spell.Spell;
import net.arcanamod.systems.spell.modules.core.StartCircle;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FociForgeContainer extends AspectContainer {
    public static final int ASPECT_H_COUNT = 4;
    public static final int ASPECT_V_COUNT = 7;
    public static final Inventory TMP_FOCI = new Inventory(9);

    protected FociForgeContainer(@Nullable ContainerType<?> type, int id){
        super(type, id);
    }

    public FociForgeTileEntity te;
    public List<AspectSlot> scrollableSlots = new ArrayList<>();
    public List<Slot> fociSlots = new ArrayList<>();

    PlayerEntity lastClickPlayer;

    public FociForgeContainer(ContainerType type, int id, IInventory playerInventory, FociForgeTileEntity te){
        super(type,id);
        this.te = te;
        addOwnSlots(playerInventory);
        addPlayerSlots(playerInventory);
        addAspectSlots(playerInventory);
        addFociSlots(playerInventory);
    }

    @SuppressWarnings("ConstantConditions")
    public FociForgeContainer(int i, PlayerInventory playerInventory, PacketBuffer packetBuffer) {
        this(ArcanaContainers.FOCI_FORGE.get(),i,playerInventory,(FociForgeTileEntity) playerInventory.player.getEntityWorld().getTileEntity(packetBuffer.readBlockPos()));
    }

    @OnlyIn(Dist.CLIENT)
    private void addPlayerSlots(IInventory playerInventory){
        int hotX = 88, invX = 148, baseY = FociForgeScreen.HEIGHT - 61;
        // Slots for the main inventory
        for(int row = 0; row < 3; row++)
            for(int col = 0; col < 9; col++){
                int x = invX + col * 18;
                int y = row * 18 + baseY;
                addSlot(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }

        for(int row = 0; row < 3; ++row)
            for(int col = 0; col < 3; ++col){
                int x = hotX + col * 18;
                int y = row * 18 + baseY;
                addSlot(new Slot(playerInventory, col + row * 3, x, y));
            }
    }

    private void addOwnSlots(IInventory playerInventory){
        @SuppressWarnings("ConstantConditions")
        IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElse(null);
        // 303, 11
        addSlot(new SlotItemHandler(itemHandler, 0, 303, 11){
            public boolean isItemValid(@Nonnull ItemStack stack){
                // only ink
                return super.isItemValid(stack) && stack.getItem() instanceof MagicDeviceItem;
            }
        });
        // 361, 12
        addSlot(new SlotItemHandler(itemHandler, 1, 361, 12){
            public boolean isItemValid(@Nonnull ItemStack stack){
                // only foci or foci parts
                return super.isItemValid(stack) && stack.getItem() == ArcanaItems.FOCUS_PARTS.get() || stack.getItem() == ArcanaItems.DEFAULT_FOCUS.get();
            }

            @Override
            public int getItemStackLimit(@Nonnull ItemStack stack) {
                return 1;
            }

            @Override
            public void onSlotChanged() {
                onFociSlotChange();
            }
        });
    }

    private void onFociSlotChange() {
        // if spell changed, keep it until saved or discarded
        // else replace current spell with nothing/new spell
        if (!te.spellState.spellModified) {
            if (te.focus() == ItemStack.EMPTY || te.focus().getItem() == ArcanaItems.FOCUS_PARTS.get()) {
                te.replaceSpell(new Spell(new StartCircle()));
            } else if (te.focus().getItem() == ArcanaItems.DEFAULT_FOCUS.get()) {
                te.replaceSpell(Spell.fromNBT(te.focus().getOrCreateTag()));
            }
        }
    }

    protected void addFociSlots(IInventory playerInventory){
        int SLOT_X = 361;
        int SLOT_Y = 40;
        int SLOT_DELTA = 17;

        for (int yy = 0; yy < FociForgeScreen.FOCI_V_COUNT; yy++) {
            int y = SLOT_Y + SLOT_DELTA * yy;
            Slot slot = new Slot(TMP_FOCI, yy, SLOT_X, y) {
                @Override
                public boolean canTakeStack(PlayerEntity player) {
                    return false;
                }
            };
            addSlot(slot);
            fociSlots.add(slot);
            ItemStack dummyFoci = new ItemStack(ArcanaItems.DEFAULT_FOCUS.get(), 1);
            dummyFoci.getOrCreateTag().putInt("style", yy);
            slot.putStack(dummyFoci);
        }
    }

    public ItemStack slotClick(int slot, int dragType, ClickType clickType, PlayerEntity player){
        lastClickPlayer = player;
        ItemStack stack;
        if (slot >= 0 && slot < inventorySlots.size() && inventorySlots.get(slot).inventory == TMP_FOCI) {
            changeFociStyle(inventorySlots.get(slot).getStack().getOrCreateTag().getInt("style"));
            stack = ItemStack.EMPTY;
        } else {
            stack = super.slotClick(slot, dragType, clickType, player);
        }
        return stack;
    }

    protected void addAspectSlots(IInventory playerInventory){
        Aspect[] primals = AspectUtils.primalAspects;
        Aspect[] sins = AspectUtils.sinAspects;
        Supplier<IAspectHandler> source = () -> IAspectHandler.getFrom(te);

        for (int xx = 0; xx < primals.length; xx++) {
            int x = 10 + 17 * xx;
            int y = 11;
            AspectSlot slot = new AspectSlot(primals[xx], source, x, y);
            slot.description = I18n.format("tooltip.arcana.fociforge."+primals[xx]);
            slot.setSymbolic(true);
            aspectSlots.add(slot);
        }
        for (int yy = 0; yy < ASPECT_V_COUNT; yy++) {
            for (int xx = 0; xx < ASPECT_H_COUNT; xx++) {
                int x = 10 + 17 * xx;
                int y = 52 + 16 * yy;
                AspectSlot slot = new AspectSlot(Aspects.EMPTY, source, x, y);
                slot.setSymbolic(true);
                aspectSlots.add(slot);
                scrollableSlots.add(slot);
            }
        }
        for (int yy = 0; yy < sins.length ; yy++) {
            int x = 95;
            int y = 52 + 16 * yy;
            AspectSlot slot = new AspectSlot(sins[yy], source, x, y);
            slot.description = I18n.format("tooltip.arcana.fociforge."+sins[yy]);
            slot.setSymbolic(true);
            aspectSlots.add(slot);
        }
    }

    private void changeFociStyle(int style) {
        Item item = te.focus().getItem();
        if (item == ArcanaItems.FOCUS_PARTS.get())
            te.setInventorySlotContents(1, new ItemStack(ArcanaItems.DEFAULT_FOCUS.get(), 1));
        if (item instanceof FocusItem) {
            te.focus().getOrCreateTag().putInt("style", style);
            changeFociSpell();
        }
    }

    public void changeFociSpell(){
        te.focus().getOrCreateTag().put("spell", te.spellState.currentSpell.toNBT(new CompoundNBT()).getCompound("spell"));
        Connection.sendWriteSpell(te.focus(), te.spellState.currentSpell.toNBT(new CompoundNBT()));
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index){
        this.setHeldAspect(null);

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
