package net.arcanamod.network;

import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.containers.FociForgeContainer;
import net.arcanamod.systems.spell.SpellState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PkFociForgeAction {

    int windowId;
    Type action;
    int ax, ay;
    int bx, by;
    int sequence;
    Aspect aspect;

    public PkFociForgeAction(int windowId, Type action, int ax, int ay, int bx, int by, int sequence, Aspect aspect) {
        this.windowId = windowId;
        this.action = action;
        this.ax = ax;
        this.ay = ay;
        this.bx = bx;
        this.by = by;
        this.sequence = sequence;
        this.aspect = aspect;
    }

    public static void encode(PkFociForgeAction msg, PacketBuffer buffer){
        buffer.writeInt(msg.windowId);
        buffer.writeEnumValue(msg.action);
        buffer.writeShort(msg.ax);
        buffer.writeShort(msg.ay);
        buffer.writeShort(msg.bx);
        buffer.writeShort(msg.by);
        buffer.writeShort(msg.sequence);
        buffer.writeString(msg.aspect.name());
    }

    public static PkFociForgeAction decode(PacketBuffer buffer){
        return new PkFociForgeAction(
                buffer.readInt(),
                buffer.readEnumValue(Type.class),
                buffer.readShort(),
                buffer.readShort(),
                buffer.readShort(),
                buffer.readShort(),
                buffer.readShort(),
                AspectUtils.getAspectByName(buffer.readString()));
    }


    public static void handle(PkFociForgeAction msg, Supplier<NetworkEvent.Context> supplier){
        supplier.get().enqueueWork(() -> {
            ServerPlayerEntity spe = supplier.get().getSender();
            if (spe.currentWindowId == msg.windowId) {
                FociForgeContainer container = (FociForgeContainer)spe.openContainer;
                SpellState state = container.te.spellState;
                boolean valid = false;
                if (msg.sequence == state.sequence) {
                    switch (msg.action) {
                        case PLACE:
                            valid = state.place(msg.ax, msg.ay, msg.bx, false);
                            break;
                        case RAISE:
                            valid = state.raise(msg.ax, msg.ay, spe.getUniqueID(), false);
                            break;
                        case LOWER:
                            valid = state.lower(msg.ax, msg.ay, msg.bx, msg.by, spe.getUniqueID(), false);
                            break;
                        case CONNECT:
                            valid = state.connect(msg.ax, msg.ay, msg.bx, msg.by, false);
                            break;
                        case DELETE:
                            valid = state.delete(msg.ax, msg.ay, spe.getUniqueID(), false);
                            break;
                        case ASSIGN:
                            valid = state.assign(msg.ax, msg.ay, msg.aspect, false);
                            break;
                    }
                }
                if (valid) {
                    state.sequence++;
                    container.te.markDirty();
                }
            }
        });
        supplier.get().setPacketHandled(true);
    }

    public enum Type {
        PLACE,
        RAISE,
        LOWER,
        CONNECT,
        DELETE,
        ASSIGN
    }
}
