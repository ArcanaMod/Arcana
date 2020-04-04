package net.kineticdevelopment.arcana.common.items;

import com.google.common.collect.ImmutableList;
import mcp.MethodsReturnNonnullByDefault;
import net.kineticdevelopment.arcana.client.Sounds;
import net.kineticdevelopment.arcana.common.items.attachment.Cap;
import net.kineticdevelopment.arcana.common.items.attachment.Focus;
import net.kineticdevelopment.arcana.core.Main;
import net.kineticdevelopment.arcana.core.aspects.AspectHandlerCapability;
import net.kineticdevelopment.arcana.core.aspects.TypedVisBattery;
import net.kineticdevelopment.arcana.core.spells.Spell;
import net.kineticdevelopment.arcana.core.wand.EnumAttachmentType;
import net.kineticdevelopment.arcana.utilities.WandUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
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
     * Getter of the attached attachments.
     * @return an array of the attached attachments
     */
    public ItemAttachment[][] getAttachments(){
        if(this.attachments == null){
            this.attachments = this.supplierAttachments.get();
        }

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