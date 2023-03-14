package varigata.hoard;

import net.minecraft.src.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static varigata.hoard.Hoard.MOD_ID;

public class ItemPileSteeldollar extends Item {

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public ItemPileSteeldollar(int i) {
        super(i);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l, double heightPlaced) {
        if (world.getBlockId(i, j, k) == Hoard.layerSteeldollar.blockID && l == 1) {
            itemstack.consumeItem(entityplayer);
            ((BlockLayerSteeldollar)Hoard.layerSteeldollar).accumulate(world, i, j, k );

            return true;
        }

        if (world.getBlockId(i, j, k) != Block.layerSnow.blockID) {
            if (l == 0) {
                --j;
            }

            if (l == 1) {
                ++j;
            }

            if (l == 2) {
                --k;
            }

            if (l == 3) {
                ++k;
            }

            if (l == 4) {
                --i;
            }

            if (l == 5) {
                ++i;
            }

            if (!world.isAirBlock(i, j, k)) {
                if (world.getBlockId(i, j, k) == Hoard.layerSteeldollar.blockID) {
                    itemstack.consumeItem(entityplayer);
                    ((BlockLayerSteeldollar)Hoard.layerSteeldollar).accumulate(world, i, j, k );

                    return true;
                }
                else {
                    return false;
                }
            }
        }

        if (Hoard.layerSteeldollar.canPlaceBlockAt(world, i, j, k)) {
            itemstack.consumeItem(entityplayer);
            // TODO: Add placement sound
            //world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), Block.wireRedstone.stepSound.func_1145_d(), (Block.wireRedstone.stepSound.getVolume() + 1.0F) / 2.0F, Block.wireRedstone.stepSound.getPitch() * 0.8F);
            world.setBlockWithNotify(i, j, k, Hoard.layerSteeldollar.blockID);
        }

        return true;

    }
}
