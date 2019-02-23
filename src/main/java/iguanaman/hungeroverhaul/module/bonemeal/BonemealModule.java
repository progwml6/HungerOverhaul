package iguanaman.hungeroverhaul.module.bonemeal;

import java.util.HashMap;
import java.util.Map;

import iguanaman.hungeroverhaul.common.config.Config;
import iguanaman.hungeroverhaul.module.bonemeal.modification.BonemealModification;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BonemealModule
{
    private static HashMap<Class<? extends Block>, BonemealModification> bonemealModificationsByBlockClass = new HashMap<Class<? extends Block>, BonemealModification>();

    private static HashMap<Block, BonemealModification> bonemealModificationsByBlock = new HashMap<Block, BonemealModification>();

    public static void registerBonemealModifier(Class<? extends Block> blockClass, BonemealModification bonemealModification)
    {
        bonemealModificationsByBlockClass.put(blockClass, bonemealModification);
    }

    public static void registerBonemealModifier(Block block, BonemealModification bonemealModification)
    {
        bonemealModificationsByBlock.put(block, bonemealModification);
    }

    public static BonemealModification getBonemealModification(Block block)
    {
        BonemealModification bonemealModification = bonemealModificationsByBlock.get(block);
        if (bonemealModification != null)
        {
            return bonemealModification;
        }
        else
        {
            return getBonemealModification(block.getClass());
        }
    }

    public static BonemealModification getBonemealModification(Class<? extends Block> blockClass)
    {
        // get exact matches first
        BonemealModification bonemealModification = bonemealModificationsByBlockClass.get(blockClass);
        // as a backup, check instanceof
        if (bonemealModification == null)
        {
            for (Map.Entry<Class<? extends Block>, BonemealModification> entry : bonemealModificationsByBlockClass.entrySet())
            {
                if (entry.getKey().isInstance(blockClass))
                {
                    return entry.getValue();
                }
            }
        }
        return bonemealModification;
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBonemealUsed(BonemealEvent event)
    {
        // only the server matters
        // can't simulate this on the client because the client
        // generates different random numbers, causing visual desyncing
        if (event.getWorld().isRemote)
        {
            return;
        }

        // do nothing if effectiveness is normal
        if (event.getResult() != Result.DEFAULT || event.isCanceled() || Config.bonemealEffectiveness == 1.0f)
        {
            return;
        }

        BonemealModification bonemealModification = getBonemealModification(event.getBlock().getBlock());

        if (bonemealModification == null)
        {
            return;
        }

        // bonemeal doesn't do anything to the registered plants when
        // effectiveness is 0
        if (Config.bonemealEffectiveness == 0.0f)
        {
            event.setCanceled(true);
            event.setResult(Result.DENY);
            return;
        }

        if (event.getWorld().rand.nextFloat() < Config.bonemealEffectiveness)
        {
            if (Config.modifyBonemealGrowth)
            {
                IBlockState state = event.getWorld().getBlockState(event.getPos());
                IBlockState resultingState = bonemealModification.getNewState(event.getWorld(), event.getPos(), state);
                //If current and new states are not equal set the new state
                if (!state.equals(resultingState))
                {
                    event.getWorld().setBlockState(event.getPos(), resultingState, 3);

                    bonemealModification.onBonemeal(event.getWorld(), event.getPos(), event.getBlock(), resultingState);

                    event.setResult(Result.ALLOW);
                }
            }
            // otherwise fall through to default implementation (Result.DEFAULT)
        }
        else
        {
            // set as handled
            event.setResult(Result.ALLOW);
        }
    }
}
