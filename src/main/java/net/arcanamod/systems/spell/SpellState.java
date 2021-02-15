package net.arcanamod.systems.spell;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.client.gui.FociForgeScreen;
import net.arcanamod.client.gui.UiUtil;
import net.arcanamod.network.Connection;
import net.arcanamod.network.PkFociForgeAction;
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
    private boolean blockDragging = false;

    public int sequence = 0;
    public boolean spellModified = false;
    public boolean active = false;
    public Spell currentSpell = null;
    public SpellModule activeModule = null; // unused on server
    public int activeModuleIndex = -1;
    public Set<SpellModule> isolated = new HashSet<>();
    public Map<SpellModule, UUID> floating = new HashMap<>(); // managed by server

    public void replaceSpell(Spell spell) {
        // ez pz copy function
        currentSpell = Spell.fromNBT(spell.toNBT(new CompoundNBT()));
        spellModified = false;
    }

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

    private SpellModule moduleFromIndex(int i) {
        SpellModule ret = null;
        Class<? extends SpellModule> Module = SpellModule.byIndex.get(i);
        if (Module != null) {
            try {
                ret = Module.getConstructor().newInstance();
            } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                e.printStackTrace();
            }
            if ((currentSpell.mainModule == null && !(ret instanceof StartSpellModule))
                || (currentSpell.mainModule != null && (ret instanceof StartSpellModule))) {
                ret = null;
            }
        }
        return ret;
    }

    public void selectModule(int i) {
        SpellModule newModule = moduleFromIndex(i);
        if (newModule != null) {
            newModule.unplaced = true;
        }
        activeModule = newModule;
        activeModuleIndex = i;
    }

    // Called on mouse down anywhere in the gui
    public void mouseDown(int x, int y, int button, Aspect currentAspect) {
        // What you can do with mouse-down:
        // - initiate a module placement (or not)
        // - raise a module
        if (activeModule != null) {
            blockDragging = activeModule.mouseDown(x, y);
        }
    }

    // Called when mouse down starts in the gui and mouse is dragged
    public void drag(int x, int y, int button, double move_x, double move_y) {
        // TODO: delegate some functionality to the module
        if (!blockDragging && activeModule == null) {
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
                boolean success = assign(x, y, currentAspect, true);
                if (success) {
                    currentAspect = Aspects.EMPTY;
                }
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
                            boolean success = connect(activeModule.x, activeModule.y, under.x, under.y, true);
                            if (success) {
                                activeModule = null;
                            }
                        }
                    }
                // place module
                } else if (activeModule.unplaced) {
                    boolean success = place(x, y, activeModuleIndex, true);
                    if (success) {
                        activeModule = null;
                    }
                // lower module
                } else if (floating.containsKey(activeModule)) {
                    boolean success = lower(activeModule.x, activeModule.y, x, y, Minecraft.getInstance().player.getUniqueID(), true);
                    if (success) {
                        activeModule = null;
                    }
                }
            // raise module
            } else if (under != null) {
                boolean success = raise(x, y, Minecraft.getInstance().player.getUniqueID(), true);
                if (success) {
                    activeModule = under;
                }
            }
        } else if (activeModule != null && floating.containsKey(activeModule)) {
            boolean success = delete(activeModule.x, activeModule.y, true);
            if (success) {
                activeModule = null;
            }
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
        Stream<SpellModule> stream = getBoundRecurse(currentSpell.mainModule);
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
                .filter(collide -> module.getSpecialPoint(collide) != null)
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

    public boolean place(int x, int y, int moduleIndex, boolean isRemote) {
        boolean result = false;
        SpellModule newModule = moduleFromIndex(moduleIndex);
        if (newModule != null && canPlace(x, y, newModule)) {
            if (isRemote) {
                Connection.sendFociForgeAction(Minecraft.getInstance().player.openContainer.windowId,
                        PkFociForgeAction.Type.PLACE,
                        x, y, moduleIndex, -1,
                        sequence,
                        Aspects.EMPTY);
                sequence++;
            }
            // place module
            newModule.unplaced = false;
            if (newModule instanceof StartSpellModule) {
                newModule.x = 0;
                newModule.y = 0;
                this.x = -x;
                this.y = -y;
                currentSpell.mainModule = newModule;
            } else {
                // TODO: Break this logic out of canPlace and call it separately
                SpellModule collision = getCollidingModules(x, y, newModule).findFirst().orElse(null);
                if (collision != null) {
                    List<SpellModule> special = getCollidingModules(x, y, newModule)
                            .filter(collide -> newModule.getSpecialPoint(collide) != null)
                            .limit(2)
                            .collect(Collectors.toList());
                    SpellModule specialMain;
                    // By the previous canPlace check, this will be valid.
                    if (special.size() == 1) {
                        specialMain = special.get(0);
                    } else {
                        specialMain = getModuleAt(x, y);
                    }
                    Point realPlacement = specialMain.getSpecialPoint(newModule);
                    x = realPlacement.x;
                    y = realPlacement.y;
                }
                newModule.x = x;
                newModule.y = y;
                isolated.add(newModule);
            }
            spellModified = true;
            result = true;
        }
        return result;
    }

    public boolean raise(int x, int y, UUID uuid, boolean isRemote) {
        boolean result = false;
        SpellModule raising = getModuleAt(x, y);
        if (raising != null && raising.canRaise(this)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
                Connection.sendFociForgeAction(Minecraft.getInstance().player.openContainer.windowId,
                        PkFociForgeAction.Type.RAISE,
                        x, y, -1, -1,
                        sequence,
                        Aspects.EMPTY);
                sequence++;
            }
            // raise module
            floating.put(raising, uuid);
            result = true;
        }
        return result;
    }

    public boolean lower(int from_x, int from_y, int to_x, int to_y, UUID uuid, boolean isRemote) {
        boolean result = false;
        SpellModule lowering = getModuleAt(from_x, from_y);
        UUID user = floating.get(lowering);
        if (lowering != null && user != null && user.equals(uuid) && canPlace(from_x, from_y, lowering)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
                Connection.sendFociForgeAction(Minecraft.getInstance().player.openContainer.windowId,
                        PkFociForgeAction.Type.LOWER,
                        from_x, from_y, to_x, to_y,
                        sequence,
                        Aspects.EMPTY);
                sequence++;
            }
            // lower module
            lowering.x = to_x;
            lowering.y = to_y;
            floating.remove(lowering);
            spellModified = true;
            result = true;
        }
        return result;
    }

    public boolean connect(int from_x, int from_y, int to_x, int to_y, boolean isRemote) {
        boolean result = false;
        SpellModule root = getModuleAt(from_x, from_y);
        SpellModule bound = getModuleAt(to_x, to_y);
        if (root != null && bound != null && root.canConnect(bound)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
                Connection.sendFociForgeAction(Minecraft.getInstance().player.openContainer.windowId,
                        PkFociForgeAction.Type.CONNECT,
                        from_x, from_y, to_x, to_y,
                        sequence,
                        Aspects.EMPTY);
                sequence++;
            }
            // connect the modules
            root.bindModule(bound);
            if (isolated.contains(bound)) {
                isolated.remove((bound));
            }
            spellModified = true;
            result = true;
        }
        return result;
    }

    public boolean delete(int x, int y, boolean isRemote) {
        boolean result = false;
        SpellModule deleting = getModuleAt(x, y);
        if (deleting != null && isValidDeletion(deleting)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
                Connection.sendFociForgeAction(Minecraft.getInstance().player.openContainer.windowId,
                        PkFociForgeAction.Type.DELETE,
                        x, y, -1, -1,
                        sequence,
                        Aspects.EMPTY);
                sequence++;
            }
            // delete module
            SpellModule parent = deleting.findParent(currentSpell.mainModule);
            if (parent == null) {
                for (SpellModule iso : isolated) {
                    parent = deleting.findParent(iso);
                    if (parent != null) {
                        break;
                    }
                }
            }
            if (parent != null) {
                parent.unbindModule(deleting);
            }
            for (SpellModule bound : deleting.getBoundModules()) {
                if (bound != null) {
                    deleting.unbindModule(bound);
                    isolated.add(bound);
                }
            }
            spellModified = true;
            result = true;
        }
        return result;
    }

    public boolean assign(int x, int y, Aspect aspect, boolean isRemote) {
        boolean result = false;
        SpellModule assigning = getModuleAt(x, y);
        if (assigning != null && assigning.canAssign(x, y, aspect)) {
            if (isRemote) {
                // send action to server
                // manage client-side state
                Connection.sendFociForgeAction(Minecraft.getInstance().player.openContainer.windowId,
                        PkFociForgeAction.Type.ASSIGN,
                        x, y, -1 ,-1,
                        sequence,
                        Aspects.EMPTY);
                sequence++;
            }
            // assign aspect to module
            assigning.assign(x, y, aspect);
            spellModified = true;
            result = true;
        }
        return result;
    }

    public void render(int guiLeft, int guiTop, int spellLeft, int spellTop, int width, int height, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();

        mc.getTextureManager().bindTexture(SPELL_RESOURCES);

        // Scissors test: In this section, rendering outside this window does nothing.
        double gui_scale = mc.getMainWindow().getGuiScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        // TODO: De-magic '85' because I don't know what it is
        GL11.glScissor((int)(gui_scale * spellLeft), (int)(gui_scale * (spellTop + 85)), (int)(gui_scale * width), (int)(gui_scale * height));

        GL11.glPushMatrix();
        // move 0, 0 to spell window
        GL11.glTranslatef(spellLeft, spellTop, 0);
        // draw background
        int bg_texX = (currentSpell == null ? 16 : 0);
        float start_x = getTopLeftBackgroundLocation(this.x);
        float start_y = getTopLeftBackgroundLocation(this.y);
        for (float bg_x = start_x; bg_x < width + 16; bg_x += 16) {
            for (float bg_y = start_y; bg_y < height + 16; bg_y += 16) {
                UiUtil.drawTexturedModalRect((int)Math.floor(bg_x), (int)Math.floor(bg_y), bg_texX, 0, 16, 16);
            }
        }

        // render according to background
        GL11.glTranslatef(x, y, 0);

        Queue<Pair<SpellModule, SpellModule>> renderQueue = new LinkedList<>();
        if (currentSpell.mainModule != null) {
            renderQueue.add(new Pair<>(null, currentSpell.mainModule));
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

        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        // Render selected module under mouse cursor
        if (activeModule != null) {
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
            activeModule.renderUnderMouse(mouseX, mouseY);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        }

        mc.getTextureManager().bindTexture(FociForgeScreen.BG);
        if (currentSpell != null) {
            // Start point

            for (int i = 0; i < 9; i++) {
                UiUtil.drawModalRectWithCustomSizedTexture(
                        guiLeft + FociForgeScreen.SPELL_X + TRAY_X + TRAY_DELTA * i,
                        guiTop + FociForgeScreen.SPELL_Y + TRAY_Y,
                        32 * i, 313, 32, 32, 397, 397);
            }
        }
    }

    public static SpellState fromNBT(CompoundNBT compound) {
        SpellState state = new SpellState();
        if (compound.contains("spell")) {
            state.currentSpell = Spell.fromNBT(compound);
        }
        if (compound.contains("isolated")) {
            ListNBT isolatedList = (ListNBT)compound.get("isolated");
            for (INBT iso : isolatedList) {
                state.isolated.add(SpellModule.fromNBTFull((CompoundNBT)iso, 0));
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
        if (compound.contains("modified")) {
            state.spellModified = compound.getBoolean("modified");
        }
        return state;
    }

    public CompoundNBT toNBT(CompoundNBT compound) {
        ListNBT isolatedNBT = new ListNBT();
        for (SpellModule iso : isolated) {
            isolatedNBT.add(iso.toNBTFull(new CompoundNBT(), 0));
        }
        ListNBT floatingNBT = new ListNBT();
        for (Map.Entry<SpellModule, UUID> flo : floating.entrySet()) {
            CompoundNBT floNBT = new CompoundNBT();
            floNBT.putInt("x", flo.getKey().x);
            floNBT.putInt("y", flo.getKey().y);
            floNBT.putUniqueId("id", flo.getValue());
            floatingNBT.add(floNBT);
        }
        if (currentSpell != null) {
            currentSpell.toNBT(compound);
        }
        compound.put("isolated", isolatedNBT);
        compound.put("floating", floatingNBT);
        compound.putBoolean("modified", spellModified);
        return compound;
    }
}
