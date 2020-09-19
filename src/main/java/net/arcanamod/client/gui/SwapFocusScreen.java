package net.arcanamod.client.gui;

import net.arcanamod.ClientProxy;
import net.arcanamod.items.WandItem;
import net.arcanamod.items.attachment.FocusItem;
import net.arcanamod.network.Connection;
import net.arcanamod.network.PkSwapFocus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SwapFocusScreen extends Screen{
	
	private Hand hand;
	private static final Random random = new Random();
	
	public SwapFocusScreen(Hand hand){
		super(new StringTextComponent(""));
		this.hand = hand;
	}
	
	public void render(int mouseX, int mouseY, float partialTicks){
		super.render(mouseX, mouseY, partialTicks);
		ItemStack wand = getMinecraft().player.getHeldItem(hand);
		if(wand.getItem() instanceof WandItem && ((WandItem)wand.getItem()).canSwapFocus()){
			//display current focus
			WandItem.getFocusStack(wand).ifPresent(stack -> getMinecraft().getItemRenderer().renderItemIntoGUI(stack, width / 2 - 8, height / 2 - 8));
			//display all foci in the inventory
			List<ItemStack> foci = getAllFociStacks();
			int size = foci.size();
			int distance = size * 5 + 28;
			int particleCount = size * 6 + 8;
			for(int i = 0; i < particleCount; i++){
				random.setSeed(i);
				double v = Math.toRadians((i + (getMinecraft().world.getGameTime() + partialTicks) / 5f) * (360f / (particleCount)));
				int colour = UiUtil.combine(random.nextInt(128) + 127, random.nextInt(128) + 127, random.nextInt(128) + 127) | 0x9F000000;
				int x = (int)(MathHelper.cos((float)v) * (distance + 4)) - 4 + width / 2;
				int y = (int)(MathHelper.sin((float)v) * (distance + 4)) - 4 + height / 2;
				GuiUtils.drawGradientRect(0, x, y, x + 8, y + 8, colour, colour);
			}
			for(int i = 0; i < size; i++){
				ItemStack focus = foci.get(i);
				int x = (int)(MathHelper.cos((float)Math.toRadians(i * (360f / size))) * distance) - 8 + width / 2;
				int y = (int)(MathHelper.sin((float)Math.toRadians(i * (360f / size))) * distance) - 8 + height / 2;
				getMinecraft().getItemRenderer().renderItemIntoGUI(focus, x, y);
			}
		}
	}
	
	public boolean mouseClicked(double mouseX, double mouseY, int button){
		// find the clicked focus
		// if you click the middle, you remove your focus
		if(mouseX >= -8 + (width / 2f) && mouseX < 8 + (width / 2f) && mouseY >= -8 + (height / 2f) && mouseY < 8 + (height / 2f)){
			// send swap focus packet
			// hand & focus index
			Connection.sendToServer(new PkSwapFocus(hand, -1));
			return true;
		}
		List<ItemStack> foci = getAllFociStacks();
		int size = foci.size();
		int distance = size * 5 + 28;
		for(int i = 0; i < size; i++){
			int x = (int)(MathHelper.cos((float)Math.toRadians(i * (360f / size))) * distance) - 8 + width / 2;
			int y = (int)(MathHelper.sin((float)Math.toRadians(i * (360f / size))) * distance) - 8 + height / 2;
			if(mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16){
				// send swap focus packet
				// hand & focus index
				Connection.sendToServer(new PkSwapFocus(hand, i));
				return true;
			}
		}
		return false;
	}
	
	private List<ItemStack> getAllFociStacks(){
		//TODO: focus pouch?
		List<ItemStack> foci = new ArrayList<>();
		PlayerInventory inventory = getMinecraft().player.inventory;
		for(int i = 0; i < inventory.getSizeInventory(); i++){
			ItemStack stack = inventory.getStackInSlot(i);
			if(stack.getItem() instanceof FocusItem)
				foci.add(stack);
		}
		return foci;
	}
	
	public boolean keyReleased(int keyCode, int scanCode, int modifiers){
		// if SWAP_FOCUS_BINDING is released, close screen
		if(!ClientProxy.SWAP_FOCUS_BINDING.isPressed()){
			Minecraft.getInstance().displayGuiScreen(null);
			return true;
		}
		return false;
	}
	
	public boolean isPauseScreen(){
		return false;
	}
}