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
import varigata.hoard.Hoard;

@Mixin(value = EntityTrackerEntry.class, remap = false)
public class EntityTracker {

    @Shadow public Entity trackedEntity;

    @Inject(method = "getSpawnPacket", at = @At(value = "HEAD"), cancellable = true)
    void hoard_getSpawnPacket(CallbackInfoReturnable<Packet> cir) {
        if (this.trackedEntity instanceof EntityFallingLayer) {
            EntityFallingLayer entityfallinglayer = (EntityFallingLayer)this.trackedEntity;

            Packet23VehicleSpawn packet = new Packet23VehicleSpawn(this.trackedEntity, 0);

            if (entityfallinglayer.blockID == Hoard.layerSteeldollar.blockID) {
                packet.type = 80;
            }
            else if (entityfallinglayer.blockID == Hoard.layerRimmedDucat.blockID) {
                packet.type = 81;
            }
            else if (entityfallinglayer.blockID == Hoard.layerGlittermix.blockID) {
                packet.type = 82;
            }
            else if (entityfallinglayer.blockID == Hoard.layerRedQuartz.blockID) {
                packet.type = 83;
            }
            else {
                packet.type = 84;
            }

            packet.field_28044_i = entityfallinglayer.metadata;

            cir.setReturnValue(packet);
            return;
        }
    }


}
