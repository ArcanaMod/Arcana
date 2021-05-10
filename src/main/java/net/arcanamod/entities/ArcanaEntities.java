package net.arcanamod.entities;

import net.arcanamod.Arcana;
import net.arcanamod.systems.taint.Taint;
import net.arcanamod.entities.tainted.*;
import net.arcanamod.entities.tainted.group.TaintedFishEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.Arcana.arcLoc;

/**
 * Initialize Entities here
 *
 * @author Mozaran
 */
@SuppressWarnings("unchecked")
public class ArcanaEntities{
	public static final DeferredRegister<EntityType<?>> T_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES,
			Arcana.MODID);
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES,
			Arcana.MODID);

	public static final RegistryObject<EntityType<KoalaEntity>> KOALA_ENTITY = ENTITY_TYPES
			.register("koala_entity", () -> EntityType.Builder.create(KoalaEntity::new, EntityClassification.CREATURE)
				.size(.6f, .6f).build(arcLoc("koala_entity").toString()));

	public static final RegistryObject<EntityType<SpiritEntity>> DAIR_SPIRIT = ENTITY_TYPES
			.register("dair_spirit_entity", () -> EntityType.Builder.create(SpiritEntity::new, EntityClassification.CREATURE)
					.size(.6f, .6f).build(arcLoc("dair_spirit_entity").toString()));

	public static final RegistryObject<EntityType<SpiritEntity>> WILLOW_SPIRIT = ENTITY_TYPES
			.register("willow_spirit_entity", () -> EntityType.Builder.create(SpiritEntity::new, EntityClassification.CREATURE)
					.size(.6f, .6f).build(arcLoc("willow_spirit_entity").toString()));

	public static final RegistryObject<EntityType<SpellCloudEntity>> SPELL_CLOUD = ENTITY_TYPES
			.register("spell_cloud", () -> EntityType.Builder.<SpellCloudEntity>create(SpellCloudEntity::new, EntityClassification.MISC)
					.immuneToFire().size(6, .5f).build(arcLoc("spell_cloud").toString()));

	public static final RegistryObject<EntityType<SpellEggEntity>> SPELL_EGG = ENTITY_TYPES
			.register("spell_egg", () -> EntityType.Builder.<SpellEggEntity>create(SpellEggEntity::new, EntityClassification.MISC)
					.immuneToFire().size(.4f, .4f).build(arcLoc("spell_egg").toString()));
	public static final RegistryObject<EntityType<SpellEggEntity>> BIG_SPELL_EGG = ENTITY_TYPES
			.register("big_spell_egg", () -> EntityType.Builder.<SpellEggEntity>create(SpellEggEntity::new, EntityClassification.MISC)
					.immuneToFire().size(.6f, .6f).build(arcLoc("big_spell_egg").toString()));

	public static final RegistryObject<EntityType<TaintBottleEntity>> TAINT_BOTTLE = ENTITY_TYPES
			.register("taint_in_a_bottle", () -> EntityType.Builder.<TaintBottleEntity>create(TaintBottleEntity::new, EntityClassification.MISC)
					.immuneToFire().size(.5f, .5f).build(arcLoc("taint_in_a_bottle").toString()));

	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_DROWNED = T_ENTITY_TYPES.register("tainted_drowned", () -> Taint.taintedEntityOf(EntityType.DROWNED));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_ELDER_GUARDIAN = T_ENTITY_TYPES.register("tainted_elder_guardian", () -> Taint.taintedEntityOf(EntityType.ELDER_GUARDIAN));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_GIANT = T_ENTITY_TYPES.register("tainted_giant", () -> Taint.taintedEntityOf(EntityType.GIANT));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_GUARDIAN = T_ENTITY_TYPES.register("tainted_guardian", () -> Taint.taintedEntityOf(EntityType.GUARDIAN));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_HUSK = T_ENTITY_TYPES.register("tainted_husk", () -> Taint.taintedEntityOf(EntityType.HUSK));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_MAGMA_CUBE = T_ENTITY_TYPES.register("tainted_magma_cube", () -> Taint.taintedEntityOf(EntityType.MAGMA_CUBE));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_ZOMBIE_PIGMAN = T_ENTITY_TYPES.register("tainted_zombie_pigman", () -> Taint.taintedEntityOf(EntityType.ZOMBIE_PIGMAN));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_STRAY = T_ENTITY_TYPES.register("tainted_stray", () -> Taint.taintedEntityOf(EntityType.STRAY));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_TROPICAL_FISH = T_ENTITY_TYPES.register("tainted_tropical_fish", () -> Taint.taintedEntityOf(EntityType.TROPICAL_FISH));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_VEX = T_ENTITY_TYPES.register("tainted_vex", () -> Taint.taintedEntityOf(EntityType.VEX));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_WITHER = T_ENTITY_TYPES.register("tainted_wither", () -> Taint.taintedEntityOf(EntityType.WITHER));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_WITHER_SKELETON = T_ENTITY_TYPES.register("tainted_wither_skeleton", () -> Taint.taintedEntityOf(EntityType.WITHER_SKELETON));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_RAVAGER = T_ENTITY_TYPES.register("tainted_ravager", () -> Taint.taintedEntityOf(EntityType.RAVAGER));

	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_PUFFERFISH = T_ENTITY_TYPES.register("tainted_pufferfish", () -> Taint.taintedEntityOf(EntityType.PUFFERFISH));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_PARROT = T_ENTITY_TYPES.register("tainted_parrot", () -> Taint.taintedEntityOf(EntityType.PARROT));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_HORSE = T_ENTITY_TYPES.register("tainted_horse", () -> Taint.taintedEntityOf(EntityType.HORSE));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_MULE = T_ENTITY_TYPES.register("tainted_mule", () -> Taint.taintedEntityOf(EntityType.MULE));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_TURTLE = T_ENTITY_TYPES.register("tainted_turtle", () -> Taint.taintedEntityOf(EntityType.TURTLE));

	public static final RegistryObject<EntityType<TaintedLlamaEntity>> TAINTED_LLAMA = T_ENTITY_TYPES.register("tainted_llama", () -> Taint.taintedEntityOf(EntityType.LLAMA));
	public static final RegistryObject<EntityType<TaintedLlamaEntity>> TAINTED_TRADER_LLAMA = T_ENTITY_TYPES.register("tainted_trader_llama", () -> Taint.taintedEntityOf(EntityType.TRADER_LLAMA));
	public static final RegistryObject<EntityType<TaintedCatEntity>> TAINTED_CAT = T_ENTITY_TYPES.register("tainted_cat", () -> Taint.taintedEntityOf(EntityType.CAT));
	public static final RegistryObject<EntityType<TaintedDonkeyEntity>> TAINTED_DONKEY = T_ENTITY_TYPES.register("tainted_donkey", () -> Taint.taintedEntityOf(EntityType.DONKEY));
	public static final RegistryObject<EntityType<TaintedPolarBearEntity>> TAINTED_POLAR_BEAR = T_ENTITY_TYPES.register("tainted_polar_bear", () -> Taint.taintedEntityOf(EntityType.POLAR_BEAR));
	public static final RegistryObject<EntityType<TaintedRabbitEntity>> TAINTED_RABBIT = T_ENTITY_TYPES.register("tainted_rabbit", () -> Taint.taintedEntityOf(EntityType.RABBIT));
	public static final RegistryObject<EntityType<TaintedSnowGolemEntity>> TAINTED_SNOW_GOLEM = T_ENTITY_TYPES.register("tainted_snow_golem", () -> Taint.taintedEntityOf(EntityType.SNOW_GOLEM));
	public static final RegistryObject<EntityType<TaintedCaveSpiderEntity>> TAINTED_CAVE_SPIDER = T_ENTITY_TYPES.register("tainted_cave_spider", () -> Taint.taintedEntityOf(EntityType.CAVE_SPIDER));
	public static final RegistryObject<EntityType<TaintedIllagerEntity>> TAINTED_ILLUSIONER = T_ENTITY_TYPES.register("tainted_illusioner", () -> Taint.taintedEntityOf(EntityType.ILLUSIONER));
	public static final RegistryObject<EntityType<TaintedPandaEntity>> TAINTED_PANDA = T_ENTITY_TYPES.register("tainted_panda", () -> Taint.taintedEntityOf(EntityType.PANDA));
	public static final RegistryObject<EntityType<TaintedBatEntity>> TAINTED_BAT = T_ENTITY_TYPES.register("tainted_bat", () -> Taint.taintedEntityOf(EntityType.BAT));
	public static final RegistryObject<EntityType<TaintedBeeEntity>> TAINTED_BEE = T_ENTITY_TYPES.register("tainted_bee", () -> Taint.taintedEntityOf(EntityType.BEE));
	public static final RegistryObject<EntityType<TaintedIllagerEntity>> TAINTED_EVOKER = T_ENTITY_TYPES.register("tainted_evoker", () -> Taint.taintedEntityOf(EntityType.EVOKER));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_FOX = T_ENTITY_TYPES.register("tainted_fox", () -> Taint.taintedEntityOf(EntityType.FOX));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_SHEEP = T_ENTITY_TYPES.register("tainted_sheep", () -> Taint.taintedEntityOf(EntityType.SHEEP));// TODO: Improve Sheep AI, Eat grass or something else.
	public static final RegistryObject<EntityType<TaintedSkeletonEntity>> TAINTED_SKELETON = T_ENTITY_TYPES.register("tainted_skeleton", () -> Taint.taintedEntityOf(EntityType.SKELETON));
	public static final RegistryObject<EntityType<TaintedSlimeEntity>> TAINTED_SLIME = T_ENTITY_TYPES.register("tainted_slime", () -> Taint.taintedEntityOf(EntityType.SLIME));
	public static final RegistryObject<EntityType<TaintedIllagerEntity>> TAINTED_VINDICATOR = T_ENTITY_TYPES.register("tainted_vindicator", () -> Taint.taintedEntityOf(EntityType.VINDICATOR));
	public static final RegistryObject<EntityType<TaintedIllagerEntity>> TAINTED_PILLAGER = T_ENTITY_TYPES.register("tainted_pillager", () -> Taint.taintedEntityOf(EntityType.PILLAGER));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_WOLF = T_ENTITY_TYPES.register("tainted_wolf", () -> Taint.taintedEntityOf(EntityType.WOLF));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_ZOMBIE = T_ENTITY_TYPES.register("tainted_zombie", () -> Taint.taintedEntityOf(EntityType.ZOMBIE));

	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_COW = T_ENTITY_TYPES.register("tainted_cow", () -> Taint.taintedEntityOf(EntityType.COW));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_PIG = T_ENTITY_TYPES.register("tainted_pig", () -> Taint.taintedEntityOf(EntityType.PIG));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_SPIDER = T_ENTITY_TYPES.register("tainted_spider", () -> Taint.taintedEntityOf(EntityType.SPIDER));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_BLAZE = T_ENTITY_TYPES.register("tainted_blaze", () -> Taint.taintedEntityOf(EntityType.BLAZE));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_CHICKEN = T_ENTITY_TYPES.register("tainted_chicken", () -> Taint.taintedEntityOf(EntityType.CHICKEN));
	public static final RegistryObject<EntityType<TaintedFishEntity>> TAINTED_COD = T_ENTITY_TYPES.register("tainted_cod", () -> Taint.taintedEntityOf(EntityType.COD));
//	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_CREEPER = T_ENTITY_TYPES.register("tainted_creeper", () -> Taint.taintedEntityOf(EntityType.CREEPER));
	public static final RegistryObject<EntityType<TaintedFishEntity>> TAINTED_DOLPHIN = T_ENTITY_TYPES.register("tainted_dolphin", () -> Taint.taintedEntityOf(EntityType.DOLPHIN));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_ENDERMAN = T_ENTITY_TYPES.register("tainted_enderman", () -> Taint.taintedEntityOf(EntityType.ENDERMAN));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_ENDERMITE = T_ENTITY_TYPES.register("tainted_endermite", () -> Taint.taintedEntityOf(EntityType.ENDERMITE));
	public static final RegistryObject<EntityType<TaintedGhastEntity>> TAINTED_GHAST = T_ENTITY_TYPES.register("tainted_ghast", () -> Taint.taintedEntityOf(EntityType.GHAST));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_MOOSHROOM = T_ENTITY_TYPES.register("tainted_mooshroom", () -> Taint.taintedEntityOf(EntityType.MOOSHROOM));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_OCELOT = T_ENTITY_TYPES.register("tainted_ocelot", () -> Taint.taintedEntityOf(EntityType.OCELOT));
	public static final RegistryObject<EntityType<TaintedFishEntity>> TAINTED_SALMON = T_ENTITY_TYPES.register("tainted_salmon", () -> Taint.taintedEntityOf(EntityType.SALMON));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_SILVERFISH = T_ENTITY_TYPES.register("tainted_silverfish", () -> Taint.taintedEntityOf(EntityType.SILVERFISH));
	public static final RegistryObject<EntityType<TaintedSquidEntity>> TAINTED_SQUID = T_ENTITY_TYPES.register("tainted_squid", () -> Taint.taintedEntityOf(EntityType.SQUID));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_VILLAGER = T_ENTITY_TYPES.register("tainted_villager", () -> Taint.taintedEntityOf(EntityType.VILLAGER));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_WANDERING_TRADER = T_ENTITY_TYPES.register("tainted_wandering_trader", () -> Taint.taintedEntityOf(EntityType.WANDERING_TRADER));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_WITCH = T_ENTITY_TYPES.register("tainted_witch", () -> Taint.taintedEntityOf(EntityType.WITCH));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_PHANTOM = T_ENTITY_TYPES.register("tainted_phantom", () -> Taint.taintedEntityOf(EntityType.PHANTOM));

	public static final RegistryObject<EntityType<SpellTrapEntity>> SPELL_TRAP = ENTITY_TYPES
			.register("spell_trap", () -> EntityType.Builder.<SpellTrapEntity>create(SpellTrapEntity::new, EntityClassification.MISC)
					.immuneToFire().size(.4f, .4f).build(arcLoc("spell_trap").toString()));
}