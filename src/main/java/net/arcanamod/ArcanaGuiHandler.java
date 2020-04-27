package net.arcanamod;

import net.arcanamod.client.gui.ResearchTableGUI;
import net.arcanamod.client.gui.VisManipulatorsGUI;
import net.arcanamod.containers.ResearchTableContainer;
import net.arcanamod.containers.VisManipulatorsContainer;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
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
	public static final int VIS_MANIPULATORS_ID = 1;
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Nullable
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		// return container
		switch(ID){
			case RESEARCH_TABLE_ID:
				BlockPos pos = new BlockPos(x, y, z);
				TileEntity te = world.getTileEntity(pos);
				if(te instanceof ResearchTableTileEntity)
					return new ResearchTableContainer(player.inventory, (ResearchTableTileEntity)te);
				else
					LOGGER.error("Tried to open research table GUI at location without a research table tiles entity! (server)");
				break;
			case VIS_MANIPULATORS_ID:
				return new VisManipulatorsContainer(player.inventory);
		}
		return null;
	}
	
	@Nullable
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
		// return gui
		switch(ID){
			case RESEARCH_TABLE_ID:  // as more IDs are added: switch on them
				BlockPos pos = new BlockPos(x, y, z);
				TileEntity te = world.getTileEntity(pos);
				if(te instanceof ResearchTableTileEntity)
					return new ResearchTableGUI((ResearchTableTileEntity)te, new ResearchTableContainer(player.inventory, (ResearchTableTileEntity)te));
				else
					LOGGER.error("Tried to open research table GUI at location without a research table tiles entity! (client)");
				break;
			case VIS_MANIPULATORS_ID:
				return new VisManipulatorsGUI(new VisManipulatorsContainer(player.inventory));
		}
		return null;
	}
}