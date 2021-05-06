package net.arcanamod.entities;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class KoalaEntity extends AnimalEntity{
	
	private EatGrassGoal eatGrassGoal;
	private int koalaTimer;
	
	public KoalaEntity(EntityType<? extends KoalaEntity> type, World worldIn){
		super(type, worldIn);
	}
	
	@Override
	public AgeableEntity createChild(ServerWorld world, AgeableEntity ageable){
		KoalaEntity entity = new KoalaEntity(ArcanaEntities.KOALA_ENTITY.get(), this.world);
		entity.onInitialSpawn(world, world.getDifficultyForLocation(new BlockPos(entity.getPosition())),
				SpawnReason.BREEDING, null, null);
		return entity;
	}
	
	@Override
	protected void registerGoals(){
		super.registerGoals();
		eatGrassGoal = new EatGrassGoal(this);
		goalSelector.addGoal(0, new SwimGoal(this));
		goalSelector.addGoal(1, new PanicGoal(this, 1.25));
		goalSelector.addGoal(2, new BreedGoal(this, 1));
		goalSelector.addGoal(3,
				new TemptGoal(this, 1.1, Ingredient.fromItems(Items.WHEAT), false));
		goalSelector.addGoal(4, new FollowParentGoal(this, 1.1D));
		goalSelector.addGoal(5, eatGrassGoal);
		goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1));
		goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6));
		goalSelector.addGoal(8, new LookRandomlyGoal(this));
	}
	
	@Override
	protected void updateAITasks(){
		koalaTimer = eatGrassGoal.getEatingGrassTimer();
		super.updateAITasks();
	}
	
	@Override
	public void livingTick(){
		if(world.isRemote)
			koalaTimer = Math.max(0, koalaTimer - 1);
		super.livingTick();
	}
	
	void registerAttributesh(){
		// TODO: stats
		LivingEntity.registerAttributes();
		getAttribute(Attributes.MAX_HEALTH).setBaseValue(16.0D);
		getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.23D);
	}
	
	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id){
		if(id == 10)
			koalaTimer = 40;
		else
			super.handleStatusUpdate(id);
	}
	
	/*@OnlyIn(Dist.CLIENT)
	public float getHeadRotationPointY(float p_70894_1_){
		if(this.koalaTimer <= 0){
			return 0.0F;
		}else if(this.koalaTimer >= 4 && this.koalaTimer <= 36){
			return 1.0F;
		}else{
			return this.koalaTimer < 4 ? ((float)this.koalaTimer - p_70894_1_) / 4.0F
					: -((float)(this.koalaTimer - 40) - p_70894_1_) / 4.0F;
		}
	}*/
	
	@OnlyIn(Dist.CLIENT)
	public float getHeadRotationAngleX(float last){
		if(koalaTimer > 4 && koalaTimer <= 36){
			float f = ((float)(koalaTimer - 4) - last) / 32f;
			return ((float)Math.PI / 5) + .21991149f * MathHelper.sin(f * 28.7f);
		}else
			return koalaTimer > 0 ? ((float)Math.PI / 5) : rotationPitch * ((float)Math.PI / 180);
	}
}
