package net.kineticdevelopment.arcana.common.objects.items;

import net.kineticdevelopment.arcana.common.init.ItemInit;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

import static com.mojang.realmsclient.gui.ChatFormatting.GRAY;
import static net.minecraft.util.text.TextFormatting.GOLD;

/**
 * Focus Item
 * 
 * @author Merijn
 */
public class ItemFocus extends Item {

    public ItemFocus(String name) {

        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setUnlocalizedName(name);
        this.setRegistryName(name);

        initModel();

        ItemInit.ITEMS.add(this);
    }

    /**
     * Adds all of the focus types to register.
     */
    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelResourceLocation focus1 = new ModelResourceLocation(getRegistryName() + "_style_1", "inventory");
        ModelResourceLocation focus2 = new ModelResourceLocation(getRegistryName() + "_style_2", "inventory");
        ModelResourceLocation focus3 = new ModelResourceLocation(getRegistryName() + "_style_3", "inventory");
        ModelResourceLocation focus4 = new ModelResourceLocation(getRegistryName() + "_style_4", "inventory");
        ModelResourceLocation focus5 = new ModelResourceLocation(getRegistryName() + "_style_5", "inventory");
        ModelResourceLocation focus6 = new ModelResourceLocation(getRegistryName() + "_style_6", "inventory");
        ModelResourceLocation focus7 = new ModelResourceLocation(getRegistryName() + "_style_7", "inventory");
        ModelResourceLocation focus8 = new ModelResourceLocation(getRegistryName() + "_style_8", "inventory");
        ModelResourceLocation focus9 = new ModelResourceLocation(getRegistryName() + "_style_9", "inventory");
        ModelResourceLocation focus10 = new ModelResourceLocation(getRegistryName() + "_style_10", "inventory");
        ModelResourceLocation focus11 = new ModelResourceLocation(getRegistryName() + "_style_11", "inventory");
        ModelResourceLocation focus12 = new ModelResourceLocation(getRegistryName() + "_style_12", "inventory");
        ModelResourceLocation focus13 = new ModelResourceLocation(getRegistryName() + "_style_13", "inventory");
        ModelResourceLocation focus14 = new ModelResourceLocation(getRegistryName() + "_style_14", "inventory");
        ModelResourceLocation focus15 = new ModelResourceLocation(getRegistryName() + "_style_15", "inventory");
        ModelResourceLocation focus16 = new ModelResourceLocation(getRegistryName() + "_style_16", "inventory");
        ModelResourceLocation focus17 = new ModelResourceLocation(getRegistryName() + "_style_17", "inventory");
        ModelResourceLocation focus18 = new ModelResourceLocation(getRegistryName() + "_style_18", "inventory");
        ModelResourceLocation focus19 = new ModelResourceLocation(getRegistryName() + "_style_19", "inventory");
        ModelResourceLocation focus20 = new ModelResourceLocation(getRegistryName() + "_style_20", "inventory");
        ModelResourceLocation focus21 = new ModelResourceLocation(getRegistryName() + "_style_21", "inventory");
        ModelResourceLocation focus22 = new ModelResourceLocation(getRegistryName() + "_style_22", "inventory");
        ModelResourceLocation focus23 = new ModelResourceLocation(getRegistryName() + "_style_23", "inventory");
        ModelResourceLocation focus24 = new ModelResourceLocation(getRegistryName() + "_style_24", "inventory");
        ModelResourceLocation focus25 = new ModelResourceLocation(getRegistryName() + "_style_25", "inventory");
        ModelResourceLocation focus26 = new ModelResourceLocation(getRegistryName() + "_style_26", "inventory");
        ModelResourceLocation focus27 = new ModelResourceLocation(getRegistryName() + "_style_27", "inventory");
        ModelResourceLocation focus28 = new ModelResourceLocation(getRegistryName() + "_style_28", "inventory");
        ModelResourceLocation focus29 = new ModelResourceLocation(getRegistryName() + "_style_29", "inventory");
        ModelResourceLocation focus30 = new ModelResourceLocation(getRegistryName() + "_style_30", "inventory");
        ModelResourceLocation focus31 = new ModelResourceLocation(getRegistryName() + "_style_31", "inventory");
        ModelResourceLocation focus32 = new ModelResourceLocation(getRegistryName() + "_style_32", "inventory");
        ModelResourceLocation focus33 = new ModelResourceLocation(getRegistryName() + "_style_33", "inventory");
        ModelResourceLocation focus34 = new ModelResourceLocation(getRegistryName() + "_style_34", "inventory");
        ModelResourceLocation focus35 = new ModelResourceLocation(getRegistryName() + "_style_35", "inventory");
        ModelResourceLocation focus36 = new ModelResourceLocation(getRegistryName() + "_style_36", "inventory");

        // Register the variants
        ModelBakery.registerItemVariants(this,
                focus1, focus2, focus3, focus4, focus5, focus6, focus7, focus8, focus9, focus10,
                focus11, focus12, focus13, focus14, focus15, focus16, focus17, focus18, focus19, focus20,
                focus21, focus22, focus23, focus24, focus25, focus26, focus27, focus28, focus29, focus30,
                focus31, focus32, focus33, focus34, focus35, focus36
        );

        // CustomMeshDefenition
        ModelLoader.setCustomMeshDefinition(this, stack -> {
            if(getTagCompoundSafe(stack).hasKey("variant")) {
                switch(getTagCompoundSafe(stack).getInteger("variant")) {
                    case 2: return focus2;
                    case 3: return focus3;
                    case 4: return focus4;
                    case 5: return focus5;
                    case 6: return focus6;
                    case 7: return focus7;
                    case 8: return focus8;
                    case 9: return focus9;
                    case 10: return focus10;
                    case 11: return focus11;
                    case 12: return focus12;
                    case 13: return focus13;
                    case 14: return focus14;
                    case 15: return focus15;
                    case 16: return focus16;
                    case 17: return focus17;
                    case 18: return focus18;
                    case 19: return focus19;
                    case 20: return focus20;
                    case 21: return focus21;
                    case 22: return focus22;
                    case 23: return focus23;
                    case 24: return focus24;
                    case 25: return focus25;
                    case 26: return focus26;
                    case 27: return focus27;
                    case 28: return focus28;
                    case 29: return focus29;
                    case 30: return focus30;
                    default: return focus1;
                }
            }
            return null;
        });
    }

    // Used to clear a focus and changes it back to focus parts
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(worldIn.isRemote) {
            return ActionResult.newResult(EnumActionResult.PASS, playerIn.getHeldItem(handIn));
        }

        ItemStack item = playerIn.getHeldItem(handIn);

        if(playerIn.isSneaking()) {
            playerIn.inventory.addItemStackToInventory(new ItemStack(ItemInit.FOCUS_PARTS));
            playerIn.inventory.removeStackFromSlot(playerIn.inventory.getSlotFor(item));
        }
        return new ActionResult<>(EnumActionResult.PASS, item);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        NBTTagCompound tag = getTagCompoundSafe(stack);
        StringBuilder sb = new StringBuilder();
        String result = "None, Focus is empty.";
        if(tag.hasKey("foci")) {
            String[] effects = tag.getCompoundTag("foci").getString("effects").split(";");
            for(String effect : effects) {
                effect = effect.toLowerCase();
                String output = effect.substring(0, 1).toUpperCase() + effect.substring(1);
                sb.append(output).append(";");
            }
            result = sb.toString();
        }

        if(GuiScreen.isShiftKeyDown()) {
            tooltip.add(GRAY + "Sneak + Right click to empty");
            tooltip.add(GOLD + "Effects: ");
            for(String s : result.split(";")) {
                tooltip.add(GOLD + s);
            }
        } else {
            tooltip.add(GRAY + "Sneak + Right click to empty");
            tooltip.add(GOLD + "Press shift for more information");
        }
    }
    private NBTTagCompound getTagCompoundSafe(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            stack.setTagCompound(tagCompound);
        }
        return tagCompound;
    }
}
