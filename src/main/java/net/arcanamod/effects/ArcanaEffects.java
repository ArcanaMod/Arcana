package net.arcanamod.effects;

import net.arcanamod.Arcana;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ArcanaEffects extends Effect {

    public ArcanaEffects(EffectType p_i50391_1_, int p_i50391_2_) {
        super(p_i50391_1_, p_i50391_2_);
    }

    public static final DeferredRegister<Effect> EFFECTS = new DeferredRegister<>(ForgeRegistries.POTIONS, Arcana.MODID);

    public static final RegistryObject<Effect> TAINTED = EFFECTS.register("tainted", TaintedEffect::new);
}