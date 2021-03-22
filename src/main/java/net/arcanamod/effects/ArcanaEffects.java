package net.arcanamod.effects;

import net.arcanamod.Arcana;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("deprecation")
public class ArcanaEffects extends Effect{
	
	public ArcanaEffects(EffectType type, int colour){
		super(type, colour);
	}
	
	// Effect UUIDs should be generated in advance
	
	public static final DeferredRegister<Effect> EFFECTS = new DeferredRegister<>(ForgeRegistries.POTIONS, Arcana.MODID);
	
	public static final RegistryObject<Effect> TAINTED = EFFECTS.register("tainted", TaintedEffect::new);
	
	public static final RegistryObject<Effect> FROZEN = EFFECTS.register("frozen", () -> new FrozenEffect().addAttributesModifier(
			SharedMonsterAttributes.MOVEMENT_SPEED,
			"4617a65e-47f6-4f2f-ac4f-eef0a46517fa", -.25,
			AttributeModifier.Operation.MULTIPLY_BASE
	));
	
	public static final RegistryObject<Effect> WARDING = EFFECTS.register("warding", () -> new WardingEffect().addAttributesModifier(
			SharedMonsterAttributes.ARMOR,
			"c429f8cd-3490-498a-ad98-21cd68e8476e", 1.5,
			AttributeModifier.Operation.MULTIPLY_BASE
	));
	
	public static final RegistryObject<Effect> VICTUS = EFFECTS.register("victus", VictusEffect::new);
}