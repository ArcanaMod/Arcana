package net.arcanamod.aspects;

public class AspectStack {

	public static final AspectStack EMPTY = new AspectStack(Aspects.EMPTY, 0);

	private boolean isEmpty;
	private float amount;
	private Aspect aspect;

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setAspect(Aspect aspect) {
		this.aspect = aspect;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Aspect getAspect() {
		return aspect;
	}

	public AspectStack(){
		this(Aspects.EMPTY,0);
	}

	public AspectStack(Aspect aspect){
		this(aspect,1);
	}

	public AspectStack(Aspect aspect, float amount){
		this.isEmpty = amount <= 0 || aspect == Aspects.EMPTY;

		this.aspect = isEmpty ? Aspects.EMPTY : aspect;
		this.amount = isEmpty ? 0 : amount;
	}

	@Override
	public String toString() {
		return "AspectStack{" +
				"amount=" + amount +
				", aspect=" + aspect +
				'}';
	}
}
