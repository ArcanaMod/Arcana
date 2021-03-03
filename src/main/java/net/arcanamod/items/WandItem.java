package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.ArcanaSounds;
import net.arcanamod.aspects.*;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.CrucibleBlock;
import net.arcanamod.client.render.aspects.AspectHelixParticleData;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.items.attachment.Focus;
import net.arcanamod.items.attachment.FocusItem;
import net.arcanamod.systems.spell.Spell;
import net.arcanamod.systems.spell.casts.ICast;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.core.CastCircle;
import net.arcanamod.systems.spell.modules.core.CastMethod;
import net.arcanamod.util.Pair;
import net.arcanamod.util.VisUtils;
import net.arcanamod.world.AuraView;
import net.arcanamod.world.Node;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class WandItem extends MagicDeviceItem{
	
	public WandItem(Properties properties){
		super(properties);
	}

	@Override
	public boolean canCraft() {
		return true;
	}

	@Override
	public boolean canUseSpells() {
		return true;
	}

	@Override
	public String getDeviceName() {
		return "Wand";
	}

	public static ItemStack withCapAndCore(String cap, String core){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("cap", cap);
		nbt.putString("core", core);
		ItemStack stack = new ItemStack(ArcanaItems.WAND.get(), 1);
		stack.setTag(nbt);
		return stack;
	}

	public static ItemStack withCapAndCore(ResourceLocation cap, ResourceLocation core){
		return withCapAndCore(cap.toString(), core.toString());
	}

	public static ItemStack withCapAndCore(Cap cap, Core core){
		return withCapAndCore(cap.getId(), core.getId());
	}

	public ActionResultType onItemUse(ItemUseContext context){
		return convert(context.getWorld(), context.getPos(), context.getPlayer());
	}
	
	public static ActionResultType convert(World world, BlockPos pos, @Nullable PlayerEntity player){
		BlockState state = world.getBlockState(pos);
		if(state.getBlock() == Blocks.CAULDRON){
			world.setBlockState(pos, ArcanaBlocks.CRUCIBLE.get().getDefaultState().with(CrucibleBlock.FULL, state.get(CauldronBlock.LEVEL) >= 2));
			world.playSound(player, pos, SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1, 1);
			for(int i = 0; i < 20; i++)
				world.addParticle(ParticleTypes.END_ROD, pos.getX() + world.rand.nextDouble(), pos.getY() + world.rand.nextDouble(), pos.getZ() + world.rand.nextDouble(), 0, 0, 0);
			return ActionResultType.SUCCESS;
		}
		if(state.getBlock() == Blocks.CRAFTING_TABLE){
			world.setBlockState(pos, ArcanaBlocks.ARCANE_CRAFTING_TABLE.get().getDefaultState());
			world.playSound(player, pos, SoundEvents.ENTITY_EVOKER_CAST_SPELL, SoundCategory.PLAYERS, 1, 1);
			for(int i = 0; i < 20; i++)
				world.addParticle(ParticleTypes.END_ROD, (pos.getX() - .1f) + world.rand.nextDouble() * 1.2f, (pos.getY() - .1f) + world.rand.nextDouble() * 1.2f, (pos.getZ() - .1f) + world.rand.nextDouble() * 1.2f, 0, 0, 0);
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.PASS;
	}

	public int getUseDuration(ItemStack stack){
		return 72000;
	}
	
	public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items){
		if(isInGroup(group)){
			// iron/wooden, silver/dair, gold/greatwood, thaumium/silverwood, void/arcanium
			items.add(withCapAndCoreForCt("iron_cap", "wood_wand"));
			items.add(withCapAndCoreForCt("silver_cap", "dair_wand"));
			items.add(withCapAndCoreForCt("gold_cap", "greatwood_wand"));
			items.add(withCapAndCoreForCt("thaumium_cap", "silverwood_wand"));
			items.add(withCapAndCoreForCt("void_cap", "arcanium_wand"));
		}
	}
	
	public static ItemStack withCapAndCoreForCt(String cap, String core){
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString("cap", "arcana:" + cap);
		nbt.putString("core", "arcana:" + core);
		ItemStack stack = new ItemStack(ArcanaItems.WAND.get(), 1);
		stack.setTag(nbt);
		return stack;
	}
	
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag){
		super.addInformation(stack, world, tooltip, flag);
		// Add focus info
		Spell spell = getFocus(stack).getSpell(stack);
		if(spell != null){
			Optional<ITextComponent> name = spell.getName(getFocusData(stack).getCompound("Spell"));
			name.ifPresent(e -> tooltip.add(new TranslationTextComponent("tooltip.arcana.spell", e,
					spell.getSpellCosts().toList().stream()
						.map(AspectStack::getAspect)
						.map(aspect -> I18n.format("aspect." + aspect.name()))
						.collect(Collectors.joining(", ")))));
		}
	}
}