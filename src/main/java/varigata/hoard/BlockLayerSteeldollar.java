package varigata.hoard;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLayerBase;
import net.minecraft.src.BlockLeavesBase;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

import static varigata.hoard.Hoard.MOD_ID;


public class BlockLayerSteeldollar extends BlockLayerBase {

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public BlockLayerSteeldollar(int i, Material material) {
        super(i, material);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
        this.setTickOnLoad(true);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        int l = world.getBlockMetadata(i, j, k) & 7;
        float f = (float)(2 * (1 + l)) / 16.0F;
        return AxisAlignedBB.getBoundingBoxFromPool((double)i + this.minX, (double)j + this.minY, (double)k + this.minZ, (double)i + this.maxX, (double)((float)j + f - 0.125F), (double)k + this.maxZ);
    }

    public void accumulate(World world, int x, int y, int z) {
        int myMetadata = world.getBlockMetadata(x, y, z);
        if (myMetadata != 7) {
            myMetadata++;
            world.setBlockMetadata(x, y, z, myMetadata);
            if (myMetadata == 7) {
                world.setBlock(x, y, z, Hoard.steeldollarBlock.blockID);
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

    public void harvestBlock(World world, EntityPlayer entityplayer, int i, int j, int k, int l) {
        super.harvestBlock(world, entityplayer, i, j, k, l);
        int i1 = Item.ammoSnowball.itemID;
        float f = 0.7F;
        double d = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5;
        double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5;
        double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5;
        EntityItem entityitem = new EntityItem(world, (double)i + d, (double)j + d1, (double)k + d2, new ItemStack(i1, 1, 0));
        entityitem.delayBeforeCanPickup = 10;
        world.entityJoinedWorld(entityitem);
    }

    public int idDropped(int i, Random random) {
        return Item.ammoSnowball.itemID;
    }

    public int quantityDropped(int metadata, Random random) {
        return 0;
    }

    //public void updateTick(World world, int i, int j, int k, Random random) {

    //}
}
