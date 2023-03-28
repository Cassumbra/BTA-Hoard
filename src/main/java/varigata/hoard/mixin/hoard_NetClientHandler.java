package varigata.hoard.mixin;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import varigata.hoard.EntityFallingLayer;
import varigata.hoard.Hoard;

@Mixin(value = NetClientHandler.class, remap = false)
public abstract class hoard_NetClientHandler {

    @Shadow private WorldClient worldClient;

    @Shadow protected abstract Entity getEntityByID(int i);

    @Inject(method = "handleVehicleSpawn", at = @At(value = "HEAD"), cancellable = true)
    void hoard_handleVehicleSpawn(Packet23VehicleSpawn packet23vehiclespawn, CallbackInfo ci) {
        double d = (double)packet23vehiclespawn.xPosition / 32.0;
        double d1 = (double)packet23vehiclespawn.yPosition / 32.0;
        double d2 = (double)packet23vehiclespawn.zPosition / 32.0;
        Entity obj = null;

        int blockid = 0; //Hoard.layerSteeldollar.blockID;//0;

        switch (packet23vehiclespawn.type) {
            case 80: blockid = Hoard.layerSteeldollar.blockID; break;
            case 81: blockid = Hoard.layerRimmedDucat.blockID; break;
            case 82: blockid = Hoard.layerGlittermix.blockID; break;
            case 83: blockid = Hoard.layerRedQuartz.blockID; break;
        }

        if (blockid != 0) {
            obj = new EntityFallingLayer(this.worldClient, d, d1, d2, blockid, packet23vehiclespawn.field_28044_i);

            // Not sure if this is what is needed? We'll see if it fixes things or not.
            ((Entity)obj).serverPosX = packet23vehiclespawn.xPosition;
            ((Entity)obj).serverPosY = packet23vehiclespawn.yPosition;
            ((Entity)obj).serverPosZ = packet23vehiclespawn.zPosition;
            ((Entity)obj).rotationYaw = packet23vehiclespawn.yaw;
            ((Entity)obj).rotationPitch = packet23vehiclespawn.pitch;
            ((Entity)obj).entityId = packet23vehiclespawn.entityId;
            this.worldClient.func_712_a(packet23vehiclespawn.entityId, (Entity)obj);

            // What is this? Is it necessary even?
            if (packet23vehiclespawn.field_28044_i > 0) {
                Entity entity;

                ((Entity)obj).setVelocity((double)packet23vehiclespawn.field_28047_e / 8000.0, (double)packet23vehiclespawn.field_28046_f / 8000.0, (double)packet23vehiclespawn.field_28045_g / 8000.0);


            }
            ci.cancel();
            return;

        }
    }
}
