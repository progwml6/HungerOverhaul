package iguanaman.hungeroverhaul.module.natura;

import java.util.Random;

import com.progwml6.natura.common.block.BlockEnumBerryBush;
import com.progwml6.natura.nether.block.bush.BlockNetherBerryBush;
import com.progwml6.natura.overworld.NaturaOverworld;
import com.progwml6.natura.overworld.block.bush.BlockOverworldBerryBush;
import com.progwml6.natura.overworld.block.crops.BlockNaturaBarley;
import com.progwml6.natura.overworld.block.crops.BlockNaturaCotton;
import com.progwml6.natura.shared.NaturaCommons;

import iguanaman.hungeroverhaul.common.config.Config;
import iguanaman.hungeroverhaul.module.bonemeal.BonemealModule;
import iguanaman.hungeroverhaul.module.bonemeal.modification.BonemealModification;
import iguanaman.hungeroverhaul.module.food.FoodModifier;
import iguanaman.hungeroverhaul.module.growth.PlantGrowthModule;
import iguanaman.hungeroverhaul.module.growth.modification.PlantGrowthModification;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import slimeknights.mantle.util.RecipeRemover;
import squeek.applecore.api.food.FoodValues;

public class NaturaModule
{
    public static Random random = new Random();

    public static void init()
    {
        random.setSeed(2 ^ 16 + 2 ^ 8 + (4 * 3 * 271));

        ItemStack barley = NaturaCommons.barley.copy();
        ItemStack barleySeeds = NaturaOverworld.barley_seeds.copy();
        ItemStack barleyFlour = NaturaCommons.barleyFlour.copy();
        ItemStack wheatFlour = NaturaCommons.wheatFlour.copy();

        if (Config.addSeedsCraftingRecipe)
        {
            GameRegistry.addRecipe(new ShapelessOreRecipe(barleySeeds, barley));
        }

        // seed recipe conflicts with the default flour recipe, so remove it
        if (Config.removeNaturaFlourCraftingRecipes || Config.addSeedsCraftingRecipe)
        {
            RecipeRemover.removeAnyRecipe(barleyFlour);
            RecipeRemover.removeAnyRecipe(wheatFlour);
        }
        if (Config.removeNaturaFlourSmeltingRecipe)
        {
            RecipeRemover.removeFurnaceRecipe(barleyFlour);
            RecipeRemover.removeFurnaceRecipe(wheatFlour);
        }
        if (Config.addAlternateNaturaFlourCraftingRecipes)
        {
            GameRegistry.addRecipe(new ShapelessOreRecipe(barleyFlour, barley, barley));
            GameRegistry.addRecipe(new ShapelessOreRecipe(wheatFlour, Items.WHEAT, Items.WHEAT));
        }

        /*
         * Food values
         */
        if (Config.modifyFoodValues && Config.useHOFoodValues)
        {
            FoodModifier.setModifiedFoodValues(NaturaCommons.raspberry, new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(NaturaCommons.blueberry, new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(NaturaCommons.blackberry, new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(NaturaCommons.maloberry, new FoodValues(1, 0.1F));

            FoodModifier.setModifiedFoodValues(NaturaCommons.blightberry, new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(NaturaCommons.duskberry, new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(NaturaCommons.skyberry, new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(NaturaCommons.stingberry, new FoodValues(1, 0.1F));

            FoodModifier.setModifiedFoodValues(NaturaCommons.berryMedley, new FoodValues(3, 0.15F));
        }

        /*
         * Plant growth
         */
        PlantGrowthModification cottonGrowthModification = new PlantGrowthModification().setNeedsSunlight(true).setGrowthTickProbability(Config.cropRegrowthMultiplier).setBiomeGrowthModifier(Type.FOREST, 1).setBiomeGrowthModifier(Type.PLAINS, 1);
        PlantGrowthModule.registerPlantGrowthModifier(BlockNaturaCotton.class, cottonGrowthModification);

        PlantGrowthModification barleyGrowthModification = new PlantGrowthModification().setNeedsSunlight(true).setGrowthTickProbability(Config.cropRegrowthMultiplier).setBiomeGrowthModifier(Type.FOREST, 1).setBiomeGrowthModifier(Type.PLAINS, 1);
        PlantGrowthModule.registerPlantGrowthModifier(BlockNaturaBarley.class, barleyGrowthModification);

        /*
         * TODO: FIX
         * PlantGrowthModification saguaroGrowthModification = new PlantGrowthModification().setNeedsSunlight(false).setGrowthTickProbability(Config.cactusRegrowthMultiplier).setBiomeGrowthModifier(Type.SANDY, 1);
         * PlantGrowthModule.registerPlantGrowthModifier(SaguaroBlock.class, saguaroGrowthModification);
        */

        PlantGrowthModification berryBushGrowthModification = new PlantGrowthModification().setNeedsSunlight(true).setGrowthTickProbability(Config.cropRegrowthMultiplier).setBiomeGrowthModifier(Type.FOREST, 1).setBiomeGrowthModifier(Type.PLAINS, 1);
        PlantGrowthModule.registerPlantGrowthModifier(BlockOverworldBerryBush.class, berryBushGrowthModification);

        PlantGrowthModification netherBushGrowthModification = new PlantGrowthModification().setNeedsSunlight(false).setGrowthTickProbability(Config.cropRegrowthMultiplier).setBiomeGrowthModifier(Type.NETHER, 1);
        PlantGrowthModule.registerPlantGrowthModifier(BlockNetherBerryBush.class, netherBushGrowthModification);

        /*
         * Bonemeal
         */
        BonemealModification naturaCottonBonemealModification = new BonemealModification()
        {
            @Override
            public IBlockState getNewState(World world, BlockPos pos, IBlockState currentState)
            {
                int currentMeta = currentState.getValue(BlockNaturaCotton.AGE);
                int metaFullyGrown = 4;
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

                return currentState.withProperty(BlockNaturaCotton.AGE, Math.min(currentMeta + metaIncrease, metaFullyGrown));
            }
        };
        BonemealModule.registerBonemealModifier(BlockNaturaCotton.class, naturaCottonBonemealModification);

        BonemealModification naturaBarleyBonemealModification = new BonemealModification()
        {
            @Override
            public IBlockState getNewState(World world, BlockPos pos, IBlockState currentState)
            {
                int currentMeta = currentState.getValue(BlockNaturaBarley.AGE);
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

                return currentState.withProperty(BlockNaturaBarley.AGE, Math.min(currentMeta + metaIncrease, metaFullyGrown));
            }
        };
        BonemealModule.registerBonemealModifier(BlockNaturaBarley.class, naturaBarleyBonemealModification);

        BonemealModification naturaBushBonemealModification = new BonemealModification()
        {
            @Override
            public IBlockState getNewState(World world, BlockPos pos, IBlockState currentState)
            {
                int currentMeta = currentState.getValue(BlockEnumBerryBush.AGE);

                int resultingMeta = currentMeta;

                if (currentMeta / 4 < 2)
                {
                    if (!(currentState.getBlock() instanceof BlockNetherBerryBush) || world.rand.nextBoolean())
                    {
                        int setMeta = world.rand.nextInt(2) + 1 + currentMeta / 4;
                        if (setMeta > 2)
                            setMeta = 2;
                        if (Config.difficultyScalingBoneMeal && world.getDifficulty().ordinal() >= EnumDifficulty.NORMAL.ordinal())
                            setMeta = 1;
                        resultingMeta = currentMeta % 4 + setMeta * 4;
                    }
                }
                return currentState.withProperty(BlockEnumBerryBush.AGE, resultingMeta);
            }

            @Override
            public void onBonemeal(World world, BlockPos pos, IBlockState state, IBlockState resultingState)
            {
                BlockPos posUp = pos.up();
                IBlockState stateAbove = world.getBlockState(posUp);

                if (stateAbove == null || stateAbove.getBlock().isAir(stateAbove, world, posUp))
                {
                    int randomRange = state.getBlock() instanceof BlockNetherBerryBush ? 6 : 3;
                    if (world.rand.nextInt(randomRange) == 0)
                        world.setBlockState(posUp, state.withProperty(BlockEnumBerryBush.AGE, resultingState.getValue(BlockEnumBerryBush.AGE) % 4), 3);
                }
            }
        };
        BonemealModule.registerBonemealModifier(BlockOverworldBerryBush.class, naturaBushBonemealModification);
        BonemealModule.registerBonemealModifier(BlockNetherBerryBush.class, naturaBushBonemealModification);
    }

}
