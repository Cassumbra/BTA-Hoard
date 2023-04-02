package varigata.hoard;

import net.minecraft.src.*;

public class EntityFallingLayer extends EntityFallingSand {

    private int pileID = -1;
    private int fullBlockID = -1;
    public int metadata = 0;

    public EntityFallingLayer(World world) {
        super(world);
        this.blockID = Block.sand.blockID;
        this.fallTime = 0;
    }

    public EntityFallingLayer(World world, double d, double d1, double d2, int i, int metadata) {
        super(world);
        this.fallTime = 0;
        this.blockID = i;
        this.metadata = metadata;
        this.pileID = ((BlockHoardLayerBase)Block.getBlock(blockID)).pileID;
        this.fullBlockID = ((BlockHoardLayerBase)Block.getBlock(blockID)).fullBlockID;
        this.preventEntitySpawning = true;
        this.setSize(0.98F, 0.98F);
        this.yOffset = this.height / 2.0F;
        this.setPosition(d, d1, d2);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = d;
        this.prevPosY = d1;
        this.prevPosZ = d2;
    }

    public void onUpdate() {
        if (this.blockID == 0) {
            this.setEntityDead();
        } else {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            ++this.fallTime;
            this.motionY -= 0.03999999910593033;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9800000190734863;
            this.motionY *= 0.9800000190734863;
            this.motionZ *= 0.9800000190734863;
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.posY);
            int k = MathHelper.floor_double(this.posZ);

            int idHere = this.worldObj.getBlockId(i, j, k);

            if (idHere == this.blockID || idHere == this.fullBlockID) {
                // Start of fall
                if (this.fallTime < 2) {
                    this.worldObj.setBlockWithNotify(i, j, k, 0);
                }
                // Two falling layers have overlapped while falling/landing. Try to merge them.
                else if (idHere == this.blockID) {
                    if (this.worldObj.canBlockBePlacedAt(this.blockID, i, j+1, k, true, 1)) {
                        this.worldObj.setBlockAndMetadataWithNotify(i, j+1, k, this.blockID, this.metadata);
                        BlockHoardLayerBase.giveLayers(this.worldObj, i, j+1, k, i, j, k);

                        this.setEntityDead();
                    }
                }
            }

            if (this.onGround && !this.isDead) {
                this.motionX *= 0.699999988079071;
                this.motionZ *= 0.699999988079071;
                this.motionY *= -0.5;
                this.setEntityDead();

                boolean canPlaceBlock = this.worldObj.canBlockBePlacedAt(this.blockID, i, j, k, true, 1);
                boolean canFallBelow = BlockHoardLayerBase.canFallBelow(this.worldObj, i, j - 1, k);
                boolean blockMatches = this.worldObj.getBlockId(i, j-1, k) != this.blockID;

                if ((!canPlaceBlock || (canFallBelow && blockMatches))) {
                    if (!this.worldObj.isMultiplayerAndNotHost) {
                        this.dropItem(this.pileID, metadata + 1);
                    }
                } else {
                    boolean trySetBlock = this.worldObj.setBlockAndMetadataWithNotify(i, j, k, this.blockID, this.metadata);

                    if (trySetBlock) {
                        if (this.worldObj.getBlockId(i, j - 1, k) == this.blockID) {
                            BlockHoardLayerBase.giveLayers(this.worldObj, i, j, k, i, j-1, k);
                        }
                        if (this.worldObj.getBlockMetadata(i, j, k) == 7) {
                            this.worldObj.setBlockWithNotify(i, j, k, this.fullBlockID);
                        }
                    }
                }

                /*
                if ((!canPlaceBlock || (canFallBelow && blockMatches)) && !this.worldObj.isMultiplayerAndNotHost) {
                    this.dropItem(this.pileID, metadata + 1);
                } else {

                    boolean trySetBlock = false;
                    if (!(!canPlaceBlock || (canFallBelow && blockMatches))) {
                        trySetBlock = this.worldObj.setBlockAndMetadataWithNotify(i, j, k, this.blockID, this.metadata);
                    }

                    if (trySetBlock) {
                        if (this.worldObj.getBlockId(i, j - 1, k) == this.blockID) {
                            BlockHoardLayerBase.giveLayers(this.worldObj, i, j, k, i, j-1, k);
                        }
                        if (this.worldObj.getBlockMetadata(i, j, k) == 7) {
                            this.worldObj.setBlockWithNotify(i, j, k, this.fullBlockID);
                        }
                    }
                }
                 */


            } else if (this.fallTime > 100 && !this.worldObj.isMultiplayerAndNotHost) {
                // TODO: Make this drop the proper thingies

                this.dropItem(this.pileID, metadata + 1);
                this.setEntityDead();
            }

        }
    }


    //else if (l == world.getBlockId(i, j - 1, k)) {
    //    int empty_spots = 7 - world.getBlockMetadata(i, j - 1, k);
    //    if
    //    world.setBlockMetadata(i, j - 1, k)
}
