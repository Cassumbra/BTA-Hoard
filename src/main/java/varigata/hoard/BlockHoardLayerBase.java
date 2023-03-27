package varigata.hoard;
import net.minecraft.src.*;

import java.util.Random;


public class BlockHoardLayerBase extends BlockLayerBase {


    public int pileID = -1;

    public static boolean fallInstantly = false;


    public void onBlockAdded(World world, int i, int j, int k) {
        world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate());
    }



    public void updateTick(World world, int i, int j, int k, Random random) {
        this.tryToFall(world, i, j, k);
    }

    private void tryToFall(World world, int i, int j, int k) {
        if (canFallBelow(world, i, j - 1, k) && j >= 0) {
            byte byte0 = 32;
            if (!fallInstantly && world.checkChunksExist(i - byte0, j - byte0, k - byte0, i + byte0, j + byte0, k + byte0)) {
                //this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F * (world.getBlockMetadata(i, j, k) + 1), 1.0F);
                EntityFallingLayer entityfallinglayer = new EntityFallingLayer(world, (double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), this.blockID, world.getBlockMetadata(i, j, k));
                world.entityJoinedWorld(entityfallinglayer);
            } else {
                world.setBlockWithNotify(i, j, k, 0);

                while(canFallBelow(world, i, j - 1, k) && j > 0) {
                    --j;
                }

                if (j > 0) {
                    world.setBlockWithNotify(i, j, k, this.blockID);
                }
            }
        }

    }

    public int tickRate() {
        return 3;
    }

    public static boolean canFallBelow(World world, int i, int j, int k) {
        int l = world.getBlockId(i, j, k);
        if (l == 0) {
            return true;
        } else if (Block.getBlock(l) instanceof BlockLayerBase) {
        //} else if (l == world.getBlockId(i, j + 1, k)) {
            return true;
        } else if (l == Block.fire.blockID) {
            return true;
        } else {
            Material material = Block.blocksList[l].blockMaterial;
            if (material == Material.water) {
                return true;
            } else {
                return material == Material.lava;
            }
        }
    }

    public Block setMaterialID(int materialID) {
        this.pileID = materialID;
        return this;
    }

    public BlockHoardLayerBase(int i, Material material) {
        super(i, material);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setTickOnLoad(true);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        int l = world.getBlockMetadata(i, j, k) & 7;
        float f = (float)(2 * (1 + l)) / 16.0F;
        //return AxisAlignedBB.getBoundingBoxFromPool((double)i + this.minX, (double)j + this.minY, (double)k + this.minZ, (double)i + this.maxX, (double)((float)j + f - 0.125F), (double)k + this.maxZ);
        return AxisAlignedBB.getBoundingBoxFromPool((double)i + this.minX, (double)j + this.minY, (double)k + this.minZ,
                (double)i + this.maxX, (double)((float)j + f), (double)k + this.maxZ);
    }

    public void accumulate(World world, int x, int y, int z) {
        int myMetadata = world.getBlockMetadata(x, y, z);
        if (myMetadata != 7) {
            myMetadata++;
            world.setBlockMetadata(x, y, z, myMetadata);
            if (myMetadata == 7) {
                world.setBlock(x, y, z, fullBlockID);//Hoard.steeldollarBlock.blockID);
            }
            world.markBlockNeedsUpdate(x, y, z);
        }
    }

    /// Layers are given to ijk
    public static void giveLayers(World world, int x, int y, int z, int i, int j, int k) {

        int giveMetadata = world.getBlockMetadata(x, y, z);
        int takeMetadata = world.getBlockMetadata(i, j, k);
        world.setBlockMetadata(i, j, k, takeMetadata + giveMetadata + 1);
        takeMetadata = world.getBlockMetadata(i, j, k);
        if (takeMetadata > 7) {
            int remainder = takeMetadata - 7;
            world.setBlockMetadata(x, y, z, remainder - 1);
            world.setBlockMetadata(i, j, k, 7);
        }
        else {
            world.setBlock(x, y, z, 0);
        }

        if (world.getBlockMetadata(i, j, k) == 7) {
            int fullBlockID = ((BlockHoardLayerBase)Block.getBlock(world.getBlockId(i, j, k))).fullBlockID;
            world.setBlockWithNotify(i, j, k, fullBlockID);
        }
        // I don't know if this one is necessary?
        if (world.getBlockMetadata(x, y, z) == 7) {
            int fullBlockID = ((BlockHoardLayerBase)Block.getBlock(world.getBlockId(x, y, z))).fullBlockID;
            world.setBlockWithNotify(x, y, z, fullBlockID);
        }
        world.markBlockNeedsUpdate(i, j, k);
        world.markBlockNeedsUpdate(x, y, z);
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k) {
        return true;
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
        world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate());
    }

    public int idDropped(int i, Random random) {
        return pileID;
    }

    public int quantityDropped(int metadata, Random random) {
        //return 0;
        return metadata + 1;
    }
}
