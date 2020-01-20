package net.kineticdevelopment.arcana.common.objects.items;

import net.kineticdevelopment.arcana.api.spells.Spell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemWand extends ItemBase {

    public ItemWand(String name) {
        super(name);
        this.setMaxStackSize(1);
        this.setMaxDamage(0);
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
}
