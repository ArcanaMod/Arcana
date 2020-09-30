package net.arcanamod.systems.spell;

import net.arcanamod.items.WandItem;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class SpellNotBuiltError extends Exception {
    public void throwException(PlayerEntity player){
        CrashReport crashreport = CrashReport.makeCrashReport(this, "Exception when spell was used");
        CrashReportCategory crashreportcategory = crashreport.makeCategory("Spell used");
        crashreportcategory.addDetail("Spell info", () -> {
            try {
                ISpell spell = WandItem.getFocus(player.getHeldItem(Hand.MAIN_HAND)).getSpell(player.getHeldItem(Hand.MAIN_HAND));
                return String.format("Aspect ID: %s with data: %s",spell.getSpellAspect().getId(),spell.getSpellData().toString());
            } catch (Throwable var2) {
                return "Spell data isn't valid!";
            }
        });
        throw new ReportedException(crashreport);
    }
}
