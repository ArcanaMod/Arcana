package net.kineticdevelopment.arcana.common.objects.items.tools.autorepair;

import net.kineticdevelopment.arcana.common.objects.items.tools.AxeBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Timer;
import java.util.TimerTask;

/** @author Wilkon (With assistance from HAVOC)
 *
 * Meant for the Void Metal Axe but can be used for other auto repair axe.
 */

public class AutoRepairAxe extends AxeBase {

    public AutoRepairAxe(String name, ToolMaterial material) {
        super(name, material);
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
        {
            if (isSelected)
            if (stack.isItemDamaged())
            {
                Timer timer = new Timer();
                timer.schedule( new TimerTask()
                {
                    public void run()
                    {
                        stack.damageItem(-1, (EntityLivingBase) entityIn);
                    }
                }, 5000, 20 * 10 * 1000);
            }
        }
    }
}