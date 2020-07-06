package net.arcanamod.test;

import net.arcanamod.aspects.Aspects;
import net.arcanamod.aspects.AspectBattery;
import net.arcanamod.aspects.AspectCell;
import net.arcanamod.aspects.AspectStack;
import org.junit.Assert;
import org.junit.Test;

public class TestAS {

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
}
