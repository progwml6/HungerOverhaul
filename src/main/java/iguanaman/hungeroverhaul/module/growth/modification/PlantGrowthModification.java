package iguanaman.hungeroverhaul.module.growth.modification;

import java.util.HashMap;
import java.util.Map;

import iguanaman.hungeroverhaul.common.config.Config;
import net.minecraftforge.common.BiomeDictionary.Type;

public class PlantGrowthModification
{
    public boolean needsSunlight = true;

    public final HashMap<Type, Float> biomeGrowthModifiers = new HashMap<Type, Float>();

    public float growthTickProbability = 0;

    public float wrongBiomeMultiplier = Config.wrongBiomeRegrowthMultiplier;

    public PlantGrowthModification setNeedsSunlight(boolean needsSunlight)
    {
        this.needsSunlight = needsSunlight;
        return this;
    }

    public float getBiomeGrowthModifier(Type biomeType)
    {
        return biomeGrowthModifiers.get(biomeType);
    }

    public PlantGrowthModification setBiomeGrowthModifier(Type biomeType, float growthModifier)
    {
        this.biomeGrowthModifiers.put(biomeType, growthModifier);
        return this;
    }

    public PlantGrowthModification setBiomeGrowthModifiers(Map<Type, Float> biomeGrowthModifiers)
    {
        this.biomeGrowthModifiers.putAll(biomeGrowthModifiers);
        return this;
    }

    public PlantGrowthModification setGrowthTickProbability(float growthTickProbability)
    {
        this.growthTickProbability = growthTickProbability;
        return this;
    }

    public PlantGrowthModification setWrongBiomeMultiplier(float wrongBiomeMultiplier)
    {
        this.wrongBiomeMultiplier = wrongBiomeMultiplier;
        return this;
    }
}
