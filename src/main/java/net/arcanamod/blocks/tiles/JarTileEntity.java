package net.arcanamod.blocks.tiles;

import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.tileentity.TileEntity;

import java.awt.*;
import java.time.LocalDateTime;

public class JarTileEntity extends TileEntity{

	public JarTileEntity(){
		super(ArcanaTiles.JAR_TE.get());
	}

	public float getAspectAmount()
	{
		float fullness = 12f;

		if (this.getWorld().getBlockState(this.getPos().down()).getBlock() == ArcanaBlocks.SILVERWOOD_PLANKS.get())
			return -(6f - fullness);
		else if (this.getWorld().getBlockState(this.getPos().down()).getBlock() == ArcanaBlocks.DAIR_PLANKS.get())
			return -(10f - fullness);
		else if (this.getWorld().getBlockState(this.getPos().down()).getBlock() == ArcanaBlocks.WILLOW_PLANKS.get())
			return -(0f - fullness);
		else if (this.getWorld().getBlockState(this.getPos().down()).getBlock() == ArcanaBlocks.EUCALYPTUS_PLANKS.get())
			return -(12f - fullness);
		else if (this.getWorld().getBlockState(this.getPos().down()).getBlock() == ArcanaBlocks.HAWTHORN_PLANKS.get())
			return LocalDateTime.now().getNano()/100000000f;
		else
			return -(8f - fullness);
	}

	public Color getAspectColor()
	{
		if (this.getWorld().getBlockState(this.getPos().down()).getBlock() == ArcanaBlocks.SILVERWOOD_PLANKS.get())
			return Color.decode("#941616");
		else if (this.getWorld().getBlockState(this.getPos().down()).getBlock() == ArcanaBlocks.DAIR_PLANKS.get())
			return Color.decode("#941616");
		else if (this.getWorld().getBlockState(this.getPos().down()).getBlock() == ArcanaBlocks.WILLOW_PLANKS.get())
			return Color.decode("#941616");
		else if (this.getWorld().getBlockState(this.getPos().down()).getBlock() == ArcanaBlocks.EUCALYPTUS_PLANKS.get())
			return Color.decode("#941616");
		else if (this.getWorld().getBlockState(this.getPos().down()).getBlock() == ArcanaBlocks.HAWTHORN_PLANKS.get())
			return getCreativeJarColor();
		else
			return Color.decode("#941616");
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