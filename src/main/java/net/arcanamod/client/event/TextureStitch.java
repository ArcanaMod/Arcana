package net.arcanamod.client.event;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.VisBattery;
import net.arcanamod.aspects.VisHandlerCapability;
import net.arcanamod.client.render.JarTileEntityRender;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.items.attachment.Focus;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TextureStitch{
	
	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event){
		if(event.getMap().getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)){
			event.addSprite(JarTileEntityRender.JAR_CONTENT_SIDE);
			event.addSprite(JarTileEntityRender.JAR_CONTENT_TOP);
			
			// add all of the wand related textures
			for(Cap cap : Cap.CAPS.values())
				event.addSprite(cap.getTextureLocation());
			for(Core core : Core.CORES.values())
				event.addSprite(core.getTextureLocation());
			for(Focus focus : Focus.FOCI)
				event.addSprite(focus.getModelLocation());
		}
	}

	//TODO: Move this to another place.
	@SubscribeEvent
	public static void onPlayerInteractEvent(PlayerInteractEvent event)
	{
		//if (event.getWorld().isRemote) return;
		if (Arcana.debug)
		if (event.getItemStack().getItem() == ArcanaItems.VIS_MANIPULATION_TOOLS.get()&&event.getWorld().getTileEntity(event.getPos())!=null)
			if (event.getWorld().getTileEntity(event.getPos()).getCapability(VisHandlerCapability.ASPECT_HANDLER).orElse(null)!=null)
				event.getPlayer().sendMessage(new StringTextComponent(((VisBattery)event.getWorld().getTileEntity(event.getPos()).getCapability(VisHandlerCapability.ASPECT_HANDLER).orElse(null)).toString()));
	}
}