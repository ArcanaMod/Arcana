package net.arcanamod.entities;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class DairSpiritEntity extends FlyingEntity implements IFlyingAnimal {
    public DairSpiritEntity(EntityType<? extends DairSpiritEntity> type, World worldIn) {
        super(type, worldIn);
        this.experienceValue = 5;
        this.moveController = new DairSpiritEntity.MoveHelperController(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new DairSpiritEntity.RandomFlyGoal(this));
        this.goalSelector.addGoal(7, new DairSpiritEntity.LookAroundGoal(this));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(16.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23D);
    }

    static class LookAroundGoal extends Goal {
        private final DairSpiritEntity parentEntity;

        public LookAroundGoal(DairSpiritEntity entity) {
            this.parentEntity = entity;
            this.setMutexFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean shouldExecute() {
            return true;
        }

        public void tick() {
            if (this.parentEntity.getAttackTarget() == null) {
                Vec3d lvt_1_1_ = this.parentEntity.getMotion();
                this.parentEntity.rotationYaw = -((float)MathHelper.atan2(lvt_1_1_.x, lvt_1_1_.z)) * 57.295776F;
                this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
            } else {
                LivingEntity lvt_1_2_ = this.parentEntity.getAttackTarget();
                double lvt_2_1_ = 64.0D;
                if (lvt_1_2_.getDistanceSq(this.parentEntity) < 4096.0D) {
                    double lvt_4_1_ = lvt_1_2_.getPosX() - this.parentEntity.getPosX();
                    double lvt_6_1_ = lvt_1_2_.getPosZ() - this.parentEntity.getPosZ();
                    this.parentEntity.rotationYaw = -((float)MathHelper.atan2(lvt_4_1_, lvt_6_1_)) * 57.295776F;
                    this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
                }
            }

        }
    }

    static class RandomFlyGoal extends Goal {
        private final DairSpiritEntity parentEntity;

        public RandomFlyGoal(DairSpiritEntity entity) {
            this.parentEntity = entity;
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean shouldExecute() {
            MovementController lvt_1_1_ = this.parentEntity.getMoveHelper();
            if (!lvt_1_1_.isUpdating()) {
                return true;
            } else {
                double lvt_2_1_ = lvt_1_1_.getX() - this.parentEntity.getPosX();
                double lvt_4_1_ = lvt_1_1_.getY() - this.parentEntity.getPosY();
                double lvt_6_1_ = lvt_1_1_.getZ() - this.parentEntity.getPosZ();
                double lvt_8_1_ = lvt_2_1_ * lvt_2_1_ + lvt_4_1_ * lvt_4_1_ + lvt_6_1_ * lvt_6_1_;
                return lvt_8_1_ < 1.0D || lvt_8_1_ > 3600.0D;
            }
        }

        public boolean shouldContinueExecuting() {
            return false;
        }

        public void startExecuting() {
            Random lvt_1_1_ = this.parentEntity.getRNG();
            double lvt_2_1_ = this.parentEntity.getPosX() + (double)((lvt_1_1_.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double lvt_4_1_ = this.parentEntity.getPosY() + (double)((lvt_1_1_.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double lvt_6_1_ = this.parentEntity.getPosZ() + (double)((lvt_1_1_.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.parentEntity.getMoveHelper().setMoveTo(lvt_2_1_, lvt_4_1_, lvt_6_1_, 1.0D);
        }
    }

    static class MoveHelperController extends MovementController {
        private final DairSpiritEntity parentEntity;
        private int courseChangeCooldown;

        public MoveHelperController(DairSpiritEntity entity) {
            super(entity);
            this.parentEntity = entity;
        }

        public void tick() {
            if (this.action == Action.MOVE_TO) {
                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                    Vec3d lvt_1_1_ = new Vec3d(this.posX - this.parentEntity.getPosX(), this.posY - this.parentEntity.getPosY(), this.posZ - this.parentEntity.getPosZ());
                    double lvt_2_1_ = lvt_1_1_.length();
                    lvt_1_1_ = lvt_1_1_.normalize();
                    if (this.func_220673_a(lvt_1_1_, MathHelper.ceil(lvt_2_1_))) {
                        this.parentEntity.setMotion(this.parentEntity.getMotion().add(lvt_1_1_.scale(0.1D)));
                    } else {
                        this.action = Action.WAIT;
                    }
                }

            }
        }

        private boolean func_220673_a(Vec3d pos, int size) {
            AxisAlignedBB boundingBox = this.parentEntity.getBoundingBox();

            for(int i = 1; i < size; ++i) {
                boundingBox = boundingBox.offset(pos);
                // Check collision
                if (!this.parentEntity.world.func_226665_a__(this.parentEntity, boundingBox)) {
                    return false;
                }
            }

            return true;
        }
    }
}
