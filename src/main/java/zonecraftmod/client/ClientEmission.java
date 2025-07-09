package zonecraftmod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.phys.Vec3;
import zonecraftmod.util.VectorUtil;

import java.util.Objects;

public class ClientEmission {
    private static boolean activeEmission = false;
    private static Vec3 emissionColor;

    public static void setEmissionActivity(boolean activity) {
        activeEmission = activity;
    }
    public static boolean getEmissionActivity() {
        return activeEmission;
    }

    public static void setEmissionColor(Vec3 color) {
        emissionColor = color;
    }
    public static Vec3 getEmissionColor() {
        return emissionColor;
    }

    public static void summonLightning(String lightningDirection) {
        //ZoneCraftMod.LOGGER.info("Command received");
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(Objects.requireNonNull(level));
        if (lightningbolt != null) {
            //ZoneCraftMod.LOGGER.info("lightning bolt ~= null");
            LocalPlayer player = minecraft.player;
            BlockPos lightningPos = new BlockPos(
                    VectorUtil.convertVec3(
                            VectorUtil.determineLocation(lightningDirection).add(player.blockPosition().getCenter())
                    )
            );
            lightningbolt.moveTo(Vec3.atBottomCenterOf(lightningPos));
            lightningbolt.setVisualOnly(true);
            level.putNonPlayerEntity(lightningbolt.getId(), lightningbolt);
        }
    }
}