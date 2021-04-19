package net.arcanamod.client.render;

import net.arcanamod.entities.BigSpellEggEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;

public class BigSpellEggEntityRenderer extends SpriteRenderer<BigSpellEggEntity> {

	public BigSpellEggEntityRenderer(EntityRendererManager renderManager, ItemRenderer itemRenderer, float scale, boolean fullbright){
		super(renderManager, itemRenderer, scale, fullbright);
	}

	public BigSpellEggEntityRenderer(EntityRendererManager rendererManager){
		this(rendererManager, Minecraft.getInstance().getItemRenderer(), 1, true);
	}
}