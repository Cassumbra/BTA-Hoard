package varigata.hoard;

import net.fabricmc.api.ModInitializer;
import net.minecraft.src.Block;
import net.minecraft.src.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockHelper;


public class Hoard implements ModInitializer {
    public static final String MOD_ID = "hoard";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Block steeldollarBlock = BlockHelper.createBlock(MOD_ID, new Block(900, Material.sand), "steeldollar", "steeldollar_block.png", Block.soundGravelFootstep, 0.1f, 0.1f, 0.0f);

    @Override
    public void onInitialize() {
        LOGGER.info("Hoard initialized.");
    }
}
