package zonecraftmod.util;

import net.minecraft.world.phys.Vec3;

public class ColorUtil {

    public static Vec3 incrementVec3(Vec3 initialV3, Vec3 finalV3) {
        double initial_x = initialV3.x;
        double initial_y = initialV3.y;
        double initial_z = initialV3.z;
        double final_x = finalV3.x;
        double final_y = finalV3.y;
        double final_z = finalV3.z;

        double newX = final_x;
        double newY = final_y;
        double newZ = final_z;
        //ZoneCraftMod.LOGGER.info("Initial Vec3: {}", initialV3.toString());
        if (initial_x < final_x) { // x (addition)
            //ZoneCraftMod.LOGGER.info("Starting X: {}", initial_x);
            if ((initial_x + 0.01) > final_x) {
                //ZoneCraftMod.LOGGER.info("Addition 1");
                newX = initial_x + (final_x - initial_x);
            } else {
                //ZoneCraftMod.LOGGER.info("Addition 2");
                newX = initial_x + 0.01;
            }
            //ZoneCraftMod.LOGGER.info("New X: {}", newX);
        } else if (initial_x > final_x) {// x (subtraction)
            //ZoneCraftMod.LOGGER.info("Starting X: {}", initial_x);
            if ((initial_x - 0.01) < final_x) {
                //ZoneCraftMod.LOGGER.info("Subtraction 1");
                newX = initial_x - (initial_x - final_x);
            } else {
                //ZoneCraftMod.LOGGER.info("Subtraction 2");
                newX = initial_x - 0.01;
            }
            //ZoneCraftMod.LOGGER.info("New X: {}", newX);
        }
        if (initial_y < final_y) {// y (addition)
            //ZoneCraftMod.LOGGER.info("Starting Y: {}", initial_y);
            if ((initial_y + 0.01) > final_y) {
                //ZoneCraftMod.LOGGER.info("Addition 1");
                newY = initial_y + (final_y - initial_y);
            } else {
                //ZoneCraftMod.LOGGER.info("Addition 2");
                newY = initial_y + 0.01;
            }
            //ZoneCraftMod.LOGGER.info("New Y: {}", newY);
        } else if (initial_y > final_y) {// y (subtraction)
            //ZoneCraftMod.LOGGER.info("Starting Y: {}", initial_y);
            if ((initial_y - 0.01) < final_y) {
                //ZoneCraftMod.LOGGER.info("Subtraction 1");
                newY = initial_y - (initial_y - final_y);
            } else {
                //ZoneCraftMod.LOGGER.info("Subtraction 2");
                newY = initial_y - 0.01;
            }
            //ZoneCraftMod.LOGGER.info("New Y: {}", newY);
        }
        if (initial_z < final_z) {// z (addition)
            //ZoneCraftMod.LOGGER.info("Starting Z: {}", initial_z);
            if ((initial_z + 0.01) > final_z) {
                //ZoneCraftMod.LOGGER.info("Addition 1");
                newZ = initial_z + (final_z - initial_z);
            } else {
                //ZoneCraftMod.LOGGER.info("Addition 2");
                newZ = initial_z + 0.01;
            }
            //ZoneCraftMod.LOGGER.info("New Z: {}", newZ);
        } else if (initial_z > final_z) {// z (subtraction)
            //ZoneCraftMod.LOGGER.info("Starting Z: {}", initial_z);
            if ((initial_z - 0.01) < final_z) {
                //ZoneCraftMod.LOGGER.info("Subtraction 1");
                newZ = initial_z - (initial_z - final_z);
            } else {
                //ZoneCraftMod.LOGGER.info("Subtraction 2");
                newZ = initial_z - 0.01;
            }
            //ZoneCraftMod.LOGGER.info("New Z: {}", newZ);
        }
        //ZoneCraftMod.LOGGER.info("Final Vec3: {}", new Vec3(newX,newY,newZ));
        return new Vec3(newX,newY,newZ);
    }

    public static Vec3 incrementVec3_m2(Vec3 initialV3, Vec3 finalV3) {
        double initial_x = initialV3.x;
        double initial_y = initialV3.y;
        double initial_z = initialV3.z;
        double final_x = finalV3.x;
        double final_y = finalV3.y;
        double final_z = finalV3.z;

        double newX = final_x;
        double newY = final_y;
        double newZ = final_z;


        if (Math.round(initial_x) < Math.round(final_x)) { // x (addition)
            if ((initial_x + 0.01) > final_x) {
                newX = initial_x + (final_x - initial_x);
            } else {
                newX = initial_x + 0.01;
            }
        } else if (Math.round(initial_x) > Math.round(final_x)) {// x (subtraction)
            if ((initial_x - 0.01) < final_x) {
                newX = initial_x - (initial_x - final_x);
            } else {
                newX = initial_x - 0.01;
            }
        }
        if (Math.round(initial_y) < Math.round(final_y)) {// y (addition)
            if ((initial_y + 0.01) > final_y) {
                newY = initial_y + (final_y - initial_y);
            } else {
                newY = initial_y + 0.01;
            }
        } else if (Math.round(initial_y) > Math.round(final_y)) {// y (subtraction)
            if ((initial_y - 0.01) < final_y) {
                newY = initial_y - (initial_y - final_y);
            } else {
                newY = initial_y - 0.01;
            }
        }
        if (Math.round(initial_z) < Math.round(final_z)) {// z (addition)
            if ((initial_z + 0.01) > final_z) {
                newZ = initial_z + (final_z - initial_z);
            } else {
                newZ = initial_z + 0.01;
            }
        } else if (Math.round(initial_z) > Math.round(final_z)) {// z (subtraction)
            if ((initial_z - 0.01) < final_z) {
                newZ = initial_z - (initial_z - final_z);
            } else {
                newZ = initial_z - 0.01;
            }
        }
        return new Vec3(newX,newY,newZ);
    }
}
