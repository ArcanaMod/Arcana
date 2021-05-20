package net.arcanamod.client.research;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.ArcanaConfig;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.client.gui.ClientUiUtil;
import net.arcanamod.client.gui.ResearchEntryScreen;
import net.arcanamod.mixin.FontRendererAccessor;
import net.arcanamod.mixin.ModContainerAccessor;
import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.systems.research.ResearchEntry;
import net.arcanamod.systems.research.impls.StringSection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.fonts.Font;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class TextFormatter{
	
	private TextFormatter(){}
	
	private static final float TEXT_WIDTH = ResearchEntryScreen.PAGE_WIDTH / ResearchEntryScreen.TEXT_SCALING;
	
	public interface Span{
		
		void render(MatrixStack stack, int x, int y);
		
		float getWidth();
		
		float getHeight();
	}
	
	// TODO: text size, shadow
	public static class TextSpan implements Span{
		
		Style renderStyle;
		
		String text;
		IFormattableTextComponent formattedText;
		
		public TextSpan(String text, Style style){
			this.text = text;
			this.renderStyle = style;
			formattedText = new StringTextComponent(text).mergeStyle(renderStyle);
		}
		
		public void render(MatrixStack stack, int x, int y){
			Minecraft.getInstance().fontRenderer.drawText(stack, formattedText, x, y, renderStyle.getColor() != null ? renderStyle.getColor().getColor() : 0);
		}
		
		public float getWidth(){
			return width(text, renderStyle);
		}
		
		public float getHeight(){
			return 9;
		}
	}
	
	public static class AspectSpan implements Span{
		
		Aspect aspect;
		
		public AspectSpan(Aspect aspect){
			this.aspect = aspect;
		}
		
		public void render(MatrixStack stack, int x, int y){
			ClientUiUtil.renderAspect(stack, aspect, x, y);
		}
		
		public float getWidth(){
			return 16;
		}
		
		public float getHeight(){
			return 16;
		}
	}
	
	public interface Paragraph{
		
		void render(MatrixStack stack, int x, int y, float scale);
		
		float getHeight();
	}
	
	public static class SpanParagraph implements Paragraph{
		
		List<Span> spans;
		boolean centred;
		
		List<List<Span>> lines = new ArrayList<>();
		float height;
		
		public SpanParagraph(List<Span> spans, boolean centred){
			this.spans = spans;
			this.centred = centred;
			
			// put the spans into different lines and keep track of height
			lines.add(new ArrayList<>());
			int curLine = 0;
			float curLineWidth = 0;
			float curLineHeight = 0;
			for(int i = 0; i < spans.size(); i++){
				Span span = spans.get(i);
				if((curLineWidth + span.getWidth()) < TEXT_WIDTH){
					lines.get(curLine).add(span);
					curLineWidth += span.getWidth() + 5;
					curLineHeight = Math.max(curLineHeight, (span.getHeight() + 1));
				}else{
					curLine++;
					lines.add(new ArrayList<>());
					curLineWidth = 0;
					height += curLineHeight;
					curLineHeight = 0;
					if(span.getWidth() < TEXT_WIDTH)
						// make sure this span gets added to the next line instead
						i--;
					else
						lines.get(curLine).add(span);
				}
			}
		}
		
		public SpanParagraph(List<Span> spans){
			this(spans, false);
		}
		
		public void render(MatrixStack stack, int x, int y, float scale){
			float curY = 0;
			for(List<Span> line : lines){
				float curX = 0;
				// recaulculate width/height
				// maybe cache these in the future?
				float lineWidth = (float)line.stream().mapToDouble(value -> value.getWidth() + 2).sum();
				float lineHeight = (float)line.stream().mapToDouble(Span::getHeight).max().orElse(1);
				if(centred)
					curX = (TEXT_WIDTH - lineWidth) / 2;
				for(Span span : line){
					span.render(stack, (int)(x + curX), (int)(y + curY + (lineHeight - span.getHeight()) / 2));
					curX += span.getWidth() + 5;
				}
				curY += lineHeight;
			}
		}
		
		public float getHeight(){
			return height;
		}
	}
	
	public static float width(String str, Style style){
		return width(str, style, Minecraft.getInstance().fontRenderer);
	}
	
	public static float width(String str, Style style, FontRenderer fr){
		float ret = 0;
		Font font = ((FontRendererAccessor)fr).callGetFont(style.getFontId());
		boolean formatting = false;
		for(char c : str.toCharArray())
			if(c == '§')
				formatting = true;
			else if(!formatting)
				ret += font.func_238557_a_(c).getAdvance(style.getBold());
			else
				formatting = false;
		return ret;
	}
	
	public static List<Paragraph> compile(String in, @Nullable StringSection section){
		// split up by (\n\n)s
		String[] paragraphs = in.split("\n");
		List<Paragraph> ret = new ArrayList<>(paragraphs.length);
		for(String paragraph : paragraphs){
			List<Span> list = new ArrayList<>();
			// splits before { and after } and at spaces
			for(String s : paragraph.split("([ ]+)|(?=\\{)|(?<=})")){
				// if it begins with { and ends with }, its a formatting fragment
				if(s.startsWith("{") && s.endsWith("}")){
				
				}else
					list.add(new TextSpan(s, Style.EMPTY));
			}
			ret.add(new SpanParagraph(list));
		}
		return ret;
		/*return Arrays.stream(paragraphs)
				.map(paragraph -> {
					List<Span> list = new ArrayList<>();
					for(String str : paragraph.split(" +")){
						TextSpan span = new TextSpan(str, Style.EMPTY);
						list.add(span);
					}
					return new SpanParagraph(list);
				})
				.collect(Collectors.toList());*/
	}
	
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