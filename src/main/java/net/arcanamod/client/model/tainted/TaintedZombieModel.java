package net.arcanamod.client.model.tainted;

import net.arcanamod.entities.tainted.TaintedEntity;
import net.minecraft.client.renderer.entity.model.AbstractZombieModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TaintedZombieModel<T extends TaintedEntity> extends AbstractZombieModel<T> {
	public TaintedZombieModel() {
		this(0.0F, 0.0F, 64, 64);
	}

	protected TaintedZombieModel(float p_i48914_1_, float p_i48914_2_, int p_i48914_3_, int p_i48914_4_) {
		super(p_i48914_1_, p_i48914_2_, p_i48914_3_, p_i48914_4_);
	}
	
	public boolean isAggressive(T entity){
		return entity.isAggressive();
	}
}