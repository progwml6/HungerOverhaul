package iguanaman.hungeroverhaul.module.harvestcraft;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.pam.harvestcraft.HarvestCraft;
import com.pam.harvestcraft.blocks.CropRegistry;
import com.pam.harvestcraft.blocks.FruitRegistry;
import com.pam.harvestcraft.blocks.growables.BlockPamCrop;
import com.pam.harvestcraft.blocks.growables.BlockPamFruit;
import com.pam.harvestcraft.blocks.growables.BlockPamFruitLog;
import com.pam.harvestcraft.blocks.growables.BlockPamSapling;
import com.pam.harvestcraft.item.ItemRegistry;

import iguanaman.hungeroverhaul.common.config.Config;
import iguanaman.hungeroverhaul.module.bonemeal.BonemealModule;
import iguanaman.hungeroverhaul.module.bonemeal.modification.BonemealModification;
import iguanaman.hungeroverhaul.module.food.FoodModifier;
import iguanaman.hungeroverhaul.module.growth.PlantGrowthModule;
import iguanaman.hungeroverhaul.module.growth.modification.PlantGrowthModification;
import iguanaman.hungeroverhaul.module.harvestcraft.helper.PamsModsHelper;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import squeek.applecore.api.food.FoodValues;

public class HarvestCraftModule
{
    public static Random random = new Random();

    public static void init()
    {
        random.setSeed(2 ^ 16 + 2 ^ 8 + (4 * 3 * 271));

        // SETUP VALUES
        if (Config.modifyFoodValues && Config.useHOFoodValues)
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

            // crops
            for (Item crop : CropRegistry.getFoods().values())
            {
                if (crop == CropRegistry.getFood(CropRegistry.CANTALOUPE))
                {
                    FoodModifier.setModifiedFoodValues(CropRegistry.getFood(CropRegistry.CANTALOUPE), new FoodValues(2, 0.1F));
                }
                else if (lowerSaturationCrops.contains(crop))
                {
                    FoodModifier.setModifiedFoodValues(crop, lowerSaturationFoodValues);
                }
                else
                {
                    FoodModifier.setModifiedFoodValues(crop, cropFoodValues);
                }
            }

            // fruits
            for (BlockPamSapling sapling : FruitRegistry.getSaplings())
            {
                FoodModifier.setModifiedFoodValues(sapling.getFruitItem(), lowerSaturationFoodValues);
            }

            FoodValues fruitJuiceFoodValues = new FoodValues(2, 0.05F);
            FoodValues jellyFoodValues = new FoodValues(2, 0.1F);
            FoodValues yogurtFoodValues = new FoodValues(2, 0.1F);
            FoodValues jellySandwichFoodValues = new FoodValues(8, 0.45F);
            FoodValues smoothieFoodValues = new FoodValues(3, 0.1F);
            FoodValues fishFoodValues = new FoodValues(1, 0.25F);

            // foods
            FoodModifier.setModifiedFoodValues(ItemRegistry.ediblerootItem, new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sunflowerseedsItem, new FoodValues(1, 0.1F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.calamarirawItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.calamaricookedItem, new FoodValues(2, 0.1F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.grilledasparagusItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.bakedsweetpotatoItem, new FoodValues(2, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.grilledeggplantItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.toastItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cheeseItem, new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.icecreamItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.grilledcheeseItem, new FoodValues(7, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.applesauceItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pumpkinbreadItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.caramelappleItem, new FoodValues(3, 0.1F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.applepieItem, new FoodValues(5, 0.25F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.teaItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.coffeeItem, new FoodValues(0, 0.0F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.popcornItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.raisinsItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.ricecakeItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.toastedcoconutItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.roastedpumpkinseedsItem, new FoodValues(1, 0.05F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.applejuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.melonjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.carrotjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.strawberryjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapejuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.blueberryjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherryjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.papayajuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.starfruitjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.orangejuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.peachjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.limejuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.mangojuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.pomegranatejuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.blackberryjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.raspberryjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.kiwijuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cranberryjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cactusfruitjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.plumjuiceItem, new FoodValues(2, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pearjuiceItem, new FoodValues(2, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.apricotjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.figjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapefruitjuiceItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.persimmonjuiceItem, fruitJuiceFoodValues);

            FoodModifier.setModifiedFoodValues(ItemRegistry.pumpkinsoupItem, new FoodValues(4, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.melonsmoothieItem, smoothieFoodValues);

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
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolatestrawberryItem, new FoodValues(3, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.peanutbutterItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.trailmixItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pbandjItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.peanutbuttercookiesItem, new FoodValues(3, 0.15F));

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

            FoodModifier.setModifiedFoodValues(ItemRegistry.pancakesItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.blueberrypancakesItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherrypieItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolatecherryItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherrysmoothieItem, smoothieFoodValues);

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
            FoodModifier.setModifiedFoodValues(ItemRegistry.papayasmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.papayayogurtItem, yogurtFoodValues);
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
            FoodModifier.setModifiedFoodValues(ItemRegistry.orangechickenItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.orangesmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.orangeyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.peachcobblerItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.peachsmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.peachyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.keylimepieItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.limesmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.limeyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.mangosmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.mangoyogurtItem, yogurtFoodValues);
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

            FoodModifier.setModifiedFoodValues(ItemRegistry.footlongItem, new FoodValues(9, 0.55F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.blueberryyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.lemonyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherryyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.strawberryyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapeyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolateyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.blackberrycobblerItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.blackberrysmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.blackberryyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolatemilkItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pumpkinyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.raspberrypieItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.raspberrysmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.raspberryyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cinnamonsugardonutItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.melonyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.kiwismoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.kiwiyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.plainyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.appleyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.saltedsunflowerseedsItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sunflowerwheatrollsItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sunflowerbroccolisaladItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cranberrysauceItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cranberrybarItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.peppermintItem, new FoodValues(2, 0.05F));

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
            FoodModifier.setModifiedFoodValues(ItemRegistry.gingerchickenItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.oldworldveggiesoupItem, new FoodValues(4, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.spicebunItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.gingeredrhubarbtartItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.lambbarleysoupItem, new FoodValues(5, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.honeylemonlambItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pumpkinoatsconesItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.beefjerkyItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.ovenroastedcauliflowerItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.leekbaconsoupItem, new FoodValues(6, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.herbbutterparsnipsItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.scallionbakedpotatoItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.soymilkItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.firmtofuItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.silkentofuItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.bamboosteamedriceItem, new FoodValues(4, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.roastedchestnutItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sweetpotatosouffleItem, new FoodValues(5, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cashewchickenItem, new FoodValues(6, 0.25F));
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
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapefruitjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapefruitjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapefruitsmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapefruityogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapefruitsodaItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.citrussaladItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pecanpieItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pralinesItem, new FoodValues(2, 0.1F));
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
            FoodModifier.setModifiedFoodValues(ItemRegistry.chickengumboItem, new FoodValues(10, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.generaltsochickenItem, new FoodValues(6, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.californiarollItem, new FoodValues(4, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.futomakiItem, new FoodValues(7, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.beansontoastItem, new FoodValues(4, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.vegemiteItem, new FoodValues(7, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.honeycombchocolatebarItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherrycoconutchocolatebarItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.fairybreadItem, new FoodValues(2, 0.05F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.timtamItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.meatpieItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chikorollItem, new FoodValues(9, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.damperItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.beetburgerItem, new FoodValues(16, 0.8F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.gherkinItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.mcpamItem, new FoodValues(10, 0.6F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.ceasarsaladItem, new FoodValues(5, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chaoscookieItem, new FoodValues(3, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolatebaconItem, new FoodValues(7, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.lambkebabItem, new FoodValues(9, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.nutellaItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.snickersbarItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.spinachpieItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.steamedspinachItem, new FoodValues(2, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.vegemiteontoastItem, new FoodValues(3, 0.15F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.anchovyrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.bassrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.carprawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.catfishrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.charrrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.clamrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.crabrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.crayfishrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.eelrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.frograwItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grouperrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.herringrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.jellyfishrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.mudfishrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.octopusrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.perchrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.scalloprawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.shrimprawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.snailrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.snapperrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.tilapiarawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.troutrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.tunarawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.turtlerawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.walleyerawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.greenheartfishItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.sardinerawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.musselrawItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.rawtofishItem, fishFoodValues);

            FoodModifier.setModifiedFoodValues(ItemRegistry.clamcookedItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.crabcookedItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.crayfishcookedItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.frogcookedItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.octopuscookedItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.scallopcookedItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.shrimpcookedItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.snailcookedItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.turtlecookedItem, fishFoodValues);

            FoodModifier.setModifiedFoodValues(ItemRegistry.appleciderItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.bangersandmashItem, new FoodValues(9, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.batteredsausageItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chorizoItem, new FoodValues(8, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.coleslawItem, new FoodValues(4, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.energydrinkItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.friedonionsItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.meatfeastpizzaItem, new FoodValues(16, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.mincepieItem, new FoodValues(6, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.onionhamburgerItem, new FoodValues(9, 0.6F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pepperoniItem, new FoodValues(8, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pickledonionsItem, new FoodValues(4, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.porksausageItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.raspberrytrifleItem, new FoodValues(6, 0.35F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.pumpkinmuffinItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.suaderoItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.randomtacoItem, new FoodValues(13, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.turkeyrawItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.turkeycookedItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.venisonrawItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.venisoncookedItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.strawberrymilkshakeItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolatemilkshakeItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.bananamilkshakeItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cornflakesItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.coleslawburgerItem, new FoodValues(10, 0.6F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.roastchickenItem, new FoodValues(7, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.roastpotatoesItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sundayroastItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.bbqpulledporkItem, new FoodValues(9, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.lambwithmintsauceItem, new FoodValues(7, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.steakandchipsItem, new FoodValues(9, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherryicecreamItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pistachioicecreamItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.neapolitanicecreamItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.spumoniicecreamItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.almondbutterItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cashewbutterItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chestnutbutterItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cornishpastyItem, new FoodValues(6, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cottagepieItem, new FoodValues(6, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.croissantItem, new FoodValues(4, 0.25F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.dimsumItem, new FoodValues(6, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.friedpecanokraItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.gooseberryjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.gooseberryjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.gooseberrymilkshakeItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.gooseberrypieItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.gooseberrysmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.gooseberryyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.hamsweetpicklesandwichItem, new FoodValues(5, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.hushpuppiesItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.kimchiItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.mochiItem, new FoodValues(3, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.museliItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.naanItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.okrachipsItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.okracreoleItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pistachiobutterItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.ploughmanslunchItem, new FoodValues(9, 0.55F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.porklomeinItem, new FoodValues(9, 0.55F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.salmonpattiesItem, new FoodValues(6, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sausageItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sausagerollItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sesameballItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sesamesnapsItem, new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.shrimpokrahushpuppiesItem, new FoodValues(6, 0.3F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.sweetpickleItem, new FoodValues(3, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.veggiestripsItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.vindalooItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.applesmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.coconutsmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cranberrysmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cranberryyogurtItem, yogurtFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.grapesmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.pearsmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.pearjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.pearjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.plumsmoothieItem, smoothieFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.plumjellyItem, jellyFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.plumjellysandwichItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.honeysandwichItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cheeseontoastItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.tunapotatoItem, new FoodValues(7, 0.6F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolaterollItem, new FoodValues(5, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.jamrollItem, new FoodValues(4, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.coconutcreamItem, fruitJuiceFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.crackerItem, new FoodValues(3, 0.2F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.paneerItem, new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.deluxechickencurryItem, new FoodValues(16, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.gravyItem, new FoodValues(3, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.mangochutneyItem, new FoodValues(3, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.marzipanItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.paneertikkamasalaItem, new FoodValues(4, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.peaandhamsoupItem, new FoodValues(6, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.potatoandleeksoupItem, new FoodValues(3, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.yorkshirepuddingItem, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.toadintheholeItem, new FoodValues(6, 0.35F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.hotandsoursoupItem, new FoodValues(4, 0.15F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.chickenchowmeinItem, new FoodValues(8, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.kungpaochickenItem, new FoodValues(9, 0.5F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.charsiuItem, new FoodValues(9, 0.5F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.sweetandsourchickenItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.baconandeggsItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.biscuitsandgravyItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.applefritterItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sweetteaItem, new FoodValues(1, 0.0F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.creepercookieItem, new FoodValues(3, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.patreonpieItem, new FoodValues(5, 0.25F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.honeybreadItem, new FoodValues(4, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.honeybunItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.honeyglazedcarrotsItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.honeyglazedhamItem, new FoodValues(6, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.honeysoyribsItem, new FoodValues(9, 0.55F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.anchovypepperonipizzaItem, new FoodValues(9, 0.6F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocovoxelsItem, new FoodValues(3, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cinnamontoastItem, jellySandwichFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cornedbeefhashItem, new FoodValues(16, 0.8F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cornedbeefItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cottoncandyItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.crackersItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.creeperwingsItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.dhalItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.durianmilkshakeItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.durianmuffinItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.homestylelunchItem, new FoodValues(9, 0.6F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.hummusItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.ironbrewItem, new FoodValues(7, 0.45F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.lasagnaItem, new FoodValues(7, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.lemondrizzlecakeItem, new FoodValues(6, 0.4F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.meatloafItem, new FoodValues(9, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.montecristosandwichItem, new FoodValues(8, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.mushroomlasagnaItem, new FoodValues(8, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.musselcookedItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.netherwingsItem, new FoodValues(7, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pizzasoupItem, new FoodValues(7, 0.1F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.poutineItem, new FoodValues(4, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.salsaItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.sardinesinhotsauceItem, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.teriyakichickenItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.toastedwesternItem, new FoodValues(9, 0.5F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.turkishdelightItem, new FoodValues(2, 0.1F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.rawtofeakItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.rawtofaconItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.rawtofeegItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.rawtofuttonItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.rawtofickenItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.rawtofabbitItem, new FoodValues(2, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.rawtofurkeyItem, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.rawtofenisonItem, new FoodValues(2, 0.05F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.cookedtofeakItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cookedtofaconItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cookedtofishItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cookedtofeegItem, fishFoodValues);
            FoodModifier.setModifiedFoodValues(ItemRegistry.cookedtofuttonItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cookedtofickenItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cookedtofabbitItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cookedtofurkeyItem, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cookedtofenisonItem, new FoodValues(6, 0.35F));

            FoodModifier.setModifiedFoodValues(ItemRegistry.carrotcakeItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.holidaycakeItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pumpkincheesecakeItem, new FoodValues(7, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pavlovaItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.lamingtonItem, new FoodValues(4, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cheesecakeItem, new FoodValues(5, 0.25F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.cherrycheesecakeItem, new FoodValues(7, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.pineappleupsidedowncakeItem, new FoodValues(6, 0.3F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.chocolatesprinklecakeItem, new FoodValues(6, 0.35F));
            FoodModifier.setModifiedFoodValues(ItemRegistry.redvelvetcakeItem, new FoodValues(7, 0.35F));
        }

        // Sapling Growth
        PlantGrowthModification genericSaplingGrowthModification = new PlantGrowthModification().setGrowthTickProbability(Config.saplingRegrowthMultiplier);
        PlantGrowthModule.registerPlantGrowthModifier(BlockPamSapling.class, genericSaplingGrowthModification);

        PlantGrowthModification temperateSaplingGrowthModification = new PlantGrowthModification().setGrowthTickProbability(Config.saplingRegrowthMultiplier).setBiomeGrowthModifier(Type.FOREST, 1).setBiomeGrowthModifier(Type.PLAINS, 1);
        for (Block temperateSapling : FruitRegistry.temperateSaplings.values())
        {
            PlantGrowthModule.registerPlantGrowthModifier(temperateSapling, temperateSaplingGrowthModification);
        }
        PlantGrowthModule.registerPlantGrowthModifier(FruitRegistry.getSapling(FruitRegistry.MAPLE), temperateSaplingGrowthModification);

        PlantGrowthModification warmSaplingGrowthModification = new PlantGrowthModification().setGrowthTickProbability(Config.saplingRegrowthMultiplier).setBiomeGrowthModifier(Type.JUNGLE, 1).setBiomeGrowthModifier(Type.SWAMP, 1);
        for (Block warmSapling : FruitRegistry.warmSaplings.values())
        {
            PlantGrowthModule.registerPlantGrowthModifier(warmSapling, warmSaplingGrowthModification);
        }
        PlantGrowthModule.registerPlantGrowthModifier(FruitRegistry.getSapling(FruitRegistry.CINNAMON), warmSaplingGrowthModification);

        // Fruit Growth
        PlantGrowthModification genericFruitGrowthModification = new PlantGrowthModification().setNeedsSunlight(false).setGrowthTickProbability(Config.treeCropRegrowthMultiplier);
        PlantGrowthModule.registerPlantGrowthModifier(BlockPamFruit.class, genericFruitGrowthModification);

        PlantGrowthModification temperateFruitGrowthModification = new PlantGrowthModification().setNeedsSunlight(false).setGrowthTickProbability(Config.treeCropRegrowthMultiplier).setBiomeGrowthModifier(Type.FOREST, 1).setBiomeGrowthModifier(Type.PLAINS, 1);
        for (Block temperateSapling : FruitRegistry.temperateSaplings.values())
        {
            Block fruitBlock = PamsModsHelper.saplingToFruitBlockMap.get(temperateSapling);
            PlantGrowthModule.registerPlantGrowthModifier(fruitBlock, temperateFruitGrowthModification);
        }
        PlantGrowthModule.registerPlantGrowthModifier(FruitRegistry.getLog(FruitRegistry.MAPLE), temperateSaplingGrowthModification);

        PlantGrowthModification warmFruitGrowthModification = new PlantGrowthModification().setNeedsSunlight(false).setGrowthTickProbability(Config.treeCropRegrowthMultiplier).setBiomeGrowthModifier(Type.JUNGLE, 1).setBiomeGrowthModifier(Type.SWAMP, 1);
        for (Block warmSapling : FruitRegistry.warmSaplings.values())
        {
            Block fruitBlock = PamsModsHelper.saplingToFruitBlockMap.get(warmSapling);
            PlantGrowthModule.registerPlantGrowthModifier(fruitBlock, warmFruitGrowthModification);
        }
        PlantGrowthModule.registerPlantGrowthModifier(FruitRegistry.getLog(FruitRegistry.CINNAMON), warmFruitGrowthModification);

        PlantGrowthModification humidCropGrowthModification = new PlantGrowthModification().setNeedsSunlight(true).setGrowthTickProbability(Config.cropRegrowthMultiplier).setBiomeGrowthModifier(Type.JUNGLE, 1).setBiomeGrowthModifier(Type.SWAMP, 1);
        PlantGrowthModule.registerPlantGrowthModifier(CropRegistry.getCrop(CropRegistry.PINEAPPLE), humidCropGrowthModification);
        PlantGrowthModule.registerPlantGrowthModifier(CropRegistry.getCrop(CropRegistry.SPICELEAF), humidCropGrowthModification);
        PlantGrowthModule.registerPlantGrowthModifier(CropRegistry.getCrop(CropRegistry.CANDLEBERRY), humidCropGrowthModification);
        PlantGrowthModule.registerPlantGrowthModifier(CropRegistry.getCrop(CropRegistry.GRAPE), humidCropGrowthModification);
        PlantGrowthModule.registerPlantGrowthModifier(CropRegistry.getCrop(CropRegistry.KIWI), humidCropGrowthModification);

        PlantGrowthModification desertCropGrowthModification = new PlantGrowthModification().setNeedsSunlight(true).setGrowthTickProbability(Config.cropRegrowthMultiplier).setBiomeGrowthModifier(Type.SANDY, 1);
        PlantGrowthModule.registerPlantGrowthModifier(CropRegistry.getCrop(CropRegistry.CACTUSFRUIT), desertCropGrowthModification);

        /*
         * Bonemeal
         */
        BonemealModification cropBonemealModification = new BonemealModification()
        {
            @Override
            public IBlockState getNewState(World world, BlockPos pos, IBlockState currentState)
            {
                int currentMeta = currentState.getValue(BlockPamCrop.CROP_AGE);
                int metaFullyGrown = 3;
                int metaIncrease = 0;

                if (currentMeta != metaFullyGrown)
                {
                    metaIncrease = 1;

                    if (Config.difficultyScalingBoneMeal && world.getDifficulty().ordinal() < EnumDifficulty.NORMAL.ordinal())
                    {
                        int metaRandomIncreaseRange = currentMeta < 3 ? 2 : 3;
                        metaIncrease += random.nextInt(metaRandomIncreaseRange);
                    }
                }

                return currentState.withProperty(BlockPamCrop.CROP_AGE, Math.min(currentMeta + metaIncrease, metaFullyGrown));
            }
        };
        BonemealModule.registerBonemealModifier(BlockPamCrop.class, cropBonemealModification);

        BonemealModification fruitBonemealModification = new BonemealModification()
        {
            @Override
            public IBlockState getNewState(World world, BlockPos pos, IBlockState currentState)
            {
                int currentMeta = currentState.getValue(BlockPamFruit.AGE);
                int metaFullyGrown = 2;
                int metaIncrease = 0;

                if (currentMeta != metaFullyGrown)
                {
                    metaIncrease = 1;

                    if (Config.difficultyScalingBoneMeal && world.getDifficulty().ordinal() < EnumDifficulty.NORMAL.ordinal())
                    {
                        int metaRandomIncreaseRange = currentMeta < 2 ? 1 : 2;
                        metaIncrease += random.nextInt(metaRandomIncreaseRange);
                    }
                }

                return currentState.withProperty(BlockPamFruit.AGE, Math.min(currentMeta + metaIncrease, metaFullyGrown));
            }
        };
        BonemealModule.registerBonemealModifier(BlockPamFruit.class, fruitBonemealModification);

        BonemealModification fruitLogBonemealModification = new BonemealModification()
        {
            @Override
            public IBlockState getNewState(World world, BlockPos pos, IBlockState currentState)
            {
                int currentMeta = currentState.getValue(BlockPamFruitLog.AGE);
                int metaFullyGrown = 2;
                int metaIncrease = 0;

                if (currentMeta != metaFullyGrown)
                {
                    metaIncrease = 1;

                    if (Config.difficultyScalingBoneMeal && world.getDifficulty().ordinal() < EnumDifficulty.NORMAL.ordinal())
                    {
                        int metaRandomIncreaseRange = currentMeta < 2 ? 1 : 2;
                        metaIncrease += random.nextInt(metaRandomIncreaseRange);
                    }
                }

                return currentState.withProperty(BlockPamFruitLog.AGE, Math.min(currentMeta + metaIncrease, metaFullyGrown));
            }
        };
        BonemealModule.registerBonemealModifier(BlockPamFruitLog.class, fruitLogBonemealModification);
    }
}
