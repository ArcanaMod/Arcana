package net.arcanamod.client.render;

import net.arcanamod.entities.SpellEggEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;

public class SpellEggEntityRenderer extends SpriteRenderer<SpellEggEntity>{
	
	public SpellEggEntityRenderer(EntityRendererManager renderManager, ItemRenderer itemRenderer, float scale, boolean fullbright){
		super(renderManager, itemRenderer, scale, fullbright);
	}
	
	public SpellEggEntityRenderer(EntityRendererManager rendererManager){
		this(rendererManager, Minecraft.getInstance().getItemRenderer(), 1, true);
	}
}