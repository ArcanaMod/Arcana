package net.arcanamod.client.gui;

import net.arcanamod.Arcana;
import net.minecraft.client.gui.screen.ConfirmOpenLinkScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;

import static net.arcanamod.Arcana.MODID;

public class ArcanaDevOptionsScreen extends Screen{
	
	public ArcanaDevOptionsScreen(){
		super(new TranslationTextComponent("devtools.screen"));
	}
	
	@Override
	protected void init(){
		super.init();
		buttons.add(new Button(this.width / 2 - 102, this.height / 4 + 24 + -16, 204, 20, I18n.format("devtools.arcana_book_editmode"), b -> Arcana.proxy.openResearchBookUI(new ResourceLocation(MODID, "arcanum"))));
		buttons.add(new Button(this.width / 2 - 102, this.height / 4 + 24 + -16 + 24 + 24, 204, 20, I18n.format("menu.reportBugs"), b -> minecraft.displayGuiScreen(new ConfirmOpenLinkScreen((p_213064_1_) -> {
			if(p_213064_1_)
				Util.getOSType().openURI("https://aka.ms/snapshotbugs?ref=game");
			minecraft.displayGuiScreen(this);
		}, "https://aka.ms/snapshotbugs?ref=game", true))));
	}
	
	public void tick(){
		super.tick();
	}
	
	public void render(int p_render_1_, int p_render_2_, float p_render_3_){
		this.renderBackground();
		this.drawCenteredString(this.font, this.title.getFormattedText(), this.width / 2, 15, 16777215);
		this.drawCenteredString(this.font, I18n.format("devtools.arcana"), this.width / 2, this.height / 4 + -16 + 6, 16777215);
		this.drawCenteredString(this.font, I18n.format("devtools.mojang"), this.width / 2, this.height / 4 + 24 + -16 + 24 + 6, 16777215);
		super.render(p_render_1_, p_render_2_, p_render_3_);
	}
	
	@Override
	public boolean isPauseScreen(){
		return false;
	}
}