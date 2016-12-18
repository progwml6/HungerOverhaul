package iguanaman.hungeroverhaul.util;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Note: This is only used when {@link IguanaConfig#difficultyScalingBoneMeal} is set to {@code true}
 */
public abstract class BonemealModification
{
    public void onBonemeal(World world, BlockPos pos, IBlockState state, IBlockState newState)
    {
    }

    public abstract IBlockState getNewState(World world, BlockPos pos, IBlockState state);
}
