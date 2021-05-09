package net.arcanamod.systems.spell.casts;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.entities.BigSpellEggEntity;
import net.arcanamod.entities.SpellCloudEntity;
import net.arcanamod.entities.SpellEggEntity;
import net.arcanamod.entities.SpellTrapEntity;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.util.Pair;
import net.arcanamod.util.RayTraceUtils;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.arcanamod.aspects.Aspects.*;

/**
 * ISpell class but it self registers.
 */
public abstract class Cast implements ICast {

	public CompoundNBT data = new CompoundNBT();

	public Cast(){
		CastRegistry.addCast(getId(),this);
	}

	public abstract ResourceLocation getId();

	public abstract ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget);
	public abstract ActionResultType useOnPlayer(PlayerEntity playerTarget);
	public abstract ActionResultType useOnEntity(PlayerEntity caster, Entity entityTarget);

	public static float getArrowVelocity(int charge) {
		float f = (float)charge / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;
		if (f > 1.0F) {
			f = 1.0F;
		}

		return f;
	}

	@Override
	public void use(UUID spellUUID, World world, PlayerEntity player, Object sender, Pair<Aspect, Aspect> cast, ICast.Action action){
		/*
		TODO LIST OF NOT ADDED CASTS:
		Chaos
		Water
		Fire+SLOTH
		Earth+ENVY
		 */
		if (action == ICast.Action.USE) {
			if (cast.getFirst() == AIR) {
				/*
				- Creates Cloud
				- Targets Entities inside
				- Lingering effect
				 */
				int raytraceDistance = 10;

 				BlockPos pos = RayTraceUtils.getTargetBlockPos(player, world, raytraceDistance);
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
					createSpellCloud(player,world,player.getPositionVec(),1);
				} else if (cast.getSecond() == WRATH) {
					// creates a trap that creates a cloud when triggered
					createSpellCloudTrap(new SpellCloudEntity.CloudVariableGrid(player,world,player.getPositionVec(),0));
				} else {
					// Default AIR SPELL
					createSpellCloud(player,world,player.getPositionVec(),0);
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
				Random random = new Random();
				if (!world.isRemote) {

					if (cast.getSecond() == ENVY) {
						// entities killed fire the same spell to the closest entity
					} else if (cast.getSecond() == LUST) {
						// homes to players / passive mobs
						SpellEggEntity eggentity = new SpellEggEntity(world, player, this);
						eggentity.setItem(new ItemStack(ArcanaItems.AMBER.get()));
						eggentity.shoot(player.getLookVec().getX(), player.getLookVec().getY(), player.getLookVec().getZ(), 1.5F, 1.0F);
						eggentity.setPosition(eggentity.getPosX(),eggentity.getPosY()-0.5,eggentity.getPosZ());
						eggentity.enableHoming(PlayerEntity.class, CreatureEntity.class);
						world.addEntity(eggentity);
					} else if (cast.getSecond() == SLOTH) {
						// the projectile bounces
						// TODO: IMPLEMENT THIS
					} else if (cast.getSecond() == PRIDE) {
						// shotguns projectiles
						for (int i = 0; i < 3; i++) {
							SpellEggEntity eggentity = new SpellEggEntity(world, player, this);
							eggentity.setItem(new ItemStack(ArcanaItems.AMBER.get()));
							eggentity.shoot(player.getLookVec().getX(), player.getLookVec().getY(), player.getLookVec().getZ(), 0.6F, 20.0F);
							eggentity.setPosition(eggentity.getPosX(),eggentity.getPosY()-0.5,eggentity.getPosZ());
							world.addEntity(eggentity);
						}
					} else if (cast.getSecond() == GREED) {
						// homes to hostile mobs
						SpellEggEntity eggentity = new SpellEggEntity(world, player, this);
						eggentity.setItem(new ItemStack(ArcanaItems.AMBER.get()));
						eggentity.shoot(player.getLookVec().getX(), player.getLookVec().getY(), player.getLookVec().getZ(), 1.5F, 1.0F);
						eggentity.setPosition(eggentity.getPosX(),eggentity.getPosY()-0.5,eggentity.getPosZ());
						eggentity.enableHoming(MobEntity.class);
						world.addEntity(eggentity);
					} else if (cast.getSecond() == GLUTTONY) {
						// the projectile is bigger
						BigSpellEggEntity eggentity = new BigSpellEggEntity(world, player, this);
						eggentity.setItem(new ItemStack(ArcanaItems.AMBER.get()));
						eggentity.shoot(player.getLookVec().getX(), player.getLookVec().getY(), player.getLookVec().getZ(), 1.5F, 1.0F);
						eggentity.setPosition(eggentity.getPosX(),eggentity.getPosY()-0.5,eggentity.getPosZ());
						world.addEntity(eggentity);
					} else if (cast.getSecond() == WRATH) {
						// fires a long range beam, longer range than the a lightning bolt but ticks slower
					} else {
						// Default FIRE SPELL
						SpellEggEntity eggentity = new SpellEggEntity(world, player, this);
						eggentity.setItem(new ItemStack(ArcanaItems.AMBER.get()));
						eggentity.shoot(player.getLookVec().getX(), player.getLookVec().getY(), player.getLookVec().getZ(), 1.5F, 1.0F);
						eggentity.setPosition(eggentity.getPosX(),eggentity.getPosY()-0.5,eggentity.getPosZ());
						world.addEntity(eggentity);
					}
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

				BlockPos pos = RayTraceUtils.getTargetBlockPos(player, world, raytraceDistance);
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
								useOnBlock(player, world, markedBlock);
							}
							listNBT.clear();
						}
					}
				} else if (cast.getSecond() == SLOTH) {
					// it takes a few seconds for the spell to cast
					DelayedCast.delayedCasts.add(new DelayedCast.Impl(t -> useOnBlock(player, world, pos),delay));

				} else if (cast.getSecond() == PRIDE) {
					// targets random nearby blocks ~5
					BlockPos.getAllInBoxMutable(pos.add(-5, -5, -5), pos.add(5, 5, 5)).forEach(blockPos -> {
						if (world.rand.nextInt(5) == 2) useOnBlock(player, world, blockPos);
					});
				} else if (cast.getSecond() == GREED) {
					// allows players to target entites, targets the block below them
					List<Entity> entities = RayTraceUtils.rayTraceEntities(world,player.getPositionVec(),player.getLookVec(), Optional.empty(),Entity.class);
					for (Entity entity : entities){
						useOnBlock(player, world,entity.getPosition());
					}
				} else if (cast.getSecond() == GLUTTONY) {
					// selects a 3 * 3 * 3 area
					BlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 1, 1)).forEach(blockPos -> useOnBlock(player, world, blockPos));
				} else if (cast.getSecond() == WRATH) {
					// Allows you to target entities instead
					List<Entity> entities = RayTraceUtils.rayTraceEntities(world,player.getPositionVec(),player.getLookVec(),Optional.empty(),Entity.class);
					for (Entity entity : entities){
						useOnEntity(player, entity);
					}
				} else {
					// Default EARTH SPELL
					useOnBlock(player, world, pos);
				}
			}
			if (cast.getFirst() == ORDER) {
				/*
				- Targets self
				 */
				int delay = 2000;
				int maxDistance = 8;

				if (cast.getSecond() == ENVY) {
					// targets all nearby players
					List<PlayerEntity> targets = world.getEntitiesWithinAABB(EntityType.PLAYER,
							new AxisAlignedBB(maxDistance, maxDistance, maxDistance, maxDistance, maxDistance, maxDistance),
							EntityPredicates.NOT_SPECTATING);
					for (PlayerEntity target : targets)
						useOnPlayer(target);
				} else if (cast.getSecond() == LUST) {
					// targets you and nearby pets
					List<PlayerEntity> targetsP = world.getEntitiesWithinAABB(EntityType.PLAYER,
							new AxisAlignedBB(maxDistance, maxDistance, maxDistance, maxDistance, maxDistance, maxDistance),
							EntityPredicates.NOT_SPECTATING);
					List<TameableEntity> targetsE = world.getEntitiesWithinAABB(TameableEntity.class,
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
					List<Entity> targetsE = world.getEntitiesWithinAABB(Entity.class,
							new AxisAlignedBB(maxDistance, maxDistance, maxDistance, maxDistance, maxDistance, maxDistance),
							EntityPredicates.NOT_SPECTATING);
					for (Entity target : targetsE)
						if (world.rand.nextInt(5)==2)
							useOnEntity(player, target);
				} else if (cast.getSecond() == GREED) {
					// targets nearby hostiles
					List<MobEntity> targetsE = world.getEntitiesWithinAABB(MobEntity.class,
							new AxisAlignedBB(maxDistance, maxDistance, maxDistance, maxDistance, maxDistance, maxDistance),
							EntityPredicates.NOT_SPECTATING);
					for (MobEntity target : targetsE)
						useOnEntity(player, target);
				} else if (cast.getSecond() == GLUTTONY) {
					// targets nearby dropped items
					List<ItemEntity> targetsE = world.getEntitiesWithinAABB(ItemEntity.class,
							new AxisAlignedBB(maxDistance, maxDistance, maxDistance, maxDistance, maxDistance, maxDistance),
							EntityPredicates.NOT_SPECTATING);
					for (ItemEntity target : targetsE)
						useOnEntity(player, target);
				} else if (cast.getSecond() == WRATH) {
					// becomes toggalable giving the player a faint glow and repeasts the spell every few seconds
					player.addPotionEffect(new EffectInstance(Effects.GLOWING,10,1));
					if (ToggleableCast.toggleableCasts.contains(Pair.of(spellUUID,new ToggleableCast.Impl(t -> useOnPlayer(player),delay))))
						ToggleableCast.toggleableCasts.remove(Pair.of(spellUUID,new ToggleableCast.Impl(t -> useOnPlayer(player),delay)));
					ToggleableCast.toggleableCasts.add(Pair.of(spellUUID, new ToggleableCast.Impl(t -> useOnPlayer(player),delay)));
				} else {
					// Default ORDER SPELL
					useOnPlayer(player);
				}
			}
			if (cast.getFirst() == CHAOS) {
				/*
				-A mid ranged lightning bolt
				- Targets the block / entity hit every few ticks
				 */
				// Not implemented yet
			}
		} else if (action == ICast.Action.SPECIAL) {
			if (cast.getFirst() == EARTH && cast.getSecond() == LUST){
				if (sender instanceof ItemStack) {
					int raytraceDistance = 8;
					BlockPos pos = RayTraceUtils.getTargetBlockPos(player, world, raytraceDistance);
					ItemStack stack = ((ItemStack) sender);
					ListNBT markedBlocksNBT = stack.getOrCreateTag().contains("MarkedBlocks") ? (ListNBT) stack.getOrCreateTag().get("MarkedBlocks") : new ListNBT();
					CompoundNBT markedBlock = new CompoundNBT();
					markedBlock.putInt("x", pos.getX());
					markedBlock.putInt("y", pos.getY());
					markedBlock.putInt("z", pos.getZ());
					markedBlocksNBT.add(markedBlock);
					stack.getOrCreateTag().put("MarkedBlocks", markedBlocksNBT);
				}
			}
		}
	}

	private void createSpellCloudTrap(SpellCloudEntity.CloudVariableGrid variableGrid) {
		SpellTrapEntity trap = new SpellTrapEntity(variableGrid);
	}

	private void createSpellCloud(PlayerEntity player, World world, Vector3d area, int rMultP) {
		SpellCloudEntity cloud = new SpellCloudEntity(world, area);
		cloud.setOwner(player);
		cloud.setDuration(800);
		cloud.setRadius(3.0F*(rMultP+1));
		cloud.setRadiusOnUse(-0.5F);
		cloud.setWaitTime(10);
		cloud.setRadiusPerTick(-cloud.getRadius() / (float)cloud.getDuration());
		cloud.setSpell(this);
		world.addEntity(cloud);
	}
}