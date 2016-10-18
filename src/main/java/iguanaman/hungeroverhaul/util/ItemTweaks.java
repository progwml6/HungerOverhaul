package iguanaman.hungeroverhaul.util;

import com.pam.harvestcraft.blocks.growables.BlockPamSapling;
import com.pam.harvestcraft.item.items.ItemPamSeedFood;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;
import iguanaman.hungeroverhaul.config.IguanaConfig;
import squeek.applecore.api.food.FoodValues;

public class ItemTweaks
{
    public static enum VillagerType
    {
        FARMER,
        LIBRARIAN,
        PRIEST,
        SMITH,
        BUTCHER
    }

    public static void init()
    {
        boolean worthLooping = IguanaConfig.modifyFoodStackSize
                || IguanaConfig.addTradesButcher
                || IguanaConfig.addHarvestCraftChestLoot
                || IguanaConfig.addTradesFarmer
                || IguanaConfig.addSaplingTradesFarmer;
        if (worthLooping)
        {
            for (Object obj : Item.REGISTRY)
            {
                Item item = (Item) obj;
                if (item instanceof ItemFood)
                {
                    ItemStack stack = new ItemStack(item);
                    FoodValues values = FoodValues.get(stack);

                    if (IguanaConfig.modifyFoodStackSize)
                        modifyStackSize(item, stack, values);
                    if (values.hunger > 9)
                    {
                        if (IguanaConfig.addTradesButcher)
                            addButcherTrade(item, stack, values);
                        if (IguanaConfig.addHarvestCraftChestLoot)
                            addChestLoot(item, stack, values);
                    }
                    if (Loader.isModLoaded("harvestcraft") && IguanaConfig.addTradesFarmer && item instanceof ItemPamSeedFood)
                    {
                        addCropTrade(item);
                    }
                }
                else if (Loader.isModLoaded("harvestcraft") && IguanaConfig.addSaplingTradesFarmer && item instanceof ItemBlock)
                {
                    Block block = Block.getBlockFromItem(item);
                    if (block instanceof BlockPamSapling)
                        addSaplingTrade(block);
                }
            }
        }
    }

    public static void modifyStackSize(Item item, ItemStack stack, FoodValues values)
    {
        int curStackSize = item.getItemStackLimit(stack);
        int newStackSize = curStackSize;

        if (values.hunger <= 2)
            newStackSize = 16 * IguanaConfig.foodStackSizeMultiplier;
        else if (values.hunger <= 5)
            newStackSize = 8 * IguanaConfig.foodStackSizeMultiplier;
        else if (values.hunger <= 8)
            newStackSize = 4 * IguanaConfig.foodStackSizeMultiplier;
        else if (values.hunger <= 11)
            newStackSize = 2 * IguanaConfig.foodStackSizeMultiplier;
        else
            newStackSize = IguanaConfig.foodStackSizeMultiplier;

        if (curStackSize > newStackSize)
            item.setMaxStackSize(newStackSize);
    }

    public static void addButcherTrade(Item item, ItemStack stack, FoodValues values)
    {
        VillagerRegistry.instance().registerVillageTradeHandler(VillagerType.BUTCHER, new TradeHandlerFood(item, item.getItemStackLimit(stack)));
    }

    public static void addSaplingTrade(Block block)
    {
        VillagerRegistry.instance().registerVillageTradeHandler(VillagerType.FARMER.ordinal(), new TradeHandlerSapling(block));
    }

    public static void addCropTrade(Item item)
    {
        VillagerRegistry.instance().registerVillageTradeHandler(VillagerType.FARMER.ordinal(), new TradeHandlerCrop(item));
    }

    public static void addChestLoot(Item item, ItemStack stack, FoodValues values)
    {
        int maxStackSize = Math.min(IguanaConfig.chestLootMaxStackSize, item.getItemStackLimit(stack));
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(new WeightedRandomChestContent(stack, 1, maxStackSize, IguanaConfig.chestLootChance));
        ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(new WeightedRandomChestContent(stack, 1, maxStackSize, IguanaConfig.chestLootChance));
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(new WeightedRandomChestContent(stack, 1, maxStackSize, IguanaConfig.chestLootChance));
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(new WeightedRandomChestContent(stack, 1, maxStackSize, IguanaConfig.chestLootChance));
    }
}
