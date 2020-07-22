package net.arcanamod.entities;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.Taint;
import net.arcanamod.client.model.tainted.TaintedFoxModel;
import net.arcanamod.client.model.tainted.TaintedSheepModel;
import net.arcanamod.client.model.tainted.TaintedWolfModel;
import net.arcanamod.client.model.tainted.TaintedZombieModel;
import net.arcanamod.client.render.tainted.*;
import net.arcanamod.entities.tainted.*;
import net.minecraft.client.renderer.entity.model.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES,
			Arcana.MODID);

	public static final RegistryObject<EntityType<KoalaEntity>> KOALA_ENTITY = ENTITY_TYPES
			.register("koala_entity", () -> EntityType.Builder.create(KoalaEntity::new, EntityClassification.CREATURE)
				.size(0.6f, 0.6f).build(arcLoc("koala_entity").toString()));

	public static final RegistryObject<EntityType<DairSpiritEntity>> DAIR_SPIRIT = ENTITY_TYPES
			.register("dair_spirit_entity", () -> EntityType.Builder.create(DairSpiritEntity::new, EntityClassification.CREATURE)
					.size(0.6f, 0.6f).build(arcLoc("dair_spirit_entity").toString()));

	// Tainted

	public static final RegistryObject<EntityType<TaintedBatEntity>> TAINTED_BAT = ENTITY_TYPES.register("tainted_bat", () -> Taint.taintedEntityOf(EntityType.BAT));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_BEE = ENTITY_TYPES.register("tainted_bee", () -> Taint.taintedEntityOf(EntityType.BEE));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_CAT = ENTITY_TYPES.register("tainted_cat", () -> Taint.taintedEntityOf(EntityType.CAT));
	public static final RegistryObject<EntityType<TaintedCaveSpiderEntity>> TAINTED_CAVE_SPIDER = ENTITY_TYPES.register("tainted_cave_spider", () -> Taint.taintedEntityOf(EntityType.CAVE_SPIDER));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_DONKEY = ENTITY_TYPES.register("tainted_donkey", () -> Taint.taintedEntityOf(EntityType.DONKEY));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_DROWNED = ENTITY_TYPES.register("tainted_drowned", () -> Taint.taintedEntityOf(EntityType.DROWNED));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_ELDER_GUARDIAN = ENTITY_TYPES.register("tainted_elder_guardian", () -> Taint.taintedEntityOf(EntityType.ELDER_GUARDIAN));
	public static final RegistryObject<EntityType<TaintedIllagerEntity>> TAINTED_EVOKER = ENTITY_TYPES.register("tainted_evoker", () -> Taint.taintedEntityOf(EntityType.EVOKER));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_FOX = ENTITY_TYPES.register("tainted_fox", () -> Taint.taintedEntityOf(EntityType.FOX));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_GIANT = ENTITY_TYPES.register("tainted_giant", () -> Taint.taintedEntityOf(EntityType.GIANT));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_GUARDIAN = ENTITY_TYPES.register("tainted_guardian", () -> Taint.taintedEntityOf(EntityType.GUARDIAN));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_HORSE = ENTITY_TYPES.register("tainted_horse", () -> Taint.taintedEntityOf(EntityType.HORSE));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_HUSK = ENTITY_TYPES.register("tainted_husk", () -> Taint.taintedEntityOf(EntityType.HUSK));
	public static final RegistryObject<EntityType<TaintedIllagerEntity>> TAINTED_ILLUSIONER = ENTITY_TYPES.register("tainted_illusioner", () -> Taint.taintedEntityOf(EntityType.ILLUSIONER));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_LLAMA = ENTITY_TYPES.register("tainted_llama", () -> Taint.taintedEntityOf(EntityType.LLAMA));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_MAGMA_CUBE = ENTITY_TYPES.register("tainted_magma_cube", () -> Taint.taintedEntityOf(EntityType.MAGMA_CUBE));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_MULE = ENTITY_TYPES.register("tainted_mule", () -> Taint.taintedEntityOf(EntityType.MULE));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_PANDA = ENTITY_TYPES.register("tainted_panda", () -> Taint.taintedEntityOf(EntityType.PANDA));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_PARROT = ENTITY_TYPES.register("tainted_parrot", () -> Taint.taintedEntityOf(EntityType.PARROT));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_PUFFERFISH = ENTITY_TYPES.register("tainted_pufferfish", () -> Taint.taintedEntityOf(EntityType.PUFFERFISH));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_ZOMBIE_PIGMAN = ENTITY_TYPES.register("tainted_zombie_pigman", () -> Taint.taintedEntityOf(EntityType.ZOMBIE_PIGMAN));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_POLAR_BEAR = ENTITY_TYPES.register("tainted_polar_bear", () -> Taint.taintedEntityOf(EntityType.POLAR_BEAR));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_RABBIT = ENTITY_TYPES.register("tainted_rabbit", () -> Taint.taintedEntityOf(EntityType.RABBIT));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_SHEEP = ENTITY_TYPES.register("tainted_sheep", () -> Taint.taintedEntityOf(EntityType.SHEEP));// TODO: Improve Sheep AI, Eat grass or something else.
	public static final RegistryObject<EntityType<TaintedSkeletonEntity>> TAINTED_SKELETON = ENTITY_TYPES.register("tainted_skeleton", () -> Taint.taintedEntityOf(EntityType.SKELETON));
	public static final RegistryObject<EntityType<TaintedSlimeEntity>> TAINTED_SLIME = ENTITY_TYPES.register("tainted_slime", () -> Taint.taintedEntityOf(EntityType.SLIME));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_SNOW_GOLEM = ENTITY_TYPES.register("tainted_snow_golem", () -> Taint.taintedEntityOf(EntityType.SNOW_GOLEM));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_STRAY = ENTITY_TYPES.register("tainted_stray", () -> Taint.taintedEntityOf(EntityType.STRAY));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_TRADER_LLAMA = ENTITY_TYPES.register("tainted_trader_llama", () -> Taint.taintedEntityOf(EntityType.TRADER_LLAMA));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_TROPICAL_FISH = ENTITY_TYPES.register("tainted_tropical_fish", () -> Taint.taintedEntityOf(EntityType.TROPICAL_FISH));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_TURTLE = ENTITY_TYPES.register("tainted_turtle", () -> Taint.taintedEntityOf(EntityType.TURTLE));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_VEX = ENTITY_TYPES.register("tainted_vex", () -> Taint.taintedEntityOf(EntityType.VEX));
	public static final RegistryObject<EntityType<TaintedIllagerEntity>> TAINTED_VINDICATOR = ENTITY_TYPES.register("tainted_vindicator", () -> Taint.taintedEntityOf(EntityType.VINDICATOR));
	public static final RegistryObject<EntityType<TaintedIllagerEntity>> TAINTED_PILLAGER = ENTITY_TYPES.register("tainted_pillager", () -> Taint.taintedEntityOf(EntityType.PILLAGER));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_WITHER = ENTITY_TYPES.register("tainted_wither", () -> Taint.taintedEntityOf(EntityType.WITHER));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_WITHER_SKELETON = ENTITY_TYPES.register("tainted_wither_skeleton", () -> Taint.taintedEntityOf(EntityType.WITHER_SKELETON));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_WOLF = ENTITY_TYPES.register("tainted_wolf", () -> Taint.taintedEntityOf(EntityType.WOLF));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_ZOMBIE = ENTITY_TYPES.register("tainted_zombie", () -> Taint.taintedEntityOf(EntityType.ZOMBIE));
	//public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_RAVAGER = ENTITY_TYPES.register("tainted_ravager", () -> Taint.taintedEntityOf(EntityType.RAVAGER));

	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_COW = ENTITY_TYPES.register("tainted_cow", () -> Taint.taintedEntityOf(EntityType.COW));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_PIG = ENTITY_TYPES.register("tainted_pig", () -> Taint.taintedEntityOf(EntityType.PIG));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_SPIDER = ENTITY_TYPES.register("tainted_spider", () -> Taint.taintedEntityOf(EntityType.SPIDER));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_BLAZE = ENTITY_TYPES.register("tainted_blaze", () -> Taint.taintedEntityOf(EntityType.BLAZE));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_CHICKEN = ENTITY_TYPES.register("tainted_chicken", () -> Taint.taintedEntityOf(EntityType.CHICKEN));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_COD = ENTITY_TYPES.register("tainted_cod", () -> Taint.taintedEntityOf(EntityType.COD));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_CREEPER = ENTITY_TYPES.register("tainted_creeper", () -> Taint.taintedEntityOf(EntityType.CREEPER));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_DOLPHIN = ENTITY_TYPES.register("tainted_dolphin", () -> Taint.taintedEntityOf(EntityType.DOLPHIN));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_ENDERMAN = ENTITY_TYPES.register("tainted_enderman", () -> Taint.taintedEntityOf(EntityType.ENDERMAN));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_ENDERMITE = ENTITY_TYPES.register("tainted_endermite", () -> Taint.taintedEntityOf(EntityType.ENDERMITE));
	public static final RegistryObject<EntityType<TaintedGhastEntity>> TAINTED_GHAST = ENTITY_TYPES.register("tainted_ghast", () -> Taint.taintedEntityOf(EntityType.GHAST));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_MOOSHROOM = ENTITY_TYPES.register("tainted_mooshroom", () -> Taint.taintedEntityOf(EntityType.MOOSHROOM));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_OCELOT = ENTITY_TYPES.register("tainted_ocelot", () -> Taint.taintedEntityOf(EntityType.OCELOT));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_SALMON = ENTITY_TYPES.register("tainted_salmon", () -> Taint.taintedEntityOf(EntityType.SALMON));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_SILVERFISH = ENTITY_TYPES.register("tainted_silverfish", () -> Taint.taintedEntityOf(EntityType.SILVERFISH));
	public static final RegistryObject<EntityType<TaintedSquidEntity>> TAINTED_SQUID = ENTITY_TYPES.register("tainted_squid", () -> Taint.taintedEntityOf(EntityType.SQUID));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_VILLAGER = ENTITY_TYPES.register("tainted_villager", () -> Taint.taintedEntityOf(EntityType.VILLAGER));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_WANDERING_TRADER = ENTITY_TYPES.register("tainted_wandering_trader", () -> Taint.taintedEntityOf(EntityType.WANDERING_TRADER));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_WITCH = ENTITY_TYPES.register("tainted_witch", () -> Taint.taintedEntityOf(EntityType.WITCH));
	public static final RegistryObject<EntityType<TaintedEntity>> TAINTED_PHANTOM = ENTITY_TYPES.register("tainted_phantom", () -> Taint.taintedEntityOf(EntityType.PHANTOM));

	@SuppressWarnings("rawtypes")
	public static void render() {
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_BAT.get(), TaintedBatRender::new);
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_BEE.get(), manager -> new TaintedEntityRender(manager, new BeeModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_CAT.get(), manager -> new TaintedEntityRender(manager, new CatModel(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_CAVE_SPIDER.get(), TaintedCaveSpiderRender::new);
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_DONKEY.get(), manager -> new TaintedEntityRender(manager, new HorseModel(0.0F)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_DROWNED.get(), manager -> new TaintedEntityRender(manager, new DrownedModel(0.5f, true)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_ELDER_GUARDIAN.get(), manager -> new TaintedEntityRender(manager, new GuardianModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_EVOKER.get(), manager -> new TaintedEntityRender(manager, new IllagerModel(0.0F, 0.0F, 64, 64)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_FOX.get(), manager -> new TaintedEntityRender(manager, new TaintedFoxModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_GIANT.get(), manager -> new TaintedEntityRender(manager, new GiantModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_GUARDIAN.get(), manager -> new TaintedEntityRender(manager, new GuardianModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_HORSE.get(), manager -> new TaintedEntityRender(manager, new HorseModel(0.0F)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_HUSK.get(), manager -> new TaintedEntityRender(manager, new ZombieModel(0.0F, false)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_ILLUSIONER.get(), manager -> new TaintedEntityRender(manager, new IllagerModel(0.0F, 0.0F, 64, 64)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_LLAMA.get(), manager -> new TaintedEntityRender(manager, new LlamaModel(0.0F)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_MAGMA_CUBE.get(), manager -> new TaintedEntityRender(manager, new MagmaCubeModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_MULE.get(), manager -> new TaintedEntityRender(manager, new HorseModel(0.0F)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_PANDA.get(), manager -> new TaintedEntityRender(manager, new PandaModel(9, 0.0F)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_PARROT.get(), manager -> new TaintedEntityRender(manager, new ParrotModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_PUFFERFISH.get(), manager -> new TaintedEntityRender(manager, new PufferFishMediumModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_ZOMBIE_PIGMAN.get(), manager -> new TaintedEntityRender(manager, new ZombieModel(0.0F, false))); // 1.16 -> ZombiefiedPiglin
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_POLAR_BEAR.get(), manager -> new TaintedEntityRender(manager, new PolarBearModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_RABBIT.get(), manager -> new TaintedEntityRender(manager, new RabbitModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SHEEP.get(), manager -> new TaintedEntityRender(manager, new TaintedSheepModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SKELETON.get(), manager -> new TaintedEntityRender(manager, new SkeletonModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SLIME.get(), TaintedSlimeRender::new);
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SNOW_GOLEM.get(), manager -> new TaintedEntityRender(manager, new SnowManModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_STRAY.get(), manager -> new TaintedEntityRender(manager, new SkeletonModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_TRADER_LLAMA.get(), manager -> new TaintedEntityRender(manager, new LlamaModel(0.0F)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_TROPICAL_FISH.get(), manager -> new TaintedEntityRender(manager, new TropicalFishAModel(0.0F)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_TURTLE.get(), manager -> new TaintedEntityRender(manager, new TurtleModel(0.0F)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_VEX.get(), manager -> new TaintedEntityRender(manager, new VexModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_VINDICATOR.get(), manager -> new TaintedEntityRender(manager, new IllagerModel(0.0F, 0.0F, 64, 64)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_PILLAGER.get(), manager -> new TaintedEntityRender(manager, new IllagerModel(0.0F, 0.0F, 64, 64)));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_WITHER.get(), manager -> new TaintedEntityRender(manager, new WitherModel(0.0F))); // Check this.
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_WITHER_SKELETON.get(), manager -> new TaintedEntityRender(manager, new SkeletonModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_WOLF.get(), manager -> new TaintedEntityRender(manager, new TaintedWolfModel()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_ZOMBIE.get(), manager -> new TaintedEntityRender(manager, new TaintedZombieModel()));
		//RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_RAVAGER.get(), manager -> new TaintedEntityRender(manager, new RavagerModel()));

		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_COW.get(), manager -> new TaintedEntityRender(manager, new CowModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_PIG.get(), manager -> new TaintedEntityRender(manager, new PigModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SPIDER.get(), manager -> new TaintedEntityRender(manager, new SpiderModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_BLAZE.get(), manager -> new TaintedEntityRender(manager, new BlazeModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_CHICKEN.get(), manager -> new TaintedEntityRender(manager, new ChickenModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_COD.get(), manager -> new TaintedEntityRender(manager, new CodModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_CREEPER.get(), manager -> new TaintedEntityRender(manager, new CreeperModel<TaintedCreeperEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_DOLPHIN.get(), manager -> new TaintedEntityRender(manager, new DolphinModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_ENDERMAN.get(), manager -> new TaintedEntityRender(manager, new EndermanModel<TaintedEntity>(0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_ENDERMITE.get(), manager -> new TaintedEntityRender(manager, new EndermiteModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_GHAST.get(), TaintedGhastRender::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_MOOSHROOM.get(), manager -> new TaintedEntityRender(manager, new CowModel<TaintedEntity>())); // No tainted_warts on top
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_OCELOT.get(), manager -> new TaintedEntityRender(manager, new OcelotModel<TaintedEntity>(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SALMON.get(), manager -> new TaintedEntityRender(manager, new SalmonModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SILVERFISH.get(), manager -> new TaintedEntityRender(manager, new SilverfishModel<TaintedEntity>()));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_SQUID.get(), TaintedSquidRenderer::new);
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_VILLAGER.get(), manager -> new TaintedEntityRender(manager, new VillagerModel<TaintedEntity>(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_WANDERING_TRADER.get(), manager -> new TaintedEntityRender(manager, new VillagerModel<TaintedEntity>(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_WITCH.get(), manager -> new TaintedEntityRender(manager, new WitchModel<TaintedEntity>(0.0F)));
		RenderingRegistry.registerEntityRenderingHandler(ArcanaEntities.TAINTED_PHANTOM.get(), manager -> new TaintedEntityRender(manager, new PhantomModel<TaintedEntity>()));
	}
}