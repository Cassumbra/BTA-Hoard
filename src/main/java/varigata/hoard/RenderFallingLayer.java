package varigata.hoard;

import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

public class RenderFallingLayer extends Render {

    private RenderBlocks field_197_d = new RenderBlocks();

    public RenderFallingLayer() {
        this.shadowSize = 0.5F;
    }
    public void doRenderFallingLayer(EntityFallingLayer entityfallinglayer, double d, double d1, double d2, float f, float f1) {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        this.loadTexture("/terrain.png");
        Block block = Block.blocksList[entityfallinglayer.blockID];
        World world = entityfallinglayer.getWorld();
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F * (entityfallinglayer.metadata + 1), 1.0F);
        GL11.glDisable(2896);
        this.field_197_d.renderBlockFallingSand(block, world, MathHelper.floor_double(entityfallinglayer.posX), MathHelper.floor_double(entityfallinglayer.posY), MathHelper.floor_double(entityfallinglayer.posZ));
        GL11.glEnable(2896);
        GL11.glPopMatrix();
    }

    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
        this.doRenderFallingLayer((EntityFallingLayer)entity, d, d1, d2, f, f1);
    }
}
