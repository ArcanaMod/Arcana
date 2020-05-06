package net.arcanamod.items;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.entities.ArcanaEntities;
import net.arcanamod.items.armor.GoggleBase;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.items.attachment.Focus;
import net.arcanamod.items.attachment.WandCore;
import net.arcanamod.util.GogglePriority;
import net.minecraft.item.Item;
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
	
	//	// Materials - Values
	//	public static Item.ToolMaterial MATERIAL_ARCANIUM = EnumHelper.addToolMaterial("material_arcanium", 4, 1987, 9.0F, 4.0F, 20);
	//	public static Item.ToolMaterial MATERIAL_VOID_METAL = EnumHelper.addToolMaterial("material_void_metal", 5, 250, 10.0F, 4.5F, 8);
	//	public static ArmorItem.ArmorMaterial ARMOR_MATERIAL_ARCANIUM = EnumHelper.addArmorMaterial("armor_material_arcanium", Arcana.MODID + ":arcanium_armor", 1987, new int[]{4, 7, 9, 4}, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.5F);
	//
	//	// Arcanium
	//	public static Item ARCANIUM_AXE = new AxeBase("arcanium_axe", MATERIAL_ARCANIUM).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_SWORD = new SwordBase("arcanium_sword", MATERIAL_ARCANIUM).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_HOE = new HoeBase("arcanium_hoe", MATERIAL_ARCANIUM).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_PICKAXE = new PickaxeBase("arcanium_pickaxe", MATERIAL_ARCANIUM).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_SHOVEL = new ShovelBase("arcanium_shovel", MATERIAL_ARCANIUM).setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	public static Item ARCANIUM_HELMET = new ArmorBase("arcanium_helmet", ARMOR_MATERIAL_ARCANIUM, 1, EquipmentSlotType.HEAD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_CHESTPLATE = new ArmorBase("arcanium_chestplate", ARMOR_MATERIAL_ARCANIUM, 1, EquipmentSlotType.CHEST).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_LEGGINGS = new ArmorBase("arcanium_leggings", ARMOR_MATERIAL_ARCANIUM, 2, EquipmentSlotType.LEGS).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_BOOTS = new ArmorBase("arcanium_boots", ARMOR_MATERIAL_ARCANIUM, 1, EquipmentSlotType.FEET).setCreativeTab(Arcana.TAB_ARCANA);
	
	//	// Void Metal
	//	public static Item VOID_METAL_AXE = new AutoRepairAxe("void_metal_axe", MATERIAL_VOID_METAL).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item VOID_METAL_SWORD = new AutoRepairSword("void_metal_sword", MATERIAL_VOID_METAL).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item VOID_METAL_HOE = new AutoRepairHoe("void_metal_hoe", MATERIAL_VOID_METAL).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item VOID_METAL_PICKAXE = new AutoRepairPickaxe("void_metal_pickaxe", MATERIAL_VOID_METAL).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item VOID_METAL_SHOVEL = new AutoRepairShovel("void_metal_shovel", MATERIAL_VOID_METAL).setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	//Items With Function
	//
	//	public static Item RESEARCH_NOTE_COMPLETE = new ItemResearchNote("research_note_complete", true).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item RESEARCH_NOTE = new ItemResearchNote("research_note", false).setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Item> SCRIBING_TOOLS = ITEMS.register("scribing_tools", () -> new Item(new Properties().group(Arcana.ITEMS).maxStackSize(1).maxDamage(100)));
	//	public static Item RESEARCH_TABLE_PLACER = new ItemResearchTable().setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item VIS_MANIPULATORS = new ItemVisManipulators().setCreativeTab(Arcana.TAB_ARCANA);
	//
	public static final RegistryObject<Item> ARCANUM = ITEMS.register("arcanum", () -> new ResearchBookItem(new Properties().group(Arcana.ITEMS), new ResourceLocation(Arcana.MODID, "arcanum")));
	public static final RegistryObject<Item> GRIMOIRE = ITEMS.register("illustrious_grimoire", () -> new ResearchBookItem(new Properties().group(Arcana.ITEMS), new ResourceLocation(Arcana.MODID, "illustrious_grimoire")));
	public static final RegistryObject<Item> CODEX = ITEMS.register("tainted_codex", () -> new ResearchBookItem(new Properties().group(Arcana.ITEMS), new ResourceLocation(Arcana.MODID, "tainted_codex")));
	public static final RegistryObject<Item> RITES = ITEMS.register("crimson_rites", () -> new ResearchBookItem(new Properties().group(Arcana.ITEMS), new ResourceLocation(Arcana.MODID, "crimson_rites")));
	//
	//  // Why is this here?
	//	// Food - Will add food class soon --Tea :D    (rip lol --Luna)
	//	public static Item CAT_MEAT_COOKED = new ItemBase("cooked_cat_meat").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item CAT_MEAT_UNCOOKED = new ItemBase("cat_meat").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item DOG_MEAT_COOKED = new ItemBase("cooked_dog_meat").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item DOG_MEAT_UNCOOKED = new ItemBase("dog_meat").setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	// Materials
	public static final RegistryObject<Item> THAUMIUM_INGOT = ITEMS.register("thaumium_ingot", () -> new Item(new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> ARCANIUM_INGOT = ITEMS.register("arcanium_ingot", () -> new Item(new Properties().group(Arcana.ITEMS)));
	public static final RegistryObject<Item> VOID_METAL_INGOT = ITEMS.register("void_metal_ingot", () -> new Item(new Properties().group(Arcana.ITEMS)));
	
	public static final RegistryObject<Item> SILVERWOOD_STICK = ITEMS.register("silverwood_stick", () -> new Item(new Properties().group(Arcana.ITEMS)));
	//	public static Item AMBER = new ItemBase("amber").setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	// Goggle Armor
	public static final RegistryObject<Item> GOGGLES_OF_REVEALING = ITEMS.register("goggles_of_revealing", () -> new GoggleBase(GoggleBase.GOGGLE_MATERIAL, new Properties().group(Arcana.ITEMS), GogglePriority.SHOW_NODE));
	//
	//	// Wand Parts
	//	public static Item FOCUS_PARTS = new ItemBase("focus_parts").setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	// Wand Attachments
	public static final RegistryObject<Cap> IRON_CAP = ITEMS.register("iron_cap", () -> new Cap(new Properties().group(Arcana.ITEMS)).setLevel(1));
	public static final RegistryObject<Cap> GOLD_CAP = ITEMS.register("gold_cap", () -> new Cap(new Properties().group(Arcana.ITEMS)).setLevel(2));
	public static final RegistryObject<Cap> COPPER_CAP = ITEMS.register("copper_cap", () -> new Cap(new Properties().group(Arcana.ITEMS)).setLevel(2));
	public static final RegistryObject<Cap> SILVER_CAP = ITEMS.register("silver_cap", () -> new Cap(new Properties().group(Arcana.ITEMS)).setLevel(2));
	public static final RegistryObject<Cap> THAUMIUM_CAP = ITEMS.register("thaumium_cap", () -> new Cap(new Properties().group(Arcana.ITEMS)).setLevel(3));
	public static final RegistryObject<Cap> VOID_CAP = ITEMS.register("void_cap", () -> new Cap(new Properties().group(Arcana.ITEMS)).setLevel(3));
	//public static Item FOCUS = new ItemFocus("focus");
	
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