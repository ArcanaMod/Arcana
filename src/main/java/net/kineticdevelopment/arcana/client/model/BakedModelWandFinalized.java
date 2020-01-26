package net.kineticdevelopment.arcana.client.model;


import net.kineticdevelopment.arcana.common.items.ItemAttachment;
import net.kineticdevelopment.arcana.common.objects.items.ItemWand;
import net.kineticdevelopment.arcana.core.wand.EnumAttachmentType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.List;

/**
 * Finalized Baked model for wands
 * 
 * @author Merijn
 */
public class BakedModelWandFinalized implements IBakedModel {

    private IBakedModel modelMain;

    private IBakedModel[][] attachmentModels;

    private ItemStack itemStack;

    public BakedModelWandFinalized(IBakedModel modelMain, IBakedModel[][] attachmentModels) {
        this.modelMain = modelMain;
        this.attachmentModels = attachmentModels;
        this.itemStack = null;
    }

    public BakedModelWandFinalized setCurrentItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return this.modelMain.getOverrides();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.modelMain.getParticleTexture();
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState arg0, EnumFacing arg1, long arg2) {
        ArrayList<BakedQuad> list = new ArrayList<>();

        List<BakedQuad> list1 = this.modelMain.getQuads(arg0, arg1, arg2);

        ItemWand wand = (ItemWand) this.itemStack.getItem();

        IBakedModel model;


        if(list1 != null && !list1.isEmpty()) {
            list.addAll(list1);
        }

        List<BakedQuad> list2;

        ItemAttachment attachment;

        for(int i = 0; i < EnumAttachmentType.values().length; ++i) {
            try {
                attachment = wand.getAttachment(itemStack, EnumAttachmentType.values()[i]);
                if(attachment == null) {
                    continue;
                }
                model = this.attachmentModels[i][attachment.getID()];
                } catch (ArrayIndexOutOfBoundsException ignored) {
                        model = null;
                }
                if(model != null) {
                    list2 = model.getQuads(arg0, arg1, arg2);

                    if (list2 != null && !list2.isEmpty()) {
                        list.addAll(list2);
                    }
                }
        }

        return list;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return this.modelMain.isAmbientOcclusion();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return this.modelMain.isBuiltInRenderer();
    }

    @Override
    public boolean isGui3d() {
        return this.modelMain.isGui3d();
    }
}
