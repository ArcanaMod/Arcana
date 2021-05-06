package net.arcanamod.client;

import net.arcanamod.client.gui.CompletePuzzleToast;
import net.arcanamod.client.gui.ResearchBookScreen;
import net.arcanamod.client.gui.ResearchEntryScreen;
import net.arcanamod.client.gui.ScribbledNoteScreen;
import net.arcanamod.event.ResearchEvent;
import net.arcanamod.systems.research.ResearchBooks;
import net.arcanamod.systems.research.ResearchEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.*;

import javax.annotation.Nullable;

public class ClientUtils {
    public static void openResearchBookUI(ResourceLocation book, Screen parentScreen, ItemStack sender){
        if(!ResearchBooks.disabled.contains(book))
            Minecraft.getInstance().displayGuiScreen(new ResearchBookScreen(ResearchBooks.books.get(book), parentScreen, sender));
        else
            Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("message.arcana.disabled").setStyle(Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED))), Util.DUMMY_UUID);
    }

    public static void openScribbledNotesUI(){
        Minecraft.getInstance().displayGuiScreen(new ScribbledNoteScreen(new StringTextComponent("")));
    }

    public static void displayPuzzleToast(@Nullable ResearchEntry entry){
        Minecraft.getInstance().getToastGui().add(new CompletePuzzleToast(entry));
    }

    public static void onResearchChange(ResearchEvent event){
        if(Minecraft.getInstance().currentScreen instanceof ResearchEntryScreen)
            ((ResearchEntryScreen)Minecraft.getInstance().currentScreen).updateButtons();
    }
}
