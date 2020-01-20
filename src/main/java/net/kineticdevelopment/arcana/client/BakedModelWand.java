package net.kineticdevelopment.arcana.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

public class BakedModelWand implements IBakedModel {

    private final IBakedModel modelMain;
    private final BakedModelWandFinalized modelFinal;
    private final OverridesList overridesList;

    public BakedModelWand(IBakedModel modelMain, IBakedModel[][] attachmentModels)
    {
        this.modelMain = modelMain;
        this.modelFinal = new BakedModelWandFinalized(this.modelMain, attachmentModels);
        this.overridesList = new OverridesList(this);
    }

    public BakedModelWandFinalized getModelFinal()
    {
        return modelFinal;
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return this.overridesList;
    }



    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return this.modelMain.getParticleTexture();
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState arg0, EnumFacing arg1, long arg2)
    {
        return this.modelMain.getQuads(arg0, arg1, arg2);
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return this.modelMain.isAmbientOcclusion();
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return this.modelMain.isBuiltInRenderer();
    }

    @Override
    public boolean isGui3d()
    {
        return this.modelMain.isGui3d();
    }

private static class OverridesList extends ItemOverrideList
{
    private BakedModelWand modelWand;

    public OverridesList(BakedModelWand modelGun)
    {
        super(Collections.EMPTY_LIST);
        this.modelWand = modelGun;
    }

    @Override
    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack itemStack, World world, EntityLivingBase entity) {
        return this.modelWand.getModelFinal().setCurrentItemStack(itemStack);

    }
}

}
