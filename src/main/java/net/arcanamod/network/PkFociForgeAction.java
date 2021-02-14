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

    public static final Logger LOGGER = LogManager.getLogger();

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
            if (spe.openContainer.windowId == msg.windowId){
                FociForgeContainer container = (FociForgeContainer)spe.openContainer;
                SpellState state = container.spellState;
                switch (msg.action) {
                    case PLACE:
                        break;
                    case RAISE:
                        break;
                    case LOWER:
                        break;
                    case CONNECT:
                        break;
                    case DELETE:
                        break;
                    case ASSIGN:
                        break;
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
