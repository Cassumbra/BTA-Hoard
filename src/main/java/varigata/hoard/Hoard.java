package varigata.hoard;

import net.fabricmc.api.ModInitializer;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLayerBase;
import net.minecraft.src.Item;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockHelper;
import turniplabs.halplibe.mixin.accessors.BlockAccessor;


public class Hoard implements ModInitializer {
    public static final String MOD_ID = "hoard";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Item pileSteeldollar = (new ItemPileSteeldollar(140)).setIconCoord(8, 3).setItemName("pile.steeldollar");

    public static final Block steeldollarBlock = BlockHelper.createBlock(MOD_ID, new Block(900, Material.sand), "steeldollar", "steeldollar_block.png", Block.soundGravelFootstep, 0.1f, 0.1f, 0.0f);
    //public static final Block layerSteeldollar = (new BlockLayerSteeldollar(901, Material.sand)).setBlockName("layer.steeldollar").setTexCoords(2, 4).setPlaceOverwrites();//.setIsLitInteriorSurface(true);
    public static final Block layerSteeldollar = new BlockLayerSteeldollar(901, Material.sand).setBlockName("layer.steeldollar").setTexCoords(2, 4).setPlaceOverwrites();//.setNotInCreativeMenu();

    static {
        ((BlockAccessor) layerSteeldollar).callSetHardness(0.1f);
        ((BlockAccessor) layerSteeldollar).callSetStepSound(Block.soundGlassFootstep);
        ((BlockLayerBase) layerSteeldollar).setFullBlockID(steeldollarBlock.blockID);
    }



    @Override
    public void onInitialize() {
        LOGGER.info("Hoard initialized.");
        //Item.itemsList[pileSteeldollar.itemID] = pileSteeldollar;
        Item.itemsList[layerSteeldollar.blockID] = new ItemBlock(layerSteeldollar.blockID - Block.blocksList.length);
    }
}
