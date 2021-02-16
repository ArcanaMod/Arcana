package net.arcanamod.systems.spell.modules.core;

import com.mojang.blaze3d.platform.GlStateManager;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.nbt.CompoundNBT;
import org.lwjgl.opengl.GL11;

public class CommentBlock extends SpellModule {

	public String comment = "";
	public int startX = 0, startY = 0;
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
	public void fromNBT(CompoundNBT compound) {
		super.fromNBT(compound);
		comment = compound.getString("comment");
	}

	@Override
	public CompoundNBT toNBT(CompoundNBT compound) {
		super.toNBT(compound);
		compound.putString("comment", comment);
		return compound;
	}

	@Override
	public boolean mouseDown(int x, int y) {
		this.startX = x;
		this.startY = y;
		this.dragging = true;
		return true;
	}

	@Override
	public int getHeight() {
		return Math.abs(startX - x);
	}

	@Override
	public int getWidth() {
		return Math.abs(startY - y);
	}


	@Override
	public void renderUnderMouse(int x, int y) {
		UiUtil.drawTexturedModalRect(x, y, 176, 0, 16, 16);
	}

	@Override
	public void renderInMinigame(int mouseX, int mouseY, ItemRenderer itemRenderer) {
		int left = Math.min(x, startX);
		int top = Math.min(y, startY);

		GlStateManager.enableBlend();
		GL11.glColor4f(1, 1, 1, 0.5f);
		UiUtil.drawModalRectWithCustomSizedTexture(left, top, 128, 0, getWidth(), getHeight(), 48, 48);
		GL11.glColor4f(1, 1, 1, 1.0f);
	}
}
