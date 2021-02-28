package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.capabilities.Researcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.arcanamod.Arcana.arcLoc;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ScribbledNotesCompleteItem extends Item{
    
    private static final ResourceLocation ROOT = arcLoc("root");
    
    public ScribbledNotesCompleteItem(Properties properties){
        super(properties);
    }

    @Override
    public boolean hasEffect(ItemStack stack){
        return true;
    }

    // gives players the arcanum on right click
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand){
        if(hand == Hand.MAIN_HAND)
            player.setItemStackToSlot(EquipmentSlotType.MAINHAND, ItemStack.EMPTY);
        else
            player.setItemStackToSlot(EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
        player.addItemStackToInventory(new ItemStack(ArcanaItems.ARCANUM.get()));
        Researcher.getFrom(player).advanceEntry(ResearchBooks.getEntry(ROOT));
        return super.onItemRightClick(world, player, hand);
    }
}