package varigata.hoard.mixin;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Random;

@Mixin(value = Entity.class, remap = false)
public abstract class LayerStepSoundFix{

    @Shadow
    public boolean noClip;
    @Final
    @Shadow
    public AxisAlignedBB boundingBox;
    @Shadow
    public double posX;
    @Shadow
    public double posY;
    @Shadow
    public double posZ;
    @Shadow
    public float yOffset;
    @Shadow
    public float ySize;
    @Shadow
    public boolean isInWeb;
    @Shadow
    public double motionX;
    @Shadow
    public double motionY;
    @Shadow
    public double motionZ;
    @Shadow
    public boolean onGround;
    @Shadow
    public World worldObj;
    @Shadow
    public boolean field_9293_aM;
    @Shadow
    public float stepHeight;
    @Shadow
    public boolean isCollidedHorizontally;
    @Shadow
    public boolean isCollidedVertically;
    @Shadow
    public boolean isCollided;
    @Shadow
    public Entity ridingEntity;
    @Shadow
    public float distanceWalkedModified;
    @Shadow
    public float prevDistanceWalkedModified;
    @Shadow
    public boolean isWalking;
    @Shadow
    private int nextStepDistance;
    @Shadow
    public int fire;
    @Shadow
    public int maxFire;

    @Shadow
    public int fireResistance;
    @Shadow
    protected Random rand;


    @Shadow
    public abstract boolean isSneaking();
    @Shadow
    protected void updateFallState(double y, boolean onGround) {
    }

    @Shadow
    public abstract boolean isWet();

    @Shadow
    protected abstract boolean canTriggerWalking();

    @Shadow
    protected abstract void dealFireDamage(int i);

    /**
     * @author Varigata
     * @reason Layer StepSounds are incorrectly handled only for snowlayers instead of generically.
     */
    @Overwrite
    public void moveEntity(double x, double y, double z) {
        if (this.noClip) {
            this.boundingBox.offset(x, y, z);
            this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0;
            this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
            this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0;
        } else {
            this.ySize *= 0.4F;
            double d3 = this.posX;
            double d4 = this.posZ;
            if (this.isInWeb) {
                this.isInWeb = false;
                x *= 0.25;
                y *= 0.05000000074505806;
                z *= 0.25;
                this.motionX = 0.0;
                this.motionY = 0.0;
                this.motionZ = 0.0;
            }

            double d5 = x;
            double d6 = y;
            double d7 = z;
            AxisAlignedBB axisalignedbb = this.boundingBox.copy();
            boolean flag = this.onGround && this.isSneaking();
            if (flag) {
                double d8;
                for(d8 = 0.05; x != 0.0 && this.worldObj.getCollidingBoundingBoxes((Entity)((Object)this), this.boundingBox.getOffsetBoundingBox(x, -1.0, 0.0)).size() == 0; d5 = x) {
                    if (x < d8 && x >= -d8) {
                        x = 0.0;
                    } else if (x > 0.0) {
                        x -= d8;
                    } else {
                        x += d8;
                    }
                }

                for(; z != 0.0 && this.worldObj.getCollidingBoundingBoxes((Entity)((Object)this), this.boundingBox.getOffsetBoundingBox(0.0, -1.0, z)).size() == 0; d7 = z) {
                    if (z < d8 && z >= -d8) {
                        z = 0.0;
                    } else if (z > 0.0) {
                        z -= d8;
                    } else {
                        z += d8;
                    }
                }
            }

            List list = this.worldObj.getCollidingBoundingBoxes((Entity)((Object)this), this.boundingBox.addCoord(x, y, z));

            for(int i = 0; i < list.size(); ++i) {
                y = ((AxisAlignedBB)list.get(i)).calculateYOffset(this.boundingBox, y);
            }

            this.boundingBox.offset(0.0, y, 0.0);
            if (!this.field_9293_aM && d6 != y) {
                z = 0.0;
                y = 0.0;
                x = 0.0;
            }

            boolean flag1 = this.onGround || d6 != y && d6 < 0.0;

            int k;
            for(k = 0; k < list.size(); ++k) {
                x = ((AxisAlignedBB)list.get(k)).calculateXOffset(this.boundingBox, x);
            }

            this.boundingBox.offset(x, 0.0, 0.0);
            if (!this.field_9293_aM && d5 != x) {
                z = 0.0;
                y = 0.0;
                x = 0.0;
            }

            for(k = 0; k < list.size(); ++k) {
                z = ((AxisAlignedBB)list.get(k)).calculateZOffset(this.boundingBox, z);
            }

            this.boundingBox.offset(0.0, 0.0, z);
            if (!this.field_9293_aM && d7 != z) {
                z = 0.0;
                y = 0.0;
                x = 0.0;
            }

            double oldY;
            int i3;
            double oldX;
            if (this.stepHeight > 0.0F && flag1 && (flag || this.ySize < 0.05F) && (d5 != x || d7 != z)) {
                oldX = x;
                oldY = y;
                double oldZ = z;
                x = d5;
                y = (double)this.stepHeight;
                z = d7;
                AxisAlignedBB axisalignedbb1 = this.boundingBox.copy();
                this.boundingBox.setBB(axisalignedbb);
                List list1 = this.worldObj.getCollidingBoundingBoxes((Entity)((Object)this), this.boundingBox.addCoord(d5, y, d7));

                for(i3 = 0; i3 < list1.size(); ++i3) {
                    y = ((AxisAlignedBB)list1.get(i3)).calculateYOffset(this.boundingBox, y);
                }

                this.boundingBox.offset(0.0, y, 0.0);
                if (!this.field_9293_aM && d6 != y) {
                    z = 0.0;
                    y = 0.0;
                    x = 0.0;
                }

                for(i3 = 0; i3 < list1.size(); ++i3) {
                    x = ((AxisAlignedBB)list1.get(i3)).calculateXOffset(this.boundingBox, x);
                }

                this.boundingBox.offset(x, 0.0, 0.0);
                if (!this.field_9293_aM && d5 != x) {
                    z = 0.0;
                    y = 0.0;
                    x = 0.0;
                }

                for(i3 = 0; i3 < list1.size(); ++i3) {
                    z = ((AxisAlignedBB)list1.get(i3)).calculateZOffset(this.boundingBox, z);
                }

                this.boundingBox.offset(0.0, 0.0, z);
                if (!this.field_9293_aM && d7 != z) {
                    z = 0.0;
                    y = 0.0;
                    x = 0.0;
                }

                if (!this.field_9293_aM && d6 != y) {
                    z = 0.0;
                    y = 0.0;
                    x = 0.0;
                } else {
                    y = (double)(-this.stepHeight);

                    for(i3 = 0; i3 < list1.size(); ++i3) {
                        y = ((AxisAlignedBB)list1.get(i3)).calculateYOffset(this.boundingBox, y);
                    }

                    this.boundingBox.offset(0.0, y, 0.0);
                }

                if (oldX * oldX + oldZ * oldZ >= x * x + z * z) {
                    x = oldX;
                    y = oldY;
                    z = oldZ;
                    this.boundingBox.setBB(axisalignedbb1);
                }
            }

            if (!this.worldObj.isMultiplayerAndNotHost || (Entity)((Object)this) instanceof EntityPlayer || !((Entity)((Object)this) instanceof EntityLiving)) {
                this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0;
                this.posY = this.boundingBox.minY + (double)this.yOffset - (double)this.ySize;
                this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0;
            }

            this.isCollidedHorizontally = d5 != x || d7 != z;
            this.isCollidedVertically = d6 != y;
            this.onGround = d6 != y && d6 < 0.0;
            this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
            this.updateFallState(y, this.onGround);
            if (d5 != x) {
                this.motionX = 0.0;
            }

            if (d6 != y) {
                this.motionY = 0.0;
            }

            if (d7 != z) {
                this.motionZ = 0.0;
            }

            oldX = this.posX - d3;
            oldY = this.posZ - d4;
            int j1;
            int l;
            int l1;
            int j3;
            if (this.canTriggerWalking() && !flag && this.ridingEntity == null) {
                this.distanceWalkedModified = (float)((double)this.distanceWalkedModified + (double)MathHelper.sqrt_double(oldX * oldX + oldY * oldY) * 0.6);
                l = MathHelper.floor_double(this.posX);
                j1 = MathHelper.floor_double(this.posY - 0.20000000298023224 - (double)this.yOffset);
                l1 = MathHelper.floor_double(this.posZ);
                j3 = this.worldObj.getBlockId(l, j1, l1);
                if (this.worldObj.getBlockId(l, j1 - 1, l1) == Block.fencePlanksOak.blockID) {
                    j3 = this.worldObj.getBlockId(l, j1 - 1, l1);
                }

                if (this.prevDistanceWalkedModified != this.distanceWalkedModified) {
                    this.isWalking = true;
                } else {
                    this.isWalking = false;
                }

                if (this.distanceWalkedModified > (float)this.nextStepDistance && j3 > 0) {
                    ++this.nextStepDistance;
                    StepSound stepsound = Block.blocksList[j3].stepSound;
                    // Modified code start
                    if (Block.getBlock(this.worldObj.getBlockId(l, j1 + 1, l1)) instanceof BlockLayerBase) {
                        stepsound = Block.getBlock(this.worldObj.getBlockId(l, j1 + 1, l1)).stepSound;
                        this.worldObj.playSoundAtEntity((Entity)((Object)this), stepsound.func_1145_d(), stepsound.getVolume() * 0.15F, stepsound.getPitch());
                    } else if (!Block.blocksList[j3].blockMaterial.getIsLiquid()) {
                        this.worldObj.playSoundAtEntity((Entity)((Object)this), stepsound.func_1145_d(), stepsound.getVolume() * 0.15F, stepsound.getPitch());
                    }

                    Block.blocksList[j3].onEntityWalking(this.worldObj, l, j1, l1, (Entity)((Object)this));
                }
            }

            l = MathHelper.floor_double(this.boundingBox.minX + 0.001);
            j1 = MathHelper.floor_double(this.boundingBox.minY + 0.001);
            l1 = MathHelper.floor_double(this.boundingBox.minZ + 0.001);
            j3 = MathHelper.floor_double(this.boundingBox.maxX - 0.001);
            i3 = MathHelper.floor_double(this.boundingBox.maxY - 0.001);
            int i4 = MathHelper.floor_double(this.boundingBox.maxZ - 0.001);
            if (this.worldObj.checkChunksExist(l, j1, l1, j3, i3, i4)) {
                for(int j4 = l; j4 <= j3; ++j4) {
                    for(int k4 = j1; k4 <= i3; ++k4) {
                        for(int l4 = l1; l4 <= i4; ++l4) {
                            int i5 = this.worldObj.getBlockId(j4, k4, l4);
                            if (i5 > 0) {
                                Block.blocksList[i5].onEntityCollidedWithBlock(this.worldObj, j4, k4, l4, (Entity)((Object)this));
                            }
                        }
                    }
                }
            }

            boolean flag2 = this.isWet();
            if (this.worldObj.isBoundingBoxBurning(this.boundingBox.getInsetBoundingBox(0.001, 0.001, 0.001))) {
                this.dealFireDamage(1);
                if (!flag2) {
                    ++this.fire;
                    if (this.fire == 0) {
                        this.fire = 300;
                    }

                    this.maxFire = this.fire;
                }
            } else if (this.fire <= 0) {
                this.fire = -this.fireResistance;
            }

            if (flag2 && this.fire > 0) {
                this.worldObj.playSoundAtEntity((Entity)((Object)this), "random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                this.fire = -this.fireResistance;
            }
        }
    }
}
