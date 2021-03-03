package net.arcanamod.systems.spell;

import net.arcanamod.aspects.AspectStack;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

public class MDModifier {
	public static class Empty extends MDModifier {}
	public static class ReducedVis extends MDModifier {
		public final List<AspectStack> aspects;
		public ReducedVis(List<AspectStack> aspects){
			this.aspects = aspects;
		}
	}
	public static class Warping extends MDModifier {
		public static void onUse(PlayerEntity playerEntity){
			//do warp things here...
		}
	}
	public static class Creative extends MDModifier {}
	public static class Mechanical extends MDModifier {}
}
