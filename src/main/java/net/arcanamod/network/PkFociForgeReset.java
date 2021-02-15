package net.arcanamod.network;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.AspectUtils;
import net.arcanamod.containers.FociForgeContainer;
import net.arcanamod.systems.spell.SpellState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public class PkFociForgeReset {
    public static final Logger LOGGER = LogManager.getLogger();

    int windowId;
    SpellState state;
    int sequence;

    public PkFociForgeReset(int windowId, SpellState state, int sequence) {
        this.windowId = windowId;
        this.state = state;
        this.sequence = sequence;
    }

    public static void encode(PkFociForgeReset msg, PacketBuffer buffer) {
        buffer.writeInt(msg.windowId);
        buffer.writeCompoundTag(msg.state.toNBT(new CompoundNBT()));
        buffer.writeInt(msg.sequence);
    }

    public static PkFociForgeReset decode(PacketBuffer buffer){
        return new PkFociForgeReset(
                buffer.readInt(),
                SpellState.fromNBT(buffer.readCompoundTag()),
                buffer.readInt()
        );
    }


    public static void handle(PkFociForgeReset msg, Supplier<NetworkEvent.Context> supplier){
        supplier.get().enqueueWork(() -> {
            PlayerEntity player = Arcana.proxy.getPlayerOnClient();
            if (player != null && player.openContainer instanceof FociForgeContainer) {
                FociForgeContainer container = (FociForgeContainer)player.openContainer;
                container.te.spellState = msg.state;
                container.te.spellState.sequence = msg.sequence;
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
