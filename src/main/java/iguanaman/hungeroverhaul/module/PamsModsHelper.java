package iguanaman.hungeroverhaul.module;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.common.collect.Maps;
import com.pam.harvestcraft.blocks.CropRegistry;
import com.pam.harvestcraft.blocks.FruitRegistry;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

public class PamsModsHelper
{
    public static Block[] PamCrops;

    public static Block[] PamFlowerCrops;

    public static Item[] PamFlowerSeeds;

    private static Field itemPamSeedFoodSoilBlock = null;

    public static HashMap<Block, Integer> crops = Maps.newHashMap();

    public static HashMap<Item, Block> fruitItemToBlockMap = new HashMap<Item, Block>();

    public static HashMap<Block, Block> saplingToFruitBlockMap = new HashMap<Block, Block>();

    public static HashMap<Item, Item> productToSeedMap = new HashMap<Item, Item>();

    public static void loadHC()
    {
        if (Loader.isModLoaded("harvestcraft"))
        {
            PamCrops = new Block[] {
                    CropRegistry.getCrop(CropRegistry.BLACKBERRY), CropRegistry.getCrop(CropRegistry.BLUEBERRY), CropRegistry.getCrop(CropRegistry.CANDLEBERRY), CropRegistry.getCrop(CropRegistry.RASPBERRY), CropRegistry.getCrop(CropRegistry.STRAWBERRY),
                    CropRegistry.getCrop(CropRegistry.CACTUSFRUIT), CropRegistry.getCrop(CropRegistry.ASPARAGUS), CropRegistry.getCrop(CropRegistry.BARLEY), CropRegistry.getCrop(CropRegistry.OATS), CropRegistry.getCrop(CropRegistry.RYE),
                    CropRegistry.getCrop(CropRegistry.CORN), CropRegistry.getCrop(CropRegistry.BAMBOOSHOOT), CropRegistry.getCrop(CropRegistry.CANTALOUPE), CropRegistry.getCrop(CropRegistry.CUCUMBER), CropRegistry.getCrop(CropRegistry.WINTERSQUASH),
                    CropRegistry.getCrop(CropRegistry.ZUCCHINI), CropRegistry.getCrop(CropRegistry.BEET), CropRegistry.getCrop(CropRegistry.ONION), CropRegistry.getCrop(CropRegistry.PARSNIP), CropRegistry.getCrop(CropRegistry.PEANUT),
                    CropRegistry.getCrop(CropRegistry.RADISH), CropRegistry.getCrop(CropRegistry.RUTABAGA), CropRegistry.getCrop(CropRegistry.SWEETPOTATO), CropRegistry.getCrop(CropRegistry.TURNIP), CropRegistry.getCrop(CropRegistry.RHUBARB),
                    CropRegistry.getCrop(CropRegistry.CELERY), CropRegistry.getCrop(CropRegistry.GARLIC), CropRegistry.getCrop(CropRegistry.GINGER), CropRegistry.getCrop(CropRegistry.SPICELEAF), CropRegistry.getCrop(CropRegistry.TEALEAF),
                    CropRegistry.getCrop(CropRegistry.COFFEE), CropRegistry.getCrop(CropRegistry.MUSTARD), CropRegistry.getCrop(CropRegistry.BROCCOLI), CropRegistry.getCrop(CropRegistry.CAULIFLOWER), CropRegistry.getCrop(CropRegistry.LEEK),
                    CropRegistry.getCrop(CropRegistry.LETTUCE), CropRegistry.getCrop(CropRegistry.SCALLION), CropRegistry.getCrop(CropRegistry.ARTICHOKE), CropRegistry.getCrop(CropRegistry.BRUSSELSPROUT), CropRegistry.getCrop(CropRegistry.CABBAGE),
                    CropRegistry.getCrop(CropRegistry.WHITEMUSHROOM), CropRegistry.getCrop(CropRegistry.BEAN), CropRegistry.getCrop(CropRegistry.SOYBEAN), CropRegistry.getCrop(CropRegistry.BELLPEPPER), CropRegistry.getCrop(CropRegistry.CHILIPEPPER),
                    CropRegistry.getCrop(CropRegistry.EGGPLANT), CropRegistry.getCrop(CropRegistry.OKRA), CropRegistry.getCrop(CropRegistry.PEAS), CropRegistry.getCrop(CropRegistry.TOMATO), CropRegistry.getCrop(CropRegistry.COTTON),
                    CropRegistry.getCrop(CropRegistry.PINEAPPLE), CropRegistry.getCrop(CropRegistry.GRAPE), CropRegistry.getCrop(CropRegistry.KIWI), CropRegistry.getCrop(CropRegistry.CRANBERRY), CropRegistry.getCrop(CropRegistry.RICE), CropRegistry.getCrop(CropRegistry.SEAWEED)
            };

            crops.put(CropRegistry.getCrop(CropRegistry.ASPARAGUS), 0);
            crops.put(CropRegistry.getCrop(CropRegistry.BARLEY), 1);
            crops.put(CropRegistry.getCrop(CropRegistry.BEAN), 2);
            crops.put(CropRegistry.getCrop(CropRegistry.BEET), 3);
            crops.put(CropRegistry.getCrop(CropRegistry.BROCCOLI), 4);
            crops.put(CropRegistry.getCrop(CropRegistry.CAULIFLOWER), 5);
            crops.put(CropRegistry.getCrop(CropRegistry.CELERY), 6);
            crops.put(CropRegistry.getCrop(CropRegistry.CRANBERRY), 7);
            crops.put(CropRegistry.getCrop(CropRegistry.GARLIC), 8);
            crops.put(CropRegistry.getCrop(CropRegistry.GINGER), 9);
            crops.put(CropRegistry.getCrop(CropRegistry.LEEK), 10);
            crops.put(CropRegistry.getCrop(CropRegistry.LETTUCE), 11);
            crops.put(CropRegistry.getCrop(CropRegistry.OATS), 12);
            crops.put(CropRegistry.getCrop(CropRegistry.ONION), 13);
            crops.put(CropRegistry.getCrop(CropRegistry.PARSNIP), 14);
            crops.put(CropRegistry.getCrop(CropRegistry.PEANUT), 15);
            crops.put(CropRegistry.getCrop(CropRegistry.PINEAPPLE), 16);
            crops.put(CropRegistry.getCrop(CropRegistry.RADISH), 17);
            crops.put(CropRegistry.getCrop(CropRegistry.RICE), 18);
            crops.put(CropRegistry.getCrop(CropRegistry.RUTABAGA), 19);
            crops.put(CropRegistry.getCrop(CropRegistry.RYE), 20);
            crops.put(CropRegistry.getCrop(CropRegistry.SCALLION), 21);
            crops.put(CropRegistry.getCrop(CropRegistry.SOYBEAN), 22);
            crops.put(CropRegistry.getCrop(CropRegistry.SPICELEAF), 23);
            crops.put(CropRegistry.getCrop(CropRegistry.SWEETPOTATO), 24);
            crops.put(CropRegistry.getCrop(CropRegistry.TEALEAF), 25);
            crops.put(CropRegistry.getCrop(CropRegistry.TURNIP), 26);
            crops.put(CropRegistry.getCrop(CropRegistry.WHITEMUSHROOM), 27);
            crops.put(CropRegistry.getCrop(CropRegistry.ARTICHOKE), 28);
            crops.put(CropRegistry.getCrop(CropRegistry.BELLPEPPER), 29);
            crops.put(CropRegistry.getCrop(CropRegistry.BLACKBERRY), 30);
            crops.put(CropRegistry.getCrop(CropRegistry.BLUEBERRY), 31);
            crops.put(CropRegistry.getCrop(CropRegistry.BRUSSELSPROUT), 32);
            crops.put(CropRegistry.getCrop(CropRegistry.CABBAGE), 33);
            crops.put(CropRegistry.getCrop(CropRegistry.CACTUSFRUIT), 34);
            crops.put(CropRegistry.getCrop(CropRegistry.CANDLEBERRY), 35);
            crops.put(CropRegistry.getCrop(CropRegistry.CANTALOUPE), 36);
            crops.put(CropRegistry.getCrop(CropRegistry.CHILIPEPPER), 37);
            crops.put(CropRegistry.getCrop(CropRegistry.COFFEE), 38);
            crops.put(CropRegistry.getCrop(CropRegistry.CORN), 39);
            crops.put(CropRegistry.getCrop(CropRegistry.COTTON), 40);
            crops.put(CropRegistry.getCrop(CropRegistry.CUCUMBER), 41);
            crops.put(CropRegistry.getCrop(CropRegistry.EGGPLANT), 42);
            crops.put(CropRegistry.getCrop(CropRegistry.GRAPE), 43);
            crops.put(CropRegistry.getCrop(CropRegistry.KIWI), 44);
            crops.put(CropRegistry.getCrop(CropRegistry.MUSTARD), 45);
            crops.put(CropRegistry.getCrop(CropRegistry.OKRA), 46);
            crops.put(CropRegistry.getCrop(CropRegistry.PEAS), 47);
            crops.put(CropRegistry.getCrop(CropRegistry.RASPBERRY), 48);
            crops.put(CropRegistry.getCrop(CropRegistry.RHUBARB), 49);
            crops.put(CropRegistry.getCrop(CropRegistry.SEAWEED), 50);
            crops.put(CropRegistry.getCrop(CropRegistry.STRAWBERRY), 51);
            crops.put(CropRegistry.getCrop(CropRegistry.TOMATO), 52);
            crops.put(CropRegistry.getCrop(CropRegistry.WINTERSQUASH), 53);
            crops.put(CropRegistry.getCrop(CropRegistry.ZUCCHINI), 54);
            crops.put(CropRegistry.getCrop(CropRegistry.BAMBOOSHOOT), 55);

            mapFruit(FruitRegistry.getSapling(FruitRegistry.APPLE), Items.APPLE, FruitRegistry.getSapling(FruitRegistry.APPLE).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.ALMOND), FruitRegistry.getFood(FruitRegistry.ALMOND), FruitRegistry.getSapling(FruitRegistry.ALMOND).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.APRICOT), FruitRegistry.getFood(FruitRegistry.APRICOT), FruitRegistry.getSapling(FruitRegistry.APRICOT).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.AVOCADO), FruitRegistry.getFood(FruitRegistry.AVOCADO), FruitRegistry.getSapling(FruitRegistry.AVOCADO).getFruit());

            mapFruit(FruitRegistry.getSapling(FruitRegistry.BANANA), FruitRegistry.getFood(FruitRegistry.BANANA), FruitRegistry.getSapling(FruitRegistry.BANANA).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.CASHEW), FruitRegistry.getFood(FruitRegistry.CASHEW), FruitRegistry.getSapling(FruitRegistry.CASHEW).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.CHERRY), FruitRegistry.getFood(FruitRegistry.CHERRY), FruitRegistry.getSapling(FruitRegistry.CHERRY).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.CHESTNUT), FruitRegistry.getFood(FruitRegistry.CHESTNUT), FruitRegistry.getSapling(FruitRegistry.CHESTNUT).getFruit());

            mapFruit(FruitRegistry.getSapling(FruitRegistry.CINNAMON), FruitRegistry.getFood(FruitRegistry.CINNAMON), FruitRegistry.getSapling(FruitRegistry.CINNAMON).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.COCONUT), FruitRegistry.getFood(FruitRegistry.COCONUT), FruitRegistry.getSapling(FruitRegistry.COCONUT).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.DATE), FruitRegistry.getFood(FruitRegistry.DATE), FruitRegistry.getSapling(FruitRegistry.DATE).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.DRAGONFRUIT), FruitRegistry.getFood(FruitRegistry.DRAGONFRUIT), FruitRegistry.getSapling(FruitRegistry.DRAGONFRUIT).getFruit());

            mapFruit(FruitRegistry.getSapling(FruitRegistry.DURIAN), FruitRegistry.getFood(FruitRegistry.DURIAN), FruitRegistry.getSapling(FruitRegistry.DURIAN).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.FIG), FruitRegistry.getFood(FruitRegistry.FIG), FruitRegistry.getSapling(FruitRegistry.FIG).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.GRAPEFRUIT), FruitRegistry.getFood(FruitRegistry.GRAPEFRUIT), FruitRegistry.getSapling(FruitRegistry.GRAPEFRUIT).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.LEMON), FruitRegistry.getFood(FruitRegistry.LEMON), FruitRegistry.getSapling(FruitRegistry.LEMON).getFruit());

            mapFruit(FruitRegistry.getSapling(FruitRegistry.LIME), FruitRegistry.getFood(FruitRegistry.LIME), FruitRegistry.getSapling(FruitRegistry.LIME).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.MAPLE), FruitRegistry.getFood(FruitRegistry.MAPLE), FruitRegistry.getSapling(FruitRegistry.MAPLE).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.MANGO), FruitRegistry.getFood(FruitRegistry.MANGO), FruitRegistry.getSapling(FruitRegistry.MANGO).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.NUTMEG), FruitRegistry.getFood(FruitRegistry.NUTMEG), FruitRegistry.getSapling(FruitRegistry.NUTMEG).getFruit());

            mapFruit(FruitRegistry.getSapling(FruitRegistry.OLIVE), FruitRegistry.getFood(FruitRegistry.OLIVE), FruitRegistry.getSapling(FruitRegistry.OLIVE).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.ORANGE), FruitRegistry.getFood(FruitRegistry.ORANGE), FruitRegistry.getSapling(FruitRegistry.ORANGE).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.PAPAYA), FruitRegistry.getFood(FruitRegistry.PAPAYA), FruitRegistry.getSapling(FruitRegistry.PAPAYA).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.PAPERBARK), FruitRegistry.getFood(FruitRegistry.PAPERBARK), FruitRegistry.getSapling(FruitRegistry.PAPERBARK).getFruit());

            mapFruit(FruitRegistry.getSapling(FruitRegistry.PEACH), FruitRegistry.getFood(FruitRegistry.PEACH), FruitRegistry.getSapling(FruitRegistry.PEACH).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.PEAR), FruitRegistry.getFood(FruitRegistry.PEAR), FruitRegistry.getSapling(FruitRegistry.PEAR).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.PECAN), FruitRegistry.getFood(FruitRegistry.PECAN), FruitRegistry.getSapling(FruitRegistry.PECAN).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.PEPPERCORN), FruitRegistry.getFood(FruitRegistry.PEPPERCORN), FruitRegistry.getSapling(FruitRegistry.PEPPERCORN).getFruit());

            mapFruit(FruitRegistry.getSapling(FruitRegistry.PERSIMMON), FruitRegistry.getFood(FruitRegistry.PERSIMMON), FruitRegistry.getSapling(FruitRegistry.PERSIMMON).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.PISTACHIO), FruitRegistry.getFood(FruitRegistry.PISTACHIO), FruitRegistry.getSapling(FruitRegistry.PISTACHIO).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.PLUM), FruitRegistry.getFood(FruitRegistry.PLUM), FruitRegistry.getSapling(FruitRegistry.PLUM).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.POMEGRANATE), FruitRegistry.getFood(FruitRegistry.POMEGRANATE), FruitRegistry.getSapling(FruitRegistry.POMEGRANATE).getFruit());

            mapFruit(FruitRegistry.getSapling(FruitRegistry.STARFRUIT), FruitRegistry.getFood(FruitRegistry.STARFRUIT), FruitRegistry.getSapling(FruitRegistry.STARFRUIT).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.VANILLABEAN), FruitRegistry.getFood(FruitRegistry.VANILLABEAN), FruitRegistry.getSapling(FruitRegistry.VANILLABEAN).getFruit());
            mapFruit(FruitRegistry.getSapling(FruitRegistry.WALNUT), FruitRegistry.getFood(FruitRegistry.WALNUT), FruitRegistry.getSapling(FruitRegistry.WALNUT).getFruit());

            for (Entry<String, ItemSeedFood> food : CropRegistry.getFoods().entrySet())
            {
                productToSeedMap.put(food.getValue(), CropRegistry.getSeed(food.getKey()));
            }

            try
            {
                itemPamSeedFoodSoilBlock = ItemSeedFood.class.getDeclaredField("soilId");
                itemPamSeedFoodSoilBlock.setAccessible(true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void loadWF()
    {
        /*if (Loader.isModLoaded("weeeflowers"))
        {
            PamFlowerCrops = new Block[]{
            weeeflowers.pamwhiteflowerCrop, weeeflowers.pamorangeflowerCrop, weeeflowers.pammagentaflowerCrop, weeeflowers.pamlightblueflowerCrop, weeeflowers.pamyellowflowerCrop,
            weeeflowers.pamlimeflowerCrop, weeeflowers.pampinkflowerCrop, weeeflowers.pamlightgreyflowerCrop, weeeflowers.pamdarkgreyflowerCrop, weeeflowers.pamcyanflowerCrop,
            weeeflowers.pampurpleflowerCrop, weeeflowers.pamblueflowerCrop, weeeflowers.pambrownflowerCrop, weeeflowers.pamgreenflowerCrop, weeeflowers.pamredflowerCrop, weeeflowers.pamblackflowerCrop
            };
        
            PamFlowerSeeds = new Item[]{
            weeeflowers.whiteflowerseedItem, weeeflowers.orangeflowerseedItem, weeeflowers.magentaflowerseedItem, weeeflowers.lightblueflowerseedItem, weeeflowers.yellowflowerseedItem,
            weeeflowers.limeflowerseedItem, weeeflowers.pinkflowerseedItem, weeeflowers.lightgreyflowerseedItem, weeeflowers.darkgreyflowerseedItem, weeeflowers.cyanflowerseedItem,
            weeeflowers.purpleflowerseedItem, weeeflowers.blueflowerseedItem, weeeflowers.brownflowerseedItem, weeeflowers.greenflowerseedItem, weeeflowers.redflowerseedItem, weeeflowers.blackflowerseedItem
            };
        }*/
    }

    public static void mapFruit(Block blockSapling, Item fruitItem, Block fruitBlock)
    {
        fruitItemToBlockMap.put(fruitItem, fruitBlock);
        saplingToFruitBlockMap.put(blockSapling, fruitBlock);
    }

    // mimics the logic in ItemPamSeedFood.onItemUse
    @SuppressWarnings("deprecation")
    public static boolean canPlantSeedFoodAt(EntityPlayer player, ItemStack itemStack, World world, BlockPos pos, EnumFacing side)
    {
        if (!player.canPlayerEdit(pos, side, itemStack) || !player.canPlayerEdit(pos.up(), side, itemStack))
            return false;

        try
        {
            Block requiredSoil = (Block) itemPamSeedFoodSoilBlock.get(itemStack.getItem());

            IBlockState soilStateBlock = world.getBlockState(pos);
            IBlockState aboveStateBlock = world.getBlockState(pos.up());

            Block soilBlock = soilStateBlock.getBlock();
            Block aboveBlock = aboveStateBlock.getBlock();
            if (soilBlock == requiredSoil && aboveBlock.isAir(aboveStateBlock, world, pos.up()))
            {
                return true;
            }
            else if (itemStack.getItem() == CropRegistry.getFood(CropRegistry.CRANBERRY) || itemStack.getItem() == CropRegistry.getFood(CropRegistry.RICE) || itemStack.getItem() == CropRegistry.getFood(CropRegistry.SEAWEED))
            {
                return aboveBlock.getMaterial(aboveStateBlock) == Material.WATER && world.getBlockState(pos.up()) == null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
