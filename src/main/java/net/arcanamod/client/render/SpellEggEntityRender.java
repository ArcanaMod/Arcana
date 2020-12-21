package net.arcanamod.client.render;

import net.arcanamod.entities.SpellEggEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.SpriteRenderer;

public class SpellEggEntityRender extends SpriteRenderer<SpellEggEntity> {
	public SpellEggEntityRender(EntityRendererManager p_i226035_1_, ItemRenderer p_i226035_2_, float p_i226035_3_, boolean p_i226035_4_) {
		super(p_i226035_1_, p_i226035_2_, p_i226035_3_, p_i226035_4_);
	}

	public SpellEggEntityRender(EntityRendererManager p_i226035_1_) {
		this(p_i226035_1_, Minecraft.getInstance().getItemRenderer(), 1.0F, true);
	}
}
