package net.arcanamod.systems.spell.modules;

import com.google.common.collect.Maps;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.systems.spell.SpellState;
import net.arcanamod.systems.spell.modules.circle.DoubleModifierCircle;
import net.arcanamod.systems.spell.modules.circle.SingleModifierCircle;
import net.arcanamod.systems.spell.modules.core.*;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public abstract class SpellModule {
	public static HashMap<String, Class<? extends SpellModule>> modules = Maps.newHashMap();
	public static HashMap<Integer, Class<? extends SpellModule>> byIndex = Maps.newHashMap();

	private List<SpellModule> bound = new ArrayList<>();
	public int x = 0, y = 0;
	public boolean unplaced = false;

	public static final ResourceLocation SPELL_MODULES = new ResourceLocation(Arcana.MODID, "textures/gui/container/foci_forge_minigame.png");

	public static SpellModule fromNBTFull(CompoundNBT spellNBT, int deepness) {
		Constructor<?> constructor;
		SpellModule createdModule = null;
		if (spellNBT.contains("name")) {
			try {
				constructor = modules.get(spellNBT.getString("name")).getConstructor();
				createdModule = (SpellModule) constructor.newInstance();
			} catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
			// get may return null. getCompound will not.
			if (!spellNBT.contains("data")) {
				createdModule = null;
			} else {
				CompoundNBT data = spellNBT.getCompound("data");
				createdModule.fromNBT(data);
			}
		}
		if (createdModule != null && spellNBT.contains("bound")) {
			for (INBT inbt : ((ListNBT) Objects.requireNonNull(spellNBT.get("bound")))) {
				if (inbt instanceof CompoundNBT) {
					CompoundNBT compound = ((CompoundNBT) inbt);
					createdModule.bindModule(SpellModule.fromNBTFull(compound, ++deepness));
				}
			}
		}
		return createdModule;
	}

	public CompoundNBT toNBTFull(CompoundNBT compound, int deepness) {
		compound.putString("name", getName());
		compound.put("data", toNBT(new CompoundNBT()));
		ListNBT boundNBT = new ListNBT();
		for (SpellModule module : getBoundModules()) {
			if (module != null) {
				CompoundNBT moduleNBT = new CompoundNBT();
				module.toNBTFull(moduleNBT, ++deepness);
				boundNBT.add(moduleNBT);
			}
		}
		compound.put("bound",boundNBT);
		return compound;
	}

	public SpellModule findParent(SpellModule spellRoot) {
		SpellModule parent = null;
		for (SpellModule bound : spellRoot.getBoundModules()) {
			if (this == bound) {
				parent = spellRoot;
				break;
			} else {
				SpellModule recurse = findParent(bound);
				if (recurse != null) {
					parent = recurse;
					break;
				}
			}
		}
		return parent;
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

	public void fromNBT(CompoundNBT compound) {
		x = compound.getInt("x");
		y = compound.getInt("y");
	}

	public CompoundNBT toNBT(CompoundNBT compound) {
		compound.putInt("x", x);
		compound.putInt("y", y);
		return compound;
	}

	public boolean withinBounds(int x, int y) {
		int relX = this.x - x;
		int relY = this.y - y;
		return (-getWidth() / 2 <= relX && relX <= getWidth() / 2
				&& -getHeight() / 2 <= relY && relY <= getHeight() / 2);
	}

	public boolean collidesWith(SpellModule other) {
		// Rectangle intersection detection (ignoring edges)
		return (other.x + other.getWidth() / 2 >= x - getWidth() / 2
				&& other.y + other.getHeight() / 2 <= y - getHeight() / 2
				&& other.x - other.getWidth() / 2 >= x + getWidth() / 2
				&& other.y - other.getHeight() / 2 <= y + getHeight() / 2);
	}

	// Foci Forge manipulation methods

	public boolean canAssign(int x, int y, Aspect aspect) {
		return false;
	}

	public void assign(int x, int y, Aspect aspect) {
	}

	public boolean canRaise(SpellState state) {
		return true;
	}

	public boolean canConnectSpecial(SpellModule connectingModule) {
		return false;
	}

	public Point getSpecialPoint(SpellModule module) {
		return null;
	}

	public int getHeight() {
		return 0;
	}

	public int getWidth() {
		return 0;
	}

	// Called when pressing the mouse button over the design area while holding a module
	public boolean mouseDown(int x, int y) { return false; }

	// Called when rendering under the mouse with 50% transparency
	public void renderUnderMouse(int x, int y) { }

	// Called when rendering the module in the spell region
	public void renderInMinigame(int mouseX, int mouseY, ItemRenderer itemRenderer) { }

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
