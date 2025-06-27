package zonecraftmod.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;
import zonecraftmod.ZoneCraftMod;
import zonecraftmod.network.packets.EmissionActivityPacket;
import zonecraftmod.network.packets.EmissionColorPacket;

import java.util.List;

public class PacketHandler {
    private static SimpleChannel INSTANCE;

    private static final String protocolVersion = "0.1";
    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ZoneCraftMod.MODID, "emission"))
                .networkProtocolVersion(() -> protocolVersion)
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();
        INSTANCE = net;

        net.messageBuilder(EmissionColorPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EmissionColorPacket::new)
                .encoder(EmissionColorPacket::toBuf)
                .consumerMainThread(EmissionColorPacket::handlePacket)
                .add();
        net.messageBuilder(EmissionActivityPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(EmissionActivityPacket::new)
                .encoder(EmissionActivityPacket::toBuf)
                .consumerMainThread(EmissionActivityPacket::handlePacket)
                .add();
    }

    public static <MSG> void sendToServer(MSG msg) {
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static <MSG> void sendToAll(MSG msg) {
        List<ServerPlayer> playerList = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers();
        for (ServerPlayer player : playerList) {
            INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
        }
    }
}
