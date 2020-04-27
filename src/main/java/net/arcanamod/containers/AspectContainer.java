package net.arcanamod.containers;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.VisHandler;
import net.arcanamod.client.gui.GuiAspectContainer;
import net.arcanamod.network.Connection;
import net.arcanamod.network.inventory.PktAspectClickHandler;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class AspectContainer extends Container{
	
	protected List<AspectSlot> aspectSlots = new ArrayList<>();
	protected Aspect heldAspect = null;
	protected int heldCount = 0;
	
	public List<AspectSlot> getAspectSlots(){
		return aspectSlots;
	}
	
	public void setAspectSlots(List<AspectSlot> aspectSlots){
		this.aspectSlots = aspectSlots;
	}
	
	public Aspect getHeldAspect(){
		return heldAspect;
	}
	
	public void setHeldAspect(Aspect heldAspect){
		this.heldAspect = heldAspect;
	}
	
	public int getHeldCount(){
		return heldCount;
	}
	
	public void setHeldCount(int heldCount){
		this.heldCount = heldCount;
	}
	
	public void handleClick(int mouseX, int mouseY, int button, GuiAspectContainer gui){
		for(AspectSlot slot : getAspectSlots()){
			if(slot.getInventory().get() != null && gui.isSlotVisible(slot) && isMouseOverSlot(mouseX, mouseY, slot, gui)){
				// gonna send a packet
				PktAspectClickHandler.ClickType type;
				if(button == 0)
					type = PktAspectClickHandler.ClickType.TAKE;
				else if(button == 1)
					type = PktAspectClickHandler.ClickType.PUT;
				else
					return;
				if(GuiScreen.isShiftKeyDown())
					type = type == PktAspectClickHandler.ClickType.PUT ? PktAspectClickHandler.ClickType.PUT_ALL : PktAspectClickHandler.ClickType.TAKE_ALL;
				// do some quick checking to make sure that the packet won't just do nothing
				// don't actually modify anything though!
				// <blah>
				PktAspectClickHandler.PktAspectClick packet = new PktAspectClickHandler.PktAspectClick(windowId, aspectSlots.indexOf(slot), type);
				Connection.network.sendToServer(packet);
			}
		}
	}
	
	protected boolean isMouseOverSlot(int mouseX, int mouseY, AspectSlot slot, GuiAspectContainer gui){
		return mouseX >= gui.getGuiLeft() + slot.x && mouseY >= gui.getGuiTop() + slot.y && mouseX < gui.getGuiLeft() + slot.x + 16 && mouseY < gui.getGuiTop() + slot.y + 16;
	}
	
	/**
	 * Gets a list of every aspect handler that's open; i.e. can be modified in this GUI and might need syncing.
	 * The contents of this list must be the same on the server and client side.
	 *
	 * @return A list containing all open AspectHandlers.
	 */
	public abstract List<VisHandler> getOpenHandlers();
	
	public List<VisHandler> getAllOpenHandlers(){
		List<VisHandler> handlers = new ArrayList<>(getOpenHandlers());
		for(AspectSlot slot : aspectSlots)
			if(slot instanceof AspectStoreSlot)
				handlers.add(((AspectStoreSlot)slot).getHolder());
		return handlers;
	}
	
	public void onContainerClosed(@Nonnull EntityPlayer player){
		super.onContainerClosed(player);
		aspectSlots.forEach(AspectSlot::onClose);
	}
	
	public void onAspectSlotChange(){
	}
}