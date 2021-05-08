package net.arcanamod.client.research.impls;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.client.research.PuzzleRenderer;
import net.arcanamod.containers.slots.AspectSlot;
import net.arcanamod.systems.research.impls.Thaumaturgy;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;

import java.util.List;

public class ThaumaturgyPuzzleRenderer implements PuzzleRenderer<Thaumaturgy> {
	@Override
	public void render(MatrixStack stack, Thaumaturgy puzzle, List<AspectSlot> puzzleSlots, List<Slot> puzzleItemSlots, int screenWidth, int screenHeight, int mouseX, int mouseY, PlayerEntity player) {

	}
}
