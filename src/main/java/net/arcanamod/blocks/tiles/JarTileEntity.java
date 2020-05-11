package net.arcanamod.blocks.tiles;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.VisHandler;
import net.arcanamod.aspects.VisHandlerCapability;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.tileentity.TileEntity;

import java.awt.*;
import java.time.LocalDateTime;

public class JarTileEntity extends TileEntity{

	public VisHandler vis = VisHandlerCapability.ASPECT_HANDLER.getDefaultInstance();
	public Aspect allowedAspect = Aspect.GREED;

	protected float smoothAmountVisContentAnimation = 0;
	protected float lastVisAmount = 0;

	long last_time = System.nanoTime();

	public JarTileEntity(){
		super(ArcanaTiles.JAR_TE.get());
	}

	public void fill(int amount)
	{
		vis.insert(allowedAspect,amount,false);
		lastVisAmount = vis.getCurrentVis(allowedAspect)-amount;
	}
	public void drain(int amount)
	{
		vis.drain(allowedAspect,amount,false);
		lastVisAmount = vis.getCurrentVis(allowedAspect)+amount;
	}

	public float getAspectAmount()
	{
		float fullness = 12f;

		long time = System.nanoTime();
		int delta_time = (int) ((time - last_time) / 1000000);
		last_time = time;

		float visScaled = vis.getCurrentVis(allowedAspect);
		Arcana.logger.debug("Combined: "+ (visScaled-lastVisAmount) + " Full: "+ (visScaled-lastVisAmount)/10f/delta_time + " Only: "+ smoothAmountVisContentAnimation
				+ " All: "+ visScaled+ " Last: "+ lastVisAmount);
		if (smoothAmountVisContentAnimation <= visScaled)
		smoothAmountVisContentAnimation += (visScaled-lastVisAmount)/10f/delta_time;
		//if (smoothAmountVisContentAnimation > visScaled) // TODO: Fix Jar draining animation
		//smoothAmountVisContentAnimation -= (visScaled-lastVisAmount)/10f/delta_time;
		return -(smoothAmountVisContentAnimation-fullness);
	}

	public Color getAspectColor()
	{
		if (this.getWorld().getBlockState(this.getPos().down()).getBlock() == ArcanaBlocks.HAWTHORN_PLANKS.get())
			return getCreativeJarColor();
		else return Aspects.getAspectColor(allowedAspect);
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
}