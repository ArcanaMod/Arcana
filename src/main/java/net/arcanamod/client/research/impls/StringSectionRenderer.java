package net.arcanamod.client.research.impls;

import net.arcanamod.client.gui.ResearchEntryGUI;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.research.impls.StringSection;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class StringSectionRenderer implements EntrySectionRenderer<StringSection>{
	
	private static int linesPerPage = (ResearchEntryGUI.PAGE_HEIGHT / 10) + 1;
	
	public String getTranslatedText(StringSection section){
		return I18n.format(section.getText());
	}
	
	public int span(StringSection section, PlayerEntity player){
		return (int)Math.ceil(fr().listFormattedStringToWidth(getTranslatedText(section), ResearchEntryGUI.PAGE_WIDTH).size() / (double)linesPerPage);
	}
	
	public void render(StringSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		List<String> lines = fr().listFormattedStringToWidth(getTranslatedText(section).replace("\\n", "\n"), ResearchEntryGUI.PAGE_WIDTH);
		lines = lines.subList(pageIndex * linesPerPage, Math.min((pageIndex + 1) * linesPerPage, lines.size()));
		
		int x = right ? ResearchEntryGUI.PAGE_X + ResearchEntryGUI.RIGHT_X_OFFSET : ResearchEntryGUI.PAGE_X;
		for(int i = 0; i < lines.size(); i++)
			fr().drawString(lines.get(i), (int)((screenWidth - 256) / 2f) + x, (int)((screenHeight - 181) / 2f) + ResearchEntryGUI.PAGE_Y + i * 10, 0x383838);
	}
	
	public void renderAfter(StringSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
	
	}
}