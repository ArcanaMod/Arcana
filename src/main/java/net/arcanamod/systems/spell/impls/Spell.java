package net.arcanamod.systems.spell.impls;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.*;
import net.arcanamod.util.Pair;
import net.arcanamod.util.RayTraceUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.arcanamod.aspects.Aspects.*;

/**
 * ISpell class but it self registers.
 */
public abstract class Spell implements ISpell {
	public Spell(){
		SpellRegistry.addSpell(getId(),this);
	}

	public static ISpell deserializeNBT(CompoundNBT compound){
		if (compound.contains("Spell")) {
			// TODO: FIX serializeNBT
			/*List<Aspect> modifiers = new ArrayList<>();
			for (INBT nbt : ((ListNBT) Objects.requireNonNull(compound.get("modifiers")))) {
				modifiers.add(Optional.ofNullable(AspectUtils.getAspectByResourceLocation(new ResourceLocation(((CompoundNBT)nbt).getString("Modifier")))).orElse(Aspects.EMPTY));
			}
			List<CastAspect> casts = new ArrayList<>();
			for (INBT nbt : ((ListNBT) Objects.requireNonNull(compound.get("casts")))) {
				casts.add(new CastAspect(
					Optional.ofNullable(AspectUtils.getAspectByResourceLocation(new ResourceLocation(((CompoundNBT)nbt).getString("Primary")))).orElse(Aspects.EMPTY),
					Optional.ofNullable(AspectUtils.getAspectByResourceLocation(new ResourceLocation(((CompoundNBT)nbt).getString("Combo")))).orElse(Aspects.EMPTY)
				));
			}
			return Spells.spellMap.get(new ResourceLocation(compound.getString("Spell"))).build(modifiers, casts, new CompoundNBT());
		*/return null;} else return null;
	}

	public static CompoundNBT serializeNBT(ISpell spell){
		CompoundNBT compound = new CompoundNBT();
		compound.putString("Spell",((Spell)spell).getId().toString()); // <-- Hardcoded here, fixes needed.
		ListNBT modifierList = new ListNBT();
		// TODO: FIX serializeNBT
		/*for (int i = 0; i < spell.getModAspects().size(); i++) {
			CompoundNBT c = new CompoundNBT();
			c.putString("Modifier", AspectUtils.getResourceLocationFromAspect(spell.getModAspects().get(i)).toString());
			modifierList.add(c);
		}

		ListNBT castList = new ListNBT();
		for (int i = 0; i < spell.getCastAspects().size(); i++) {
			CompoundNBT c = new CompoundNBT();
			c.putString("Primary",AspectUtils.getResourceLocationFromAspect(spell.getCastAspects().get(i).primaryAspect).toString());
			c.putString("Combo",AspectUtils.getResourceLocationFromAspect(spell.getCastAspects().get(i).comboAspect).toString());
			castList.add(c);
		}*/
		compound.put("modifiers",modifierList);
		//compound.put("casts",castList);

		return compound;
	}

	public abstract ResourceLocation getId();
	
	public Optional<ITextComponent> getName(CompoundNBT nbt){
		return Optional.of(new TranslationTextComponent("spell." + getId().getNamespace() + "." + getId().getPath()));
	}
	
	@Override
	public void use(PlayerEntity player, Action action){
		Pair<Aspect,Aspect> cast = getSpellData().primaryCast;
		if (cast.getFirst()==AIR){
			/*
			- Creates Cloud
			- Targets Entities inside
			- Lingering effect
			 */
			if (cast.getSecond()==ENVY){
				// enemies killed by the cloud spawn another smaller cloud
			}
			else if (cast.getSecond()==LUST){
				// slowly moves towards the closest player / passive mob
			}
			else if (cast.getSecond()==SLOTH){
				// the cloud last longer
			}
			else if (cast.getSecond()==PRIDE){
				// cloud splits and disopates
			}
			else if (cast.getSecond()==GREED){
				// slowly moves towards the closest hostile mob
			}
			else if (cast.getSecond()==GLUTTONY){
				// the cloud is bigger
			}
			else if (cast.getSecond()==WRATH){
				// creates a trap that creates a cloud when triggered
			}
			else {
				// Default AIR SPELL
			}
		}
		if (cast.getFirst()==WATER){
			/*
			- Creates an AOE blast
			- Targets any entity hit
			 */
			if (cast.getSecond()==ENVY){
				// the effect grows the more entities it hits
			}
			else if (cast.getSecond()==LUST){
				// cant target hostile mobs
			}
			else if (cast.getSecond()==SLOTH){
				// the AOE slows to a crawl allowing entities to be hit multiple times
			}
			else if (cast.getSecond()==PRIDE){
				// creates multiple AOE waves in an AOE
			}
			else if (cast.getSecond()==GREED){
				// can only target hostile mobs
			}
			else if (cast.getSecond()==GLUTTONY){
				// the AOE effect is bigger
			}
			else if (cast.getSecond()==WRATH){
				// the AOE effect becomes diectional wave with a longer range
			}
			else {
				// Default WATER SPELL
			}
		}
		if (cast.getFirst()==FIRE){
			/*
			- Fires a projectilets with a long range
			- Targets the block / entity hit
			 */
			if (cast.getSecond()==ENVY){
				// entities killed fire the same spell to the closest entity
			}
			else if (cast.getSecond()==LUST){
				// homes to players / passive mobs
			}
			else if (cast.getSecond()==SLOTH){
				// the projectile bounces
			}
			else if (cast.getSecond()==PRIDE){
				// shotguns projectiles
			}
			else if (cast.getSecond()==GREED){
				// homes to hostile mobs
			}
			else if (cast.getSecond()==GLUTTONY){
				// the projectile is bigger
			}
			else if (cast.getSecond()==WRATH){
				// fires a long range beam, longer range than the a lightning bolt but ticks slower
			}
			else {
				// Default FIRE SPELL
			}
		}
		if (cast.getFirst()==EARTH){
			/*
			- Allows the player to select a block from far away
			- Can not target entities
			- Targets the selected block
			 */
			if (cast.getSecond()==ENVY){
				// targets all connected blocks of the same type within a 8 block range
			}
			else if (cast.getSecond()==LUST){
				// targets marked blocks (blocks can be marked with shift - rightclick while holding the wand
			}
			else if (cast.getSecond()==SLOTH){
				// it takes a few seconds for the spell to cast
			}
			else if (cast.getSecond()==PRIDE){
				// targets random nearby blocks
			}
			else if (cast.getSecond()==GREED){
				// allows players to target entites, targets the block below them
			}
			else if (cast.getSecond()==GLUTTONY){
				// selects a 3 * 3 * 3 area
			}
			else if (cast.getSecond()==WRATH){
				// Allows you to target entities instead
			}
			else {
				// Default EARTH SPELL
			}
		}
		if (cast.getFirst()==ORDER){
			/*
			- Targets self
			 */

			int maxDistance = 8;

			if (cast.getSecond()==ENVY){
				// targets all nearby players
				List<PlayerEntity> targets = player.world.getEntitiesWithinAABB(EntityType.PLAYER,
						new AxisAlignedBB(maxDistance,maxDistance,maxDistance,maxDistance,maxDistance,maxDistance),
						EntityPredicates.NOT_SPECTATING);
				for (PlayerEntity target : targets)
					useOnPlayer(target);
			}
			else if (cast.getSecond()==LUST){
				// targets you and nearby pets
			}
			else if (cast.getSecond()==SLOTH){
				// Invalid Spell
			}
			else if (cast.getSecond()==PRIDE){
				// targets random nearby entities
			}
			else if (cast.getSecond()==GREED){
				// targets nearby hostiles
			}
			else if (cast.getSecond()==GLUTTONY){
				// targets nearby dropped items
			}
			else if (cast.getSecond()==WRATH){
				// becomes toggalable giving the player a faint glow and repeasts the spell every few seconds
			}
			else {
				// Default ORDER SPELL
			}
		}
		if (cast.getFirst()==CHAOS){
			/*
			-A mid ranged lightning bolt
			- Targets the block / entity hit every few ticks
			 */
			// Not implemented yet
		}
	}

	public abstract void useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget);
	public abstract void useOnPlayer(PlayerEntity playerTarget);
	public abstract void useOnEntity(PlayerEntity caster, Entity entityTarget);
}
