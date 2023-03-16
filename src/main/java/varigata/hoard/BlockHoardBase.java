package varigata.hoard;

import net.minecraft.src.Block;
import net.minecraft.src.Material;

import java.util.Random;

public class BlockHoardBase extends Block {
    public BlockHoardBase(int i, Material material) {
        super(i, material);
    }

    public int pileID = -1;

    public Block setMaterialID(int materialID) {
        this.pileID = materialID;
        return this;
    }

    public int idDropped(int i, Random random) {
        return pileID;
    }

    public int quantityDropped(int metadata, Random random) {
        return 8;
    }
}
