package zonecraftmod.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import zonecraftmod.client.ClientEmission;

import java.util.function.Supplier;

public class EmissionColorPacket { // Server to Client
    private final double vecX;
    private final double vecY;
    private final double vecZ;

    public EmissionColorPacket(double x, double y, double z) {
        this.vecX = x;
        this.vecY = y;
        this.vecZ = z;
    }
    public EmissionColorPacket(FriendlyByteBuf buf) {
        this.vecX = buf.readDouble();
        this.vecY = buf.readDouble();
        this.vecZ = buf.readDouble();
    }
    public void toBuf(FriendlyByteBuf buf) {
        buf.writeDouble(vecX);
        buf.writeDouble(vecY);
        buf.writeDouble(vecZ);
    }

    public boolean handlePacket(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context cxt = supplier.get();
        cxt.enqueueWork(() -> {
            ClientEmission.setEmissionColor(new Vec3(vecX,vecY,vecZ));
        });
        return true;
    }
}