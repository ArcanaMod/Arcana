package net.arcanamod.mixin;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.EnumMap;

@Mixin(ModContainer.class)
public interface ModContainerAccessor{
	
	// The target is a Forge class, and is as such not obfuscated and does not require remapping.
	// Enabling remapping just produces a compiler warning.
	@Accessor(remap = false)
	EnumMap<ModConfig.Type, ModConfig> getConfigs();
}