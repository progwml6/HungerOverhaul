package iguanaman.hungeroverhaul.module.harvestcraft.helper;

import com.google.common.collect.Maps;
import com.pam.harvestcraft.blocks.CropRegistry;
import com.pam.harvestcraft.blocks.FruitRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamCrop;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.Loader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

public class PamsModsHelper
{
    public static Block[] PamCrops;

    public static Block[] PamFlowerCrops;

    public static Item[] PamFlowerSeeds;

    public static HashMap<Block, Integer> crops = Maps.newHashMap();

    public static HashMap<Item, Block> fruitItemToBlockMap = new HashMap<Item, Block>();

    public static HashMap<Block, Block> saplingToFruitBlockMap = new HashMap<Block, Block>();

    public static HashMap<Block, Block> fruitBlockToBlockMap = new HashMap<Block, Block>();

    public static HashMap<Item, Item> productToSeedMap = new HashMap<Item, Item>();

    public static void loadHC()
    {
        if (Loader.isModLoaded("harvestcraft"))
        {
            Collection<BlockPamCrop> cropRegistry = CropRegistry.getCrops().values();
            PamCrops = CropRegistry.getCrops().values().toArray(new BlockPamCrop[cropRegistry.size()]);
            int i = 0;
            for(Block crop : PamCrops)
            {
                crops.put(crop, i++);
            }

            // somewhat of a hack to use FruitRegistry.foodItems since HarvestCraft should really be exposing an API.
            for(String fruitName : FruitRegistry.foodItems.keySet())
            {
                mapFruit(
                        FruitRegistry.getSapling(fruitName),
                        FruitRegistry.getFood(fruitName),
                        FruitRegistry.getSapling(fruitName).getFruit()
                );
            }
            // mapFruit(FruitRegistry.getSapling(FruitRegistry.APPLE), Items.APPLE, FruitRegistry.getSapling(FruitRegistry.APPLE).getFruit());
            for (Entry<String, ItemSeedFood> food : CropRegistry.getFoods().entrySet())
            {
                productToSeedMap.put(food.getValue(), CropRegistry.getSeed(food.getKey()));
            }
        }
    }

    public static void mapFruit(Block blockSapling, Item fruitItem, Block fruitBlock)
    {
        fruitItemToBlockMap.put(fruitItem, fruitBlock);
        saplingToFruitBlockMap.put(blockSapling, fruitBlock);
        fruitBlockToBlockMap.put(fruitBlock, fruitBlock);
    }

    // mimics the logic in ItemSeedFood.onItemUse which ItemPamSeedFood uses.
    public static boolean canPlantSeedFoodAt(ItemStack itemstack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing facing)
    {
        IBlockState state = worldIn.getBlockState(pos);

        return facing == EnumFacing.UP && playerIn.canPlayerEdit(pos.offset(facing), facing, itemstack) && state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, (IPlantable) itemstack.getItem()) && worldIn.isAirBlock(pos.up());
    }
}
