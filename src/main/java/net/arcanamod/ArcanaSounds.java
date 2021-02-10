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
	public static SoundType TAINT = new SoundType(0.6F, 1.0F,Impl.taint_step,Impl.taint_step,Impl.taint_step,Impl.taint_step,Impl.taint_step);

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

		@SoundName("taint_step")
		public static final SoundEvent taint_step = null;

		// Spell Cast

		@SoundName("spell_cast")
		public static final SoundEvent spell_cast = null;

		public static void init() {}

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