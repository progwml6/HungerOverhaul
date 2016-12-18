package iguanaman.hungeroverhaul.module;

import biomesoplenty.api.item.BOPItems;
import iguanaman.hungeroverhaul.config.IguanaConfig;
import iguanaman.hungeroverhaul.food.FoodModifier;
import net.minecraft.item.ItemStack;
import squeek.applecore.api.food.FoodValues;

public class ModuleBOP
{
    public static void init()
    {
        if (IguanaConfig.modifyFoodValues && IguanaConfig.useHOFoodValues)
        {
            FoodModifier.setModifiedFoodValues(new ItemStack(BOPItems.berries, 1), new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(new ItemStack(BOPItems.shroompowder, 1), new FoodValues(1, 0.05F));
            // FoodModifier.setModifiedFoodValues(new ItemStack(BOPCItems.food, 1, 2), new FoodValues(1, 0.05F)); TODO: cropWildcarrots is dead?
            FoodModifier.setModifiedFoodValues(new ItemStack(BOPItems.saladfruit, 1), new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(new ItemStack(BOPItems.saladveggie, 1), new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(new ItemStack(BOPItems.saladshroom, 1), new FoodValues(3, 0.15F));
            FoodModifier.setModifiedFoodValues(new ItemStack(BOPItems.persimmon, 1), new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(new ItemStack(BOPItems.filled_honeycomb, 1), new FoodValues(1, 0.05F));
            FoodModifier.setModifiedFoodValues(new ItemStack(BOPItems.ambrosia, 1), new FoodValues(3, 0.2F));
            FoodModifier.setModifiedFoodValues(new ItemStack(BOPItems.turnip, 1), new FoodValues(1, 0.05F));
        }
    }
}
