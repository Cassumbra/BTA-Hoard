package varigata.hoard.mixin;

import net.minecraft.src.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import varigata.hoard.Hoard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;

@Mixin(value = TileEntityTrommel.class, remap = false)
public class TrommelRecipes extends TileEntity {

    //private static final WeightedRandomBag trommelDropsSteeldollarPile;
    private static final HashMap<Integer, List<WeightedRandomBag>> sievableItems = new HashMap<>();

    @Shadow
    private ItemStack[] itemStacks;
    @Final
    @Shadow
    private final Random rand = new Random();


    @Inject(method = "canProduce", at = @At(value = "HEAD"), cancellable = true)
    private void hoard_canProduce(int slotIndex, CallbackInfoReturnable<Boolean> cir) {
        if (itemStacks[slotIndex] != null && sievableItems.containsKey(itemStacks[slotIndex].itemID)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "sieveItem", at = @At(value = "HEAD"), cancellable = true)
    public void hoard_sieveItem(int slotIndex, CallbackInfo ci) {
        ItemStack itemstack = this.itemStacks[slotIndex];
        int item_id = itemstack.itemID;
        if (sievableItems.containsKey(item_id)) {
            ArrayList<ItemStack> itemStackList = new ArrayList<>();
            sievableItems.get(item_id).forEach(bag ->{
                ItemStack itemResult = ((WeightedRandomLootObject)bag.getRandom()).getItemStack();
                System.out.println("itemresult: " + itemResult.stackSize + " " + itemResult.getItemName());
                itemStackList.add(itemResult);

            });
            --this.itemStacks[slotIndex].stackSize;
            if (this.itemStacks[slotIndex].stackSize <= 0) {
                this.itemStacks[slotIndex] = null;
            }

            itemStackList.forEach(itemResult -> {

                // Figure out which way the hole in the block is facing
                int xOffset = 0;
                int zOffset = 0;
                int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) & 7;
                if (meta == 2) {
                    xOffset = -1;
                } else if (meta == 5) {
                    zOffset = -1;
                } else if (meta == 3) {
                    xOffset = 1;
                } else if (meta == 4) {
                    zOffset = 1;
                }
                // Figure out the block/chest that output will get inserted into (if any)
                int adjacentId = this.worldObj.getBlockId(this.xCoord + xOffset, this.yCoord, this.zCoord + zOffset);
                TileEntityChest chest = null;
                if (Block.blocksList[adjacentId] instanceof BlockChest) {
                    chest = (TileEntityChest)this.worldObj.getBlockTileEntity(this.xCoord + xOffset, this.yCoord, this.zCoord + zOffset);
                }
                // If we have a chest, start trying to insert into it.
                if (chest != null) {
                    int i = 0;

                    label78:
                    while(true) {
                        ItemStack slot;
                        // Don't try to insert into empty slots until we've looked at every occupied slot.
                        if (i >= chest.getSizeInventory()) {
                            // If the stacksize of our item is 0 or less, stop.
                            if (itemResult.stackSize <= 0) {
                                ci.cancel();
                                return;
                            }

                            // Loop over every empty slot in the chest and try to insert there.
                            i = 0;
                            while(true) {
                                if (i >= chest.getSizeInventory()) {
                                    break label78;
                                }

                                slot = chest.getStackInSlot(i);
                                // If there is nothing in the slot we are trying to insert into, insert into it.
                                if (slot == null) {
                                    chest.setInventorySlotContents(i, itemResult);
                                    ci.cancel();
                                    return;
                                }

                                ++i;
                            }
                        }
                        slot = chest.getStackInSlot(i);
                        // Check to see that slot has the same contents as the item we are trying to insert
                        if (slot != null && slot.itemID == itemResult.itemID && slot.getMetadata() == itemResult.getMetadata()) {
                            while(slot.stackSize + 1 <= slot.getMaxStackSize()) {
                                ++slot.stackSize;
                                --itemResult.stackSize;
                                chest.setInventorySlotContents(i, slot);
                                if (itemResult.stackSize <= 0) {
                                    ci.cancel();
                                    return;
                                }


                            }
                        }

                        ++i;
                    }
                }

                if (itemResult.stackSize > 0) {
                    float f = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem = new EntityItem(this.worldObj, (double)((float)this.xCoord + f), (double)((float)this.yCoord + f1), (double)((float)this.zCoord + f2), itemResult);
                    float f3 = 0.05F;
                    entityitem.motionX = (double)((float)this.rand.nextGaussian() * f3);
                    entityitem.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                    entityitem.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                    this.worldObj.entityJoinedWorld(entityitem);
                }

            });

            if (this.rand.nextInt(4000) == 0) {
                float f = 0.125F;
                float f1 = 0.125F;
                EntitySlime entityslime = new EntitySlime(this.worldObj);
                entityslime.setSlimeSize(1);
                entityslime.setLocationAndAngles((double)this.xCoord + (double)f, (double)this.yCoord + 1.0, (double)this.zCoord + (double)f1, this.rand.nextFloat() * 360.0F, 0.0F);
                float f3 = 0.05F;
                entityslime.motionX = (double)((float)this.rand.nextGaussian() * f3);
                entityslime.motionY = (double)((float)this.rand.nextGaussian() * f3 + 0.2F);
                entityslime.motionZ = (double)((float)this.rand.nextGaussian() * f3);
                this.worldObj.entityJoinedWorld(entityslime);
            }


            ci.cancel();
            return;
        }

    }


    static {
        WeightedRandomBag<WeightedRandomLootObject> steel = new WeightedRandomBag<>();
        steel.addEntry(new WeightedRandomLootObject(new ItemStack(Item.ingotSteel), 5), 100);
        WeightedRandomBag<WeightedRandomLootObject> olivine = new WeightedRandomBag<>();
        olivine.addEntry(new WeightedRandomLootObject(new ItemStack(Item.olivine), 18), 100);
        sievableItems.put(Hoard.pileSteeldollar.itemID, asList(steel, olivine));

        WeightedRandomBag<WeightedRandomLootObject> gold = new WeightedRandomBag<>();
        gold.addEntry(new WeightedRandomLootObject(new ItemStack(Item.ingotGold), 4), 100);
        WeightedRandomBag<WeightedRandomLootObject> lapis = new WeightedRandomBag<>();
        lapis.addEntry(new WeightedRandomLootObject(new ItemStack(Item.dye, 1, 4), 13), 100);
        sievableItems.put(Hoard.pileRimmedDucat.itemID, asList(gold, lapis));

        WeightedRandomBag<WeightedRandomLootObject> diamond = new WeightedRandomBag<>();
        diamond.addEntry(new WeightedRandomLootObject(new ItemStack(Item.diamond), 2), 100);
        sievableItems.put(Hoard.pileGlittermix.itemID, asList(gold, diamond));

        WeightedRandomBag<WeightedRandomLootObject> redstone = new WeightedRandomBag<>();
        redstone.addEntry(new WeightedRandomLootObject(new ItemStack(Block.blockRedstone), 14), 100);
        WeightedRandomBag<WeightedRandomLootObject> quartz = new WeightedRandomBag<>();
        quartz.addEntry(new WeightedRandomLootObject(new ItemStack(Item.quartz), 36), 100);
        sievableItems.put(Hoard.pileRedQuartz.itemID, asList(redstone, quartz));
    }
}
