package net.arcanamod.mixin;

import net.arcanamod.ClientProxy;
import net.arcanamod.client.gui.SwapFocusScreen;
import net.arcanamod.items.MagicDeviceItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Hand;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public abstract class MinecraftClientMixin{
	
	@Shadow
	public abstract void displayGuiScreen(@Nullable Screen guiScreen);
	
	@Shadow
	@Nullable
	public ClientPlayerEntity player;
	
	@Shadow
	@Final
	private static Logger LOGGER;
	
	@Inject(method = "processKeyBinds",
	        at = @At("HEAD"))
	private void processKeyBinds(CallbackInfo ci){
		try{
			while(ClientProxy.SWAP_FOCUS_BINDING.isPressed())
				for(Hand hand : Hand.values())
					if(player.getHeldItem(hand).getItem() instanceof MagicDeviceItem){
						if(((MagicDeviceItem)player.getHeldItem(hand).getItem()).canUseSpells() && ((MagicDeviceItem) player.getHeldItem(hand).getItem()).canSwapFocus(player)){
							displayGuiScreen(new SwapFocusScreen(hand));
							break;
						}
					}
		}catch(Exception exception){
			LOGGER.error("OBJECT TYPE ERROR!!!");
		}
	}
}