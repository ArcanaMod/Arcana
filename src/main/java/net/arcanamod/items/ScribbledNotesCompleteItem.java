package net.arcanamod.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ScribbledNotesCompleteItem extends Item {
    public ScribbledNotesCompleteItem(Properties properties) {
        super(properties);


    }

    @Override
    public boolean hasEffect(ItemStack stack){
        return true;
    }

    //gives players
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn){
        if(handIn == Hand.MAIN_HAND){
            playerIn.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
        }
        else {
            playerIn.setItemStackToSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
        }
        playerIn.addItemStackToInventory(new ItemStack(ArcanaItems.ARCANUM.get()));

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

}
