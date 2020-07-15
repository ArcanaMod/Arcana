package net.arcanamod;

import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.blocks.tiles.AspectWindowTileEntity;
import net.arcanamod.client.event.FogColorHandler;
import net.arcanamod.client.event.InitScreenHandler;
import net.arcanamod.client.event.RenderTooltip;
import net.arcanamod.client.event.TextureStitch;
import net.arcanamod.client.gui.ResearchBookScreen;
import net.arcanamod.client.gui.ResearchEntryScreen;
import net.arcanamod.client.gui.ScribbledNoteScreen;
import net.arcanamod.client.model.WandModelLoader;
import net.arcanamod.client.render.ArcanaParticles;
import net.arcanamod.client.research.EntrySectionRenderer;
import net.arcanamod.client.research.PuzzleRenderer;
import net.arcanamod.client.research.RequirementRenderer;
import net.arcanamod.event.ResearchEvent;
import net.arcanamod.research.ResearchBooks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static net.arcanamod.Arcana.MODID;

/**
 * Client Proxy
 *
 * @author Atlas, Luna
 */
public class ClientProxy extends CommonProxy{

	public void construct(){
		super.construct();
		FMLJavaModLoadingContext.get().getModEventBus().addListener(TextureStitch::onTextureStitch);
		
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(RenderTooltip::onRenderTooltipColor);
		modEventBus.addListener(RenderTooltip::makeTooltip);
		modEventBus.addListener(FogColorHandler::setFog);
		modEventBus.addListener(InitScreenHandler::onInitGuiEvent);

		MinecraftForge.EVENT_BUS.register(InitScreenHandler.class);
		
		ArcanaParticles.PARTICLE_TYPES.register(modEventBus);
	}
	
	@Override
	public void preInit(FMLCommonSetupEvent event){
		super.preInit(event);
		EntrySectionRenderer.init();
		RequirementRenderer.init();
		PuzzleRenderer.init();
		ModelLoaderRegistry.registerLoader(new ResourceLocation(MODID, "wand_loader"), new WandModelLoader());
		
		Minecraft.getInstance().getBlockColors().register((state, access, pos, index) -> {
			if(access == null || pos == null || access.getTileEntity(pos) == null)
				return 0xFF1F0D0B;
			return ((AspectWindowTileEntity)access.getTileEntity(pos)).getColor();
		}, ArcanaBlocks.ASPECT_WINDOW.get());
		
		Minecraft.getInstance().getBlockColors().register((state, access, pos, index) ->
				access != null && pos != null ? BiomeColors.getWaterColor(access, pos) : -1,
				ArcanaBlocks.CRUCIBLE.get()
		);
	}
	
	public void openResearchBookUI(ResourceLocation book){
		Minecraft.getInstance().displayGuiScreen(new ResearchBookScreen(ResearchBooks.books.get(book)));
	}

	@Override
	public void openScribbledNotesUI(){
		Minecraft.getInstance().displayGuiScreen(new ScribbledNoteScreen(new StringTextComponent("")));
	}
	
	public void onResearchChange(ResearchEvent event){
		if(Minecraft.getInstance().currentScreen instanceof ResearchEntryScreen)
			((ResearchEntryScreen)Minecraft.getInstance().currentScreen).updateButtonVisibility();
	}
	
	public PlayerEntity getPlayerOnClient(){
		return Minecraft.getInstance().player;
	}
	
	public World getWorldOnClient(){
		return Minecraft.getInstance().world;
	}
	
	public void scheduleOnClient(Runnable runnable){
		Minecraft.getInstance().deferTask(runnable);
	}
	
	public ItemStack getAspectItemStackForDisplay(){
		if(Minecraft.getInstance().player == null)
			return super.getAspectItemStackForDisplay();
		else
			return AspectUtils.aspectStacks.get((Minecraft.getInstance().player.ticksExisted / 20) % AspectUtils.aspectStacks.size());
	}
}