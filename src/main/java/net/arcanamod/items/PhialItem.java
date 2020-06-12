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
    public PhialItem() {
        super(new Properties().group(Arcana.ITEMS));
        this.addPropertyOverride(new ResourceLocation("aspect"), new IItemPropertyGetter() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public float call(ItemStack itemStack, @Nullable World world, @Nullable LivingEntity livingEntity) {
                IAspectHandler vis = IAspectHandler.getFrom(itemStack);
                if (world == null)
                    world = livingEntity.world;
                if (vis.getHoldersAmount()==0)
                    return -1;
                if (vis.getHolder(0)!=null)
                    return -1;
                if (world.dimension.isSurfaceWorld())
                    return vis.getHolder(0).getContainedAspect().ordinal()-1;
                else
                    return random.nextInt(58);
            }
        });
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack is = playerIn.getHeldItem(handIn);

        if(is.getTag() != null) {
            Arcana.logger.debug(is.getTag().toString() + " : " + IAspectHandler.getFrom(is).toString());
        }

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        AspectBattery battery = new AspectBattery(1,8);
        return battery;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        IAspectHandler aspectHandler = IAspectHandler.getFrom(stack);
        if (aspectHandler!=null && aspectHandler.getHolder(0)!=null)
        {
            String aspectName = aspectHandler.getHolder(0).getContainedAspect().toString().toLowerCase();
            return new TranslationTextComponent("item.arcana.phial", aspectName.substring(0, 1).toUpperCase() + aspectName.substring(1)).applyTextStyle(Rarity.RARE.color);
        }
        else
            return new TranslationTextComponent("item.arcana.empty_phial");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        AspectBattery vis = (AspectBattery) IAspectHandler.getFrom(stack);
        if (vis!=null) {
            if (vis.getHolder(0)!=null) {
                AspectStack aspectStack = vis.getHolder(0).getContainedAspectStack();
                tooltip.add(new TranslationTextComponent("tooltip.contains_aspect",
                        aspectStack.getAspect().name().toLowerCase().substring(0, 1).toUpperCase() + aspectStack.getAspect().name().toLowerCase().substring(1), aspectStack.getAmount()));
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        IAspectHandler vis = IAspectHandler.getFrom(stack);
        if (vis!=null) {
            Aspect aspect = vis.getHolder(0).getContainedAspect();
            int amount = vis.getHolder(0).getCurrentVis();
            if (aspect!=null && amount!=0) {
                CompoundNBT compoundNBT = new CompoundNBT();
                compoundNBT.putInt("id", aspect.ordinal() - 1);
                compoundNBT.putInt("amount", amount);
                return compoundNBT;
            }
        }
        return null;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        IAspectHandler cap = IAspectHandler.getFrom(stack);
        cap.insert(0,new AspectStack(Aspect.values()[nbt.getInt("id")-1],nbt.getInt("amount")),false);
    }
}