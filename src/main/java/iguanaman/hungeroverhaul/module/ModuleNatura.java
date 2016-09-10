package iguanaman.hungeroverhaul.module;

import net.minecraftforge.fml.common.registry.GameRegistry;
import iguanaman.hungeroverhaul.config.IguanaConfig;
import iguanaman.hungeroverhaul.food.FoodModifier;
import iguanaman.hungeroverhaul.util.BonemealModification;
import iguanaman.hungeroverhaul.util.PlantGrowthModification;
import iguanaman.hungeroverhaul.util.RecipeRemover;
import com.progwml6.natura.Natura;
import com.progwml6.natura.blocks.crops.BerryBush;
import com.progwml6.natura.blocks.crops.CropBlock;
import com.progwml6.natura.blocks.crops.NetherBerryBush;
import com.progwml6.natura.blocks.trees.SaguaroBlock;
import com.progwml6.natura.common.NContent;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import squeek.applecore.api.food.FoodValues;

public class ModuleNatura
{
    public static void init()
    {
        ItemStack barley = new ItemStack(NContent.plantItem, 1, 0);
        ItemStack barleySeeds = new ItemStack(NContent.seeds, 1, 0);
        ItemStack barleyFlour = new ItemStack(NContent.plantItem, 1, 1);
        ItemStack wheatFlour = new ItemStack(NContent.plantItem, 1, 2);

        if (IguanaConfig.addSeedsCraftingRecipe)
        {
            GameRegistry.addRecipe(new ShapelessOreRecipe(barleySeeds, barley));
        }

        // seed recipe conflicts with the default flour recipe, so remove it
        if (IguanaConfig.removeNaturaFlourCraftingRecipes || IguanaConfig.addSeedsCraftingRecipe)
        {
            RecipeRemover.removeAnyRecipe(barleyFlour);
            RecipeRemover.removeAnyRecipe(wheatFlour);
        }
        if (IguanaConfig.removeNaturaFlourSmeltingRecipe)
        {
            RecipeRemover.removeFurnaceRecipe(barleyFlour);
            RecipeRemover.removeFurnaceRecipe(wheatFlour);
        }
        if (IguanaConfig.addAlternateNaturaFlourCraftingRecipes)
        {
            GameRegistry.addRecipe(new ShapelessOreRecipe(barleyFlour, barley, barley));
            GameRegistry.addRecipe(new ShapelessOreRecipe(wheatFlour, Items.wheat, Items.wheat));
        }

        /*
         * Food values
         */
        if (IguanaConfig.modifyFoodValues && IguanaConfig.useHOFoodValues)
        {
            for (int i = 0; i < 4; i++)
                FoodModifier.setModifiedFoodValues(new ItemStack(NContent.berryItem, 1, i), new FoodValues(1, 0.1F));
            for (int i = 0; i < 4; i++)
                FoodModifier.setModifiedFoodValues(new ItemStack(NContent.netherBerryItem, 1, i), new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(NContent.berryMedley, new FoodValues(3, 0.15F));
        }

        /*
         * Plant growth
         */
        PlantGrowthModification cropGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(true)
                .setGrowthTickProbability(IguanaConfig.cropRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.FOREST, 1)
                .setBiomeGrowthModifier(Type.PLAINS, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(CropBlock.class, cropGrowthModification);

        PlantGrowthModification saguaroGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(false)
                .setGrowthTickProbability(IguanaConfig.cactusRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.SANDY, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(SaguaroBlock.class, saguaroGrowthModification);

        PlantGrowthModification berryBushGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(true)
                .setGrowthTickProbability(IguanaConfig.cropRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.FOREST, 1)
                .setBiomeGrowthModifier(Type.PLAINS, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(BerryBush.class, berryBushGrowthModification);

        PlantGrowthModification netherBushGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(false)
                .setGrowthTickProbability(IguanaConfig.cropRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.NETHER, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(NetherBerryBush.class, netherBushGrowthModification);

        /*
         * Bonemeal
         */
        BonemealModification naturaCropBonemealModification = new BonemealModification()
        {
            @Override
            public int getNewMeta(World world, int x, int y, int z, Block block, int currentMeta)
            {
                int metaFullyGrown = currentMeta <= 3 ? 3 : 8;
                int metaIncrease = 0;
                if (currentMeta != metaFullyGrown)
                {
                    metaIncrease = 1;
                    if (IguanaConfig.difficultyScalingBoneMeal && world.difficultySetting.ordinal() < EnumDifficulty.NORMAL.ordinal())
                    {
                        int metaRandomIncreaseRange = currentMeta < 3 ? 2 : 3;
                        metaIncrease += Natura.random.nextInt(metaRandomIncreaseRange);
                    }
                }
                return Math.min(currentMeta + metaIncrease, metaFullyGrown);
            }
        };
        ModuleBonemeal.registerBonemealModifier(CropBlock.class, naturaCropBonemealModification);

        BonemealModification naturaBushBonemealModification = new BonemealModification()
        {
            @Override
            public int getNewMeta(World world, int x, int y, int z, Block block, int currentMeta)
            {
                int resultingMeta = currentMeta;
                if (currentMeta / 4 < 2)
                {
                    if (!(block instanceof NetherBerryBush) || world.rand.nextBoolean())
                    {
                        int setMeta = world.rand.nextInt(2) + 1 + currentMeta / 4;
                        if (setMeta > 2)
                            setMeta = 2;
                        if (IguanaConfig.difficultyScalingBoneMeal && world.difficultySetting.ordinal() >= EnumDifficulty.NORMAL.ordinal())
                            setMeta = 1;
                        resultingMeta = currentMeta % 4 + setMeta * 4;
                    }
                }
                return resultingMeta;
            }

            @Override
            public void onBonemeal(World world, int x, int y, int z, Block block, int meta)
            {
                Block blockAbove = world.getBlock(x, y + 1, z);
                if (blockAbove == null || blockAbove.isAir(world, x, y + 1, z))
                {
                    int randomRange = block instanceof NetherBerryBush ? 6 : 3;
                    if (world.rand.nextInt(randomRange) == 0)
                        world.setBlock(x, y + 1, z, block, meta % 4, 3);
                }
            }
        };
        ModuleBonemeal.registerBonemealModifier(BerryBush.class, naturaBushBonemealModification);
        ModuleBonemeal.registerBonemealModifier(NetherBerryBush.class, naturaBushBonemealModification);
    }

}
