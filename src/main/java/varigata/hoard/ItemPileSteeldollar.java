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
        boolean placed = false;
        // Placing onto top of stack
        if (world.getBlockId(i, j, k) == Hoard.layerSteeldollar.blockID && l == 1) {
            ((BlockLayerSteeldollar)Hoard.layerSteeldollar).accumulate(world, i, j, k );

            placed = true;
        }
        // Placing from side of a block
        else if (world.getBlockId(i, j, k) != Block.layerSnow.blockID) {

            switch (l){
                case 0: --j; break;
                case 1: ++j; break;
                case 2: --k; break;
                case 3: ++k; break;
                case 4: --i; break;
                case 5: ++i; break;
            }

            if (!world.isAirBlock(i, j, k)) {
                if (world.getBlockId(i, j, k) == Hoard.layerSteeldollar.blockID) {
                    ((BlockLayerSteeldollar)Hoard.layerSteeldollar).accumulate(world, i, j, k );

                    placed = true;
                }
                else {
                    return false;
                }
            }
        }
        if (!placed && Hoard.layerSteeldollar.canPlaceBlockAt(world, i, j, k)) {
            world.setBlockWithNotify(i, j, k, Hoard.layerSteeldollar.blockID);
            placed = true;
        }

        if (placed) {
            itemstack.consumeItem(entityplayer);
            world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), Hoard.layerSteeldollar.stepSound.func_1145_d(), (Hoard.layerSteeldollar.stepSound.getVolume() + 1.0F) / 2.0F, Hoard.layerSteeldollar.stepSound.getPitch() * 0.8F);
        }

        return placed;
    }
}
