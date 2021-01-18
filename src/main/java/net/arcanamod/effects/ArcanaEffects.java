package net.arcanamod.effects;

import net.arcanamod.Arcana;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.UUID;

public class ArcanaEffects extends Effect{
	
	public ArcanaEffects(EffectType type, int colour){
		super(type, colour);
	}
	
	public static final DeferredRegister<Effect> EFFECTS = new DeferredRegister<>(ForgeRegistries.POTIONS, Arcana.MODID);
	
	public static final RegistryObject<Effect> TAINTED = EFFECTS.register("tainted", TaintedEffect::new);
	public static final RegistryObject<Effect> FROZEN = EFFECTS.register("frozen", FrozenEffect::new);
	public static final RegistryObject<Effect> WARDING = EFFECTS.register("warding", ()-> new WardingEffect().addAttributesModifier(
				SharedMonsterAttributes.ARMOR,
				UUID.randomUUID().toString(), 8.0d,
				AttributeModifier.Operation.MULTIPLY_TOTAL
		)
	);
	public static final RegistryObject<Effect> VICTUS = EFFECTS.register("victus",VictusEffect::new);
}