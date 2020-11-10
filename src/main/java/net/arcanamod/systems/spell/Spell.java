package net.arcanamod.systems.spell;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.systems.spell.casts.Cast;
import net.arcanamod.systems.spell.casts.Casts;
import net.arcanamod.systems.spell.casts.ICast;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.circle.DoubleModifierCircle;
import net.arcanamod.systems.spell.modules.core.*;
import net.arcanamod.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static net.arcanamod.util.Pair.of;
import static net.arcanamod.aspects.AspectUtils.deserializeAspect;

public class Spell implements ISpell {
	public SpellModule mainModule = null;

	public static ICast deserializeNBT(CompoundNBT compound){
		if (compound.contains("Spell")) {
			return null;
		} else return null;
	}

	public static CompoundNBT serializeNBT(ICast spell){
		CompoundNBT compound = new CompoundNBT();

		return compound;
	}

	private static SpellModule runSpellModule(SpellModule toUnbound, PlayerEntity caster, Object sender, ICast.Action action,
											  List<Pair<Aspect,Aspect>> castMethodsAspects, List<ICast> casts) {
		if (toUnbound.getBoundedModules().size() > 0){
			for (SpellModule module : toUnbound.getBoundedModules()) {
				if (module instanceof CastMethod)
					castMethodsAspects.add(of(((CastMethod) module).aspect,Aspects.EMPTY));
				else if (module instanceof CastMethodSin) {
					castMethodsAspects.get(castMethodsAspects.size()-1).setSecond(((CastMethodSin) module).aspect);
				} else if (module instanceof CastCircle)
					casts.add(((CastCircle) module).cast);
				return runSpellModule(module, caster, sender, action, castMethodsAspects, casts);
			}
		}else{
			for (ICast cast : casts){
				for (Pair<Aspect,Aspect> castMethodsAspect : castMethodsAspects)
				cast.use(caster,sender,castMethodsAspect,action);
			}
		}
		return null;
	}

	public static void runSpell(Spell spell, PlayerEntity caster, Object sender, ICast.Action action){
		for (SpellModule module : spell.mainModule.getBoundedModules()) {
			runSpellModule(module, caster, sender, action, new ArrayList<>(),new ArrayList<>());
		}
	}

	// Example of spell
	public static Spell createBasicSpell(){
		Spell spell = new Spell();
		Connector startToCastMethod_connector = new Connector();
		Connector castMethodToCastCircle_connector = new Connector();
		DoubleModifierCircle doubleModifierCircle = new DoubleModifierCircle();
		CastCircle castCircle = new CastCircle();
		doubleModifierCircle.firstAspect = Aspects.AIR;
		doubleModifierCircle.secondAspect = Aspects.FIRE;
		castCircle.cast = Casts.MINING_SPELL.build(new CompoundNBT());
		castCircle.bindModule(doubleModifierCircle);
		castMethodToCastCircle_connector.bindModule(castCircle);
		CastMethod castMethod = new CastMethod();
		castMethod.aspect = Aspects.EARTH;
		castMethod.bindModule(castMethodToCastCircle_connector);
		startToCastMethod_connector.bindModule(castMethod);
		spell.mainModule = new StartCircle();
		spell.mainModule.bindModule(startToCastMethod_connector);
		return spell;
	}
}