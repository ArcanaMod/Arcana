package net.arcanamod.systems.spell;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.items.WandItem;
import net.arcanamod.systems.spell.casts.Casts;
import net.arcanamod.systems.spell.casts.ICast;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.circle.DoubleModifierCircle;
import net.arcanamod.systems.spell.modules.core.*;
import net.arcanamod.util.Pair;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static net.arcanamod.util.Pair.of;

/**
 * Spell is made of SpellModules that are bound together.
 */
public class Spell implements ISpell {
	public SpellModule mainModule = null;

	private static Logger logger = LogManager.getLogger();

	/**
	 * Create new Spell serializer that can be used to deserialize or serialize spell.
	 * @return Spell serializer.
	 */
	public static Serializer getSerializer() {
		return new Spell.Serializer();
	}

	public static class Serializer{
		/**
		 * Spell to Spell NBT
		 * @param spell Spell to NBT serialize
		 * @param compound Existing CompoundNBT or new.
		 * @return Serialized Spell
		 */
		public CompoundNBT serializeNBT(Spell spell, CompoundNBT compound){
			compound.put("spell",serialize(spell.mainModule, 0));
			return compound;
		}

		/**
		 * Spell NBT to Spell Object
		 * @param compound Spell NBT
		 * @return Deserialized Spell
		 */
		public Spell deserializeNBT(CompoundNBT compound){
			Spell spell = new Spell();
			spell.mainModule = new StartCircle();
			spell.mainModule.bindModule(deserialize(spell.mainModule, (CompoundNBT) Objects.requireNonNull(compound.get("spell")), 0));
			return spell;
		}

		private CompoundNBT serialize(SpellModule toSerialize, int deepness) {
			CompoundNBT moduleNBT = new CompoundNBT();
			ListNBT boundList = new ListNBT();
			for (SpellModule module : toSerialize.getBoundModules()) {
				moduleNBT.putString("name",module.getName());
				moduleNBT.put("data",module.toNBT());
				boundList.add(serialize(module,++deepness));
			}
			moduleNBT.put("bound",boundList);
			return moduleNBT;
		}

		private SpellModule deserialize(SpellModule toDeserialize, CompoundNBT spellNBT, int deepness) {
			SpellModule createdModule = null;
			if (spellNBT.getString("name")!=null&&spellNBT.getString("name")!="")
				 createdModule = SpellModule.fromNBT(spellNBT);

			if (spellNBT.get("bound") != null && createdModule != null) {
				for (INBT inbt : ((ListNBT) spellNBT.get("bound"))) {
					if (inbt instanceof CompoundNBT) {
						CompoundNBT compound = ((CompoundNBT) inbt);
						createdModule.bindModule(deserialize(toDeserialize, compound, ++deepness));
					}
				}
			}
			return createdModule;
		}
	}

	/**
	 * Run Spell.
	 * Goes trough all spell modules and executes {@link ICast}.
	 * @param spell Spell to run.
	 * @param caster Player that uses the Spell.
	 * @param sender {@link net.minecraft.item.ItemStack} that {@link net.minecraft.item.Item} extends {@link WandItem}
	 * @param action Spell use Action.
	 */
	public static void runSpell(Spell spell, PlayerEntity caster, Object sender, ICast.Action action){
		for (SpellModule module : spell.mainModule.getBoundModules()) {
			runSpellModule(module, caster, sender, action, new ArrayList<>(),new ArrayList<>());
		}
	}

	/**
	 * Run spell Recursion.
	 */
	private static SpellModule runSpellModule(SpellModule toUnbound, PlayerEntity caster, Object sender, ICast.Action action,
											  List<Pair<Aspect,Aspect>> castMethodsAspects, List<ICast> casts) {
		if (toUnbound.getBoundModules().size() > 0){
			for (SpellModule module : toUnbound.getBoundModules()) {
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
					for (SpellModule module : WandItem.getFocus(player.getHeldItem(Hand.MAIN_HAND)).getSpell(player.getHeldItem(Hand.MAIN_HAND)).mainModule.getBoundModules()) {
						updateSpellStatusBarRecursive(module, player, new ArrayList<>());
					}
				}
			}
		}
	}

	private static SpellModule updateSpellStatusBarRecursive(SpellModule toUnbound, PlayerEntity player,
											  List<Pair<Aspect,Aspect>> castMethodsAspects) {
		if (toUnbound.getBoundModules().size() > 0){
			for (SpellModule module : toUnbound.getBoundModules()) {
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

	/**
	 * Example spells.
	 */
	public static class Samples{
		/**
		 * Create example basic Spell that is used for testing.
		 * @return a basic Spell
		 */
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

		/**
		 * Create example advanced Spell that is used for testing.
		 * @return a basic Spell
		 */
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