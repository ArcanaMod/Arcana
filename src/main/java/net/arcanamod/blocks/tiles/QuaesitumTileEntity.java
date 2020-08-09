package net.arcanamod.blocks.tiles;

import com.google.common.collect.Maps;
import io.netty.buffer.Unpooled;
import net.arcanamod.containers.ArcanaContainers;
import net.arcanamod.containers.QuaesitumContainer;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.ArtifactItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;
import java.util.Map;

public class QuaesitumTileEntity extends LockableTileEntity implements ITickableTileEntity, ISidedInventory
{
    public int cooldown = 0;

    protected QuaesitumTileEntity(TileEntityType<?> typeIn)
    {
        super(typeIn);
    }

    public QuaesitumTileEntity()
    {
        super(ArcanaTiles.QUAESITUM_TE.get());
    }

    protected ITextComponent getDefaultName()
    {
        return new TranslationTextComponent("container.quaesitum");
    }

    protected Container createMenu(int id, PlayerInventory player)
    {
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer(8,8));
        buffer.writeBlockPos(pos);
        return new QuaesitumContainer(ArcanaContainers.QUAESITUM_CONTAINER.get(),id, player, this, this.furnaceData, buffer);
    }

    private static final int[] SLOTS_UP = new int[]{0,1};
    private static final int[] SLOTS_DOWN = new int[]{2, 3, 4, 5};
    protected NonNullList<ItemStack> items = NonNullList.withSize(13, ItemStack.EMPTY);
    private int burnTime;
    private int cookTime;
    private int cookTimeTotal;
    private int redstoneMode;
    private int searchMode;
    private int successChance;
    private int failureChance;
    private int lossChance;
    public final IIntArray furnaceData = new IIntArray() {
        @Override
        public int get(int index) {
            switch(index) {
                case 0:
                    return QuaesitumTileEntity.this.burnTime;
                case 1:
                    return QuaesitumTileEntity.this.redstoneMode;
                case 2:
                    return QuaesitumTileEntity.this.cookTime;
                case 3:
                    return QuaesitumTileEntity.this.cookTimeTotal;
                case 4:
                    return QuaesitumTileEntity.this.searchMode;
                case 5:
                    return QuaesitumTileEntity.this.successChance;
                case 6:
                    return QuaesitumTileEntity.this.failureChance;
                case 7:
                    return QuaesitumTileEntity.this.lossChance;
                default:
                    return 0;
            }
        }

        @Override
        public void set(int index, int value) {
            switch(index) {
                case 0:
                    QuaesitumTileEntity.this.burnTime = value;
                    break;
                case 1:
                    QuaesitumTileEntity.this.redstoneMode = value;
                    break;
                case 2:
                    QuaesitumTileEntity.this.cookTime = value;
                    break;
                case 3:
                    QuaesitumTileEntity.this.cookTimeTotal = value;
                    break;
                case 4:
                    QuaesitumTileEntity.this.searchMode = value;
                    break;
                case 5:
                    QuaesitumTileEntity.this.successChance = value;
                    break;
                case 6:
                    QuaesitumTileEntity.this.failureChance = value;
                    break;
                case 7:
                    QuaesitumTileEntity.this.lossChance = value;
                    break;
            }

        }

        public int size() {
            return 8;
        }
    };

    public void setRedstoneMode(int redstoneMode)
    {
        furnaceData.set(1,redstoneMode);
    }

    public int getRedstoneMode()
    {
        return furnaceData.get(1);
    }

    public static Map<Item, Integer> getBurnTimes() {
        Map<Item, Integer> map = Maps.newLinkedHashMap();

        addItemBurnTime(map, ArcanaItems.SCRIBING_TOOLS.get(), 600);
        return map;
    }

    private static void addItemBurnTime(Map<Item, Integer> map, IItemProvider itemProvider, int burnTimeIn) {
        map.put(itemProvider.asItem(), burnTimeIn);
    }

    private boolean isBurning() {
        return this.burnTime > 0;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.items);
        this.burnTime = compound.getInt("BurnTime");
        this.cookTime = compound.getInt("CookTime");
        this.cookTimeTotal = compound.getInt("CookTimeTotal");
        this.redstoneMode = compound.getInt("RedstoneMode");
        this.searchMode = compound.getInt("SearchMode");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("BurnTime", this.burnTime);
        compound.putInt("CookTime", this.cookTime);
        compound.putInt("CookTimeTotal", this.cookTimeTotal);
        compound.putInt("RedstoneMode", this.redstoneMode);
        compound.putInt("SearchMode", this.searchMode);
        ItemStackHelper.saveAllItems(compound, this.items);

        return compound;
    }

    @Override
    public void tick()
    {
        if (cooldown >= 80)
        {
            if (cookTime>0)
            {
                /*world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(pos.offset(Direction.UP, 10).offset(Direction.NORTH, 10).offset(Direction.WEST, 10),
                        pos.offset(Direction.DOWN, 10).offset(Direction.SOUTH, 10).offset(Direction.EAST, 10))).forEach(playerEntity ->
                        world.playSound(playerEntity, pos, ArcanaSounds.scribble, SoundCategory.BLOCKS, 40f, 1f));*/
                cooldown = 0;
            }
        }
        else
        {
            cooldown++;
        }

        boolean flag = this.isBurning();
        boolean flag1 = false;
        if (this.isBurning()) {
            --this.burnTime;
        }

        if (!this.world.isRemote) {
            if (searchMode == 1)
            {
            }

            ItemStack itemstack = this.items.get(9);
            if (this.isBurning() || !itemstack.isEmpty() && !this.items.get(10).isEmpty()) {
                if (!this.isBurning() && this.canSmelt())
                {
                    if (itemstack.getMaxDamage() > itemstack.getDamage())
                    {
                        this.burnTime = this.getBurnTime(itemstack);
                    }
                    //this.recipesUsed = this.burnTime;
                    if (this.isBurning())
                    {
                        flag1 = true;
                        if (itemstack.hasContainerItem())
                            this.items.set(9, itemstack.getContainerItem());
                        else
                        if (!itemstack.isEmpty())
                        {
                            Item item = itemstack.getItem();
                            if (itemstack.getMaxDamage() > itemstack.getDamage())
                            {
                                itemstack.setDamage(itemstack.getDamage() + 1);
                            }
                            /*if (itemstack.isEmpty()) {
                                this.items.set(9, itemstack.getContainerItem());
                            }*/
                        }
                    }
                }

                if (this.isBurning() && this.canSmelt()) {
                    if (this.cookTime == this.cookTimeTotal)
                    {
                        this.cookTime = 0;
                        this.cookTimeTotal = getCookTimeTotal();
                        this.placeItemsInRightSlot();
                        flag1 = true;
                    }
                    ++this.cookTime;
                } else {
                    this.cookTime = 0;
                }
            } else if (!this.isBurning() && this.cookTime > 0) {
                this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.cookTimeTotal);
            }

            if (flag != this.isBurning()) {
                flag1 = true;
                //this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).with(AbstractFurnaceBlock.LIT, Boolean.valueOf(this.isBurning())), 3);
            }
        }

        if (flag1) {
            this.markDirty();
        }
    }

    private int getCookTimeTotal()
    {
        return 80; // 100
    }

    protected boolean canSmelt() {
        if (!this.items.get(10).isEmpty()) {
            if (!(this.items.get(10).getItem() instanceof ArtifactItem)){
                return false;
            } else if (redstoneMode==1&&world.getRedstonePowerFromNeighbors(pos)>0||redstoneMode==2&&world.getRedstonePowerFromNeighbors(pos)<1){
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private void placeItemsInRightSlot(/*Here was recipe*/)
    {
        if (this.canSmelt()) {
            ItemStack itemstack = this.items.get(10);
            ItemStack itemstackO = ((ArtifactItem)itemstack.getItem()).getResearchNote();
            ItemStack itemstack0 = this.items.get(0);
            ItemStack itemstack1 = this.items.get(1);
            ItemStack itemstack2 = this.items.get(2);
            ItemStack itemstack3 = this.items.get(3);
            ItemStack itemstack4 = this.items.get(4);
            ItemStack itemstack5 = this.items.get(5);
            ItemStack itemstack6 = this.items.get(6);
            ItemStack itemstack7 = this.items.get(7);
            ItemStack itemstack8 = this.items.get(8);
            if (itemstack0.isEmpty()) {
                this.items.set(0, itemstackO.copy());
            } else if (itemstack0.getItem() == itemstackO.getItem()) {
                itemstack0.grow(itemstackO.getCount());
            } else
                if (itemstack1.isEmpty()) {
                    this.items.set(1, itemstackO.copy());
                } else if (itemstack1.getItem() == itemstackO.getItem()) {
                    itemstack1.grow(itemstackO.getCount());
                } else
                    if (itemstack2.isEmpty()) {
                        this.items.set(2, itemstackO.copy());
                    } else if (itemstack2.getItem() == itemstackO.getItem()) {
                        itemstack2.grow(itemstackO.getCount());
                    } else
                        if (itemstack3.isEmpty()) {
                            this.items.set(3, itemstackO.copy());
                        } else if (itemstack3.getItem() == itemstackO.getItem()) {
                            itemstack3.grow(itemstackO.getCount());
                        } else
                            if (itemstack4.isEmpty()) {
                                this.items.set(4, itemstackO.copy());
                            } else if (itemstack4.getItem() == itemstackO.getItem()) {
                                itemstack4.grow(itemstackO.getCount());
                            } else
                                if (itemstack5.isEmpty()) {
                                    this.items.set(5, itemstackO.copy());
                                } else if (itemstack5.getItem() == itemstackO.getItem()) {
                                    itemstack5.grow(itemstackO.getCount());
                                } else
                                    if (itemstack6.isEmpty()) {
                                        this.items.set(6, itemstackO.copy());
                                    } else if (itemstack6.getItem() == itemstackO.getItem()) {
                                        itemstack6.grow(itemstackO.getCount());
                                    }

            if (itemstack.getCount() == 1)
                this.items.set(10, ItemStack.EMPTY);
            else
                itemstack.shrink(1);
        }
    }

    protected int getBurnTime(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        } else {
            Item item = stack.getItem();
            return getBurnTimes().getOrDefault(item, 0);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        if (side == Direction.DOWN) {
            return SLOTS_DOWN;
        } else {
            return SLOTS_UP;
        }
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side.
     */
    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side.
     */
    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction)
    {
        return true;
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the stack in the given slot.
     */
    @Override
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag) {
            this.cookTimeTotal = getCookTimeTotal();
            this.cookTime = 0;
            this.markDirty();
        }

    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     */
    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index < 9)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    public void onCrafting(PlayerEntity player) {
    }

    net.minecraftforge.common.util.LazyOptional<? extends net.minecraftforge.items.IItemHandler>[] handlers =
            net.minecraftforge.items.wrapper.SidedInvWrapper.create(this, Direction.UP, Direction.DOWN, Direction.NORTH);

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable Direction facing) {
        if (!this.removed && facing != null && capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (facing == Direction.UP)
                return handlers[0].cast();
            else if (facing == Direction.DOWN)
                return handlers[1].cast();
            else
                return handlers[2].cast();
        }
        return super.getCapability(capability, facing);
    }

    /**
     * invalidates a tile entity
     */
    @Override
    public void remove() {
        super.remove();
        for (int x = 0; x < handlers.length; x++)
            handlers[x].invalidate();
    }
}
