package iguanaman.hungeroverhaul.module;

import com.progwml6.natura.nether.block.bush.BlockNetherBerryBush;
import com.progwml6.natura.overworld.block.bush.BlockOverworldBerryBush;
import com.progwml6.natura.shared.NaturaCommons;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import iguanaman.hungeroverhaul.food.FoodModifier;
import iguanaman.hungeroverhaul.util.BonemealModification;
import iguanaman.hungeroverhaul.util.PlantGrowthModification;
import iguanaman.hungeroverhaul.util.RecipeRemover;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import squeek.applecore.api.food.FoodValues;

//HACKY
//TODO: FIX HACKYNESS
public class ModuleNatura
{
    public static void init()
    {
        ItemStack barley = NaturaCommons.barley;
        //ItemStack barleySeeds = new ItemStack(NContent.seeds, 1, 0);
        ItemStack barleyFlour = NaturaCommons.barleyFlour;
        ItemStack wheatFlour = NaturaCommons.wheatFlour;

        //if (IguanaConfig.addSeedsCraftingRecipe)
        //{
        //    GameRegistry.addRecipe(new ShapelessOreRecipe(barleySeeds, barley));
        //}

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
            GameRegistry.addRecipe(new ShapelessOreRecipe(wheatFlour, Items.WHEAT, Items.WHEAT));
        }

        /*
         * Food values
         */
        if (IguanaConfig.modifyFoodValues && IguanaConfig.useHOFoodValues)
        {
            //for (int i = 0; i < 4; i++)
            //    FoodModifier.setModifiedFoodValues(new ItemStack(NaturaCommons.berryItem, 1, i), new FoodValues(1, 0.1F));
            //for (int i = 0; i < 4; i++)
            //    FoodModifier.setModifiedFoodValues(new ItemStack(NaturaCommons.netherBerryItem, 1, i), new FoodValues(1, 0.1F));
            FoodModifier.setModifiedFoodValues(NaturaCommons.berryMedley, new FoodValues(3, 0.15F));
        }

        /*
         * Plant growth
         */
        // TODO: FIX
        /*PlantGrowthModification cropGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(true)
                .setGrowthTickProbability(IguanaConfig.cropRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.FOREST, 1)
                .setBiomeGrowthModifier(Type.PLAINS, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(CropBlock.class, cropGrowthModification);
        
        PlantGrowthModification saguaroGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(false)
                .setGrowthTickProbability(IguanaConfig.cactusRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.SANDY, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(SaguaroBlock.class, saguaroGrowthModification);*/

        PlantGrowthModification berryBushGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(true)
                .setGrowthTickProbability(IguanaConfig.cropRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.FOREST, 1)
                .setBiomeGrowthModifier(Type.PLAINS, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(BlockOverworldBerryBush.class, berryBushGrowthModification);

        PlantGrowthModification netherBushGrowthModification = new PlantGrowthModification()
                .setNeedsSunlight(false)
                .setGrowthTickProbability(IguanaConfig.cropRegrowthMultiplier)
                .setBiomeGrowthModifier(Type.NETHER, 1);
        ModulePlantGrowth.registerPlantGrowthModifier(BlockNetherBerryBush.class, netherBushGrowthModification);

        /*
         * Bonemeal
         */
        //TODO: FIX
        /*BonemealModification naturaCropBonemealModification = new BonemealModification()
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
        ModuleBonemeal.registerBonemealModifier(CropBlock.class, naturaCropBonemealModification);*/

        BonemealModification naturaBushBonemealModification = new BonemealModification()
        {
            @SuppressWarnings("deprecation")
            @Override
            public IBlockState getNewState(World world, BlockPos pos, IBlockState state)
            {
                Block block = state.getBlock();

                int resultingMeta = state.getBlock().getMetaFromState(state);
                if (block.getMetaFromState(state) / 4 < 2)
                {
                    if (!(block instanceof BlockOverworldBerryBush) || world.rand.nextBoolean())
                    {
                        int setMeta = world.rand.nextInt(2) + 1 + block.getMetaFromState(state) / 4;
                        if (setMeta > 2)
                            setMeta = 2;
                        if (IguanaConfig.difficultyScalingBoneMeal && world.getDifficulty().ordinal() >= EnumDifficulty.NORMAL.ordinal())
                            setMeta = 1;
                        resultingMeta = block.getMetaFromState(state) % 4 + setMeta * 4;
                    }
                }
                return block.getStateFromMeta(resultingMeta);
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onBonemeal(World world, BlockPos pos, IBlockState state, IBlockState newState)
            {
                Block block = state.getBlock();
                Block newBlock = newState.getBlock();

                BlockPos posUp = pos.up();
                IBlockState stateAbove = world.getBlockState(posUp);

                if (stateAbove == null || stateAbove.getBlock().isAir(stateAbove, world, posUp))
                {
                    int randomRange = state.getBlock() instanceof BlockNetherBerryBush ? 6 : 3;
                    if (world.rand.nextInt(randomRange) == 0)
                        world.setBlockState(posUp, block.getStateFromMeta(newBlock.getMetaFromState(newState) % 4), 3);
                }
            }
        };
        ModuleBonemeal.registerBonemealModifier(BlockOverworldBerryBush.class, naturaBushBonemealModification);
        ModuleBonemeal.registerBonemealModifier(BlockNetherBerryBush.class, naturaBushBonemealModification);
    }

}
