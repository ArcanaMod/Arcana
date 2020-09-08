package net.arcanamod.blocks;

import net.arcanamod.blocks.tiles.AspectCrystallizerTileEntity;
import net.arcanamod.client.gui.FociForgeScreen;
import net.arcanamod.client.gui.ResearchBookScreen;
import net.arcanamod.research.ResearchBooks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class FociForgeBlock extends Block {
	public FociForgeBlock(Block.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult){
		Minecraft.getInstance().displayGuiScreen(new FociForgeScreen());
		return super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
	}
}
