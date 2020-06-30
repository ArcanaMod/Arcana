package net.arcanamod.blocks.tiles;

import net.arcanamod.aspects.AspectBattery;
import net.arcanamod.aspects.IAspectHandler;
import net.arcanamod.aspects.IVisShareable;
import net.arcanamod.blocks.multiblocks.ITaintScrubberExtension;
import net.arcanamod.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class TaintScrubberTileEntity extends TileEntity implements ITickableTileEntity {

	private int nextRefresh = 10;
	private List<Pair<ITaintScrubberExtension, BlockPos>> extensions = new ArrayList<>();

	public TaintScrubberTileEntity() {
		super(ArcanaTiles.TAINT_SCRUBBER_TE.get());
	}

	@Override
	public void tick() {
		if (nextRefresh>=10){
			searchForExtensions();
			nextRefresh%=10;
		} else nextRefresh++;

		List<Pair<ITaintScrubberExtension, BlockPos>> toRemove = new ArrayList<>();
		for (Pair<ITaintScrubberExtension, BlockPos> extension : extensions){
			if (extension.getFirst().isValidConnection(world,extension.getSecond())){
				extension.getFirst().run(world,extension.getSecond());
			} else {extension.getFirst().sendUpdate(world,extension.getSecond()); toRemove.add(extension);};
		}
		extensions.removeAll(toRemove);
	}

	private void searchForExtensions() {
		extensions.clear();
		BlockPos.getAllInBox(getPos().north(2).east(2).up(3),getPos().south(2).west(2).down(3)).forEach(currPos -> {
			BlockState state = world.getBlockState(currPos);
			if (state.getBlock() instanceof ITaintScrubberExtension){
				ITaintScrubberExtension extension = ((ITaintScrubberExtension)state.getBlock());
				if (extension.isValidConnection(world,currPos)){
					extensions.add(Pair.of(extension,new BlockPos(currPos)));
				}
				extension.sendUpdate(world,currPos);
			}
		});
	}
}
