package iguanaman.hungeroverhaul.module;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.GameRegistry;
import iguanaman.hungeroverhaul.config.IguanaConfig;
import iguanaman.hungeroverhaul.food.FoodModifier;
import iguanaman.hungeroverhaul.util.BonemealModification;
import iguanaman.hungeroverhaul.util.PlantGrowthModification;
import net.minecraft.block.*;
import net.minecraft.init.Items;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import squeek.applecore.api.food.FoodValues;

public class ModuleVanilla
{
    public static void init()
    {
        if (IguanaConfig.addSeedsCraftingRecipe)
            GameRegistry.addRecipe(new ShapelessOreRecipe(Items.WHEAT_SEEDS, Items.WHEAT));

        /*
         * Food values
         */
        if (IguanaConfig.modifyFoodValues && IguanaConfig.useHOFoodValues)
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
        PlantGrowthModification cropGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(true)
                .setGrowthTickProbability(IguanaConfig.cropRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.FOREST, 1)
                .setBiomeGrowthModifier(Type.PLAINS, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(BlockCrops.class, cropGrowthModification);

        PlantGrowthModification reedGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(true)
                .setGrowthTickProbability(IguanaConfig.sugarcaneRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.JUNGLE, 1)
                .setBiomeGrowthModifier(Type.SWAMP, 1)
                .setWrongBiomeMultiplier(IguanaConfig.wrongBiomeRegrowthMultiplierSugarcane);
        ModulePlantGrowth.registerPlantGrowthModifier(BlockReed.class, reedGrowthModification);

        PlantGrowthModification stemGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(true)
                .setGrowthTickProbability(IguanaConfig.cropRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.JUNGLE, 1)
                .setBiomeGrowthModifier(Type.SWAMP, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(BlockStem.class, stemGrowthModification);

        PlantGrowthModification cocoaGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(false)
                .setGrowthTickProbability(IguanaConfig.cocoaRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.JUNGLE, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(BlockCocoa.class, cocoaGrowthModification);

        PlantGrowthModification cactusGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(false)
                .setGrowthTickProbability(IguanaConfig.cactusRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.SANDY, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(BlockCactus.class, cactusGrowthModification);

        PlantGrowthModification saplingGrowthModification = new PlantGrowthModification()
                .setGrowthTickProbability(IguanaConfig.saplingRegrowthMultiplier);
        ModulePlantGrowth.registerPlantGrowthModifier(BlockSapling.class, saplingGrowthModification);

        PlantGrowthModification netherWartGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(false)
                .setGrowthTickProbability(IguanaConfig.netherWartRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.NETHER, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(BlockNetherWart.class, netherWartGrowthModification);

        /*
         * Bonemeal
         */
        BonemealModification cropBonemealModification = new BonemealModification()
        {
            @Override
            public int getNewMeta(World world, BlockPos pos, IBlockState block, int currentMeta)
            {
                int metaIncrease = 1;
                if (IguanaConfig.difficultyScalingBoneMeal && world.getDifficulty().getDifficultyId() < EnumDifficulty.EASY.getDifficultyId())
                    metaIncrease = world.rand.nextInt(3);
                return Math.min(currentMeta + metaIncrease, 7);
            }
        };
        ModuleBonemeal.registerBonemealModifier(BlockCrops.class, cropBonemealModification);
    }
}
