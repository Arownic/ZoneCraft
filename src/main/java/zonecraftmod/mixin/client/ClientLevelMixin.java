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
public abstract class ClientLevelMixin extends Level {
    protected ClientLevelMixin(WritableLevelData p_270739_, ResourceKey<net.minecraft.world.level.Level> p_270683_, RegistryAccess p_270200_, Holder<DimensionType> p_270240_, Supplier<ProfilerFiller> p_270692_, boolean p_270904_, boolean p_270470_, long p_270248_, int p_270466_) {
        super(p_270739_, p_270683_, p_270200_, p_270240_, p_270692_, p_270904_, p_270470_, p_270248_, p_270466_);
    }

    @Inject(at = @At("HEAD"), method = "getSkyColor", cancellable = true)
    public void zonecraft$colorsky(Vec3 p_171661_, float tick, CallbackInfoReturnable<Vec3> cir) {
        if (Boolean.TRUE.equals(ClientEmission.getEmissionActivity()) & ClientEmission.getEmissionColor() != null) {
            cir.setReturnValue(ClientEmission.getEmissionColor());
        }
        //int flashTime = ((ClientLevel) (Object) this).getSkyFlashTime();
        // add lightning flash effects client
    }
}
