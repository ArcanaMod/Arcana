package net.kineticdevelopment.arcana.common.init;

import net.kineticdevelopment.arcana.common.items.ItemAttachment;
import net.kineticdevelopment.arcana.common.items.attachment.Cap;
import net.kineticdevelopment.arcana.common.items.attachment.Focus;
import net.kineticdevelopment.arcana.common.objects.items.ItemFocus;
import net.kineticdevelopment.arcana.common.objects.items.ItemWand;
import net.kineticdevelopment.arcana.core.Main;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemInit {

	public static final List<Item> ITEMS = new ArrayList<Item>();

	// Wand Attachments
	public static ItemAttachment IRON_CAP = new Cap("iron_cap").setId(0);
	public static ItemAttachment GOLD_CAP = new Cap("gold_cap").setId(1);
	public static ItemAttachment THAUMIUM_CAP = new Cap("thaumium_cap").setId(2);
	public static ItemAttachment VOID_CAP = new Cap("void_cap").setId(3);
	public static ItemAttachment COPPER_CAP = new Cap("copper_cap").setId(4);
	public static ItemAttachment SILVER_CAP = new Cap("silver_cap").setId(5);
	public static ItemAttachment ELEMENTIUM_CAP = new Cap("elementium_cap").setId(6);
	public static ItemAttachment MANASTEEL_CAP = new Cap("manasteel_cap").setId(7);
	public static ItemAttachment TERRASTEEL_CAP = new Cap("terrasteel_cap").setId(8);

	public static ItemAttachment[] CAPS = new ItemAttachment[] {IRON_CAP, GOLD_CAP, THAUMIUM_CAP, VOID_CAP, COPPER_CAP, SILVER_CAP ,ELEMENTIUM_CAP, MANASTEEL_CAP, TERRASTEEL_CAP};

	public static Item FOCUS = new ItemFocus("focus");

	// Wand stuff
	public static Item WOOD_WAND = new ItemWand("wood_wand").setAttachments(() -> {
		ItemAttachment[][] attachments = new ItemAttachment[2][CAPS.length];

		for (int i = 0; i < CAPS.length; i++) {
			ItemAttachment attachment = CAPS[i];
			if(attachments[0] == null) {
				attachments[0] = new ItemAttachment[] {attachment};
			}
			attachments[0][i] = attachment;
		}
		attachments[1] = new ItemAttachment[] {Focus.DEFAULT};
		return attachments;
	}).setCreativeTab(Main.ARCANA);

	public static Item GREATWOOD_WAND = new ItemWand("greatwood_wand").setAttachments(() -> {
		ItemAttachment[][] attachments = new ItemAttachment[2][CAPS.length];

		attachments[0] = new ItemAttachment[] {IRON_CAP, COPPER_CAP, GOLD_CAP, SILVER_CAP, MANASTEEL_CAP};

		attachments[1] = new ItemAttachment[] {Focus.DEFAULT};
		return attachments;
	}).setCreativeTab(Main.ARCANA);

	public static Item TAINTED_WAND = new ItemWand("tainted_wand").setAttachments(() -> {
		ItemAttachment[][] attachments = new ItemAttachment[2][CAPS.length];

		attachments[0] = new ItemAttachment[] {IRON_CAP, COPPER_CAP, GOLD_CAP, SILVER_CAP, MANASTEEL_CAP};

		attachments[1] = new ItemAttachment[] {Focus.DEFAULT};
		return attachments;
	}).setCreativeTab(Main.ARCANA);

	public static Item DAIR_WAND = new ItemWand("dair_wand").setAttachments(() -> {
		ItemAttachment[][] attachments = new ItemAttachment[2][CAPS.length];

		attachments[0] = new ItemAttachment[] {IRON_CAP, GOLD_CAP, THAUMIUM_CAP, COPPER_CAP, SILVER_CAP, MANASTEEL_CAP, ELEMENTIUM_CAP};

		attachments[1] = new ItemAttachment[] {Focus.DEFAULT};
		return attachments;
	}).setCreativeTab(Main.ARCANA);

	public static Item HAWTHORN_WAND = new ItemWand("hawthorn_wand").setAttachments(() -> {
		ItemAttachment[][] attachments = new ItemAttachment[2][CAPS.length];

		attachments[0] = new ItemAttachment[] {IRON_CAP, COPPER_CAP, GOLD_CAP, SILVER_CAP, MANASTEEL_CAP, ELEMENTIUM_CAP, THAUMIUM_CAP};

		attachments[1] = new ItemAttachment[] {Focus.DEFAULT};
		return attachments;
	}).setCreativeTab(Main.ARCANA);

	public static Item SILVERWOOD_WAND = new ItemWand("silverwood_wand").setAttachments(() -> {
		ItemAttachment[][] attachments = new ItemAttachment[2][CAPS.length];

		attachments[0] = CAPS;

		attachments[1] = new ItemAttachment[] {Focus.DEFAULT};
		return attachments;
	}).setCreativeTab(Main.ARCANA);

	public static Item ARCANIUM_WAND = new ItemWand("arcanium_wand").setAttachments(() -> {
		ItemAttachment[][] attachments = new ItemAttachment[2][CAPS.length];

		attachments[0] = CAPS;

		attachments[1] = new ItemAttachment[] {Focus.DEFAULT};
		return attachments;
	}).setCreativeTab(Main.ARCANA);


}
