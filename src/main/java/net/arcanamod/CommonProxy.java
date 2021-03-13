package net.arcanamod;

import net.arcanamod.aspects.AspectUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;


/**
 * Common Proxy
 *
 * @author Atlas
 */
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy{
	
	public void construct(){
	}
	
	public void preInit(FMLCommonSetupEvent event){
	}

	public PlayerEntity getPlayerOnClient(){
		return null;
	}
	
	public World getWorldOnClient(){
		return null;
	}
	
	public void scheduleOnClient(Runnable runnable){
	}
	
	public ItemStack getAspectItemStackForDisplay(){
		return AspectUtils.aspectStacks.get(0);
	}
}