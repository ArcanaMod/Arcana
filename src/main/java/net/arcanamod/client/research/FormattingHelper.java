package net.arcanamod.client.research;

import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.mixin.ModContainerAccessor;
import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.systems.research.ResearchEntry;
import net.arcanamod.systems.research.impls.StringSection;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormattingHelper{
	
	public static String process(String in, @Nullable StringSection section){
		// Formatted sections appear as such:
		//    {$config:arcana:General.MaxAlembicAir}
		// An open brace, a dollar sign, a formatting type, colon separated parameters, and a closing brace.
		// There's currently only config-formatted sections, but hey, might wanna extend that later.
		if(section != null && ArcanaConfig.ENTRY_TITLES.get()){
			ResearchEntry entry = ResearchBooks.getEntry(section.getEntry());
			if(entry.sections().get(0) == section)
				in = "{~c}" + TextFormatting.ITALIC + I18n.format(entry.name()) + TextFormatting.RESET + "{~sep}" + in;
		}
		if(in.contains("{$")){
			Pattern findBraces = Pattern.compile("(\\{\\$.*?})");
			Matcher braces = findBraces.matcher(in);
			while(braces.find()){
				String inlineSection = braces.group().substring(2, braces.group().length() - 1);
				String[] parts = inlineSection.split(":");
				String replaceWith = I18n.format("researchEntry.invalidInline", inlineSection);
				String name = parts[0];
				if(name.equals("config") && parts.length == 3)
					replaceWith = inlineConfig(parts[1], parts[2]);
				else if(name.equals("numOfAspects"))
					replaceWith = String.valueOf(Aspects.getWithoutEmpty().size());
				in = in.replace(braces.group(), replaceWith);
			}
		}
		return in;
	}
	
	public static String inlineConfig(String modid, String configName){
		// iterate through mod containers
		AtomicReference<String> ret = new AtomicReference<>(I18n.format("researchEntry.invalidConfig", modid, configName));
		ModList.get().forEachModContainer((s, container) -> {
			if(s.equals(modid))
				((ModContainerAccessor)container).getConfigs().forEach((type, config) -> {
					if(config.getConfigData().contains(configName))
						// Have to cast to integer
						// get() returns a T, as in "whatever you ask for"
						// Java assumes the char[] version of valueOf and dies trying to cast it
						ret.set(String.valueOf((Object)config.getConfigData().get(configName)));
				});
		});
		
		return ret.get();
	}
}