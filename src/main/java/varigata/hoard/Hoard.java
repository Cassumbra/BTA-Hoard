package varigata.hoard;

import net.fabricmc.api.ModInitializer;
import net.minecraft.src.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.*;


public class Hoard implements ModInitializer {
    public static final String MOD_ID = "hoard";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Set up textures because Halplibe is a little silly.
    public static final int[] sd_tex_x = BlockCoords.nextCoords();
    static {
        TextureHelper.addTextureToTerrain(MOD_ID, "steeldollar_tex_x.png", sd_tex_x[0], sd_tex_x[1]);
    }
    public static final int[] sd_tex_y = BlockCoords.nextCoords();
    static {
        TextureHelper.addTextureToTerrain(MOD_ID, "steeldollar_tex_y.png", sd_tex_y[0], sd_tex_y[1]);
    }
    public static final int[] sd_tex_z = BlockCoords.nextCoords();
    static {
        TextureHelper.addTextureToTerrain(MOD_ID, "steeldollar_tex_z.png", sd_tex_z[0], sd_tex_z[1]);
    }


    // Items
    public static final Item pileSteeldollar = ItemHelper.createItem(MOD_ID, new ItemPileSteeldollar(140), "pile.steeldollar", "steeldollar_pile.png");
    public static final Item steeldollar = ItemHelper.createItem(MOD_ID, new Item(141), "steeldollar", "steeldollar.png");

    public static final Block steeldollarBlock = BlockHelper.createBlock(MOD_ID, new BlockSteeldollar(900, Material.sand), "block.steeldollar",
            sd_tex_y[0], sd_tex_y[1], sd_tex_y[0], sd_tex_y[1],sd_tex_x[0], sd_tex_x[1], sd_tex_z[0], sd_tex_z[1], sd_tex_x[0], sd_tex_x[1], sd_tex_z[0], sd_tex_z[1],
            Block.soundMetalFootstep, 3.0F, 5.0F, 0);

    // Blocks
    public static final Block layerSteeldollar = BlockHelper.createBlock(MOD_ID, new BlockLayerSteeldollar(901, Material.sand), "layer.steeldollar",
            sd_tex_y[0], sd_tex_y[1], sd_tex_y[0], sd_tex_y[1],sd_tex_x[0], sd_tex_x[1], sd_tex_z[0], sd_tex_z[1], sd_tex_x[0], sd_tex_x[1], sd_tex_z[0], sd_tex_z[1],
            Block.soundMetalFootstep, 3.0F, 5.0F, 0).setNotInCreativeMenu();

    static {
        ((BlockLayerBase) layerSteeldollar).setFullBlockID(steeldollarBlock.blockID);
    }



    @Override
    public void onInitialize() {
        LOGGER.info("Hoard initialized.");

        RecipeHelper.Crafting.createRecipe(pileSteeldollar, 1, new Object[]{"###", "ABA", "###", 'A', Block.blockOlivine, 'B', Block.blockSteel});
        RecipeHelper.Crafting.createShapelessRecipe(steeldollar, 8, new Object[]{new ItemStack(pileSteeldollar, 1)});
        RecipeHelper.Crafting.createShapelessRecipe(pileSteeldollar, 8, new Object[]{new ItemStack(steeldollarBlock, 1)});
        RecipeHelper.Crafting.createShapelessRecipe(pileSteeldollar, 1, new Object[]{new ItemStack(steeldollar, 1), new ItemStack(Hoard.steeldollar, 1), new ItemStack(Hoard.steeldollar, 1), new ItemStack(Hoard.steeldollar, 1), new ItemStack(Hoard.steeldollar, 1), new ItemStack(Hoard.steeldollar, 1), new ItemStack(Hoard.steeldollar, 1), new ItemStack(Hoard.steeldollar, 1)});
        RecipeHelper.Crafting.createShapelessRecipe(steeldollarBlock, 1, new Object[]{new ItemStack(pileSteeldollar, 1), new ItemStack(Hoard.pileSteeldollar, 1), new ItemStack(Hoard.pileSteeldollar, 1), new ItemStack(Hoard.pileSteeldollar, 1), new ItemStack(Hoard.pileSteeldollar, 1), new ItemStack(Hoard.pileSteeldollar, 1), new ItemStack(Hoard.pileSteeldollar, 1), new ItemStack(Hoard.pileSteeldollar, 1)});

    }
}
