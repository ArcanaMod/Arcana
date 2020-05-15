package net.arcanamod.items;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.entities.ArcanaEntities;
import net.arcanamod.items.armor.ArcanaArmourMaterials;
import net.arcanamod.items.armor.AutoRepairArmorItem;
import net.arcanamod.items.armor.GoggleBase;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Focus;
import net.arcanamod.items.attachment.WandCore;
import net.arcanamod.items.tools.*;
import net.arcanamod.util.GogglePriority;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.Item.Properties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Initialize Items here
 *
 * @author Merijn, Tea, Luna
 * @see ArcanaEntities
 * @see ArcanaBlocks
 */
@SuppressWarnings("unused")
public class ArcanaItems{
	
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Arcana.MODID);
	
	// Arcanium
	public static final RegistryObject<Item> ARCANIUM_SWORD = ITEMS.register("arcanium_sword", () -> new SwordItem(ArcanaToolTiers.ARCANIUM, 3, -2.4f, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> ARCANIUM_SHOVEL = ITEMS.register("arcanium_shovel", () -> new ShovelItem(ArcanaToolTiers.ARCANIUM, 1.5f, -3, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> ARCANIUM_PICKAXE = ITEMS.register("arcanium_pickaxe", () -> new PickaxeItem(ArcanaToolTiers.ARCANIUM, 1, -2.8f, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> ARCANIUM_AXE = ITEMS.register("arcanium_axe", () -> new AxeItem(ArcanaToolTiers.ARCANIUM, 5.5f, -3, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> ARCANIUM_HOE = ITEMS.register("arcanium_hoe", () -> new HoeItem(ArcanaToolTiers.ARCANIUM, -.5f, new Properties().group(Arcana.ITEMS)));
	
	public static final RegistryObject<Item> ARCANIUM_HELMET = ITEMS.register("arcanium_helmet", () -> new ArmorItem(ArcanaArmourMaterials.ARCANIUM, EquipmentSlotType.HEAD, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> ARCANIUM_CHESTPLATE = ITEMS.register("arcanium_chestplate", () -> new ArmorItem(ArcanaArmourMaterials.ARCANIUM, EquipmentSlotType.CHEST, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> ARCANIUM_LEGGINGS = ITEMS.register("arcanium_leggings", () -> new ArmorItem(ArcanaArmourMaterials.ARCANIUM, EquipmentSlotType.LEGS, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> ARCANIUM_BOOTS = ITEMS.register("arcanium_boots", () -> new ArmorItem(ArcanaArmourMaterials.ARCANIUM, EquipmentSlotType.FEET, new Properties().group(Arcana.ITEMS)));
	
	// Void Metal
	public static final RegistryObject<Item> VOID_METAL_SWORD = ITEMS.register("void_metal_sword", () -> new AutoRepairSwordItem(ArcanaToolTiers.VOID_METAL, 3, -2.4f, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> VOID_METAL_SHOVEL = ITEMS.register("void_metal_shovel", () -> new AutoRepairShovelItem(ArcanaToolTiers.VOID_METAL, 1.5f, -3, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> VOID_METAL_PICKAXE = ITEMS.register("void_metal_pickaxe", () -> new AutoRepairPickaxeItem(ArcanaToolTiers.VOID_METAL, 1, -2.8f, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> VOID_METAL_AXE = ITEMS.register("void_metal_axe", () -> new AutoRepairAxeItem(ArcanaToolTiers.VOID_METAL, 5.5f, -3, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> VOID_METAL_HOE = ITEMS.register("void_metal_hoe", () -> new AutoRepairHoeItem(ArcanaToolTiers.VOID_METAL, -.5f, new Properties().group(Arcana.ITEMS)));
	
	public static final RegistryObject<Item> VOID_METAL_HELMET = ITEMS.register("void_metal_helmet", () -> new AutoRepairArmorItem(ArcanaArmourMaterials.VOID_METAL, EquipmentSlotType.HEAD, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> VOID_METAL_CHESTPLATE = ITEMS.register("void_metal_chestplate", () -> new AutoRepairArmorItem(ArcanaArmourMaterials.VOID_METAL, EquipmentSlotType.CHEST, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> VOID_METAL_LEGGINGS = ITEMS.register("void_metal_leggings", () -> new AutoRepairArmorItem(ArcanaArmourMaterials.VOID_METAL, EquipmentSlotType.LEGS, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> VOID_METAL_BOOTS = ITEMS.register("void_metal_boots", () -> new AutoRepairArmorItem(ArcanaArmourMaterials.VOID_METAL, EquipmentSlotType.FEET, new Properties().group(Arcana.ITEMS)));
	
	//Items With Function
	
	//	public static Item RESEARCH_NOTE_COMPLETE = new ItemResearchNote("research_note_complete", true).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item RESEARCH_NOTE = new ItemResearchNote("research_note", false).setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Item> SCRIBING_TOOLS = ITEMS.register("scribing_tools", () -> new Item(new Properties().group(Arcana.ITEMS).maxStackSize(1).maxDamage(100).setNoRepair()));
	public static final RegistryObject<Item> RESEARCH_TABLE_PLACER = ITEMS.register("research_table_placer", () -> new ResearchTableItem(new Properties().group(Arcana.ITEMS).maxStackSize(1)));
	//	public static Item VIS_MANIPULATORS = new ItemVisManipulators().setCreativeTab(Arcana.TAB_ARCANA);
	//public static final RegistryObject<Item> EMPTY_PHIAL = ITEMS.register("empty_phial", () -> new Item(new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> PHIAL = ITEMS.register("phial", () -> new PhialItem());
	
	// Books
	public static final RegistryObject<Item> ARCANUM = ITEMS.register("arcanum", () -> new ResearchBookItem(new Properties().group(Arcana.ITEMS), new ResourceLocation(Arcana.MODID, "arcanum")));
	public static final RegistryObject<Item> GRIMOIRE = ITEMS.register("illustrious_grimoire", () -> new ResearchBookItem(new Properties().group(Arcana.ITEMS), new ResourceLocation(Arcana.MODID, "illustrious_grimoire")));
	public static final RegistryObject<Item> CODEX = ITEMS.register("tainted_codex", () -> new ResearchBookItem(new Properties().group(Arcana.ITEMS), new ResourceLocation(Arcana.MODID, "tainted_codex")));
	public static final RegistryObject<Item> RITES = ITEMS.register("crimson_rites", () -> new ResearchBookItem(new Properties().group(Arcana.ITEMS), new ResourceLocation(Arcana.MODID, "crimson_rites")));
	
	// Materials
	public static final RegistryObject<Item> THAUMIUM_INGOT = ITEMS.register("thaumium_ingot", () -> new Item(new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> ARCANIUM_INGOT = ITEMS.register("arcanium_ingot", () -> new Item(new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> VOID_METAL_INGOT = ITEMS.register("void_metal_ingot", () -> new Item(new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> VOID_METAL_NUGGET = ITEMS.register("void_metal_nugget", () -> new Item(new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item(new Properties().group(Arcana.ITEMS)));
	
	public static final RegistryObject<Item> SILVERWOOD_STICK = ITEMS.register("silverwood_stick", () -> new Item(new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> AMBER = ITEMS.register("amber", () -> new Item(new Item.Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> QUICKSILVER = ITEMS.register("quicksilver", () -> new Item(new Item.Properties().group(Arcana.ITEMS)));
	
	// Goggles
	public static final RegistryObject<Item> GOGGLES_OF_REVEALING = ITEMS.register("goggles_of_revealing", () -> new GoggleBase(ArcanaArmourMaterials.GOGGLES, new Properties().group(Arcana.ITEMS), GogglePriority.SHOW_NODE));
	
	// Foci
	public static final RegistryObject<Item> FOCUS_PARTS = ITEMS.register("focus_parts", () -> new Item(new Item.Properties().group(Arcana.ITEMS)));
	
	// Wand Attachments
	public static final RegistryObject<Cap> VOID_CAP = ITEMS.register("void_cap", () -> new Cap(new Properties().group(Arcana.ITEMS)).setLevel(3));
	public static final RegistryObject<Cap> IRON_CAP = ITEMS.register("iron_cap", () -> new Cap(new Properties().group(Arcana.ITEMS)).setLevel(1));
	public static final RegistryObject<Cap> GOLD_CAP = ITEMS.register("gold_cap", () -> new Cap(new Properties().group(Arcana.ITEMS)).setLevel(2));
	public static final RegistryObject<Cap> COPPER_CAP = ITEMS.register("copper_cap", () -> new Cap(new Properties().group(Arcana.ITEMS)).setLevel(2));
	public static final RegistryObject<Cap> SILVER_CAP = ITEMS.register("silver_cap", () -> new Cap(new Properties().group(Arcana.ITEMS)).setLevel(2));
	public static final RegistryObject<Cap> THAUMIUM_CAP = ITEMS.register("thaumium_cap", () -> new Cap(new Properties().group(Arcana.ITEMS)).setLevel(3));
	
	public static RegistryObject<Focus> FOCUS_NONE = ITEMS.register("focus_none", () -> new Focus(new Properties()).setId(0));
	public static RegistryObject<Focus> FOCUS_DEFAULT = ITEMS.register("focus_default", () -> new Focus(new Properties()).setId(1));
	
	// Wands
	public static final RegistryObject<ItemWand> WOOD_WAND = ITEMS.register("wood_wand", () -> new ItemWand(new Properties().group(Arcana.ITEMS)).setLevel(1));
	// 2 is the default level
	public static final RegistryObject<ItemWand> GREATWOOD_WAND = ITEMS.register("greatwood_wand", () -> new ItemWand(new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<ItemWand> TAINTED_WAND = ITEMS.register("tainted_wand", () -> new ItemWand(new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<ItemWand> DAIR_WAND = ITEMS.register("dair_wand", () -> new ItemWand(new Properties().group(Arcana.ITEMS)).setLevel(3));
	public static final RegistryObject<ItemWand> HAWTHORN_WAND = ITEMS.register("hawthorn_wand", () -> new ItemWand(new Properties().group(Arcana.ITEMS)).setLevel(3));
	public static final RegistryObject<ItemWand> SILVERWOOD_WAND = ITEMS.register("silverwood_wand", () -> new ItemWand(new Properties().group(Arcana.ITEMS)).setLevel(4));
	public static final RegistryObject<ItemWand> ARCANIUM_WAND = ITEMS.register("arcanium_wand", () -> new ItemWand(new Properties().group(Arcana.ITEMS)).setLevel(4));
	
	// iLlEgAl fOrWaRd rEfErEnCe I don't care
	
	public static final RegistryObject<Item> GREATWOOD_WAND_CORE = ITEMS.register("greatwood_wand_core", () -> new WandCore(new Properties().group(Arcana.ITEMS), GREATWOOD_WAND.get()));
	public static final RegistryObject<Item> TAINTED_WAND_CORE = ITEMS.register("tainted_wand_core", () -> new WandCore(new Properties().group(Arcana.ITEMS), TAINTED_WAND.get()));
	public static final RegistryObject<Item> DAIR_WAND_CORE = ITEMS.register("dair_wand_core", () -> new WandCore(new Properties().group(Arcana.ITEMS), DAIR_WAND.get()));
	public static final RegistryObject<Item> HAWTHORN_WAND_CORE = ITEMS.register("hawthorn_wand_core", () -> new WandCore(new Properties().group(Arcana.ITEMS), HAWTHORN_WAND.get()));
	public static final RegistryObject<Item> SILVERWOOD_WAND_CORE = ITEMS.register("silverwood_wand_core", () -> new WandCore(new Properties().group(Arcana.ITEMS), SILVERWOOD_WAND.get()));
	public static final RegistryObject<Item> ARCANIUM_WAND_CORE = ITEMS.register("arcanium_wand_core", () -> new WandCore(new Properties().group(Arcana.ITEMS), ARCANIUM_WAND.get()));
}