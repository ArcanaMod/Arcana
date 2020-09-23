package net.arcanamod.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.arcanamod.aspects.*;
import net.arcanamod.systems.spell.CastAspect;
import net.arcanamod.systems.spell.ISpell;
import net.arcanamod.systems.spell.Spells;
import net.arcanamod.systems.spell.impls.Spell;
import net.minecraft.nbt.CompoundNBT;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	@Test
	public void SerializeAndDeserialize(){
		ISpell spell = Spell.deserializeNBT(Spell.serializeNBT(Spells.MINING_SPELL.build(
				Collections.emptyList(),
				Collections.singletonList(new CastAspect(Aspects.CHAOS,Aspects.GREED)),
				new CompoundNBT())));
		System.out.print(spell.getModAspects());
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
