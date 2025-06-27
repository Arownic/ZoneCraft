package zonecraftmod;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;
import zonecraftmod.network.PacketHandler;
import zonecraftmod.network.packets.EmissionActivityPacket;
import zonecraftmod.network.packets.EmissionColorPacket;
import zonecraftmod.util.ColorUtil;

public class Emission {

    private static final Vec3 emmissionColor = new Vec3(1, 0.1, 0);
    private static Vec3 defaultSkyColor;
    private static Vec3 lastKnownColor;
    private static boolean activeEmmission = false;
    private static boolean isForwardTransition = true;
    private static boolean isTransitioning = false;
    private static boolean playersTakeDamage = false;
    public static boolean stillPoint = false;
    private static int tickCount = 0;

    static MinecraftServer minecraftServer = ServerLifecycleHooks.getCurrentServer();
    public static Vec3 getSkyColor() {
        Vec3 basevec3 = new Vec3(0,400,0);
        ServerLevel level = minecraftServer.overworld();
        float f = level.getTimeOfDay(1.0F);
        Vec3 vec3 = basevec3.subtract(2.0D, 2.0D, 2.0D).scale(0.25D);
        BiomeManager biomemanager = level.getBiomeManager();
        Vec3 vec31 = CubicSampler.gaussianSampleVec3(vec3, (p_194161_, p_194162_, p_194163_) -> {
            return Vec3.fromRGB24(biomemanager.getNoiseBiomeAtQuart(p_194161_, p_194162_, p_194163_).value().getSkyColor());
        });
        float f1 = Mth.cos(f * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f1 = Mth.clamp(f1, 0.0F, 1.0F);
        float f2 = (float)vec31.x * f1;
        float f3 = (float)vec31.y * f1;
        float f4 = (float)vec31.z * f1;
        float f5 = level.getRainLevel(1.0F);
        if (f5 > 0.0F) {
            float f6 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
            float f7 = 1.0F - f5 * 0.75F;
            f2 = f2 * f7 + f6 * (1.0F - f7);
            f3 = f3 * f7 + f6 * (1.0F - f7);
            f4 = f4 * f7 + f6 * (1.0F - f7);
        }

        float f9 = level.getThunderLevel(1.0F);
        if (f9 > 0.0F) {
            float f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
            float f8 = 1.0F - f9 * 0.75F;
            f2 = f2 * f8 + f10 * (1.0F - f8);
            f3 = f3 * f8 + f10 * (1.0F - f8);
            f4 = f4 * f8 + f10 * (1.0F - f8);
        }

        return new Vec3((double)f2, (double)f3, (double)f4);
    }

    public static void startEmission() {
        activeEmmission = true;
        isTransitioning = true;
        isForwardTransition = true;
        defaultSkyColor = getSkyColor();
        lastKnownColor = defaultSkyColor;
        GameRules rules = minecraftServer.getGameRules();
        rules.getRule(GameRules.RULE_DAYLIGHT).set(false, minecraftServer);
        rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, minecraftServer);
        PacketHandler.sendToAll(new EmissionActivityPacket(true));
    }

    public static void emissionTick() {
        if (activeEmmission) {
            if (isTransitioning) {
                if (isForwardTransition) {
                    if (lastKnownColor.equals(emmissionColor)) {
                        isForwardTransition = false; // false equals backward transition
                        isTransitioning = false;
                        stillPoint = true;
                        playersTakeDamage = true;
                        tickCount = 100;
                        ZoneCraftMod.LOGGER.info("Completed Forward Transition");
                    }
                    if (tickCount < 1) {
                        ZoneCraftMod.LOGGER.info("Presumed SkyColor: " + defaultSkyColor);
                        ZoneCraftMod.LOGGER.info("Last known Color: " + lastKnownColor);
                        Vec3 color = ColorUtil.incrementVec3(lastKnownColor, emmissionColor);
                        PacketHandler.sendToAll(new EmissionColorPacket(color.x, color.y, color.z));
                        lastKnownColor = color;
                        tickCount = 5;
                        ZoneCraftMod.LOGGER.info("Cycle");
                        ZoneCraftMod.LOGGER.info("Current RGB Color: " + color.x + ", " + color.y + ", " + color.z);
                    }
                } else {
                    if (lastKnownColor.equals(defaultSkyColor)) {
                        isTransitioning = false;
                        isForwardTransition = true;
                        activeEmmission = false;
                        tickCount = 0;
                        ZoneCraftMod.LOGGER.info("Completed Emission");
                        GameRules rules = minecraftServer.getGameRules();
                        rules.getRule(GameRules.RULE_DAYLIGHT).set(true, minecraftServer);
                        rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(true, minecraftServer);
                        PacketHandler.sendToAll(new EmissionActivityPacket(false));
                    }
                    if (tickCount < 1) {
                        ZoneCraftMod.LOGGER.info("Presumed SkyColor: " + defaultSkyColor);
                        Vec3 color = ColorUtil.incrementVec3_m2(lastKnownColor, defaultSkyColor);
                        PacketHandler.sendToAll(new EmissionColorPacket(color.x, color.y, color.z));
                        lastKnownColor = color;
                        tickCount = 5;
                        ZoneCraftMod.LOGGER.info("Cycle");
                        ZoneCraftMod.LOGGER.info("Current RGB Color: " + color.x + ", " + color.y + ", " + color.z);
                    }
                }
            } else if (stillPoint) {
                if (tickCount < 1) {
                    isTransitioning = true;
                    stillPoint = false;
                    playersTakeDamage = false;
                    tickCount = 5;
                    ZoneCraftMod.LOGGER.info("Completed Still Point");
                }
            }
            tickCount--;
        }
        /*
        if (ZoneCraftMod.activeEmmission || ZoneCraftMod.emmissionTransition) {
            Vec3 color = zonecraft$currentSkyColor;
            if (ZoneCraftMod.emmissionTransition) {
                if (zonecraft$transitionType) {
                    if (zonecraft$currentSkyColor.equals(zonecraft$originalSkyColor)) {
                        ZoneCraftMod.emmissionTransition = false;
                        //ZoneCraftMod.activeEmmission = false;
                        zonecraft$transitionType = false;
                        ZoneCraftMod.LOGGER.info("Completed");
                    }
                    if (zonecraft$ticksBeforeChange < 1) {
                        ZoneCraftMod.LOGGER.info("Presumed SkyColor: " + Emission.getSkyColor());
                        color = Vec3Util.incrementVec3_m2(zonecraft$currentSkyColor, zonecraft$originalSkyColor);
                        zonecraft$currentSkyColor = color;
                        zonecraft$ticksBeforeChange = 125;
                        ZoneCraftMod.LOGGER.info("Cycle");
                        ZoneCraftMod.LOGGER.info("Current RGB Color: " + color.x + ", " + color.y + ", " + color.z);
                    }
                } else if (!zonecraft$transitionType) {
                    if (zonecraft$currentSkyColor.equals(ZoneCraftMod.emmissionColor)) {
                        ZoneCraftMod.emmissionTransition = false;
                        ZoneCraftMod.activeEmmission = true;
                        ZoneCraftMod.LOGGER.info("Completed");
                        zonecraft$ticksBeforeChange = 250;
                    }
                    if (zonecraft$ticksBeforeChange < 1) {
                        ZoneCraftMod.LOGGER.info("Presumed SkyColor: " + Emission.getSkyColor());
                        color = Vec3Util.incrementVec3(zonecraft$currentSkyColor, ZoneCraftMod.emmissionColor);
                        zonecraft$currentSkyColor = color;
                        zonecraft$ticksBeforeChange = 125;
                        ZoneCraftMod.LOGGER.info("Cycle");
                        ZoneCraftMod.LOGGER.info("Current RGB Color: " + color.x + ", " + color.y + ", " + color.z);
                    }
                }
            } else if (ZoneCraftMod.activeEmmission) {
                color = ZoneCraftMod.emmissionColor;
                if (zonecraft$ticksBeforeChange < 1) {
                    ZoneCraftMod.emmissionTransition = true;
                    zonecraft$transitionType = true;
                    ZoneCraftMod.activeEmmission = false;
                    zonecraft$ticksBeforeChange = 125;
                }
            } else {cir.cancel();}

            float timeOfDay = this.getTimeOfDay(tick);
            float rainLevel = this.getRainLevel(tick);
            float thunderLevel = this.getThunderLevel(tick);
            int flashTime = ((ClientLevel) (Object) this).getSkyFlashTime();

            float f1 = Mth.cos(timeOfDay * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
            f1 = Mth.clamp(f1, 0.1F, 1.0F);
            float red = (float)color.x * f1;
            float green = (float)color.y * f1;
            float blue = (float)color.z * f1;

            if (rainLevel > 0.0F) {
                float f6 = (red * 0.3F + green * 0.59F + blue * 0.11F) * 0.6F;
                float f7 = 1.0F - rainLevel * 0.75F;
                red = red * f7 + f6 * (1.0F - f7);
                green = green * f7 + f6 * (1.0F - f7);
                blue = blue * f7 + f6 * (1.0F - f7);
            }

            if (thunderLevel > 0.0F) {
                float f10 = (red * 0.3F + green * 0.59F + blue * 0.11F) * 0.2F;
                float f8 = 1.0F - thunderLevel * 0.75F;
                red = red * f8 + f10 * (1.0F - f8);
                green = green * f8 + f10 * (1.0F - f8);
                blue = blue * f8 + f10 * (1.0F - f8);
            }

            if (flashTime > 0) {
                float f11 = (float)flashTime - tick;
                if (f11 > 1.0F) {
                    f11 = 1.0F;
                }

                f11 *= 0.45F;
                red = red * (1.0F - f11) + 0.8F * f11;
                green = green * (1.0F - f11) + 0.8F * f11;
                blue = blue * (1.0F - f11) + 1.0F * f11;
            }
            zonecraft$ticksBeforeChange--;
            cir.setReturnValue(new Vec3(red, green, blue));
         */
    }

    public static boolean isEmissionActive() {
        return activeEmmission;
    }
}
