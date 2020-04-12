package net.kineticdevelopment.arcana.client.gui.minigames;

import net.kineticdevelopment.arcana.client.research.ClientBooks;
import net.kineticdevelopment.arcana.common.objects.tile.ResearchTableTileEntity;
import net.kineticdevelopment.arcana.core.research.Puzzle;
import net.kineticdevelopment.arcana.core.research.impls.Guesswork;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public final class GuessworkRenderer{
	
	public static void renderGuesswork(ResearchTableTileEntity rt){
		// get the actual guesswork
		NBTTagCompound compound = rt.note().getTagCompound();
		if(compound != null){
			Puzzle p = ClientBooks.puzzles.get(new ResourceLocation(compound.getString("puzzle")));
			if(p instanceof Guesswork){
				Guesswork gw = (Guesswork)p;
				
			}
		}
	}
}