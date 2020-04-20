package net.kineticdevelopment.arcana.common.items;

import com.google.common.collect.ImmutableList;
import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.client.Sounds;
import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.kineticdevelopment.arcana.common.items.attachment.Cap;
import net.kineticdevelopment.arcana.common.items.attachment.Focus;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.aspects.AspectHandlerCapability;
import net.kineticdevelopment.arcana.core.aspects.TypedVisBattery;
import net.kineticdevelopment.arcana.core.spells.Spell;
import net.kineticdevelopment.arcana.core.wand.EnumAttachmentType;
import net.kineticdevelopment.arcana.utilities.WandUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

/**
 * Wand Item
 * 
 * @author Merijn
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemWand extends Item{

    public static List<ItemWand> WANDS = new ArrayList<>();
    
    protected int level = 2;
    protected ItemAttachment[][] attachments;
    
    // remove at some point
    public static final Supplier<ItemAttachment[][]> supplierAttachments = () -> {
        List<Cap> allowed = new ArrayList<>(Cap.CAPS);
        // fix models
        allowed.sort(Comparator.comparingInt(Cap::getID));
        List<Focus> allowedFoci = ImmutableList.of(Focus.NONE, Focus.DEFAULT); //TODO: change with foci
        return new ItemAttachment[][]{allowed.toArray(new Cap[0]), allowedFoci.toArray(new Focus[0])};
    };

    public ItemWand(String name){
        setMaxStackSize(1);
        setMaxDamage(0);
        setUnlocalizedName(name);
        setRegistryName(name);

        this.attachments = null;

        WANDS.add(this);
    }

	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt){
		return TypedVisBattery.primalBattery(WandUtil.getCore(stack).getMaxVis());
	}

    @Nullable
    public NBTTagCompound getNBTShareTag(ItemStack stack){
        NBTTagCompound tag = super.getNBTShareTag(stack);
        if(tag != null){
            NBTTagCompound aspectNBT = stack.getCapability(AspectHandlerCapability.ASPECT_HANDLER, null).serializeNBT();
            tag.setTag("aspect_handler", aspectNBT);
        }
        return tag;
    }

    public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt){
        super.readNBTShareTag(stack, nbt);
        if(nbt != null)
            stack.getCapability(AspectHandlerCapability.ASPECT_HANDLER, null).deserializeNBT(nbt.getCompoundTag("aspect_handler"));
    }
    
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
        return slotChanged;
    }
    
    /**
     * Casts a spell if a focus is assigned
     * @param world World the action is performed in
     * @param player Player that performs the action
     * @param hand Hand the wand is in
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if(world.isRemote) {
            return ActionResult.newResult(EnumActionResult.PASS, player.getHeldItem(hand));
        }
        if(hand == EnumHand.MAIN_HAND) {
            ItemStack item = player.getHeldItem(hand);
            if (item.getTagCompound() != null) {
                if (!item.getTagCompound().getCompoundTag("foci").hasNoTags()) {
                    Spell spell = Spell.fromNBT(item.getTagCompound().getCompoundTag("foci"));
                    spell.cast(player);
                    world.playSound(null, player.getPosition(), Sounds.SPELL_CAST, SoundCategory.PLAYERS, 0.5f, 0f);
                }
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }
    
    /**
     * Called when a Block is right-clicked with this Item. The wand uses this to transform blocks when applicable.
     *
     * @param player
     * 		The player using the wand.
     * @param world
     * 		The world the wand is used in.
     * @param pos
     * 		The position of the block clicked.
     * @param hand
     * 		The hand the wand is in.
     * @param facing
     * 		Which side of the block is being clicked (?).
     * @param hitX
     * 		Where on the block on the X axis the player has clicked.
     * @param hitY
     * 		Where on the block on the Y axis the player has clicked.
     * @param hitZ
     * 		Where on the block on the Z axis the player has clicked.
     * @return Whether the wand did anything.
     */
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
        Block block = world.getBlockState(pos).getBlock();
        // hardcoded for now, but this could be made into recipes later (probably 1.13+ when recipes can work with non-crafting tables)
        if(block == Blocks.BOOKSHELF){
            // destroy the block
            world.setBlockToAir(pos);
            // create an Arcanum item
            if(!world.isRemote){
                EntityItem entity = new EntityItem(world, pos.getX() + .5, pos.getY(), pos.getZ() + .5, new ItemStack(ItemInit.ARCANUM));
                entity.setDefaultPickupDelay();
                world.spawnEntity(entity);
            }
            // display particles ane effects (to do)
            return EnumActionResult.SUCCESS;
        }
        return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
    }
    
    /**
     * Getter of the attached attachments.
     * @return an array of the attached attachments
     */
    public ItemAttachment[][] getAttachments(){
        if(this.attachments == null)
            this.attachments = supplierAttachments.get();

        return this.attachments;
    }

    /**
     * Gets an attachment based on {@link EnumAttachmentType} and the ID of the attachment
     * @param type Attachment type
     * @param id ID of the attachment
     * @return {@link ItemAttachment} of the given type and id
     */
    @Nullable
    public ItemAttachment getAttachment(EnumAttachmentType type, int id) {
        ItemAttachment attachment = null;

        for(ItemAttachment a: this.getAttachments()[type.getSlot()]) {
            if(a.getID() == id) {
                attachment = a;
            }
        }
        return attachment;
    }

    /**
     * Gets amount of the allowed types
     * @param type Attachement type {@link EnumAttachmentType}
     * @return Amount of the given types
     */
    public int getAmountForSlot(EnumAttachmentType type) {
        return this.getAttachments()[type.getSlot()].length;
    }

    /**
     * Gets a attachment based on the NBT of the given ItemStack and the given type
     * @param itemStack Itemstack to get the NBT from
     * @param type Type of the requested attachment
     * @return {@link ItemAttachment} of the given type and ItemStack NBT
     */
    @Nullable
    public ItemAttachment getAttachment(ItemStack itemStack, EnumAttachmentType type) {
        return this.getAttachment(type, Main.getNBT(itemStack).getInteger(type.getKey()));
    }
    
    public boolean capAllowed(Cap cap){
        return cap.getLevel() <= level;
    }
    
    public int getLevel(){
        return level;
    }
    
    public ItemWand setLevel(int level){
        this.level = level;
        return this;
    }
    
    public String getItemStackDisplayName(ItemStack stack){
        String s = getUnlocalizedName(stack) + ".name";
        // Using server-side I18n -- replace when updating.
        return I18n.translateToLocalFormatted(s, I18n.translateToLocal(getAttachment(stack, EnumAttachmentType.CAP).getUnlocalizedName() + ".prefix"));
    }
}