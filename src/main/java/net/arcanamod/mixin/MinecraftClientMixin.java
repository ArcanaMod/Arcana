package net.arcanamod.mixin;

import net.arcanamod.ClientProxy;
import net.arcanamod.client.gui.SwapFocusScreen;
import net.arcanamod.items.WandItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Hand;
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
	
	@Inject(method = "processKeyBinds",
	        at = @At("HEAD"))
	private void processKeyBinds(CallbackInfo ci){
		while(ClientProxy.SWAP_FOCUS_BINDING.isPressed())
			for(Hand hand : Hand.values())
				if(player.getHeldItem(hand).getItem() instanceof WandItem){
					displayGuiScreen(new SwapFocusScreen(hand));
					break;
				}
	}
}