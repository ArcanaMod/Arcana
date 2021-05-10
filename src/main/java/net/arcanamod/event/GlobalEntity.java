package net.arcanamod.event;

import net.arcanamod.entities.ArcanaEntities;
import net.arcanamod.entities.tainted.TaintedEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = "arcana")
public class GlobalEntity {
	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event){
		ArcanaEntities.T_ENTITY_TYPES.getEntries().forEach(regobj -> {
			if (regobj.isPresent()){
				event.put((EntityType<? extends LivingEntity>)regobj.get(), TaintedEntity.registerAttributes().create());
			}
		});
	}
}