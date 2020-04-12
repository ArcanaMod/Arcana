package net.kineticdevelopment.arcana.common.objects.items.tools.autorepair;

import net.kineticdevelopment.arcana.common.objects.items.tools.ShovelBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Timer;
import java.util.TimerTask;

/** @author Wilkon, Mozaran (With assistance from HAVOC)
 *
 * Meant for the Void Metal Shovel but can be used for other auto repair shovels.
 */

public class AutoRepairShovel extends ShovelBase {
    private final int FULL_TIMER = 100;

    public AutoRepairShovel(String name, ToolMaterial material) {
        super(name, material);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        return !(newStack.getItem() == oldStack.getItem() &&
                (newStack.isItemStackDamageable() || newStack.getMetadata() == oldStack.getMetadata()));
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        if (isSelected) {
            NBTTagCompound tag = stack.getTagCompound();
            if(tag == null) {
                stack.setTagCompound(new NBTTagCompound());
                tag = stack.getTagCompound();
            }
            if(tag != null && !tag.hasKey("repair_timer")) {
                tag.setInteger("repair_timer", FULL_TIMER);
            }

            if(tag != null && tag.getInteger("repair_timer") > 0){
                tag.setInteger("repair_timer", tag.getInteger("repair_timer") - 1);
            } else if (tag != null) {
                tag.setInteger("repair_timer", FULL_TIMER);
                stack.damageItem(-1, (EntityLivingBase) entityIn);
            }
        }
    }
}