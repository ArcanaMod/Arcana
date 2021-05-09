package net.arcanamod.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.function.Function;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DelegatingBlock extends Block{
	protected final Block parentBlock;
	private static final Method fillStateContainer = ObfuscationReflectionHelper.findMethod(Block.class, "func_206840_a", StateContainer.Builder.class);
	private static final Field blockColorsField = ObfuscationReflectionHelper.findField(AbstractBlock.Properties.class, "field_235800_b_");
	
	public DelegatingBlock(Block blockIn, @Nullable SoundType sound){
		super(propertiesWithSound(Properties.from(blockIn), sound));
		this.parentBlock = blockIn;
		
		// Refill the state container - Block does this too early
		StateContainer.Builder<Block, BlockState> builder = new StateContainer.Builder<>(this);
		fillStateContainer(builder);
		stateContainer = builder.createStateContainer(Block::getDefaultState, BlockState::new);
		setDefaultState(stateContainer.getBaseState());
	}
	
	public DelegatingBlock(Block blockIn){
		this(blockIn, null);
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
		// parentBlock.fillStateContainer(builder);
		// protected access
		// can't AT it to public because its overriden with "less" (i.e. protected) visibility.
		if(parentBlock != null)
			try{
				// (Lnet/minecraft/state/StateContainer$Builder;)V
				fillStateContainer.setAccessible(true);
				fillStateContainer.invoke(parentBlock, builder);
				fillStateContainer.setAccessible(false);
			}catch(IllegalAccessException | InvocationTargetException e){
				e.printStackTrace();
				System.err.println("Unable to delegate blockstate!");
			}
	}
	
	public FluidState getFluidState(BlockState state){
		return parentBlock.getFluidState(state);
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static BlockState switchBlock(BlockState state, Block block){
		BlockState base = block.stateContainer.getBaseState();
		// A helper method doesn't work here...
		for(Property property : state.getProperties())
			if(base.hasProperty(property))
				base = base.with(property, state.get(property));
		return base;
	}
	
	public BlockState getStateForPlacement(BlockItemUseContext context){
		BlockState placement = parentBlock.getStateForPlacement(context);
		return placement != null
				? switchBlock(placement, this)
				: null;
	}
	
	public BlockState rotate(BlockState state, IWorld world, BlockPos pos, Rotation direction){
		return switchBlock(parentBlock.rotate(state, world, pos, direction), this);
	}
	
	public BlockState rotate(BlockState state, Rotation rot){
		return switchBlock(parentBlock.rotate(state, rot), this);
	}
	
	public BlockState mirror(BlockState state, Mirror mirror){
		return switchBlock(parentBlock.mirror(state, mirror), this);
	}
	
	public BlockState getStateAtViewpoint(BlockState state, IBlockReader world, BlockPos pos, Vector3d viewpoint){
		return switchBlock(parentBlock.getStateAtViewpoint(state, world, pos, viewpoint), this);
	}
	
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random){
		parentBlock.randomTick(state, world, pos, random);
	}
	
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand){
		parentBlock.tick(state, world, pos, rand);
	}
	
	public boolean isTransparent(BlockState state){
		return parentBlock != null && parentBlock.isTransparent(state);
	}
	
	public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face){
		return parentBlock.getFireSpreadSpeed(state, world, pos, face);
	}
	
	public float getExplosionResistance(){
		return parentBlock.getExplosionResistance();
	}
	
	public void onBlockExploded(BlockState state, World world, BlockPos pos, Explosion explosion){
		parentBlock.onBlockExploded(state, world, pos, explosion);
	}
	
	public float getExplosionResistance(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion){
		return parentBlock.getExplosionResistance(state, world, pos, explosion);
	}
	
	public boolean addRunningEffects(BlockState state, World world, BlockPos pos, Entity entity){
		return parentBlock.addRunningEffects(state, world, pos, entity);
	}
	
	public boolean addDestroyEffects(BlockState state, World world, BlockPos pos, ParticleManager manager){
		return parentBlock.addDestroyEffects(state, world, pos, manager);
	}
	
	public boolean addHitEffects(BlockState state, World worldObj, RayTraceResult target, ParticleManager manager){
		return parentBlock.addHitEffects(state, worldObj, target, manager);
	}
	
	public boolean addLandingEffects(BlockState state1, ServerWorld worldserver, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles){
		return parentBlock.addLandingEffects(state1, worldserver, pos, state2, entity, numberOfParticles);
	}
	
	public float getSlipperiness(){
		return parentBlock.getSlipperiness();
	}
	
	public boolean isToolEffective(BlockState state, ToolType tool){
		return parentBlock.isToolEffective(state, tool);
	}
	
	public boolean canHarvestBlock(BlockState state, IBlockReader world, BlockPos pos, PlayerEntity player){
		return parentBlock.canHarvestBlock(state, world, pos, player);
	}
	
	@Override
	public boolean ticksRandomly(BlockState state){
		return parentBlock.ticksRandomly(state);
	}
	
	@Override
	public void animateTick(BlockState state, World world, BlockPos position, Random rand){
		parentBlock.animateTick(state, world, position, rand);
	}
	
	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader world, BlockPos pos){
		return parentBlock.propagatesSkylightDown(state, world, pos);
	}
	
	/*@Override
	public int tickRate(IWorldReader world){
		return parentBlock.tickRate(world);
	}*/
	
	@Override
	public void dropXpOnBlockBreak(ServerWorld world, BlockPos pos, int amount){
		parentBlock.dropXpOnBlockBreak(world, pos, amount);
	}
	
	@Override
	public void onExplosionDestroy(World world, BlockPos pos, Explosion explosion){
		parentBlock.onExplosionDestroy(world, pos, explosion);
	}
	
	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity){
		parentBlock.onEntityWalk(world, pos, entity);
	}
	
	@Override
	public boolean canSpawnInBlock(){
		return parentBlock.canSpawnInBlock();
	}
	
	@Override
	public void onFallenUpon(World world, BlockPos pos, Entity entity, float num){
		parentBlock.onFallenUpon(world, pos, entity, num);
	}
	
	@Override
	public void onLanded(IBlockReader world, Entity entity){
		parentBlock.onLanded(world, entity);
	}
	
	@Override
	public float getSpeedFactor(){
		return parentBlock.getSpeedFactor();
	}
	
	@Override
	public float getJumpFactor(){
		return parentBlock.getJumpFactor();
	}
	
	/*@Override
	public void onProjectileCollision(World world, BlockState state, BlockRayTraceResult trace, Entity entity){
		parentBlock.onProjectileCollision(world, state, trace, entity);
	}*/
	
	@Override
	public void fillWithRain(World world, BlockPos pos){
		parentBlock.fillWithRain(world, pos);
	}
	
	@Override
	public OffsetType getOffsetType(){
		return parentBlock.getOffsetType();
	}
	
	@Override
	public boolean isVariableOpacity(){
		return parentBlock.isVariableOpacity();
	}
	
	@Override
	public float getSlipperiness(BlockState state, IWorldReader world, BlockPos pos, @Nullable Entity entity){
		return parentBlock.getSlipperiness(state, world, pos, entity);
	}
	
	@Nullable
	@Override
	public ToolType getHarvestTool(BlockState state){
		return parentBlock.getHarvestTool(state);
	}
	
	@Override
	public int getHarvestLevel(BlockState state){
		return parentBlock.getHarvestLevel(state);
	}
	
	@Override
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable){
		return parentBlock.canSustainPlant(state, world, pos, facing, plantable);
	}
	
	@Override
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos){
		return parentBlock.getRenderShape(state, worldIn, pos);
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return parentBlock.getCollisionShape(state, worldIn, pos, context);
	}
	
	@Override
	public VoxelShape getRaytraceShape(BlockState state, IBlockReader worldIn, BlockPos pos){
		return parentBlock.getRaytraceShape(state, worldIn, pos);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context){
		return parentBlock.getShape(state, worldIn, pos, context);
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos){
		return parentBlock.isValidPosition(state, worldIn, pos);
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos){
		return parentBlock.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext){
		return parentBlock.isReplaceable(state, useContext);
	}
	
	@Override
	public boolean isReplaceable(BlockState state, Fluid fluid){
		return parentBlock.isReplaceable(state, fluid);
	}
	
	public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player){
		parentBlock.onBlockClicked(state, world, pos, player);
	}
	
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult raytrace){
		return parentBlock.onBlockActivated(state, world, pos, player, hand, raytrace);
	}
	
	public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch){
		return parentBlock.getExpDrop(state, world, pos, fortune, silktouch);
	}
	
	public int getLightValue(BlockState state, IBlockReader world, BlockPos pos){
		return parentBlock.getLightValue(state, world, pos);
	}
	
	private static Properties propertiesWithSound(Properties properties, @Nullable SoundType soundType){
		// FIXME: state-properties are added too late, so we have to clear these two block-properties to avoid a crash
		properties.setLightLevel(__ -> 0);
		blockColorsField.setAccessible(true);
		try{
			blockColorsField.set(properties, (Function<BlockState, MaterialColor>)__ -> MaterialColor.PURPLE);
		}catch(IllegalAccessException e){
			e.printStackTrace();
			System.err.println("[Arcana, hackfix] Unable to clear block properties!");
		}
		if(soundType == null)
			return properties;
		else
			return properties.sound(soundType);
	}
	
	public IFormattableTextComponent getTranslatedName(){
		return parentBlock.getTranslatedName();
	}
}