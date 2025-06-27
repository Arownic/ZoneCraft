package zonecraftmod.client;

import net.minecraft.world.phys.Vec3;

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

    public static void summonLightning() {

    }
}