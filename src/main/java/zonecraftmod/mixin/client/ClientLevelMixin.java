package zonecraftmod.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import zonecraftmod.client.ClientEmission;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public class ClientLevelMixin {
    @Inject(at = @At("HEAD"), method = "getSkyColor", cancellable = true)
    public void zonecraft$colorsky(Vec3 p_171661_, float tick, CallbackInfoReturnable<Vec3> cir) {
        if (ClientEmission.getEmissionActivity() && ClientEmission.getEmissionColor() != null) {
            cir.setReturnValue(ClientEmission.getEmissionColor());
        }
        //int flashTime = ((ClientLevel) (Object) this).getSkyFlashTime();
        // add lightning flash effects client
    }
}
