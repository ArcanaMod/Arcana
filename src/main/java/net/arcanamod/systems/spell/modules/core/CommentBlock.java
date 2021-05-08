package net.arcanamod.systems.spell.modules.core;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.client.gui.ClientUiUtil;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.nbt.CompoundNBT;

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
	public int getOutputAmount() {
		return 0;
	}

	@Override
	public boolean canConnect(SpellModule connectingModule, boolean special) {
		return false;
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
	public void renderUnderMouse(int x, int y, ItemRenderer itemRenderer, boolean floating, MatrixStack stack) {
		ClientUiUtil.drawTexturedModalRect(stack, x, y, 176, 0, 16, 16);
	}

	@Override
	public void renderInMinigame(int mouseX, int mouseY, ItemRenderer itemRenderer, boolean floating, MatrixStack stack) {
		int left = Math.min(x, startX);
		int top = Math.min(y, startY);

		ClientUiUtil.drawModalRectWithCustomSizedTexture(stack, left, top, 128, 0, getWidth(), getHeight(), 48, 48);
	}
}
