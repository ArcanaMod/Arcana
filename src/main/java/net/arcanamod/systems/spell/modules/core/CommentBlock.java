package net.arcanamod.systems.spell.modules.core;

import com.mojang.blaze3d.platform.GlStateManager;
import net.arcanamod.systems.spell.SpellState;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import org.lwjgl.opengl.GL11;

public class CommentBlock extends SpellModule {

	public String comment = "";
	public double width, height;
	boolean dragging = false, set = false;

	@Override
	public String getName() {
		return "comment";
	}

	@Override
	public int getInputAmount() {
		return 0;
	}

	@Override
	public boolean canConnect(SpellModule connectingModule) {
		return false;
	}

	@Override
	public int getOutputAmount() {
		return 0;
	}

	@Override
	public CompoundNBT toNBT() {
		CompoundNBT compound = new CompoundNBT();
		compound.putString("comment", comment);
		compound.putInt("x", x);
		compound.putInt("y", y);
		return compound;
	}
}
