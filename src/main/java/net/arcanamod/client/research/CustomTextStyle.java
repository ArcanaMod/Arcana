package net.arcanamod.client.research;

public class CustomTextStyle{
	
	private int colour;
	private boolean bold, italics, underline, obfuscated, wavy, shadow, strikethrough, subscript, superscript;
	private float size;
	
	public static final CustomTextStyle EMPTY = new CustomTextStyle(0, false, false, false, false, false, 1, false, false, false, false);
	
	public CustomTextStyle(int colour, boolean bold, boolean italics, boolean underline, boolean obfuscated, boolean wavy, float size, boolean shadow, boolean strikethrough, boolean subscript, boolean superscript){
		this.colour = colour;
		this.bold = bold;
		this.italics = italics;
		this.underline = underline;
		this.obfuscated = obfuscated;
		this.wavy = wavy;
		this.size = size;
		this.shadow = shadow;
		this.strikethrough = strikethrough;
	}
	
	public CustomTextStyle(CustomTextStyle in){
		this(in.colour, in.bold, in.italics, in.underline, in.obfuscated, in.wavy, in.size, in.shadow, in.strikethrough, in.subscript, in.superscript);
	}
	
	public CustomTextStyle copy(){
		return new CustomTextStyle(this);
	}
	
	public int getColour(){
		return colour;
	}
	
	public boolean isBold(){
		return bold;
	}
	
	public boolean isItalics(){
		return italics;
	}
	
	public boolean isUnderline(){
		return underline;
	}
	
	public boolean isObfuscated(){
		return obfuscated;
	}
	
	public boolean isWavy(){
		return wavy;
	}
	
	public float getSize(){
		return size;
	}
	
	public CustomTextStyle setColour(int colour){
		this.colour = colour;
		return this;
	}
	
	public CustomTextStyle setBold(boolean bold){
		this.bold = bold;
		return this;
	}
	
	public CustomTextStyle setItalics(boolean italics){
		this.italics = italics;
		return this;
	}
	
	public CustomTextStyle setUnderline(boolean underline){
		this.underline = underline;
		return this;
	}
	
	public CustomTextStyle setObfuscated(boolean obfuscated){
		this.obfuscated = obfuscated;
		return this;
	}
	
	public CustomTextStyle setWavy(boolean wavy){
		this.wavy = wavy;
		return this;
	}
	
	public CustomTextStyle setSize(float size){
		this.size = size;
		return this;
	}
	
	public boolean isShadow(){
		return shadow;
	}
	
	public boolean isStrikethrough(){
		return strikethrough;
	}
	
	public CustomTextStyle setShadow(boolean shadow){
		this.shadow = shadow;
		return this;
	}
	
	public CustomTextStyle setStrikethrough(boolean strikethrough){
		this.strikethrough = strikethrough;
		return this;
	}
	
	public boolean isSubscript(){
		return subscript;
	}
	
	public boolean isSuperscript(){
		return superscript;
	}
	
	public CustomTextStyle setSubscript(boolean subscript){
		this.subscript = subscript;
		return this;
	}
	
	public CustomTextStyle setSuperscript(boolean superscript){
		this.superscript = superscript;
		return this;
	}
}