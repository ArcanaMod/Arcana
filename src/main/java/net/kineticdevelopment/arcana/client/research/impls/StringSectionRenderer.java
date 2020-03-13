package net.kineticdevelopment.arcana.client.research.impls;

import net.kineticdevelopment.arcana.client.research.EntrySectionRenderer;
import net.kineticdevelopment.arcana.core.research.impls.StringSection;
import net.minecraft.entity.player.EntityPlayer;

public class StringSectionRenderer implements EntrySectionRenderer<StringSection>{
	
	public int span(StringSection section, EntityPlayer player){
		return 0;
	}
	
	public void render(StringSection section, int mouseX, int mouseY, boolean right, EntityPlayer player){
	
	}
	
	public void renderAfter(StringSection section, int mouseX, int mouseY, boolean right, EntityPlayer player){
	
	}
}