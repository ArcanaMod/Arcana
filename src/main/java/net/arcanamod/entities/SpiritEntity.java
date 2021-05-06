package net.arcanamod.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class SpiritEntity extends FlyingEntity implements IFlyingAnimal {
    public SpiritEntity(EntityType<? extends SpiritEntity> type, World world){
        super(type, world);
        experienceValue = 5;
        moveController = new SpiritEntity.MoveHelperController(this);
    }

    @Override
    protected void registerGoals(){
        super.registerGoals();
        goalSelector.addGoal(5, new SpiritEntity.RandomFlyGoal(this));
        goalSelector.addGoal(7, new SpiritEntity.LookAroundGoal(this));
    }

    //@Override
    protected void registerAttributesh(){
        registerAttributes();
        // TODO: stats
        getAttribute(Attributes.MAX_HEALTH).setBaseValue(16);
        getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(.23);
    }

    static class LookAroundGoal extends Goal {

        private final SpiritEntity parentEntity;

        public LookAroundGoal(SpiritEntity entity){
            parentEntity = entity;
            setMutexFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean shouldExecute(){
            return true;
        }

        public void tick(){
            if(parentEntity.getAttackTarget() == null){
                Vector3d motion = parentEntity.getMotion();
                parentEntity.rotationYaw = -((float) MathHelper.atan2(motion.x, motion.z)) * 57.295776F;
                parentEntity.renderYawOffset = parentEntity.rotationYaw;
            }else{
                LivingEntity lvt_1_2_ = parentEntity.getAttackTarget();
                if(lvt_1_2_.getDistanceSq(parentEntity) < 4096.0D){
                    double lvt_4_1_ = lvt_1_2_.getPosX() - parentEntity.getPosX();
                    double lvt_6_1_ = lvt_1_2_.getPosZ() - parentEntity.getPosZ();
                    parentEntity.rotationYaw = -((float)MathHelper.atan2(lvt_4_1_, lvt_6_1_)) * 57.295776F;
                    parentEntity.renderYawOffset = parentEntity.rotationYaw;
                }
            }

        }
    }

    static class RandomFlyGoal extends Goal{
        private final SpiritEntity parentEntity;

        public RandomFlyGoal(SpiritEntity entity){
            parentEntity = entity;
            setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean shouldExecute(){
            MovementController movementController = parentEntity.getMoveHelper();
            if(!movementController.isUpdating()){
                return true;
            }else{
                double x = movementController.getX() - parentEntity.getPosX();
                double y = movementController.getY() - parentEntity.getPosY();
                double z = movementController.getZ() - parentEntity.getPosZ();
                double distSquared = x * x + y * y + z * z;
                return distSquared < 1 || distSquared > 3600;
            }
        }

        public boolean shouldContinueExecuting(){
            return false;
        }

        public void startExecuting(){
            Random rand = parentEntity.getRNG();
            double x = parentEntity.getPosX() + (rand.nextFloat() * 2f - 1) * 16;
            double y = parentEntity.getPosY() + (rand.nextFloat() * 2f - 1) * 16;
            double z = parentEntity.getPosZ() + (rand.nextFloat() * 2f - 1) * 16;
            parentEntity.getMoveHelper().setMoveTo(x, y, z, 1);
        }
    }

    static class MoveHelperController extends MovementController{
        private final SpiritEntity parentEntity;
        private int courseChangeCooldown;

        public MoveHelperController(SpiritEntity entity){
            super(entity);
            this.parentEntity = entity;
        }

        public void tick(){
            if(action == Action.MOVE_TO)
                if(courseChangeCooldown-- <= 0){
                    courseChangeCooldown += parentEntity.getRNG().nextInt(5) + 2;
                    Vector3d posDiff = new Vector3d(posX - parentEntity.getPosX(), posY - parentEntity.getPosY(), posZ - parentEntity.getPosZ());
                    double posLength = posDiff.length();
                    posDiff = posDiff.normalize();
                    if(canMove(posDiff, MathHelper.ceil(posLength)))
                        parentEntity.setMotion(parentEntity.getMotion().add(posDiff.scale(.1)));
                    else
                        action = Action.WAIT;
                }
        }

        private boolean canMove(Vector3d pos, int size){
            AxisAlignedBB boundingBox = this.parentEntity.getBoundingBox();

            for(int i = 1; i < size; ++i){
                boundingBox = boundingBox.offset(pos);
                // Check collision
                if(!parentEntity.world.hasNoCollisions(this.parentEntity, boundingBox))
                    return false;
            }
            return true;
        }
    }
}
