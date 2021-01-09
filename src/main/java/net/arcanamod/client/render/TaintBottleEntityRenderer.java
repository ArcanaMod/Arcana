package net.arcanamod.client.render;

import net.arcanamod.entities.TaintBottleEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;

public class TaintBottleEntityRenderer extends SpriteRenderer<TaintBottleEntity>{
	
	public TaintBottleEntityRenderer(EntityRendererManager renderManager, ItemRenderer itemRenderer, float scale, boolean fullbright){
		super(renderManager, itemRenderer, scale, fullbright);
	}
	
	public TaintBottleEntityRenderer(EntityRendererManager rendererManager){
		this(rendererManager, Minecraft.getInstance().getItemRenderer(), 1, true);
	}
}