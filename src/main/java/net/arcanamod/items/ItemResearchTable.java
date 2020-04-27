package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.aspects.VisBattery;
import net.arcanamod.aspects.VisHandlerCapability;
import net.arcanamod.blocks.ArcanaBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.arcanamod.blocks.BlockResearchTable.EnumSide.RIGHT;
import static net.arcanamod.blocks.BlockResearchTable.PART;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemResearchTable extends ItemBase{
	
	public ItemResearchTable(){
		super("research_table_placer");
		setMaxDamage(0);
		setMaxStackSize(1);
	}
	
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt){
		VisBattery battery = new VisBattery();
		NBTTagCompound compound = stack.getTagCompound();
		if(compound != null && compound.hasKey("BlockEntityTag"))
			battery.deserializeNBT(compound.getCompoundTag("BlockEntityTag").getCompoundTag("Aspects"));
		return battery;
	}
	
	@Nullable
	public NBTTagCompound getNBTShareTag(ItemStack stack){
		NBTTagCompound tag = super.getNBTShareTag(stack);
		if(tag != null && tag.hasKey("BlockEntityTag")){
			NBTTagCompound aspectNBT = stack.getCapability(VisHandlerCapability.ASPECT_HANDLER, null).serializeNBT();
			tag.getCompoundTag("BlockEntityTag").setTag("Aspects", aspectNBT);
		}
		return tag;
	}
	
	public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt){
		super.readNBTShareTag(stack, nbt);
		if(nbt != null)
			stack.getCapability(VisHandlerCapability.ASPECT_HANDLER, null).deserializeNBT(nbt.getCompoundTag("BlockEntityTag").getCompoundTag("Aspects"));
	}
	
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
		if(world.isRemote)
			return EnumActionResult.SUCCESS;
		
		if(!world.getBlockState(pos).getBlock().isReplaceable(world, pos))
			pos = pos.up();
		
		EnumFacing direction = player.getHorizontalFacing();
		BlockPos blockpos = pos.offset(direction.rotateY());
		ItemStack itemStack = player.getHeldItem(hand);
		if(player.canPlayerEdit(pos, facing, itemStack) && player.canPlayerEdit(blockpos, facing, itemStack)){
			boolean canReplaceMain = world.getBlockState(pos).getBlock().isReplaceable(world, pos) || world.getBlockState(pos).getBlock().isAir(world.getBlockState(pos), world, pos);
			boolean canReplaceSecond = world.getBlockState(blockpos).getBlock().isReplaceable(world, blockpos) || world.getBlockState(blockpos).getBlock().isAir(world.getBlockState(blockpos), world, blockpos);
			if(canReplaceMain && canReplaceSecond){
				IBlockState state = ArcanaBlocks.RESEARCH_TABLE.getStateForPlacement(world, pos, direction, hitX, hitY, hitZ, 0, player, hand);
				world.setBlockState(pos, state, 10);
				world.setBlockState(blockpos, state.withProperty(PART, RIGHT), 10);
				
				// play sound
				SoundType soundtype = state.getBlock().getSoundType(state, world, pos, player);
				world.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				
				// update world
				// this should be updated to follow ItemBed, I don't think this will cause problems but still
				world.notifyNeighborsRespectDebug(pos, state.getBlock(), false);
				world.notifyNeighborsRespectDebug(blockpos, state.getBlock(), false);
				
				if(player instanceof EntityPlayerMP)
					CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, itemStack);
				
				ItemBlock.setTileEntityNBT(world, player, pos, itemStack);
				
				itemStack.shrink(1);
				return EnumActionResult.SUCCESS;
			}
		}
		return EnumActionResult.FAIL;
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag){
		NBTTagCompound compound = stack.getTagCompound();
		if(compound != null && compound.hasKey("BlockEntityTag")){
			NBTTagCompound data = compound.getCompoundTag("BlockEntityTag");
			ItemStackHandler items = new ItemStackHandler(3);
			items.deserializeNBT(data.getCompoundTag("Items"));
			if(!items.getStackInSlot(0).isEmpty())
				tooltip.add("Wand: " + items.getStackInSlot(0).getDisplayName());
			if(!items.getStackInSlot(1).isEmpty())
				tooltip.add("Ink: " + items.getStackInSlot(1).getDisplayName());
			if(!items.getStackInSlot(2).isEmpty())
				tooltip.add("Note: " + items.getStackInSlot(2).getDisplayName());
			if(!data.getCompoundTag("Aspects").hasNoTags())
				tooltip.add("Contains vis");
		}
	}
}