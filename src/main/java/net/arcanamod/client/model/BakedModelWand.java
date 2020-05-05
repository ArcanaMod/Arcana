package net.arcanamod.client.model;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Baked Model for wands
 *
 * @author Merijn
 */
public class BakedModelWand implements IBakedModel{
	
	private final IBakedModel modelMain;
	private final BakedModelWandFinalized modelFinal;
	private final OverridesList overridesList;
	
	public BakedModelWand(IBakedModel modelMain, IBakedModel[][] attachmentModels){
		this.modelMain = modelMain;
		this.modelFinal = new BakedModelWandFinalized(this.modelMain, attachmentModels);
		this.overridesList = new OverridesList(this);
	}
	
	public BakedModelWandFinalized getModelFinal(){
		return modelFinal;
	}
	
	@Override
	public ItemOverrideList getOverrides(){
		return this.overridesList;
	}
	
	@Override
	public TextureAtlasSprite getParticleTexture(){
		return this.modelMain.getParticleTexture();
	}
	
	@Override
	public List<BakedQuad> getQuads(BlockState arg0, Direction arg1, Random arg2){
		return this.modelMain.getQuads(arg0, arg1, arg2);
	}
	
	@Override
	public boolean isAmbientOcclusion(){
		return this.modelMain.isAmbientOcclusion();
	}
	
	@Override
	public boolean isBuiltInRenderer(){
		return this.modelMain.isBuiltInRenderer();
	}
	
	@Override
	public boolean isGui3d(){
		return this.modelMain.isGui3d();
	}
	
	public boolean func_230044_c_(){
		return false;
	}
	
	private static class OverridesList extends ItemOverrideList{
		private BakedModelWand modelWand;
		
		public OverridesList(BakedModelWand modelGun){
			this.modelWand = modelGun;
		}
		
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack itemStack, World world, LivingEntity entity){
			return this.modelWand.getModelFinal().setCurrentItemStack(itemStack);
			
		}
	}
}
