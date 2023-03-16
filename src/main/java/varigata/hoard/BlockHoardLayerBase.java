package varigata.hoard;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLayerBase;
import net.minecraft.src.BlockLeavesBase;
import net.minecraft.src.Material;
import net.minecraft.src.World;

import java.util.Random;


public class BlockHoardLayerBase extends BlockLayerBase {


    public int pileID = -1;

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

    public boolean canPlaceBlockAt(World world, int i, int j, int k) {
        int l = world.getBlockId(i, j - 1, k);
        if (l == 0 || !Block.blocksList[l].isOpaqueCube() && !(Block.blocksList[l] instanceof BlockLeavesBase)) {
            // Change this, later, so that we can place blocks in air and have them fall.
            return false;
        }
        //else if (l == 901) {//Block.BlockLayerSteeldollar.blockID) {
        //    ((BlockLayerSteeldollar)Hoard.layerSteeldollar).accumulate(world, i, j - 1, k );
        //}
        else {
            Material material = world.getBlockMaterial(i, j - 1, k);
            return material == Material.leaves || material.getIsSolid();
        }
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
        this.func_314_h(world, i, j, k);
    }

    private boolean func_314_h(World world, int i, int j, int k) {
        if (!this.canPlaceBlockAt(world, i, j, k)) {
            this.dropBlockAsItem(world, i, j, k, world.getBlockMetadata(i, j, k));
            world.setBlockWithNotify(i, j, k, 0);
            return false;
        } else {
            return true;
        }
    }

    public int idDropped(int i, Random random) {
        return pileID;
    }

    public int quantityDropped(int metadata, Random random) {
        //return 0;
        return metadata + 1;
    }
}
