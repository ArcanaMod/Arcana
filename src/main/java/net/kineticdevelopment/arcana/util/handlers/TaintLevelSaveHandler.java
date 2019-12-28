package net.kineticdevelopment.arcana.util.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class TaintLevelSaveHandler
{
	
	public static void createTaintLevelFile(World world)
	{
		final File crate = new File("saves/"+world.getWorldInfo().getWorldName(), "taintLevel.txt");
		try(FileWriter fw = new FileWriter("saves/"+world.getWorldInfo().getWorldName()+"/taintLevel.txt", false); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw))
		{
			out.print("0");
		} 
		catch (IOException e) 
		{
			
	    }
	}
    
    public int getTaintLevel(World world)
    {
    	int taintLevel;
        try (BufferedReader br = Files.newBufferedReader(Paths.get("saves/"+world.getWorldInfo().getWorldName()+"/taintLevel.txt"))) 
        {
            String line;
            taintLevel = Integer.parseInt(br.readLine());
            return taintLevel;

        } 
        catch(IOException e) 
        {
            System.err.format("IOException: %s%n", e);
        }
        return 22603;
    }
    
    public static void increaseTaintLevel(World world, int amount)
    {
    	int taintLevel = 22603;
        try (BufferedReader br = Files.newBufferedReader(Paths.get("saves/"+world.getWorldInfo().getWorldName()+"/taintLevel.txt"))) 
        {
            String line;
            taintLevel = Integer.parseInt(br.readLine());
            taintLevel = taintLevel + amount;
            System.out.println("Taint Level is now "+taintLevel);
        } 
        catch(IOException e) 
        {
            System.err.format("IOException: %s%n", e);
        }
        
        final File crate = new File("saves/"+world.getWorldInfo().getWorldName(), "taintLevel.txt");
		try(FileWriter fw = new FileWriter("saves/"+world.getWorldInfo().getWorldName()+"/taintLevel.txt", false); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw))
		{
			out.print(taintLevel);
		} 
		catch (IOException e) 
		{
			
	    }
    }
}
