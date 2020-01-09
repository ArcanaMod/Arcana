package net.kineticdevelopment.arcana.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
	public static final String MODID = "arcana";
	public static final String NAME = "Arcana";
	public static final String VERSION = "1.0";
	
	public static final Logger logger = LogManager.getLogger("Arcana");
	
	@SidedProxy(clientSide="net.kineticdevelopment.arcana.core.ClientProxy", serverSide="net.kineticdevelopment.arcana.core.CommonProxy")
    public static CommonProxy proxy;
}