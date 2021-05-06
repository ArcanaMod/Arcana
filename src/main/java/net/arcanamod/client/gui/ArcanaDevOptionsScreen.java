package net.arcanamod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.Arcana;
import net.arcanamod.client.ClientUtils;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.ConfirmOpenLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

import static net.arcanamod.Arcana.MODID;

public class ArcanaDevOptionsScreen extends Screen{
	
	public ArcanaDevOptionsScreen(){
		super(new TranslationTextComponent("devtools.screen"));
	}
	
	@Override
	protected void init(){
		buttons.add(new Button(this.width / 2 - 102, this.height / 4 + 24 + -16, 204, 20, new TranslationTextComponent("devtools.arcana_book_editmode"), b -> ClientUtils.openResearchBookUI(new ResourceLocation(MODID, "arcanum"), this, null)));
		buttons.add(new Button(this.width / 2 - 102, this.height / 4 + 24 + -16 + 24 + 24, 204, 20, new TranslationTextComponent("menu.reportBugs"), b -> minecraft.displayGuiScreen(new ConfirmOpenLinkScreen((p_213064_1_) -> {
			if(p_213064_1_)
				Util.getOSType().openURI("https://aka.ms/snapshotbugs?ref=game");
			minecraft.displayGuiScreen(this);
		}, "https://aka.ms/snapshotbugs?ref=game", true))));
		super.init();
	}
	
	public void tick(){
		super.tick();
	}
	
	public void render(MatrixStack stack, int p_render_1_, int p_render_2_, float p_render_3_){
		this.renderBackground(stack);
		drawCenteredString(stack, this.font, this.title.getString(), this.width / 2, 15, 16777215);
		drawCenteredString(stack, this.font, I18n.format("devtools.arcana"), this.width / 2, this.height / 4 + -16 + 6, 16777215);
		drawCenteredString(stack, this.font, I18n.format("devtools.mojang"), this.width / 2, this.height / 4 + 24 + -16 + 24 + 6, 16777215);
		super.render(stack, p_render_1_, p_render_2_, p_render_3_);
	}
	
	@Override
	public boolean isPauseScreen(){
		return false;
	}
	
	@Override
	public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_){
		return super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
	}
}