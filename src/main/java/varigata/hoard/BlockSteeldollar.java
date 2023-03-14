package varigata.hoard;

import net.minecraft.src.Block;
import net.minecraft.src.Material;

import java.util.Random;

public class BlockSteeldollar extends Block {
    public BlockSteeldollar(int i, Material material) {
        super(i, material);
    }

    public int idDropped(int i, Random random) {
        return Hoard.pileSteeldollar.itemID;
    }

    public int quantityDropped(int metadata, Random random) {
        return 8;
    }
}
