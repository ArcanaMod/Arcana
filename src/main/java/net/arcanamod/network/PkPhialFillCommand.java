package net.arcanamod.network;

import net.arcanamod.Arcana;
import net.arcanamod.aspects.Aspect;
import net.arcanamod.aspects.VisHandler;
import net.arcanamod.aspects.VisHandlerCapability;
import net.arcanamod.research.Researcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class PkPhialFillCommand
{
    public static final Logger LOGGER = LogManager.getLogger();

    public PkPhialFillCommand(){
    }

    public static void encode(PkPhialFillCommand msg, PacketBuffer buffer){
        CompoundNBT compound = new CompoundNBT();
        buffer.writeCompoundTag(compound);
    }

    public static PkPhialFillCommand decode(PacketBuffer buffer){
        return new PkPhialFillCommand();
    }

    public static void handle(PkPhialFillCommand msg, Supplier<NetworkEvent.Context> supplier){
        // from server to client
        supplier.get().enqueueWork(() -> {
            PlayerEntity pe = Arcana.proxy.getPlayerOnClient();
            VisHandler vis = pe.getHeldItemMainhand().getCapability(VisHandlerCapability.ASPECT_HANDLER).orElse(null);
            if (vis!=null)
            {
                vis.insert(Aspect.EXCHANGE, 8, false);
            }
        });
        supplier.get().setPacketHandled(true);
    }
}