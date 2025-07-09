package zonecraftmod;

import com.mojang.logging.LogUtils;
import de.maxhenkel.admiral.MinecraftAdmiral;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import zonecraftmod.commands.EmissionCommand;
import zonecraftmod.config.ClientConfig;
import zonecraftmod.config.ServerConfig;
import zonecraftmod.network.PacketHandler;

@Mod(ZoneCraftMod.MODID)
public class ZoneCraftMod {
    public static final String MODID = "zonecraft";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ZoneCraftMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent evt) {
        evt.enqueueWork(PacketHandler::register);
    }

    @SubscribeEvent
    public void serverTick(TickEvent.ServerTickEvent event) {
        if (Emission.isEmissionActive() && event.phase.equals(TickEvent.Phase.END)) {
            Emission.emissionTick();
        }
    }

    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent evt) {
        MinecraftAdmiral.builder(evt.getDispatcher(), evt.getBuildContext()).addCommandClasses(EmissionCommand.class).build();
    }
}
