package net.arcanamod.systems.spell;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.client.gui.FociForgeScreen;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.StartSpellModule;
import net.arcanamod.systems.spell.modules.core.Connector;
import net.arcanamod.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/// HEAVY IN PROGRESS
/// This and FociForgeScreen are in progress
/// missteps and bad design are prominent and will be fixed
public class SpellState {

    public static final int NO_MODULE = -1;
    private static final ResourceLocation SPELL_RESOURCES = new ResourceLocation(Arcana.MODID, "textures/gui/container/foci_forge_minigame.png");

    public static final int TRAY_X = 9 - FociForgeScreen.SPELL_X;
    public static final int TRAY_Y = 169 - FociForgeScreen.SPELL_Y;
    public static final int TRAY_DELTA = 35;
    public static final int TRAY_SIZE = 32;

    private static final float MIN_BOUND = -2048;
    private static final float MAX_BOUND = 2047;


    private float x = 0;
    private float y = 0;
    private boolean dragging = false;
    public boolean active = false;
    public SpellModule mainModule = null;
    public SpellModule activeModule = null; // unused on server
    public List<SpellModule> isolated = new LinkedList<>();
    public Map<SpellModule, UUID> floating = new HashMap<>(); // managed by server

    private void move(float x, float y) {
        this.x = Math.min(Math.max(MIN_BOUND, this.x + x), MAX_BOUND);
        this.y = Math.min(Math.max(MIN_BOUND, this.y + y), MAX_BOUND);
    }

    // Return x mod 16 where -16 <= ret <= 0
    private static float getTopLeftBackgroundLocation(float val) {
        if (val < 0) {
            return val % 16;
        } else {
            return (val % 16) - 16;
        }
    }

    public void selectModule(int i) {
        Class<? extends SpellModule> Module = SpellModule.byIndex.get(i);
        if (Module == null) {
            // deselection logic
            activeModule = null;
        } else {
            if ((mainModule == null && !Module.isAssignableFrom(StartSpellModule.class))
                || mainModule != null && Module.isAssignableFrom(StartSpellModule.class)) {

                try {
                    activeModule = Module.getConstructor().newInstance();
                    activeModule.unplaced = true;
                } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Called on mouse down anywhere in the gui
    public void mouseDown(int x, int y, int button, Aspect currentAspect) {
        // What you can do with mouse-down:
        // - initiate a module placement (or not)
        // - raise a module
        if (activeModule != null) {
            boolean set = activeModule.mouseDown((int)x, (int)y);
            if (set) {

            }
        }
    }

    // Called when mouse down starts in the gui and mouse is dragged
    public void drag(int x, int y, int button, double move_x, double move_y) {
        // TODO: delegate some functionality to the module
        dragging = true;
        if (activeModule == null) {
            move((float)move_x, (float)move_y);
        }
    }

    // Called on mouse up anywhere in the gui
    public void mouseUp(int x, int y, int button, Aspect currentAspect) {
        // What you can do with mouse-up:
        // - select a new module
        // - assign an aspect to a module
        // - mark connection point and connect modules
        // - place a new module
        // - raise a module
        // - lower a floated module
        // - delete a module (drop outside of board)

        // select a new module (clientside)
        int moduleCount = SpellModule.byIndex.size();
        if ((activeModule == null || activeModule.unplaced)
                && x >= TRAY_X && x <= TRAY_X + (moduleCount - 1) * TRAY_DELTA + TRAY_SIZE
                && y >= TRAY_Y && y <= TRAY_Y + TRAY_SIZE) {
            for (int i = 0; i < moduleCount; i++) {
                if (x >= TRAY_X + i * TRAY_DELTA && x <= TRAY_X + i * TRAY_DELTA + TRAY_SIZE) {
                    selectModule(i);
                    break;
                }
            }
        } else if (x >= 0 && x <= FociForgeScreen.SPELL_WIDTH && y >= 0 && y <= FociForgeScreen.SPELL_HEIGHT) {
            SpellModule under = getModuleAt(x, y);
            // assign aspect
            if (currentAspect != null) {
                assign(x, y, currentAspect, true);
            } else if (activeModule != null) {
                // mark connection
                // TODO: Should this be hardcoded? Connections aren't truly a module
                if (activeModule instanceof Connector) {
                    if (under != null) {
                        Connector connector = (Connector) activeModule;
                        if (!connector.startMarked) {
                            activeModule.x = under.x;
                            activeModule.y = under.y;
                            connector.startMarked = true;
                        } else {
                            connect(getModuleAt(activeModule.x, activeModule.y), under, true);
                            activeModule = null;
                        }
                    }
                // place module
                } else if (activeModule.unplaced) {
                    place(x, y, activeModule, true);
                // lower module
                } else if (floating.containsKey(activeModule)) {
                    lower(activeModule.x, activeModule.y, x, y, true);
                }
            // raise module
            } else if (under != null) {
                raise(x, y, true);
            }
        } else if (activeModule != null && floating.containsKey(activeModule)) {
            delete(activeModule.x, activeModule.y, true);
        // select out of bounds, active module not raised. Just remove the active module.
        } else {
            activeModule = null;
        }
    }

    private Stream<SpellModule> getBoundRecurse(SpellModule module) {
        if (module == null) {
            return Stream.empty();
        } else {
            return Stream.concat(Stream.of(module), module.getBoundModules().stream());
        }
    }

    private Stream<SpellModule> getPlacedModules() {
        Stream<SpellModule> stream = getBoundRecurse(mainModule);
        for (SpellModule module : isolated) {
            stream = Stream.concat(stream, getBoundRecurse(module));
        }
        return stream;
    }

    @Nullable
    private SpellModule getModuleAt(int x, int y) {
        return getPlacedModules().filter(module -> module.withinBounds(x, y)).findFirst().orElse(null);
    }

    public Stream<SpellModule> getCollidingModules(int x, int y, SpellModule module) {
        return getPlacedModules()
            .filter(other -> module != other)
            .filter(other -> other.collidesWith(module));
    }

    private boolean canPlace(int x, int y, SpellModule module) {
        boolean valid = true;
        SpellModule collision = getCollidingModules(x, y, module).findFirst().orElse(null);
        if (collision != null) {
            List<SpellModule> special = getCollidingModules(x, y, module)
                .filter(collide -> module.getSpecialPoint(collide) == null)
                .limit(2)
                .collect(Collectors.toList());
            if (special.size() == 0) {
                valid = false;
            } else {
                SpellModule specialMain;
                if (special.size() == 1) {
                    specialMain = special.get(0);
                } else {
                    // ignore previous result: get module underneath cursor
                    specialMain = getModuleAt(x, y);
                    if (specialMain == null || !specialMain.canConnectSpecial(module)) {
                        valid = false;
                    }
                }

                // check if a potential special placement candidate exists
                if (valid) {
                    Point specialPoint = specialMain.getSpecialPoint(module);
                    List<SpellModule> specialCollisions = getCollidingModules(specialPoint.x, specialPoint.y, module)
                        .limit(2)
                        .collect(Collectors.toList());
                    if (specialCollisions.size() > 1
                        || (specialCollisions.size() == 1
                            && specialCollisions.get(0) != specialMain)) {
                        valid = false;
                    }
                }
            }
        }

        return valid;
    }

    public boolean isValidDeletion(SpellModule module) {
        return false;
    }

    public void place(int x, int y, SpellModule module, boolean isRemote) {
        if(module != null && canPlace(x, y, module)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
            }
            // place module
        }
    }

    public void raise(int x, int y, boolean isRemote) {
        SpellModule raising = getModuleAt(x, y);
        if (raising != null && raising.canRaise(this)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
            }
            // raise module
        }
    }

    public void lower(int x, int y, int newX, int newY, boolean isRemote) {
        SpellModule lowering = getModuleAt(x, y);

        if (lowering != null && canPlace(x, y, lowering)) {
            if (x == newX && y == newY) {
                // send action to server
                // manage client-side state
            }
            // lower module
        }
    }

    public void connect(SpellModule a, SpellModule b, boolean isRemote) {
        if (a != null && b != null && a.canConnect(b)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
            }
            // connect the modules
        }
    }

    public void delete(int x, int y, boolean isRemote) {
        SpellModule deleting = getModuleAt(x, y);
        if (isValidDeletion(deleting)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
            }
            // delete module
        }
    }

    public void assign(int x, int y, Aspect aspect, boolean isRemote) {
        SpellModule assigning = getModuleAt(x, y);
        if (assigning != null && assigning.canAssign(x, y, aspect)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
            }
            // assign aspect to module
        }
    }





    public void render(int guiLeft, int guiTop, int spellLeft, int spellTop, int width, int height, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();

        mc.getTextureManager().bindTexture(FociForgeScreen.BG);
        if (mainModule != null) {
            // Start point

            for (int i = 1; i < 9; i++) {
                UiUtil.drawModalRectWithCustomSizedTexture(
                        guiLeft + FociForgeScreen.SPELL_X + TRAY_X + TRAY_DELTA * i,
                        guiTop + FociForgeScreen.SPELL_Y + TRAY_Y,
                        32 * i, 313, 32, 32, 397, 397);
            }
        }

        mc.getTextureManager().bindTexture(SPELL_RESOURCES);
        // Render selected module under mouse cursor
        if (activeModule != null) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            activeModule.renderUnderMouse(mouseX, mouseY);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }

        GL11.glPushMatrix();
        // move 0, 0 to spell window
        GL11.glTranslatef(spellLeft, spellTop, 0);

        // Scissors test: In this section, rendering outside this window does nothing.
        double gui_scale = mc.getMainWindow().getGuiScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        // TODO: De-magic '85' because I don't know what it is
        GL11.glScissor((int)(gui_scale * spellLeft), (int)(gui_scale * (spellTop + 85)), (int)(gui_scale * width), (int)(gui_scale * height));

        // draw background
        int bg_texX = (mainModule == null ? 16 : 0);
        float start_x = getTopLeftBackgroundLocation(this.x);
        float start_y = getTopLeftBackgroundLocation(this.y);
        for (float bg_x = start_x; bg_x < width + 16; bg_x += 16) {
            for (float bg_y = start_y; bg_y < height + 16; bg_y += 16) {
                UiUtil.drawTexturedModalRect((int)Math.floor(bg_x), (int)Math.floor(bg_y), bg_texX, 0, 16, 16);
            }
        }

        Queue<Pair<SpellModule, SpellModule>> renderQueue = new LinkedList<>();
        if (mainModule != null) {
            renderQueue.add(new Pair<>(null, mainModule));
        }
        for (SpellModule floater : isolated) {
           renderQueue.add(new Pair<>(null, floater));
        }

        while(!renderQueue.isEmpty()) {
            Pair<SpellModule, SpellModule> next = renderQueue.remove();
            SpellModule root = next.getFirst();
            SpellModule base = next.getSecond();
            if (root != null) {
                // render connector
            }
            if (!floating.containsKey(base)) {
                base.renderInMinigame(mouseX, mouseY);
            } else if (floating.get(base) != mc.player.getUniqueID()) {
                GL11.glColor4f(1.0f, 1.0f, 1.0f, .75f);
                base.renderInMinigame(mouseX, mouseY);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            } else { // current player is floating module
                GL11.glColor4f(1.0f, 1.0f, 1.0f, .25f);
                base.renderInMinigame(mouseX, mouseY);
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            }

            for (SpellModule bound : base.getBoundModules()) {
                renderQueue.add(new Pair<>(base, bound));
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }

    public static SpellState fromNBT(CompoundNBT compound) {
        SpellState state = new SpellState();
        if (compound.contains("mainmodule")) {
            state.mainModule = SpellModule.fromNBT((CompoundNBT)compound.get("mainmodule"), 0);
        }
        if (compound.contains("isolated")) {
            ListNBT isolatedList = (ListNBT)compound.get("isolated");
            for (INBT iso : isolatedList) {
                state.isolated.add(SpellModule.fromNBT((CompoundNBT)iso, 0));
            }
        }
        if (compound.contains("floating")) {
            ListNBT floatingList = (ListNBT)compound.get("isolated");
            for (INBT flo : floatingList) {
                CompoundNBT floGet = (CompoundNBT)flo;
                if (floGet.contains("x") && floGet.contains("y") && floGet.contains("id")) {
                    int x = floGet.getInt("x");
                    int y = floGet.getInt("y");
                    UUID id = floGet.getUniqueId("id");
                    SpellModule found = state.getModuleAt(x, y);
                    if (found != null) {
                        state.floating.put(found, id);
                    }
                }
            }
        }
        return state;
    }

    public CompoundNBT toNBT(CompoundNBT compound) {
        ListNBT isolatedNBT = new ListNBT();
        for (SpellModule iso : isolated) {
            isolatedNBT.add(iso.toNBT(new CompoundNBT(), 0));
        }
        ListNBT floatingNBT = new ListNBT();
        for (Map.Entry<SpellModule, UUID> flo : floating.entrySet()) {
            CompoundNBT floNBT = new CompoundNBT();
            floNBT.putInt("x", flo.getKey().x);
            floNBT.putInt("y", flo.getKey().y);
            floNBT.putUniqueId("id", flo.getValue());
            floatingNBT.add(floNBT);
        }
        compound.put("mainmodule", mainModule.toNBT(new CompoundNBT(), 0));
        compound.put("isolated", isolatedNBT);
        compound.put("floating", floatingNBT);
        return compound;
    }
}
