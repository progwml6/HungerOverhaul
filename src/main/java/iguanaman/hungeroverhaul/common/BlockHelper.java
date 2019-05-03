package iguanaman.hungeroverhaul.common;

import com.pam.harvestcraft.blocks.growables.BlockPamCrop;
import com.pam.harvestcraft.blocks.growables.BlockPamFruit;
import com.progwml6.natura.overworld.NaturaOverworld;
import iguanaman.hungeroverhaul.module.harvestcraft.helper.PamsModsHelper;
import iguanaman.hungeroverhaul.module.natura.helper.NaturaHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.List;

public class BlockHelper
{
    public static List<ItemStack> modifyCropDrops(List<ItemStack> drops, IBlockState state, int minSeeds, int maxSeeds, int minProduce, int maxProduce)
    {
        List<ItemStack> modifiedDrops = new ArrayList<ItemStack>();

        int seeds = RandomHelper.getRandomIntFromRange(minSeeds, maxSeeds);
        int produce = RandomHelper.getRandomIntFromRange(minProduce, maxProduce);
        ItemStack seedItem = BlockHelper.getSeedsOfBlock(state, seeds);
        ItemStack produceItem = BlockHelper.getProduceOfBlock(state, produce);
        boolean produceIsNotSeed = (seedItem.getItem() != produceItem.getItem() || seedItem.getItemDamage() != produceItem.getItemDamage());

        boolean shouldProduceNotDrop = BlockHelper.shouldProduceNotDrop(state);

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
        if (produceIsNotSeed && seedItem.getCount() > 0)
        {
            modifiedDrops.add(seedItem);
        }

        if (produceItem.getCount() > 0 && !shouldProduceNotDrop)
        {
            modifiedDrops.add(produceItem);
        }

        return modifiedDrops;
    }

    public static ItemStack getSeedOfBlock(IBlockState state)
    {
        return getSeedsOfBlock(state, 1);
    }

    public static ItemStack getSeedsOfBlock(IBlockState state, int num)
    {
        return new ItemStack(getSeedItem(state), num, getSeedMetadata(state));
    }

    public static ItemStack getProduceOfBlock(IBlockState state)
    {
        return getProduceOfBlock(state, 1);
    }

    public static ItemStack getProduceOfBlock(IBlockState state, int num)
    {
        return new ItemStack(getProduceItem(state), num, getProduceMetadata(state));
    }

    public static Item getSeedItem(IBlockState state)
    {
        Item itemDropped = state.getBlock().getItemDropped(state, RandomHelper.random, 0);

        if (Loader.isModLoaded("natura") && (state.getBlock() == NaturaOverworld.barleyCrop || state.getBlock() == NaturaOverworld.cottonCrop))
        {
            Item seedForProduct = NaturaHelper.cropToSeedMap.get(state.getBlock());

            if (seedForProduct != Items.AIR)
            {
                return seedForProduct;
            }
        }
        else if (Loader.isModLoaded("harvestcraft") && state.getBlock() instanceof BlockPamCrop)
        {
            Item seedForProduct = PamsModsHelper.productToSeedMap.get(itemDropped);

            if (seedForProduct != Items.AIR)
            {
                return seedForProduct;
            }
        }

        return itemDropped;
    }

    public static Item getProduceItem(IBlockState state)
    {
        return state.getBlock().getItemDropped(state, RandomHelper.random, 0);
    }

    public static int getProduceMetadata(IBlockState state)
    {
        return state.getBlock().damageDropped(state);
    }

    public static int getSeedMetadata(IBlockState state)
    {
        if (Loader.isModLoaded("natura") && state.getBlock() == NaturaOverworld.barleyCrop)
        {
            return NaturaOverworld.barley_seeds.copy().getItemDamage();
        }
        else if (Loader.isModLoaded("natura") && state.getBlock() == NaturaOverworld.cottonCrop)
        {
            return NaturaOverworld.cotton_seeds.copy().getItemDamage();
        }
        else
        {
            return state.getBlock().damageDropped(state);
        }
    }

    public static boolean shouldProduceNotDrop(IBlockState state)
    {
        if (Loader.isModLoaded("harvestcraft") && state.getBlock() instanceof BlockPamFruit)
        {
            Block seed = PamsModsHelper.fruitBlockToBlockMap.get(state.getBlock());

            return seed == state.getBlock() || seed == Blocks.AIR;
        }

        return false;
    }
}
