package net.arcanamod.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.arcanamod.ArcanaConfig;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.client.settings.SliderPercentageOption;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import static net.minecraft.util.text.ITextComponent.getTextComponentOrEmpty;

public final class ConfigScreen extends Screen {

    private static final int TITLE_HEIGHT = 8;

    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 20;
    private static final int DONE_BUTTON_TOP_OFFSET = 26;

    private final Screen parentScreen;

    private OptionsRowList optionsRowList;

    public ConfigScreen(Screen parentScreen) {
        super(new StringTextComponent("Arcana Config"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        super.init();
        this.optionsRowList = new OptionsRowList(
                this.minecraft, this.width, this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );

        this.optionsRowList.addOption(new SliderPercentageOption("config.client.custom_book_width",
                -1.0f, 200.0f, 1.0f,
                (gs) -> ArcanaConfig.CUSTOM_BOOK_WIDTH.get().doubleValue(),
                (gs, value) -> ArcanaConfig.CUSTOM_BOOK_WIDTH.set(value.intValue()),
                (gs, value) -> getTextComponentOrEmpty(I18n.format("config.client.custom_book_width")
                        + ": " + (value.get(gs) < 0 ? I18n.format("config.client.auto") : String.format("%.1f", value.get(gs))))
        ));

        this.optionsRowList.addOption(new SliderPercentageOption("config.client.custom_book_height",
                -1.0f, 200.0f, 1.0f,
                (gs) -> ArcanaConfig.CUSTOM_BOOK_HEIGHT.get().doubleValue(),
                (gs, value) -> ArcanaConfig.CUSTOM_BOOK_HEIGHT.set(value.intValue()),
                (gs, value) -> getTextComponentOrEmpty(I18n.format("config.client.custom_book_height")
                        + ": " + (value.get(gs) < 0 ? I18n.format("config.client.auto") : String.format("%.1f", value.get(gs))))
        ));

        this.optionsRowList.addOption(new SliderPercentageOption("config.client.book_text_scaling",
                0.5f, 2.0f, 0.1f,
                (gs) -> ArcanaConfig.BOOK_TEXT_SCALING.get(),
                (gs, value) -> ArcanaConfig.BOOK_TEXT_SCALING.set(value),
                (gs, value) -> getTextComponentOrEmpty(I18n.format("config.client.book_text_scaling")
                        + ": " + String.format("%.1f", value.get(gs)))
        ));

        this.optionsRowList.addOption(new SliderPercentageOption("config.client.wand_hud_x",
                0.0f, 16.0f, 1.0f,
                (gs) -> ArcanaConfig.WAND_HUD_X.get(),
                (gs, value) -> ArcanaConfig.WAND_HUD_X.set(value),
                (gs, value) -> getTextComponentOrEmpty(I18n.format("config.client.wand_hud_x")
                        + ": " + String.format("%.1f", value.get(gs)))
        ));

        this.optionsRowList.addOption(new SliderPercentageOption("config.client.wand_hud_y",
                0.0f, 16.0f, 1.0f,
                (gs) -> ArcanaConfig.WAND_HUD_Y.get(),
                (gs, value) -> ArcanaConfig.WAND_HUD_Y.set(value),
                (gs, value) -> getTextComponentOrEmpty(I18n.format("config.client.wand_hud_y")
                        + ": " + String.format("%.1f", value.get(gs)))
        ));

        this.optionsRowList.addOption(new SliderPercentageOption("config.client.wand_hud_scaling",
                0.5f, 2.0f, 0.1f,
                (gs) -> ArcanaConfig.WAND_HUD_SCALING.get(),
                (gs, value) -> ArcanaConfig.WAND_HUD_SCALING.set(value),
                (gs, value) -> getTextComponentOrEmpty(I18n.format("config.client.wand_hud_scaling")
                        + ": " + String.format("%.1f", value.get(gs)))
        ));

        this.optionsRowList.addOption(new BooleanOption("config.client.wand_hud_left",
                (gs) -> ArcanaConfig.WAND_HUD_LEFT.get(),
                (gs, value) -> ArcanaConfig.WAND_HUD_LEFT.set(value)
        ));

        this.optionsRowList.addOption(new BooleanOption("config.client.wand_hud_top",
                (gs) -> ArcanaConfig.WAND_HUD_TOP.get(),
                (gs, value) -> ArcanaConfig.WAND_HUD_TOP.set(value)
        ));

        this.optionsRowList.addOption(new BooleanOption("config.client.block_huds_top",
                (gs) -> ArcanaConfig.BLOCK_HUDS_TOP.get(),
                (gs, value) -> ArcanaConfig.BLOCK_HUDS_TOP.set(value)
        ));

        this.optionsRowList.addOption(new SliderPercentageOption("config.client.jar_animation_speed",
                0.5f, 2.0f, 0.1f,
                (gs) -> ArcanaConfig.JAR_ANIMATION_SPEED.get(),
                (gs, value) -> ArcanaConfig.JAR_ANIMATION_SPEED.set((double)(Math.round(value * 10)) / 10),
                (gs, value) -> getTextComponentOrEmpty(I18n.format("config.client.jar_animation_speed")
                        + ": " + String.format("%.1f", value.get(gs)))
        ));

        this.optionsRowList.addOption(new BooleanOption("config.client.no_jar_animation",
                (gs) -> ArcanaConfig.NO_JAR_ANIMATION.get(),
                (gs, value) -> ArcanaConfig.NO_JAR_ANIMATION.set(value)
        ));

        // Allow above options to be manipulated
        this.children.add(this.optionsRowList);

        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                getTextComponentOrEmpty(I18n.format("gui.done")),
                button -> this.onClose()
        ));
    }

    @Override
    public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        this.optionsRowList.render(stack, mouseX, mouseY, partialTicks);
        drawCenteredString(stack, this.font, this.title.getString(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        super.render(stack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        this.minecraft.displayGuiScreen(this.parentScreen);
    }
}
