package net.arcanamod.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.arcanamod.aspects.*;
import org.junit.Assert;
import org.junit.Test;

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
	public void createBasicSpell(){
		Cast
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
