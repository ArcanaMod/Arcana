package net.kineticdevelopment.arcana.client.research.impls;

import net.kineticdevelopment.arcana.client.gui.ResearchEntryGUI;
import net.kineticdevelopment.arcana.client.research.EntrySectionRenderer;
import net.kineticdevelopment.arcana.core.research.impls.StringSection;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

import java.util.List;

public class StringSectionRenderer implements EntrySectionRenderer<StringSection>{
	
	private static int linesPerPage = ResearchEntryGUI.PAGE_HEIGHT / 9;
	
	public String getTranslatedText(StringSection section){
		return I18n.format(section.getText());
	}
	
	public int span(StringSection section, EntityPlayer player){
		return (int)Math.ceil(fr().listFormattedStringToWidth(getTranslatedText(section), ResearchEntryGUI.PAGE_WIDTH).size() / (double)linesPerPage);
	}
	
	public void render(StringSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player){
		List<String> lines = fr().listFormattedStringToWidth(getTranslatedText(section), ResearchEntryGUI.PAGE_WIDTH);
		lines = lines.subList(pageIndex * linesPerPage,
							  Math.min((pageIndex + 1) * linesPerPage,
									   lines.size()));
		
		int x = right ? ResearchEntryGUI.PAGE_X + ResearchEntryGUI.RIGHT_X_OFFSET : ResearchEntryGUI.PAGE_X;
		for(int i = 0; i < lines.size(); i++)
			fr().drawString(lines.get(i), (int)((screenWidth - 256) / 2f) + x, (int)((screenHeight - 256) / 2f) + ResearchEntryGUI.PAGE_Y + i * 9, 0);
	}
	
	public void renderAfter(StringSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, EntityPlayer player){
	
	}
}