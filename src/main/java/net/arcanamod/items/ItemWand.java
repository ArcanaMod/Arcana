package net.arcanamod.items;

import mcp.MethodsReturnNonnullByDefault;
import net.arcanamod.wand.EnumAttachmentType;
import net.minecraft.item.Item;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

/**
 * Wand Item
 *
 * @author Merijn
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemWand extends Item{
	
	public static List<ItemWand> WANDS = new ArrayList<>();
	protected int level = 2;
	
	public ItemWand(Properties properties){
		super(properties);
		WANDS.add(this);
	}
	
	public int getLevel(){
		return level;
	}
	
	public ItemWand setLevel(int level){
		this.level = level;
		return this;
	}
	
	/*
	
	
	protected ItemAttachment[][] attachments;
	
	// remove at some point
	public static final Supplier<ItemAttachment[][]> supplierAttachments = () -> {
		List<Cap> allowed = new ArrayList<>(Cap.CAPS);
		// fix models
		allowed.sort(Comparator.comparingInt(Cap::getID));
		List<Focus> allowedFoci = ImmutableList.of(Focus.NONE, Focus.DEFAULT); //TODO: change with foci
		return new ItemAttachment[][]{allowed.toArray(new Cap[0]), allowedFoci.toArray(new Focus[0])};
	};
	
	
	
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt){
		return TypedVisBattery.primalBattery(WandUtil.getCore(stack).getMaxVis());
	}
	
	@Nullable
	public CompoundNBT getNBTShareTag(ItemStack stack){
		CompoundNBT tag = super.getNBTShareTag(stack);
		if(tag != null){
			CompoundNBT aspectNBT = stack.getCapability(VisHandlerCapability.ASPECT_HANDLER, null).serializeNBT();
			tag.setTag("aspect_handler", aspectNBT);
		}
		return tag;
	}
	
	public void readNBTShareTag(ItemStack stack, @Nullable CompoundNBT nbt){
		super.readNBTShareTag(stack, nbt);
		if(nbt != null)
			stack.getCapability(VisHandlerCapability.ASPECT_HANDLER, null).deserializeNBT(nbt.getCompoundTag("aspect_handler"));
	}
	
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged){
		return slotChanged;
	}
	
	*//**
	 * Casts a spell if a focus is assigned
	 *
	 * @param world
	 * 		World the action is performed in
	 * @param player
	 * 		Player that performs the action
	 * @param hand
	 * 		Hand the wand is in
	 *//*
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand){
		if(world.isRemote){
			return ActionResult.newResult(ActionResultType.PASS, player.getHeldItem(hand));
		}
		if(hand == Hand.MAIN_HAND){
			ItemStack item = player.getHeldItem(hand);
			if(item.getTagCompound() != null){
				if(!item.getTagCompound().getCompoundTag("foci").hasNoTags()){
					Spell spell = Spell.fromNBT(item.getTagCompound().getCompoundTag("foci"));
					spell.cast(player);
					world.playSound(null, player.getPosition(), Sounds.SPELL_CAST, SoundCategory.PLAYERS, 0.5f, 0f);
				}
			}
		}
		return new ActionResult<>(ActionResultType.PASS, player.getHeldItem(hand));
	}
	
	*//**
	 * Called when a Block is right-clicked with this Item. The wand uses this to transform blocks when applicable.
	 *
	 * @param player
	 * 		The player using the wand.
	 * @param world
	 * 		The world the wand is used in.
	 * @param pos
	 * 		The position of the block clicked.
	 * @param hand
	 * 		The hand the wand is in.
	 * @param facing
	 * 		Which side of the block is being clicked (?).
	 * @param hitX
	 * 		Where on the block on the X axis the player has clicked.
	 * @param hitY
	 * 		Where on the block on the Y axis the player has clicked.
	 * @param hitZ
	 * 		Where on the block on the Z axis the player has clicked.
	 * @return Whether the wand did anything.
	 *//*
	public ActionResultType onItemUse(PlayerEntity player, World world, BlockPos pos, Hand hand, Direction facing, float hitX, float hitY, float hitZ){
		Block block = world.getBlockState(pos).getBlock();
		// hardcoded for now, but this could be made into recipes later (probably 1.13+ when recipes can work with non-crafting tables)
		if(block == Blocks.BOOKSHELF){
			// destroy the block
			world.setBlockToAir(pos);
			// create an Arcanum item
			if(!world.isRemote){
				ItemEntity entity = new ItemEntity(world, pos.getX() + .5, pos.getY(), pos.getZ() + .5, new ItemStack(ArcanaItems.ARCANUM));
				entity.setDefaultPickupDelay();
				world.spawnEntity(entity);
			}
			// display particles ane effects (to do)
			return ActionResultType.SUCCESS;
		}else if(block == Blocks.CAULDRON){
			BlockState crucible = ArcanaBlocks.CRUCIBLE.getDefaultState();
			world.setBlockState(pos, crucible);
			// display particles ane effects (to do)
			return ActionResultType.SUCCESS;
		}
		return super.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
	}
	
	*//**
	 * Getter of the attached attachments.
	 *
	 * @return an array of the attached attachments
	 *//*
	public ItemAttachment[][] getAttachments(){
		if(this.attachments == null)
			this.attachments = supplierAttachments.get();
		
		return this.attachments;
	}
	
	*//**
	 * Gets an attachment based on {@link EnumAttachmentType} and the ID of the attachment
	 *
	 * @param type
	 * 		Attachment type
	 * @param id
	 * 		ID of the attachment
	 * @return {@link ItemAttachment} of the given type and id
	 *//*
	@Nullable
	public ItemAttachment getAttachment(EnumAttachmentType type, int id){
		ItemAttachment attachment = null;
		
		for(ItemAttachment a : this.getAttachments()[type.getSlot()]){
			if(a.getID() == id){
				attachment = a;
			}
		}
		return attachment;
	}
	
	*//**
	 * Gets amount of the allowed types
	 *
	 * @param type
	 * 		Attachement type {@link EnumAttachmentType}
	 * @return Amount of the given types
	 *//*
	public int getAmountForSlot(EnumAttachmentType type){
		return this.getAttachments()[type.getSlot()].length;
	}
	
	*//**
	 * Gets a attachment based on the NBT of the given ItemStack and the given type
	 *
	 * @param itemStack
	 * 		Itemstack to get the NBT from
	 * @param type
	 * 		Type of the requested attachment
	 * @return {@link ItemAttachment} of the given type and ItemStack NBT
	 *//*
	@Nullable
	public ItemAttachment getAttachment(ItemStack itemStack, EnumAttachmentType type){
		return this.getAttachment(type, Arcana.getNBT(itemStack).getInteger(type.getKey()));
	}
	
	public boolean capAllowed(Cap cap){
		return cap.getLevel() <= level;
	}
	
	public String getItemStackDisplayName(ItemStack stack){
		String s = getUnlocalizedName(stack) + ".name";
		// Using server-side I18n -- replace when updating.
		ItemAttachment attachment = getAttachment(stack, EnumAttachmentType.CAP);
		return I18n.translateToLocalFormatted(s, I18n.translateToLocal(attachment != null ? attachment.getUnlocalizedName() + ".prefix" : "invalid_cap.prefix"));
	}*/
}