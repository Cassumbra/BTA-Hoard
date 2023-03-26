package varigata.hoard.mixin;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityTrackerEntry;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet23VehicleSpawn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import varigata.hoard.EntityFallingLayer;

@Mixin(value = EntityTrackerEntry.class, remap = false)
public class EntityTracker {

    @Shadow public Entity trackedEntity;

    @Inject(method = "getSpawnPacket", at = @At(value = "HEAD"), cancellable = true)
    void hoard_getSpawnPacket(CallbackInfoReturnable<Packet> cir) {
        if (this.trackedEntity instanceof EntityFallingLayer) {
            //EntityFallingLayer entityfallinglayer = (EntityFallingLayer)this.trackedEntity;
            cir.setReturnValue(new Packet23VehicleSpawn(this.trackedEntity, 70));
            return;
        }
    }
}
