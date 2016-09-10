package iguanaman.hungeroverhaul.module;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import iguanaman.hungeroverhaul.util.PlantGrowthModification;
import iguanaman.hungeroverhaul.util.RandomHelper;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import squeek.applecore.api.plants.PlantGrowthEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModulePlantGrowth
{
    private static HashMap<Class<? extends Block>, PlantGrowthModification> plantGrowthModificationsByBlockClass = new HashMap<Class<? extends Block>, PlantGrowthModification>();
    private static HashMap<Block, PlantGrowthModification> plantGrowthModificationsByBlock = new HashMap<Block, PlantGrowthModification>();

    public static void registerPlantGrowthModifier(Class<? extends Block> blockClass, PlantGrowthModification growthModification)
    {
        plantGrowthModificationsByBlockClass.put(blockClass, growthModification);
    }

    public static void registerPlantGrowthModifier(Block block, PlantGrowthModification growthModification)
    {
        plantGrowthModificationsByBlock.put(block, growthModification);
    }

    public static PlantGrowthModification getPlantGrowthModification(Block block)
    {
        if (block == null)
            return null;

        PlantGrowthModification growthModification = plantGrowthModificationsByBlock.get(block);

        if (growthModification != null)
            return growthModification;
        else
            return getPlantGrowthModification(block.getClass());
    }

    public static PlantGrowthModification getPlantGrowthModification(Class<? extends Block> blockClass)
    {
        // get exact matches first
        PlantGrowthModification growthModifier = plantGrowthModificationsByBlockClass.get(blockClass);
        // as a backup, check instanceof
        if (growthModifier == null)
        {
            for (Map.Entry<Class<? extends Block>, PlantGrowthModification> entry : plantGrowthModificationsByBlockClass.entrySet())
            {
                if (entry.getKey().isAssignableFrom(blockClass))
                    return entry.getValue();
            }
        }
        return growthModifier;
    }

    @SubscribeEvent
    public void allowGrowthTick(PlantGrowthEvent.AllowGrowthTick event)
    {
        PlantGrowthModification growthModification = getPlantGrowthModification(event.block.getClass());

        if (growthModification == null)
            return;

        // sunlight
        float sunlightModifier = !growthModification.needsSunlight || (growthModification.needsSunlight && event.world.isDaytime() && event.world.canBlockSeeSky(event.pos)) ? 1 : IguanaConfig.noSunlightRegrowthMultiplier;
        if (sunlightModifier == 0)
        {
            event.setResult(Result.DENY);
            return;
        }

        // biome
        float biomeModifier = growthModification.wrongBiomeMultiplier;
        if (!growthModification.biomeGrowthModifiers.isEmpty())
        {
            biomeModifier = IguanaConfig.wrongBiomeRegrowthMultiplier;
            Biome biome = event.world.getBiomeForCoordsBody(event.pos);//.getBiomeGenForCoords(event.x, event.z);
            for (BiomeDictionary.Type type : BiomeDictionary.getTypesForBiome(biome))
            {
                if (growthModification.biomeGrowthModifiers.containsKey(type))
                {
                    biomeModifier = growthModification.getBiomeGrowthModifier(type);
                    break;
                }
            }
        }
        if (biomeModifier == 0)
        {
            event.setResult(Result.DENY);
            return;
        }

        // random
        if (RandomHelper.nextFloat(event.random, growthModification.growthTickProbability * biomeModifier * sunlightModifier) >= 1)
        {
            event.setResult(Result.DENY);
            return;
        }

        // still go through with the default conditionals
        event.setResult(Result.DEFAULT);
    }
}
