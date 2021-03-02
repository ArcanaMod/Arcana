package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.client.gui.ResearchEntryScreen;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.client.research.FormattingHelper;
import net.arcanamod.systems.research.impls.StringSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

import static net.arcanamod.client.gui.ResearchEntryScreen.*;
import static net.arcanamod.client.gui.UiUtil.drawTexturedModalRect;

public class StringSectionRenderer implements EntrySectionRenderer<StringSection>{
	
	private static int linesPerPage = (ResearchEntryScreen.PAGE_HEIGHT / (int)(10 * TEXT_SCALING)) + 1;
	
	public String getTranslatedText(StringSection section){
		// TODO: make this only run when needed
		return FormattingHelper.process(I18n.format(section.getText()).replace("{~sep}", "\n{~sep}\n"));
	}
	
	public int span(StringSection section, PlayerEntity player){
		return (int)Math.ceil(fr().listFormattedStringToWidth(getTranslatedText(section), (int)(ResearchEntryScreen.PAGE_WIDTH / ResearchEntryScreen.TEXT_SCALING)).size() / (double)linesPerPage);
	}
	
	public void render(StringSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		List<String> lines = fr().listFormattedStringToWidth(getTranslatedText(section), (int)(ResearchEntryScreen.PAGE_WIDTH / TEXT_SCALING));
		lines = lines.subList(pageIndex * linesPerPage, Math.min((pageIndex + 1) * linesPerPage, lines.size()));
		
		int x = right ? ResearchEntryScreen.PAGE_X + ResearchEntryScreen.RIGHT_X_OFFSET : ResearchEntryScreen.PAGE_X;
		RenderSystem.pushMatrix();
		RenderSystem.scalef(TEXT_SCALING, TEXT_SCALING, 1);
		for(int i = 0; i < lines.size(); i++){
			float lineX = ((int)((screenWidth - 256) / 2f) + x) / TEXT_SCALING;
			float lineY = ((int)((screenHeight - 181) / 2f) + ResearchEntryScreen.PAGE_Y + i * (10 * TEXT_SCALING) + HEIGHT_OFFSET) / TEXT_SCALING;
			if(lines.get(i).equals("{~sep}")){
				// always called from ResearchEntryScreen
				// if we want to reuse renderers we'll make this a liiiiiittle better
				mc().getTextureManager().bindTexture(((ResearchEntryScreen)(mc().currentScreen)).bg);
				RenderSystem.color4f(1f, 1f, 1f, 1f);
				drawTexturedModalRect((int)(lineX + (PAGE_WIDTH / TEXT_SCALING - 86) / (2)), (int)lineY + 3, 29, 184, 86, 3);
			}else
				fr().drawString(lines.get(i), lineX, lineY, 0x383838);
		}
		RenderSystem.popMatrix();
	}
	
	public void renderAfter(StringSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){}
}