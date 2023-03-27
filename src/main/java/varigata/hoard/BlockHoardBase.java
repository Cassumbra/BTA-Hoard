package varigata.hoard;

import net.minecraft.src.Block;
import net.minecraft.src.BlockLayerBase;
import net.minecraft.src.Material;
import net.minecraft.src.World;

import java.util.Random;

public class BlockHoardBase extends Block {

    public int layerID = -1;
    public int pileID = -1;

    public static boolean fallInstantly = false;

    public BlockHoardBase(int i, Material material) {
        super(i, material);
    }

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
                //this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                EntityFallingLayer entityfallinglayer = new EntityFallingLayer(world, (double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), this.layerID, 7);
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

    public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
        world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate());
    }

    public Block setMaterialID(int materialID) {
        this.pileID = materialID;
        return this;
    }

    public Block setLayerID(int layerID) {
        this.layerID = layerID;
        return this;
    }

    public int idDropped(int i, Random random) {
        return pileID;
    }

    public int quantityDropped(int metadata, Random random) {
        return 8;
    }


}
