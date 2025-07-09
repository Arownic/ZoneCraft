package zonecraftmod;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;
import zonecraftmod.config.ServerConfig;
import zonecraftmod.network.PacketHandler;
import zonecraftmod.network.packets.EmissionActivityPacket;
import zonecraftmod.network.packets.EmissionColorPacket;
import zonecraftmod.util.VectorUtil;

public class Emission {
    static final Vec3 emissionColor = new Vec3(1, 0.1, 0);
    static Vec3 defaultSkyColor;
    static Vec3 lastKnownColor;
    static boolean activeEmission = false;
    static boolean isForwardTransition = true;
    static boolean isTransitioning = false;
    static boolean playersTakeDamage = false;
    static boolean stillPoint = false;
    static boolean changeIncrements = true;

    static double incrementX = Double.NaN;
    static double incrementY = Double.NaN;
    static double incrementZ = Double.NaN;

    private static int tickCount = 1;
    private static int lightningTick = 5;

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
        if (!activeEmission) {
            activeEmission = true;
            isTransitioning = true;
            isForwardTransition = true;
            defaultSkyColor = getSkyColor();
            lastKnownColor = defaultSkyColor;
            GameRules rules = minecraftServer.getGameRules();
            rules.getRule(GameRules.RULE_DAYLIGHT).set(false, minecraftServer);
            rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, minecraftServer);
            PacketHandler.sendToAll(new EmissionActivityPacket(true));
        }
    }


    public static void emissionTick() {
        if (activeEmission) {
            /*
            if (lightningTick < 1) {
                PacketHandler.sendToAll(new SummonLightningPacket());
                lightningTick = 5;
            }
            */
            if (isTransitioning) {
                if (isForwardTransition) {
                    if (changeIncrements) {
                        incrementX = VectorUtil.getIncrementValue(defaultSkyColor.x,emissionColor.x, ServerConfig.timeToEmissionPeek);
                        incrementY = VectorUtil.getIncrementValue(defaultSkyColor.y,emissionColor.y, ServerConfig.timeToEmissionPeek);
                        incrementZ = VectorUtil.getIncrementValue(defaultSkyColor.z,emissionColor.z, ServerConfig.timeToEmissionPeek);
                        ZoneCraftMod.LOGGER.info("Increments:");
                        ZoneCraftMod.LOGGER.info("X: {}", incrementX);
                        ZoneCraftMod.LOGGER.info("Y: {}", incrementY);
                        ZoneCraftMod.LOGGER.info("Z: {}", incrementZ);
                        changeIncrements = false;
                    }
                    if (lastKnownColor.equals(emissionColor)) {
                        isForwardTransition = false; // false equals backward transition
                        isTransitioning = false;
                        stillPoint = true;
                        playersTakeDamage = true;
                        changeIncrements = true;
                        tickCount = ServerConfig.emissionPeekTime*20;
                        ZoneCraftMod.LOGGER.info("Completed Forward Transition");
                    }
                    if (tickCount < 1) {
                        ZoneCraftMod.LOGGER.info("Presumed SkyColor: " + defaultSkyColor);
                        ZoneCraftMod.LOGGER.info("Last known Color: " + lastKnownColor);
                        //Vec3 color = Vec3Util.incrementVec3(lastKnownColor, emissionColor);
                        //Vec3 color = lastKnownColor.add(new Vec3(incrementX, incrementY, incrementZ));
                        Vec3 color = new Vec3(
                                VectorUtil.incrementVec3Value(incrementX,lastKnownColor.x(),emissionColor.x()),
                                VectorUtil.incrementVec3Value(incrementY,lastKnownColor.y(),emissionColor.y()),
                                VectorUtil.incrementVec3Value(incrementZ,lastKnownColor.z(),emissionColor.z())
                        );
                        PacketHandler.sendToAll(new EmissionColorPacket(color.x, color.y, color.z));
                        lastKnownColor = color;
                        tickCount = 1;
                        ZoneCraftMod.LOGGER.info("Cycle");
                        ZoneCraftMod.LOGGER.info("Current RGB Color: " + color.x + ", " + color.y + ", " + color.z);
                    }
                } else {
                    if (changeIncrements) {
                        incrementX = VectorUtil.getIncrementValue(emissionColor.x,defaultSkyColor.x, ServerConfig.timeFromEmissionPeek);
                        incrementY = VectorUtil.getIncrementValue(emissionColor.y,defaultSkyColor.y, ServerConfig.timeFromEmissionPeek);
                        incrementZ = VectorUtil.getIncrementValue(emissionColor.z,defaultSkyColor.z, ServerConfig.timeFromEmissionPeek);
                        ZoneCraftMod.LOGGER.info("Increments:");
                        ZoneCraftMod.LOGGER.info("X: {}", incrementX);
                        ZoneCraftMod.LOGGER.info("Y: {}", incrementY);
                        ZoneCraftMod.LOGGER.info("Z: {}", incrementZ);
                        changeIncrements = false;
                    }
                    if (lastKnownColor.equals(defaultSkyColor)) {
                        isTransitioning = false;
                        isForwardTransition = true;
                        activeEmission = false;
                        changeIncrements = true;
                        tickCount = 1;
                        ZoneCraftMod.LOGGER.info("Completed Emission");
                        GameRules rules = minecraftServer.getGameRules();
                        rules.getRule(GameRules.RULE_DAYLIGHT).set(true, minecraftServer);
                        rules.getRule(GameRules.RULE_WEATHER_CYCLE).set(true, minecraftServer);
                        PacketHandler.sendToAll(new EmissionActivityPacket(false));
                    }
                    if (tickCount < 1) {
                        ZoneCraftMod.LOGGER.info("Presumed SkyColor: " + defaultSkyColor);
                        //Vec3 color = lastKnownColor.add(new Vec3(-incrementX, -incrementY, -incrementZ));
                        Vec3 color = new Vec3(
                                VectorUtil.incrementVec3Value(incrementX,lastKnownColor.x(),defaultSkyColor.x()),
                                VectorUtil.incrementVec3Value(incrementY,lastKnownColor.y(),defaultSkyColor.y()),
                                VectorUtil.incrementVec3Value(incrementZ,lastKnownColor.z(),defaultSkyColor.z())
                        );
                        //Vec3 color = Vec3Util.incrementVec3(lastKnownColor, defaultSkyColor);
                        PacketHandler.sendToAll(new EmissionColorPacket(color.x, color.y, color.z));
                        lastKnownColor = color;
                        tickCount = 1;
                        ZoneCraftMod.LOGGER.info("Cycle");
                        ZoneCraftMod.LOGGER.info("Current RGB Color: " + color.x + ", " + color.y + ", " + color.z);
                    }
                }
            } else if (stillPoint) {
                ZoneCraftMod.LOGGER.info("peek cycle");
                // add damage detection
                if (tickCount < 1) {
                    isTransitioning = true;
                    stillPoint = false;
                    playersTakeDamage = false;
                    tickCount = 1;
                    ZoneCraftMod.LOGGER.info("Completed Still Point");
                }
            }
        }
        //lightningTick--;
        tickCount--;
    }

    public static boolean isEmissionActive() {
        return activeEmission;
    }
}
