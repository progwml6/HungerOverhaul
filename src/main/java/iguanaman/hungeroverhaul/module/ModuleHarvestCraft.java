package iguanaman.hungeroverhaul.module;

import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import com.pam.harvestcraft.HarvestCraft;
import com.pam.harvestcraft.blocks.CropRegistry;
import com.pam.harvestcraft.blocks.FruitRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamFruit;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling;
import com.pam.harvestcraft.item.ItemRegistry;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import iguanaman.hungeroverhaul.food.FoodModifier;
import iguanaman.hungeroverhaul.util.BonemealModification;
import iguanaman.hungeroverhaul.util.PlantGrowthModification;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import squeek.applecore.api.food.FoodValues;

public class ModuleHarvestCraft
{
    public static void init()
    {
        // SETUP VALUES
        if (IguanaConfig.modifyFoodValues && IguanaConfig.useHOFoodValues)
        {
            // crop special cases (unsure why these are singled out, exactly; was like this in 1.6.4)
            List<Item> lowerSaturationCrops = Arrays.asList(new Item[] {
                    CropRegistry.getFood(CropRegistry.RICE),
                    CropRegistry.getFood(CropRegistry.CHILIPEPPER),
                    CropRegistry.getFood(CropRegistry.BELLPEPPER),
                    CropRegistry.getFood(CropRegistry.BLACKBERRY),
                    CropRegistry.getFood(CropRegistry.BLUEBERRY),
                    CropRegistry.getFood(CropRegistry.CACTUSFRUIT),
                    FruitRegistry.getFood(FruitRegistry.CHERRY),
                    CropRegistry.getFood(CropRegistry.CORN),
                    CropRegistry.getFood(CropRegistry.CRANBERRY),
                    CropRegistry.getFood(CropRegistry.CUCUMBER),
                    CropRegistry.getFood(CropRegistry.EGGPLANT),
                    CropRegistry.getFood(CropRegistry.GRAPE),
                    CropRegistry.getFood(CropRegistry.KIWI),
                    CropRegistry.getFood(CropRegistry.LETTUCE),
                    CropRegistry.getFood(CropRegistry.RASPBERRY),
                    CropRegistry.getFood(CropRegistry.SPICELEAF),
                    CropRegistry.getFood(CropRegistry.STRAWBERRY),
                    CropRegistry.getFood(CropRegistry.TEALEAF),
                    CropRegistry.getFood(CropRegistry.TOMATO),
                    CropRegistry.getFood(CropRegistry.ZUCCHINI),
            });

            HarvestCraft.config.cropfoodRestore = 1;

            FoodValues cropFoodValues = new FoodValues(HarvestCraft.config.cropfoodRestore, 0.1F);
            FoodValues lowerSaturationFoodValues = new FoodValues(HarvestCraft.config.cropfoodRestore, 0.05F);

            //Crops
            for (Entry<String, ItemSeedFood> food : CropRegistry.getFoods().entrySet())
            {
                if (food.getValue() == CropRegistry.getFood(CropRegistry.CANTALOUPE))
                {
                    FoodModifier.setModifiedFoodValues(CropRegistry.getFood(CropRegistry.CANTALOUPE), new FoodValues(2, 0.1F));
                }
                else if (lowerSaturationCrops.contains(food.getValue()))
                {
                    FoodModifier.setModifiedFoodValues(food.getValue(), lowerSaturationFoodValues);
                }
                else
                {
                    FoodModifier.setModifiedFoodValues(food.getValue(), cropFoodValues);
                }
            }

            // Fruit
            for (BlockPamSapling sapling : FruitRegistry.getSaplings())
            {
                FoodModifier.setModifiedFoodValues(sapling.getFruitItem(), lowerSaturationFoodValues);
            }

            FoodValues fruitJuiceFoodValues = new FoodValues(2, 0.05F);
            FoodValues jellyFoodValues = new FoodValues(2, 0.1F);
            FoodValues yogurtFoodValues = new FoodValues(2, 0.1F);
            FoodValues jellySandwichFoodValues = new FoodValues(8, 0.45F);
            FoodValues smoothieFoodValues = new FoodValues(3, 0.1F);

            // foods
            FoodModifier.setModifiedFoodValues(ItemRegistry.toastItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cheeseItem, new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.icecreamItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.grilledcheeseItem, new FoodValues(7, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.applejuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.applesauceItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.applepieItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.caramelappleItem, new FoodValues(3, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pumpkinbreadItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.roastedpumpkinseedsItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pumpkinsoupItem, new FoodValues(4, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.melonjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.melonsmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.carrotjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.carrotcakeItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.carrotsoupItem, new FoodValues(4, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.glazedcarrotsItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.butteredpotatoItem, new FoodValues(4, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.loadedbakedpotatoItem, new FoodValues(8, 0.6F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.mashedpotatoesItem, new FoodValues(5, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.potatosaladItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.potatosoupItem, new FoodValues(4, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.friesItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.grilledmushroomItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.stuffedmushroomItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chickensandwichItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chickennoodlesoupItem, new FoodValues(7, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chickenpotpieItem, new FoodValues(8, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.breadedporkchopItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.hotdogItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.bakedhamItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.hamburgerItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cheeseburgerItem, new FoodValues(8, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.baconcheeseburgerItem, new FoodValues(9, 0.6F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.potroastItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.fishsandwichItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.fishsticksItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.fishandchipsItem, new FoodValues(8, 0.45F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.friedeggItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.scrambledeggItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.boiledeggItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.eggsaladItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.caramelItem, new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.taffyItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.spidereyesoupItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.zombiejerkyItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolatebarItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.hotchocolateItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolateicecreamItem, new FoodValues(3, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.vegetablesoupItem, new FoodValues(6, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.stockItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.fruitsaladItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.spagettiItem, new FoodValues(7, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.spagettiandmeatballsItem, new FoodValues(10, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.tomatosoupItem, new FoodValues(3, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chickenparmasanItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pizzaItem, new FoodValues(8, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.springsaladItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.porklettucewrapItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.fishlettucewrapItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.bltItem, new FoodValues(9, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.leafychickensandwichItem, new FoodValues(8, 0.45F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.leafyfishsandwichItem, new FoodValues(8, 0.45F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.deluxecheeseburgerItem, new FoodValues(10, 0.6F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.delightedmealItem, new FoodValues(16, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.onionsoupItem, new FoodValues(4, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.potatocakesItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.hashItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.braisedonionsItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.heartybreakfastItem, new FoodValues(15, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cornonthecobItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cornbreadItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.tortillaItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.nachoesItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.tacoItem, new FoodValues(8, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.fishtacoItem, new FoodValues(8, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.creamedcornItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.strawberrysmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.strawberrypieItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.strawberrysaladItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.strawberryjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolatestrawberryItem, new FoodValues(3, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.peanutbutterItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.trailmixItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pbandjItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.peanutbuttercookiesItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapejuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapejellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapesaladItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.raisincookiesItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.picklesItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cucumbersaladItem, new FoodValues(5, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cucumbersoupItem, new FoodValues(4, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.vegetarianlettucewrapItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.marinatedcucumbersItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.ricesoupItem, new FoodValues(5, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.friedriceItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.mushroomrisottoItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.curryItem, new FoodValues(10, 0.55F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.rainbowcurryItem, new FoodValues(13, 0.7F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.refriedbeansItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.bakedbeansItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.beansandriceItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chiliItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.beanburritoItem, new FoodValues(8, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.stuffedpepperItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.veggiestirfryItem, new FoodValues(8, 0.45F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.grilledskewersItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.supremepizzaItem, new FoodValues(12, 0.7F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.omeletItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.hotwingsItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chilipoppersItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.extremechiliItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chilichocolateItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.lemonaideItem, new FoodValues(1, 0.0F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.lemonbarItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.fishdinnerItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.lemonsmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.lemonmeringueItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.candiedlemonItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.lemonchickenItem, new FoodValues(7, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.blueberrysmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.blueberrypieItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.blueberrymuffinItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.blueberryjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.pancakesItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.blueberrypancakesItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherryjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherrypieItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolatecherryItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherrysmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cheesecakeItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherrycheesecakeItem, new FoodValues(7, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.stuffedeggplantItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.eggplantparmItem, new FoodValues(8, 0.45F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.raspberryicedteaItem, new FoodValues(1, 0.0F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chaiteaItem, new FoodValues(0, 0.0F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.espressoItem, new FoodValues(1, 0.0F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.coffeeconlecheItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.mochaicecreamItem, new FoodValues(3, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pickledbeetsItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.beetsaladItem, new FoodValues(4, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.beetsoupItem, new FoodValues(4, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.bakedbeetsItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.broccolimacItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.broccolindipItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.creamedbroccolisoupItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sweetpotatopieItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.candiedsweetpotatoesItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.mashedsweetpotatoesItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.steamedpeasItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.splitpeasoupItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pineappleupsidedowncakeItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pineapplehamItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pineappleyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.turnipsoupItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.roastedrootveggiemedleyItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.bakedturnipsItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.gingerbreadItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.gingersnapsItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.candiedgingerItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.softpretzelandmustardItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.spicymustardporkItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.spicygreensItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.garlicbreadItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.garlicmashedpotatoesItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.garlicchickenItem, new FoodValues(7, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.summerradishsaladItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.summersquashwithradishItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.celeryandpeanutbutterItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chickencelerycasseroleItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.peasandceleryItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.celerysoupItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.zucchinibreadItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.zucchinifriesItem, new FoodValues(8, 0.45F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.zestyzucchiniItem, new FoodValues(9, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.zucchinibakeItem, new FoodValues(9, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.asparagusquicheItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.asparagussoupItem, new FoodValues(4, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.walnutraisinbreadItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.candiedwalnutsItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.brownieItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.papayajuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.papayasmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.papayayogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.starfruitjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.starfruitsmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.starfruityogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.guacamoleItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.creamofavocadosoupItem, new FoodValues(6, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.avocadoburritoItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.poachedpearItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.fruitcrumbleItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pearyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.plumyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.bananasplitItem, new FoodValues(7, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.banananutbreadItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.bananasmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.bananayogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.coconutmilkItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chickencurryItem, new FoodValues(9, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.coconutshrimpItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.coconutyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.orangejuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.orangechickenItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.orangesmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.orangeyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.peachjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.peachcobblerItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.peachsmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.peachyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.limejuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.keylimepieItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.limesmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.limeyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.mangojuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.mangosmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.mangoyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.pomegranatejuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.pomegranatesmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.pomegranateyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.vanillayogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cinnamonrollItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.frenchtoastItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.marshmellowsItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.donutItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolatedonutItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.powdereddonutItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.jellydonutItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.frosteddonutItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cactussoupItem, new FoodValues(3, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.wafflesItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.seedsoupItem, new FoodValues(3, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.softpretzelItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.jellybeansItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.biscuitItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.creamcookieItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.jaffaItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.friedchickenItem, new FoodValues(7, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolatesprinklecakeItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.redvelvetcakeItem, new FoodValues(7, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.footlongItem, new FoodValues(9, 0.55F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.blueberryyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.lemonyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherryyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.strawberryyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapeyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolateyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.blackberryjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.blackberrycobblerItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.blackberrysmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.blackberryyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolatemilkItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pumpkinyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.raspberryjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.raspberrypieItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.raspberrysmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.raspberryyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cinnamonsugardonutItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.melonyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.kiwijuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.kiwismoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.kiwiyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.plainyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.appleyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.saltedsunflowerseedsItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sunflowerwheatrollsItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sunflowerbroccolisaladItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cranberryjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cranberrysauceItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cranberrybarItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.peppermintItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cactusfruitjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.baklavaItem, new FoodValues(7, 0.45F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.gummybearsItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.baconmushroomburgerItem, new FoodValues(10, 0.65F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.fruitpunchItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.meatystewItem, new FoodValues(6, 0.45F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.mixedsaladItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pinacoladaItem, new FoodValues(2, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.shepardspieItem, new FoodValues(6, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.eggnogItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.custardItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sushiItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.gardensoupItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(Items.MUTTON, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(Items.COOKED_MUTTON, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.calamarirawItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.calamaricookedItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.applejellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.applejellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.blackberryjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.blackberryjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.blueberryjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.blueberryjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherryjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherryjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cranberryjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cranberryjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.kiwijellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.kiwijellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.lemonjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.lemonjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.limejellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.limejellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.mangojellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.mangojellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.orangejellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.orangejellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.papayajellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.papayajellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.peachjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.peachjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.pomegranatejellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.pomegranatejellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.raspberryjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.raspberryjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.starfruitjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.starfruitjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.strawberryjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.strawberryjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.watermelonjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.watermelonjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherrysodaItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.colasodaItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.gingersodaItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapesodaItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.lemonlimesodaItem, new FoodValues(3, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.orangesodaItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.rootbeersodaItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.strawberrysodaItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.caramelicecreamItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.mintchocolatechipicemcreamItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.strawberryicecreamItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.vanillaicecreamItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.ediblerootItem, new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.gingerchickenItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.oldworldveggiesoupItem, new FoodValues(4, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.spicebunItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.gingeredrhubarbtartItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.lambbarleysoupItem, new FoodValues(5, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.honeylemonlambItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pumpkinoatsconesItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.beefjerkyItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.plumjuiceItem, new FoodValues(2, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pearjuiceItem, new FoodValues(2, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.ovenroastedcauliflowerItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.leekbaconsoupItem, new FoodValues(6, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.herbbutterparsnipsItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.scallionbakedpotatoItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.soymilkItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.firmtofuItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.silkentofuItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.bakedsweetpotatoItem, new FoodValues(2, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.grilledasparagusItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.toastedcoconutItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.coffeeItem, new FoodValues(0, 0.0F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.popcornItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.grilledeggplantItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.raisinsItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.ricecakeItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.teaItem, new FoodValues(2, 0.1F));

            // new foods to 1.7.10
            FoodModifier.setModifiedFoodValues(ItemRegistry.bamboosteamedriceItem, new FoodValues(4, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.roastedchestnutItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sweetpotatosouffleItem, new FoodValues(5, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cashewchickenItem, new FoodValues(6, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.apricotjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.apricotyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.apricotglazedporkItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.apricotjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.apricotjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.apricotsmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.figbarItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.figjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.figjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.figsmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.figyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.figjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapefruitjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapefruitjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapefruitjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapefruitsmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapefruityogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapefruitsodaItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.citrussaladItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pecanpieItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pralinesItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.persimmonjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.persimmonyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.persimmonsmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.persimmonjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.persimmonjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.pistachiobakedsalmonItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.baconwrappeddatesItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.datenutbreadItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.maplesyruppancakesItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.maplesyrupwafflesItem, new FoodValues(7, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.maplesausageItem, new FoodValues(1, 0.45F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.mapleoatmealItem, new FoodValues(5, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.peachesandcreamoatmealItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cinnamonappleoatmealItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.maplecandiedbaconItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.toastsandwichItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.potatoandcheesepirogiItem, new FoodValues(5, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.zeppoleItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sausageinbreadItem, new FoodValues(16, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolatecaramelfudgeItem, new FoodValues(5, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.lavendershortbreadItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.beefwellingtonItem, new FoodValues(16, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.epicbaconItem, new FoodValues(16, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.manjuuItem, new FoodValues(4, 0.1F));
        }

        // SAPLING GROWTH
        PlantGrowthModification genericSaplingGrowthModification = new PlantGrowthModification()
                .setGrowthTickProbability(IguanaConfig.saplingRegrowthMultiplier);
        ModulePlantGrowth.registerPlantGrowthModifier(BlockPamSapling.class, genericSaplingGrowthModification);

        PlantGrowthModification temperateSaplingGrowthModification = new PlantGrowthModification()
                .setGrowthTickProbability(IguanaConfig.saplingRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.FOREST, 1)
                .setBiomeGrowthModifier(Type.PLAINS, 1);
        for (Block temperateSapling : FruitRegistry.temperateSaplings.values())
        {
            ModulePlantGrowth.registerPlantGrowthModifier(temperateSapling, temperateSaplingGrowthModification);
        }
        ModulePlantGrowth.registerPlantGrowthModifier(FruitRegistry.getSapling(FruitRegistry.MAPLE), temperateSaplingGrowthModification);

        PlantGrowthModification warmSaplingGrowthModification = new PlantGrowthModification()
                .setGrowthTickProbability(IguanaConfig.saplingRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.JUNGLE, 1)
                .setBiomeGrowthModifier(Type.SWAMP, 1);
        for (Block warmSapling : FruitRegistry.warmSaplings.values())//BlockRegistry.PamWarmSaplings)
        {
            ModulePlantGrowth.registerPlantGrowthModifier(warmSapling, warmSaplingGrowthModification);
        }
        ModulePlantGrowth.registerPlantGrowthModifier(FruitRegistry.getSapling(FruitRegistry.CINNAMON), warmSaplingGrowthModification);

        // FRUIT GROWTH
        PlantGrowthModification genericFruitGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(false)
                .setGrowthTickProbability(IguanaConfig.treeCropRegrowthMultiplier);
        ModulePlantGrowth.registerPlantGrowthModifier(BlockPamFruit.class, genericFruitGrowthModification);

        PlantGrowthModification temperateFruitGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(false)
                .setGrowthTickProbability(IguanaConfig.treeCropRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.FOREST, 1)
                .setBiomeGrowthModifier(Type.PLAINS, 1);
        for (Block temperateSapling : FruitRegistry.temperateSaplings.values()) //BlockRegistry.PamTemperateSaplings)
        {
            Block fruitBlock = PamsModsHelper.saplingToFruitBlockMap.get(temperateSapling);
            ModulePlantGrowth.registerPlantGrowthModifier(fruitBlock, temperateFruitGrowthModification);
        }
        ModulePlantGrowth.registerPlantGrowthModifier(FruitRegistry.getLog(FruitRegistry.MAPLE), temperateSaplingGrowthModification);

        PlantGrowthModification warmFruitGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(false)
                .setGrowthTickProbability(IguanaConfig.treeCropRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.JUNGLE, 1)
                .setBiomeGrowthModifier(Type.SWAMP, 1);
        for (Block warmSapling : FruitRegistry.warmSaplings.values()) //BlockRegistry.PamWarmSaplings)
        {
            Block fruitBlock = PamsModsHelper.saplingToFruitBlockMap.get(warmSapling);
            ModulePlantGrowth.registerPlantGrowthModifier(fruitBlock, warmFruitGrowthModification);
        }
        ModulePlantGrowth.registerPlantGrowthModifier(FruitRegistry.getLog(FruitRegistry.CINNAMON), warmFruitGrowthModification);

        // CROP GROWTH
        // only need to register the exceptions here,
        // the BlockCrops modification will cover the rest
        PlantGrowthModification humidCropGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(true)
                .setGrowthTickProbability(IguanaConfig.cropRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.JUNGLE, 1)
                .setBiomeGrowthModifier(Type.SWAMP, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(CropRegistry.getCrop(CropRegistry.PINEAPPLE), humidCropGrowthModification);
        ModulePlantGrowth.registerPlantGrowthModifier(CropRegistry.getCrop(CropRegistry.SPICELEAF), humidCropGrowthModification);
        ModulePlantGrowth.registerPlantGrowthModifier(CropRegistry.getCrop(CropRegistry.CANDLEBERRY), humidCropGrowthModification);
        ModulePlantGrowth.registerPlantGrowthModifier(CropRegistry.getCrop(CropRegistry.GRAPE), humidCropGrowthModification);
        ModulePlantGrowth.registerPlantGrowthModifier(CropRegistry.getCrop(CropRegistry.KIWI), humidCropGrowthModification);

        PlantGrowthModification desertCropGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(true)
                .setGrowthTickProbability(IguanaConfig.cropRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.SANDY, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(CropRegistry.getCrop(CropRegistry.CACTUSFRUIT), desertCropGrowthModification);

        /*
         * Bonemeal
         */
        BonemealModification fruitBonemealModification = new BonemealModification()
        {
            @SuppressWarnings("deprecation")
            @Override
            public IBlockState getNewState(World world, BlockPos pos, IBlockState state)
            {
                Block block = state.getBlock();
                int currentMeta = block.getMetaFromState(state);

                return block.getStateFromMeta(Math.min(currentMeta + 1, 2));
            }
        };
        ModuleBonemeal.registerBonemealModifier(BlockPamFruit.class, fruitBonemealModification);
    }
}
