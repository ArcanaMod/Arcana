package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.client.gui.ClientUiUtil;
import net.arcanamod.client.gui.ResearchEntryScreen;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.systems.research.ResearchBook;
import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.systems.research.impls.AspectCombosSection;
import net.arcanamod.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.List;

import static net.arcanamod.client.gui.ResearchEntryScreen.*;

public class AspectCombosSectionRenderer implements EntrySectionRenderer<AspectCombosSection>{
	
	protected ResourceLocation textures = null;
	
	public void render(MatrixStack stack, AspectCombosSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		ResearchBook book = ResearchBooks.getEntry(section.getEntry()).category().book();
		// don't make a new rloc every frame
		if(textures == null || !textures.getNamespace().equals(book.getKey().getNamespace()))
			textures = new ResourceLocation(book.getKey().getNamespace(), "textures/gui/research/" + book.getPrefix() + ResearchEntryScreen.OVERLAY_SUFFIX);
		
		int x = (right ? PAGE_X + RIGHT_X_OFFSET : PAGE_X) + (screenWidth - 256) / 2 + 4;
		int y = PAGE_Y + (screenHeight - 181) / 2 + HEIGHT_OFFSET + 10;
		
		List<Pair<Aspect, Aspect>> list = Aspects.COMBOS_AS_LIST;
		for(int i = pageIndex * 5, size = list.size(); i < size && i < (pageIndex + 1) * 5; i++){
			Pair<Aspect, Aspect> pair = list.get(i);
			int dispIndex = i - pageIndex * 5;
			ClientUiUtil.renderAspect(stack, pair.getFirst(), x, y + 30 * dispIndex);
			ClientUiUtil.renderAspect(stack, pair.getSecond(), x + 40, y + 30 * dispIndex);
			ClientUiUtil.renderAspect(stack, Aspects.COMBINATIONS.get(pair), x + 80, y + 30 * dispIndex);
			mc().getTextureManager().bindTexture(textures);
			ClientUiUtil.drawTexturedModalRect(stack, x + 20, y + 30 * dispIndex, 105, 161, 12, 13);
			ClientUiUtil.drawTexturedModalRect(stack, x + 60, y + 30 * dispIndex, 118, 161, 12, 13);
		}
	}
	
	public void renderAfter(MatrixStack stack, AspectCombosSection section, int pageIndex, int screenWidth, int screenHeight, int mouseX, int mouseY, boolean right, PlayerEntity player){
		int x = (right ? PAGE_X + RIGHT_X_OFFSET : PAGE_X) + (screenWidth - 256) / 2 + 4;
		int y = PAGE_Y + (screenHeight - 181) / 2 + HEIGHT_OFFSET + 10;
		
		List<Pair<Aspect, Aspect>> list = Aspects.COMBOS_AS_LIST;
		for(int i = pageIndex * 5, size = list.size(); i < size && i < (pageIndex + 1) * 5; i++){
			Pair<Aspect, Aspect> pair = list.get(i);
			int dispIndex = i - pageIndex * 5;
			if(mouseX >= x && mouseX < x + 16 && mouseY >= y + 30 * dispIndex && mouseY < y + 30 * dispIndex + 16){
				ClientUiUtil.drawAspectTooltip(stack, pair.getFirst(), "", mouseX, mouseY, screenWidth, screenHeight);
				break;
			}
			if(mouseX >= x + 40 && mouseX < x + 40 + 16 && mouseY >= y + 30 * dispIndex && mouseY < y + 30 * dispIndex + 16){
				ClientUiUtil.drawAspectTooltip(stack, pair.getSecond(), "", mouseX, mouseY, screenWidth, screenHeight);
				break;
			}
			if(mouseX >= x + 80 && mouseX < x + 80 + 16 && mouseY >= y + 30 * dispIndex && mouseY < y + 30 * dispIndex + 16){
				ClientUiUtil.drawAspectTooltip(stack, Aspects.COMBINATIONS.get(pair), "", mouseX, mouseY, screenWidth, screenHeight);
				break;
			}
		}
	}
	
	public int span(AspectCombosSection section, PlayerEntity player){
		// how many aspects fit on a page? lets say 3 for now
		return (int)Math.ceil(Aspects.COMBINATIONS.size() / 5f);
	}
}