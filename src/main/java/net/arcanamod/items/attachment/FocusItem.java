package net.arcanamod.items.attachment;

import net.arcanamod.systems.spell.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FocusItem extends Item implements Focus{
	public static final int DEFAULT_NUMSTYLES = 36;

	private int numStyles;
	private final List<ResourceLocation> modelLocations;
	
	public FocusItem(Properties properties, int numStyles, ResourceLocation... locations){
		super(properties);
		this.numStyles = numStyles;
		modelLocations = Arrays.asList(locations);
		Focus.FOCI.add(this);
		// TODO: property overrides are now defined separately
		//addPropertyOverride(new ResourceLocation("style"), (stack, world, entity) -> stack.getOrCreateTag().getInt("style"));
	}
	
	public ResourceLocation getModelLocation(CompoundNBT nbt){
		return modelLocations.get(Math.min(nbt.getInt("style"), modelLocations.size() - 1));
	}
	
	public List<ResourceLocation> getAllModelLocations(){
		return modelLocations;
	}
	
	public int numStyles(){
		return numStyles;
	}
	
	public Optional<Item> getAssociatedItem(){
		return Optional.of(this);
	}

	public Spell getSpell(ItemStack stack) {
		// Spell.fromNBT already uses .getCompound("spell"), so it's not needed here
		return Spell.fromNBT(stack.getOrCreateTag().getCompound("focusData")); // .getCompound("spell"));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		Logger logger = LogManager.getLogger();
		logger.debug(playerIn.getHeldItem(handIn).getOrCreateTag().toString());
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

	public static int getColourAspect(ItemStack stack) {
		Spell spell = Spell.fromNBT(stack.getOrCreateTag().getCompound("spell"));
		return spell.getSpellColor();
	}
}
