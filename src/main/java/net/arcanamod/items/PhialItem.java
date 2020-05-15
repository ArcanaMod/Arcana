package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.VisHandler;
import net.arcanamod.aspects.VisHandlerCapability;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PhialItem extends Item
{
    //public VisHandler vh = VisHandlerCapability.ASPECT_HANDLER.getDefaultInstance();

    private Aspect aspectName;

    public PhialItem(Aspect aspectName)
    {
        super(new Properties().group(Arcana.ITEMS));
        this.aspectName = aspectName;
        this.addPropertyOverride(new ResourceLocation("aspect"), new IItemPropertyGetter() {
            @OnlyIn(Dist.CLIENT)
            @Override
            public float call(ItemStack p_call_1_, @Nullable World p_call_2_, @Nullable LivingEntity p_call_3_)
            {
                return random.nextInt(58);//aspectName.ordinal()-1;
            }
        });
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
    {
        return null;
    }
}