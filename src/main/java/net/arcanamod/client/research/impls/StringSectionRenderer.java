package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.client.gui.ResearchEntryScreen;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.systems.research.impls.StringSection;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

import static net.arcanamod.client.gui.ResearchEntryScreen.HEIGHT_OFFSET;
import static net.arcanamod.client.gui.ResearchEntryScreen.TEXT_SCALING;

public class StringSectionRenderer implements EntrySectionRenderer<StringSection>{
	
	private static int linesPerPage = (ResearchEntryScreen.PAGE_HEIGHT / (int)(10 * TEXT_SCALING)) + 1;
	
	public String getTranslatedText(StringSection section){
		return I18n.format(section.getText());
	}
	
	public int span(StringSection section, PlayerEntity player){
		return (int)Math.ceil(fr().listFormattedStringToWidth(getTranslatedText(section), (int)(ResearchEntryScreen.PAGE_WIDTH / ResearchEntryScreen.TEXT_SCALING)).size() / (double)linesPerPage);
	}
	
	public void render(StringSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		List<String> lines = fr().listFormattedStringToWidth(getTranslatedText(section).replace("\\n", "\n"), (int)(ResearchEntryScreen.PAGE_WIDTH / TEXT_SCALING));
		lines = lines.subList(pageIndex * linesPerPage, Math.min((pageIndex + 1) * linesPerPage, lines.size()));
		
		int x = right ? ResearchEntryScreen.PAGE_X + ResearchEntryScreen.RIGHT_X_OFFSET : ResearchEntryScreen.PAGE_X;
		RenderSystem.scalef(TEXT_SCALING, TEXT_SCALING, 1f);
		for(int i = 0; i < lines.size(); i++)
			fr().drawString(lines.get(i), ((int)((screenWidth - 256) / 2f) + x) / TEXT_SCALING, ((int)((screenHeight - 181) / 2f) + ResearchEntryScreen.PAGE_Y + i * (10 * TEXT_SCALING) + HEIGHT_OFFSET) / TEXT_SCALING, 0x383838);
		RenderSystem.scalef(1f / TEXT_SCALING, 1f / TEXT_SCALING, 1f);
	}
	
	public void renderAfter(StringSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){}
}