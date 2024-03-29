package net.arcanamod.event;

import com.google.common.collect.Sets;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.ArcanaSounds;
import net.arcanamod.capabilities.Researcher;
import net.arcanamod.capabilities.TaintTrackable;
import net.arcanamod.effects.ArcanaEffects;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.systems.spell.casts.DelayedCast;
import net.arcanamod.systems.spell.casts.ToggleableCast;
import net.arcanamod.systems.taint.Taint;
import net.arcanamod.world.AuraView;
import net.arcanamod.world.Node;
import net.arcanamod.world.NodeType;
import net.arcanamod.world.ServerAuraView;
import net.minecraft.advancements.Advancement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.arcanamod.ArcanaVariables.arcLoc;

@Mod.EventBusSubscriber
public class EntityTickHandler{

	@SubscribeEvent
	public static void playerSleep(PlayerSleepInBedEvent event){
		if (!event.getPlayer().world.isRemote) {
			ServerPlayerEntity playerEntity = (ServerPlayerEntity) event.getPlayer();
			Advancement interacted = playerEntity.world.getServer().getAdvancementManager().getAdvancement(arcLoc("interacted_with_magic"));
			Advancement gotArcanum = playerEntity.world.getServer().getAdvancementManager().getAdvancement(arcLoc("arcanum_accepted"));
			if (playerEntity.inventory.hasAny(Sets.newHashSet(Items.PAPER,ArcanaItems.SCRIBBLED_NOTES.get())) && playerEntity.getAdvancements().getProgress(interacted).isDone() && !playerEntity.getAdvancements().getProgress(gotArcanum).isDone()){
				playerEntity.getAdvancements().getProgress(gotArcanum).grantCriterion("impossible");
				playerEntity.inventory.addItemStackToInventory(new ItemStack(ArcanaItems.SCRIBBLED_NOTES_COMPLETE.get()));
			}
		}
	}

	@SubscribeEvent
	public static void tickPlayer(TickEvent.PlayerTickEvent event){
		PlayerEntity player = event.player;
		
		// Give completed scribbled note when player is near node
		if(player instanceof ServerPlayerEntity && event.side == LogicalSide.SERVER && event.phase == TickEvent.Phase.END){
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
			// If the player is near a node,
			AuraView view = new ServerAuraView(serverPlayerEntity.getServerWorld());
			Collection<Node> ranged = new ArrayList<>(view.getNodesWithinAABB(player.getBoundingBox().grow(2)));

			if(!ranged.isEmpty()){
				// and is holding the scribbled notes item,
				if (player.inventory.hasItemStack(new ItemStack(ArcanaItems.SCRIBBLED_NOTES.get()))) {
					// it switches it for a complete version,
					player.inventory.setInventorySlotContents(getSlotFor(new ItemStack(ArcanaItems.SCRIBBLED_NOTES.get()), player.inventory), new ItemStack(ArcanaItems.SCRIBBLED_NOTES_COMPLETE.get()));
					// and gives them a status message.
					ITextComponent status = new TranslationTextComponent("status.get_complete_note").mergeStyle(TextFormatting.ITALIC, TextFormatting.LIGHT_PURPLE);
					serverPlayerEntity.sendStatusMessage(status, false);
					// and grant the advancement.
					MinecraftServer server = player.world.getServer();
					Advancement interacted = server.getAdvancementManager().getAdvancement(arcLoc("interacted_with_magic"));
					((ServerPlayerEntity) player).getAdvancements().getProgress(interacted).grantCriterion("impossible");
					Advancement gotArcanum = server.getAdvancementManager().getAdvancement(arcLoc("arcanum_accepted"));
					((ServerPlayerEntity) player).getAdvancements().getProgress(gotArcanum).grantCriterion("impossible");
				} else {
					MinecraftServer server = player.world.getServer();
					Advancement interacted = server.getAdvancementManager().getAdvancement(arcLoc("interacted_with_magic"));
					if (interacted != null)
						if(!serverPlayerEntity.getAdvancements().getProgress(interacted).isDone()){
							// grant the advancement.
							serverPlayerEntity.getAdvancements().getProgress(interacted).grantCriterion("impossible");

							ITextComponent status = new TranslationTextComponent("status.sleep_paper_get_arcanum").mergeStyle(TextFormatting.ITALIC, TextFormatting.LIGHT_PURPLE);
							serverPlayerEntity.sendStatusMessage(status, false);
						}
				}
			}
			
			List<DelayedCast.Impl> spellsScheduledToDeletion = new ArrayList<>();
			DelayedCast.delayedCasts.forEach(delayedCast -> {
				if(delayedCast.ticks >= delayedCast.ticksPassed){
					delayedCast.spellEvent.accept(0);
					spellsScheduledToDeletion.add(delayedCast);
				}else
					delayedCast.ticksPassed++;
			});
			DelayedCast.delayedCasts.removeAll(spellsScheduledToDeletion);

			ToggleableCast.toggleableCasts.forEach(toggleableCast -> {
				if(toggleableCast.getSecond().ticks >= toggleableCast.getSecond().ticksPassed){
					toggleableCast.getSecond().spellEvent.accept(0);
					toggleableCast.getSecond().ticksPassed = 0;
				}else
					toggleableCast.getSecond().ticksPassed++;
			});
		}

	}
	
	@SubscribeEvent
	public static void tickEntities(LivingEvent.LivingUpdateEvent event){
		LivingEntity living = event.getEntityLiving();
		TaintTrackable trackable = TaintTrackable.getFrom(living);
		if(trackable != null && trackable.isTracking()){
			if(Taint.isAreaInTaintBiome(living.getPosition(), living.world)){
				trackable.setInTaintBiome(true);
				trackable.addTimeInTaintBiome(1);
				if(!Taint.isTainted(living.getType()) && trackable.getTimeInTaintBiome() > ArcanaConfig.TAINT_EFFECT_TIME.get())
					living.addPotionEffect(new EffectInstance(ArcanaEffects.TAINTED.get(), 5 * 20, 0, true, true));
			}else{
				trackable.setInTaintBiome(false);
				trackable.setTimeInTaintBiome(0);
				trackable.setTracking(false);
			}
		}
	}
	
	private static int getSlotFor(ItemStack stack, PlayerInventory self){
		for(int i = 0; i < self.mainInventory.size(); ++i)
			if(!self.mainInventory.get(i).isEmpty() && stackEqualExact(stack, self.mainInventory.get(i)))
				return i;
		
		return -1;
	}
	
	private static boolean stackEqualExact(ItemStack stack1, ItemStack stack2){
		return stack1.getItem() == stack2.getItem() && ItemStack.areItemStackTagsEqual(stack1, stack2);
	}
}