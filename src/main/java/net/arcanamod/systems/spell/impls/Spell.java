package net.arcanamod.systems.spell.impls;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.entities.SpellCloudEntity;
import net.arcanamod.systems.spell.*;
import net.arcanamod.util.Pair;
import net.arcanamod.util.RayTraceUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.arcanamod.aspects.Aspects.*;

/**
 * ISpell class but it self registers.
 */
public abstract class Spell implements ISpell {

	protected SpellData data;

	public boolean isBuilt = false;

	public Spell(){
		SpellRegistry.addSpell(getId(),this);
	}

	private static Aspect deserializeAspect(CompoundNBT compound,String deserializableAspect){
		return AspectUtils.getAspectByResourceLocation(new ResourceLocation(compound.getString(deserializableAspect)));
	}

	public static ISpell deserializeNBT(CompoundNBT compound){
		if (compound.contains("Spell")) {

			return Spells.spellMap.get(new ResourceLocation(compound.getString("Spell"))).build(
					new SpellData(deserializeAspect(compound, "FirstModifier"),
								  deserializeAspect(compound, "SecondModifier"),
								  deserializeAspect(compound, "SinModifier"),
								  Pair.of(deserializeAspect(compound, "FirstPrimaryCast"),
										  deserializeAspect(compound, "SecondPrimaryCast")),
								  Pair.of(deserializeAspect(compound, "FirstPlusCast"),
										  deserializeAspect(compound, "SecondPlusCast"))
							),
					new CompoundNBT());
		} else return null;
	}

	public static CompoundNBT serializeNBT(ISpell spell){
		CompoundNBT compound = new CompoundNBT();
		compound.putString("Spell",((Spell)spell).getId().toString()); // <-- Hardcoded here, fixes needed.

		compound.putString("FirstModifier", AspectUtils.getResourceLocationFromAspect(spell.getSpellData().firstModifier).toString());
		compound.putString("SecondModifier", AspectUtils.getResourceLocationFromAspect(spell.getSpellData().secondModifier).toString());
		compound.putString("SinModifier", AspectUtils.getResourceLocationFromAspect(spell.getSpellData().sinModifier).toString());

		compound.putString("FirstPrimaryCast", AspectUtils.getResourceLocationFromAspect(spell.getSpellData().primaryCast.getFirst()).toString());
		compound.putString("SecondPrimaryCast", AspectUtils.getResourceLocationFromAspect(spell.getSpellData().primaryCast.getSecond()).toString());

		compound.putString("FirstPlusCast", AspectUtils.getResourceLocationFromAspect(spell.getSpellData().plusCast.getFirst()).toString());
		compound.putString("SecondPlusCast", AspectUtils.getResourceLocationFromAspect(spell.getSpellData().plusCast.getSecond()).toString());

		return compound;
	}

	public abstract ResourceLocation getId();
	
	public Optional<ITextComponent> getName(CompoundNBT nbt){
		return Optional.of(new TranslationTextComponent("spell." + getId().getNamespace() + "." + getId().getPath()));
	}
	
	@Override
	public ActionResultType use(PlayerEntity player, Object sender, Action action){
		if (action == Action.USE) {
			Pair<Aspect, Aspect> cast = getSpellData().primaryCast;
			if (cast.getFirst() == AIR) {
				/*
				- Creates Cloud
				- Targets Entities inside
				- Lingering effect
				 */
				int raytraceDistance = 10;

				BlockPos pos = RayTraceUtils.getTargetBlockPos(player, player.world, raytraceDistance);
				if (cast.getSecond() == ENVY) {
					// enemies killed by the cloud spawn another smaller cloud
				} else if (cast.getSecond() == LUST) {
					// slowly moves towards the closest player / passive mob
				} else if (cast.getSecond() == SLOTH) {
					// the cloud last longer
				} else if (cast.getSecond() == PRIDE) {
					// cloud splits and disopates
				} else if (cast.getSecond() == GREED) {
					// slowly moves towards the closest hostile mob
				} else if (cast.getSecond() == GLUTTONY) {
					// the cloud is bigger
				} else if (cast.getSecond() == WRATH) {
					// creates a trap that creates a cloud when triggered
				} else {
					// Default AIR SPELL

					createSpellCloud(player,player.world,new Vec3d(pos));
				}
			}
			if (cast.getFirst() == WATER) {
				/*
				- Creates an AOE blast
				- Targets any entity hit
				 */
				if (cast.getSecond() == ENVY) {
					// the effect grows the more entities it hits
				} else if (cast.getSecond() == LUST) {
					// cant target hostile mobs
				} else if (cast.getSecond() == SLOTH) {
					// the AOE slows to a crawl allowing entities to be hit multiple times
				} else if (cast.getSecond() == PRIDE) {
					// creates multiple AOE waves in an AOE
				} else if (cast.getSecond() == GREED) {
					// can only target hostile mobs
				} else if (cast.getSecond() == GLUTTONY) {
					// the AOE effect is bigger
				} else if (cast.getSecond() == WRATH) {
					// the AOE effect becomes diectional wave with a longer range
				} else {
					// Default WATER SPELL
				}
			}
			if (cast.getFirst() == FIRE) {
				/*
				- Fires a projectilets with a long range
				- Targets the block / entity hit
				 */
				if (cast.getSecond() == ENVY) {
					// entities killed fire the same spell to the closest entity
				} else if (cast.getSecond() == LUST) {
					// homes to players / passive mobs
				} else if (cast.getSecond() == SLOTH) {
					// the projectile bounces
				} else if (cast.getSecond() == PRIDE) {
					// shotguns projectiles
				} else if (cast.getSecond() == GREED) {
					// homes to hostile mobs
				} else if (cast.getSecond() == GLUTTONY) {
					// the projectile is bigger
				} else if (cast.getSecond() == WRATH) {
					// fires a long range beam, longer range than the a lightning bolt but ticks slower
				} else {
					// Default FIRE SPELL
				}
			}
			if (cast.getFirst() == EARTH) {
				/*
				- Allows the player to select a block from far away
				- Can not target entities
				- Targets the selected block
				 */
				int raytraceDistance = 10;
				int delay = 4000;

				BlockPos pos = RayTraceUtils.getTargetBlockPos(player, player.world, raytraceDistance);
				if (cast.getSecond() == ENVY) {
					// targets all connected blocks of the same type within a 8 block range
					// TODO: implement this.
				} else if (cast.getSecond() == LUST) {
					// targets marked blocks (blocks can be marked with shift - rightclick while holding the wand
					if (sender instanceof ItemStack) {
						List<BlockPos> markedBlocks;
						if (((ItemStack)sender).getOrCreateTag().contains("MarkedBlocks")) {
							//player.sendMessage(((ListNBT) ((ItemStack) sender).getOrCreateTag().get("MarkedBlocks")).toFormattedComponent());
							ListNBT listNBT = ((ListNBT) ((ItemStack) sender).getOrCreateTag().get("MarkedBlocks"));
							markedBlocks = listNBT.stream()
									.map(inbt -> new BlockPos(((CompoundNBT) inbt).getInt("x"), ((CompoundNBT) inbt).getInt("y"), ((CompoundNBT) inbt).getInt("z"))).collect(Collectors.toList());
							for (BlockPos markedBlock : markedBlocks) {
								useOnBlock(player, player.world, markedBlock);
							}
							listNBT.clear();
						} else return ActionResultType.FAIL;
					} else return ActionResultType.FAIL;
				} else if (cast.getSecond() == SLOTH) {
					// it takes a few seconds for the spell to cast
					DelayedSpellManager.delayedSpells.add(new DelayedSpell(t -> useOnBlock(player, player.world, pos),delay));
					
				} else if (cast.getSecond() == PRIDE) {
					// targets random nearby blocks ~5
					BlockPos.getAllInBoxMutable(pos.add(-5, -5, -5), pos.add(5, 5, 5)).forEach(blockPos -> {
						if (player.world.rand.nextInt(5) == 2) useOnBlock(player, player.world, blockPos);
					});
				} else if (cast.getSecond() == GREED) {
					// allows players to target entites, targets the block below them
					List<Entity> entities = RayTraceUtils.rayTraceEntities(player.world,player.getPositionVec(),player.getLookVec(),Optional.empty(),Entity.class);
					for (Entity entity : entities){
						useOnBlock(player, player.world,entity.getPosition());
					}
				} else if (cast.getSecond() == GLUTTONY) {
					// selects a 3 * 3 * 3 area
					BlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 1, 1)).forEach(blockPos -> useOnBlock(player, player.world, blockPos));
				} else if (cast.getSecond() == WRATH) {
					// Allows you to target entities instead
					List<Entity> entities = RayTraceUtils.rayTraceEntities(player.world,player.getPositionVec(),player.getLookVec(),Optional.empty(),Entity.class);
					for (Entity entity : entities){
						useOnEntity(player, entity);
					}
				} else {
					// Default EARTH SPELL
					return useOnBlock(player, player.world, pos);
				}
			}
			if (cast.getFirst() == ORDER) {
				/*
				- Targets self
				 */
				int maxDistance = 8;

				if (cast.getSecond() == ENVY) {
					// targets all nearby players
					List<PlayerEntity> targets = player.world.getEntitiesWithinAABB(EntityType.PLAYER,
							new AxisAlignedBB(maxDistance, maxDistance, maxDistance, maxDistance, maxDistance, maxDistance),
							EntityPredicates.NOT_SPECTATING);
					for (PlayerEntity target : targets)
						useOnPlayer(target);
				} else if (cast.getSecond() == LUST) {
					// targets you and nearby pets
					List<PlayerEntity> targetsP = player.world.getEntitiesWithinAABB(EntityType.PLAYER,
							new AxisAlignedBB(maxDistance, maxDistance, maxDistance, maxDistance, maxDistance, maxDistance),
							EntityPredicates.NOT_SPECTATING);
					List<TameableEntity> targetsE = player.world.getEntitiesWithinAABB(TameableEntity.class,
							new AxisAlignedBB(maxDistance, maxDistance, maxDistance, maxDistance, maxDistance, maxDistance),
							EntityPredicates.NOT_SPECTATING);
					for (TameableEntity target : targetsE)
						useOnEntity(player, target);
					for (PlayerEntity target : targetsP)
						useOnPlayer(target);
				} else if (cast.getSecond() == SLOTH) {
					// invalid Spell
					player.sendStatusMessage(new TranslationTextComponent("status.arcana.invalid_spell"), true);
				} else if (cast.getSecond() == PRIDE) {
					// targets random nearby entities
					List<Entity> targetsE = player.world.getEntitiesWithinAABB(Entity.class,
							new AxisAlignedBB(maxDistance, maxDistance, maxDistance, maxDistance, maxDistance, maxDistance),
							EntityPredicates.NOT_SPECTATING);
					for (Entity target : targetsE)
						if (player.world.rand.nextInt(5)==2)
							useOnEntity(player, target);
				} else if (cast.getSecond() == GREED) {
					// targets nearby hostiles
					List<MobEntity> targetsE = player.world.getEntitiesWithinAABB(MobEntity.class,
							new AxisAlignedBB(maxDistance, maxDistance, maxDistance, maxDistance, maxDistance, maxDistance),
							EntityPredicates.NOT_SPECTATING);
					for (MobEntity target : targetsE)
						useOnEntity(player, target);
				} else if (cast.getSecond() == GLUTTONY) {
					// targets nearby dropped items
					List<ItemEntity> targetsE = player.world.getEntitiesWithinAABB(ItemEntity.class,
							new AxisAlignedBB(maxDistance, maxDistance, maxDistance, maxDistance, maxDistance, maxDistance),
							EntityPredicates.NOT_SPECTATING);
					for (ItemEntity target : targetsE)
						useOnEntity(player, target);
				} else if (cast.getSecond() == WRATH) {
					// becomes toggalable giving the player a faint glow and repeasts the spell every few seconds
					// TODO: Implement this.
				} else {
					// Default ORDER SPELL
					return useOnPlayer(player);
				}
			}
			if (cast.getFirst() == CHAOS) {
				/*
				-A mid ranged lightning bolt
				- Targets the block / entity hit every few ticks
				 */
				// Not implemented yet
			}
		} else if (action == Action.SPECIAL) {
			if (getSpellData().primaryCast.getFirst() == EARTH && getSpellData().primaryCast.getSecond() == LUST){
				if (sender instanceof ItemStack) {
					int raytraceDistance = 8;
					BlockPos pos = RayTraceUtils.getTargetBlockPos(player, player.world, raytraceDistance);
					ItemStack stack = ((ItemStack) sender);
					ListNBT markedBlocksNBT = stack.getOrCreateTag().contains("MarkedBlocks") ? (ListNBT) stack.getOrCreateTag().get("MarkedBlocks") : new ListNBT();
					CompoundNBT markedBlock = new CompoundNBT();
					markedBlock.putInt("x", pos.getX());
					markedBlock.putInt("y", pos.getY());
					markedBlock.putInt("z", pos.getZ());
					markedBlocksNBT.add(markedBlock);
					stack.getOrCreateTag().put("MarkedBlocks", markedBlocksNBT);
				}
				return ActionResultType.PASS;
			}
		}
		return ActionResultType.SUCCESS;
	}

	private void createSpellCloud(PlayerEntity player, World world, Vec3d area) {
		if (!world.isRemote) {
			SpellCloudEntity cloud = new SpellCloudEntity(world, area);
			cloud.setSpell(this);
			cloud.setDuration(100);
			((ServerWorld)world).summonEntity(cloud);
		}
	}

	public abstract ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget);
	public abstract ActionResultType useOnPlayer(PlayerEntity playerTarget);
	public abstract ActionResultType useOnEntity(PlayerEntity caster, Entity entityTarget);
}
