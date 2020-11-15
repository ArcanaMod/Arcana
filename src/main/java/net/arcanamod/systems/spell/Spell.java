package net.arcanamod.systems.spell;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.items.WandItem;
import net.arcanamod.systems.spell.casts.Cast;
import net.arcanamod.systems.spell.casts.Casts;
import net.arcanamod.systems.spell.casts.ICast;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.circle.DoubleModifierCircle;
import net.arcanamod.systems.spell.modules.core.*;
import net.arcanamod.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.arcanamod.util.Pair.of;
import static net.arcanamod.aspects.AspectUtils.deserializeAspect;

public class Spell implements ISpell {
	public SpellModule mainModule = null;

	public static Spell deserializeNBT(CompoundNBT compound){
		if (compound.contains("Spell")) {
			return null;
		} else return null;
	}

	public static CompoundNBT serializeNBT(Spell spell){
		CompoundNBT compound = new CompoundNBT();

		return compound;
	}

	public static class Serializer{
		private Object __;

		private Spell deserialized = null;
		private CompoundNBT serialized = null;

		public CompoundNBT serializeNBT(Spell spell, CompoundNBT compound){
			compound.put("spell",serialize(spell.mainModule, 0));
			return compound;
		}

		public Spell deserializeNBT(CompoundNBT compound){
			__ = deserialize(compound);
			return deserialized;
		}

		/*private SpellModule serialize(SpellModule toSerialize, CompoundNBT boundRef, int deepness) {
			ListNBT boundList = new ListNBT();
			for (SpellModule module : toSerialize.getBoundedModules()) {
				CompoundNBT moduleNBT = new CompoundNBT();
				moduleNBT.putString("name",module.getName());
				moduleNBT.put("data",module.toNBT());
				moduleNBT.put("bound",boundRef);
				return serialize(module,moduleNBT,++deepness);
			}
			serialized = boundRef;
			return null;
		}*/

		private CompoundNBT serialize(SpellModule toSerialize, int deepness) {
			CompoundNBT moduleNBT = new CompoundNBT();
			ListNBT boundList = new ListNBT();
			for (SpellModule module : toSerialize.getBoundedModules()) {
				moduleNBT.putString("name",module.getName());
				moduleNBT.put("data",module.toNBT());
				boundList.add(serialize(module,++deepness));
			}
			moduleNBT.put("bound",boundList);
			return moduleNBT;
		}

		private Spell deserialize(CompoundNBT compound) {

			return null;
		}
	}

	public static void runSpell(Spell spell, PlayerEntity caster, Object sender, ICast.Action action){
		for (SpellModule module : spell.mainModule.getBoundedModules()) {
			runSpellModule(module, caster, sender, action, new ArrayList<>(),new ArrayList<>());
		}
	}

	private static SpellModule runSpellModule(SpellModule toUnbound, PlayerEntity caster, Object sender, ICast.Action action,
											  List<Pair<Aspect,Aspect>> castMethodsAspects, List<ICast> casts) {
		if (toUnbound.getBoundedModules().size() > 0){
			for (SpellModule module : toUnbound.getBoundedModules()) {
				if (module instanceof CastMethod)
					castMethodsAspects.add(of(((CastMethod) module).aspect,Aspects.EMPTY));
				else if (module instanceof CastMethodSin) {
					castMethodsAspects.get(castMethodsAspects.size()-1).setSecond(((CastMethodSin) module).aspect);
				} else if (module instanceof CastCircle)
					casts.add(((CastCircle) module).cast);
				return runSpellModule(module, caster, sender, action, castMethodsAspects, casts);
			}
		}else{
			for (ICast cast : casts){
				for (Pair<Aspect,Aspect> castMethodsAspect : castMethodsAspects)
				cast.use(caster,sender,castMethodsAspect,action);
			}
		}
		return null;
	}

	public static void updateSpellStatusBar(PlayerEntity player){
		if (player.getHeldItem(Hand.MAIN_HAND).getItem() instanceof WandItem) {
			if (WandItem.getFocus(player.getHeldItem(Hand.MAIN_HAND)) != null) {
				if (WandItem.getFocus(player.getHeldItem(Hand.MAIN_HAND)).getSpell(player.getHeldItem(Hand.MAIN_HAND))!=null) {
					for (SpellModule module : WandItem.getFocus(player.getHeldItem(Hand.MAIN_HAND)).getSpell(player.getHeldItem(Hand.MAIN_HAND)).mainModule.getBoundedModules()) {
						updateSpellStatusBarRecursive(module, player, new ArrayList<>());
					}
				}
			}
		}
	}

	private static SpellModule updateSpellStatusBarRecursive(SpellModule toUnbound, PlayerEntity player,
											  List<Pair<Aspect,Aspect>> castMethodsAspects) {
		if (toUnbound.getBoundedModules().size() > 0){
			for (SpellModule module : toUnbound.getBoundedModules()) {
				if (module instanceof CastMethod)
					castMethodsAspects.add(of(((CastMethod) module).aspect, Aspects.EMPTY));
				else if (module instanceof CastMethodSin) {
					castMethodsAspects.get(castMethodsAspects.size() - 1).setSecond(((CastMethodSin) module).aspect);
					return updateSpellStatusBarRecursive(module, player, castMethodsAspects);
				}
			}
		}else{
			for (Pair<Aspect,Aspect> castMethodsAspect : castMethodsAspects)
			if (castMethodsAspect.getFirst() == Aspects.EARTH && castMethodsAspect.getSecond() == Aspects.LUST)
				if (player.isCrouching()) {
					player.sendStatusMessage(new TranslationTextComponent("status.arcana.selection_mode"), true);
				} else {
					player.sendStatusMessage(new TranslationTextComponent("status.arcana.break_mode"), true);
				}
		}
		return null;
	}

	// Example of spell
	public static Spell createBasicSpell(){
		Spell spell = new Spell();
		Connector startToCastMethod_connector = new Connector();
		Connector castMethodToCastCircle_connector = new Connector();
		DoubleModifierCircle doubleModifierCircle = new DoubleModifierCircle();
		CastCircle castCircle = new CastCircle();
		doubleModifierCircle.firstAspect = Aspects.AIR;
		doubleModifierCircle.secondAspect = Aspects.FIRE;
		castCircle.cast = Casts.MINING_SPELL.build(new CompoundNBT());
		castCircle.bindModule(doubleModifierCircle);
		castMethodToCastCircle_connector.bindModule(castCircle);
		CastMethod castMethod = new CastMethod();
		castMethod.aspect = Aspects.EARTH;
		castMethod.bindModule(castMethodToCastCircle_connector);
		startToCastMethod_connector.bindModule(castMethod);
		spell.mainModule = new StartCircle();
		spell.mainModule.bindModule(startToCastMethod_connector);
		return spell;
	}

	// Example of spell
	public static Spell createAdvancedSpell(){
		Spell spell = new Spell();
		Connector startToCastMethod_connector = new Connector();
		Connector castMethodToCastCircle0_connector = new Connector();
		Connector castMethodToCastCircle1_connector = new Connector();
		DoubleModifierCircle doubleModifierCircle = new DoubleModifierCircle();
		CastCircle castCircle0 = new CastCircle();
		CastCircle castCircle1 = new CastCircle();
		castMethodToCastCircle1_connector.bindModule(castCircle1);
		doubleModifierCircle.firstAspect = Aspects.AIR;
		doubleModifierCircle.secondAspect = Aspects.FIRE;
		castCircle0.cast = Casts.MINING_SPELL.build(new CompoundNBT());
		castCircle0.bindModule(doubleModifierCircle);
		castCircle1.cast = Casts.FABRIC_SPELL;
		castMethodToCastCircle0_connector.bindModule(castCircle0);
		CastMethod castMethod = new CastMethod();
		castMethod.aspect = Aspects.EARTH;
		castMethod.bindModule(castMethodToCastCircle0_connector);
		castMethod.bindModule(castMethodToCastCircle1_connector);
		startToCastMethod_connector.bindModule(castMethod);
		spell.mainModule = new StartCircle();
		spell.mainModule.bindModule(startToCastMethod_connector);
		return spell;
	}

	/**
	 * Cost of spell in AspectStacks.
	 *
	 * @return returns cost of spell.
	 */
	@Override
	public SpellCosts getSpellCosts() {
		return new SpellCosts(0,0,0,1,0,0,0); // Temp
	}

	public Optional<ITextComponent> getName(CompoundNBT nbt){
		return Optional.empty(); // TODO: add tooltip
		//return Optional.of(new TranslationTextComponent("spell." + getId().getNamespace() + "." + getId().getPath()));
	}

	public Aspect getSpellColor() {
		return Aspects.AURA; // TODO: blend colors
	}
}