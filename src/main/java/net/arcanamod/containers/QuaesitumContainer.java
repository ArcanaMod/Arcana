package net.arcanamod.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class QuaesitumContainer extends Container
{
    private final IInventory furnaceInventory;
    private final BlockPos pos;
    public final IIntArray ints;
    protected final World world;

    protected QuaesitumContainer(ContainerType<?> containerTypeIn, int id, PlayerInventory playerInventoryIn, PacketBuffer buf) {
        this(containerTypeIn, id, playerInventoryIn, new Inventory(13), new IntArray(8),buf);
    }

    public QuaesitumContainer(ContainerType<?> containerTypeIn, int id, PlayerInventory playerInventoryIn, IInventory furnaceInventoryIn, IIntArray p_i50104_6_, PacketBuffer buf) {
        super(containerTypeIn, id);
        assertInventorySize(furnaceInventoryIn, 13);
        assertIntArraySize(p_i50104_6_, 8);
        this.furnaceInventory = furnaceInventoryIn;
        this.ints = p_i50104_6_;
        this.world = playerInventoryIn.player.world;
        this.pos = buf.readBlockPos();
        this.addSlot(new Slot(furnaceInventoryIn, 9, 134, 12));//paper
        this.addSlot(new Slot(furnaceInventoryIn, 10, 24, 26));//main
        this.addSlot(new Slot(furnaceInventoryIn, 11, 24, 49));//2nd
        this.addSlot(new Slot(furnaceInventoryIn, 12, 24, 71));//3nd

        this.addSlot(new Slot(furnaceInventoryIn, 0, 116, 35));
        this.addSlot(new Slot(furnaceInventoryIn, 1, 134, 35));
        this.addSlot(new Slot(furnaceInventoryIn, 2, 152, 35));
        this.addSlot(new Slot(furnaceInventoryIn, 3, 116, 53));
        this.addSlot(new Slot(furnaceInventoryIn, 4, 134, 53));
        this.addSlot(new Slot(furnaceInventoryIn, 5, 152, 53));
        this.addSlot(new Slot(furnaceInventoryIn, 6, 116, 71));
        this.addSlot(new Slot(furnaceInventoryIn, 7, 134, 71));
        this.addSlot(new Slot(furnaceInventoryIn, 8, 152, 71));

        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventoryIn, j + i * 9 + 9, 8 + j * 18, 105 + i * 18));
            }
        }

        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventoryIn, k, 8 + k * 18, 163));
        }

        this.trackIntArray(p_i50104_6_);
    }

    public QuaesitumContainer(int i, PlayerInventory playerInventory, PacketBuffer packetBuffer)
    {
        this(ArcanaContainers.QUAESITUM_CONTAINER.get(), i, playerInventory, new Inventory(13), new IntArray(8),packetBuffer);
    }

    public BlockPos getBlockPos()
    {
        return pos;
    }

    public void clear() {
        this.furnaceInventory.clear();
    }

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.furnaceInventory.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 2) {
                if (!this.mergeItemStack(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            } else if (index != 1 && index != 0) {
                if (index >= 3 && index < 30) {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 3, 39, false)) {
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

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.ints.get(2);
        int j = this.ints.get(3);
        return j != 0 && i != 0 ? i * 52 / j : -1;
    }

    @OnlyIn(Dist.CLIENT)
    public int getSuccessChangeScaled() {
        int i = this.ints.get(5);
        int j = 20;
        return j != 0 && i != 0 ? i / j : -1;
    }

    @OnlyIn(Dist.CLIENT)
    public int getFailureChangeScaled() {
        int i = this.ints.get(6);
        int j = 20;
        return j != 0 && i != 0 ? i / j : -1;
    }

    @OnlyIn(Dist.CLIENT)
    public int getLossChangeScaled() {
        int i = this.ints.get(7);
        int j = 20;
        return j != 0 && i != 0 ? i / j : -1;
    }

    @OnlyIn(Dist.CLIENT)
    public int getBurnLeftScaled()
    {
        return this.ints.get(0) * 13 / 180;
    }

    @OnlyIn(Dist.CLIENT)
    public boolean func_217061_l() {
        return this.ints.get(0) > 0;
    }
}
