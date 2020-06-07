package net.arcanamod.aspects.aspectstest;

import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class AspectTank implements IAspectHandler, IAspectTank {

	protected Predicate<AspectStack> validator;
	@Nonnull
	protected AspectStack aspect = AspectStack.EMPTY;
	protected int capacity;

	public AspectTank(int capacity)
	{
		this(capacity, e -> true);
	}

	public AspectTank(int capacity, Predicate<AspectStack> validator)
	{
		this.capacity = capacity;
		this.validator = validator;
	}

	public AspectTank setCapacity(int capacity)
	{
		this.capacity = capacity;
		return this;
	}

	public AspectTank setValidator(Predicate<AspectStack> validator)
	{
		if (validator != null) {
			this.validator = validator;
		}
		return this;
	}

	public boolean isAspectValid(AspectStack stack)
	{
		return validator.test(stack);
	}

	public int getCapacity()
	{
		return capacity;
	}

	@Nonnull
	public AspectStack getAspect()
	{
		return aspect;
	}

	public int getAspectAmount()
	{
		return aspect.getAmount();
	}

	public AspectTank readFromNBT(CompoundNBT nbt) {

		AspectStack aspect = AspectStack.loadAspectStackFromNBT(nbt);
		setAspect(aspect);
		return this;
	}

	public CompoundNBT writeToNBT(CompoundNBT nbt) {

		aspect.writeToNBT(nbt);

		return nbt;
	}

	@Override
	public int getTanks() {

		return 1;
	}

	@Nonnull
	@Override
	public AspectStack getAspectInTank(int tank) {

		return getAspect();
	}

	@Override
	public int getTankCapacity(int tank) {

		return getCapacity();
	}

	@Override
	public boolean isAspectValid(int tank, @Nonnull AspectStack stack) {

		return isAspectValid(stack);
	}

	@Override
	public int fill(AspectStack resource, IAspectHandler.AspectAction action)
	{
		if (resource.isEmpty() || !isAspectValid(resource))
		{
			return 0;
		}
		if (action.simulate())
		{
			if (aspect.isEmpty())
			{
				return Math.min(capacity, resource.getAmount());
			}
			if (!aspect.isAspectEqual(resource))
			{
				return 0;
			}
			return Math.min(capacity - aspect.getAmount(), resource.getAmount());
		}
		if (aspect.isEmpty())
		{
			aspect = new AspectStack(resource, Math.min(capacity, resource.getAmount()));
			onContentsChanged();
			return aspect.getAmount();
		}
		if (!aspect.isAspectEqual(resource))
		{
			return 0;
		}
		int filled = capacity - aspect.getAmount();

		if (resource.getAmount() < filled)
		{
			aspect.grow(resource.getAmount());
			filled = resource.getAmount();
		}
		else
		{
			aspect.setAmount(capacity);
		}
		if (filled > 0)
			onContentsChanged();
		return filled;
	}

	@Nonnull
	@Override
	public AspectStack drain(AspectStack resource, IAspectHandler.AspectAction action)
	{
		if (resource.isEmpty() || !resource.isAspectEqual(aspect))
		{
			return AspectStack.EMPTY;
		}
		return drain(resource.getAmount(), action);
	}

	@Nonnull
	@Override
	public AspectStack drain(int maxDrain, IAspectHandler.AspectAction action)
	{
		int drained = maxDrain;
		if (aspect.getAmount() < drained)
		{
			drained = aspect.getAmount();
		}
		AspectStack stack = new AspectStack(aspect, drained);
		if (action.execute() && drained > 0)
		{
			aspect.shrink(drained);
		}
		if (drained > 0)
			onContentsChanged();
		return stack;
	}

	protected void onContentsChanged()
	{

	}

	public void setAspect(AspectStack stack)
	{
		this.aspect = stack;
	}

	public boolean isEmpty()
	{
		return aspect.isEmpty();
	}

	public int getSpace()
	{
		return Math.max(0, capacity - aspect.getAmount());
	}
}
