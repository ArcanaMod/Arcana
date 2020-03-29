package net.kineticdevelopment.arcana;

import net.kineticdevelopment.arcana.client.gui.ResearchTableGUI;
import net.kineticdevelopment.arcana.common.objects.containers.ResearchTableContainer;
import net.kineticdevelopment.arcana.common.objects.tile.ResearchTableTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

/**
 * Handles opening containers.
 */
public class ArcanaGuiHandler implements IGuiHandler{
	
	public static final int RESEARCH_TABLE_ID = 0;
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Nullable
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		// return container
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		if(ID == RESEARCH_TABLE_ID) // as more IDs are added: switch on them
			if(te instanceof ResearchTableTileEntity)
				return new ResearchTableContainer(player.inventory, (ResearchTableTileEntity)te);
			else
				LOGGER.error("Tried to open research table GUI at location without a research table tile entity! (server)");
		return null;
	}
	
	@Nullable
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		// return gui
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		if(ID == RESEARCH_TABLE_ID) // as more IDs are added: switch on them
			if(te instanceof ResearchTableTileEntity)
				return new ResearchTableGUI((ResearchTableTileEntity)te, new ResearchTableContainer(player.inventory, (ResearchTableTileEntity)te));
			else
				LOGGER.error("Tried to open research table GUI at location without a research table tile entity! (client)");
		return null;
	}
}