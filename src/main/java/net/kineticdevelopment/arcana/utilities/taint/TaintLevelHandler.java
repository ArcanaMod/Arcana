package net.kineticdevelopment.arcana.utilities.taint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import net.kineticdevelopment.arcana.utilities.ArcanaFileUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Handles everything to do with the taint level
 * 
 * @author Atlas
 * @see TaintHandler
 */
public class TaintLevelHandler {

	/**
	 * Creates the taint level file for the world
	 * @param world
	 */
	public static void createTaintLevelFile(World world) {

		File dir = ArcanaFileUtils.getWorldDirectory(world);

		if (!world.isRemote) {

			if (Minecraft.getMinecraft().isSingleplayer()) {

				final File file = new File(dir, "Arcana/TaintLevel.taint");
				file.mkdirs();

				NBTTagCompound nbt;

				try {

					if (!file.exists()) {

						file.createNewFile();

						nbt = new NBTTagCompound();

						nbt.setFloat("TaintLevel", 0);

						try (FileOutputStream fileoutputstream = new FileOutputStream(file)) {

							CompressedStreamTools.writeCompressed(nbt, fileoutputstream);

						} catch (IOException e) {

							e.printStackTrace();

						}

					}

				}

				catch (IOException e) {

					e.printStackTrace();

				}

			}

		}

		if (world.isRemote) {

			final File file = new File(dir, "Arcana/TaintLevel.taint");
			file.mkdirs();

			NBTTagCompound nbt;

			try {

				if (!file.exists()) {

					file.createNewFile();

					nbt = new NBTTagCompound();

					nbt.setFloat("TaintLevel", 0);

					try (FileOutputStream fileoutputstream = new FileOutputStream(file)) {

						CompressedStreamTools.writeCompressed(nbt, fileoutputstream);

					} catch (IOException e) {

						e.printStackTrace();

					}

				}

			}

			catch (IOException e) {

				e.printStackTrace();

			}

		}

	}
	
	/**
	 * Retrieves the taint level of the world from the file
	 * @param world
	 * @return
	 */
	public static float getTaintLevel(World world) {

		File dir = ArcanaFileUtils.getWorldDirectory(world);

		if (!world.isRemote) {

			NBTTagCompound nbt;

			final File file = new File(dir, "Arcana/TaintLevel.taint");
			file.mkdirs();

			try {

				nbt = CompressedStreamTools.readCompressed(new FileInputStream(file));

				float returnvalue = nbt.getFloat("TaintLevel");

				return returnvalue;

			} catch (IOException e) {

				e.printStackTrace();

			}

			return 2147483647;

		}

		else {

			return 80082;

		}

	}
	
	/**
	 * Increases the world taint level
	 * @param world
	 * @param amount
	 */
	public static void increaseTaintLevel(World world, float amount) {

		if (world instanceof WorldClient) {

			return;

		}

		File dir = ArcanaFileUtils.getWorldDirectory(world);

		if (!world.isRemote) {

			final File file = new File(dir, "Arcana/TaintLevel.taint");
			file.mkdirs();

			NBTTagCompound nbt;

			try {

				if (!file.exists()) {

					file.createNewFile();

					nbt = new NBTTagCompound();

				}

				else {

					nbt = CompressedStreamTools.readCompressed(new FileInputStream(file));

				}

				nbt.setFloat("TaintLevel", getTaintLevel(world) + amount);

				System.out.println((getTaintLevel(world) + amount));

				try (FileOutputStream fileoutputstream = new FileOutputStream(file)) {

					CompressedStreamTools.writeCompressed(nbt, fileoutputstream);

				} catch (IOException e) {

					e.printStackTrace();

				}

			}

			catch (IOException e) {

				e.printStackTrace();

			}

		}

	}
	
	public static void setTaintLevel(World world, float amount) {

		if (world instanceof WorldClient) {

			return;

		}

		File dir = ArcanaFileUtils.getWorldDirectory(world);

		if (!world.isRemote) {

			final File file = new File(dir, "Arcana/TaintLevel.taint");
			file.mkdirs();

			NBTTagCompound nbt;

			try {

				if (!file.exists()) {

					file.createNewFile();

					nbt = new NBTTagCompound();

				}

				else {

					nbt = CompressedStreamTools.readCompressed(new FileInputStream(file));

				}

				nbt.setFloat("TaintLevel", amount);

				try (FileOutputStream fileoutputstream = new FileOutputStream(file)) {

					CompressedStreamTools.writeCompressed(nbt, fileoutputstream);

				} catch (IOException e) {

					e.printStackTrace();

				}

			}

			catch (IOException e) {

				e.printStackTrace();

			}

		}

	}
	
	/**
	 * Decreases the world taint level
	 * @param world
	 * @param amount
	 */
	public static void decreaseTaintLevel(World world, float amount) {

		File dir = ArcanaFileUtils.getWorldDirectory(world);

		if (!world.isRemote) {

			final File file = new File(dir, "Arcana/TaintLevel.taint");

			NBTTagCompound nbt;

			try {

				if (!file.exists()) {

					file.createNewFile();

					nbt = new NBTTagCompound();

				}

				else {

					nbt = CompressedStreamTools.readCompressed(new FileInputStream(file));

				}

				nbt.setFloat("TaintLevel", getTaintLevel(world) - amount);

				try (FileOutputStream fileoutputstream = new FileOutputStream(file)) {

					CompressedStreamTools.writeCompressed(nbt, fileoutputstream);

				} catch (IOException e) {

					e.printStackTrace();

				}

			}

			catch (IOException e) {

				e.printStackTrace();

			}

		}

	}

}
