package iguanaman.hungeroverhaul.module.biomesoplenty;

import biomesoplenty.api.item.BOPItems;
import iguanaman.hungeroverhaul.common.config.Config;
import iguanaman.hungeroverhaul.module.food.FoodModifier;
import net.minecraft.item.ItemStack;
import squeek.applecore.api.food.FoodValues;

public class BiomesOPlentyModule
{
    public static void init()
    {
        if (Config.modifyFoodValues && Config.useHOFoodValues)
        {
            FoodModifier.setModifiedFoodValues(new ItemStack(BOPItems.berries, 1), new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(new ItemStack(BOPItems.shroompowder, 1), new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(new ItemStack(BOPItems.persimmon, 1), new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(new ItemStack(BOPItems.filled_honeycomb, 1), new FoodValues(1, 0.05F));
        }
    }
}
