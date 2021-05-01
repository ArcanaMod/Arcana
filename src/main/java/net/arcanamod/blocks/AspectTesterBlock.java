package net.arcanamod.blocks;

import net.arcanamod.blocks.tiles.AspectTesterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.*;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public class AspectTesterBlock extends Block {
	public AspectTesterBlock(Block.Properties properties) {
		super(properties);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new StringTextComponent("DEV: Only for testing new AspectHandler.").setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.GRAY))));
		tooltip.add(new StringTextComponent("Can crash the game!").setStyle(Style.EMPTY.setColor(Color.fromInt(0xFF0000))));
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new AspectTesterTileEntity();
	}
}