package net.arcanamod.effects;

import net.arcanamod.Arcana;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ArcanaEffects extends Effect{
	
	public ArcanaEffects(EffectType type, int colour){
		super(type, colour);
	}
	
	public static final DeferredRegister<Effect> EFFECTS = new DeferredRegister<>(ForgeRegistries.POTIONS, Arcana.MODID);
	
	public static final RegistryObject<Effect> TAINTED = EFFECTS.register("tainted", TaintedEffect::new);
}