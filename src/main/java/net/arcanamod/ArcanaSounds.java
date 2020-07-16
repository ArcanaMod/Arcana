package net.arcanamod;

import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
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

public class ArcanaSounds {
	public static SoundType JAR = new SoundType(0.6F, 1.0F,Impl.jar_break,Impl.jar_step,Impl.jar_place,Impl.jar_break,Impl.jar_step);

	@ObjectHolder(Arcana.MODID)
	@ParametersAreNonnullByDefault
	@SuppressWarnings("null")
	public static class Impl {

		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.FIELD)
		private static @interface SoundName {
			String value();
		}

		@SoundName("jar_break")
		public static final SoundEvent jar_break = null;

		@SoundName("jar_step")
		public static final SoundEvent jar_step = null;

		@SoundName("jar_place")
		public static final SoundEvent jar_place = null;

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
