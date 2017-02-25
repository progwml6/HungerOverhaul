package iguanaman.hungeroverhaul.module.vanilla;

import iguanaman.hungeroverhaul.common.config.Config;
import iguanaman.hungeroverhaul.module.bonemeal.BonemealModule;
import iguanaman.hungeroverhaul.module.bonemeal.modification.BonemealModification;
import iguanaman.hungeroverhaul.module.food.FoodModifier;
import iguanaman.hungeroverhaul.module.growth.PlantGrowthModule;
import iguanaman.hungeroverhaul.module.growth.modification.PlantGrowthModification;
import net.minecraft.block.BlockBeetroot;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockCarrot;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockPotato;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockStem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import squeek.applecore.api.food.FoodValues;

public class VanillaModule
{
    public static void init()
    {
        if (Config.addSeedsCraftingRecipe)
        {
            GameRegistry.addRecipe(new ShapelessOreRecipe(Items.WHEAT_SEEDS, Items.WHEAT));
        }

        /*
         * Food values
         */
        if (Config.modifyFoodValues && Config.useHOFoodValues)
        {
            FoodModifier.setModifiedFoodValues(Items.APPLE, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(Items.BREAD, new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(Items.PORKCHOP, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(Items.COOKED_PORKCHOP, new FoodValues(2, 0.15F));
            FoodModifier.setModifiedFoodValues(Items.FISH, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(Items.COOKED_FISH, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(Items.COOKIE, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(Items.MELON, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(Items.BEEF, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(Items.COOKED_BEEF, new FoodValues(2, 0.15F));
            FoodModifier.setModifiedFoodValues(Items.CHICKEN, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(Items.COOKED_CHICKEN, new FoodValues(2, 0.15F));
            FoodModifier.setModifiedFoodValues(Items.ROTTEN_FLESH, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(Items.BAKED_POTATO, new FoodValues(2, 0.15F));
            FoodModifier.setModifiedFoodValues(Items.POISONOUS_POTATO, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(Items.PUMPKIN_PIE, new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(Items.MUSHROOM_STEW, new FoodValues(2, 0.1F));
            FoodModifier.setModifiedFoodValues(Items.CARROT, new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(Items.POTATO, new FoodValues(1, 0.05F));
        }

        /*
         * Plant growth
         */
        PlantGrowthModification cropGrowthModification = new PlantGrowthModification().setNeedsSunlight(true).setGrowthTickProbability(Config.cropRegrowthMultiplier).setBiomeGrowthModifier(Type.FOREST, 1).setBiomeGrowthModifier(Type.PLAINS, 1);
        PlantGrowthModule.registerPlantGrowthModifier(BlockCrops.class, cropGrowthModification);
        PlantGrowthModule.registerPlantGrowthModifier(BlockCarrot.class, cropGrowthModification);
        PlantGrowthModule.registerPlantGrowthModifier(BlockPotato.class, cropGrowthModification);
        PlantGrowthModule.registerPlantGrowthModifier(BlockBeetroot.class, cropGrowthModification);

        PlantGrowthModification reedGrowthModification = new PlantGrowthModification().setNeedsSunlight(true).setGrowthTickProbability(Config.sugarcaneRegrowthMultiplier).setBiomeGrowthModifier(Type.JUNGLE, 1).setBiomeGrowthModifier(Type.SWAMP, 1).setWrongBiomeMultiplier(Config.wrongBiomeRegrowthMultiplierSugarcane);
        PlantGrowthModule.registerPlantGrowthModifier(BlockReed.class, reedGrowthModification);

        PlantGrowthModification stemGrowthModification = new PlantGrowthModification().setNeedsSunlight(true).setGrowthTickProbability(Config.cropRegrowthMultiplier).setBiomeGrowthModifier(Type.JUNGLE, 1).setBiomeGrowthModifier(Type.SWAMP, 1);
        PlantGrowthModule.registerPlantGrowthModifier(BlockStem.class, stemGrowthModification);

        PlantGrowthModification cocoaGrowthModification = new PlantGrowthModification().setNeedsSunlight(false).setGrowthTickProbability(Config.cocoaRegrowthMultiplier).setBiomeGrowthModifier(Type.JUNGLE, 1);
        PlantGrowthModule.registerPlantGrowthModifier(BlockCocoa.class, cocoaGrowthModification);

        PlantGrowthModification cactusGrowthModification = new PlantGrowthModification().setNeedsSunlight(false).setGrowthTickProbability(Config.cactusRegrowthMultiplier).setBiomeGrowthModifier(Type.SANDY, 1);
        PlantGrowthModule.registerPlantGrowthModifier(BlockCactus.class, cactusGrowthModification);

        PlantGrowthModification saplingGrowthModification = new PlantGrowthModification().setGrowthTickProbability(Config.saplingRegrowthMultiplier);
        PlantGrowthModule.registerPlantGrowthModifier(BlockSapling.class, saplingGrowthModification);

        PlantGrowthModification netherWartGrowthModification = new PlantGrowthModification().setNeedsSunlight(false).setGrowthTickProbability(Config.netherWartRegrowthMultiplier).setBiomeGrowthModifier(Type.NETHER, 1);
        PlantGrowthModule.registerPlantGrowthModifier(BlockNetherWart.class, netherWartGrowthModification);

        /*
         * Bonemeal
         */
        BonemealModification cropBonemealModification = new BonemealModification()
        {
            @Override
            public IBlockState getNewState(World world, BlockPos pos, IBlockState currentState)
            {
                if (currentState.getBlock() instanceof BlockCrops)
                {
                    int currentMeta = currentState.getValue(BlockCrops.AGE);
                    int metaIncrease = 1;

                    if (Config.difficultyScalingBoneMeal && world.getDifficulty().getDifficultyId() < EnumDifficulty.EASY.getDifficultyId())
                    {
                        metaIncrease = world.rand.nextInt(3);
                    }

                    return currentState.withProperty(BlockCrops.AGE, Math.min(currentMeta + metaIncrease, 7));
                }
                else
                {
                    return currentState;
                }
            }
        };
        BonemealModule.registerBonemealModifier(BlockCrops.class, cropBonemealModification);
        BonemealModule.registerBonemealModifier(BlockCarrot.class, cropBonemealModification);
        BonemealModule.registerBonemealModifier(BlockPotato.class, cropBonemealModification);

        BonemealModification beetrootBonemealModification = new BonemealModification()
        {
            @Override
            public IBlockState getNewState(World world, BlockPos pos, IBlockState currentState)
            {
                if (currentState.getBlock() instanceof BlockBeetroot)
                {
                    int currentMeta = currentState.getValue(BlockBeetroot.BEETROOT_AGE);
                    int metaIncrease = 1;

                    if (Config.difficultyScalingBoneMeal && world.getDifficulty().getDifficultyId() < EnumDifficulty.EASY.getDifficultyId())
                    {
                        metaIncrease = world.rand.nextInt(2);
                    }

                    return currentState.withProperty(BlockBeetroot.BEETROOT_AGE, Math.min(currentMeta + metaIncrease, 3));
                }
                else
                {
                    return currentState;
                }
            }
        };
        BonemealModule.registerBonemealModifier(BlockBeetroot.class, beetrootBonemealModification);
    }
}
