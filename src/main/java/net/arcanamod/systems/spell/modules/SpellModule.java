package net.arcanamod.systems.spell.modules;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.systems.spell.SpellState;
import net.arcanamod.systems.spell.modules.circle.DoubleModifierCircle;
import net.arcanamod.systems.spell.modules.circle.SinModifierCircle;
import net.arcanamod.systems.spell.modules.circle.SingleModifierCircle;
import net.arcanamod.systems.spell.modules.core.*;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

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

	public List<SpellModule> bound = new ArrayList<>();
	public List<SpellModule> boundSpecial = new ArrayList<>();
	public SpellModule parent = null;
	public int x = 0, y = 0;
	public boolean unplaced = false;

	// Generates a SpellModule from NBT, including all of its bound modules.
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
					SpellModule next = SpellModule.fromNBTFull(compound, ++deepness);
					if (next != null) {
						next.setParent(createdModule);
					}
				}
			}
		}
		return createdModule;
	}

	// Generates a CompoundNBT representing this module, including bound modules.
	public CompoundNBT toNBTFull(CompoundNBT compound, int deepness) {
		compound.putString("name", getName());
		compound.put("data", toNBT(new CompoundNBT()));
		ListNBT boundNBT = new ListNBT();
		for (SpellModule module : bound) {
			if (module != null) {
				CompoundNBT moduleNBT = new CompoundNBT();
				module.toNBTFull(moduleNBT, ++deepness);
				boundNBT.add(moduleNBT);
			}
		}
		compound.put("bound",boundNBT);
		return compound;
	}

	public boolean isStartModule() {
		return false;
	}

	public boolean isCircleModule() {
		return false;
	}

	public boolean isCastModifier() {
		return false;
	}

	// Returns true if spellRoot is an ancestor of this module.
	public boolean isChildOf(SpellModule spellRoot) {
		SpellModule parent = this.parent;
		boolean found = false;
		while (parent != null) {
			parent = parent.parent;
			if (parent == spellRoot) {
				found = true;
				break;
			}
		}
		return found;
	}

	public abstract String getName();

	// Returns the maximum amount of modules that can connect to this module.
	public int getInputAmount(){
		return 1;
	}

	// Returns the maximum amount of modules that this module can connect to.
	public int getOutputAmount(){
		return 4;
	}

	// Returns true if a connection can be formed from this to connectingModule.
	// If special is true, the connection will check if th
	// Modules should override getConnectionStart, getConnectionEnd and/or canConnectSpecial instead
	public boolean canConnect(SpellModule connectingModule, boolean special) {
		SpellModule start = this.getConnectionStart();
		SpellModule end = connectingModule.getConnectionEnd(special);
		if (start == null || end == null || start == end) {
			return false;
		} else if (special) {
			return canConnectSpecial(end);
		} else {
			// Note: This assumes max 1 parent
			return bound.size() < start.getOutputAmount()
					&& end.parent == null && end.getInputAmount() > 0;
		}
	}

	// Returns true if connectingModule has an alternate connection point.
	public boolean canConnectSpecial(SpellModule connectingModule) {
		return boundSpecial.contains(connectingModule);
	}

	// Returns an alternate connection point for module.
	public Point getSpecialPoint(SpellModule module) {
		return null;
	}

	// Returns module to direct connection start points. May be null if connections are not accepted.
	public SpellModule getConnectionStart() {
		if (bound.size() < getOutputAmount()) {
			return this;
		} else {
			return null;
		}
	}

	// Returns module to direct connection end points. May be null if connections are not accepted.
	public SpellModule getConnectionEnd(boolean special) {
		if (parent == null || special) {
			return this;
		} else {
			return null;
		}
	}

	// Sets the parent module and manages binding properties.
	public void setParent(SpellModule parentNew) {
		if (parent != null) {
			parent.unbindModule(this);
		}
		if (parentNew != null) {
			parentNew.bindModule(this);
		}
	}

	// Binds module to this, forming a connection. SpellState functions should use setParent.
	public void bindModule(SpellModule module){
		bound.add(module);
		module.parent = this;
		if (canConnectSpecial(module)) {
			boundSpecial.add(module);
		}
	}

	// Unbinds an existing module, severing its connection. SpellState functions should use setParent.
	public void unbindModule(SpellModule module){
		if (module != null) {
			module.parent = null;
			bound.remove(module);
			boundSpecial.remove(module);
		}
	}

	// Fills this module with values from a CompoundNBT
	public void fromNBT(CompoundNBT compound) {
		x = compound.getInt("x");
		y = compound.getInt("y");
	}

	// Writes the values of this module to the input CompoundNBT and returns it.
	public CompoundNBT toNBT(CompoundNBT compound) {
		compound.putInt("x", x);
		compound.putInt("y", y);
		return compound;
	}

	// Returns true if the (x,y) coordinate passed is within this module's bounds.
	public boolean withinBounds(int x, int y) {
		int relX = this.x - x;
		int relY = this.y - y;
		return (-getWidth() / 2 <= relX && relX <= getWidth() / 2
				&& -getHeight() / 2 <= relY && relY <= getHeight() / 2);
	}

	// Returns true if module other at (x,y) will collide with this module.
	public boolean collidesWith(int x, int y, SpellModule other) {
		// Rectangle intersection detection (ignoring edges)
		return ( x - other.getWidth() / 2 <= this.x + getWidth() / 2
				&& y - other.getHeight() / 2 <= this.y + getHeight() / 2
				&& x + other.getWidth() / 2 >= this.x - getWidth() / 2
				&& y + other.getHeight() / 2 >= this.y - getHeight() / 2);
	}

	// Returns true if aspect can be assigned to this module at (x,y).
	public boolean canAssign(int x, int y, Aspect aspect) {
		return false;
	}

	// Assigns aspect to the slot at (x,y).
	public void assign(int x, int y, Aspect aspect) { }

	// Returns true if this module may be raised on the SpellState board
	public boolean canRaise(SpellState state) {
		return boundSpecial.isEmpty();
	}

	// Returns the module's height on the grid.
	// TODO: Maybe separate this from the render dimensions
	public abstract int getHeight();

	// Returns the module's width on the grid.
	// TODO: Maybe separate this from the render dimensions
	public abstract int getWidth();

	// Initiates a mouseDown command from the client.
	// Returns true if dragging the active module should override (disable) dragging the GUI
	public boolean mouseDown(int x, int y) { return false; }

	// Called when rendering a floating module under the mouse.
	public void renderUnderMouse(int x, int y, ItemRenderer itemRenderer, boolean floating, MatrixStack stack) { }

	// Renders the module as a member of the SpellState board.
	public void renderInMinigame(int mouseX, int mouseY, ItemRenderer itemRenderer, boolean floating, MatrixStack stack) { }

	// Returns location where connections from this module should start.
	public Point getConnectionRenderStart() {
		return new Point(this.x, this.y);
	}

	// Returns location where connections to this module should end.
	public Point getConnectionRenderEnd() {
		return new Point(this.x, this.y);
	}

	// Registers a class with the global SpellModule maps.
	private static void registerModule(String id, Class<? extends SpellModule> clazz, int index) {
		modules.put(id, clazz);
		byIndex.put(index, clazz);
	}

	static {
		registerModule("start_circle", StartCircle.class, 0);
		registerModule("cast_circle", CastCircle.class, 1);
		registerModule("single_modifier_circle", SingleModifierCircle.class, 2);
		registerModule("double_modifier_circle", DoubleModifierCircle.class, 3);
		registerModule("sin_modifier_circle", SinModifierCircle.class, 4);
		registerModule("cast_method", CastMethod.class, 5);
		registerModule("cast_method_sin", CastMethodSin.class, 6);
		registerModule("connector", Connector.class, 7);
		registerModule("comment", CommentBlock.class, 8);
	}

}
