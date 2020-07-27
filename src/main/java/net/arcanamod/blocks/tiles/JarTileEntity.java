package net.arcanamod.blocks.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.*;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.JarBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class JarTileEntity extends TileEntity implements ITickableTileEntity, VisShareable{
	
	private final JarBlock.Type jarType;
	public AspectBattery vis = new AspectBattery(1, 100);
	private int lastVis;
	
	private int clientVis;
	private int visAnimationSpeed = ArcanaConfig.JAR_ANIMATION_SPEED.get();
	
	public JarTileEntity(JarBlock.Type type){
		super(ArcanaTiles.JAR_TE.get());
		this.jarType = type;
		vis.getHolder(0).setIgnoreFullness(type == JarBlock.Type.VOID);
	}
	
	/**
	 * @deprecated This is required when TileEntity is created. Please use JarTileEntity(JarBlock.Type).
	 */
	@Deprecated
	public JarTileEntity(){
		super(ArcanaTiles.JAR_TE.get());
		this.jarType = JarBlock.Type.BASIC;
	}
	
	@Override
	public void read(CompoundNBT compound){
		super.read(compound);
		vis.deserializeNBT(compound.getCompound("aspects"));
		clientVis = vis.getHolder(0).getCurrentVis();
	}
	
	@Override
	public CompoundNBT write(CompoundNBT compound){
		CompoundNBT aspectsNbt = vis.serializeNBT();
		compound.put("aspects", aspectsNbt);
		return super.write(compound);
	}
	
	public Color getAspectColor(){
		if(this.getWorld().getBlockState(this.getPos().down()).getBlock() == ArcanaBlocks.DUNGEON_BRICKS.get())
			return getCreativeJarColor();
		else
			return vis.getHolder(0).getContainedAspect() != Aspects.EMPTY ? new Color(vis.getHolder(0).getContainedAspect().getColorRange().get(2)) : Color.WHITE;
	}
	
	public int nextColor = 0;
	
	public Color getCreativeJarColor(){
		nextColor++;
		if(nextColor >= 800)
			nextColor = 0;
		
		final int ARRAY_SIZE = 100;
		double jump = 360.0 / (ARRAY_SIZE * 1.0);
		int[] colors = new int[ARRAY_SIZE];
		for(int i = 0; i < colors.length; i++)
			colors[i] = Color.HSBtoRGB((float)(jump * i), 1.0f, 1.0f);
		return new Color(colors[nextColor / 8]);
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap){
		if(cap == AspectHandlerCapability.ASPECT_HANDLER)
			return vis.getCapability(AspectHandlerCapability.ASPECT_HANDLER).cast();
		return LazyOptional.empty();
	}
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side){
		return getCapability(cap);
	}
	
	@Override
	@Nullable
	public SUpdateTileEntityPacket getUpdatePacket(){
		CompoundNBT nbtTagCompound = new CompoundNBT();
		write(nbtTagCompound);
		return new SUpdateTileEntityPacket(pos, -1, nbtTagCompound);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
		read(pkt.getNbtCompound());
	}
	
	/* Creates a tag containing all of the TileEntity information, used by vanilla to transmit from server to client */
	@Override
	public CompoundNBT getUpdateTag(){
		CompoundNBT nbtTagCompound = new CompoundNBT();
		write(nbtTagCompound);
		return nbtTagCompound;
	}
	
	/* Populates this TileEntity with information from the tag, used by vanilla to transmit from server to client */
	@Override
	public void handleUpdateTag(CompoundNBT tag){
		this.read(tag);
	}
	
	@Override
	public boolean isVisShareable(){
		return true;
	}

	@Override
	public boolean isSharingWithResearchTable(){
		return false;
	}
	
	@Override
	public boolean isSecure(){
		return jarType == JarBlock.Type.SECURED;
	}
	
	@Override
	public void tick(){
		int newVis = vis.getHolder(0).getCurrentVis();
		if(lastVis != newVis && world != null)
			world.updateComparatorOutputLevel(pos, world.getBlockState(pos).getBlock());
		lastVis = newVis;
		if(!getJarType().equals(JarBlock.Type.SECURED))
			vis.getHolder(0).getContainedAspect().aspectTick(this);
		if(clientVis > newVis)
			clientVis = Math.max(clientVis - visAnimationSpeed, newVis);
		else if(clientVis < newVis)
			clientVis = Math.min(clientVis + visAnimationSpeed, newVis);
	}
	
	public float getClientVis(float partialTicks){
		int newVis = vis.getHolder(0).getCurrentVis();
		if(clientVis > newVis)
			return Math.max(clientVis - (visAnimationSpeed * partialTicks), newVis);
		else if(clientVis < newVis)
			return Math.min(clientVis + (visAnimationSpeed * partialTicks), newVis);
		return clientVis;
	}
	
	public JarBlock.Type getJarType(){
		return jarType;
	}
}