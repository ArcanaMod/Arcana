package net.arcanamod.aspects.aspectstest;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

public class AspectStack {

	public static final AspectStack EMPTY = new AspectStack(Aspect.EMPTY, 0);

	private boolean isEmpty;
	private CompoundNBT tag;
	private int amount;
	private Aspect _aspect;

	public AspectStack(Aspect aspect, int amount)
	{
		if (aspect == null)
		{
			Arcana.logger.fatal("Null aspect supplied to AspectStack. Did you try and create a stack for an unregistered aspect?");
			throw new IllegalArgumentException("Cannot create a AspectStack from a null aspect");
		}
		this._aspect = aspect;
		this.amount = amount;

		updateEmpty();
	}

	public AspectStack(Aspect aspect, int amount, CompoundNBT nbt)
	{
		this(aspect, amount);

		if (nbt != null)
		{
			tag = nbt.copy();
		}
	}

	public AspectStack(AspectStack stack, int amount)
	{
		this(stack.getAspect(), amount, stack.tag);
	}

	/**
	 * This provides a safe method for retrieving a AspectStack - if the Aspect is invalid, the stack
	 * will return as null.
	 */
	public static AspectStack loadAspectStackFromNBT(CompoundNBT nbt)
	{
		if (nbt == null)
		{
			return EMPTY;
		}
		if (!nbt.contains("AspectName", Constants.NBT.TAG_STRING))
		{
			return EMPTY;
		}

		ResourceLocation aspectName = new ResourceLocation(nbt.getString("AspectName"));
		Aspect aspect = Aspect.valueOf(aspectName.getPath());
		if (aspect == null)
		{
			return EMPTY;
		}
		AspectStack stack = new AspectStack(aspect, nbt.getInt("Amount"));

		if (nbt.contains("Tag", Constants.NBT.TAG_COMPOUND))
		{
			stack.tag = nbt.getCompound("Tag");
		}
		return stack;
	}

	public CompoundNBT writeToNBT(CompoundNBT nbt)
	{
		nbt.putString("AspectName", getAspect().toString());
		nbt.putInt("Amount", amount);

		if (tag != null)
		{
			nbt.put("Tag", tag);
		}
		return nbt;
	}

	public void writeToPacket(PacketBuffer buf)
	{
		buf.writeEnumValue(getAspect());
		buf.writeVarInt(getAmount());
		buf.writeCompoundTag(tag);
	}

	public static AspectStack readFromPacket(PacketBuffer buf)
	{
		Aspect aspect = buf.readEnumValue(Aspect.class);
		int amount = buf.readVarInt();
		CompoundNBT tag = buf.readCompoundTag();
		if (aspect == Aspect.EMPTY) return EMPTY;
		return new AspectStack(aspect, amount, tag);
	}

	public final Aspect getAspect()
	{
		return isEmpty ? Aspect.EMPTY : _aspect;
	}

	public final Aspect getRawAspect()
	{
		return _aspect;
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	protected void updateEmpty() {
		isEmpty = getRawAspect() == Aspect.EMPTY || amount <= 0;
	}

	public int getAmount()
	{
		return isEmpty ? 0 : amount ;
	}

	public void setAmount(int amount)
	{
		if (getRawAspect() == Aspect.EMPTY) throw new IllegalStateException("Can't modify the empty stack.");
		this.amount = amount;
		updateEmpty();
	}

	public void grow(int amount) {
		setAmount(this.amount + amount);
	}

	public void shrink(int amount) {
		setAmount(this.amount - amount);
	}

	public boolean hasTag()
	{
		return tag != null;
	}

	public CompoundNBT getTag()
	{
		return tag;
	}

	public void setTag(CompoundNBT tag)
	{
		if (getRawAspect() == Aspect.EMPTY) throw new IllegalStateException("Can't modify the empty stack.");
		this.tag = tag;
	}

	public CompoundNBT getOrCreateTag()
	{
		if (tag == null)
			setTag(new CompoundNBT());
		return tag;
	}

	public CompoundNBT getChildTag(String childName)
	{
		if (tag == null)
			return null;
		return tag.getCompound(childName);
	}

	public CompoundNBT getOrCreateChildTag(String childName)
	{
		getOrCreateTag();
		CompoundNBT child = tag.getCompound(childName);
		if (!tag.contains(childName, Constants.NBT.TAG_COMPOUND))
		{
			tag.put(childName, child);
		}
		return child;
	}

	public void removeChildTag(String childName)
	{
		if (tag != null)
			tag.remove(childName);
	}

	/**
	 * @return A copy of this AspectStack
	 */
	public AspectStack copy()
	{
		return new AspectStack(getAspect(), amount, tag);
	}

	/**
	 * Determines if the AspectIDs and NBT Tags are equal. This does not check amounts.
	 *
	 * @param other
	 *            The AspectStack for comparison
	 * @return true if the Aspects (IDs and NBT Tags) are the same
	 */
	public boolean isAspectEqual(@Nonnull AspectStack other)
	{
		return getAspect() == other.getAspect() && isAspectStackTagEqual(other);
	}

	private boolean isAspectStackTagEqual(AspectStack other)
	{
		return tag == null ? other.tag == null : other.tag != null && tag.equals(other.tag);
	}

	/**
	 * Determines if the NBT Tags are equal. Useful if the AspectIDs are known to be equal.
	 */
	public static boolean areAspectStackTagsEqual(@Nonnull AspectStack stack1, @Nonnull AspectStack stack2)
	{
		return stack1.isAspectStackTagEqual(stack2);
	}

	/**
	 * Determines if the Aspects are equal and this stack is larger.
	 *
	 * @param other
	 * @return true if this AspectStack contains the other AspectStack (same aspect and >= amount)
	 */
	public boolean containsAspect(@Nonnull AspectStack other)
	{
		return isAspectEqual(other) && amount >= other.amount;
	}

	/**
	 * Determines if the AspectIDs, Amounts, and NBT Tags are all equal.
	 *
	 * @param other
	 *            - the AspectStack for comparison
	 * @return true if the two AspectStacks are exactly the same
	 */
	public boolean isAspectStackIdentical(AspectStack other)
	{
		return isAspectEqual(other) && amount == other.amount;
	}

	/*/**
	 * Determines if the AspectIDs and NBT Tags are equal compared to a registered container
	 * ItemStack. This does not check amounts.
	 *
	 * @param other
	 *            The ItemStack for comparison
	 * @return true if the Aspects (IDs and NBT Tags) are the same
	 /
	public boolean isAspectEqual(@Nonnull ItemStack other)
	{
		return AspectUtil.getAspectContained(other).map(this::isAspectEqual).orElse(false);
	}*/

	@Override
	public final int hashCode()
	{
		int code = 1;
		code = 31*code + getAspect().hashCode();
		code = 31*code + amount;
		if (tag != null)
			code = 31*code + tag.hashCode();
		return code;
	}

	/**
	 * Default equality comparison for a AspectStack. Same functionality as isAspectEqual().
	 *
	 * This is included for use in data structures.
	 */
	@Override
	public final boolean equals(Object o)
	{
		if (!(o instanceof AspectStack))
		{
			return false;
		}
		return isAspectEqual((AspectStack) o);
	}
}
