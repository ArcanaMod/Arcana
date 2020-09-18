package net.arcanamod.systems.spell.impls;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.systems.spell.CastAspect;
import net.arcanamod.systems.spell.ISpell;
import net.arcanamod.systems.spell.SpellExtraData;
import net.arcanamod.systems.spell.SpellRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

/**
 * ISpell class but it self registers.
 */
public abstract class DefaultSpell implements ISpell {
	public DefaultSpell(){
		SpellRegistry.addSpell(getId(),this);
	}

	public abstract ISpell build(Aspect[] modAspects, CastAspect[] castAspects, SpellExtraData data);

	public abstract ResourceLocation getId();

	@Override
	public abstract Aspect getSpellAspect();

	@Override
	public abstract Aspect[] getModAspects();

	@Override
	public abstract CastAspect[] getCastAspects();

	@Override
	public abstract AspectStack[] getAspectCosts();

	@Override
	public abstract int getComplexity();

	@Override
	public abstract void use(PlayerEntity player, Action action);
}
