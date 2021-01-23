package net.arcanamod.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.util.HandSide;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FirstPersonRenderer.class)
public interface AccessorFirstPersonRenderer {
	@Invoker("transformSideFirstPerson")
	void botania_transformSideFirstPerson(MatrixStack ms, HandSide side, float equip);

	@Invoker("transformFirstPerson")
	void botania_transformFirstPerson(MatrixStack ms, HandSide side, float swing);
}