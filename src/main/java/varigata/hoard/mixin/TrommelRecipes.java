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
        //System.out.println("a");
        //System.out.println("itemstacks slotindex: " + itemStacks[slotIndex]);
        if (itemStacks[slotIndex] != null && sievableItems.containsKey(itemStacks[slotIndex].itemID)) {
            //System.out.println("b");
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "sieveItem", at = @At(value = "HEAD"), cancellable = true)
    public void hoard_sieveItem(int slotIndex, CallbackInfo ci) {
        //System.out.println("sievableitems" + sievableItems);
        ItemStack itemstack = this.itemStacks[slotIndex];
        int item_id = itemstack.itemID;
        if (sievableItems.containsKey(item_id)) {
            //System.out.println("I'M GONNA FUCKING FUCK!!!!");
            ArrayList<ItemStack> itemStackList = new ArrayList<>();
            //System.out.println("NEO PROBLEM DETECTOR 1 ONLINE B]");
            sievableItems.get(item_id).forEach(bag ->{
                //System.out.println("NEO PROBLEM DETECTOR 2 ONLINE B]");
                //PROBLEM LOCATED
                //System.out.println("bag" + bag);
                //ItemStack itemResult = ((WeightedRandomLootObject)bag.getRandom()).getItemStack();
                //itemStackList.add(itemResult);

            });
            //itemStackList.add(new ItemStack(Block.blockSteel, 1));
            //System.out.println("NEO PROBLEM DETECTOR 3 ONLINE B]");
            --this.itemStacks[slotIndex].stackSize;
            if (this.itemStacks[slotIndex].stackSize <= 0) {
                //System.out.println("NEO PROBLEM DETECTOR 4 ONLINE B]");
                this.itemStacks[slotIndex] = null;
            }
            //System.out.println("Problem detector number 1 is on duty. >:^)");
            //itemStackList.forEach(itemResult -> {
                ItemStack itemResult = new ItemStack(Block.blockSteel, 1);
                System.out.println("itemresult stacksize: " + itemResult.stackSize);
                //--itemResult.stackSize;
                //System.out.println("itemresult stacksize after subtraction: " + itemResult.stackSize);
                //++itemResult.stackSize;
                //System.out.println("itemresult stacksize after addition: " + itemResult.stackSize);
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
                //System.out.println("Problem detector number 2 is on duty. >:^)");
                int adjacentId = this.worldObj.getBlockId(this.xCoord + xOffset, this.yCoord, this.zCoord + zOffset);
                TileEntityChest chest = null;
                if (Block.blocksList[adjacentId] instanceof BlockChest) {
                    chest = (TileEntityChest)this.worldObj.getBlockTileEntity(this.xCoord + xOffset, this.yCoord, this.zCoord + zOffset);
                }
                //System.out.println("Problem detector number 3 is on duty. >:^)");
                if (chest != null) {
                    int i = 0;

                    label78:
                    while(true) {
                        ItemStack slot;
                        if (i >= chest.getSizeInventory()) {
                            if (itemResult.stackSize <= 0) {
                                ci.cancel();
                            }

                            i = 0;

                            while(true) {
                                if (i >= chest.getSizeInventory()) {
                                    break label78;
                                }

                                slot = chest.getStackInSlot(i);
                                if (slot == null) {
                                    chest.setInventorySlotContents(i, itemResult);
                                    ci.cancel();
                                }

                                ++i;
                            }
                        }
                        //System.out.println("Problem detector number 4 is on duty. >:^)");
                        slot = chest.getStackInSlot(i);
                        if (slot != null && slot.itemID == itemResult.itemID && slot.getMetadata() == itemResult.getMetadata()) {
                            while(slot.stackSize + 1 <= slot.getMaxStackSize()) {
                                ++slot.stackSize;
                                chest.setInventorySlotContents(i, slot);
                                if (itemResult.stackSize <= 0) {
                                    ci.cancel();
                                }

                                --itemResult.stackSize;
                                System.out.println("itemresult stacksize after subtraction: " + itemResult.stackSize);
                            }
                        }

                        ++i;
                    }
                }
                //System.out.println("Problem detector number 5 is on duty. >:^)");
                if (itemResult.stackSize > 0) {
                    System.out.println("itemresult stacksize after if statement: " + itemResult.stackSize);
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

            //});

            //System.out.println("Problem detector number 6 is on duty. >:^)");
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

            //System.out.println("Problem detector number 7 is on duty. >:^)");

            ci.cancel();
        }

    }


    static {
        //ArrayList pileSteeldollarSieve = new ArrayList<>();
        //pileSteeldollarSieve.add()
        WeightedRandomBag<WeightedRandomLootObject> steelBlock = new WeightedRandomBag<>();
        steelBlock.addEntry(new WeightedRandomLootObject(new ItemStack(Block.blockSteel), 1, 1), 100);
        steelBlock.addEntry(new WeightedRandomLootObject(new ItemStack(Block.blockSteel), 1, 1), 100);
        WeightedRandomBag<WeightedRandomLootObject> olivineBlock = new WeightedRandomBag<>();
        olivineBlock.addEntry(new WeightedRandomLootObject(new ItemStack(Block.blockOlivine), 1, 1), 100);
        olivineBlock.addEntry(new WeightedRandomLootObject(new ItemStack(Block.blockOlivine), 1, 1), 100);
        sievableItems.put(Hoard.pileSteeldollar.itemID, asList(steelBlock, olivineBlock));
    }

    //@Inject(method = "getItemResult", at = @At(value = "HEAD"))
    //private void hoard_trommelGetItemResult(ItemStack slotItem, CallbackInfoReturnable<ItemStack> cir) {

    //}
}
