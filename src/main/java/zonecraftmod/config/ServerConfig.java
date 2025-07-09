package zonecraftmod.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import zonecraftmod.ZoneCraftMod;

@Mod.EventBusSubscriber(modid = ZoneCraftMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ServerConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.IntValue TIME_BETWEEN = BUILDER.comment("Time in seconds between Emissions").defineInRange("time_between_emissions", 180, 0, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue TO_PEEK = BUILDER.comment("Time in seconds to the peek of an Emission").defineInRange("time_to_peek", 30, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue PEEK_TIME = BUILDER.comment("Time in seconds for the during of the Emission peek").defineInRange("peek_time", 45, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.IntValue FROM_PEEK = BUILDER.comment("Time in seconds from the peek of an Emission").defineInRange("time_from_peek", 15, 1, Integer.MAX_VALUE);
    private static final ForgeConfigSpec.ConfigValue<String> LIGHTNING_DIR = BUILDER.comment("The Direction which lightning is seen during an Emission").define("lightning_direction", "east");

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static Integer timeBetweenEmissions;
    public static Integer timeToEmissionPeek;
    public static Integer emissionPeekTime;
    public static Integer timeFromEmissionPeek;
    public static String lightningDirection;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        timeBetweenEmissions = TIME_BETWEEN.get();
        timeToEmissionPeek = TO_PEEK.get();
        emissionPeekTime = PEEK_TIME.get();
        timeFromEmissionPeek = FROM_PEEK.get();
        lightningDirection = LIGHTNING_DIR.get();
    }
}
