package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.client.gui.ResearchEntryScreen;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.client.research.TextFormatter;
import net.arcanamod.systems.research.impls.StringSection;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;

import java.util.*;

import static net.arcanamod.client.gui.ClientUiUtil.drawTexturedModalRect;
import static net.arcanamod.client.gui.ResearchEntryScreen.*;

public class StringSectionRenderer implements EntrySectionRenderer<StringSection>{
	
	private static int PAGE_HEIGHT = (int)((ResearchEntryScreen.PAGE_HEIGHT / TEXT_SCALING) + 1);
	private static Map<StringSection, List<TextFormatter.Paragraph>> textCache = new HashMap<>();
	private static final int PARAGRAPH_SPACING = 6;
	
	public String getTranslatedText(StringSection section){
		// TODO: make this only run when needed
		return TextFormatter.process(I18n.format(section.getText()), section).replace("{~sep}", "\n{~sep}\n");
	}
	
	public int span(StringSection section, PlayerEntity player){
		List<TextFormatter.Paragraph> paragraphs = textCache.computeIfAbsent(section, s -> TextFormatter.compile(getTranslatedText(s), s));
		int curPage = 1;
		float curPageHeight = 0;
		for(int i = 0; i < paragraphs.size(); i++){
			TextFormatter.Paragraph paragraph = paragraphs.get(i);
			if((curPageHeight + paragraph.getHeight()) < PAGE_HEIGHT)
				curPageHeight += paragraph.getHeight() + PARAGRAPH_SPACING;
			else{
				curPage++;
				curPageHeight = 0;
				if(paragraph.getHeight() < PAGE_HEIGHT)
					// make sure this span gets added to the next line instead
					i--;
			}
		}
		return curPage;
	}
	
	public void render(MatrixStack stack, StringSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		/*List<String> lines = *//*fr().listFormattedStringToWidth*//*new ArrayList<>(Collections.singleton(getTranslatedText(section))*//*, (int)(ResearchEntryScreen.PAGE_WIDTH / TEXT_SCALING)*//*);
		lines = lines.subList(pageIndex * linesPerPage, Math.min((pageIndex + 1) * linesPerPage, lines.size()));
		
		int x = right ? ResearchEntryScreen.PAGE_X + ResearchEntryScreen.RIGHT_X_OFFSET : ResearchEntryScreen.PAGE_X;
		RenderSystem.pushMatrix();
		RenderSystem.scalef(TEXT_SCALING, TEXT_SCALING, 1);
		boolean paragraphCentred = false;
		for(int i = 0; i < lines.size(); i++){
			float lineX = ((int)((screenWidth - 256) / 2f) + x) / TEXT_SCALING;
			float lineY = ((int)((screenHeight - 181) / 2f) + ResearchEntryScreen.PAGE_Y + i * (10 * TEXT_SCALING) + HEIGHT_OFFSET) / TEXT_SCALING;
			String text = lines.get(i);
			if(text.equals("{~sep}")){
				paragraphCentred = false;
				// always called from ResearchEntryScreen
				// if we want to reuse renderers we'll make this a liiiiiittle better
				mc().getTextureManager().bindTexture(((ResearchEntryScreen)(mc().currentScreen)).bg);
				RenderSystem.color4f(1f, 1f, 1f, 1f);
				drawTexturedModalRect(stack, (int)(lineX + (PAGE_WIDTH / TEXT_SCALING - 86) / (2)), (int)lineY + 3, 29, 184, 86, 3);
			}else if(text.equals(""))
				paragraphCentred = false;
			else{
				boolean startCentre = text.startsWith("{~c}");
				if(startCentre){
					paragraphCentred = true;
					text = text.substring(4);
				}
				fr().drawString(stack, text, lineX + (paragraphCentred ? (PAGE_WIDTH / TEXT_SCALING - fr().getStringWidth(text)) / 2 : 0), lineY, 0x383838);
			}
		}
		RenderSystem.popMatrix();*/
		List<TextFormatter.Paragraph> paragraphs = textCache.computeIfAbsent(section, s -> TextFormatter.compile(getTranslatedText(s), s));
		stack.push();
		stack.scale(TEXT_SCALING, TEXT_SCALING, 1);
		int x = right ? ResearchEntryScreen.PAGE_X + ResearchEntryScreen.RIGHT_X_OFFSET : ResearchEntryScreen.PAGE_X;
		float lineX = ((int)((screenWidth - 256) / 2f) + x) / TEXT_SCALING;
		float curY = ((int)((screenHeight - 181) / 2f) + ResearchEntryScreen.PAGE_Y + HEIGHT_OFFSET) / TEXT_SCALING;
		// pick which paragraphs to display
		int curPage = 0;
		float curPageHeight = 0;
		for(int i = 0; i < paragraphs.size(); i++){
			TextFormatter.Paragraph paragraph = paragraphs.get(i);
			if((curPageHeight + paragraph.getHeight()) < PAGE_HEIGHT){
				if(curPage == pageIndex){
					paragraph.render(stack, (int)lineX, (int)curY, TEXT_SCALING);
					curY += paragraph.getHeight() + 6;
				}
				curPageHeight += paragraph.getHeight() + PARAGRAPH_SPACING;
			}else{
				curPage++;
				curPageHeight = 0;
				if(paragraph.getHeight() < PAGE_HEIGHT)
					// make sure this span gets added to the next line instead
					i--;
				else if(curPage == pageIndex){
					paragraph.render(stack, (int)lineX, (int)curY, TEXT_SCALING);
					curY += paragraph.getHeight() + 6;
				}
			}
			
		}
		stack.pop();
	}
	
	public void renderAfter(MatrixStack stack, StringSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){}
	
	public static void clearCache(){
		textCache = new HashMap<>();
	}
}