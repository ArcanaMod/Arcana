package net.arcanamod.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.arcanamod.client.event.ClientTickHandler;
import net.arcanamod.client.model.ArcanumModel;
import net.arcanamod.items.ArcanaItems;
import net.arcanamod.mixin.AccessorFirstPersonRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.arcanamod.ArcanaVariables.arcLoc;

public class BookRenderer {
	public static final Logger LOGGER = LogManager.getLogger();

	private static final ArcanumModel model = new ArcanumModel();
	@SuppressWarnings("deprecation")
	public static final Material TEXTURE = new Material(AtlasTexture.LOCATION_BLOCKS_TEXTURE, arcLoc("models/items/thaumonomicon_model"));

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void renderHand(RenderHandEvent evt) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.gameSettings.thirdPersonView != 0
				|| mc.player.getHeldItem(evt.getHand()).isEmpty()
				|| mc.player.getHeldItem(evt.getHand()).getItem() != ArcanaItems.ARCANUM.get()) {
			return;
		}
		evt.setCanceled(true);
		try {
			renderFirstPersonItem(mc.player, evt.getPartialTicks(), evt.getInterpolatedPitch(), evt.getHand(), evt.getSwingProgress(), evt.getItemStack(), evt.getEquipProgress(), evt.getMatrixStack(), evt.getBuffers(), evt.getLight());
		} catch (Throwable throwable) {
			LOGGER.warn("Failed to render book", throwable);
		}
	}

	// [VanillaCopy] FirstPersonRenderer, irrelevant branches stripped out
	private static void renderFirstPersonItem(AbstractClientPlayerEntity player, float partialTicks, float pitch, Hand hand, float swingProgress, ItemStack stack, float equipProgress, MatrixStack ms, IRenderTypeBuffer buffers, int light) {
		boolean flag = hand == Hand.MAIN_HAND;
		HandSide handside = flag ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
		ms.push();
		{
			boolean flag3 = handside == HandSide.RIGHT;
			{
				float f5 = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
				float f6 = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * ((float) Math.PI * 2F));
				float f10 = -0.2F * MathHelper.sin(swingProgress * (float) Math.PI);
				int l = flag3 ? 1 : -1;
				ms.translate((double) ((float) l * f5), (double) f6, (double) f10);
				((AccessorFirstPersonRenderer) Minecraft.getInstance().getFirstPersonRenderer()).botania_transformSideFirstPerson(ms, handside, equipProgress);
				((AccessorFirstPersonRenderer) Minecraft.getInstance().getFirstPersonRenderer()).botania_transformFirstPerson(ms, handside, swingProgress);
			}

			doRender(stack, handside, ms, buffers, light, partialTicks);
		}

		ms.pop();
	}

	private static void doRender(ItemStack stack, HandSide side, MatrixStack ms, IRenderTypeBuffer buffers, int light, float partialTicks) {
		Minecraft mc = Minecraft.getInstance();

		ms.push();

		float ticks = ClientTickHandler.ticksWithLexicaOpen;
		if (ticks > 0 && ticks < 10) {
			if (stack.getOrCreateTag().getBoolean("open")) {
				ticks += partialTicks;
			} else {
				ticks -= partialTicks;
			}
		}

		// Move whole book
		if (side == HandSide.RIGHT) {
			ms.translate(0.3F + 0.02F * ticks, 0.125F + 0.01F * ticks, -0.2F - 0.035F * ticks);
			ms.rotate(Vector3f.YP.rotationDegrees(74F + ticks * 10));
		} else {
			ms.translate(0.1F - 0.02F * ticks, 0.125F + 0.01F * ticks, -0.2F - 0.035F * ticks);
			ms.rotate(Vector3f.YP.rotationDegrees(74F + ticks * 10));
		}

		ms.rotate(Vector3f.ZP.rotationDegrees(-0.9F + ticks * 2.85F));
		float opening = MathHelper.clamp(ticks / 12F, 0, 1);

		float pageFlipTicks = ClientTickHandler.pageFlipTicks;
		if (pageFlipTicks > 0) {
			pageFlipTicks -= ClientTickHandler.partialTicks;
		}

		float pageFlip = pageFlipTicks / 5F + 0.8f;

		float leftPageAngle = MathHelper.frac(pageFlip + 0.25F) * 1.6F - 0.3F;
		float rightPageAngle = MathHelper.frac(pageFlip + 0.75F) * 1.6F - 0.3F;
		model.func_228247_a_(ClientTickHandler.total, MathHelper.clamp(leftPageAngle, 0.0F, 1.0F), MathHelper.clamp(rightPageAngle, 0.0F, 1.0F), opening);

		Material mat = TEXTURE;
		IVertexBuilder buffer = mat.getBuffer(buffers, RenderType::getEntitySolid);
		model.render(ms, buffer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

		ms.pop();
	}

	private static String toControlCodes(String s) {
		return s.replaceAll(".", "\u00a7$0");
	}
}
