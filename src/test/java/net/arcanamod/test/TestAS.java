package net.arcanamod.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.arcanamod.aspects.*;
import net.arcanamod.systems.spell.Spell;
import net.arcanamod.systems.spell.casts.Cast;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.circle.DoubleModifierCircle;
import net.arcanamod.systems.spell.modules.core.CastCircle;
import net.arcanamod.systems.spell.modules.core.CastMethod;
import net.arcanamod.systems.spell.modules.core.Connector;
import net.arcanamod.systems.spell.modules.core.StartCircle;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestAS {

	@Test
	public void vertexTest(){
		int[] v = spreadVertices(6);
		System.out.println(v);
	}

	private int[] spreadVertices(int amount){
		int[] r = new int[amount];
		for (int i = 0; i < amount; i++){
			r[i] = i-(amount/2);
		}
		return r;
	}

	private Spell createBasicSpell(){
		Spell spell = new Spell();
		Connector startToCastMethod_connector = new Connector();
		Connector castMethodToCastCircle_connector = new Connector();
		DoubleModifierCircle doubleModifierCircle = new DoubleModifierCircle();
		CastCircle castCircle = new CastCircle();
		doubleModifierCircle.firstAspect = Aspects.AIR;
		doubleModifierCircle.secondAspect = Aspects.FIRE;
		castCircle.aspect = Aspects.MINING;
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

	private SpellModule rUnbound(SpellModule toUnbound, List<Aspect> castMethodsAspects) {
		if (toUnbound.getBoundedModules().size() > 0){
			for (SpellModule module : toUnbound.getBoundedModules()) {
				if (module instanceof CastMethod)
					castMethodsAspects.add(((CastMethod) module).aspect);
				return rUnbound(module, castMethodsAspects);
			}
		}else{
			Cast
		}
		return null;
	}

	@Test
	public void testAndCreateBasicSpell(){
		Assert.assertNotNull(createBasicSpell());
	}

	@Test
	public void basicSpellToFunction(){
		Spell spell = createBasicSpell();
		for (SpellModule module : spell.mainModule.getBoundedModules()) {
			rUnbound(module, new ArrayList<>());
		}
	}

	@Test
	public void TestInsertedBattery()
	{
		AspectBattery battery = new AspectBattery(1, 8);
		battery.createCell(new AspectCell(8));
		Assert.assertNotNull(battery);
		battery.insert(0,new AspectStack(Aspects.EXCHANGE,16),false);
		System.out.println(battery.toString());
		Assert.assertNotNull(battery.getHolder(0));
		Assert.assertTrue(battery.getHolder(0).getCurrentVis()==8);
	}

	@Test
	public void TestDrainedBattery()
	{
		AspectBattery battery = new AspectBattery(1, 8);
		battery.createCell(new AspectCell(8));
		Assert.assertNotNull(battery);
		battery.insert(0,new AspectStack(Aspects.EXCHANGE,16),false);
		battery.drain(0,new AspectStack(Aspects.EXCHANGE,4),false);
		System.out.println(battery.toString());
		Assert.assertNotNull(battery.getHolder(0));
		Assert.assertTrue(battery.getHolder(0).getCurrentVis()==4);
	}

	@Test
	public void TestBatteryToString()
	{
		AspectBattery battery = new AspectBattery(3, 8);
		AspectCell cell = new AspectCell();
		cell.insert(new AspectStack(Aspects.ENDER,80),false);
		battery.createCell(cell);
		System.out.println(battery.toString());
		System.out.print(this.aspectHandlerToJson(battery));
	}

	public String aspectHandlerToJson(IAspectHandler handler) {
		Gson gson = new GsonBuilder()
				.setPrettyPrinting()
				.create();
		return gson.toJson(handler);
	}
}
