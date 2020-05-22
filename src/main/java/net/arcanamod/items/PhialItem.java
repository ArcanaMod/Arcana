package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PhialItem extends Item
{
    public PhialItem()
    {
        super(new Properties().group(Arcana.ITEMS));
        this.addPropertyOverride(new ResourceLocation("aspect"), new IItemPropertyGetter() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public float call(ItemStack itemStack, @Nullable World world, @Nullable LivingEntity livingEntity) {
                if (world == null)
                    world = livingEntity.world;
                if (itemStack.getCapability(VisHandlerCapability.ASPECT_HANDLER).orElse(null).getContainedAspects().size()==0)
                    return -1;
                if (world.dimension.isSurfaceWorld())
                    return getAspectFromBattery(itemStack).ordinal()-1;
                else
                    return random.nextInt(58);
            }
        });
    }

    private Aspect getAspectFromBattery(ItemStack stack) {
        VisHandler handler = stack.getCapability(VisHandlerCapability.ASPECT_HANDLER).orElse(null);
        if (handler.getContainedAspects().size()!=0)
            return (Aspect)(handler.getContainedAspects().toArray()[0]);
        else
            return Aspect.EMPTY;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack is = playerIn.getHeldItem(handIn);

        if (!is.isEmpty())
        if (is.getCapability(VisHandlerCapability.ASPECT_HANDLER).orElse(null).getContainedAspects().size()==0)
        {
            is.getCapability(VisHandlerCapability.ASPECT_HANDLER).orElse(null).insert(Aspect.STRENGTH,8,false);
        }

        if(is.getTag() == null){
            is.setTag(getShareTag(is));
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        VisBattery battery = new VisBattery(8);
        return battery;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack)
    {
        if (stack.getCapability(VisHandlerCapability.ASPECT_HANDLER).orElse(null).getContainedAspects().size()!=0)
        {
            String aspectName = getAspectFromBattery(stack).toString().toLowerCase();
            return new TranslationTextComponent("item.arcana.phial", aspectName.substring(0, 1).toUpperCase() + aspectName.substring(1)).applyTextStyle(Rarity.RARE.color);
        }
        else
            return new TranslationTextComponent("item.arcana.empty_phial");
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack)
    {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt("id",getAspectFromBattery(stack).ordinal()-1);
        return compoundNBT;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        VisHandler cap = stack.getCapability(VisHandlerCapability.ASPECT_HANDLER).orElse(null);
        cap.insert(Aspect.values()[nbt.getInt("id")-1],8,false);
    }
}