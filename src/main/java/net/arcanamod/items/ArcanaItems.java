package net.arcanamod.items;

import net.arcanamod.Arcana;
import net.arcanamod.blocks.ArcanaBlocks;
import net.arcanamod.entities.ArcanaEntities;
import net.arcanamod.items.armor.GoggleBase;
import net.arcanamod.items.attachment.Cap;
import net.arcanamod.util.GogglePriority;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
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
public class ArcanaItems{
	
	public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Arcana.MODID);
	
	//	// Materials - Values
	//	public static Item.ToolMaterial MATERIAL_ARCANIUM = EnumHelper.addToolMaterial("material_arcanium", 4, 1987, 9.0F, 4.0F, 20);
	//	public static Item.ToolMaterial MATERIAL_VOID_METAL = EnumHelper.addToolMaterial("material_void_metal", 5, 250, 10.0F, 4.5F, 8);
	//	public static ArmorItem.ArmorMaterial ARMOR_MATERIAL_ARCANIUM = EnumHelper.addArmorMaterial("armor_material_arcanium", Arcana.MODID + ":arcanium_armor", 1987, new int[]{4, 7, 9, 4}, 20, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.5F);
	//
	//	//Weapons
	//	public static Item ARCANIUM_AXE = new AxeBase("arcanium_axe", MATERIAL_ARCANIUM).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_SWORD = new SwordBase("arcanium_sword", MATERIAL_ARCANIUM).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item VOID_METAL_AXE = new AutoRepairAxe("void_metal_axe", MATERIAL_VOID_METAL).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item VOID_METAL_SWORD = new AutoRepairSword("void_metal_sword", MATERIAL_VOID_METAL).setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	//Tools
	//	public static Item ARCANIUM_HOE = new HoeBase("arcanium_hoe", MATERIAL_ARCANIUM).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_PICKAXE = new PickaxeBase("arcanium_pickaxe", MATERIAL_ARCANIUM).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_SHOVEL = new ShovelBase("arcanium_shovel", MATERIAL_ARCANIUM).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item VOID_METAL_HOE = new AutoRepairHoe("void_metal_hoe", MATERIAL_VOID_METAL).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item VOID_METAL_PICKAXE = new AutoRepairPickaxe("void_metal_pickaxe", MATERIAL_VOID_METAL).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item VOID_METAL_SHOVEL = new AutoRepairShovel("void_metal_shovel", MATERIAL_VOID_METAL).setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	//Armor
	//	public static Item ARCANIUM_HELMET = new ArmorBase("arcanium_helmet", ARMOR_MATERIAL_ARCANIUM, 1, EquipmentSlotType.HEAD).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_CHESTPLATE = new ArmorBase("arcanium_chestplate", ARMOR_MATERIAL_ARCANIUM, 1, EquipmentSlotType.CHEST).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_LEGGINGS = new ArmorBase("arcanium_leggings", ARMOR_MATERIAL_ARCANIUM, 2, EquipmentSlotType.LEGS).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_BOOTS = new ArmorBase("arcanium_boots", ARMOR_MATERIAL_ARCANIUM, 1, EquipmentSlotType.FEET).setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	//Items With Function
	//
	//	public static Item RESEARCH_NOTE_COMPLETE = new ItemResearchNote("research_note_complete", true).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item RESEARCH_NOTE = new ItemResearchNote("research_note", false).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item INK = new ItemBase("ink").setMaxStackSize(1).setMaxDamage(100).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item RESEARCH_TABLE_PLACER = new ItemResearchTable().setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item VIS_MANIPULATORS = new ItemVisManipulators().setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	public static Item ARCANUM = new ItemResearchBook("arcanum", new ResourceLocation(Arcana.MODID, "arcanum")).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item GRIMOIRE = new ItemResearchBook("illustrious_grimoire", new ResourceLocation(Arcana.MODID, "illustrious_grimoire")).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item CODEX = new ItemResearchBook("tainted_codex", new ResourceLocation(Arcana.MODID, "tainted_codex")).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item RITES = new ItemResearchBook("crimson_rites", new ResourceLocation(Arcana.MODID, "crimson_rites")).setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	//Food - Will add food class soon --Tea :D    (rip lol --Luna)
	//	public static Item CAT_MEAT_COOKED = new ItemBase("cooked_cat_meat").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item CAT_MEAT_UNCOOKED = new ItemBase("cat_meat").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item DOG_MEAT_COOKED = new ItemBase("cooked_dog_meat").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item DOG_MEAT_UNCOOKED = new ItemBase("dog_meat").setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	// Materials
	//	public static Item THAUMIUM_INGOT = new ItemBase("thaumium_ingot").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_INGOT = new ItemBase("arcanium_ingot").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item VOID_METAL_INGOT = new ItemBase("void_metal_ingot").setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	public static Item SILVERWOOD_STICK = new ItemBase("silverwood_stick").setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item AMBER = new ItemBase("amber").setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	// Goggle Armor
	public static final RegistryObject<Item> GOGGLES_OF_REVEALING = ITEMS.register("goggles_of_revealing", () -> new GoggleBase(GoggleBase.GOGGLE_MATERIAL, new Properties().group(Arcana.ITEMS), GogglePriority.SHOW_NODE));
	//
	//	// Wand Parts
	//	public static Item FOCUS_PARTS = new ItemBase("focus_parts").setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	// Wand Attachments
	//	public static Cap IRON_CAP = (Cap)new Cap("iron_cap").setId(0).setLevel(1).setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Item> IRON_CAP = ITEMS.register("iron_cap", () -> new Cap(new Properties().group(Arcana.ITEMS)));
	//	public static Cap GOLD_CAP = (Cap)new Cap("gold_cap").setId(1).setLevel(2).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Cap COPPER_CAP = (Cap)new Cap("copper_cap").setId(4).setLevel(2).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Cap SILVER_CAP = (Cap)new Cap("silver_cap").setId(5).setLevel(2).setCreativeTab(Arcana.TAB_ARCANA);
	//	//public static Cap MANASTEEL_CAP = (Cap)new Cap("manasteel_cap").setId(7).setLevel(2).setCreativeTab(Main.TAB_ARCANA);
	//	public static Cap THAUMIUM_CAP = (Cap)new Cap("thaumium_cap").setId(2).setLevel(3).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Cap VOID_CAP = (Cap)new Cap("void_cap").setId(3).setLevel(3).setCreativeTab(Arcana.TAB_ARCANA);
	//	//public static Cap ELEMENTIUM_CAP = (Cap)new Cap("elementium_cap").setId(6).setLevel(3).setCreativeTab(Main.TAB_ARCANA);
	//	//public static Cap TERRASTEEL_CAP = (Cap)new Cap("terrasteel_cap").setId(8).setLevel(4).setCreativeTab(Main.TAB_ARCANA);
	//
	//	public static Item FOCUS = new ItemFocus("focus");
	//
	//	// Wand stuff
	//	public static ItemWand WOOD_WAND = (ItemWand)new ItemWand("wood_wand").setLevel(1).setCreativeTab(Arcana.TAB_ARCANA);
	public static final RegistryObject<Item> WOOD_WAND = ITEMS.register("wood_wand", () -> new ItemWand(new Properties().group(Arcana.ITEMS)));
	//	public static ItemWand GREATWOOD_WAND = (ItemWand)new ItemWand("greatwood_wand").setLevel(2).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static ItemWand TAINTED_WAND = (ItemWand)new ItemWand("tainted_wand").setLevel(2).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static ItemWand DAIR_WAND = (ItemWand)new ItemWand("dair_wand").setLevel(3).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static ItemWand HAWTHORN_WAND = (ItemWand)new ItemWand("hawthorn_wand").setLevel(3).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static ItemWand SILVERWOOD_WAND = (ItemWand)new ItemWand("silverwood_wand").setLevel(4).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static ItemWand ARCANIUM_WAND = (ItemWand)new ItemWand("arcanium_wand").setLevel(4).setCreativeTab(Arcana.TAB_ARCANA);
	//
	//	// iLlEgAl fOrWaRd rEfErEnCe I don't care
	//
	//	public static Item GREATWOOD_WAND_CORE = new WandCore("greatwood_wand_core", GREATWOOD_WAND).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item TAINTED_WAND_CORE = new WandCore("tainted_wand_core", TAINTED_WAND).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item DAIR_WAND_CORE = new WandCore("dair_wand_core", DAIR_WAND).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item HAWTHORN_WAND_CORE = new WandCore("hawthorn_wand_core", HAWTHORN_WAND).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item SILVERWOOD_WAND_CORE = new WandCore("silverwood_wand_core", SILVERWOOD_WAND).setCreativeTab(Arcana.TAB_ARCANA);
	//	public static Item ARCANIUM_WAND_CORE = new WandCore("arcanium_wand_core", ARCANIUM_WAND).setCreativeTab(Arcana.TAB_ARCANA);
	//	// Wooden wands are supported by RecipeWands.
}