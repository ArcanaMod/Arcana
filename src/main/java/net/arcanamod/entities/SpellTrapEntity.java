package net.arcanamod.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;

public class SpellTrapEntity extends Entity {
	SpellCloudEntity.CloudVariableGrid variableGrid;

	public SpellTrapEntity(SpellCloudEntity.CloudVariableGrid variableGrid) {
		super(ArcanaEntities.SPELL_TRAP.get(),variableGrid.world);
		this.variableGrid = variableGrid;
	}

	public SpellTrapEntity(EntityType<?> spellTrapEntityEntityType, World world) {
		super(spellTrapEntityEntityType,world);
	}

	@Override
	protected void registerData() {

	}

	@Override
	protected void readAdditional(CompoundNBT compound) {

	}

	@Override
	protected void writeAdditional(CompoundNBT compound) {

	}

	@Override
	public IPacket<?> createSpawnPacket() {
		return null;
	}
}
