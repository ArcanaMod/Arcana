package net.arcanamod.entities.tainted;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TaintedSquidEntity extends SquidEntity{
	public TaintedSquidEntity(EntityType<? extends Entity> type, World worldIn) {
		super((EntityType<? extends SquidEntity>) type, worldIn);
	}

	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return SquidEntity.func_234227_m_()
				.createMutableAttribute(Attributes.ATTACK_DAMAGE, 0.5D)
				.createMutableAttribute(Attributes.ATTACK_KNOCKBACK, 0.0D);
	}

	protected void registerGoals() {
		this.goalSelector.addGoal(0,new MeleeAttackGoal(this,1,false));
		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAttack));
		//this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
		//this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.6D, 1.4D, EntityPredicates.NOT_SPECTATING::test));
		this.goalSelector.addGoal(4, new TaintedSquidEntity.MoveRandomGoal(this));
	}

	private boolean shouldAttack(LivingEntity entity) {
		return true;
	}

	class MoveRandomGoal extends Goal {
		private final TaintedSquidEntity squid;

		public MoveRandomGoal(TaintedSquidEntity p_i48823_2_) {
			this.squid = p_i48823_2_;
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean shouldExecute() {
			return true;
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			int i = this.squid.getIdleTime();
			if (i > 100) {
				this.squid.setMovementVector(0.0F, 0.0F, 0.0F);
			} else if (this.squid.getRNG().nextInt(50) == 0 || !this.squid.inWater || !this.squid.hasMovementVector()) {
				float f = this.squid.getRNG().nextFloat() * ((float)Math.PI * 2F);
				float f1 = MathHelper.cos(f) * 0.2F;
				float f2 = -0.1F + this.squid.getRNG().nextFloat() * 0.2F;
				float f3 = MathHelper.sin(f) * 0.2F;
				this.squid.setMovementVector(f1, f2, f3);
			}

		}
	}
}