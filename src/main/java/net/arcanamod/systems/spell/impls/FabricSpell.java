package net.arcanamod.systems.spell.impls;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
import net.arcanamod.systems.spell.CastAspect;
import net.arcanamod.systems.spell.ISpell;
import net.arcanamod.systems.spell.SpellCosts;
import net.arcanamod.systems.spell.SpellData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class FabricSpell extends Spell {

	private SpellData data;
	private int distance = 10;

	public boolean isBuilt = false;

	@Override
	public ISpell build(SpellData data, CompoundNBT compound) {
		this.data = data;
		if (compound.contains("distance"))
			this.distance = compound.getInt("distance");
		isBuilt = true;
		return this;
	}

	@Override
	public ResourceLocation getId() {
		return ArcanaVariables.arcLoc("fabric");
	}

	@Override
	public Aspect getSpellAspect() {
		return Aspects.FABRIC;
	}

	@Override
	public SpellData getSpellData() {
		return data;
	}

	@Override
	public SpellCosts getSpellCosts() {
		return new SpellCosts(0,0,0,0,0,0,1);
	}

	/**
	 * How spell is complex to use / create
	 *
	 * @return returns spell complexity.
	 */
	@Override
	public int getComplexity() {
		return -666;
	}

	@Override
	public int getSpellDuration() {
		return 0;
	}

	@Override
	public void useOnEntity(PlayerEntity caster, Entity target) {
		target.sendMessage(new StringTextComponent("MCP is broken everyone shold use Yarn"));
		target.sendMessage(new StringTextComponent(target.getName().getString()+" gets gold award on r/minecraft"));
		target.sendMessage(new StringTextComponent(target.getName().getString()+" gets gold award on r/minecraft"));
	}

	@Override
	public void useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {
		caster.world.setTileEntity(blockTarget,new ResearchTableTileEntity());
	}

	@Override
	public void useOnPlayer(PlayerEntity playerTarget) {
		playerTarget.sendMessage(new StringTextComponent("MCP is broken everyone shold use Yarn"));
		playerTarget.sendMessage(new StringTextComponent(playerTarget.getName().getString()+" gets gold award on r/minecraft"));
		playerTarget.sendMessage(new StringTextComponent(playerTarget.getName().getString()+" gets gold award on r/minecraft"));
	}
}
