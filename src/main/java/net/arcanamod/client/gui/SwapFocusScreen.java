package net.arcanamod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.ClientProxy;
import net.arcanamod.items.MagicDeviceItem;
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
	
	public void render(MatrixStack matricies, int mouseX, int mouseY, float partialTicks){
		super.render(matricies, mouseX, mouseY, partialTicks);
		ItemStack wand = getMinecraft().player.getHeldItem(hand);
		if(wand.getItem() instanceof MagicDeviceItem && ((MagicDeviceItem)wand.getItem()).canSwapFocus(getMinecraft().player)){
			//display current focus
			MagicDeviceItem.getFocusStack(wand).ifPresent(stack -> getMinecraft().getItemRenderer().renderItemIntoGUI(stack, width / 2 - 8, height / 2 - 8));
			//display all foci in the inventory
			List<ItemStack> foci = getAllFociStacks();
			int size = foci.size();
			int distance = size * 5 + 28;
			int particleCount = size * 12 + 16;
			for(int i = 0; i < particleCount; i++){
				random.setSeed(i);
				double v = Math.toRadians((i + (getMinecraft().player.ticksExisted + partialTicks) / (5f + random.nextInt(5) - 2)) * (360f / (particleCount)));
				int colour = UiUtil.combine(random.nextInt(128) + 127, random.nextInt(128) + 127, random.nextInt(128) + 127) | 0x6F000000;
				int distance1 = distance + random.nextInt(21) - 10;
				int x = (int)(MathHelper.cos((float)v) * (distance1 + 4)) - 4 + width / 2;
				int y = (int)(MathHelper.sin((float)v) * (distance1 + 4)) - 4 + height / 2;
				GuiUtils.drawGradientRect(matricies.getLast().getMatrix(), 0, x, y, x + 8, y + 8, colour, colour);
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
		return select(mouseX, mouseY, true);
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
	
	public boolean select(double mouseX, double mouseY, boolean clicked){
		// find the nearest focus
		// if you click the middle, you remove your focus
		if(mouseX >= -8 + (width / 2f) && mouseX < 8 + (width / 2f) && mouseY >= -8 + (height / 2f) && mouseY < 8 + (height / 2f)){
			// if you didn't click, and the mouse is in the middle, do nothing
			if(!clicked)
				return true;
			// send swap focus packet
			// index = -1 AKA remove focus
			Connection.sendToServer(new PkSwapFocus(hand, -1));
			return true;
		}
		List<ItemStack> foci = getAllFociStacks();
		int size = foci.size();
		//int distance = size * 5 + 28;
		// get nearest focus
		// find what slice the mouse falls in
		if(size > 0){
			double angle = Math.toDegrees(Math.atan2(mouseY - height / 2d, mouseX - width / 2d)) + (180d / size);
			angle = angle % 360;
			angle = angle < 0 ? 360 + angle : angle;
			int item = (int)(angle / (360d / size));
			Connection.sendToServer(new PkSwapFocus(hand, item));
			return true;
		}
		return false;
	}
	
	public boolean keyReleased(int keyCode, int scanCode, int modifiers){
		// if SWAP_FOCUS_BINDING is released, close screen
		if(!ClientProxy.SWAP_FOCUS_BINDING.isPressed()){
			double mX = getMinecraft().mouseHelper.getMouseX() * (double)getMinecraft().getMainWindow().getScaledWidth() / (double)getMinecraft().getMainWindow().getWidth();
			double mY = getMinecraft().mouseHelper.getMouseY() * (double)getMinecraft().getMainWindow().getScaledHeight() / (double)getMinecraft().getMainWindow().getHeight();
			select(mX, mY, false);
			Minecraft.getInstance().displayGuiScreen(null);
			return true;
		}
		return false;
	}
	
	public boolean isPauseScreen(){
		return false;
	}
}