package iguanaman.hungeroverhaul.module;

import iguanaman.hungeroverhaul.config.IguanaConfig;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import slimeknights.tconstruct.armor.TinkerArmor;
import slimeknights.tconstruct.library.crafting.DryingRackRecipes;
import slimeknights.tconstruct.util.config.PHConstruct;

public class ModuleTConstruct
{
    public static void init()
    {
        if (!PHConstruct.enableHealthRegen)
        {
            PHConstruct.enableHealthRegen = true;
            IguanaConfig.healthRegenRatePercentage = 0;
        }
        if (PHConstruct.alphaRegen)
        {
            PHConstruct.alphaRegen = false;
            IguanaConfig.foodRegensHealth = true;
        }
        if (IguanaConfig.dryingRackTimeMultiplier != 1)
        {
            DryingRackRecipes.recipes.clear();

            int dryingTime = 20 * 60 * 20; //in minutes

            DryingRackRecipes.addDryingRecipe(Items.beef, dryingTime, new ItemStack(TinkerArmor.jerky, 1, 0));
            DryingRackRecipes.addDryingRecipe(Items.chicken, dryingTime, new ItemStack(TinkerArmor.jerky, 1, 1));
            DryingRackRecipes.addDryingRecipe(Items.porkchop, dryingTime, new ItemStack(TinkerArmor.jerky, 1, 2));
            DryingRackRecipes.addDryingRecipe(Items.fish, dryingTime, new ItemStack(TinkerArmor.jerky, 1, 4));
            DryingRackRecipes.addDryingRecipe(Items.rotten_flesh, dryingTime, new ItemStack(TinkerArmor.jerky, 1, 5));
        }
    }
}
