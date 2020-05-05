package net.arcanamod.blocks.bases.tainted;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.bases.LeavesBase;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Random;

/**
 * Tainted Leaves, all tainted leaves should either be this, or extend it
 *
 * @author Mozaran
 * @see LeavesBase
 */
public class TaintedLeavesBase extends LeavesBase{
	String name;
	
	public TaintedLeavesBase(String name){
		super(name);
		setTickRandomly(true);
		this.name = name;
	}
	
	//    @Override
	//    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
	//
	//        int h = 0;
	//
	//        int f = (int) TaintLevelHandler.getTaintLevel(worldIn);
	//
	//        if (f >= 5 && f <= 9) {
	//            h = ThreadLocalRandom.current().nextInt(0, 10);
	//        } else if (f >= 10 && f <= 19) {
	//            h = ThreadLocalRandom.current().nextInt(0, 9);
	//        } else if (f >= 20 && f <= 29) {
	//            h = ThreadLocalRandom.current().nextInt(0, 8);
	//        } else if (f >= 30 && f <= 39) {
	//            h = ThreadLocalRandom.current().nextInt(0, 7);
	//        } else if (f >= 40 && f <= 49) {
	//            h = ThreadLocalRandom.current().nextInt(0, 6);
	//        } else if (f >= 50 && f <= 59) {
	//            h = ThreadLocalRandom.current().nextInt(0, 5);
	//        } else if (f >= 60 && f <= 69) {
	//            h = ThreadLocalRandom.current().nextInt(0, 4);
	//        } else if (f >= 70 && f <= 79) {
	//            h = ThreadLocalRandom.current().nextInt(0, 3);
	//        } else if (f >= 80 && f <= 89) {
	//            h = ThreadLocalRandom.current().nextInt(0, 2);
	//        } else if (f >= 90 && f <= 99) {
	//            h = ThreadLocalRandom.current().nextInt(0, 1);
	//        } else if (f >= 100) {
	//            h = 1;
	//        }
	////        System.out.println("Spread? "+h);
	//        if (h == 1) {
	//            TaintHandler.spreadTaint(worldIn, pos);
	////            System.out.println("Spreading Taint...");
	//        }
	//
	//        boolean surrounded = true;
	//
	//        for (int x = -1; x < 2; x++) {
	//
	//            for (int y = -1; y < 2; y++) {
	//
	//                for (int z = -1; z < 2; z++) {
	//
	//                    BlockPos nPos = pos.add(x, y, z);
	//
	//                    Block b = worldIn.getBlockState(nPos).getBlock();
	//
	//
	//                    if(!(b instanceof TaintedBlockBase) && !(b instanceof BlockAir)) {
	//                        surrounded = false;
	//                    }
	//                }
	//            }
	//        }
	//
	//        if(surrounded == true) {
	//            worldIn.setBlockState(pos, state.withProperty(BlockStateInit.FULLYTAINTED, true));
	//            this.needsRandomTick = !surrounded;
	////            System.out.println(pos + ": Ticking? "+this.needsRandomTick);
	//        }
	//    }
	
	@Override
	public Item getItemDropped(BlockState state, Random rand, int fortune){
		String untaintedName = "un" + name;
		Block block = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(Arcana.MODID, untaintedName.replace("leaves", "sapling")));
		if(block == null)
			return Item.getItemFromBlock(this);
		return Item.getItemFromBlock(block);
	}
	
	@Override
	public void getSubBlocks(ItemGroup itemIn, NonNullList<ItemStack> items){
		Block block = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(Arcana.MODID, "un" + name));
		items.add(new ItemStack(block));
	}
	
	@Override
	protected ItemStack getSilkTouchDrop(BlockState state){
		Block block = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(Arcana.MODID, "un" + name));
		return new ItemStack(Item.getItemFromBlock(block));
	}
	
	public NonNullList<ItemStack> onSheared(ItemStack item, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune){
		Block block = GameRegistry.findRegistry(Block.class).getValue(new ResourceLocation(Arcana.MODID, "un" + name));
		return NonNullList.withSize(1, new ItemStack(block));
	}
}
