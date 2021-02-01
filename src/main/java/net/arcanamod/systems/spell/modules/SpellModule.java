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
	public static HashMap<Integer, Class<? extends SpellModule>> byIndex = Maps.newHashMap();

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

	private static void registerModule(String id, Class<? extends SpellModule> clazz, int index) {
		modules.put(id, clazz);
		byIndex.put(index, clazz);
	}

	static {
		registerModule("start_circle", StartCircle.class, 0);
		registerModule("cast_circle", CastCircle.class, 1);
		registerModule("single_modifier_circle", SingleModifierCircle.class, 2);
		registerModule("double_modifier_circle", DoubleModifierCircle.class, 3);
		registerModule("sin_modifier_circle", SingleModifierCircle.class, 4);
		registerModule("cast_method", CastMethod.class, 5);
		registerModule("cast_method_sin", CastMethodSin.class, 6);
		registerModule("connector", Connector.class, 7);
		registerModule("comment", CommentBlock.class, 8);
	}
}
