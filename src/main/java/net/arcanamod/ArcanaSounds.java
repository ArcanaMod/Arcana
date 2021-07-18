package net.arcanamod;

import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.ObjectHolderRegistry;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Locale;

@SuppressWarnings({"ConstantConditions", "DanglingJavadoc"})
/**
 *	Sound class that contains:
 * 	- Impl (SoundEvent registration)
 * 	- SoundTypes
 * 	- Play SoundEvent functions
 */
public class ArcanaSounds {

	// SoundTypes
	public static SoundType JAR = new SoundType(0.6F, 1.0F,Impl.jar_break,Impl.jar_step,Impl.jar_place,Impl.jar_break,Impl.jar_step);
	public static SoundType TAINT = new SoundType(0.6F, 1.4F,Impl.taint_break,Impl.taint_step,Impl.taint_place,Impl.taint_break,Impl.taint_step);
	public static SoundType CRYSTAL = new SoundType(0.6F, 1.0F,Impl.crystal_break,Impl.crystal_place,Impl.crystal_place,Impl.crystal_break,Impl.crystal_place);

	// SoundEvents
	@SuppressWarnings("ConstantConditions")
	public static void playPhialshelfSlideSound(PlayerEntity playerEntity){
		if (!playerEntity.world.isRemote) return;
		if (ArcanaSounds.Impl.phialshelf_slide.equals(null)) return;
		playerEntity.playSound(ArcanaSounds.Impl.phialshelf_slide, SoundCategory.BLOCKS,0.4f,1.2f);
	}

	@SuppressWarnings("ConstantConditions")
	public static void playPhialCorkpopSound(PlayerEntity playerEntity){
		if (!playerEntity.world.isRemote) return;
		if (ArcanaSounds.Impl.phial_corkpop.equals(null)) return;
		playerEntity.playSound(ArcanaSounds.Impl.phial_corkpop, SoundCategory.BLOCKS,0.4f,1.4f);
	}

	public static void playSpellCastSound(PlayerEntity playerEntity) {
		if (!playerEntity.world.isRemote) return;
		if (ArcanaSounds.Impl.spell_cast.equals(null)) return;
		playerEntity.playSound(ArcanaSounds.Impl.spell_cast, SoundCategory.PLAYERS,0.4f,1.0f);
	}

	//Impl
	@ObjectHolder(Arcana.MODID)
	@ParametersAreNonnullByDefault
	@SuppressWarnings({"null", "DanglingJavadoc"})
	/**
	 * Implementation of SoundEvents.
	 * Every SoundEvent in this class will be registered.
	 * You get SoundName from sounds.json
	 */
	public static class Impl {

		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.FIELD)
		private static @interface SoundName {
			String value();
		}

		// Jar/Phial Sounds

		@SoundName("jar_break")
		public static final SoundEvent jar_break = null;

		@SoundName("jar_step")
		public static final SoundEvent jar_step = null;

		@SoundName("jar_place")
		public static final SoundEvent jar_place = null;

		@SoundName("phialshelf_slide")
		public static final SoundEvent phialshelf_slide = null;

		@SoundName("phial_corkpop")
		public static final SoundEvent phial_corkpop = null;

		// Tainted Block Sounds

		@SoundName("taint_break")
		public static final SoundEvent taint_break = null;
		@SoundName("taint_step")
		public static final SoundEvent taint_step = null;
		@SoundName("taint_place")
		public static final SoundEvent taint_place = null;

		// Crystal Block Sounds

		@SoundName("crystal_break")
		public static final SoundEvent crystal_break = null;

		@SoundName("crystal_place")
		public static final SoundEvent crystal_place = null;

		//Music Discs
		@SoundName("music_arcana_theme")
		public static final SoundEvent music_arcana_theme = null;

		@SoundName("music_arcana_green_sleeves")
		public static final SoundEvent music_arcana_green_sleeves = null;

		// Spell Cast

		@SoundName("spell_cast")
		public static final SoundEvent spell_cast = null;

		public static void init() {}

		// Nodes

		@SoundName("arcananodes")
		public static final SoundEvent arcananodes = null;

		@SoundName("arcananodesnegative")
		public static final SoundEvent arcananodesnegative = null;

		@SoundName("arcana_hunger_node")
		public static final SoundEvent arcana_hunger_node = null;

		static {
			for (Field f : ArcanaSounds.Impl.class.getDeclaredFields()) {
				if (f.isAnnotationPresent(SoundName.class)) {
					ForgeRegistries.SOUND_EVENTS.register(new SoundEvent(new ResourceLocation(Arcana.MODID, f.getAnnotation(SoundName.class).value())).setRegistryName(f.getName().toLowerCase(Locale.ROOT)));
				}
			}

			ObjectHolderRegistry.applyObjectHolders();
		}
	}
}