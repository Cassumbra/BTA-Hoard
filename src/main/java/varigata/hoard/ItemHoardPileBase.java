package varigata.hoard;

import net.minecraft.src.*;

public class ItemHoardPileBase extends Item {

    public void setLayerID(int layerID) {
        this.layerID = layerID;
    }

    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }

    public int layerID = -1;
    public int blockID = -1;


    public ItemHoardPileBase(int i) {
        super(i);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, double heightPlaced) {
        boolean placed = false;
        // Placing onto top of stack
        if (world.getBlockId(i, j, k) == layerID && l == 1) {
            ((BlockHoardLayerBase)Block.getBlock(layerID)).accumulate(world, i, j, k );

            placed = true;
        }
        // Placing from side of a block
        else if (world.getBlockId(i, j, k) != blockID) {

            switch (l){
                case 0: --j; break;
                case 1: ++j; break;
                case 2: --k; break;
                case 3: ++k; break;
                case 4: --i; break;
                case 5: ++i; break;
            }

            if (!world.isAirBlock(i, j, k)) {
                if (world.getBlockId(i, j, k) == layerID) {
                    ((BlockHoardLayerBase)Block.getBlock(layerID)).accumulate(world, i, j, k );

                    placed = true;
                }
                else {
                    return false;
                }
            }
        }
        if (!placed && Block.getBlock(layerID).canPlaceBlockAt(world, i, j, k)) {
            world.setBlockWithNotify(i, j, k, layerID);
            placed = true;
        }

        if (placed) {
            itemstack.consumeItem(entityplayer);
            world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), "step.gold1", (Block.getBlock(layerID).stepSound.getVolume() + 1.0F) / 2.0F, Block.getBlock(layerID).stepSound.getPitch() * 0.8F);
            //world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), Block.getBlock(layerID).stepSound.func_1145_d(), (Block.getBlock(layerID).stepSound.getVolume() + 1.0F) / 2.0F, Block.getBlock(layerID).stepSound.getPitch() * 0.8F);
        }

        return placed;
    }
}
