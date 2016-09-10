package iguanaman.hungeroverhaul.module;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.pam.harvestcraft.blocks.BlockRegistry;
import com.pam.harvestcraft.item.items.ItemPamSeedFood;
import com.pam.harvestcraft.item.ItemRegistry;
import com.pam.weeeflowers.weeeflowers;

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
            PamCrops = new Block[]{
            BlockRegistry.pamblackberryCrop, BlockRegistry.pamblueberryCrop, BlockRegistry.pamcandleberryCrop, BlockRegistry.pamraspberryCrop, BlockRegistry.pamstrawberryCrop,
            BlockRegistry.pamcactusfruitCrop, BlockRegistry.pamasparagusCrop, BlockRegistry.pambarleyCrop, BlockRegistry.pamoatsCrop, BlockRegistry.pamryeCrop,
            BlockRegistry.pamcornCrop, BlockRegistry.pambambooshootCrop, BlockRegistry.pamcantaloupeCrop, BlockRegistry.pamcucumberCrop, BlockRegistry.pamwintersquashCrop,
            BlockRegistry.pamzucchiniCrop, BlockRegistry.pambeetCrop, BlockRegistry.pamonionCrop, BlockRegistry.pamparsnipCrop, BlockRegistry.pampeanutCrop,
            BlockRegistry.pamradishCrop, BlockRegistry.pamrutabagaCrop, BlockRegistry.pamsweetpotatoCrop, BlockRegistry.pamturnipCrop, BlockRegistry.pamrhubarbCrop,
            BlockRegistry.pamceleryCrop, BlockRegistry.pamgarlicCrop, BlockRegistry.pamgingerCrop, BlockRegistry.pamspiceleafCrop, BlockRegistry.pamtealeafCrop,
            BlockRegistry.pamcoffeebeanCrop, BlockRegistry.pammustardseedsCrop, BlockRegistry.pambroccoliCrop, BlockRegistry.pamcauliflowerCrop, BlockRegistry.pamleekCrop,
            BlockRegistry.pamlettuceCrop, BlockRegistry.pamscallionCrop, BlockRegistry.pamartichokeCrop, BlockRegistry.pambrusselsproutCrop, BlockRegistry.pamcabbageCrop,
            BlockRegistry.pamwhitemushroomCrop, BlockRegistry.pambeanCrop, BlockRegistry.pamsoybeanCrop, BlockRegistry.pambellpepperCrop, BlockRegistry.pamchilipepperCrop,
            BlockRegistry.pameggplantCrop, BlockRegistry.pamokraCrop, BlockRegistry.pampeasCrop, BlockRegistry.pamtomatoCrop, BlockRegistry.pamcottonCrop,
            BlockRegistry.pampineappleCrop, BlockRegistry.pamgrapeCrop, BlockRegistry.pamkiwiCrop, BlockRegistry.pamcranberryCrop, BlockRegistry.pamriceCrop, BlockRegistry.pamseaweedCrop
            };

            crops.put(BlockRegistry.pamasparagusCrop, 0);
            crops.put(BlockRegistry.pambarleyCrop, 1);
            crops.put(BlockRegistry.pambeanCrop, 2);
            crops.put(BlockRegistry.pambeetCrop, 3);
            crops.put(BlockRegistry.pambroccoliCrop, 4);
            crops.put(BlockRegistry.pamcauliflowerCrop, 5);
            crops.put(BlockRegistry.pamceleryCrop, 6);
            crops.put(BlockRegistry.pamcranberryCrop, 7);
            crops.put(BlockRegistry.pamgarlicCrop, 8);
            crops.put(BlockRegistry.pamgingerCrop, 9);
            crops.put(BlockRegistry.pamleekCrop, 10);
            crops.put(BlockRegistry.pamlettuceCrop, 11);
            crops.put(BlockRegistry.pamoatsCrop, 12);
            crops.put(BlockRegistry.pamonionCrop, 13);
            crops.put(BlockRegistry.pamparsnipCrop, 14);
            crops.put(BlockRegistry.pampeanutCrop, 15);
            crops.put(BlockRegistry.pampineappleCrop, 16);
            crops.put(BlockRegistry.pamradishCrop, 17);
            crops.put(BlockRegistry.pamriceCrop, 18);
            crops.put(BlockRegistry.pamrutabagaCrop, 19);
            crops.put(BlockRegistry.pamryeCrop, 20);
            crops.put(BlockRegistry.pamscallionCrop, 21);
            crops.put(BlockRegistry.pamsoybeanCrop, 22);
            crops.put(BlockRegistry.pamspiceleafCrop, 23);
            crops.put(BlockRegistry.pamsweetpotatoCrop, 24);
            crops.put(BlockRegistry.pamtealeafCrop, 25);
            crops.put(BlockRegistry.pamturnipCrop, 26);
            crops.put(BlockRegistry.pamwhitemushroomCrop, 27);
            crops.put(BlockRegistry.pamartichokeCrop, 28);
            crops.put(BlockRegistry.pambellpepperCrop, 29);
            crops.put(BlockRegistry.pamblackberryCrop, 30);
            crops.put(BlockRegistry.pamblueberryCrop, 31);
            crops.put(BlockRegistry.pambrusselsproutCrop, 32);
            crops.put(BlockRegistry.pamcabbageCrop, 33);
            crops.put(BlockRegistry.pamcactusfruitCrop, 34);
            crops.put(BlockRegistry.pamcandleberryCrop, 35);
            crops.put(BlockRegistry.pamcantaloupeCrop, 36);
            crops.put(BlockRegistry.pamchilipepperCrop, 37);
            crops.put(BlockRegistry.pamcoffeebeanCrop, 38);
            crops.put(BlockRegistry.pamcornCrop, 39);
            crops.put(BlockRegistry.pamcottonCrop, 40);
            crops.put(BlockRegistry.pamcucumberCrop, 41);
            crops.put(BlockRegistry.pameggplantCrop, 42);
            crops.put(BlockRegistry.pamgrapeCrop, 43);
            crops.put(BlockRegistry.pamkiwiCrop, 44);
            crops.put(BlockRegistry.pammustardseedsCrop, 45);
            crops.put(BlockRegistry.pamokraCrop, 46);
            crops.put(BlockRegistry.pampeasCrop, 47);
            crops.put(BlockRegistry.pamraspberryCrop, 48);
            crops.put(BlockRegistry.pamrhubarbCrop, 49);
            crops.put(BlockRegistry.pamseaweedCrop, 50);
            crops.put(BlockRegistry.pamstrawberryCrop, 51);
            crops.put(BlockRegistry.pamtomatoCrop, 52);
            crops.put(BlockRegistry.pamwintersquashCrop, 53);
            crops.put(BlockRegistry.pamzucchiniCrop, 54);
            crops.put(BlockRegistry.pambambooshootCrop, 55);

            mapFruit(BlockRegistry.pamappleSapling, Items.apple, BlockRegistry.pamApple);
            mapFruit(BlockRegistry.pamalmondSapling, ItemRegistry.almondItem, BlockRegistry.pamAlmond);
            mapFruit(BlockRegistry.pamapricotSapling, ItemRegistry.apricotItem, BlockRegistry.pamApricot);
            mapFruit(BlockRegistry.pamavocadoSapling, ItemRegistry.avocadoItem, BlockRegistry.pamAvocado);
            mapFruit(BlockRegistry.pambananaSapling, ItemRegistry.bananaItem, BlockRegistry.pamBanana);
            mapFruit(BlockRegistry.pamcashewSapling, ItemRegistry.cashewItem, BlockRegistry.pamCashew);
            mapFruit(BlockRegistry.pamcherrySapling, ItemRegistry.cherryItem, BlockRegistry.pamCherry);
            mapFruit(BlockRegistry.pamchestnutSapling, ItemRegistry.chestnutItem, BlockRegistry.pamChestnut);
            mapFruit(BlockRegistry.pamcinnamonSapling, ItemRegistry.cinnamonItem, BlockRegistry.pamCinnamon);
            mapFruit(BlockRegistry.pamcoconutSapling, ItemRegistry.coconutItem, BlockRegistry.pamCoconut);
            mapFruit(BlockRegistry.pamdateSapling, ItemRegistry.dateItem, BlockRegistry.pamDate);
            mapFruit(BlockRegistry.pamdragonfruitSapling, ItemRegistry.dragonfruitItem, BlockRegistry.pamDragonfruit);
            mapFruit(BlockRegistry.pamdurianSapling, ItemRegistry.durianItem, BlockRegistry.pamDurian);
            mapFruit(BlockRegistry.pamfigSapling, ItemRegistry.figItem, BlockRegistry.pamFig);
            mapFruit(BlockRegistry.pamgrapefruitSapling, ItemRegistry.grapefruitItem, BlockRegistry.pamGrapefruit);
            mapFruit(BlockRegistry.pamlemonSapling, ItemRegistry.lemonItem, BlockRegistry.pamLemon);
            mapFruit(BlockRegistry.pamlimeSapling, ItemRegistry.limeItem, BlockRegistry.pamLime);
            mapFruit(BlockRegistry.pammapleSapling, ItemRegistry.maplesyrupItem, BlockRegistry.pamMaple);
            mapFruit(BlockRegistry.pammangoSapling, ItemRegistry.mangoItem, BlockRegistry.pamMango);
            mapFruit(BlockRegistry.pamnutmegSapling, ItemRegistry.nutmegItem, BlockRegistry.pamNutmeg);
            mapFruit(BlockRegistry.pamoliveSapling, ItemRegistry.oliveItem, BlockRegistry.pamOlive);
            mapFruit(BlockRegistry.pamorangeSapling, ItemRegistry.orangeItem, BlockRegistry.pamOrange);
            mapFruit(BlockRegistry.pampapayaSapling, ItemRegistry.papayaItem, BlockRegistry.pamPapaya);
            mapFruit(BlockRegistry.pampaperbarkSapling, Items.paper, BlockRegistry.pamPaperbark);
            mapFruit(BlockRegistry.pampeachSapling, ItemRegistry.peachItem, BlockRegistry.pamPeach);
            mapFruit(BlockRegistry.pampearSapling, ItemRegistry.pearItem, BlockRegistry.pamPear);
            mapFruit(BlockRegistry.pampecanSapling, ItemRegistry.pecanItem, BlockRegistry.pamPecan);
            mapFruit(BlockRegistry.pampeppercornSapling, ItemRegistry.peppercornItem, BlockRegistry.pamPeppercorn);
            mapFruit(BlockRegistry.pampersimmonSapling, ItemRegistry.persimmonItem, BlockRegistry.pamPersimmon);
            mapFruit(BlockRegistry.pampistachioSapling, ItemRegistry.pistachioItem, BlockRegistry.pamPistachio);
            mapFruit(BlockRegistry.pamplumSapling, ItemRegistry.plumItem, BlockRegistry.pamPlum);
            mapFruit(BlockRegistry.pampomegranateSapling, ItemRegistry.pomegranateItem, BlockRegistry.pamPomegranate);
            mapFruit(BlockRegistry.pamstarfruitSapling, ItemRegistry.starfruitItem, BlockRegistry.pamStarfruit);
            mapFruit(BlockRegistry.pamvanillabeanSapling, ItemRegistry.vanillabeanItem, BlockRegistry.pamVanillabean);
            mapFruit(BlockRegistry.pamwalnutSapling, ItemRegistry.walnutItem, BlockRegistry.pamWalnut);

            for (int i=0; i < ItemRegistry.PamSeeds.length && i < ItemRegistry.PamCropItems.length; i++)
            {
                Item seed = ItemRegistry.PamSeeds[i];
                Item product = ItemRegistry.PamCropItems[i];
                productToSeedMap.put(product, seed);
            }

            try
            {
                itemPamSeedFoodSoilBlock = ItemPamSeedFood.class.getDeclaredField("soilId");
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
        if (Loader.isModLoaded("weeeflowers"))
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
        }
    }

    public static void mapFruit(Block blockSapling, Item fruitItem, Block fruitBlock)
    {
        fruitItemToBlockMap.put(fruitItem, fruitBlock);
        saplingToFruitBlockMap.put(blockSapling, fruitBlock);
    }

    // mimics the logic in ItemPamSeedFood.onItemUse
    public static boolean canPlantSeedFoodAt(EntityPlayer player, ItemStack itemStack, World world, int x, int y, int z, int side)
    {
        if (!player.canPlayerEdit(x, y, z, side, itemStack) || !player.canPlayerEdit(x, y + 1, z, side, itemStack))
            return false;

        try
        {
            Block requiredSoil = (Block) itemPamSeedFoodSoilBlock.get(itemStack.getItem());
            Block soilBlock = world.getBlock(x, y, z);
            Block aboveBlock = world.getBlock(x, y + 1, z);
            if (soilBlock == requiredSoil && aboveBlock.isAir(world, x, y + 1, z))
            {
                return true;
            }
            else if (itemStack.getItem() == ItemRegistry.cranberryItem || itemStack.getItem() == ItemRegistry.riceItem || itemStack.getItem() == ItemRegistry.seaweedItem)
            {
                return aboveBlock.getMaterial() == Material.water && world.getBlockMetadata(x, y + 1, z) == 0;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
