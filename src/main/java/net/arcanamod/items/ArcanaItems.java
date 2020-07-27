package net.arcanamod.items;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.effects.ArcanaEffects;
import net.arcanamod.items.armor.ArcanaArmourMaterials;
import net.arcanamod.items.armor.AutoRepairArmorItem;
import net.arcanamod.items.armor.GogglesItem;
import net.arcanamod.items.attachment.CapItem;
import net.arcanamod.items.attachment.Core;
import net.arcanamod.items.attachment.CoreItem;
import net.arcanamod.items.attachment.FocusItem;
import net.arcanamod.items.tools.*;
import net.arcanamod.util.GogglePriority;
import net.arcanamod.util.annotations.GIM;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.item.Item.Properties;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static net.arcanamod.Arcana.MODID;
import static net.arcanamod.Arcana.arcLoc;

/**
 * Initialize Items here
 *
 * @author Merijn, Tea, Luna
 * @see ArcanaBlocks
 */
@SuppressWarnings("unused")
public class ArcanaItems{
	
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, MODID);
	
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

	// Silver Tools
	public static final RegistryObject<Item> SILVER_SWORD = ITEMS.register("silver_sword", () -> new SilverSwordItem(ArcanaToolTiers.SILVER, 3, -2.4f, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> SILVER_SHOVEL = ITEMS.register("silver_shovel", () -> new SilverShovelItem(ArcanaToolTiers.SILVER, 1.5f, -3, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> SILVER_PICKAXE = ITEMS.register("silver_pickaxe", () -> new SilverPickaxeItem(ArcanaToolTiers.SILVER, 1, -2.8f, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> SILVER_AXE = ITEMS.register("silver_axe", () -> new SilverAxeItem(ArcanaToolTiers.SILVER, 5.5f, -3, new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> SILVER_HOE = ITEMS.register("silver_hoe", () -> new SilverHoeItem(ArcanaToolTiers.SILVER, -.5f, new Properties().group(Arcana.ITEMS)));

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

	public static final RegistryObject<Item> RESEARCH_NOTE_COMPLETE = ITEMS.register("research_note_complete", () -> new ResearchNoteItem(new Item.Properties().group(Arcana.ITEMS),true));
	public static final RegistryObject<Item> RESEARCH_NOTE = ITEMS.register("research_note", () -> new ResearchNoteItem(new Item.Properties().group(Arcana.ITEMS),false));
	public static final RegistryObject<Item> SCRIBING_TOOLS = ITEMS.register("scribing_tools", () -> new Item(new Properties().group(Arcana.ITEMS).maxStackSize(1).maxDamage(100).setNoRepair()));
	public static final RegistryObject<Item> RESEARCH_TABLE_PLACER = ITEMS.register("research_table_placer", () -> new ResearchTableItem(new Properties().group(Arcana.ITEMS).maxStackSize(1)));
	public static final RegistryObject<Item> VIS_MANIPULATION_TOOLS = ITEMS.register("vis_manipulation_tools", () -> new VisManipulatorsItem(new Item.Properties().group(Arcana.ITEMS).maxStackSize(1)));
	public static final RegistryObject<Item> PHIAL = ITEMS.register("phial", PhialItem::new);
	public static final RegistryObject<Item> SCRIBBLED_NOTES_COMPLETE = ITEMS.register("scribbled_notes_complete", () -> new ScribbledNotesCompleteItem(new Properties().group(Arcana.ITEMS).maxStackSize(1)));
	public static final RegistryObject<Item> SCRIBBLED_NOTES = ITEMS.register("scribbled_notes", () -> new ScribbledNoteItem(new Properties().group(Arcana.ITEMS).maxStackSize(1)));
	
	// Books
	public static final RegistryObject<Item> ARCANUM = ITEMS.register("arcanum", () -> new ResearchBookItem(new Properties().group(Arcana.ITEMS).maxStackSize(1), new ResourceLocation(MODID, "arcanum")));
	public static final RegistryObject<Item> GRIMOIRE = ITEMS.register("illustrious_grimoire", () -> new ResearchBookItem(new Properties().group(Arcana.ITEMS).maxStackSize(1), new ResourceLocation(MODID, "illustrious_grimoire")));
	public static final RegistryObject<Item> CODEX = ITEMS.register("tainted_codex", () -> new ResearchBookItem(new Properties().group(Arcana.ITEMS).maxStackSize(1), new ResourceLocation(MODID, "tainted_codex")));
	public static final RegistryObject<Item> RITES = ITEMS.register("crimson_rites", () -> new ResearchBookItem(new Properties().group(Arcana.ITEMS).maxStackSize(1), new ResourceLocation(MODID, "crimson_rites")));
	
	// Materials
	public static final RegistryObject<Item> THAUMIUM_INGOT = ITEMS.register("thaumium_ingot", () -> new Item(new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> ARCANIUM_INGOT = ITEMS.register("arcanium_ingot", () -> new Item(new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> VOID_METAL_INGOT = ITEMS.register("void_metal_ingot", () -> new Item(new Properties().group(Arcana.ITEMS)));
	@GIM(type = GIM.Type.ITEM) public static final RegistryObject<Item> VOID_METAL_NUGGET = ITEMS.register("void_metal_nugget", () -> new Item(new Properties().group(Arcana.ITEMS)));
	@GIM(type = GIM.Type.ITEM) public static final RegistryObject<Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item(new Properties().group(Arcana.ITEMS)));
	
	public static final RegistryObject<Item> SILVERWOOD_STICK = ITEMS.register("silverwood_stick", () -> new Item(new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> AMBER = ITEMS.register("amber", () -> new Item(new Item.Properties().group(Arcana.ITEMS)));
	//public static final RegistryObject<Item> QUICKSILVER = ITEMS.register("quicksilver", () -> new Item(new Item.Properties().group(Arcana.ITEMS)));
	
	// Goggles
	public static final RegistryObject<Item> GOGGLES_OF_REVEALING = ITEMS.register("goggles_of_revealing", () -> new GogglesItem(ArcanaArmourMaterials.GOGGLES, new Properties().group(Arcana.ITEMS), GogglePriority.SHOW_NODE));
	
	// Caps
	public static final RegistryObject<CapItem> IRON_CAP = ITEMS.register("iron_cap", () -> new CapItem(new Properties().group(Arcana.ITEMS), 2, 1, 1, arcLoc("iron_cap")));
	public static final RegistryObject<CapItem> GOLD_CAP = ITEMS.register("gold_cap", () -> new CapItem(new Properties().group(Arcana.ITEMS), 3, 2, 2, arcLoc("gold_cap")));
	public static final RegistryObject<CapItem> COPPER_CAP = ITEMS.register("copper_cap", () -> new CapItem(new Properties().group(Arcana.ITEMS), 2, 1, 2, arcLoc("copper_cap")));
	public static final RegistryObject<CapItem> SILVER_CAP = ITEMS.register("silver_cap", () -> new CapItem(new Properties().group(Arcana.ITEMS), 3, 2, 2, arcLoc("silver_cap")));
	public static final RegistryObject<CapItem> THAUMIUM_CAP = ITEMS.register("thaumium_cap", () -> new CapItem(new Properties().group(Arcana.ITEMS), 6, 4, 3, arcLoc("thaumium_cap")));
	public static final RegistryObject<CapItem> VOID_CAP = ITEMS.register("void_cap", () -> new CapItem(new Properties().group(Arcana.ITEMS), 8, 4, 3, arcLoc("void_cap")));
	
	// Foci
	public static RegistryObject<FocusItem> DEFAULT_FOCUS = ITEMS.register("wand_focus", () -> new FocusItem(new Properties(), arcLoc("wand_focus")));
	public static final RegistryObject<Item> FOCUS_PARTS = ITEMS.register("focus_parts", () -> new Item(new Item.Properties().group(Arcana.ITEMS)));
	
	// Cores
	public static final Core.Impl WOOD_WAND_CORE = new Core.Impl(25, 1, arcLoc("wood_wand"));
	public static final RegistryObject<CoreItem> GREATWOOD_WAND_CORE = ITEMS.register("greatwood_wand_core", () -> new CoreItem(new Properties().group(Arcana.ITEMS), 35, 2, arcLoc("greatwood_wand")));
	public static final RegistryObject<CoreItem> TAINTED_WAND_CORE = ITEMS.register("tainted_wand_core", () -> new CoreItem(new Properties().group(Arcana.ITEMS), 45, 2, arcLoc("tainted_wand")));
	public static final RegistryObject<CoreItem> DAIR_WAND_CORE = ITEMS.register("dair_wand_core", () -> new CoreItem(new Properties().group(Arcana.ITEMS), 30, 3, arcLoc("dair_wand")));
	public static final RegistryObject<CoreItem> HAWTHORN_WAND_CORE = ITEMS.register("hawthorn_wand_core", () -> new CoreItem(new Properties().group(Arcana.ITEMS), 40, 3, arcLoc("hawthorn_wand")));
	public static final RegistryObject<CoreItem> SILVERWOOD_WAND_CORE = ITEMS.register("silverwood_wand_core", () -> new CoreItem(new Properties().group(Arcana.ITEMS), 40, 4, arcLoc("silverwood_wand")));
	public static final RegistryObject<CoreItem> ARCANIUM_WAND_CORE = ITEMS.register("arcanium_wand_core", () -> new CoreItem(new Properties().group(Arcana.ITEMS), 50, 4, arcLoc("arcanium_wand")));
	
	public static final RegistryObject<Item> WAND = ITEMS.register("wand", () -> new WandItem(new Properties().group(Arcana.ITEMS).maxStackSize(1)));

	//Tainted Items
	public static final RegistryObject<Item> TAINTED_MELON_SLICE = ITEMS.register("tainted_melon_slice", () -> new Item(new Properties().group(Arcana.ITEMS).food(new Food.Builder().hunger(1).saturation(0.6f).effect(() -> new EffectInstance(ArcanaEffects.TAINTED.get(), 600, 0), 1f).build())));
	public static final RegistryObject<Item> TAINTED_POTATO = ITEMS.register("tainted_potato", () -> new Item(new Properties().group(Arcana.ITEMS).food(new Food.Builder().hunger(1).saturation(0.8f).effect(() -> new EffectInstance(ArcanaEffects.TAINTED.get(), 600, 0), 1f).build())));
	public static final RegistryObject<Item> BAKED_TAINTED_POTATO = ITEMS.register("baked_tainted_potato", () -> new Item(new Properties().group(Arcana.ITEMS).food(new Food.Builder().hunger(3).saturation(3.0f).effect(() -> new EffectInstance(ArcanaEffects.TAINTED.get(), 600, 0), 1f).build())));
	public static final RegistryObject<Item> TAINTED_BEETROOT = ITEMS.register("tainted_beetroot", () -> new Item(new Properties().group(Arcana.ITEMS).food(new Food.Builder().hunger(1).saturation(0.6f).effect(() -> new EffectInstance(ArcanaEffects.TAINTED.get(), 600, 0), 1f).build())));
	public static final RegistryObject<Item> TAINTED_CARROT = ITEMS.register("tainted_carrot", () -> new Item(new Properties().group(Arcana.ITEMS).food(new Food.Builder().hunger(2).saturation(1.8f).effect(() -> new EffectInstance(ArcanaEffects.TAINTED.get(), 600, 0), 1f).build())));
	public static final RegistryObject<Item> TAINTED_BERRIES = ITEMS.register("tainted_berries", () -> new Item(new Properties().group(Arcana.ITEMS).food(new Food.Builder().hunger(1).saturation(0.2f).effect(() -> new EffectInstance(ArcanaEffects.TAINTED.get(), 600, 0), 1f).build())));

	public static final RegistryObject<Item> RAW_TAINTED_COD = ITEMS.register("raw_tainted_cod", () -> new Item(new Properties().group(Arcana.ITEMS).food(new Food.Builder().hunger(1).saturation(0.2f).effect(() -> new EffectInstance(ArcanaEffects.TAINTED.get(), 600, 0), 1f).build())));
	public static final RegistryObject<Item> COOKED_TAINTED_COD = ITEMS.register("cooked_tainted_cod", () -> new Item(new Properties().group(Arcana.ITEMS).food(new Food.Builder().hunger(3).saturation(3.0f).effect(() -> new EffectInstance(ArcanaEffects.TAINTED.get(), 600, 0), 1f).build())));
	public static final RegistryObject<Item> RAW_TAINTED_SALMON = ITEMS.register("raw_tainted_salmon", () -> new Item(new Properties().group(Arcana.ITEMS).food(new Food.Builder().hunger(1).saturation(0.2f).effect(() -> new EffectInstance(ArcanaEffects.TAINTED.get(), 600, 0), 1f).build())));
	public static final RegistryObject<Item> COOKED_TAINTED_SALMON = ITEMS.register("cooked_tainted_salmon", () -> new Item(new Properties().group(Arcana.ITEMS).food(new Food.Builder().hunger(3).saturation(4.8f).effect(() -> new EffectInstance(ArcanaEffects.TAINTED.get(), 600, 0), 1f).build())));
	public static final RegistryObject<Item> TAINTED_TROPICAL_FISH = ITEMS.register("tainted_tropical_fish", () -> new Item(new Properties().group(Arcana.ITEMS).food(new Food.Builder().hunger(1).saturation(0.2f).effect(() -> new EffectInstance(ArcanaEffects.TAINTED.get(), 600, 0), 1f).build())));
	public static final RegistryObject<Item> TAINTED_PUFFERFISH = ITEMS.register("tainted_pufferfish", () -> new Item(new Properties().group(Arcana.ITEMS).food(new Food.Builder().hunger(1).saturation(0.2f).effect(() -> new EffectInstance(ArcanaEffects.TAINTED.get(), 600, 0), 1f).effect(() -> new EffectInstance(Effects.HUNGER, 300, 2), 1f).effect(() -> new EffectInstance(Effects.NAUSEA, 300, 1), 1f).effect(() -> new EffectInstance(Effects.POISON, 1200, 3), 1f).build())));
	public static final RegistryObject<Item> TAINTED_SUGAR_CANE = ITEMS.register("tainted_sugar_cane", () -> new Item(new Properties().group(Arcana.ITEMS)));
}