package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

import static net.arcanamod.ArcanaSounds.playPhialCorkpopSound;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PhialItem extends Item implements IOverrideAspects {
	
	public PhialItem(){
		super(new Properties().group(Arcana.ITEMS));
	}
	
	@SuppressWarnings("ConstantConditions")
	public ActionResultType onItemUse(ItemUseContext context){
		BlockPos pos = context.getPos();
		TileEntity tile = context.getWorld().getTileEntity(pos);
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
							float min = Math.min(holder.getCurrentVis(), 8);
							playPhialCorkpopSound(context.getPlayer());
							Aspect aspect = holder.getContainedAspect();
							// create a filled phial
							ItemStack capedItemStack = new ItemStack(ArcanaItems.PHIAL.get());
							IAspectHandler.getFrom(capedItemStack).insert(0, new AspectStack(aspect, min), false);
							if(capedItemStack.getTag() == null)
								capedItemStack.setTag(capedItemStack.getShareTag());
							// take an empty phial and give the filled one
							context.getItem().shrink(1);
							context.getPlayer().inventory.addItemStackToInventory(capedItemStack); //player.addItemStackToInventory gives sound and player.inventory.addItemStackToInventory not.
							holder.drain(new AspectStack(aspect, min), false);
							return ActionResultType.SUCCESS;
						}
				}else{
					// insert to block
					for(IAspectHolder holder : tileHandle.getHolders())
						if((holder.getCapacity() - holder.getCurrentVis() > 0 || holder.isIgnoringFullness()) && (holder.getContainedAspect() == myHandle.getContainedAspect() || holder.getContainedAspect() == Aspects.EMPTY)){
							float inserted = holder.insert(new AspectStack(myHandle.getContainedAspect(), myHandle.getCurrentVis()), false);
							playPhialCorkpopSound(context.getPlayer());
							if(inserted != 0){
								ItemStack new_phial = new ItemStack(this, 1);
								IAspectHolder old_holder = IAspectHandler.getFrom(context.getItem()).getHolder(0);
								IAspectHolder new_holder = IAspectHandler.getFrom(new_phial).getHolder(0);
								new_holder.insert(new AspectStack(old_holder.getContainedAspect(), inserted), false);
								new_phial.setTag(new_phial.getShareTag());
								if (!context.getPlayer().isCreative())
									context.getPlayer().addItemStackToInventory(new_phial);
							}else
								if (!context.getPlayer().isCreative())
									context.getPlayer().inventory.addItemStackToInventory(new ItemStack(ArcanaItems.PHIAL.get())); //player.addItemStackToInventory gives sound and player.inventory.addItemStackToInventory not.
							context.getItem().shrink(1);
							return ActionResultType.SUCCESS;
						}
				}
			}
		}
		return super.onItemUse(context);
	}
	
	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt){
		return new AspectBattery(1, 8);
	}
	
	@Override
	public ITextComponent getDisplayName(ItemStack stack){
		IAspectHandler aspectHandler = IAspectHandler.getFrom(stack);
		if(aspectHandler != null && aspectHandler.getHolder(0) != null){
			if(aspectHandler.getHolder(0).getContainedAspect() != Aspects.EMPTY){
				String aspectName = AspectUtils.getLocalizedAspectDisplayName(aspectHandler.getHolder(0).getContainedAspect());
				return new TranslationTextComponent("item.arcana.phial", aspectName).mergeStyle(Rarity.RARE.color);
			}
		}
		return new TranslationTextComponent("item.arcana.empty_phial");
	}
	
	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn){
		AspectBattery vis = (AspectBattery)IAspectHandler.getFrom(stack);
		if(vis != null){
			if(vis.getHolder(0) != null){
				if(vis.getHolder(0).getContainedAspect() != Aspects.EMPTY){
					AspectStack aspectStack = vis.getHolder(0).getContainedAspectStack();
					tooltip.add(new TranslationTextComponent("tooltip.contains_aspect",
							aspectStack.getAspect().name().toLowerCase().substring(0, 1).toUpperCase() + aspectStack.getAspect().name().toLowerCase().substring(1), (int)aspectStack.getAmount()));
				}
			}
		}
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
	@Nullable
	@Override
	public CompoundNBT getShareTag(ItemStack stack){
		IAspectHandler vis = IAspectHandler.getFrom(stack);
		if(vis != null){
			if(vis.getHolder(0) != null){
				Aspect aspect = vis.getHolder(0).getContainedAspect();
				float amount = vis.getHolder(0).getCurrentVis();
				if(aspect != null && amount != 0){
					CompoundNBT compoundNBT = new CompoundNBT();
					compoundNBT.putInt("id", aspect.getId() - 1);
					compoundNBT.putFloat("amount", amount);
					return compoundNBT;
				}
			}
		}
		return null;
	}
	
	@Override
	public void readShareTag(ItemStack stack, @Nullable CompoundNBT nbt){
		if(nbt != null){
			IAspectHandler cap = IAspectHandler.getFrom(stack);
			if(cap != null)
				cap.insert(0, new AspectStack(Aspects.getAll().get(nbt.getInt("id") - 1), nbt.getInt("amount")), false);
		}
	}
	
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items){
		if(isInGroup(group)){
			for(Aspect aspect : Aspects.getAll()){
				items.add(withAspect(aspect));
			}
		}
	}
	
	private ItemStack withAspect(Aspect aspect){
		ItemStack stack = new ItemStack(this);
		IAspectHandler cap = IAspectHandler.getFrom(stack);
		if(cap != null)
			cap.insert(0, new AspectStack(aspect, 8), false);
		stack.setTag(stack.getShareTag());
		return stack;
	}

	@Override
	public List<AspectStack> getAspectStacks(ItemStack stack) {
		IAspectHolder myHolder = IAspectHandler.getFrom(stack).getHolder(0);
		if (myHolder==null) return IOverrideAspects.setSpecialOverrideType(SpecialOverrideType.DEFAULT);
 		if (myHolder.getContainedAspect()==Aspects.EMPTY) return IOverrideAspects.setSpecialOverrideType(SpecialOverrideType.NONE);
		return Collections.singletonList(myHolder.getContainedAspectStack());
	}

	public static Aspect getAspect(ItemStack stack) {
		IAspectHolder myHolder = IAspectHandler.getFrom(stack).getHolder(0);
		return myHolder.getContainedAspect();
	}
}