package net.arcanamod.entities;

import net.arcanamod.Arcana;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Initialize Entities here
 *
 * @author Mozaran
 */
public class ArcanaEntities{
	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES,
			Arcana.MODID);

	public static final RegistryObject<EntityType<KoalaEntity>> KOALA_ENTITY = ENTITY_TYPES
			.register("koala_entity", () -> EntityType.Builder.<KoalaEntity>create(KoalaEntity::new, EntityClassification.CREATURE)
				.size(0.6f, 0.6f).build(new ResourceLocation(Arcana.MODID, "koala_entity").toString()));

	public static final RegistryObject<EntityType<DairSpiritEntity>> DAIR_SPIRIT = ENTITY_TYPES
			.register("dair_spirit_entity", () -> EntityType.Builder.<DairSpiritEntity>create(DairSpiritEntity::new, EntityClassification.CREATURE)
					.size(0.6f, 0.6f).build(new ResourceLocation(Arcana.MODID, "dair_spirit_entity").toString()));
}
