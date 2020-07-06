package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PhialItem extends Item{
    
    public PhialItem(){
        super(new Properties().group(Arcana.ITEMS));
        this.addPropertyOverride(new ResourceLocation("aspect"), new IItemPropertyGetter(){
            @OnlyIn(Dist.CLIENT)
            @Override
            public float call(ItemStack itemStack, @Nullable World world, @Nullable LivingEntity livingEntity){
                IAspectHandler vis = IAspectHandler.getFrom(itemStack);
                if(world == null)
                    world = livingEntity.world;
                if(vis.getHoldersAmount() == 0)
                    return -1;
                if(vis.getHolder(0) == null)
                    return -1;
                if(world.dimension.isSurfaceWorld())
                    return vis.getHolder(0).getContainedAspect().getId() - 1;
                else
                    return random.nextInt(58);
            }
        });
    }
    
    public ActionResultType onItemUse(ItemUseContext context){
        TileEntity tile = context.getWorld().getTileEntity(context.getPos());
        if(tile != null){
            LazyOptional<IAspectHandler> cap = tile.getCapability(AspectHandlerCapability.ASPECT_HANDLER);
            if(cap.isPresent()){
                //noinspection ConstantConditions
                IAspectHandler tileHandle = cap.orElse(null);
                IAspectHolder myHandle = IAspectHandler.getFrom(context.getItem()).getHolder(0);
                if(myHandle.getCurrentVis() <= 0){
                    // drain from block
                    // pick first holder with >0 vis
                    // and take from it
                    for(IAspectHolder holder : tileHandle.getHolders())
                        if(holder.getCurrentVis() > 0){
                            int min = Math.min(holder.getCurrentVis(), 8);
                            Aspect aspect = holder.getContainedAspect();
                            // create a filled phial
                            ItemStack capedItemStack = new ItemStack(ArcanaItems.PHIAL.get());
                            IAspectHandler.getFrom(capedItemStack).insert(0, new AspectStack(aspect, min), false);
                            if(capedItemStack.getTag() == null)
                                capedItemStack.setTag(capedItemStack.getShareTag());
                            // take an empty phial and give the filled one
                            context.getItem().shrink(1);
                            context.getPlayer().addItemStackToInventory(capedItemStack);
                            holder.drain(new AspectStack(aspect, min), false);
                            return ActionResultType.SUCCESS;
                        }
                }else{
                    // insert to block
                    for(IAspectHolder holder : tileHandle.getHolders())
                        if(holder.getCapacity() - holder.getCurrentVis() > 0 && (holder.getContainedAspect() == myHandle.getContainedAspect() || holder.getContainedAspect() == Aspects.EMPTY)){
                            holder.insert(new AspectStack(myHandle.getContainedAspect(), myHandle.getCurrentVis()), false);
                            context.getItem().shrink(1);
                            context.getPlayer().addItemStackToInventory(new ItemStack(ArcanaItems.PHIAL.get()));
                            return ActionResultType.SUCCESS;
                        }
                }
            }
        }
        return super.onItemUse(context);
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
            if (aspectHandler.getHolder(0).getContainedAspect()!= Aspects.EMPTY) {
                String aspectName = AspectUtils.getLocalizedAspectDisplayName(aspectHandler.getHolder(0).getContainedAspect());
                return new TranslationTextComponent("item.arcana.phial", aspectName).applyTextStyle(Rarity.RARE.color);
            }
        }
        return new TranslationTextComponent("item.arcana.empty_phial");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        AspectBattery vis = (AspectBattery) IAspectHandler.getFrom(stack);
        if (vis!=null) {
            if (vis.getHolder(0)!=null) {
                if (vis.getHolder(0).getContainedAspect()!= Aspects.EMPTY) {
                    AspectStack aspectStack = vis.getHolder(0).getContainedAspectStack();
                    tooltip.add(new TranslationTextComponent("tooltip.contains_aspect",
                            aspectStack.getAspect().name().toLowerCase().substring(0, 1).toUpperCase() + aspectStack.getAspect().name().toLowerCase().substring(1), aspectStack.getAmount()));
                }
            }
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Nullable
    @Override
    public CompoundNBT getShareTag(ItemStack stack) {
        IAspectHandler vis = IAspectHandler.getFrom(stack);
        if (vis!=null) {
            if (vis.getHolder(0)!=null) {
                Aspect aspect = vis.getHolder(0).getContainedAspect();
                int amount = vis.getHolder(0).getCurrentVis();
                if (aspect != null && amount != 0) {
                    CompoundNBT compoundNBT = new CompoundNBT();
                    compoundNBT.putInt("id", aspect.getId() - 1);
                    compoundNBT.putInt("amount", amount);
                    return compoundNBT;
                }
            }
        }
        return null;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt) {
        IAspectHandler cap = IAspectHandler.getFrom(stack);
        cap.insert(0,new AspectStack(Aspects.values()[nbt.getInt("id")-1],nbt.getInt("amount")),false);
    }
}