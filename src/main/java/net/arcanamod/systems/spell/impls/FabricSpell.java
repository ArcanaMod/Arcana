package net.arcanamod.systems.spell.impls;

import net.arcanamod.ArcanaVariables;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.blocks.tiles.ResearchTableTileEntity;
import net.arcanamod.systems.spell.ISpell;
import net.arcanamod.systems.spell.SpellCosts;
import net.arcanamod.systems.spell.SpellData;
import net.arcanamod.systems.spell.SpellNotBuiltError;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;

public class FabricSpell extends Spell {

	private SpellData data;

	public boolean isBuilt = false;

	@Override
	public ISpell build(SpellData data, CompoundNBT compound) {
		this.data = data;
		isBuilt = true;
		return this;
	}

	@Override
	public ResourceLocation getId() {
		return ArcanaVariables.arcLoc("fabric");
	}

	@Override
	public Aspect getSpellAspect() {
		return Aspects.FABRIC;
	}

	@Override
	public SpellData getSpellData() {
		return data;
	}

	@Override
	public SpellCosts getSpellCosts() {
		return new SpellCosts(0,0,0,0,0,0,1);
	}

	/**
	 * How spell is complex to use / create
	 *
	 * @return returns spell complexity.
	 */
	@Override
	public int getComplexity() {
		return -666;
	}

	@Override
	public int getSpellDuration() {
		return 0;
	}

	@Override
	public ActionResultType useOnEntity(PlayerEntity caster, Entity target) {
		target.sendMessage(new StringTextComponent("MCP is broken everyone shold use Yarn"));
		target.sendMessage(new StringTextComponent(target.getName().getString()+" gets gold award on r/minecraft"));
		target.sendMessage(new StringTextComponent(target.getName().getString()+" gets gold award on r/minecraft"));
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType useOnBlock(PlayerEntity caster, World world, BlockPos blockTarget) {
		TileEntity tileentity = new ResearchTableTileEntity();
		Throwable throwable = new SpellNotBuiltError();
		CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking block entity");
		CrashReportCategory crashreportcategory = crashreport.makeCategory("Block entity being ticked");
		tileentity.addInfoToCrashReport(crashreportcategory);
		if (net.minecraftforge.common.ForgeConfig.SERVER.removeErroringTileEntities.get()) {
			LogManager.getLogger().fatal("{}", crashreport.getCompleteReport());
			//tileentity.remove();
			//this.removeTileEntity(tileentity.getPos());
		} else
			throw new ReportedException(crashreport);
		return ActionResultType.SUCCESS;
	}

	@Override
	public ActionResultType useOnPlayer(PlayerEntity playerTarget) {
		playerTarget.sendMessage(new StringTextComponent("MCP is broken everyone shold use Yarn"));
		playerTarget.sendMessage(new StringTextComponent(playerTarget.getName().getString()+" gets gold award on r/minecraft"));
		playerTarget.sendMessage(new StringTextComponent(playerTarget.getName().getString()+" gets gold award on r/minecraft"));
		return ActionResultType.SUCCESS;
	}
}
