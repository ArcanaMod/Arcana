package net.arcanamod.systems.spell.modules;

import com.google.common.collect.Maps;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.systems.spell.casts.Casts;
import net.arcanamod.systems.spell.modules.circle.DoubleModifierCircle;
import net.arcanamod.systems.spell.modules.circle.SinModifierCircle;
import net.arcanamod.systems.spell.modules.circle.SingleModifierCircle;
import net.arcanamod.systems.spell.modules.core.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class SpellModule {
	public static HashMap<String, Class<? extends SpellModule>> modules = Maps.newHashMap();

	private List<SpellModule> bound = new ArrayList<>();

	public static SpellModule fromNBT(CompoundNBT spellNBT) {
		Constructor<?> constructor;
		try {
			constructor = modules.get(spellNBT.getString("name")).getConstructor();
			SpellModule createdModule = (SpellModule) constructor.newInstance();
			if (createdModule instanceof CastCircle)
				((CastCircle)createdModule).cast = Casts.castMap.get(new ResourceLocation(((CompoundNBT)spellNBT.get("data")).getString("cast")));
			if (createdModule instanceof CastMethod)
				((CastMethod)createdModule).aspect = AspectUtils.deserializeAspect((CompoundNBT)spellNBT.get("data"),"aspect");
			if (createdModule instanceof CastMethodSin)
				((CastMethodSin)createdModule).aspect = AspectUtils.deserializeAspect((CompoundNBT)spellNBT.get("data"),"aspect");
			if (createdModule instanceof SingleModifierCircle)
				((SingleModifierCircle)createdModule).aspect = AspectUtils.deserializeAspect((CompoundNBT)spellNBT.get("data"),"aspect");
			if (createdModule instanceof SinModifierCircle)
				((SinModifierCircle)createdModule).aspect = AspectUtils.deserializeAspect((CompoundNBT)spellNBT.get("data"),"aspect");
			if (createdModule instanceof DoubleModifierCircle) {
				((DoubleModifierCircle) createdModule).firstAspect = AspectUtils.deserializeAspect((CompoundNBT) spellNBT.get("data"), "firstAspect");
				((DoubleModifierCircle) createdModule).secondAspect = AspectUtils.deserializeAspect((CompoundNBT) spellNBT.get("data"), "secondAspect");
			}
			return createdModule;
		} catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public abstract String getName();

	public int getOutputAmount(){
		return 5;
	}

	public int getInputAmount(){
		return 5;
	}

	public abstract boolean canConnect(SpellModule connectingModule);

	// addModule
	public void bindModule(SpellModule module){
		bound.add(module);
	}

	// removeModule
	public void unbindModule(SpellModule module){
		bound.remove(module);
	}

	public List<SpellModule> getBoundModules(){
		return bound;
	}

	public abstract CompoundNBT toNBT();

	static {
		modules.put("start_circle", StartCircle.class);
		modules.put("cast_method_sin", CastMethodSin.class);
		modules.put("cast_method", CastMethod.class);
		modules.put("cast_circle", CastCircle.class);
		modules.put("sin_modifier_circle", SingleModifierCircle.class);
		modules.put("single_modifier_circle", SingleModifierCircle.class);
		modules.put("double_modifier_circle", DoubleModifierCircle.class);
		modules.put("connector", Connector.class);
		modules.put("comment", CommentBlock.class);
	}
}
