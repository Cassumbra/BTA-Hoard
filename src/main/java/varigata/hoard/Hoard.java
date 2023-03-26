package varigata.hoard;

import net.fabricmc.api.ModInitializer;
import net.minecraft.src.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.*;


public class Hoard implements ModInitializer {

    public static final String MOD_ID = "hoard";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);



    public static final StepSound soundCoinsFootstep = new StepSound("gravel", 1.0F, 4.0F);


    // Items
    public static final Item pileSteeldollar = ItemHelper.createItem(MOD_ID, new ItemHoardPileBase(140), "pile.steeldollar", "steeldollar_pile.png");
    public static final Item steeldollar = ItemHelper.createItem(MOD_ID, new Item(141), "steeldollar", "steeldollar.png");

    public static final Item pileRimmedDucat = ItemHelper.createItem(MOD_ID, new ItemHoardPileBase(142), "pile.rimmedDucat", "rimmedDucat_pile.png");
    public static final Item rimmedDucat = ItemHelper.createItem(MOD_ID, new Item(143), "rimmedDucat", "rimmedDucat.png");

    public static final Item pileGlittermix = ItemHelper.createItem(MOD_ID, new ItemHoardPileBase(144), "pile.glittermix", "glittermix_pile.png");
    public static final Item glittermix = ItemHelper.createItem(MOD_ID, new Item(145), "glittermix", "glittermix.png");

    // Blocks
    public static final Block blockSteeldollar = BlockHelper.createBlock(MOD_ID, new BlockHoardBase(900, Material.sand), "block.steeldollar",
            "steeldollar_tex_y.png", "steeldollar_tex_y.png", "steeldollar_tex_x.png", "steeldollar_tex_z.png", "steeldollar_tex_x.png", "steeldollar_tex_z.png",
            Block.soundMetalFootstep, 3.0F, 5.0F, 0);
    public static final Block layerSteeldollar = BlockHelper.createBlock(MOD_ID, new BlockHoardLayerBase(901, Material.sand), "layer.steeldollar",
            "steeldollar_tex_y.png", "steeldollar_tex_y.png", "steeldollar_tex_x.png", "steeldollar_tex_z.png", "steeldollar_tex_x.png", "steeldollar_tex_z.png",
            Block.soundMetalFootstep, 3.0F, 5.0F, 0).setNotInCreativeMenu();

    public static final Block blockRimmedDucat = BlockHelper.createBlock(MOD_ID, new BlockHoardBase(902, Material.sand), "block.rimmedDucat",
            "rimmedDucat_2_topbottom.png", "rimmedDucat_2.png",
            soundCoinsFootstep, 3.0F, 5.0F, 0);
    public static final Block layerRimmedDucat = BlockHelper.createBlock(MOD_ID, new BlockHoardLayerBase(903, Material.sand), "layer.rimmedDucat",
            "rimmedDucat_2_topbottom.png","rimmedDucat_2.png",
            soundCoinsFootstep, 3.0F, 5.0F, 0).setNotInCreativeMenu();

    public static final Block blockGlittermix = BlockHelper.createBlock(MOD_ID, new BlockHoardBase(904, Material.sand), "block.glittermix",
            "glittermix_2.png",
            soundCoinsFootstep, 3.0F, 5.0F, 0);
    public static final Block layerGlittermix = BlockHelper.createBlock(MOD_ID, new BlockHoardLayerBase(905, Material.sand), "layer.glittermix",
            "glittermix_2.png",
            soundCoinsFootstep, 3.0F, 5.0F, 0).setNotInCreativeMenu();



    @Override
    public void onInitialize() {
        LOGGER.info("Hoard initialized.");

        EntityHelper.createEntity(EntityFallingLayer.class, new RenderFallingSand(), 60, "Falling Layer");

        setHoardProperties(pileSteeldollar, layerSteeldollar, blockSteeldollar);
        setHoardProperties(pileRimmedDucat, layerRimmedDucat, blockRimmedDucat);
        setHoardProperties(pileGlittermix, layerGlittermix, blockGlittermix);

        RecipeHelper.Crafting.createRecipe(pileSteeldollar, 1, new Object[]{"###", "ABA", "###", 'A', Block.blockOlivine, 'B', Block.blockSteel});
        RecipeHelper.Crafting.createRecipe(pileRimmedDucat, 1, new Object[]{"ABA", "BCB", "ABA", 'A', new ItemStack(Item.dye, 1, 4), 'B', Item.ingotGold, 'C', Block.blockLapis});
        RecipeHelper.Crafting.createRecipe(pileGlittermix, 1, new Object[]{"###", "ABA", "BBB", 'A', Item.diamond, 'B', Item.ingotGold});

        generateHoardRecipes(steeldollar, pileSteeldollar, blockSteeldollar);
        generateHoardRecipes(rimmedDucat, pileRimmedDucat, blockRimmedDucat);
        generateHoardRecipes(glittermix, pileGlittermix, blockGlittermix);
    }

    // TODO: These might be useful later when we have more hoard types.
    // It might make more sense to do this one manually. -But what if we had a function for automatically setting up properties?
    //public List generateHoard() {

    //}
    // Function for automatically setting up properties
    public static void setHoardProperties(Item pile, Block layer, Block block) {
        ((ItemHoardPileBase) pile).setLayerID(layer.blockID);
        ((ItemHoardPileBase) pile).setLayerID(layer.blockID);
        ((BlockHoardBase) block).setMaterialID(pile.itemID);
        ((BlockHoardBase) block).setLayerID(layer.blockID);
        ((BlockHoardLayerBase) layer).setMaterialID(pile.itemID);
        ((BlockLayerBase) layer).setFullBlockID(block.blockID);
    }

    // TODO: Might be good if we could specify conversion rate(s). IE: 2 items = 1 pile
    public void generateHoardRecipes(Item item, Item pile, Block block) {
        RecipeHelper.Crafting.createShapelessRecipe(item, 8, new Object[]{new ItemStack(pile, 1)});
        RecipeHelper.Crafting.createShapelessRecipe(pile, 8, new Object[]{new ItemStack(block, 1)});
        RecipeHelper.Crafting.createShapelessRecipe(pile, 1, new Object[]{new ItemStack(item, 1), new ItemStack(item, 1), new ItemStack(item, 1), new ItemStack(item, 1), new ItemStack(item, 1), new ItemStack(item, 1), new ItemStack(item, 1), new ItemStack(item, 1)});
        RecipeHelper.Crafting.createShapelessRecipe(block, 1, new Object[]{new ItemStack(pile, 1), new ItemStack(pile, 1), new ItemStack(pile, 1), new ItemStack(pile, 1), new ItemStack(pile, 1), new ItemStack(pile, 1), new ItemStack(pile, 1), new ItemStack(pile, 1)});
    }
}
