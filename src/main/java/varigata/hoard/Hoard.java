package varigata.hoard;

import net.fabricmc.api.ModInitializer;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLayerBase;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.HalpLibe;
import turniplabs.halplibe.helper.*;
import turniplabs.halplibe.mixin.accessors.BlockAccessor;


public class Hoard implements ModInitializer {
    public static final String MOD_ID = "hoard";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Item pileSteeldollar = (new ItemPileSteeldollar(140)).setIconCoord(8, 3).setItemName("pile.steeldollar");
    //public static final Block steeldollarBlock = BlockHelper.createBlock(MOD_ID, new Block(900, Material.sand), "steeldollar", "steeldollar_block_topbottom.png", "steeldollar_block_topbottom.png", "steeldollar_block_sides.png", "steeldollar_block_sides_2.png", "steeldollar_block_sides.png", "steeldollar_block_sides_2.png", Block.soundGravelFootstep, 0.1f, 0.1f, 0.0f);
    public static final Block steeldollarBlock = new BlockSteelDollar(900, Material.sand).setBlockName("block.steeldollar");
    public static final Block layerSteeldollar = new BlockLayerSteeldollar(901, Material.sand).setBlockName("layer.steeldollar").setNotInCreativeMenu();



    static {
        ((BlockAccessor) steeldollarBlock).callSetHardness(3.0f);
        ((BlockAccessor) steeldollarBlock).callSetResistance(5.0f);
        ((BlockAccessor) steeldollarBlock).callSetStepSound(Block.soundMetalFootstep);

        ((BlockAccessor) layerSteeldollar).callSetHardness(3.0f);
        ((BlockAccessor) steeldollarBlock).callSetResistance(5.0f);
        ((BlockAccessor) layerSteeldollar).callSetStepSound(Block.soundMetalFootstep);
        ((BlockLayerBase) layerSteeldollar).setFullBlockID(steeldollarBlock.blockID);
    }



    @Override
    public void onInitialize() {
        LOGGER.info("Hoard initialized.");
        Item.itemsList[steeldollarBlock.blockID] = new ItemBlock(steeldollarBlock.blockID - Block.blocksList.length);
        Item.itemsList[layerSteeldollar.blockID] = new ItemBlock(layerSteeldollar.blockID - Block.blocksList.length);

        // Recipes
        //RecipeHelper.Crafting

        // Block Textures
        // Steeldollar
        int[] topbottom = BlockCoords.nextCoords();
        TextureHelper.addTextureToTerrain(MOD_ID, "steeldollar_block_topbottom.png", topbottom[0], topbottom[1]);
        int[] sides_1 = BlockCoords.nextCoords();
        TextureHelper.addTextureToTerrain(MOD_ID, "steeldollar_block_sides.png", sides_1[0], sides_1[1]);
        int[] sides_2 = BlockCoords.nextCoords();
        TextureHelper.addTextureToTerrain(MOD_ID, "steeldollar_block_sides_2.png", sides_2[0], sides_2[1]);
        layerSteeldollar.setTexCoords(topbottom[0], topbottom[1], topbottom[0], topbottom[1], sides_1[0], sides_1[1], sides_2[0], sides_2[1], sides_1[0], sides_1[1], sides_2[0], sides_2[1]);
        steeldollarBlock.setTexCoords(topbottom[0], topbottom[1], topbottom[0], topbottom[1], sides_1[0], sides_1[1], sides_2[0], sides_2[1], sides_1[0], sides_1[1], sides_2[0], sides_2[1]);

        // Item Textures
        // Steeldollar
        int[] one = ItemCoords.nextCoords();
        TextureHelper.addTextureToItems(MOD_ID, "steeldollar_pile.png", one[0], one[1]);
        pileSteeldollar.setIconCoord(one[0], one[1]).setItemName(HalpLibe.addModId(MOD_ID, "pile.steeldollar"));

    }
}
