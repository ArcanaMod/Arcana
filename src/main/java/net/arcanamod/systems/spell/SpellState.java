package net.arcanamod.systems.spell;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.Aspects;
import net.arcanamod.client.gui.ClientUiUtil;
import net.arcanamod.client.gui.FociForgeScreen;
import net.arcanamod.network.Connection;
import net.arcanamod.network.PkFociForgeAction;
import net.arcanamod.systems.spell.modules.SpellModule;
import net.arcanamod.systems.spell.modules.core.Connector;
import net.arcanamod.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
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
    private boolean dragging = false;

    public int sequence = 0;
    public boolean spellModified = false;
    public boolean active = false;
    public Spell currentSpell = new Spell(null);
    public SpellModule activeModule = null; // unused on server
    public int activeModuleIndex = -1;
    public Set<SpellModule> isolated = new HashSet<>();
    public BiMap<SpellModule, UUID> floating = HashBiMap.create(); // managed by server

    public void replaceSpell(Spell spell) {
        // ez pz copy function
        currentSpell = Spell.fromNBT(spell.toNBT(new CompoundNBT()));
        spellModified = false;
    }

    private void move(float x, float y) {
        this.x = Math.min(Math.max(MIN_BOUND, this.x + x), MAX_BOUND);
        this.y = Math.min(Math.max(MIN_BOUND, this.y + y), MAX_BOUND);
        dragging = true;
    }

    // Return (imperfect) x mod 16 where -16 <= ret <= 0
    private static float getNegativeMod16(float val) {
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
            // if sun, no sun
            // if no sun, only sun
            if (ret != null && currentSpell != null
                && ((currentSpell.mainModule == null && !ret.isStartModule())
                    || (currentSpell.mainModule != null && ret.isStartModule()))) {
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
        } else {
            blockDragging = false;
        }
    }

    // Called when mouse down starts in the gui and mouse is dragged
    public void drag(int x, int y, int button, double move_x, double move_y) {
        // TODO: delegate some functionality to the module
        if (!blockDragging && activeModule == null) {
            move((float)move_x, (float)move_y);
            dragging = true;
        }
    }

    // Called on mouse up anywhere in the gui
    public boolean mouseUp(int x, int y, int button, Aspect currentAspect, ItemStack heldItem) {
        boolean success = false;
        int spellX = x - (int)this.x;
        int spellY = y - (int)this.y;
        // prevent interaction if holding an item
        if (heldItem.isEmpty()) {
            if (button == 0) {
                if (!dragging) {
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
                        success = true;
                    } else if (x >= 0 && x <= FociForgeScreen.SPELL_WIDTH && y >= 0 && y <= FociForgeScreen.SPELL_HEIGHT) {
                        SpellModule under = getModuleAt(spellX, spellY);
                        // assign aspect
                        if (currentAspect != null) {
                            success = assign(spellX, spellY, currentAspect, true);
                            if (success) {
                                currentAspect = Aspects.EMPTY;
                            }
                        } else if (activeModule != null) {
                            // mark connection
                            // TODO: Should this be hardcoded? Connections aren't truly a module
                            if (activeModule instanceof Connector) {
                                if (under != null) {
                                    Connector connector = (Connector) activeModule;
                                    if (!connector.startMarked && under.getConnectionStart() != null) {
                                        SpellModule realUnder = under.getConnectionStart();
                                        activeModule.x = realUnder.x;
                                        activeModule.y = realUnder.y;
                                        connector.startMarked = true;
                                        success = true;
                                    } else if (connector.startMarked && under.getConnectionEnd(false) != null) {
                                        success = connect(activeModule.x, activeModule.y, under.x, under.y, true);
                                        if (success) {
                                            activeModule = null;
                                        }
                                    }
                                }
                                // place module
                            } else if (activeModule.unplaced) {
                                success = place(spellX, spellY, activeModuleIndex, true);
                                if (success) {
                                    activeModule = null;
                                }
                                // lower module
                            } else if (floating.containsKey(activeModule)) {
                                success = lower(activeModule.x, activeModule.y, spellX, spellY, Minecraft.getInstance().player.getUniqueID(), true);
                                if (success) {
                                    activeModule = null;
                                }
                            }
                            // raise module
                        } else if (under != null) {
                            success = raise(spellX, spellY, Minecraft.getInstance().player.getUniqueID(), true);
                            if (success) {
                                activeModule = under;
                            }
                        }
                    } else if (activeModule != null && floating.containsKey(activeModule)) {
                        // delete module
                        success = delete(activeModule.x, activeModule.y, Minecraft.getInstance().player.getUniqueID(),true);
                        if (success) {
                            activeModule = null;
                        }
                        // select out of bounds, active module not raised. Just remove the active module.
                    } else {
                        activeModule = null;
                        success = false;
                    }
                }
                dragging = false;
                blockDragging = false;
            } else if (button == 1) {
                if (activeModule != null) {
                    if (activeModule.unplaced) {
                        // clear unplaced template
                        activeModule = null;
                        success = true;
                    } else {
                        // lower lifted module
                        success = lower(activeModule.x, activeModule.y, activeModule.x, activeModule.y, Minecraft.getInstance().player.getUniqueID(), true);
                        if (success) {
                            activeModule = null;
                        }
                    }
                } else {
                    // attempt to remove aspect from module
                    success = assign(spellX, spellY, Aspects.EMPTY, true);
                    // if something happened, don't touch held aspect
                    // if nothing happened, get rid of aspect
                    success = !success;
                }
            }
        }
        return success;
    }

    // Adds all bound modules recursively to a builder.
    // Guarantees no null values are added.
    private void addBoundRecurse(SpellModule module, @Nonnull Stream.Builder<SpellModule> builder) {
        if (module != null) {
            builder.add(module);
            for (SpellModule bound : module.bound) {
                addBoundRecurse(bound, builder);
            }
            for (SpellModule bound : module.boundSpecial) {
                addBoundRecurse(bound, builder);
            }
        }
    }

    private Stream<SpellModule> getPlacedModules() {
        Stream.Builder<SpellModule> builder = Stream.builder();
        // requires spell to be initialized
        if (currentSpell != null && currentSpell.mainModule != null) {
            addBoundRecurse(currentSpell.mainModule, builder);
            for (SpellModule module : isolated) {
                addBoundRecurse(module, builder);
            }
        }
        return builder.build();
    }

    @Nullable
    private SpellModule getModuleRaised(int x, int y, UUID uuid) {
        return floating.keySet().stream()
                .filter(module -> module.withinBounds(x, y))
                .filter(module -> floating.get(module) == uuid)
                .findFirst().orElse(null);
    }

    @Nullable
    private SpellModule getModuleAt(int x, int y) {
        return getPlacedModules()
                .filter(module -> module.withinBounds(x, y))
                .findFirst()
                .orElse(null);
    }

    public Stream<SpellModule> getCollidingModules(int x, int y, SpellModule module) {
        return getPlacedModules()
            .filter(other -> module != other)
            .filter(other -> other.collidesWith(x, y, module));
    }

    public Stream<SpellModule> getCollidingSpecialTails(int x, int y, SpellModule module) {
        Set<SpellModule> cores = new HashSet<>();
        getCollidingModules(x, y, module).forEach(collision -> {
            Queue<SpellModule> tail = new ArrayDeque<>();
            tail.add(collision);
            while (!tail.isEmpty()) {
                SpellModule next = tail.remove();
                if (next.canConnectSpecial(module)) {
                    cores.add(next);
                }
                if (next.parent != null && next.parent.boundSpecial.contains(next)) {
                    tail.add(next.parent);
                }
            }
        });
        return cores.stream();
    }

    private Pair<Point, SpellModule> getPlacementPoint(int x, int y, SpellModule module) {
        Point location = null;
        SpellModule specialParent = null;
        SpellModule collision = getCollidingModules(x, y, module).findFirst().orElse(null);
        if (collision == null) {
            location = new Point(x, y);
        } else {
            // Redo the check but search for special connections
            List<SpellModule> special = getCollidingSpecialTails(x, y, module)
                    .filter(collide -> collide.canConnectSpecial(module))
                    .limit(2)
                    .collect(Collectors.toList());
            if (special.size() > 0) {
                if (special.size() == 1) {
                    specialParent = special.get(0);
                } else {
                    // ignore previous result, get module underneath cursor
                    specialParent = getModuleAt(x, y);
                }

                // check if a potential special placement candidate exists
                if (specialParent != null) {
                    if (specialParent.canConnectSpecial(module)) {
                        Point specialPoint = specialParent.getSpecialPoint(module);
                        // Check if any modules collide here
                        List<SpellModule> specialCollisions = getCollidingSpecialTails(specialPoint.x, specialPoint.y, module)
                                .limit(2)
                                .collect(Collectors.toList());
                        if (specialCollisions.size() == 0
                                || (specialCollisions.size() == 1
                                    && specialCollisions.get(0) == specialParent)) {
                            location = specialPoint;
                        }
                    }
                }
            }
        }

        return new Pair<>(location, specialParent);
    }

    private boolean canPlace(int x, int y, SpellModule module) {
        Pair<Point, SpellModule> point = getPlacementPoint(x, y, module);
        return (point.getFirst() != null);
    }

    // Assumes module is raised
    public boolean canDelete(SpellModule module) {
        return true;
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
            if (newModule.isStartModule()) {
                newModule.x = 0;
                newModule.y = 0;
                this.x = FociForgeScreen.SPELL_WIDTH/2;
                this.y = FociForgeScreen.SPELL_HEIGHT/2;
                currentSpell.mainModule = newModule;
            } else {
                Pair<Point, SpellModule> placement = getPlacementPoint(x, y, newModule);
                Point realPlacement = placement.getFirst();
                SpellModule specialParent = placement.getSecond();
                newModule.x = realPlacement.x;
                newModule.y = realPlacement.y;
                if (specialParent == null) {
                    isolated.add(newModule);
                } else {
                    newModule.setParent(specialParent);
                }
            }
            spellModified = true;
            result = true;
        }
        return result;
    }

    public boolean raise(int x, int y, UUID uuid, boolean isRemote) {
        boolean result = false;
        SpellModule raising = getModuleAt(x, y);
        if (raising != null && !floating.containsValue(uuid) && raising.canRaise(this)) {
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
        SpellModule lowering = getModuleRaised(from_x, from_y, uuid);
        if (lowering != null && canPlace(to_x, to_y, lowering)) {
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
            Pair<Point, SpellModule> placement = getPlacementPoint(to_x, to_y, lowering);
            Point realPlacement = placement.getFirst();
            SpellModule specialParent = placement.getSecond();
            lowering.x = realPlacement.x;
            lowering.y = realPlacement.y;
            floating.remove(lowering);
            // new special connection and parent
            if (lowering.parent == null || lowering.parent.boundSpecial.contains(lowering)) {
                lowering.setParent(specialParent);
            }
            if (lowering.parent == null) {
                isolated.add(lowering);
            } else {
                isolated.remove(lowering);
            }
            spellModified = true;
            result = true;
        }
        return result;
    }

    public boolean connect(int from_x, int from_y, int to_x, int to_y, boolean isRemote) {
        boolean result = false;
        SpellModule root = getModuleAt(from_x, from_y);
        SpellModule bound = getModuleAt(to_x, to_y);
        if (root != null && bound != null && root.canConnect(bound, false)) {
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
            SpellModule start = root.getConnectionStart();
            SpellModule end = bound.getConnectionEnd(false);
            // connect the modules
            
            end.setParent(start);
            if (isolated.contains(end)) {
                isolated.remove((end));
            }
            spellModified = true;
            result = true;
        }
        return result;
    }

    public boolean delete(int x, int y, UUID uuid, boolean isRemote) {
        boolean result = false;
        SpellModule deleting = getModuleRaised(x, y, uuid);
        if (deleting != null && canDelete(deleting)) {
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
            if (deleting == currentSpell.mainModule) {
                // now you've gone and done it
                currentSpell.mainModule = null;
            } else if (isolated.contains(deleting)) {
                isolated.remove(deleting);
            }
            List<SpellModule> toUnbind = new ArrayList<>();
            toUnbind.addAll(deleting.bound);
            toUnbind.addAll(deleting.boundSpecial);
            
            for (SpellModule bound : toUnbind) {
                bound.setParent(null);
                isolated.add(bound);
            }
            floating.remove(deleting);
            deleting.setParent(null);
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
                        aspect);
                sequence++;
            }
            // assign aspect to module
            assigning.assign(x, y, aspect);
            spellModified = true;
            result = true;
        }
        return result;
    }

    public void exitGui() {
        UUID client = Arcana.proxy.getPlayerOnClient().getUniqueID();
        if (floating.containsValue(client)) {
            SpellModule raised = floating.inverse().get(client);
            lower(raised.x, raised.y, raised.x, raised.y, client, true);
        }
        activeModule = null;
    }

    public void render(MatrixStack stack, int guiLeft, int guiTop, int spellLeft, int spellTop, int width, int height, int mouseX, int mouseY) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bindTexture(SPELL_RESOURCES);
        RenderSystem.enableBlend();

        // Scissors test: In this section, rendering outside this window does nothing.
        double gui_scale = mc.getMainWindow().getGuiScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        // TODO: De-magic '85' because I don't know what it is
        GL11.glScissor((int)(gui_scale * spellLeft), (int)(gui_scale * (spellTop + 85)), (int)(gui_scale * width), (int)(gui_scale * height));
        RenderSystem.pushMatrix();
        // move 0, 0 to spell window
        RenderSystem.translatef(spellLeft, spellTop, 0);
        // draw background
        int bg_texX = (currentSpell == null ? 16 : 0);
        float start_x = getNegativeMod16(this.x);
        float start_y = getNegativeMod16(this.y);
        for (float bg_x = start_x; bg_x < width + 16; bg_x += 16) {
            for (float bg_y = start_y; bg_y < height + 16; bg_y += 16) {
                ClientUiUtil.drawTexturedModalRect(stack, (int)Math.floor(bg_x), (int)Math.floor(bg_y), bg_texX, 0, 16, 16);
            }
        }


        if (currentSpell != null) {
            // render starting at board location
            RenderSystem.translatef(x, y, 0);

            Queue<Pair<SpellModule, SpellModule>> moduleQueue = new LinkedList<>();
            Queue<Pair<SpellModule, SpellModule>> connectQueue = new LinkedList<>();
            Queue<SpellModule> renderQueue = new LinkedList<>();

            if (currentSpell.mainModule != null) {
                moduleQueue.add(new Pair<>(null, currentSpell.mainModule));
            }
            for (SpellModule floater : isolated) {
                if (floater != null) {
                    moduleQueue.add(new Pair<>(null, floater));
                }
            }
            while(!moduleQueue.isEmpty()) {
                Pair<SpellModule, SpellModule> next = moduleQueue.remove();
                SpellModule root = next.getFirst();
                SpellModule base = next.getSecond();
                if (root != null && !root.canConnectSpecial(base)) {
                    connectQueue.add(next);
                }
                renderQueue.add(base);
                for (SpellModule bound : base.bound) {
                    if (bound != null) {
                        moduleQueue.add(new Pair<>(base, bound));
                    }
                }
                for (SpellModule bound : base.boundSpecial) {
                    if (bound != null) {
                        moduleQueue.add(new Pair<>(base, bound));
                    }
                }
            }

            while(!connectQueue.isEmpty()) {
                mc.getTextureManager().bindTexture(SPELL_RESOURCES);
                Pair<SpellModule, SpellModule> next = connectQueue.remove();
                SpellModule root = next.getFirst();
                SpellModule base = next.getSecond();

                RenderSystem.pushMatrix();
                Point rootPoint = root.getConnectionRenderStart();
                Point basePoint = base.getConnectionRenderEnd();
                double distance = Math.sqrt(Math.pow(rootPoint.x - basePoint.x, 2) + Math.pow(rootPoint.y - basePoint.y, 2));
                // render connector with 0,0 at root
                RenderSystem.translatef(rootPoint.x, rootPoint.y, 0);
                // sprite rendered from (0,0) to (x, 0), so we rotate the matrix to draw accordingly
                float angle = (float)(Math.atan2(basePoint.y - rootPoint.y, basePoint.x - rootPoint.x) * 180 / Math.PI);
                RenderSystem.rotatef(angle, 0, 0, 1);
                ClientUiUtil.drawTexturedModalRect(stack, -8, -8, 176, 16, 16, 16);
                ClientUiUtil.drawTexturedModalRect(stack, (int)distance - 8, -8, 208, 16, 16, 16);
                if (distance > 16) {
                    RenderSystem.translatef(8,0,0);
                    RenderSystem.scalef(((float)distance - 16.0f) / 16.0f,1,  1);
                    ClientUiUtil.drawTexturedModalRect(stack, 0, -8, 192, 16, 16, 16);
                }
                RenderSystem.popMatrix();
            }

            while(!renderQueue.isEmpty()) {
                mc.getTextureManager().bindTexture(SPELL_RESOURCES);
                SpellModule next = renderQueue.remove();
                if (!floating.containsKey(next)) {
                    next.renderInMinigame(mouseX, mouseY, mc.getItemRenderer(), false, stack);
                } else { // current player is floating module
                    RenderSystem.color4f(1.0f, 1.0f, 1.0f, .75f);
                    next.renderInMinigame(mouseX, mouseY, mc.getItemRenderer(), true, stack);
                    RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                }
            }
        }

        if (activeModule instanceof Connector) {
            activeModule.renderInMinigame(mouseX, mouseY, mc.getItemRenderer(), (!activeModule.unplaced), stack);
        }

        RenderSystem.popMatrix();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        mc.getTextureManager().bindTexture(SPELL_RESOURCES);
        // Render selected module under mouse cursor
        if (activeModule != null) {
            activeModule.renderUnderMouse(mouseX, mouseY, mc.getItemRenderer(), (!activeModule.unplaced), stack);
        }

        mc.getTextureManager().bindTexture(FociForgeScreen.BG);
        if (currentSpell != null) {
            for (int i = 0; i < 9; i++) {
                ClientUiUtil.drawModalRectWithCustomSizedTexture(stack,
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
            if (state.currentSpell.mainModule != null) {
                state.x = 32 - state.currentSpell.mainModule.x;
                state.y = 32 - state.currentSpell.mainModule.y;
            }
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
