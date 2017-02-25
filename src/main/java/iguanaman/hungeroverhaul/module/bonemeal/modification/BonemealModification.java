package iguanaman.hungeroverhaul.module.bonemeal.modification;

import iguanaman.hungeroverhaul.common.config.Config;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Note: This is only used when {@link Config#difficultyScalingBoneMeal} is set to {@code true}
 */
public abstract class BonemealModification
{
    public void onBonemeal(World world, BlockPos pos, IBlockState state, IBlockState resultingState)
    {
    }

    public abstract IBlockState getNewState(World world, BlockPos pos, IBlockState currentState);
}
