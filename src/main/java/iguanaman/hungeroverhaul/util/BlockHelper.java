package iguanaman.hungeroverhaul.util;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import iguanaman.hungeroverhaul.module.PamsModsHelper;

import java.util.ArrayList;
import java.util.List;

import com.progwml6.natura.blocks.crops.CropBlock;
import com.progwml6.natura.common.NContent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.pam.harvestcraft.BlockPamCrop;

import net.minecraftforge.fml.common.Loader;

public class BlockHelper
{
    public static List<ItemStack> modifyCropDrops(List<ItemStack> drops, Block block, int meta, int minSeeds, int maxSeeds, int minProduce, int maxProduce)
    {
        List<ItemStack> modifiedDrops = new ArrayList<ItemStack>();

        int seeds = RandomHelper.getRandomIntFromRange(minSeeds, maxSeeds);
        int produce = RandomHelper.getRandomIntFromRange(minProduce, maxProduce);
        ItemStack seedItem = BlockHelper.getSeedsOfBlock(block, meta, seeds);
        ItemStack produceItem = BlockHelper.getProduceOfBlock(block, meta, produce);
        boolean produceIsNotSeed = (seedItem.getItem() != produceItem.getItem() || seedItem.getItemDamage() != produceItem.getItemDamage());

        for (ItemStack item : drops)
        {
            // don't include seed/produce already in the list; we'll add them back afterwards
            if (item.isItemEqual(seedItem) || item.isItemEqual(produceItem))
            {
                continue;
            }

            modifiedDrops.add(item);
        }

        // only add seeds if they are different from produce
        if (produceIsNotSeed && seedItem.stackSize > 0)
            modifiedDrops.add(seedItem);

        if (produceItem.stackSize > 0)
            modifiedDrops.add(produceItem);

        return modifiedDrops;
    }

    public static ItemStack getSeedOfBlock(Block block, int meta)
    {
        return getSeedsOfBlock(block, meta, 1);
    }

    public static ItemStack getSeedsOfBlock(Block block, int meta, int num)
    {
        return new ItemStack(getSeedItem(block, meta), num, getSeedMetadata(block, meta));
    }

    public static ItemStack getProduceOfBlock(Block block, int meta)
    {
        return getProduceOfBlock(block, meta, 1);
    }

    public static ItemStack getProduceOfBlock(Block block, int meta, int num)
    {
        return new ItemStack(getProduceItem(block, meta), num, getProduceMetadata(block, meta));
    }

    public static Item getSeedItem(Block block, int meta)
    {
        Item itemDropped = block.getItemDropped(0, RandomHelper.random, 0);
        if (Loader.isModLoaded("harvestcraft") && block instanceof BlockPamCrop)
        {
            Item seedForProduct = PamsModsHelper.productToSeedMap.get(itemDropped);
            if (seedForProduct != null)
                return seedForProduct;
        }
        return itemDropped;
    }

    public static Item getProduceItem(Block block, int meta)
    {
        return block.getItemDropped(meta, RandomHelper.random, 0);
    }

    public static int getProduceMetadata(Block block, int meta)
    {
        return block.damageDropped(meta);
    }

    public static int getSeedMetadata(Block block, int meta)
    {
        if (Loader.isModLoaded("Natura") && block == NContent.crops)
        {
            return ((CropBlock) block).seedDamageDropped(meta);
        }
        else
        {
            return block.damageDropped(0);
        }
    }
}
