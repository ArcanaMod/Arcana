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

    public ItemAttachment[][] getAttachments() {
        if(this.attachments == null)
        {
            this.attachments = this.supplierAttachments.get();
        }

        return this.attachments;
    }

    public ItemAttachment getAttachment(EnumAttachmentType type, int index) {
        return this.getAttachments()[type.getSlot()][index];
    }

    public int getAmmountForSlot(EnumAttachmentType type) {
        return this.getAttachments()[type.getSlot()].length;
    }

    public ItemAttachment getAttachment(ItemStack itemStack, EnumAttachmentType type) {
        return this.getAttachment(type, Main.getNBT(itemStack).getInteger(type.getKey()));
    }

    public ItemWand setAttachments(Supplier<ItemAttachment[][]> attachments) {
        this.supplierAttachments = attachments;
        this.attachments = null;
        return this;
    }



}
