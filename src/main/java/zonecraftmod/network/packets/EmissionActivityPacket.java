package zonecraftmod.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import zonecraftmod.client.ClientEmission;

import java.util.function.Supplier;

public class EmissionActivityPacket { // Server to Client
    private final boolean active;

    public EmissionActivityPacket(boolean isActive) {
        this.active = isActive;
    }
    public EmissionActivityPacket(FriendlyByteBuf buf) {
        this.active = buf.readBoolean();
    }
    public void toBuf(FriendlyByteBuf buf) {
        buf.writeBoolean(active);
    }

    public boolean handlePacket(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context cxt = supplier.get();
        cxt.enqueueWork(() -> ClientEmission.setEmissionActivity(active));
        return true;
    }
}