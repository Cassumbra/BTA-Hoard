package varigata.hoard;

import net.fabricmc.api.ModInitializer;
import net.minecraft.src.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.*;

import java.util.ArrayList;
import java.util.List;


public class Hoard implements ModInitializer {

    public static final String MOD_ID = "hoard";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);



    // Items
    public static final Item pileSteeldollar = ItemHelper.createItem(MOD_ID, new ItemHoardPileBase(140), "pile.steeldollar", "steeldollar_pile.png");
    public static final Item steeldollar = ItemHelper.createItem(MOD_ID, new Item(141), "steeldollar", "steeldollar.png");

    // Blocks
    public static final Block steeldollarBlock = BlockHelper.createBlock(MOD_ID, new BlockHoardBase(900, Material.sand), "block.steeldollar",
            "steeldollar_tex_y.png", "steeldollar_tex_y.png", "steeldollar_tex_x.png", "steeldollar_tex_z.png", "steeldollar_tex_x.png", "steeldollar_tex_z.png",
            Block.soundMetalFootstep, 3.0F, 5.0F, 0);


    public static final Block layerSteeldollar = BlockHelper.createBlock(MOD_ID, new BlockHoardLayerBase(901, Material.sand), "layer.steeldollar",
            "steeldollar_tex_y.png", "steeldollar_tex_y.png", "steeldollar_tex_x.png", "steeldollar_tex_z.png", "steeldollar_tex_x.png", "steeldollar_tex_z.png",
            Block.soundMetalFootstep, 3.0F, 5.0F, 0).setNotInCreativeMenu();


    static {
        ((ItemHoardPileBase) pileSteeldollar).setLayerID(layerSteeldollar.blockID);
        ((ItemHoardPileBase) pileSteeldollar).setLayerID(layerSteeldollar.blockID);
        ((BlockHoardBase) steeldollarBlock).setMaterialID(pileSteeldollar.itemID);
        ((BlockHoardLayerBase) layerSteeldollar).setMaterialID(pileSteeldollar.itemID);
        ((BlockLayerBase) layerSteeldollar).setFullBlockID(steeldollarBlock.blockID);
    }



    @Override
    public void onInitialize() {
        LOGGER.info("Hoard initialized.");

        RecipeHelper.Crafting.createRecipe(pileSteeldollar, 1, new Object[]{"###", "ABA", "###", 'A', Block.blockOlivine, 'B', Block.blockSteel});
        generateHoardRecipes(steeldollar, pileSteeldollar, steeldollarBlock);
        //RecipeHelper.Crafting.createShapelessRecipe(steeldollar, 8, new Object[]{new ItemStack(pileSteeldollar, 1)});
        //RecipeHelper.Crafting.createShapelessRecipe(pileSteeldollar, 8, new Object[]{new ItemStack(steeldollarBlock, 1)});
        //RecipeHelper.Crafting.createShapelessRecipe(pileSteeldollar, 1, new Object[]{new ItemStack(steeldollar, 1), new ItemStack(Hoard.steeldollar, 1), new ItemStack(Hoard.steeldollar, 1), new ItemStack(Hoard.steeldollar, 1), new ItemStack(Hoard.steeldollar, 1), new ItemStack(Hoard.steeldollar, 1), new ItemStack(Hoard.steeldollar, 1), new ItemStack(Hoard.steeldollar, 1)});
        //RecipeHelper.Crafting.createShapelessRecipe(steeldollarBlock, 1, new Object[]{new ItemStack(pileSteeldollar, 1), new ItemStack(Hoard.pileSteeldollar, 1), new ItemStack(Hoard.pileSteeldollar, 1), new ItemStack(Hoard.pileSteeldollar, 1), new ItemStack(Hoard.pileSteeldollar, 1), new ItemStack(Hoard.pileSteeldollar, 1), new ItemStack(Hoard.pileSteeldollar, 1), new ItemStack(Hoard.pileSteeldollar, 1)});

    }

    // TODO: These might be useful later when we have more hoard types.
    // It might make more sense to do this one manually. -But what if we had a function for automatically setting up properties?
    //public List generateHoard() {

    //}
    // TODO: Might be good if we could specify conversion rate(s). IE: 2 items = 1 pile
    public void generateHoardRecipes(Item item, Item pile, Block block) {
        RecipeHelper.Crafting.createShapelessRecipe(item, 8, new Object[]{new ItemStack(pile, 1)});
        RecipeHelper.Crafting.createShapelessRecipe(pile, 8, new Object[]{new ItemStack(block, 1)});
        RecipeHelper.Crafting.createShapelessRecipe(pile, 1, new Object[]{new ItemStack(item, 1), new ItemStack(item, 1), new ItemStack(item, 1), new ItemStack(item, 1), new ItemStack(item, 1), new ItemStack(item, 1), new ItemStack(item, 1), new ItemStack(item, 1)});
        RecipeHelper.Crafting.createShapelessRecipe(block, 1, new Object[]{new ItemStack(pile, 1), new ItemStack(pile, 1), new ItemStack(pile, 1), new ItemStack(pile, 1), new ItemStack(pile, 1), new ItemStack(pile, 1), new ItemStack(pile, 1), new ItemStack(pile, 1)});

    }
}
