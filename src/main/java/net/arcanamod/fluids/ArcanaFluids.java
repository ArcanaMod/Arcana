package net.arcanamod.fluids;

import net.arcanamod.Arcana;
import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.ArcanaVariables.arcLoc;
import static net.arcanamod.blocks.ArcanaBlocks.BLOCKS;
import static net.arcanamod.items.ArcanaItems.ITEMS;

public class ArcanaFluids {
	public static void init(IEventBus bus) {
		ArcanaFluids.FLUIDS.register(bus);
	}

	public static final ResourceLocation FLUID_STILL = arcLoc("fluid/tainted_goo");
	public static final ResourceLocation FLUID_FLOWING = arcLoc("fluid/tainted_goo_flowing");
	
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, Arcana.MODID);

	public static RegistryObject<FlowingFluid> TAINT_FLUID = FLUIDS.register("tainted_goo", () ->
			new ForgeFlowingFluid.Source(ArcanaFluids.taint_fluid_properties)
	);
	public static RegistryObject<FlowingFluid> TAINT_FLUID_FLOWING = FLUIDS.register("tainted_goo_flowing", () ->
			new ForgeFlowingFluid.Flowing(ArcanaFluids.taint_fluid_properties)
	);

	public static RegistryObject<FlowingFluidBlock> TAINT_FLUID_BLOCK = BLOCKS.register("tainted_goo", () ->
			new TaintFluid(TAINT_FLUID, Block.Properties.create(Material.WATER).doesNotBlockMovement().hardnessAndResistance(100.0F).noDrops())
	);
	public static RegistryObject<Item> TAINT_FLUID_BUCKET = ITEMS.register("tainted_goo_bucket", () ->
			new BucketItem(TAINT_FLUID, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(Arcana.TAINT))
	);

	public static final ForgeFlowingFluid.Properties taint_fluid_properties =
			new ForgeFlowingFluid.Properties(TAINT_FLUID, TAINT_FLUID_FLOWING, FluidAttributes.builder(FLUID_STILL, FLUID_FLOWING))
					.bucket(TAINT_FLUID_BUCKET).block(TAINT_FLUID_BLOCK);
}