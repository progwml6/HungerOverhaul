package iguanaman.hungeroverhaul.util;

import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.pam.harvestcraft.blocks.growables.BlockPamSapling;
import com.pam.harvestcraft.item.items.ItemPamSeedFood;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.entity.passive.EntityVillager.PriceInfo;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import squeek.applecore.api.food.FoodValues;

public class ItemTweaks
{
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
        VillagerProfession p = VillagerRegistry.instance().getRegistry().getValue(new ResourceLocation("minecraft:butcher"));
        VillagerCareer pCareer = p.getCareer(4);
        int stackSize = item.getItemStackLimit(stack);

        pCareer.addTrade(1, new GiveItemForEmeralds(new PriceInfo(1, 1), new ItemStack(item, stackSize, 0), new PriceInfo(stackSize, stackSize)));
    }

    public static void addSaplingTrade(Block block)
    {
        VillagerProfession p = VillagerRegistry.instance().getRegistry().getValue(new ResourceLocation("minecraft:farmer"));
        VillagerCareer pCareer = p.getCareer(0);

        pCareer.addTrade(1, new GiveItemForEmeralds(null, new ItemStack(block, 1, 0), null));
    }

    public static void addCropTrade(Item item)
    {
        VillagerProfession p = VillagerRegistry.instance().getRegistry().getValue(new ResourceLocation("minecraft:farmer"));
        VillagerCareer pCareer = p.getCareer(0);

        pCareer.addTrade(1, new GiveEmeraldsForItem(null, new ItemStack(item, 16, 0), new PriceInfo(16, 16)));
    }

    public static void addChestLoot(Item item, ItemStack stack, FoodValues values)
    {
        //int maxStackSize = Math.min(IguanaConfig.chestLootMaxStackSize, item.getItemStackLimit(stack));

        //LootEventDispatcher.addItem(CHESTS_SIMPLE_DUNGEON, new WeightedRandomChestContent(stack, 1, maxStackSize, IguanaConfig.chestLootChance));
        //LootEventDispatcher.addItem(CHESTS_ABANDONED_MINESHAFT, new WeightedRandomChestContent(stack, 1, maxStackSize, IguanaConfig.chestLootChance));
        //LootEventDispatcher.addItem(CHESTS_DESERT_PYRAMID, new WeightedRandomChestContent(stack, 1, maxStackSize, IguanaConfig.chestLootChance));
        //LootEventDispatcher.addItem(CHESTS_JUNGLE_TEMPLE, new WeightedRandomChestContent(stack, 1, maxStackSize, IguanaConfig.chestLootChance));
    }

    public static class GiveItemForEmeralds implements ITradeList
    {
        @Nullable
        public PriceInfo emeraldPriceInfo;

        @Nonnull
        public ItemStack itemToSell;

        @Nullable
        public PriceInfo sellInfo;

        public GiveItemForEmeralds(@Nullable PriceInfo emeraldPriceInfo, @Nonnull ItemStack itemToSell, @Nullable PriceInfo sellInfo)
        {
            this.emeraldPriceInfo = emeraldPriceInfo;
            this.itemToSell = itemToSell;
            this.sellInfo = sellInfo;
        }

        @Override
        public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
        {
            int i = 1;
            if (this.sellInfo != null)
            {
                i = this.sellInfo.getPrice(random);
            }

            int j = 1;
            if (this.emeraldPriceInfo != null)
            {
                j = this.emeraldPriceInfo.getPrice(random);
            }

            ItemStack sellStack = this.itemToSell.copy();
            sellStack.stackSize = i;

            ItemStack emeralds = new ItemStack(Items.EMERALD, j);

            recipeList.add(new MerchantRecipe(emeralds, sellStack));
        }
    }

    public static class GiveEmeraldsForItem implements ITradeList
    {
        @Nullable
        public PriceInfo emeraldPriceInfo;

        @Nonnull
        public ItemStack itemToSell;

        @Nullable
        public PriceInfo sellInfo;

        public GiveEmeraldsForItem(@Nullable PriceInfo emeraldPriceInfo, @Nonnull ItemStack itemToSell, @Nullable PriceInfo sellInfo)
        {
            this.emeraldPriceInfo = emeraldPriceInfo;
            this.itemToSell = itemToSell;
            this.sellInfo = sellInfo;
        }

        @Override
        public void modifyMerchantRecipeList(MerchantRecipeList recipeList, Random random)
        {
            int i = 1;
            if (this.sellInfo != null)
            {
                i = this.sellInfo.getPrice(random);
            }

            int j = 1;
            if (this.emeraldPriceInfo != null)
            {
                j = this.emeraldPriceInfo.getPrice(random);
            }

            ItemStack sellStack = this.itemToSell.copy();
            sellStack.stackSize = i;

            ItemStack emeralds = new ItemStack(Items.EMERALD, j);

            recipeList.add(new MerchantRecipe(sellStack, emeralds));
        }
    }
}
