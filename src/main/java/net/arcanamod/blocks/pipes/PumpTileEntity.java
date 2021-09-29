package net.arcanamod.blocks.pipes;

import net.arcanamod.aspects.AspectStack;
import net.arcanamod.aspects.handlers.AspectHandler;
import net.arcanamod.blocks.tiles.ArcanaTiles;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class PumpTileEntity extends TubeTileEntity{
	
	protected static int PULL_AMOUNT = 3;
	protected static int PULL_TIME = 5;
	protected static float SPECK_SPEED = 0.5f;
	
	Direction direction;
	
	// pull aspects from containers and convert them into specks
	public PumpTileEntity(){
		super(ArcanaTiles.ASPECT_PUMP_TE.get());
		this.direction = Direction.UP;
	}
	
	public PumpTileEntity(Direction direction){
		super(ArcanaTiles.ASPECT_PUMP_TE.get());
		this.direction = direction;
	}
	
	public void tick(){
		super.tick();
		// pull new specks
		if(specks.size() < MAX_SPECKS && getWorld().getGameTime() % PULL_TIME == 0){
			TileEntity from = getWorld().getTileEntity(getPos().offset(direction.getOpposite()));
			AspectHandler handler = AspectHandler.getFrom(from);
			if(handler != null){
				AspectStack stack = handler.drainAny(PULL_AMOUNT);
				if(!stack.isEmpty())
					addSpeck(new AspectSpeck(stack, SPECK_SPEED, direction, 0));
			}
		}
	}
}