package net.kineticdevelopment.arcana.common.objects.items;

import net.kineticdevelopment.arcana.api.spells.Spell;
import net.kineticdevelopment.arcana.api.wand.EnumAttachmentType;
import net.kineticdevelopment.arcana.common.items.ItemAttachment;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Wand Item
 * 
 * @author Merijn
 */
public class ItemWand extends Item {

    public static List<ItemWand> WANDS = new ArrayList<>();

    protected ItemAttachment[][] attachments;

    public Supplier<ItemAttachment[][]> supplierAttachments;

    public ItemWand(String name) {
        setMaxStackSize(1);
        setMaxDamage(0);
        setUnlocalizedName(name);
        setRegistryName(name);

        this.supplierAttachments = ItemAttachment::buildDefaultArray;
        this.attachments = null;

        WANDS.add(this);
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
                }
            }
        }
        return new ActionResult<>(EnumActionResult.PASS, player.getHeldItem(hand));
    }

    /**
     * Getter of the attached attachments. If none returns the default array in {@link ItemAttachment}
     * @return Array of the attached attachments
     */
    public ItemAttachment[][] getAttachments() {
        if(this.attachments == null)
        {
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
    public int getAmmountForSlot(EnumAttachmentType type) {
        return this.getAttachments()[type.getSlot()].length;
    }

    /**
     * Gets a attachment based on the NBT of the given ItemStack and the given type
     * @param itemStack Itemstack to get the NBT from
     * @param type Type of the requested attachment
     * @return {@link ItemAttachment} of the given type and ItemStack NBT
     */
    public ItemAttachment getAttachment(ItemStack itemStack, EnumAttachmentType type) {
        return this.getAttachment(type, Main.getNBT(itemStack).getInteger(type.getKey()));
    }

    /**
     * Sets the allowed attachments for this item.
     * @param attachments Supplier of the ItemAttachment Array with all the attachments
     * @return Instance of this item, Used to chain methods.
     */
    public ItemWand setAttachments(Supplier<ItemAttachment[][]> attachments) {
        this.supplierAttachments = attachments;
        this.attachments = null;
        return this;
    }



}
