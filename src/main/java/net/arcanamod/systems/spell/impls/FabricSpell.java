package net.arcanamod.systems.spell.impls;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
import net.arcanamod.systems.spell.CastAspect;
import net.arcanamod.systems.spell.ISpell;
import net.arcanamod.systems.spell.SpellExtraData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class FabricSpell extends Spell {

	private Aspect[] modAspects;
	private CastAspect[] castAspects;
	private int distance = 10;

	public boolean isBuilt = false;

	@Override
	public ISpell build(Aspect[] modAspects, CastAspect[] castAspects, SpellExtraData data) {
		this.modAspects = modAspects;
		this.castAspects = castAspects;
		if (data.contains("distance"))
			this.distance = data.read("distance");
		isBuilt = true;
		return this;
	}

	@Override
	public ResourceLocation getId() {
		return Arcana.arcLoc("fabric");
	}

	@Override
	public Aspect getSpellAspect() {
		return Aspects.FABRIC;
	}

	@Override
	public Aspect[] getModAspects() {
		return modAspects;
	}

	@Override
	public CastAspect[] getCastAspects() {
		return castAspects;
	}

	/**
	 * Cost of spell in AspectStacks.
	 *
	 * @return returns cost of spell.
	 */
	@Override
	public AspectStack[] getAspectCosts() {
		return new AspectStack[0];
	}

	/**
	 * How spell is complex to use / create
	 *
	 * @return returns spell complexity.
	 */
	@Override
	public int getComplexity() {
		return 0;
	}

	@Override
	public int getSpellDuration() {
		return 0;
	}

	@Override
	public void use(PlayerEntity player, Action action) {
		useCasts(this,player,castAspects);
	}

	@Override
	public void onAirCast(PlayerEntity caster, World world, BlockPos pos, int area, int duration) {
		world.setTileEntity(pos,new ResearchTableTileEntity());
	}

	@Override
	public void onWaterCast(PlayerEntity caster, List<Entity> entityTargets) {
		for (Entity target : entityTargets){
			target.sendMessage(new StringTextComponent("MCP is broken everyone shold use Yarn"));
			target.sendMessage(new StringTextComponent(target.getName().getString()+" gets gold award on r/minecraft"));
			target.sendMessage(new StringTextComponent(target.getName().getString()+" gets gold award on r/minecraft"));
		}
	}

	@Override
	public void onFireCast(PlayerEntity caster, @Nullable Entity entityTarget, BlockPos blockTarget) {
		caster.world.setTileEntity(blockTarget,new ResearchTableTileEntity());
	}

	@Override
	public void onEarthCast(PlayerEntity caster, BlockPos blockTarget) {
		caster.world.setTileEntity(blockTarget,new ResearchTableTileEntity());
	}

	@Override
	public void onOrderCast(PlayerEntity playerTarget) {
		playerTarget.sendMessage(new StringTextComponent("MCP is broken everyone shold use Yarn"));
		playerTarget.sendMessage(new StringTextComponent(playerTarget.getName().getString()+" gets gold award on r/minecraft"));
		playerTarget.sendMessage(new StringTextComponent(playerTarget.getName().getString()+" gets gold award on r/minecraft"));
	}

	@Override
	public void onChaosCast(PlayerEntity caster, Entity entityTarget, BlockPos blockTarget) {
		caster.world.setTileEntity(blockTarget,new ResearchTableTileEntity());
	}
}
