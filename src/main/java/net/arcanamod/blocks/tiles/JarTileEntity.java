package net.arcanamod.blocks.tiles;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.*;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import java.awt.*;
import java.time.LocalDateTime;

public class JarTileEntity extends TileEntity{

	public AspectBattery vis = new AspectBattery(1);
	public Aspect allowedAspect = Aspect.EMPTY;

	protected float smoothAmountVisContentAnimation = 0;
	protected float lastVisAmount = 0;

	long last_time = System.nanoTime();

	public JarTileEntity(){
		super(ArcanaTiles.JAR_TE.get());
	}

	public Aspect getAllowedAspect()
	{
		return allowedAspect;
	}

	public void fill(int amount, Aspect aspect) {
		lastVisAmount = vis.getHolder(0).getCurrentVis()/8f;
		allowedAspect = aspect;
		vis.insert(0, new AspectStack(allowedAspect,amount),false);
	}
	public void drain(int amount) {
		lastVisAmount = vis.getHolder(0).getCurrentVis()/8f;
		vis.drain(0, new AspectStack(allowedAspect,amount),false);
	}

	public float getAspectAmount() {
		float fullness = 12f;

		long time = System.nanoTime();
		int delta_time = (int) ((time - last_time) / 1000000);
		last_time = time;

		if (vis.getHoldersAmount()!=0){
			float visScaled = vis.getHolder(0).getCurrentVis()/8f;
			//Arcana.logger.debug("visScaled: "+ visScaled + " lastVisAmount: "+ lastVisAmount + " sAVCA: "+ smoothAmountVisContentAnimation);
			if (Math.round(smoothAmountVisContentAnimation*10f)/10f!=Math.round(visScaled*10f)/10f)
				smoothAmountVisContentAnimation += (visScaled-lastVisAmount)/5f/delta_time;
			else if (smoothAmountVisContentAnimation != Math.round(smoothAmountVisContentAnimation*100f)/100f)
				smoothAmountVisContentAnimation = Math.round(smoothAmountVisContentAnimation*100f)/100f;
			if (visScaled==0)
				smoothAmountVisContentAnimation = 0;
		}
		return -(smoothAmountVisContentAnimation-fullness);
	}

	public Color getAspectColor()
	{
		if (this.getWorld().getBlockState(this.getPos().down()).getBlock() == ArcanaBlocks.DUNGEON_BRICKS.get())
			return getCreativeJarColor();
		else return allowedAspect != Aspect.EMPTY ? new Color(allowedAspect.getAspectColor()[2]) : Color.WHITE;
	}

	public int nextColor = 0;

	public Color getCreativeJarColor()
	{
		nextColor++;
		if (nextColor >= 800)
			nextColor=0;

		final int ARRAY_SIZE = 100;
		double jump = 360.0 / (ARRAY_SIZE*1.0);
		int[] colors = new int[ARRAY_SIZE];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = Color.HSBtoRGB((float) (jump*i), 1.0f, 1.0f);
		}
		return new Color(colors[nextColor/8]);
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
		if (cap == AspectHandlerCapability.ASPECT_HANDLER)
			return vis.getCapability(AspectHandlerCapability.ASPECT_HANDLER).cast();
		return null;
	}
}