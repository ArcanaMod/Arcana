package net.arcanamod.blocks.pipes;

import net.arcanamod.aspects.handlers.AspectHandler;
import net.arcanamod.blocks.tiles.ArcanaTiles;
import net.minecraft.block.BlockState;
import net.minecraft.block.SixWayBlock;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TubeTileEntity extends TileEntity implements ITickableTileEntity{
	
	protected static final int MAX_SPECKS = 1000;
	
	List<AspectSpeck> specks = new ArrayList<>();
	
	public TubeTileEntity(){
		this(ArcanaTiles.ASPECT_TUBE_TE.get());
	}
	
	public TubeTileEntity(TileEntityType<?> type){
		super(type);
	}
	
	public void tick(){
		// Move every speck along by (speed / 20f).
		// If there is a connection in the speck's direction, keep moving it until its position exceeds 0.5f.
			// When it does, pass it to pipes or insert it into AspectHandlers.
		// If not, keep moving it until its position exceeds SIZE.
			// Then make it move in the direction of a connection.
			// Prefer down, then random horizontals, then up.
		// If a speck exceeds SIZE perpendicularly to their direction (how?), bring it back to the centre.
		// If there's too many specks (1000?), explode.
		if(specks.size() > MAX_SPECKS){
			specks.clear();
			// also explode or smth
		}
		List<AspectSpeck> toRemove = new ArrayList<>();
		for(AspectSpeck speck : specks){
			Direction dir = speck.direction;
			speck.pos += speck.speed / 20f;
			float max = 0.75f;
			BlockState state = getWorld().getBlockState(pos);
			boolean connected = state.get(SixWayBlock.FACING_TO_PROPERTY_MAP.get(dir));
			if(connected)
				max = 1;
			Optional<Direction> forcedDir = redirect(speck, connected);
			if(forcedDir.isPresent() && speck.pos >= .5f && speck.pos <= max){
				speck.direction = forcedDir.get();
			}else if(speck.pos > max){
				// transfer, pass, or bounce
				BlockPos dest = pos.offset(dir);
				TileEntity te = world.getTileEntity(dest);
				if(te instanceof TubeTileEntity){
					TubeTileEntity tube = (TubeTileEntity)te;
					if(tube.enabled()){
						toRemove.add(speck);
						tube.addSpeck(speck);
						speck.pos = 0;
					}
				}else if(AspectHandler.getOptional(te).isPresent()){
					toRemove.add(speck);
					AspectHandler.getFrom(te).insert(speck.payload);
				}else{
					if(state.get(SixWayBlock.DOWN))
						speck.direction = Direction.DOWN;
					else if(state.get(SixWayBlock.NORTH) || state.get(SixWayBlock.SOUTH) || state.get(SixWayBlock.EAST) || state.get(SixWayBlock.WEST)){
						List<Direction> directions = new ArrayList<>();
						if(state.get(SixWayBlock.NORTH)) directions.add(Direction.NORTH);
						if(state.get(SixWayBlock.SOUTH)) directions.add(Direction.SOUTH);
						if(state.get(SixWayBlock.EAST)) directions.add(Direction.EAST);
						if(state.get(SixWayBlock.WEST)) directions.add(Direction.WEST);
						speck.direction = directions.get(getWorld().rand.nextInt(directions.size()));
					}else if(state.get(SixWayBlock.UP))
						speck.direction = Direction.UP;
				}
			}
		}
		specks.removeAll(toRemove);
	}
	
	protected Optional<Direction> redirect(AspectSpeck speck, boolean canPass){
		return Optional.empty();
	}
	
	public void addSpeck(AspectSpeck speck){
		specks.add(speck);
	}
	
	public boolean enabled(){
		return true;
	}
}