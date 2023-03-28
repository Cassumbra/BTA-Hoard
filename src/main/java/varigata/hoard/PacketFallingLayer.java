package varigata.hoard;

import net.minecraft.src.*;

public class PacketFallingLayer extends Packet23VehicleSpawn {

    public int metadata;

    public PacketFallingLayer(Entity entity, int i) {
        this(entity, i, 0);
    }

    public PacketFallingLayer(Entity entity, int i, int j) {
        this.metadata = ((EntityFallingLayer)entity).metadata;
        this.entityId = entity.entityId;
        this.xPosition = MathHelper.floor_double(entity.posX * 32.0);
        this.yPosition = MathHelper.floor_double(entity.posY * 32.0);
        this.zPosition = MathHelper.floor_double(entity.posZ * 32.0);
        this.pitch = entity.rotationPitch;
        this.yaw = entity.rotationYaw;
        this.type = i;
        this.field_28044_i = j;
        if (j > 0) {
            double d = entity.motionX;
            double d1 = entity.motionY;
            double d2 = entity.motionZ;
            double d3 = 3.9;
            if (d < -d3) {
                d = -d3;
            }

            if (d1 < -d3) {
                d1 = -d3;
            }

            if (d2 < -d3) {
                d2 = -d3;
            }

            if (d > d3) {
                d = d3;
            }

            if (d1 > d3) {
                d1 = d3;
            }

            if (d2 > d3) {
                d2 = d3;
            }

            this.field_28047_e = (int)(d * 8000.0);
            this.field_28046_f = (int)(d1 * 8000.0);
            this.field_28045_g = (int)(d2 * 8000.0);
        }

        //if (entity instanceof EntityArrow) {
        //    this.arrowType = ((EntityArrow)entity).arrowType;
        //}

    }

    public void processPacket(NetHandler nethandler) {
        nethandler.handleVehicleSpawn(this);
    }
}
