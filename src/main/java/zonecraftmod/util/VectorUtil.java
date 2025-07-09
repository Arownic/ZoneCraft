package zonecraftmod.util;

import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public class VectorUtil {
    public static double getIncrementValue(double startingValue, double endingValue, int timeTaken) {
        return Double.sum(endingValue, -startingValue)/(timeTaken*20);
    }

    public static Vec3i convertVec3(Vec3 vector) {
        return new Vec3i((int)vector.x, (int)vector.y, (int)vector.z);
    }

    public static Vec3 determineLocation(String direction) {
        Vec3 change = Vec3.ZERO;
        change = switch (direction) {
            case "west" -> new Vec3(-20, 0, 0);
            case "east" -> new Vec3(20, 0, 0);
            case "north" -> new Vec3(0, 0, -20);
            case "south" -> new Vec3(0, 0, 20);
            default -> change;
        };
        return change;
    }
    public static double incrementVec3Value(double increment, double currentValue, double maxValue) {
        if (increment < 0) { // increment is negative
            if (currentValue + increment > maxValue) {
                return currentValue+increment;
            }
        } else if (increment > 0) { // increment is positive
            if (currentValue + increment < maxValue) {
                return currentValue+increment;
            }
        }
        return maxValue;
    }
}
